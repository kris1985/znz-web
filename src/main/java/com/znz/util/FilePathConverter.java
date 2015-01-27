package com.znz.util;

import java.io.File;

/**
 * Created by huangtao on 2015/1/27.
 */
public class FilePathConverter {

    public static String encode(String absoulotePath){
       // System.out.println(absoulotePath);
        //System.out.println(File.separator);
        return  absoulotePath.replaceAll("\\"+File.separator,Constants.FILE_SEPARATOR);
    }

    public static String decode(String absoulotePath){
        return  absoulotePath.replaceAll(Constants.FILE_SEPARATOR,"\\"+File.separator);
    }
}
