package com.znz.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.znz.dao.CategoryMapper;
import com.znz.model.Category;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.znz.dao.UserMapper;
import com.znz.model.User;
import com.znz.util.Constants;
import com.znz.util.PermissionUtil;
import com.znz.vo.*;

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




    @RequestMapping(value = "/list")
    public @ResponseBody JqGridData<Category> list2(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") String page,
                                                    @RequestParam(value = "rows", defaultValue = "10") String rows,
                                                    @RequestParam(value = "sidx", required = false) String sidx,
                                                    @RequestParam(value = "sord", required = false) String sord,
                                                    @RequestParam(value = "filters", required = false) String filters) {
        if (!PermissionUtil.checkPermisson(request)) {
            throw new RuntimeException("无权访问");
        }
        UserQueryVO userQueryVO = new UserQueryVO();
        PageParameter pageParameter = new PageParameter(Integer.parseInt(page), Integer.parseInt(rows));

        List<Category> categories = categoryMapper.selectAll();
        int total = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                    / pageParameter.getPageSize();
        JqGridData jqGridData = new JqGridData(total, pageParameter
            .getCurrentPage(),pageParameter.getTotalCount(), categories);
        return jqGridData;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody ResultVO delete(HttpServletRequest request, @PathVariable int id) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
            Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!checkPermisson(userSession)) {
            resultVO.setMsg("无权限操作");
        }else{
            categoryMapper.deleteByPrimaryKey(id);
            resultVO.setCode(0);
        }
        return resultVO;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public @ResponseBody ResultVO edit(HttpServletRequest request, @Valid @ModelAttribute CategoryVO categoryVO) {
        if("add".equals(categoryVO.getOper())){
            return add(request,categoryVO);
        } else if("edit".equals(categoryVO.getOper())){
            return update(request, categoryVO);
        }else if("del".equals(categoryVO.getOper())){
            return delete(request, Integer.parseInt(request.getParameter("id")));
        }else {
            return null;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody ResultVO add(HttpServletRequest request, @Valid @ModelAttribute CategoryVO categoryVO) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
            Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!checkPermisson(userSession)) {
            resultVO.setMsg("无权限操作");
        }
        Category temp = categoryMapper.selectByName(categoryVO.getName());
        if (temp != null) {
            resultVO.setMsg("类别名称已经存在，请使用新的类别名称");
            throw new RuntimeException("类别名称已经存在，请使用新的类别名称");
        }else{
            Category category = new Category();
            BeanUtils.copyProperties(categoryVO, category);
            category.setId(null);
            Integer maxSortId = categoryMapper.selectMaxSortId();
            if(maxSortId==null){
                maxSortId = 0;
            }
            category.setSortId(null);
            category.setSortId(maxSortId);
            categoryMapper.insert(category);
            resultVO.setCode(0);
        }
        return resultVO;
    }

  /*  @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String modify(@PathVariable int id, Model model) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        model.addAttribute("category", category);
        return "/admin/userUpdate";
    }
*/
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody ResultVO update(HttpServletRequest request, @Valid @ModelAttribute CategoryVO categoryVO) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
            Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!checkPermisson(userSession)) {
            resultVO.setMsg("无权限操作");
        } else{
            Category category = new Category();
            category.setSortId(categoryVO.getSortId());
            category.setName(categoryVO.getName());
            category.setId(Integer.parseInt(categoryVO.getId()));
            categoryMapper.updateByPrimaryKeySelective(category);
            resultVO.setCode(0);
        }
        return resultVO;
    }

/*
    @RequestMapping(value = "/auths", method = RequestMethod.GET)
    public @ResponseBody List<AuthFileVO> lisAlltAuth(HttpServletRequest request) {
        String rootPath = request.getSession().getServletContext()
            .getRealPath(Constants.UPLOAD_ROOT_PATH);
        List<AuthFileVO> auths = getAuth(rootPath);
        return auths;
    }

    private List<AuthFileVO> getAuth(String rootPath) {
        File root = new File(rootPath);
        File[] files = root.listFiles();
        List<AuthFileVO> auths = new ArrayList<AuthFileVO>();
        if (files != null && files.length > 0) {
            AuthFileVO vo = null;
            for (File f : files) {
                vo = new AuthFileVO();
                vo.setAuthName(f.getName());
                auths.add(vo);
            }
        }
        return auths;
    }*/

    private boolean checkPermisson(UserSession userSession) {
        if (userSession.getUser().getUserType() != 2 && userSession.getUser().getUserType() != 3) {
            return false;
        }
        return true;
    }
}
