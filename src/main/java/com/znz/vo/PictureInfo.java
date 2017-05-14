package com.znz.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2017/5/13.
 */
@Data
public class PictureInfo {


    @Data
    class Picture{
        private Long id;
        private String name;
        private String filePath;
        private Integer clickTimes;
        private Integer downloadTimes;
        private boolean myRecommend;
        private List<String> attachs;
    }

    @Data
    class PictureProperty{
        private String url;
        private String parameter;
    }
}
