package com.znz.util;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2017/4/5.
 */
public class AESUtil {

    // 加密
    public static String Encrypt(String src, String key) throws Exception {
        if (key == null) {
            return null;
        }
        // 判断Key是否为16位
        if (key.length() != 16) {
            return null;
        }
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"0102030405060708
        IvParameterSpec iv = new IvParameterSpec(key.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,iv);
        byte[] encrypted = cipher.doFinal(src.getBytes());
       // return Base64.encode(encrypted);
        return new BASE64Encoder().encode(encrypted);
        // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    // 解密
    public static String Decrypt(String src, String key) throws Exception {
        try {
            // 判断Key是否正确
            if (key == null) {
                return null;
            }
            // 判断Key是否为16位
            if (key.length() != 16) {
                return null;
            }
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec,iv);
            //byte[] encrypted1 = Base64.decode(src);// 先用base64解密
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(src);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) throws Exception {
        String key = "b868a6c1-e554-4f";
        String src = "6228480402564890099";
        System.out.println(src);
        // 加密
        long start = System.currentTimeMillis();
        String enString = AESUtil.Encrypt(src, key);
        System.out.println("加密后的字串是：" + enString);

        long useTime = System.currentTimeMillis() - start;
        System.out.println("加密耗时：" + useTime + "毫秒");

        // 解密
        start = System.currentTimeMillis();
        String DeString = "";
        for(int i=0;i<60000;i++)
         DeString = AESUtil.Decrypt(enString, key);
        System.out.println("解密后的字串是：" + DeString);
        useTime = System.currentTimeMillis() - start;
        System.out.println("解密耗时：" + useTime + "毫秒");
    }
}


