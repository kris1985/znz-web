package com.znz.dao;

import com.znz.model.Category;
import com.znz.model.SubCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SubCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SubCategory record);

    SubCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SubCategory record);

    int updateByPrimaryKey(SubCategory record);

    List<SubCategory> selectAll(@Param("parentId") Integer parentId);

    SubCategory selectByName(String name);

    Integer selectMaxSortId(Integer parentId);

    List<SubCategory> selectByParentId(Integer parentId);

    Integer selectMaxPartionCode();

}