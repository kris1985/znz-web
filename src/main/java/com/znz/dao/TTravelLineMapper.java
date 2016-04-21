package com.znz.dao;

import com.znz.model.TTravelLine;
import com.znz.vo.TravelLineQueryQueryVO;

import java.util.List;

public interface TTravelLineMapper {
    int deleteByPrimaryKey(String id);

    int insert(TTravelLine record);

    int insertSelective(TTravelLine record);

    TTravelLine selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TTravelLine record);

    int updateByPrimaryKey(TTravelLine record);

    List<TTravelLine> selectByPage(TravelLineQueryQueryVO queryQueryVO);
}