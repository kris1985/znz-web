package com.znz.controller;

import com.znz.util.ImageUtil;
import com.znz.util.MyFileUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zeroturnaround.zip.ZipUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * Created by huangtao on 2015/1/23.
 */

@RestController
@RequestMapping("/admin/file")
public class FileController {

    @RequestMapping(value = "/upload" , method= RequestMethod.POST)
    public void processUpload(HttpServletRequest request, @RequestParam MultipartFile [] files, Model model) throws IOException {
        System.out.println("--------------------------------------");
        String parentDir = "test";
        String realPath  = request.getSession().getServletContext().getRealPath("/");
        for(MultipartFile file:files){
                String originalName = file.getOriginalFilename();
                String extName =originalName.substring(originalName.lastIndexOf(".")+1);
                String preName =originalName.substring(0,originalName.lastIndexOf("."));
                System.out.println(file.getOriginalFilename()+":"+extName);
                String pathname = realPath + "/upload/" + parentDir + "/" + extName;
                System.out.println("path:"+pathname);
                File descFile  = new File(pathname);
                FileUtils.copyInputStreamToFile(file.getInputStream(),descFile);
            if(!extName.equalsIgnoreCase("zip")){
                ImageUtil.thumbnailImage(pathname,120,100);
            }
            if(extName.equalsIgnoreCase("zip")){
                //在临时目录解压并删除zip包
                File tem = FileUtils.getTempDirectory();
                String tempFilePath = tem.getPath()+"/"+preName;
                System.out.println("tempFilePath"+tempFilePath);
                ZipUtil.unpack(descFile, new File(tempFilePath));
                FileUtils.forceDelete(descFile);//删除zip
                ImageUtil.thumbnailImage(tempFilePath,120,100);//生产缩略图
                MyFileUtil.moveFiles(new File(tempFilePath),new File(realPath + "/upload/" + parentDir));//移动到指定目录
                //删除临时文件
                FileUtils.deleteDirectory(new File(tempFilePath));
            }

        }

       // model.addAttribute("message", "File '" + file[0].getOriginalFilename() + "' uploaded successfully");
    }
}
