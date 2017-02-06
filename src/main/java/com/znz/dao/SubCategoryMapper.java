package com.znz.dao;

import com.znz.model.Category;
import com.znz.model.SubCategory;

import java.util.List;

public interface SubCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SubCategory record);

    int insertSelective(SubCategory record);

    SubCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SubCategory record);

    int updateByPrimaryKey(SubCategory record);

    List<SubCategory> selectAll();

    SubCategory selectByName(String name);

    Integer selectMaxSortId(Integer parentId);
}