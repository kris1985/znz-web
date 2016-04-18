package com.znz.dao;

import com.znz.model.TTravelLine;

public interface TTravelLineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TTravelLine record);

    int insertSelective(TTravelLine record);

    TTravelLine selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TTravelLine record);

    int updateByPrimaryKey(TTravelLine record);
}