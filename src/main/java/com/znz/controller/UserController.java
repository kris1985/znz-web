package com.znz.controller;

import com.znz.dao.UserMapper;
import com.znz.model.User;
import com.znz.vo.UserLoginVO;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * Created by huangtao on 2015/1/22.
 */

@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private Md5PasswordEncoder md5Encoder;

    @RequestMapping(value = "/login" , method= RequestMethod.POST)
    public String simple(HttpServletRequest request,@Valid @ModelAttribute("userLoginVO") UserLoginVO userLoginVO,BindingResult br,Model model) {
        if (br.hasErrors()){
            model.addAttribute("br",br);
            return  "/index";
        }
        String userName = userLoginVO.getUserName();
        String pwd = userLoginVO.getPwd();
        System.out.println(userName+":"+pwd);
        User user =  userMapper.selectByUser(userName);
        if(user == null || !pwd.equals(user.getPwd())){
            model.addAttribute("error", "用户或名密码不正确");
            return  "/index";
        }else{
            int limitIpFlag = user.getLimitIpFlag();
            if(1==limitIpFlag){
                if(StringUtils.isEmpty(user.getLimitIps()) ){
                    if(!user.getLimitIps().contains(request.getRemoteHost())){
                        model.addAttribute("error", "IP鉴权不通过");
                        return  "/index";
                    }
                }
            }else{
                if(StringUtils.isEmpty(user.getSessionId())){
                   // request.getSession().
                }
                user.setUpdateTime(new Date());
                user.setLimitIps(request.getRemoteHost());
                user.setSessionId(request.getSession().getId());
                userMapper.updateByPrimaryKeySelective(user);
            }

            if(2==user.getUserType()){
              return  "redirect:/admin/desktop";
            }
           return "redirect:/admin/file/list";
        }
    }
}
