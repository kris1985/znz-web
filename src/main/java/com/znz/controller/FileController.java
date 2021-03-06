package com.znz.controller;

import com.znz.config.AppConfig;
import com.znz.util.Constants;
import com.znz.util.FilePathConverter;
import com.znz.util.ImageUtil;
import com.znz.util.MyFileUtil;
import com.znz.vo.FileNodeVO;
import com.znz.vo.FileTreeVO;
import com.znz.vo.ListChildVO;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zeroturnaround.zip.ZipUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by huangtao on 2015/1/23.
 */

@Controller
@RequestMapping("/admin/file")
public class FileController {

    @Resource
    private AppConfig appConfig;

    @RequestMapping(value = "/upload/{parentDir}" , method= RequestMethod.POST)
    public  @ResponseBody void processUpload(HttpServletRequest request, @RequestParam MultipartFile [] files, Model model,@PathVariable String parentDir) throws IOException {
        String realPath  = request.getSession().getServletContext().getRealPath(Constants.UPLOAD_ROOT_PATH);
        for(MultipartFile file:files){
                String originalName = file.getOriginalFilename();
                String extName =originalName.substring(originalName.lastIndexOf(".")+1);
                String preName =originalName.substring(0,originalName.lastIndexOf("."));
                System.out.println(file.getOriginalFilename()+":"+extName);
                String pathname = realPath  +"/"+ parentDir + "/" + originalName;
                System.out.println("path:"+pathname);
                File descFile  = new File(pathname);
                FileUtils.copyInputStreamToFile(file.getInputStream(),descFile);
            if(!extName.equalsIgnoreCase("zip")){
                ImageUtil.thumbnailImage(pathname,appConfig.getImgThumbWidth(),appConfig.getImgThumbHeight());
            }
            if(extName.equalsIgnoreCase("zip")){
                //在临时目录解压并删除zip包
                File tem = FileUtils.getTempDirectory();
                String tempFilePath = tem.getPath()+"/"+preName;
                System.out.println("tempFilePath"+tempFilePath);
                ZipUtil.unpack(descFile, new File(tempFilePath));
                FileUtils.forceDelete(descFile);//删除zip
                ImageUtil.thumbnailImage(tempFilePath,appConfig.getImgThumbWidth(),appConfig.getImgThumbHeight());//生产缩略图
                MyFileUtil.moveFiles(new File(tempFilePath),new File(realPath + "/upload/" + parentDir));//移动到指定目录
                //删除临时文件
                FileUtils.deleteDirectory(new File(tempFilePath));
            }

        }


    }

    @RequestMapping(value = "/list" , method= RequestMethod.GET)
    public String list()  {
        return  "/admin/files";
    }

    @RequestMapping(value = "/space" , method= RequestMethod.GET)
    public String getSpace(HttpServletRequest request,Model model)  {
        String realPath = request.getSession().getServletContext().getRealPath("/");
        File file = new File(realPath);
      /*  System.out.println("Free space = " + file.getFreeSpace());
        System.out.println("used space = " + (file.getTotalSpace()-file.getFreeSpace()));
        System.out.println("Usable space = " + file.getUsableSpace());*/
        double usedSpace = (double)(file.getTotalSpace()/1024/1024/1024-file.getFreeSpace()/1024/1024/1024);
        double freeSpace = (double)file.getFreeSpace()/1024/1024/1024;
        BigDecimal   used   =   new BigDecimal(usedSpace);
        usedSpace = used.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal   free   =   new BigDecimal(freeSpace);
        freeSpace = free.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        model.addAttribute("freeSpace",freeSpace);
        model.addAttribute("usedSpace", usedSpace);
        return  "/admin/space";
    }

    @RequestMapping(value = "/tree" , method= RequestMethod.GET)
    public @ResponseBody List<FileTreeVO> listTree(HttpServletRequest request,Model model)  {
        String rootPath = request.getSession().getServletContext().getRealPath(Constants.UPLOAD_ROOT_PATH);
        File rootFile = new File(rootPath);
        List<FileTreeVO> list  = new ArrayList<FileTreeVO>();
        FileTreeVO root = new FileTreeVO();
        root.setId(FilePathConverter.encode(rootFile.getAbsolutePath()));
        root.setText(rootFile.getName());
        root.setParent("#");
        list.add(root);
        FileTreeVO fileTreeVO;
        MyFileUtil.listFile(rootFile, list);
        return  list;
    }

    @RequestMapping(value = "/chidren/{filePath}" , method= RequestMethod.GET)
    public @ResponseBody
    ListChildVO listChidren(HttpServletRequest request,Model model,@PathVariable String filePath)  {
        String realPath = request.getSession().getServletContext().getRealPath(Constants.UPLOAD_ROOT_PATH);
        ListChildVO vo = new ListChildVO();
        File file = new File(FilePathConverter.decode(filePath));
        List<FileNodeVO> parentNodes = new ArrayList<FileNodeVO>();
        FileNodeVO currentNode = new FileNodeVO();
        currentNode.setName(file.getName());
        currentNode.setPath(FilePathConverter.encode(file.getAbsolutePath()));
        currentNode.setDirectory(true);
        parentNodes.add(currentNode);
        MyFileUtil.getParentNode(file,new File(realPath),parentNodes);
        File files [] = file.listFiles();
        if(files!=null && files.length>0) {
            List<FileNodeVO> fileNodes = new ArrayList<FileNodeVO>();
            FileNodeVO fileNode = null;
            for (File f : files) {
                fileNode = new FileNodeVO();
                fileNode.setDirectory(f.isDirectory());
                fileNode.setName(f.getName());
                fileNode.setPath(FilePathConverter.encode(f.getAbsolutePath()));
                if(!f.isDirectory()){
                    fileNode.setUrl(f.getAbsolutePath().replace(realPath,request.getContextPath()+Constants.UPLOAD_ROOT_PATH));
                }
                fileNodes.add(fileNode);
            }
            vo.setFileNodes(fileNodes);
        }
        Collections.reverse(parentNodes);
        vo.setParentNodes(parentNodes);
        return  vo;
    }




}
