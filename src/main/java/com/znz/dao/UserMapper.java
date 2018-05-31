package com.znz.dao;

import com.znz.model.User;
import com.znz.vo.UserQueryVO;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    User selectByUser(  String userName);

    List<User> selectAllUser(int userType);

    int downloadTimes();

    List<User> selectByFirstCategory(String firstCategoryId);

    User selectByToken(String token);

    void updateToken();

}