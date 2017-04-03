package com.znz.dao;

import com.znz.model.PicRecommend;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2017/4/3.
 */
public interface PicRecommendMapper {

    int insert(PicRecommend picRecommend);

    int delete(@Param("pictureId") Long pictureId, @Param("userId")Integer userId);
}
