package com.znz.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.zeroturnaround.zip.ZipUtil;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * Created by huangtao on 2015/1/23.
 */
public class Test {

    public static  void  main(String args[]) throws IOException, NoSuchAlgorithmException {
        //ZipUtil.unpack(new File("d:/temp/test.zip"), new File("d:/temp/"));
        //ZipUtil.explode(new File("d:/temp/temp.zip"));
       // String extName ="11132.txt".substring("11132.txt".lastIndexOf(".")+1);
        //System.out.println(extName);
       // FileUtils.moveDirectory(new File("d:/temp/test2"),new File("d:/temp/test3"));
        //FileUtils.moveDirectoryToDirectory(new File("d:/temp/test2"),new File("d:/temp/test3"),false);
        //FileUtils.moveToDirectory(new File("d:/temp/test2"),new File("d:/temp/test3"),false);
        //FileUtils.moveFileToDirectory(new File("d:/temp/test2"),new File("d:/temp/test3"),false);
       // FileUtils.moveFile(new File("d:/temp/test2"),new File("d:/temp/test3"));

        //moveFiles(new File("d:/temp/test2"),new File("d:/temp/test3"));
       // System.out.println(DateTime.now().toString("yyyyMMDD"));
        String test = "from t_picture where 1=1 t_picture_category".replaceAll("t_picture[^\\_]","t_picture"+0+" ");
        System.out.println(test);
        Integer s = new ArrayList<Integer>().stream().findAny().orElse(null);
        System.out.println(s);
//        System.out.println(UUID.randomUUID().clockSequence());
        MessageDigest md = MessageDigest.getInstance("MD5");
        UUID uuid = UUID.randomUUID();
        String guidStr = uuid.toString();
        md.update(guidStr.getBytes(), 0, guidStr.length());
        System.out.println(new BigInteger(1, md.digest()).toString(16));




    }

    public static String safeUrlBase64Encode(byte[] data){
        String encodeBase64 = new BASE64Encoder().encode(data);
        String safeBase64Str = encodeBase64.replace('+', '-');
        safeBase64Str = safeBase64Str.replace('/', '_');
        safeBase64Str = safeBase64Str.replaceAll("=", "");
        return safeBase64Str;
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
