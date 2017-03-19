package com.znz.dao;

import com.znz.model.PictureCategory;

import java.util.List;

public interface PictureCategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PictureCategory record);

    int insertSelective(PictureCategory record);

    PictureCategory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PictureCategory record);

    int updateByPrimaryKey(PictureCategory record);

    void batchInsert(List<PictureCategory> pictureCategories);


    List<Long> selectByCategoryId(Integer subCategoryId);

    List<Long> selectByCategoryIds(List<Integer> subCategoryIds);

    void deleteByPictrueIds(List<Long> pictureIds);
}