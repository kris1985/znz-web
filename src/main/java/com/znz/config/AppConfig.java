package com.znz.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2015/1/23.
 */
@Component
@Data
public class AppConfig {

    @Value("${img.thumb.width}")
    private int imgThumbWidth;

    @Value("${img.thumb.height}")
    private int imgThumbHeight;

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.bucketName}")
    private String bucketName;

    @Value("${oss.path}")
    private String ossPath;

}
