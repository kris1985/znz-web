package com.znz.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectRequest;
import com.znz.config.AppConfig;
import com.znz.dao.*;
import com.znz.model.*;
import com.znz.util.Constants;
import com.znz.util.FilePathConverter;
import com.znz.util.ImageUtil;
import com.znz.util.MyFileUtil;
import com.znz.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zeroturnaround.zip.ZipUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

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
    private CategoryMapper categoryMapper;


    private static String endpoint        = "oss-cn-shanghai.aliyuncs.com";
    private static String accessKeyId     = "fuRj5S4SBVnHH9el";
    private static String accessKeySecret = "AcJFEjMEvGGE9XsI9QokqIY8LITjL2";
    private static String bucketName      = "testznz";
    private static String key             = "kris1986";

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    void processUpload(HttpServletRequest request, @RequestParam MultipartFile[] files, Model model) throws IOException {
        // String realPath  = request.getSession().getServletContext().getRealPath(Constants.UPLOAD_ROOT_PATH);
        UserSession userSession = (UserSession) request.getSession().getAttribute(Constants.USER_SESSION);
        ResultVO result = new ResultVO();
        if (!checkPermisson(userSession, result)) {
            throw new RuntimeException("无权限操作");
        }
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            for (MultipartFile file : files) {
                String originalName = file.getOriginalFilename();
                ossClient.putObject(new PutObjectRequest(bucketName, originalName, file.getInputStream()));
                Picture picture = new Picture();
                picture.setName(originalName);
                picture.setFilePath(originalName);
                picture.setCreateTime(new Date());
                picture.setCreateUser(userSession.getUser().getUserName());
                picture.setClickTimes(0);
                picture.setDownloadTimes(0);
                pictureMapper.insert(picture);

                PictureCategory pictureCategory = new PictureCategory();
                pictureCategory.setPictureId(picture.getId());
                pictureCategory.setSubCategoryId(1);//todo
                pictureCategoryMapper.insert(pictureCategory);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            ossClient.shutdown();
        }
    }

    @RequestMapping(value = "/listPicture", method = RequestMethod.GET)
    public   String list(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") String page,
                                                   @RequestParam(value = "rows", defaultValue = "10") String rows,
                                                   @RequestParam(value = "sidx", required = false) String sidx,
                                                   @RequestParam(value = "sord", required = false) String sord,
                                                   @RequestParam(value = "filters", required = false) String filters, Model model) {
        PageParameter pageParameter = new PageParameter(Integer.parseInt(page), Integer.parseInt(rows));
        FileQueryVO fileQueryVO = new FileQueryVO();
        fileQueryVO.setPage(pageParameter);
        List<Picture> pictures =  pictureMapper.selectByPage(fileQueryVO);
        int total = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                / pageParameter.getPageSize();
        model.addAttribute("pictures",pictures);


        List<SubCategory> subCategories = subCategoryMapper.selectAll();
        List<Category> categories = categoryMapper.selectAll();
        List<SubCategoryVO> subCategoryVOs = new ArrayList<>();
        Map<Integer,String> map = new HashedMap();
        if(!CollectionUtils.isEmpty(subCategories) && !CollectionUtils.isEmpty(categories)){
            for(Category category:categories){
                map.put(category.getId(),category.getName());
            }
            for(SubCategory subCategory:subCategories){
                SubCategoryVO subCategoryVO = new SubCategoryVO();
                subCategoryVO.setId(String.valueOf(subCategory.getId()));
                subCategoryVO.setName(subCategory.getName());
                subCategoryVO.setParentId(subCategory.getParentId());
                subCategoryVO.setSortId(subCategory.getSortId());
                subCategoryVO.setParentName(map.get(subCategory.getParentId()));
                subCategoryVOs.add(subCategoryVO);
            }
        }
        model.addAttribute("subCategoryVOs",subCategoryVOs);
        return "admin/pictureList";
    }




    @RequestMapping(value = "/toListImg", method = RequestMethod.GET)
    public String toListImg(HttpServletRequest request, @RequestParam String path, @RequestParam String suffix, Model model) {
        model.addAttribute("path", path);
        model.addAttribute("selectedFileName", suffix);
        return "admin/album";
    }

    @RequestMapping(value = "/listImg/{path}", method = RequestMethod.GET)
    public String listImg(HttpServletRequest request, @PathVariable String path, @RequestParam String suffix, Model model) {
        path = FilePathConverter.decode(path + "." + suffix);
        String realPath ="";
        File f = new File(path);
        File parent = f.getParentFile();
        List<FileNodeVO> list = new ArrayList<FileNodeVO>();
        File files[] = parent.listFiles();
        FileNodeVO fileNode = null;
        FileNodeVO selected = null;
        for (File file : files) {
            if (file.getName().startsWith(ImageUtil.DEFAULT_THUMB_PREVFIX)) {
                fileNode = new FileNodeVO();
                fileNode.setLastModified(file.lastModified());
                fileNode.setName(file.getName());
                fileNode.setThumbUrl(file.getAbsolutePath().replace(realPath, request.getContextPath() + Constants.UPLOAD_ROOT_PATH).replaceAll("\\\\", "/"));
                fileNode.setUrl(fileNode.getThumbUrl().replaceFirst(ImageUtil.DEFAULT_THUMB_PREVFIX, "").replaceAll("\\\\", "/"));
                if (file.getName().equals(f.getName())) {
                    fileNode.setSelected(true);
                    selected = fileNode;
                }
                list.add(fileNode);
            }
        }
        Collections.sort(list, new Comparator<FileNodeVO>() {
            @Override
            public int compare(FileNodeVO o1, FileNodeVO o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        int currentIndex = list.indexOf(selected);

        String selectedImg = path.replace(realPath, request.getContextPath() + Constants.UPLOAD_ROOT_PATH);
        model.addAttribute("selectedImg", selectedImg);
        //model.addAttribute("encodeImg",FilePathConverter.encode(path));
        model.addAttribute("imgs", list);
        model.addAttribute("parentName", parent.getName());
        model.addAttribute("currentIndex", currentIndex);
        return "admin/album";
    }

    @RequestMapping(value = "/delete/{path}", method = RequestMethod.GET)
    public
    @ResponseBody
    ResultVO delete(HttpServletRequest request, @PathVariable String path) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(Constants.USER_SESSION);
        ResultVO result = new ResultVO();
        if (!checkPermisson(userSession, result)) {
            return result;
        }
        path = FilePathConverter.decode(path);
        String realPath = "";
        if (realPath.equals(path)) {
            result.setCode(-1);
            result.setMsg("根目录不能删除");
            return result;
        }
        File f = new File(path);
        if (!f.exists()) {
            result.setCode(-1);
            result.setMsg("文件不存在或已被删除");
            return result;
        }
        try {
            if (!f.isDirectory()) {
                FileUtils.deleteQuietly(f);//删除缩略图
                FileUtils.deleteQuietly(new File(f.getAbsolutePath().replaceFirst(ImageUtil.DEFAULT_THUMB_PREVFIX, "")));//删除原图
            } else {
                FileUtils.forceDelete(f);
            }
        } catch (IOException e) {
            log.error("删除文件失败", e);
            result.setCode(-1);
            result.setMsg("删除文件失败");
            return result;
        }
        result.setCode(0);
        result.setMsg("删除成功");
        return result;
    }

    private boolean checkPermisson(UserSession userSession, ResultVO result) {
        if (userSession.getUser().getUserType() != 2 && userSession.getUser().getUserType() != 3) {
            result.setCode(-1);
            result.setMsg("无权限操作");
            return false;
        }
        return true;
    }


    @RequestMapping(value = "/mkdir/{path}/{old}", method = RequestMethod.GET)
    public
    @ResponseBody
    String mkdir(@PathVariable("path") String path, @PathVariable("old") String old) throws UnsupportedEncodingException {
        path = FilePathConverter.decode(path);
        old = FilePathConverter.decode(old);
        if (StringUtils.isNoneBlank(path)) {
            path = path.replaceAll("_anchor", "");
        }
        if (StringUtils.isNoneBlank(old)) {
            old = old.replaceAll("_anchor", "");
        }
        File f = new File(path);
        File oldFile = new File(old);
        if (f.exists()) {
            return "文件夹已经存在，请重新输入";
        } else {
            if (!oldFile.exists()) {
                f.mkdirs();
            } else {
                oldFile.renameTo(f);

            }

        }
        return "0";
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



}
