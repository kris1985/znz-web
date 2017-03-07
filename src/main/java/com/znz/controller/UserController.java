package com.znz.controller;

import java.io.File;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.znz.dao.SubCategoryMapper;
import com.znz.dao.UserAuthMapper;
import com.znz.model.SubCategory;
import com.znz.util.PermissionUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.znz.dao.UserMapper;
import com.znz.model.User;
import com.znz.model.UserAuth;
import com.znz.util.Constants;
import com.znz.util.UserType;
import com.znz.vo.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by huangtao on 2015/1/22.
 */

@Slf4j
@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Resource
    private UserMapper     userMapper;

    @Resource
    SubCategoryMapper subCategoryMapper;

    @Resource
    private UserAuthMapper userAuthMapper;



    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody List<UserVO> list() {
        List<User> users = userMapper.selectAllUser(1);
        List<UserVO> userVOs = null;
        UserVO userVO = null;
        if (!CollectionUtils.isEmpty(users)) {
            userVOs = new ArrayList<UserVO>();
            for (User user : users) {
                userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                userVOs.add(userVO);
            }
        }

        return userVOs;
    }



    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public @ResponseBody ResultVO delete(HttpServletRequest request, @PathVariable int userId) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
            Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!checkPermisson(userSession)) {
            resultVO.setMsg("无权限操作");
        }else{
            userMapper.deleteByPrimaryKey(userId);
            resultVO.setCode(0);
        }
        return resultVO;
    }


    @RequestMapping(value = "/add" , method= RequestMethod.POST)
    public String  add(HttpServletRequest request, @Valid @ModelAttribute UserAddVO userAddVO, BindingResult br, Model model){
        UserSession userSession =  (UserSession)request.getSession().getAttribute(Constants.USER_SESSION);
        if (!checkPermisson(userSession)){
            throw  new RuntimeException("无权限操作");
        }
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
        BeanUtils.copyProperties(userAddVO, user);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setLastLoginTime(new Date());
        user.setUserType(UserType.NORMAL.getType());//普通用户
        user.setDownloadPerDay(0);
        user.setDownloadTotal(0);
        if(userAddVO.getRecommendFlag()==null){
            user.setRecommendFlag(0);
        }
        if(userAddVO.getWatermarkVO()!=null){
            user.setWatermark(JSON.toJSONString(userAddVO.getWatermarkVO()));
        }
        userMapper.insert(user);
        List<String> auths  = userAddVO.getAuths();
        if(!CollectionUtils.isEmpty(auths)){
            for (String auth:auths){
                UserAuth userAuth = new UserAuth();
                userAuth.setUserId(user.getUserId());
                userAuth.setCreateTime(new Date());
                userAuth.setAuthId(auth);
                userAuthMapper.insert(userAuth);
            }
        }
        return "redirect:/admin/user/users";
    }

    @RequestMapping(value = "/update/{userId}", method = RequestMethod.GET)
    public String modify(@PathVariable int userId, Model model) {
        User user = userMapper.selectByPrimaryKey(userId);
        model.addAttribute("user", user);
        if(user.getWatermark()!=null){
            WatermarkVO watermarkVO = JSON.parseObject(user.getWatermark(),WatermarkVO.class);
            model.addAttribute("watermarkVO", watermarkVO);
        }
        return "/admin/userUpdate2";
    }

    @RequestMapping(value = "/update" , method= RequestMethod.POST)
    public String  update(HttpServletRequest request,@Valid @ModelAttribute UserAddVO userAddVO,BindingResult br,Model model){
        UserSession userSession =  (UserSession)request.getSession().getAttribute(Constants.USER_SESSION);
        if (!checkPermisson(userSession)){
            throw  new RuntimeException("无权限操作");
        }
        if (br.hasErrors()){
            model.addAttribute("br",br);
            return  "/admin/userAdd";
        }
        User user = new User();
        BeanUtils.copyProperties(userAddVO, user);
        user.setUpdateTime(new Date());
        if(userAddVO.getRecommendFlag()==null){
            user.setRecommendFlag(0);
        }
        if(userAddVO.getWatermarkVO()!=null){
            user.setWatermark(JSON.toJSONString(userAddVO.getWatermarkVO()));
        }
        List<String> auths  = userAddVO.getAuths();
        userMapper.updateByPrimaryKeySelective(user);
        if(!CollectionUtils.isEmpty(auths)){
            userAuthMapper.deleteByUserId(userAddVO.getUserId());
            for (String auth:auths){
                UserAuth userAuth = new UserAuth();
                userAuth.setUserId(userAddVO.getUserId());
                userAuth.setCreateTime(new Date());
                userAuth.setAuthId(auth);
                userAuthMapper.insert(userAuth);
            }
        }
        return "redirect:/admin/user/users";
    }

    @RequestMapping(value = "/auths/{userId}" , method= RequestMethod.GET)
      public @ResponseBody List<AuthFileVO> listAuthByUser(HttpServletRequest request,@PathVariable int userId){
        String rootPath = request.getSession().getServletContext().getRealPath(Constants.UPLOAD_ROOT_PATH);
        List<AuthFileVO> allAuths = getAuth();
        List<AuthFileVO> retAuths = new ArrayList<AuthFileVO>();
        List<UserAuth> userAuths = userAuthMapper.listByUserId(userId);
        if(!CollectionUtils.isEmpty(userAuths)){
            for (UserAuth userAuth:userAuths){
                AuthFileVO authFileVO = new AuthFileVO();
                SubCategory subCategory = subCategoryMapper.selectByPrimaryKey(Integer.parseInt(userAuth.getAuthId()));
               // log.info("-1--"+authFileVO.getAuthName());
                authFileVO.setAuthName(subCategory.getName());
                authFileVO.setChecked(1);
                authFileVO.setAuthId(userAuth.getAuthId());
                authFileVO.setCheckBox("<input type=\"checkbox\"  name=\"auths\" checked value="+userAuth.getAuthId()+">"+authFileVO.getAuthName());
                if(!retAuths.contains(authFileVO)){
                    retAuths.add(authFileVO);
                }
            }
        }
        if(!CollectionUtils.isEmpty(allAuths)){
            for (AuthFileVO vo:allAuths){
                //log.info("-2--"+vo.getAuthName());
                vo.setCheckBox("<input type=\"checkbox\"  name=\"auths\"  value=" + vo.getAuthId() + ">" + vo.getAuthName());
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

    @RequestMapping(value = "/auths", method = RequestMethod.GET)
    public @ResponseBody List<AuthFileVO> lisAlltAuth(HttpServletRequest request) {
        List<AuthFileVO> auths = getAuth();
        return auths;
    }

    private List<AuthFileVO> getAuth() {
        List<AuthFileVO> auths = new ArrayList<AuthFileVO>();
        List<SubCategory> list = subCategoryMapper.selectAll(0);
        AuthFileVO vo = null;
        for (SubCategory f : list) {
            vo = new AuthFileVO();
            vo.setAuthId(String.valueOf(f.getId()));
            vo.setAuthName(f.getName());
            auths.add(vo);
        }
        return auths;
    }



    private boolean checkPermisson(UserSession userSession) {
        if (userSession.getUser().getUserType() != 2 && userSession.getUser().getUserType() != 3) {
            return false;
        }
        return true;
    }
}
