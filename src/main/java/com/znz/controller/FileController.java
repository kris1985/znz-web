package com.znz.controller;

import com.znz.config.AppConfig;
import com.znz.dao.UserAuthMapper;
import com.znz.dao.UserMapper;
import com.znz.model.User;
import com.znz.model.UserAuth;
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
import javax.servlet.jsp.PageContext;
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
    private UserAuthMapper userAuthMapper;

    @Resource
    private UserMapper userMapper;

    public static List<FileTreeVO> treeCache = new ArrayList<FileTreeVO>(1024000);

    public static Map<String ,List<FileTreeVO>>  userFilesCache = new HashedMap(1024000);

    public static Map<String ,ListChildVO> childCache = new  HashedMap(102400);


    @RequestMapping(value = "/upload/{parentDir}", method = RequestMethod.POST)
    public
    @ResponseBody
    void processUpload(HttpServletRequest request, @RequestParam MultipartFile[] files, Model model, @PathVariable String parentDir) throws IOException {
        // String realPath  = request.getSession().getServletContext().getRealPath(Constants.UPLOAD_ROOT_PATH);
        UserSession userSession = (UserSession) request.getSession().getAttribute(Constants.USER_SESSION);
        ResultVO result = new ResultVO();
        if (!checkPermisson(userSession, result)) {
            throw new RuntimeException("无权限操作");
        }
        parentDir = FilePathConverter.decode(parentDir).replace("_anchor", "");
        for (MultipartFile file : files) {
            String originalName = file.getOriginalFilename();
            String extName = originalName.substring(originalName.lastIndexOf(".") + 1);
            String preName = originalName.substring(0, originalName.lastIndexOf("."));
            //System.out.println(file.getOriginalFilename()+":"+extName);
            String pathname = parentDir + "/" + originalName;
            // System.out.println("path:"+pathname);
            File descFile = new File(pathname);
            FileUtils.copyInputStreamToFile(file.getInputStream(), descFile);
            if (!extName.equalsIgnoreCase("zip")) {
                ImageUtil.thumbnailImage(pathname, appConfig.getImgThumbWidth(), appConfig.getImgThumbHeight());
            }
            if (extName.equalsIgnoreCase("zip")) {
                //在临时目录解压并删除zip包
                File tem = FileUtils.getTempDirectory();
                String tempFilePath = tem.getPath() + "/" + preName;
                // System.out.println("tempFilePath"+tempFilePath);
                ZipUtil.unpack(descFile, new File(tempFilePath));
                FileUtils.forceDelete(descFile);//删除zip
                ImageUtil.thumbnailImage(tempFilePath, appConfig.getImgThumbWidth(), appConfig.getImgThumbHeight());//生产缩略图
                MyFileUtil.moveFiles(new File(tempFilePath), new File(parentDir));//移动到指定目录
                //删除临时文件
                FileUtils.deleteDirectory(new File(tempFilePath));
            }
        }
        invalidCache();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "/admin/files";
    }

    @RequestMapping(value = "/space", method = RequestMethod.GET)
    public String getSpace(HttpServletRequest request, Model model) {
        String realPath = request.getSession().getServletContext().getRealPath("/");
        File file = new File(realPath);
      /*  System.out.println("Free space = " + file.getFreeSpace());
        System.out.println("used space = " + (file.getTotalSpace()-file.getFreeSpace()));
        System.out.println("Usable space = " + file.getUsableSpace());*/
        double usedSpace = (double) (file.getTotalSpace() / 1024 / 1024 / 1024 - file.getFreeSpace() / 1024 / 1024 / 1024);
        double freeSpace = (double) file.getFreeSpace() / 1024 / 1024 / 1024;
        BigDecimal used = new BigDecimal(usedSpace);
        usedSpace = used.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal free = new BigDecimal(freeSpace);
        freeSpace = free.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        model.addAttribute("freeSpace", freeSpace);
        model.addAttribute("usedSpace", usedSpace);
        return "/admin/space";
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public
    @ResponseBody
    List<FileTreeVO> listTree(HttpServletRequest request) {
        if(!CollectionUtils.isEmpty(treeCache)){
            return  treeCache;
        }
        String rootPath = getRealPath(request);
       // log.info("rootPath:" + rootPath);
        File rootFile = new File(rootPath);
        List<FileTreeVO> list = new ArrayList<FileTreeVO>();
        FileTreeVO root = new FileTreeVO();
        root.setId(FilePathConverter.encode(rootFile.getAbsolutePath()));
        //root.setText(rootFile.getName());
        root.setText("目录");
        root.setParent("#");
        list.add(root);
        UserSession userSession = (UserSession) request.getSession().getAttribute(Constants.USER_SESSION);
        if (userSession.getUser().getUserType() == 2 || userSession.getUser().getUserType() == 3) {
            MyFileUtil.listFile(rootFile, list);
            Collections.sort(list, new Comparator<FileTreeVO>() {
                @Override
                public int compare(FileTreeVO o1, FileTreeVO o2) {
                    return o2.getText().compareToIgnoreCase(o1.getText());
                }
            });
            treeCache = list;
        } else {
            String userName = userSession.getUser().getUserName();
            List<FileTreeVO> fileTreeVOs = userFilesCache.get(userName);
            if(!CollectionUtils.isEmpty(fileTreeVOs)){
                return fileTreeVOs;
            }
            List<UserAuth> auths = userSession.getUserAuths();
            if (!CollectionUtils.isEmpty(auths)) {
                for (UserAuth userAuth : auths) {
                    FileTreeVO secondDir = new FileTreeVO();
                    //log.info("pa2th--------------------------------" + rootPath + userAuth.getFilePath());
                    secondDir.setId(FilePathConverter.encode(rootPath + userAuth.getFilePath()));
                    //log.info("pa3th--------------------------------" + secondDir.getId());
                    secondDir.setText(userAuth.getFilePath());
                    secondDir.setParent(root.getId());
                    list.add(secondDir);
                    String path = rootPath + userAuth.getFilePath();
                   // log.info("path--------------------------------" + path);
                    MyFileUtil.listFile(new File(path), list);
                }
                Collections.sort(list, new Comparator<FileTreeVO>() {
                    @Override
                    public int compare(FileTreeVO o1, FileTreeVO o2) {
                        return o2.getText().compareToIgnoreCase(o1.getText());
                    }
                });
                userFilesCache.put(userSession.getUser().getUserName(),list);
            } else {
                list = Collections.emptyList();
            }
        }
        return list;
    }

    public static String getRealPath(HttpServletRequest request) {
        String realPath = request.getSession().getServletContext().getRealPath(Constants.UPLOAD_ROOT_PATH);
        //log.info("real2Path:"+ realPath);
        //log.info("real3Path:"+ File.separator);
        if (!File.separator.equals(realPath.charAt(realPath.length() - 1))) {
           // realPath += File.separator;
        }
        return realPath;
    }

    @RequestMapping(value = "/chidren/{filePath}", method = RequestMethod.GET)
    public
    @ResponseBody
    ListChildVO listChidren(HttpServletRequest request, @PathVariable String filePath) {
        ListChildVO childVO =  childCache.get(filePath);
        if(childVO!=null){
            return childVO;
        }
        String realPath = getRealPath(request);
        ListChildVO vo = new ListChildVO();
        File file = new File(FilePathConverter.decode(filePath));
        List<FileNodeVO> parentNodes = new ArrayList<FileNodeVO>();
        FileNodeVO currentNode = new FileNodeVO();
        currentNode.setName(file.getName());
        currentNode.setPath(FilePathConverter.encode(file.getAbsolutePath()));
        currentNode.setDirectory(true);
        parentNodes.add(currentNode);
        MyFileUtil.getParentNode(file, new File(realPath), parentNodes);
        File files[] = file.listFiles();
        if (files != null && files.length > 0) {
            List<FileNodeVO> fileNodes = new ArrayList<FileNodeVO>();
            FileNodeVO fileNode = null;
            List<String> authsFiLeNames = new ArrayList<String>();
            UserSession userSession = (UserSession) request.getSession().getAttribute(Constants.USER_SESSION);
            if (userSession.getUser().getUserType() == 1) {
                List<UserAuth> auths = userSession.getUserAuths();
                if (!CollectionUtils.isEmpty(auths)) {
                    if ("ZNZ".equals(file.getName())) {
                        for (UserAuth userAuth : auths) {
                            authsFiLeNames.add(userAuth.getFilePath());
                        }
                    }
                }
            }
            for (File f : files) {
                if ("ZNZ".equals(file.getName()) && userSession.getUser().getUserType() == 1) {
                    if (!authsFiLeNames.contains(f.getName())) {
                        continue;
                    }
                }
                fileNode = new FileNodeVO();
                fileNode.setDirectory(f.isDirectory());
                fileNode.setName(f.getName().replaceFirst(ImageUtil.DEFAULT_THUMB_PREVFIX, ""));
                fileNode.setPath(FilePathConverter.encode(f.getAbsolutePath()));
                fileNode.setLastModified(f.lastModified());
                if (f.isDirectory()) {
                    fileNodes.add(fileNode);
                } else if (f.getName().startsWith(ImageUtil.DEFAULT_THUMB_PREVFIX)) {
                  /*  log.info("--1--"+f.getAbsolutePath());
                    log.info("--2--"+f.getAbsolutePath().replace(realPath, request.getContextPath() + Constants.UPLOAD_ROOT_PATH));
                    log.info("--33--"+f.getAbsolutePath().replace(realPath, request.getContextPath() + Constants.UPLOAD_ROOT_PATH).replaceAll("\\\\","/"));
                    log.info("f.getAbsolutePath():"+f.getAbsolutePath());
                    log.info("request.getContextPath():"+request.getContextPath());
                    log.info("realPath:"+ realPath);*/
                    fileNode.setThumbUrl(f.getAbsolutePath().replace(realPath, request.getContextPath() + Constants.UPLOAD_ROOT_PATH).replaceAll("\\\\", "/"));
                    fileNode.setUrl(fileNode.getThumbUrl().replaceFirst(ImageUtil.DEFAULT_THUMB_PREVFIX, "").replaceAll("\\\\", "/"));//解决火狐下图片不显示问题
                    fileNodes.add(fileNode);
                }
            }
          /* for (FileNodeVO fileNodeVO:fileNodes){
               log.info(fileNodeVO.getName() );
           }
            log.info(",");*/
            Collections.sort(fileNodes, new Comparator<FileNodeVO>() {
                @Override
                public int compare(FileNodeVO o1, FileNodeVO o2) {
                    return o2.getName().compareToIgnoreCase(o1.getName());
                }
            });
           /* for (FileNodeVO fileNodeVO:fileNodes){
                log.info(fileNodeVO.getName());
            }*/
            vo.setFileNodes(fileNodes);
        }
        Collections.reverse(parentNodes);
        vo.setParentNodes(parentNodes);
        childCache.put(filePath,vo);
        return vo;
    }

    public static void invalidCache(){
        treeCache.clear();
        userFilesCache.clear();
        childCache.clear();
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
        String realPath = getRealPath(request);
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
        String realPath = getRealPath(request);
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
            result.setCode(-1);
            result.setMsg("删除文件失败");
            return result;
        }
        result.setCode(0);
        result.setMsg("删除成功");
        invalidCache();
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
                userAuthMapper.updateByFilePath(oldFile.getName(), f.getName());
            }

        }
        invalidCache();
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

    @RequestMapping(value = "/download/{imgPath}", method = RequestMethod.GET)
    public void download(@PathVariable("imgPath") String imgPath, HttpServletRequest request, HttpServletResponse response) throws Exception {
       // log.info(imgPath);
        imgPath = FilePathConverter.decode(imgPath).replace("$dot$", ".");
        String realPath = request.getSession().getServletContext().getRealPath("/");
        realPath = realPath + imgPath;

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
        downLoad(realPath, response, false);
        user.setDownloadPerDay(user.getDownloadPerDay()+1);
        user.setDownloadTotal(user.getDownloadTotal()+1);
        user.setUserId(userSession.getUser().getUserId());
        userMapper.updateByPrimaryKeySelective(user);
    }

    public void downLoad(String filePath, HttpServletResponse response, boolean isOnLine) throws Exception {
        File f = new File(filePath);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        OutputStream out = response.getOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        try {
            response.reset(); // 非常重要
            if (isOnLine) { // 在线打开方式
                URL u = new URL("file:///" + filePath);
                response.setContentType(u.openConnection().getContentType());
                response.setHeader("Content-Disposition", "inline; filename=" + f.getName());
                // 文件名应该编码成UTF-8
            } else { // 纯下载方式
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(f.getName(), "UTF-8"));
            }

            while ((len = br.read(buf)) > 0)
                out.write(buf, 0, len);
        } finally {
            br.close();
            out.close();
        }

    }

}
