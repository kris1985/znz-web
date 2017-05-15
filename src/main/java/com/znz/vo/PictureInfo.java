package com.znz.vo;

import lombok.Data;
import org.springframework.security.web.PortResolverImpl;

import java.util.List;

/**
 * Created by Administrator on 2017/5/13.
 */
@Data
public class PictureInfo {

    private List<Picture> pictures;
    private PictureProperty pictureProperty;

    @Data
    public static class Picture{
        private Long id;
        private String name;
        private String filePath;
        private Integer clickTimes;
        private Integer downloadTimes;
        private boolean myRecommend;
        private List<String> attachs;
    }

    @Data
    public static  class PictureProperty{
        private String url;
        private String paramPrefix;
        private String sizeParam;
        private String waterMark;
    }
}
