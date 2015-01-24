package com.znz.controller;

import com.znz.config.AppConfig;
import com.znz.util.ImageUtil;
import com.znz.util.MyFileUtil;
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

/**
 * Created by huangtao on 2015/1/23.
 */

@Controller
@RequestMapping("/admin/file")
public class FileController {

    @Resource
    private AppConfig appConfig;

    @RequestMapping(value = "/upload" , method= RequestMethod.POST)
    public  @ResponseBody void processUpload(HttpServletRequest request, @RequestParam MultipartFile [] files, Model model) throws IOException {

        System.out.println("--------------------------------------");
        String parentDir = "test";
        String realPath  = request.getSession().getServletContext().getRealPath("/");
        for(MultipartFile file:files){
                String originalName = file.getOriginalFilename();
                String extName =originalName.substring(originalName.lastIndexOf(".")+1);
                String preName =originalName.substring(0,originalName.lastIndexOf("."));
                System.out.println(file.getOriginalFilename()+":"+extName);
                String pathname = realPath + "/upload/" + parentDir + "/" + originalName;
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
}
