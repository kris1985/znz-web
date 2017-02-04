package com.znz.dao;

import com.znz.model.SubCategory;

public interface SubCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SubCategory record);

    int insertSelective(SubCategory record);

    SubCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SubCategory record);

    int updateByPrimaryKey(SubCategory record);
}