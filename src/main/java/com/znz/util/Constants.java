package com.znz.util;

import com.aliyun.oss.model.DeleteObjectsRequest;
import com.znz.model.Picture;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2015/1/25.
 */
public class Constants {

    public static  final String  UPLOAD_ROOT_PATH = "/upload/ZNZ/";

    public static  final String  UPLOAD_ROOT_PATH2 = "/upload/ZNZ";

    public static  final String  UPLOAD_BG_ROOT_PATH = "/upload/bg/";

    public static  final String  FILE_SEPARATOR = "FILE_SEPARATOR";
    
    public static  final String  USER_SESSION = "userSession";

    public static  final String  ADMIN_FLAG = "isAdmin";

    public static  final String  WATERMARK_PARAM = "watermarkParam";
    public static  final String INDEX_PAGE ="https://www.tyulan.com/";


    public static void main(String[] args) {

        List<String> keys = new ArrayList<>();
        for(int i=0;i<2001;i++){
            keys.add(i+"");
        }
       deleteFile(keys);
        //  System.out.println(2001/1000);
    }

    public static void deleteFile( List<String> keys) {
        if (!CollectionUtils.isEmpty(keys)) {
            if(keys.size()>1000){
                int count = (keys.size()+1000)/1000;
                List<String> childs ;
                for(int i=0;i<count;i++){
                    int endIndex = (i+1)*1000;
                    if(endIndex>keys.size()){
                        endIndex = keys.size();
                    }
                    childs = keys.subList(i*1000,endIndex);
                    System.out.println(childs.get(childs.size()-1));
                  /*  DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(appConfig.getBucketName());
                    deleteObjectsRequest.setKeys(childs);
                    ossClient.deleteObjects(deleteObjectsRequest);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
            }else{
               /* DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(appConfig.getBucketName());
                deleteObjectsRequest.setKeys(keys);
                ossClient.deleteObjects(deleteObjectsRequest);*/
            }

        }
    }
}
