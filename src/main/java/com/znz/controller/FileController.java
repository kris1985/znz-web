package com.znz.controller;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.znz.config.AppConfig;
import com.znz.dao.*;
import com.znz.model.*;
import com.znz.service.CategoryService;
import com.znz.util.*;
import com.znz.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zeroturnaround.zip.ZipUtil;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by huangtao on 2015/1/23.
 */

@Slf4j
@Controller
@RequestMapping("/admin/file")
public class FileController {

    public static final String ZNZ_OSS_CN_SHENZHEN_INTERNAL_ALIYUNCS_COM = "znz.oss-cn-shenzhen-internal.aliyuncs.com/";
    @Resource
    private AppConfig appConfig;


    @Resource
    private UserMapper userMapper;

    @Resource
    private PictureMapper pictureMapper;


    @Resource
    private PictureCategoryMapper pictureCategoryMapper;

    @Resource
    private SubCategoryMapper subCategoryMapper;

    @Resource
    private OSSClient ossClient;

    @Resource
    private CategoryService categoryService;



    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    void processUpload(HttpServletRequest request, @RequestParam MultipartFile[] files, String category,String secondCategory) throws IOException {
        UserSession userSession = (UserSession) request.getSession().getAttribute(Constants.USER_SESSION);
        if (!PermissionUtil.checkPermisson(request)) {
            throw new RuntimeException("无权限操作");
        }
        List<PictureCategory> pictureCategories = new ArrayList<>();
        String[] categorys = category.split(",");
        try {
            Integer partionCode = categoryService.getPartionCodeBy2(Integer.parseInt(secondCategory));
            PartionCodeHoder.set(String.valueOf(partionCode));
            for (MultipartFile file : files) {
                String originalName = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String path = uuid + getSuffix(originalName);
                boolean b = upload(ossClient, file, path);
                if (b) {
                    Picture picture = new Picture();
                    picture.setName(originalName);
                    picture.setFilePath(path);
                    picture.setCreateTime(new Date());
                    picture.setCreateUser(userSession.getUser().getUserName());
                    picture.setClickTimes(0);
                    picture.setDownloadTimes(0);
                    picture.setGid(partionCode+"_"+uuid);
                    BigDecimal size = new BigDecimal(file.getSize());
                    BufferedImage sourceImg = ImageIO.read(file.getInputStream());
                    size = size.divide(new BigDecimal(1024),BigDecimal.ROUND_HALF_UP).setScale(0);
                    picture.setSize(String.valueOf(size));
                    picture.setWidth(String.valueOf(sourceImg.getWidth()));
                    picture.setHeight(String.valueOf(sourceImg.getHeight()));
                    pictureMapper.insert(picture);

                    for (String c : categorys) {
                        PictureCategory pictureCategory = new PictureCategory();
                        pictureCategory.setPictureId(picture.getId());
                        pictureCategory.setSubCategoryId(Integer.parseInt(c));
                        pictureCategories.add(pictureCategory);
                    }
                    pictureCategoryMapper.batchInsert(pictureCategories);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }finally {
            PartionCodeHoder.clear();
        }
    }


    @RequestMapping(value = "/uploadWatermark", method = RequestMethod.POST)
    public
    @ResponseBody
    ResultVO uploadWatermark(HttpServletRequest request, @RequestParam MultipartFile file, Model model) {
        ResultVO result = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            throw new RuntimeException("无权限操作");
        }
        try {
            String originalName = file.getOriginalFilename();
            String path = "watermark_"+originalName;
            boolean b = upload(ossClient, file, path);
            if (b) {
                result.setCode(0);
                result.setMsg(path);
            }
            return result;
        } catch (Exception e) {
            result.setCode(0);
            log.error(e.getMessage(), e);
            return result;
        }
    }

    public String getSuffix(String originalName) {
        return originalName.substring(originalName.indexOf("."), originalName.length());
    }

    public boolean upload(OSSClient ossClient, MultipartFile file, String originalName) {
        try {
            ossClient.putObject(new PutObjectRequest(appConfig.getBucketName(), originalName, file.getInputStream()));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

    }


    @RequestMapping(value = "/uploadChild", method = RequestMethod.POST)
    public
    @ResponseBody
    void uploadChild(HttpServletRequest request, @RequestParam MultipartFile file, Long pictureId,String secondCategory) throws IOException {
        if (!PermissionUtil.checkPermisson(request)) {
            throw new RuntimeException("无权限操作");
        }
        try {
            Picture picture = pictureMapper.selectByPrimaryKey(pictureId);
            Integer partionCode = categoryService.getPartionCodeBy2(Integer.parseInt(secondCategory));
            PartionCodeHoder.set(String.valueOf(partionCode));
            String fileName = file.getOriginalFilename();
            String childPath = UUID.randomUUID().toString() + getSuffix(file.getOriginalFilename());
            boolean b = upload(ossClient, file, childPath);
            if (b) {
                String attach = childPath;
                Picture pic = new Picture();
                pic.setId(pictureId);
                if (!StringUtils.isEmpty(picture.getAttach())) {
                    pic.setAttach(picture.getAttach()+","+attach+"|"+fileName);
                }else{
                    pic.setAttach(attach+"|"+fileName);
                }
                pictureMapper.updateByPrimaryKeySelective(pic);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }finally {
            PartionCodeHoder.clear();
        }
    }


    @RequestMapping(value = "/toUpload", method = RequestMethod.GET)
    public String toUpload(String category,String secondCategory, Model model) throws IOException {
        model.addAttribute("category", category);
        model.addAttribute("secondCategory", secondCategory);
        return "admin/upload2";
    }



    @RequestMapping(value = "/toListImg", method = RequestMethod.GET)
    public String toListImg(HttpServletRequest request, @RequestParam String path, @RequestParam String suffix, Model model) {
        model.addAttribute("path", path);
        model.addAttribute("selectedFileName", suffix);
        return "admin/album";
    }

    @RequestMapping(value = "/listImg", method = RequestMethod.POST)
    public String listImg( Long selectedId, String ids, String secondSelectedId,
                          String fourthSelectedId, Integer currentPage, Integer totalPage, Integer totalCount,Integer pageSize,Integer recommendId, Model model) {
        Integer partionCode = categoryService.getPartionCodeBy2(Integer.parseInt(secondSelectedId));
        PartionCodeHoder.set(String.valueOf(partionCode));
        List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s)).collect(Collectors.toList());
        List<Picture> pictures = pictureMapper.selectByIds(listIds);
        Picture picture = pictureMapper.selectByPrimaryKey(selectedId);
        PartionCodeHoder.clear();
        for(Picture p :pictures){
            if(StringUtils.isNoneBlank(p.getAttach())){
                p.setAttach(p.getFilePath()+"|"+p.getName()+","+p.getAttach());//加上原图
            }
        }
        int currentIndex =  listIds.indexOf(selectedId);
        model.addAttribute("selectedImg", picture.getFilePath());
        model.addAttribute("selectedName", picture.getName());
        model.addAttribute("attachs", picture.getFilePath());
        model.addAttribute("currentIndex", currentIndex);
        model.addAttribute("pictures", pictures);
        model.addAttribute("fourthSelectedId", fourthSelectedId);
        model.addAttribute("secondSelectedId", secondSelectedId);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("recommendId",recommendId);
        model.addAttribute("totalIndex", (currentIndex+1)+pageSize*(currentPage-1));
        return "admin/album";
    }


    @RequestMapping(value = "/reloadListImg", method = RequestMethod.POST)
    public String reloadListImg(String fourthSelectedId,String secondSelectedId,Integer currentPage,Integer pageSize,String moveFlag,Integer totalCount,Integer recommendId, Model model) {
        if("pre".equals(moveFlag)){
            currentPage = currentPage-1;
        }else{
            currentPage = currentPage +1;
        }
        Integer partionCode = categoryService.getPartionCodeBy2(Integer.parseInt(secondSelectedId));
        PartionCodeHoder.set(String.valueOf(partionCode));
        PageParameter pageParameter = new PageParameter(currentPage, pageSize);
        List<Set<Integer>> categoryConditions = new ArrayList<>();
        FileQueryVO fileQueryVO = new FileQueryVO();
        fileQueryVO.setPage(pageParameter);
        fileQueryVO.setRecommendId(recommendId);
        List<Picture> pictures;
        if(!StringUtils.isBlank(fourthSelectedId)){
            String[] ids = fourthSelectedId.split("[;]");
            Set<Integer> set ;
            for(String x:ids){
                set = new HashSet<>();
                String[] arr = x.split(",");
                for(String item:arr){
                    if(StringUtils.isNumeric(item)){
                        set.add(Integer.parseInt(item));
                    }
                }
                if(!CollectionUtils.isEmpty(set)){
                    categoryConditions.add(set);
                }
            }
            fileQueryVO.setCategoryConditions(categoryConditions);
            pictures =  pictureMapper.selectByPage(fileQueryVO);
        }else{
            pictures = pictureMapper.selectBySimplePage(fileQueryVO);
        }

        int totalPage = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                / pageParameter.getPageSize();
        Picture picture = pictures.get(0);
        for(Picture p :pictures){
            if(StringUtils.isNoneBlank(p.getAttach())){
                p.setAttach(p.getFilePath()+"|"+p.getName()+","+p.getAttach());//加上原图
            }
        }
        PartionCodeHoder.clear();
        model.addAttribute("selectedImg", picture.getFilePath());
        model.addAttribute("selectedName", picture.getName());
        model.addAttribute("currentIndex", 0);
        model.addAttribute("pictures", pictures);
        model.addAttribute("fourthSelectedId", fourthSelectedId);
        model.addAttribute("secondSelectedId", secondSelectedId);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("moveFlag",moveFlag);
        if("pre".equals(moveFlag)){
            model.addAttribute("totalIndex", pageSize*currentPage);
        }else{
            model.addAttribute("totalIndex", (currentPage-1)*pageSize+1);
        }
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("recommendId",recommendId);
        return "admin/album";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public
    @ResponseBody
    ResultVO delete(HttpServletRequest request, Long pictureId,Integer secondSelectedId) {
        ResultVO result = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            throw new RuntimeException("无权限操作");
        }
        List<String> keys = new ArrayList<>();
        try {
            Integer partionCode = categoryService.getPartionCodeBy2(secondSelectedId);
            PartionCodeHoder.set(String.valueOf(partionCode));
            Picture picture = pictureMapper.selectByPrimaryKey(pictureId);
            int i =  pictureMapper.deleteByPrimaryKey(pictureId);
            List list = new ArrayList();
            list.add(pictureId);
            pictureCategoryMapper.deleteByPictrueIds(list);
            deleteFile(pictureId, ossClient, keys, picture, i);
            result.setCode(0);
        } catch (Exception e) {
            log.error("删除文件失败", e);
            result.setCode(-1);
            result.setMsg("删除文件失败");
            return result;
        }finally {
            PartionCodeHoder.clear();
        }
        return result;
    }

    @RequestMapping(value = "/deleteAttach", method = RequestMethod.GET)
    public
    @ResponseBody
    ResultVO deleteAttach(HttpServletRequest request, Long pictureId,String attachPath) {
        ResultVO result = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            throw new RuntimeException("无权限操作");
        }
        List<String> keys = new ArrayList<>();
        try {
            Picture picture = pictureMapper.selectByPrimaryKey(pictureId);
            String attach = picture.getAttach();
            if(StringUtils.isNoneBlank(attach)){
              List<String> list =   Arrays.stream(attach.split(",")).filter(s-> !s.substring(0,s.indexOf("|")).equals(attachPath)).collect(Collectors.toList());
              if(!CollectionUtils.isEmpty(list)){
                  attach =  StringUtils.join(list,",");
              }else{
                  attach = "";
              }
                Picture p = new Picture();
                p.setId(pictureId);
                p.setAttach(attach);
                pictureMapper.updateByPrimaryKeySelective(p);
                keys.add(attachPath);
                deleteFile( ossClient, keys);
            }
            result.setCode(0);
        } catch (Exception e) {
            log.error("删除文件失败", e);
            result.setCode(-1);
            result.setMsg("删除文件失败");
            return result;
        }
        return result;
    }

    public void deleteFile(Long pictureId, OSSClient ossClient, List<String> keys, Picture picture, int i) {
        try{
            if (picture != null & i>0) {
                keys.add(picture.getFilePath());
                String attach = picture.getAttach();
                if (StringUtils.isNoneBlank(attach)) {
                    List<String> attachs = Arrays.stream(attach.split(",")).map(s->s.substring(0,s.indexOf("|"))).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(attachs)) {
                        keys.addAll(attachs);
                    }
                }
                deleteFile( ossClient, keys);
            }
        }catch (Exception e){
            log.error(e.getLocalizedMessage(),e);
        }

    }

    public void deleteFile( OSSClient ossClient, List<String> keys) {
        if (!CollectionUtils.isEmpty(keys)) {
            try{
                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(appConfig.getBucketName());
                deleteObjectsRequest.setKeys(keys);
                ossClient.deleteObjects(deleteObjectsRequest);
            }catch (Exception e){
                log.error(e.getLocalizedMessage(),e);
            }

        }
    }

    @RequestMapping(value = "/uploadIndexBg", method = RequestMethod.POST)
    public
    @ResponseBody
    String uploadIndexBg(HttpServletRequest request, @RequestParam MultipartFile file) throws IOException {
        String realPath = request.getSession().getServletContext().getRealPath(Constants.UPLOAD_BG_ROOT_PATH);
        String originalName = file.getOriginalFilename();
        String extName = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
        String pathname = realPath + "/indexBg" + extName;
        File descFile = new File(pathname);
        FileUtils.copyInputStreamToFile(file.getInputStream(), descFile);
        return pathname;
    }

    @RequestMapping(value = "/toUpdateIndexBg", method = RequestMethod.GET)
    public String toUpdateIndexBg() {
        return "admin/updateIndexBg";
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download( String imgPath,String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserSession userSession = (UserSession) request.getSession().getAttribute(Constants.USER_SESSION);
        User user = userMapper.selectByPrimaryKey(userSession.getUser().getUserId());
        if(user.getDownloadPerDay()>user.getMaxDownloadTimes()){
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write("<script>alert('超出每天最大下载次数,请明日再下载')</script>");
            printWriter.flush();
            printWriter.close();
        }
        downLoad(imgPath, fileName, response);
        user.setDownloadPerDay(user.getDownloadPerDay()+1);
        user.setDownloadTotal(user.getDownloadTotal()+1);
        user.setUserId(userSession.getUser().getUserId());
        userMapper.updateByPrimaryKeySelective(user);
    }

    public void downLoad(String filePath,String fileName, HttpServletResponse response) throws Exception {
        byte[] buf = new byte[1024];
        int len = 0;
        BufferedInputStream reader = null;
        OutputStream out = null;
       // OSSClient ossClient = null;
        try {
            /*ossClient = new OSSClient(appConfig.getEndpoint(), appConfig.getAccessKeyId(), appConfig.getAccessKeySecret());
            OSSObject ossObject = ossClient.getObject(appConfig.getBucketName(), filePath);*/
            URL url = new URL(filePath);
            URLConnection con = url.openConnection();
            reader = new BufferedInputStream(con.getInputStream());
            out = response.getOutputStream();
            response.reset(); // 非常重要
            response.setContentType("applicatoin/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            while ((len = reader.read(buf)) > 0)
                out.write(buf, 0, len);
        } finally {
            if(out!=null){
                out.close();
            }
            if(reader!=null){
                reader.close();
            }
        }

    }


    @RequestMapping(value = "/modifyDate", method = RequestMethod.GET)
    public @ResponseBody String modifyDate(String partionCodes) throws IOException {
        String arrs[] = partionCodes.split(",");
        for (String partionCode:arrs){
            modify(partionCode);
        }
        return "处理完成";
    }

    public void modify(String partionCode) {
        PartionCodeHoder.set(partionCode);
        int currentPage = 1;
        while (true){
            PageParameter pageParameter = new PageParameter(currentPage,120);
            FileQueryVO fileQueryVO = new FileQueryVO();
            fileQueryVO.setPage(pageParameter);
            List<Picture> list = pictureMapper.selectBySimplePage(new FileQueryVO());
            int totalPage = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                    / pageParameter.getPageSize();
            if(currentPage>totalPage || CollectionUtils.isEmpty(list)){
                break;
            }
            for(Picture picture:list){
                updateWH(picture);
            }
            currentPage++;
        }
        PartionCodeHoder.clear();
    }

    private void updateWH(Picture picture) {
        HttpURLConnection httpconn = null;
        InputStream inputStream = null;
        try{
            if(StringUtils.isNoneBlank(picture.getWidth())){
                return;
            }
             String url = ZNZ_OSS_CN_SHENZHEN_INTERNAL_ALIYUNCS_COM +picture.getFilePath();
            httpconn = (HttpURLConnection)new URL(url).openConnection();
            inputStream  = httpconn.getInputStream();
            BigDecimal size = new BigDecimal(httpconn.getContentLength());
            size = size.divide(new BigDecimal(1024),BigDecimal.ROUND_HALF_UP).setScale(0);
            BufferedImage sourceImg = ImageIO.read(inputStream);
            Picture p = new Picture();
            p.setId(picture.getId());
            p.setWidth(String.valueOf(sourceImg.getWidth()));
            p.setHeight(String.valueOf(sourceImg.getHeight()));
            p.setSize(String.valueOf(size));
            pictureMapper.updateByPrimaryKeySelective(p);
        }catch (Exception e){
            log.error(e.getLocalizedMessage(),e);
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpconn!=null){
                try {
                    httpconn.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
