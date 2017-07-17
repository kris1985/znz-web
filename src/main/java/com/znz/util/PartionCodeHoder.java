package com.znz.util;

import com.znz.model.Picture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017/6/3.
 */
public class PartionCodeHoder {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();


    public static void set(String partionCode){
        threadLocal.set(partionCode);
    }

    public static String get(){
        return threadLocal.get();
    }

    public static void clear(){
         threadLocal.remove();
    }

    public static void main(String[] args) {
            try{
                String url = "http://znz.oss-cn-shenzhen.aliyuncs.com/a243d763-b4b6-4a2c-b26c-d14723b83f83.jpg";
                HttpURLConnection httpconn = (HttpURLConnection) new URL(url).openConnection();
                InputStream inputStream = httpconn.getInputStream();
                BigDecimal size = new BigDecimal(httpconn.getContentLength());
                size = size.divide(new BigDecimal(1024),BigDecimal.ROUND_HALF_UP).setScale(0);
                System.out.println(size);
                BufferedImage sourceImg = ImageIO.read(inputStream);
                System.out.println(sourceImg.getWidth());   // 源图宽度
                System.out.println(sourceImg.getHeight());

            }catch (Exception e){
                e.printStackTrace();
            }

    }
}
