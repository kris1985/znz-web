package com.znz.dao;

import com.znz.model.TTravelZyx;
import com.znz.vo.TravelZyxQueryVO;

import java.util.List;

public interface TTravelZyxMapper {
    int deleteByPrimaryKey(String lineid);

    int insert(TTravelZyx record);

    int insertSelective(TTravelZyx record);

    TTravelZyx selectByPrimaryKey(String lineid);

    int updateByPrimaryKeySelective(TTravelZyx record);

    int updateByPrimaryKey(TTravelZyx record);

    List<TTravelZyx> selectByPage(TravelZyxQueryVO queryQueryVO);
}