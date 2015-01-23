package com.znz.util;

import java.io.File;
import java.util.Date;

public class ThreadDemo implements Runnable {
     // 要统计的磁盘路径
     private String path;
 
     // 构造方法
     public ThreadDemo(String path) {
       this.path = path;
    }
 
     // 主方法
     public static void main(String[] args) {
         // 得到根目录
         File[] root = File.listRoots();
         System.out.println("所有目录:");
         for (int i = 0; i < root.length; i++) {
             System.out.println(root[i].getAbsolutePath());
         }
        System.out.println("=====================================");
         for (int i = 0; i < root.length; i++) {
     System.out.println("开始处理"+root[i].getAbsolutePath()+"目录");
             // 创建线程对象
             ThreadDemo td = new ThreadDemo("d:/workspace");
             new Thread(td).start();
         }
     }

     // 重写run方法
     public void run() {
         long start = new Date().getTime();
         int num = countFile(path);
         long end = new Date().getTime();
         System.out.println(path + "统计共有" + num + "个文件！共耗时：[" + (end - start) + "]ms");
     }
 
     // 统计文件数目的方法
     public int countFile(String path) {
         int count = 0;
         File file = new File(path);
         // 得到该目录下的所有文件
         File[] subFile = file.listFiles();
         // 如果该目录为空或
         if (null == subFile || subFile.length == 0) {
             return count;
         }
         for (int i = 0; i < subFile.length; i++) {
             if (subFile[i].isDirectory()) {
                 // System.out.println(subFile[i].getAbsolutePath());
                count += countFile(subFile[i].getAbsolutePath());
             } else if (subFile[i].isFile()) {
            	 if(subFile[i].getName().indexOf("TimeServerHandler")>0){
                // if (getPostfix(subFile[i].getName()).equals("gif")) {
                     System.out.println("文件名称"+subFile[i].getAbsolutePath());
                 }
                 count++;
             }
         }
         return count;
     }
 
     /**
      * 获取inputFilePath的后缀名，如："e:/test.pptx"的后缀名为："pptx"<br>
      * 
      * @param inputFilePath
      * @return
      */
     public String getPostfix(String inputFilePath) {
         return inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1);
     }
 }