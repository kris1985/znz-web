package com.znz.controller;

import com.znz.dao.UserMapper;
import com.znz.model.User;
import com.znz.vo.UserLoginVO;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by huangtao on 2015/1/22.
 */

@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private Md5PasswordEncoder md5Encoder;

    @RequestMapping(value = "/login" , method= RequestMethod.POST)
    public ModelAndView simple(HttpServletRequest request,UserLoginVO userLoginVO) {
        String userName = userLoginVO.getUserName();
        String pwd = userLoginVO.getPwd();
        System.out.println(userName+":"+pwd);
        ModelAndView model = new ModelAndView();
        User user =  userMapper.selectByUser(userName);
        if(user == null || !pwd.equals(user.getPwd())){
            model.addObject("error", "用户名密码不正确");
            model.setViewName("index");
        }else{
            System.out.println("===================================="+user.getCompany());
            model.setViewName("success");
        }
        return model;
    }
}
