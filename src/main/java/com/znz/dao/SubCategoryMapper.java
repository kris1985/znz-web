package com.znz.dao;

import com.znz.model.Category;
import com.znz.model.SubCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface SubCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SubCategory record);

    SubCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SubCategory record);

    int updateByPrimaryKey(SubCategory record);

    List<SubCategory> selectAll(@Param("parentId") Integer parentId);

    SubCategory selectByName(String name);

    SubCategory selectByNameAndPpid(@Param("name") String name, @Param("ppid") Integer ppid);

    Integer selectMaxSortId(Integer parentId);

    List<SubCategory> selectByParentId(Integer parentId);

    Integer selectMaxPartionCode();

    List<SubCategory> selectByPpid(Integer ppid);

    SubCategory selectSingleByPpid(Integer ppid);

    /**
     *  查询有品牌的类别ID
     * @return
     */
    Set<Integer> selectBrandCategoryIds();


}