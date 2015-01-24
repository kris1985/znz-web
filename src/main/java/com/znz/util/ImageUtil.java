package com.znz.util;

import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

/**
 * 生产缩略图工具类
 */
@Slf4j
public class ImageUtil {

	
	private static String DEFAULT_THUMB_PREVFIX = "thumb_";
    private static Boolean DEFAULT_FORCE = false;

    
    public static void main(String[] args) {
    	new ImageUtil().thumbnailImage("d:/git/test", 200, 160);
    }
    
    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 根据图片路径生成缩略图 </p>
     * @param imgFile    原图片路径
     * @param w            缩略图宽
     * @param h            缩略图高
     * @param prevfix    生成缩略图的前缀
     * @param force        是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
     */
    public  static void thumbnailImage1(File imgFile, int w, int h, String prevfix, boolean force){
        if(imgFile.exists()){
            try {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");
                String suffix = null;
                // 获取图片后缀
                if(imgFile.getName().indexOf(".") > -1) {
                    suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
                if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()+",") < 0){
                    log.error("Sorry, the image suffix is illegal. the standard image suffix is {}.",types);
                    return ;
                }
                Image img = ImageIO.read(imgFile);
                // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
                if(!force){
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if((width*1.0)/w < (height*1.0)/h){
                        if(width > w){
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w/(width*1.0)));
                            log.info("change image's height, width:+"+w+" height:"+h);
                        }
                    } else {
                        if(height > h){
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h/(height*1.0)));
                            log.info("change image's width,  width:+"+w+" height:"+h);
                        }
                    }
                }
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
                g.dispose();
                String p = imgFile.getPath();
                // 将图片保存在原目录并加上前缀
                ImageIO.write(bi, suffix, new File(p.substring(0,p.lastIndexOf(File.separator)) + File.separator + prevfix +imgFile.getName()));
            } catch (IOException e) {
               e.printStackTrace();
            }
        }else{
            log.warn("the src image is not exist.");
        }
    }
    
    public static void thumbnailImage(File file, int w, int h, String prevfix, boolean force){
         if(!file.isDirectory()){
             thumbnailImage1(file, w, h, prevfix, force);
         }
    	  File[] files = file.listFiles();
    	  if(files==null)return ;
    	  for (File f : files) {
              if(f.isDirectory()){// 判断是否文件夹
            	  thumbnailImage(f, w, h, prevfix, force);
              }else{
                  thumbnailImage1(f, w, h, prevfix, force);
              }
          }
      
    }
    
    public static void thumbnailImage(String imagePath, int w, int h, String prevfix, boolean force){
        File srcImg = new File(imagePath);
        thumbnailImage(srcImg, w, h, prevfix, force);
    }
    
    public static void thumbnailImage(String imagePath, int w, int h, boolean force){
        thumbnailImage(imagePath, w, h, DEFAULT_THUMB_PREVFIX, DEFAULT_FORCE);
    }
    
    public static void thumbnailImage(String imagePath, int w, int h){
        thumbnailImage(imagePath, w, h, DEFAULT_FORCE);
    }
}
