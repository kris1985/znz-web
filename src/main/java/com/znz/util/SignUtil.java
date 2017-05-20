package com.znz.util;

import com.alibaba.fastjson.JSON;
import com.znz.controller.LoginController;
import com.znz.vo.BaseRequest;
import com.znz.vo.SignInRequest;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/20.
 */
public class SignUtil {

    public  static String sign(Map<String,Object> paramMap){
        // 对参数名进行字典排序
        String[] keyArray = paramMap.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);
        // 拼接有序的参数名-值串
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : keyArray) {
            stringBuilder.append(key).append(paramMap.get(key));
        }
        String key = "b868a6c1-e554-4f";
        stringBuilder.append(key);
        String codes = stringBuilder.toString();
        String sign = org.apache.commons.codec.digest.DigestUtils.shaHex(codes).toUpperCase();
        return sign;
    }

    public static void main(String[] args) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("a","a");
        paramMap.put("b","b");
        System.out.println(sign(paramMap));

        BaseRequest<SignInRequest> baseRequest = new BaseRequest<>();
        baseRequest.setImei("123");
        baseRequest.setToken("343556773");
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUserName("ere");
        signInRequest.setPassword("sfdf");
        baseRequest.setData(signInRequest);
        System.out.println(JSON.toJSONString(baseRequest));
//7b762a11b090005d1124d93c1d03ae00
        System.out.println(new Md5PasswordEncoder().encodePassword("5555"+ LoginController.EKY,""));
    }
}
