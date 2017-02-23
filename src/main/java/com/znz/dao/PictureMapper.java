package com.znz.dao;

import com.znz.model.Picture;
import com.znz.vo.FileQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PictureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Picture record);

    int insertSelective(Picture record);

    Picture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Picture record);

    int updateByPrimaryKey(Picture record);

    List<Picture> selectByPage(FileQueryVO fileQueryVO);
}