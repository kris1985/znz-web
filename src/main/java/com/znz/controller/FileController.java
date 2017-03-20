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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.math.BigDecimal;
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



    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    void processUpload(HttpServletRequest request, @RequestParam MultipartFile[] files, String category, Model model) throws IOException {
        UserSession userSession = (UserSession) request.getSession().getAttribute(Constants.USER_SESSION);
        ResultVO result = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            throw new RuntimeException("无权限操作");
        }

        List<PictureCategory> pictureCategories = new ArrayList<>();
        String[] categorys = category.split(",");
        try {
            for (MultipartFile file : files) {
                String originalName = file.getOriginalFilename();
                String path = UUID.randomUUID().toString() + getSuffix(originalName);
                boolean b = upload(ossClient, file, path);
                if (b) {
                    Picture picture = new Picture();
                    picture.setName(originalName);
                    picture.setFilePath(path);
                    picture.setCreateTime(new Date());
                    picture.setCreateUser(userSession.getUser().getUserName());
                    picture.setClickTimes(0);
                    picture.setDownloadTimes(0);
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
    void uploadChild(HttpServletRequest request, @RequestParam MultipartFile file, Long pictureId) throws IOException {
        if (!PermissionUtil.checkPermisson(request)) {
            throw new RuntimeException("无权限操作");
        }
        Picture picture = pictureMapper.selectByPrimaryKey(pictureId);
        try {
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
        }
    }


    @RequestMapping(value = "/toUpload", method = RequestMethod.GET)
    public String toUpload(String category, Model model) throws IOException {
        model.addAttribute("category", category);
        return "admin/upload2";
    }



    @RequestMapping(value = "/toListImg", method = RequestMethod.GET)
    public String toListImg(HttpServletRequest request, @RequestParam String path, @RequestParam String suffix, Model model) {
        model.addAttribute("path", path);
        model.addAttribute("selectedFileName", suffix);
        return "admin/album";
    }

    @RequestMapping(value = "/listImg", method = RequestMethod.POST)
    public String listImg(HttpServletRequest request, Long id, String ids,String fourthSelectedId,Integer currentPage,Integer totalPage,Integer pageSize, Model model) {
        List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s)).collect(Collectors.toList());
        List<Picture> pictures = pictureMapper.selectByIds(listIds);
        Picture picture = pictureMapper.selectByPrimaryKey(id);
        for(Picture p :pictures){
            if(StringUtils.isNoneBlank(p.getAttach())){
                p.setAttach(p.getFilePath()+"|"+p.getName()+","+p.getAttach());//加上原图
            }
        }
        model.addAttribute("selectedImg", picture.getFilePath());
        model.addAttribute("selectedName", picture.getName());
        model.addAttribute("attachs", picture.getFilePath());
        model.addAttribute("currentIndex", listIds.indexOf(id));
        model.addAttribute("pictures", pictures);
        model.addAttribute("fourthSelectedId", fourthSelectedId);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageSize", pageSize);
        return "admin/album";
    }


    @RequestMapping(value = "/reloadListImg", method = RequestMethod.POST)
    public String reloadListImg(String fourthSelectedId,Integer currentPage,Integer pageSize, Model model) {
        PageParameter pageParameter = new PageParameter(currentPage, pageSize);
        List<Set<Integer>> categoryConditions = new ArrayList<>();
        FileQueryVO fileQueryVO = new FileQueryVO();
        fileQueryVO.setPage(pageParameter);
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
        List<Picture> pictures =  pictureMapper.selectByPage(fileQueryVO);
        int totalPage = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                / pageParameter.getPageSize();


        Picture picture = pictures.get(0);
        for(Picture p :pictures){
            if(StringUtils.isNoneBlank(p.getAttach())){
                p.setAttach(p.getFilePath()+"|"+p.getName()+","+p.getAttach());//加上原图
            }
        }
        model.addAttribute("selectedImg", picture.getFilePath());
        model.addAttribute("selectedName", picture.getName());
        model.addAttribute("currentIndex", 0);
        model.addAttribute("pictures", pictures);
        model.addAttribute("fourthSelectedId", fourthSelectedId);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("pageSize",pageSize);
        return "admin/album";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public
    @ResponseBody
    ResultVO delete(HttpServletRequest request, Long pictureId) {
        ResultVO result = new ResultVO();
        if (!PermissionUtil.checkPermisson(request)) {
            throw new RuntimeException("无权限操作");
        }
        List<String> keys = new ArrayList<>();
        try {
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
            response.setContentType("application/x-msdownload");
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

}
