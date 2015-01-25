package com.znz.controller;

import com.znz.dao.UserAuthMapper;
import com.znz.dao.UserMapper;
import com.znz.model.User;
import com.znz.model.UserAuth;
import com.znz.util.Constants;
import com.znz.util.UserType;
import com.znz.vo.AuthFileVO;
import com.znz.vo.UserAddVO;
import com.znz.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.util.*;

/**
 * Created by huangtao on 2015/1/22.
 */

@Slf4j
@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserAuthMapper userAuthMapper;

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
        }
        //账号密码验证正确
        int limitIpFlag = user.getLimitIpFlag();
        //鉴权IP
        if(1==limitIpFlag && !StringUtils.isEmpty(user.getLimitIps()) && !user.getLimitIps().contains(request.getRemoteHost())){
                    model.addAttribute("error", "IP鉴权不通过");
                    return  "/index";
        }
        //鉴权通过
        if(StringUtils.isEmpty(user.getSessionId())){
           // request.getSession().
        }
        user.setUpdateTime(new Date());
        user.setLimitIps(request.getRemoteHost());
        user.setSessionId(request.getSession().getId());
        userMapper.updateByPrimaryKeySelective(user);
        request.getSession().setAttribute("user",user);
        //管理员
        if(2==user.getUserType()){
          return  "redirect:/admin/desktop";
        }
       return "redirect:/admin/file/list";

    }

    @RequestMapping(value = "/list" , method= RequestMethod.GET)
    public @ResponseBody List<User> list(){
        List<User> users = userMapper.selectAllUser(1);
        return  users;
    }

    @RequestMapping(value = "/delete/{userId}" , method= RequestMethod.GET)
    public String  delete(@PathVariable int userId){
        userMapper.deleteByPrimaryKey(userId);
        return "redirect:/admin/user/users";
    }

    @RequestMapping(value = "/add" , method= RequestMethod.POST)
    public String  add(@Valid @ModelAttribute UserAddVO userAddVO,BindingResult br,Model model){
        if (br.hasErrors()){
            model.addAttribute("br",br);
            return  "/admin/userAdd";
        }
       User temUser =  userMapper.selectByUser(userAddVO.getUserName());
        if(temUser!=null){
            model.addAttribute("error", "用户名已经存在，请使用新的用户名");
            return  "/admin/userAdd";
        }
        User user = new User();
        BeanUtils.copyProperties(userAddVO,user);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setUserType(UserType.NORMAL.getType());//普通用户
        user.setDownloadPerDay(0);
        user.setDownloadTotal(0);
        userMapper.insert(user);
        List<String> auths  = userAddVO.getAuths();
        if(!CollectionUtils.isEmpty(auths)){
            for (String auth:auths){
                UserAuth userAuth = new UserAuth();
                userAuth.setUserId(user.getUserId());
                userAuth.setUpdateTime(new Date());
                userAuth.setCreateTime(new Date());
                userAuth.setFilePath(auth);
                userAuthMapper.insert(userAuth);
            }
        }
        return "redirect:/admin/user/users";
    }

    @RequestMapping(value = "/update/{userId}" , method= RequestMethod.GET)
    public String  modify(@PathVariable int userId,Model model){
        User user = userMapper.selectByPrimaryKey(userId);
        model.addAttribute("user",user);
        return "/admin/userUpdate";
    }

    @RequestMapping(value = "/update" , method= RequestMethod.POST)
    public String  update(@Valid @ModelAttribute UserAddVO userAddVO,BindingResult br,Model model){
        if (br.hasErrors()){
            model.addAttribute("br",br);
            return  "/admin/userAdd";
        }
        User user = new User();
        BeanUtils.copyProperties(userAddVO, user);
        user.setUpdateTime(new Date());
        List<String> auths  = userAddVO.getAuths();
        userMapper.updateByPrimaryKeySelective(user);
        if(!CollectionUtils.isEmpty(auths)){
            userAuthMapper.deleteByUserId(userAddVO.getUserId());
            for (String auth:auths){
                UserAuth userAuth = new UserAuth();
                userAuth.setUserId(userAddVO.getUserId());
                userAuth.setUpdateTime(new Date());
                userAuth.setCreateTime(new Date());
                userAuth.setFilePath(auth);
                userAuthMapper.insert(userAuth);
            }
        }
        return "redirect:/admin/user/users";
    }

    @RequestMapping(value = "/auths/{userId}" , method= RequestMethod.GET)
      public @ResponseBody List<AuthFileVO> listAuthByUser(HttpServletRequest request,@PathVariable int userId){
        String rootPath = request.getSession().getServletContext().getRealPath(Constants.UPLOAD_ROOT_PATH);
        List<AuthFileVO> allAuths = getAuth(rootPath);
        List<AuthFileVO> retAuths = new ArrayList<AuthFileVO>();
        List<UserAuth> userAuths = userAuthMapper.listByUserId(userId);

        if(!CollectionUtils.isEmpty(userAuths)){
            for (UserAuth userAuth:userAuths){
                AuthFileVO authFileVO = new AuthFileVO();
                authFileVO.setAuthName(userAuth.getFilePath());
                authFileVO.setChecked(1);
                authFileVO.setCheckBox("<input type=\"checkbox\"  name=\"auths\" checked value="+userAuth.getFilePath()+">"+userAuth.getFilePath());
                if(!retAuths.contains(authFileVO)){
                    retAuths.add(authFileVO);
                }
            }
        }
        if(!CollectionUtils.isEmpty(allAuths)){
            for (AuthFileVO vo:allAuths){
                vo.setCheckBox("<input type=\"checkbox\"  name=\"auths\"  value="+vo.getAuthName()+">"+vo.getAuthName());
                vo.setChecked(0);
                boolean contains = retAuths.contains(vo);
                if(!contains){
                    retAuths.add(vo);
                }
            }
        }
        Collections.sort(retAuths,new Comparator<AuthFileVO>() {
            @Override
            public int compare(AuthFileVO o1, AuthFileVO o2) {
                if(o1.getChecked()==o2.getChecked()){
                    return 0;
                }else if(o1.getChecked()<o2.getChecked()){
                    return 1;
                }else {
                    return -1;
                }
            }
        });
        return  retAuths;
    }

    @RequestMapping(value = "/auths" , method= RequestMethod.GET)
    public @ResponseBody List<AuthFileVO> lisAlltAuth(HttpServletRequest request){
        String rootPath = request.getSession().getServletContext().getRealPath(Constants.UPLOAD_ROOT_PATH);
        List<AuthFileVO> auths = getAuth(rootPath);
        return  auths;
    }

    private List<AuthFileVO> getAuth(String rootPath) {
        File root = new File(rootPath);
        File[] files = root.listFiles();
        List<AuthFileVO> auths = new ArrayList<AuthFileVO>();
        if(files!=null&&files.length>0){
            AuthFileVO vo = null;
            for(File f : files){
                vo = new AuthFileVO();
                vo.setAuthName(f.getName());
                auths.add(vo);
            }
        }
        return auths;
    }
}
