package com.znz.controller;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
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



    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    void processUpload(HttpServletRequest request, @RequestParam MultipartFile[] files, String category, Model model) throws IOException {
        // String realPath  = request.getSession().getServletContext().getRealPath(Constants.UPLOAD_ROOT_PATH);
        UserSession userSession = (UserSession) request.getSession().getAttribute(Constants.USER_SESSION);
        ResultVO result = new ResultVO();
        if (!checkPermisson(userSession, result)) {
            throw new RuntimeException("无权限操作");
        }
        OSSClient ossClient = new OSSClient(appConfig.getEndpoint(), appConfig.getAccessKeyId(), appConfig.getAccessKeySecret());
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
        } finally {
            ossClient.shutdown();
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
    void uploadChild(HttpServletRequest request, @RequestParam MultipartFile[] files, Long pictureId) throws IOException {
        UserSession userSession = (UserSession) request.getSession().getAttribute(Constants.USER_SESSION);
        ResultVO result = new ResultVO();
        if (!checkPermisson(userSession, result)) {
            throw new RuntimeException("无权限操作");
        }
        OSSClient ossClient = new OSSClient(appConfig.getEndpoint(), appConfig.getAccessKeyId(), appConfig.getAccessKeySecret());
        try {
            List<String> attachs = new ArrayList<>();
            String childPath;
            for (MultipartFile file : files) {
                childPath = UUID.randomUUID().toString() + getSuffix(file.getOriginalFilename());
                boolean b = upload(ossClient, file, childPath);
                if (b) {
                    attachs.add(childPath);
                }
            }
            if (!CollectionUtils.isEmpty(attachs)) {
                Picture picture = new Picture();
                picture.setId(pictureId);
                picture.setAttach(JSON.toJSONString(attachs));
                pictureMapper.updateByPrimaryKeySelective(picture);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ossClient.shutdown();
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

    @RequestMapping(value = "/listImg/{id}", method = RequestMethod.GET)
    public String listImg(HttpServletRequest request, @PathVariable Long id, String ids, String filePaths, Model model) {
        Picture picture = pictureMapper.selectByPrimaryKey(id);
        model.addAttribute("selectedImg", picture.getFilePath());
        model.addAttribute("imgs", Arrays.asList(filePaths.split(",")));
        model.addAttribute("parentName", picture.getName());
        List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s)).collect(Collectors.toList());
        if(StringUtils.isNoneBlank(picture.getAttach())){
            try{
                model.addAttribute("attachs", JSON.parseArray(picture.getAttach(),String.class));
            }catch (Exception e){
                log.error(e.getLocalizedMessage(),e);
            }
        }
        model.addAttribute("currentIndex", Collections.binarySearch(listIds, id));
        return "admin/album";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public
    @ResponseBody
    ResultVO delete(HttpServletRequest request, Long pictureId) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(Constants.USER_SESSION);
        ResultVO result = new ResultVO();
        if (!checkPermisson(userSession, result)) {
            return result;
        }
        OSSClient ossClient = new OSSClient(appConfig.getEndpoint(), appConfig.getAccessKeyId(), appConfig.getAccessKeySecret());
        List<String> keys = new ArrayList<>();
        try {
            Picture picture = pictureMapper.selectByPrimaryKey(pictureId);
            int i =  pictureMapper.deleteByPrimaryKey(pictureId);
            deleteFile(pictureId, ossClient, keys, picture, i);
            result.setCode(0);
        } catch (Exception e) {
            log.error("删除文件失败", e);
            result.setCode(-1);
            result.setMsg("删除文件失败");
            return result;
        } finally {
            ossClient.shutdown();
        }
        return result;
    }

    public void deleteFile(Long pictureId, OSSClient ossClient, List<String> keys, Picture picture, int i) {
        if (picture != null & i>0) {
            pictureCategoryMapper.deleteByPrimaryKey(pictureId);
            keys.add(picture.getFilePath());
            String attach = picture.getAttach();
            if (StringUtils.isNoneBlank(attach)) {
                List<String> attachs = JSON.parseArray(attach, String.class);
                if (!CollectionUtils.isEmpty(attachs)) {
                    keys.addAll(attachs);
                }
            }
            deleteFile(pictureId, ossClient, keys);
        }
    }

    public void deleteFile(Long pictureId, OSSClient ossClient, List<String> keys) {
        if (!CollectionUtils.isEmpty(keys)) {
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(appConfig.getBucketName());
            deleteObjectsRequest.setKeys(keys);
            ossClient.deleteObjects(deleteObjectsRequest);
        }
    }


    private boolean checkPermisson(UserSession userSession, ResultVO result) {
        if (userSession.getUser().getUserType() != 2 && userSession.getUser().getUserType() != 3) {
            result.setCode(-1);
            result.setMsg("无权限操作");
            return false;
        }
        return true;
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
