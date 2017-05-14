package com.znz.util;

import com.alibaba.fastjson.JSON;
import com.znz.vo.WatermarkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/5/15.
 */
@Slf4j
public class WaterMarkUtil {

    public static String getWaterMark(String waterMark)  {
        if(StringUtils.isEmpty(waterMark)){
            return "";
        }
        String watermarkParam = "";
        try{
            WatermarkVO watermarkVO = JSON.parseObject(waterMark,WatermarkVO.class);
            if(!StringUtils.isEmpty(watermarkVO.getImage())){
                if(watermarkVO.getP()!=null){
                    String img = watermarkVO.getImage()+"?x-oss-process=image/resize,P_"+watermarkVO.getP();
                    String base64 = safeUrlBase64Encode(img.getBytes("UTF-8"));
                    watermarkParam+="/watermark,image_"+base64;
                }else {
                    watermarkParam+="/watermark,image_"+safeUrlBase64Encode(watermarkVO.getImage().getBytes());
                }
                if(!watermarkParam.equals("") && watermarkVO.getT()!=null){
                    watermarkParam+=",t_"+watermarkVO.getT();
                }
                if(!watermarkParam.equals("") && watermarkVO.getG()!=null){
                    watermarkParam+=",g_"+watermarkVO.getG();
                }
                if(!watermarkParam.equals("") && watermarkVO.getX()!=null){
                    watermarkParam+=",x_"+watermarkVO.getX();
                }
                if(!watermarkParam.equals("") && watermarkVO.getY()!=null){
                    watermarkParam+=",y_"+watermarkVO.getY();
                }
            }
        }catch (Exception e){
            log.error(e.getLocalizedMessage(),e);
        }
        return watermarkParam;
    }

    public static String safeUrlBase64Encode(byte[] data){
        String encodeBase64 = new BASE64Encoder().encode(data);
        String safeBase64Str = encodeBase64.replace('+', '-');
        safeBase64Str = safeBase64Str.replace('/', '_');
        safeBase64Str = safeBase64Str.replaceAll("=", "");
        safeBase64Str = safeBase64Str.replaceAll("\r\n", "");
        safeBase64Str = safeBase64Str.replaceAll("\n", "");
        return safeBase64Str;
    }
}
