package com.znz.util;

import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by huangtao on 2015/1/23.
 */
public class Test {

    public static  void  main(String args[]) throws IOException {
        //ZipUtil.unpack(new File("d:/temp/test.zip"), new File("d:/temp/"));
        //ZipUtil.explode(new File("d:/temp/temp.zip"));
        String extName ="11132.txt".substring("11132.txt".lastIndexOf(".")+1);
        System.out.println(extName);
       // FileUtils.moveDirectory(new File("d:/temp/test2"),new File("d:/temp/test3"));
        //FileUtils.moveDirectoryToDirectory(new File("d:/temp/test2"),new File("d:/temp/test3"),false);
        //FileUtils.moveToDirectory(new File("d:/temp/test2"),new File("d:/temp/test3"),false);
        //FileUtils.moveFileToDirectory(new File("d:/temp/test2"),new File("d:/temp/test3"),false);
       // FileUtils.moveFile(new File("d:/temp/test2"),new File("d:/temp/test3"));

        moveFiles(new File("d:/temp/test2"),new File("d:/temp/test3"));

    }

    public static void moveFiles(File srcfile,File destFile) throws IOException {
        File[] files = srcfile.listFiles();
        if(files==null)return ;
        for (File f : files) {
            if(f.isDirectory()){// 判断是否文件夹
                FileUtils.moveDirectoryToDirectory(f,destFile,false);
            }else{
                FileUtils.moveFileToDirectory(f,destFile,true);
            }
        }
    }
}
