package com.znz.controller;

import java.io.File;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.znz.dao.CategoryMapper;
import com.znz.model.Category;
import com.znz.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.znz.dao.UserAuthMapper;
import com.znz.dao.UserMapper;
import com.znz.model.User;
import com.znz.model.UserAuth;
import com.znz.util.Constants;
import com.znz.util.UserType;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by huangtao on 2015/1/22.
 */

@Slf4j
@Controller
@RequestMapping("/admin/category")
public class CategoryController {

    @Resource
    private CategoryMapper categoryMapper;




    @RequestMapping(value = "/list" , method= RequestMethod.GET)
    public @ResponseBody List<Category> list(){
        List<Category> categories = categoryMapper.selectAll();
        return  categories;
    }

    @RequestMapping(value = "/delete/{id}" , method= RequestMethod.GET)
    public String  delete(HttpServletRequest request,@PathVariable int id){
        UserSession userSession =  (UserSession)request.getSession().getAttribute(Constants.USER_SESSION);
        if (!checkPermisson(userSession)){
            throw  new RuntimeException("无权限操作");
        }
        categoryMapper.deleteByPrimaryKey(id);
        return "redirect:/admin/category/categorys";
    }

    @RequestMapping(value = "/add" , method= RequestMethod.POST)
    public String  add(HttpServletRequest request, @Valid @ModelAttribute CategoryVO categoryVO, BindingResult br, Model model){
        UserSession userSession =  (UserSession)request.getSession().getAttribute(Constants.USER_SESSION);
        if (!checkPermisson(userSession)){
            throw  new RuntimeException("无权限操作");
        }
        if (br.hasErrors()){
            model.addAttribute("br",br);
            return  "/admin/userAdd";
        }
        Category old =  categoryMapper.selectByName(categoryVO.getName());
        if(old!=null){
            model.addAttribute("error", "类型已经存在，请使用新的用户名");
            return  "/admin/userAdd";
        }
        Category c = new Category();
        BeanUtils.copyProperties(categoryVO,c);
        categoryMapper.insertSelective(c);
        return "redirect:/admin/cateogry/users";
    }

   /* @RequestMapping(value = "/update/{userId}" , method= RequestMethod.GET)
    public String  modify(@PathVariable int userId,Model model){
        Category category = categoryMapper.selectByPrimaryKey(userId);
        model.addAttribute("category",category);
        return "/admin/userUpdate";
    }

    @RequestMapping(value = "/update" , method= RequestMethod.POST)
    public String  update(HttpServletRequest request,@Valid @ModelAttribute CategoryVO categoryVO,BindingResult br,Model model){
        UserSession userSession =  (UserSession)request.getSession().getAttribute(Constants.USER_SESSION);
        if (!checkPermisson(userSession)){
            throw  new RuntimeException("无权限操作");
        }
        if (br.hasErrors()){
            model.addAttribute("br",br);
            return  "/admin/userAdd";
        }
        Category category = new Category();
        BeanUtils.copyProperties(categoryVO, category);
        categoryMapper.updateByPrimaryKeySelective(category);
        return "redirect:/admin/category/categorys";
    }
*/


    private boolean checkPermisson(UserSession userSession) {
        if( userSession.getUser().getUserType()!=2 && userSession.getUser().getUserType()!=3){
            return false;
        }
        return true;
    }
}
