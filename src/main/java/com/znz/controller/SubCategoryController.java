package com.znz.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.fastjson.JSON;
import com.znz.dao.SubCategoryMapper;
import com.znz.model.SubCategory;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.znz.dao.CategoryMapper;
import com.znz.model.Category;
import com.znz.util.Constants;
import com.znz.util.PermissionUtil;
import com.znz.vo.*;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by huangtao on 2015/1/22.
 */

@Slf4j
@Controller
@RequestMapping("/admin/subCategory")
public class SubCategoryController {

    @Resource
    private SubCategoryMapper subCategoryMapper;

    @Resource
    private CategoryMapper categoryMapper;




    @RequestMapping(value = "/list")
    public @ResponseBody JqGridData<SubCategoryVO> list2(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") String page,
                                                    @RequestParam(value = "rows", defaultValue = "10") String rows,
                                                    @RequestParam(value = "sidx", required = false) String sidx,
                                                    @RequestParam(value = "sord", required = false) String sord,
                                                    @RequestParam(value = "filters", required = false) String filters) {
        if (!PermissionUtil.checkPermisson(request)) {
            throw new RuntimeException("无权访问");
        }
        PageParameter pageParameter = new PageParameter(Integer.parseInt(page), Integer.parseInt(rows));
       /* if(StringUtils.isNotEmpty(filters)){
            SearchFilter searchFilter = JSON.parseObject(filters, SearchFilter.class);
            List<SearchField> rules = searchFilter.getRules();
            for (SearchField field:rules){
                if(StringUtils.isEmpty(field.getData())){
                    continue;
                }
                if(field.getField().equals("userName")){
                    userQueryVO.setUserName("%" + field.getData() + "%");
                }else if(field.getField().equals("company")){
                    userQueryVO.setCompany("%" + field.getData() + "%");
                }
            }
        }*/
        List<SubCategory> subCategories = subCategoryMapper.selectAll();
        int total = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                    / pageParameter.getPageSize();
        List<Category> categories = categoryMapper.selectAll();
        List<SubCategoryVO> subCategoryVOs = new ArrayList<>();
        Map<Integer,String> map = new HashedMap();
        if(!CollectionUtils.isEmpty(subCategories) && !CollectionUtils.isEmpty(categories)){
            for(Category category:categories){
                map.put(category.getId(),category.getName());
            }
            for(SubCategory subCategory:subCategories){
                SubCategoryVO subCategoryVO = new SubCategoryVO();
                subCategoryVO.setId(String.valueOf(subCategory.getId()));
                subCategoryVO.setName(subCategory.getName());
                subCategoryVO.setParentId(subCategory.getParentId());
                subCategoryVO.setSortId(subCategory.getSortId());
                subCategoryVO.setParentName(map.get(subCategory.getParentId()));
                subCategoryVOs.add(subCategoryVO);
            }
        }
        JqGridData jqGridData = new JqGridData(total, pageParameter
            .getCurrentPage(),pageParameter.getTotalCount(), subCategoryVOs);
        return jqGridData;
    }

    @RequestMapping(value = "/listCategory")
    public @ResponseBody String listCategory(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setCharacterEncoding("UTF-8");
        if (!PermissionUtil.checkPermisson(request)) {
            throw new RuntimeException("无权访问");
        }
        List<Category> categories = categoryMapper.selectAll();
        StringBuffer stringBuffer = new StringBuffer("<select>");
        if(!CollectionUtils.isEmpty(categories)){
            for(Category category:categories){
                stringBuffer.append("<option value='").append(category.getId()).append("' >").append(new String(category.getName().getBytes("utf-8"),"iso-8859-1")).append("</option>");
            }
        }
        stringBuffer.append("</select>");
        return stringBuffer.toString();
    }

    @RequestMapping(value = "/getCategory")
    public @ResponseBody List<SubCategoryVO> getALLCategory(HttpServletRequest request, HttpServletResponse response){
        List<SubCategory> subCategories = subCategoryMapper.selectAll();
        List<Category> categories = categoryMapper.selectAll();
        List<SubCategoryVO> subCategoryVOs = new ArrayList<>();
        Map<Integer,String> map = new HashedMap();
        if(!CollectionUtils.isEmpty(subCategories) && !CollectionUtils.isEmpty(categories)){
            for(Category category:categories){
                map.put(category.getId(),category.getName());
            }
            for(SubCategory subCategory:subCategories){
                SubCategoryVO subCategoryVO = new SubCategoryVO();
                subCategoryVO.setId(String.valueOf(subCategory.getId()));
                subCategoryVO.setName(subCategory.getName());
                subCategoryVO.setParentId(subCategory.getParentId());
                subCategoryVO.setSortId(subCategory.getSortId());
                subCategoryVO.setParentName(map.get(subCategory.getParentId()));
                subCategoryVOs.add(subCategoryVO);
            }
        }
        return subCategoryVOs;
    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody ResultVO delete(HttpServletRequest request, @PathVariable int id) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
            Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!checkPermisson(userSession)) {
            resultVO.setMsg("无权限操作");
        }else{
            subCategoryMapper.deleteByPrimaryKey(id);
            resultVO.setCode(0);
        }
        return resultVO;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public @ResponseBody ResultVO edit(HttpServletRequest request, @Valid @ModelAttribute SubCategoryVO subCategoryVO) {
        if("add".equals(subCategoryVO.getOper())){
            return add(request,subCategoryVO);
        } else if("edit".equals(subCategoryVO.getOper())){
            return update(request, subCategoryVO);
        }else if("del".equals(subCategoryVO.getOper())){
            return delete(request, Integer.parseInt(request.getParameter("id")));
        }else {
            return null;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody ResultVO add(HttpServletRequest request, @Valid @ModelAttribute SubCategoryVO subCategoryVO) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
            Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!checkPermisson(userSession)) {
            resultVO.setMsg("无权限操作");
        }
        SubCategory temp = subCategoryMapper.selectByName(subCategoryVO.getName());
        if (temp != null) {
            resultVO.setMsg("类别名称已经存在，请使用新的类别名称");
        }else{
            SubCategory subCategory = new SubCategory();
            BeanUtils.copyProperties(subCategoryVO, subCategory);
            subCategory.setId(null);
            Integer maxSortId = subCategory.getSortId();
            if(maxSortId==null){
                maxSortId = subCategoryMapper.selectMaxSortId(Integer.parseInt(subCategoryVO.getParentName()));
            }
            subCategory.setSortId(maxSortId);
            subCategory.setParentId(Integer.parseInt(subCategoryVO.getParentName()));
            subCategoryMapper.insert(subCategory);
            resultVO.setCode(0);
        }
        return resultVO;
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody ResultVO update(HttpServletRequest request, @Valid @ModelAttribute SubCategoryVO subCategoryVO) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
            Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!checkPermisson(userSession)) {
            resultVO.setMsg("无权限操作");
        } else{
            SubCategory subCategory = new SubCategory();
            subCategory.setSortId(subCategoryVO.getSortId());
            subCategory.setName(subCategoryVO.getName());
            subCategory.setId(Integer.parseInt(subCategoryVO.getId()));
            subCategory.setParentId(Integer.parseInt(subCategoryVO.getParentName()));//特殊处理
            subCategoryMapper.updateByPrimaryKeySelective(subCategory);
            resultVO.setCode(0);
        }
        return resultVO;
    }


    private boolean checkPermisson(UserSession userSession) {
        if (userSession.getUser().getUserType() != 2 && userSession.getUser().getUserType() != 3) {
            return false;
        }
        return true;
    }
}
