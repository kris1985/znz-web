package com.znz.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by huangtao on 2015/1/23.
 */
public class MyFileUtil {

    public static void moveFiles(File srcfile,File destFile) throws IOException {
        File[] files = srcfile.listFiles();
        if(files==null)return ;
        for (File f : files) {
            if(f.isDirectory()){// 判断是否文件夹
                FileUtils.moveDirectoryToDirectory(f, destFile, false);
            }else{
                FileUtils.moveFileToDirectory(f,destFile,true);
            }
        }
    }
}
