package com.znz.dao;

import com.znz.model.PictureCategory;

public interface PictureCategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PictureCategory record);

    int insertSelective(PictureCategory record);

    PictureCategory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PictureCategory record);

    int updateByPrimaryKey(PictureCategory record);
}