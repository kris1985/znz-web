package com.znz.util;

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
}
