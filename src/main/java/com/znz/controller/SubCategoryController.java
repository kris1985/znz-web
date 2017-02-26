package com.znz.controller;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.fastjson.JSON;
import com.znz.dao.PictureCategoryMapper;
import com.znz.dao.PictureMapper;
import com.znz.dao.SubCategoryMapper;
import com.znz.model.Picture;
import com.znz.model.SubCategory;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private SubCategoryMapper     subCategoryMapper;

    @Resource
    private CategoryMapper        categoryMapper;

    @Resource
    private PictureMapper         pictureMapper;

    @Resource
    private PictureCategoryMapper pictureCategoryMapper;

    @RequestMapping(value = "/showCategory")
    public String showCategory(String firstSelectedId,String secondSelectedId,String thirdSelectedId,String fourthSelectedId, Model model){
        List<SubCategory> subCategories = subCategoryMapper.selectAll(null);
        List<SubCategoryVO> subCategoryVOs = new ArrayList<>();
        if(!CollectionUtils.isEmpty(subCategories) ){
            SubCategoryVO subCategoryVO ;
            for(SubCategory subCategory:subCategories){
                if(firstSelectedId==null && subCategory.getCategoryLevel() == 0){
                    firstSelectedId = String.valueOf(subCategory.getId());
                }
                if(secondSelectedId==null && String.valueOf(subCategory.getParentId()).equals(firstSelectedId) ){
                    secondSelectedId = String.valueOf(subCategory.getId());
                }
                subCategoryVO = new SubCategoryVO();
                subCategoryVO.setId(String.valueOf(subCategory.getId()));
                subCategoryVO.setName(subCategory.getName());
                subCategoryVO.setParentId(subCategory.getParentId());
                subCategoryVO.setSortId(subCategory.getSortId());
                subCategoryVO.setAllFlag(subCategory.getAllFlag());
                subCategoryVO.setCategoryLevel(subCategory.getCategoryLevel());
                if(subCategory.getCategoryLevel() == 2){
                    subCategoryVO.setChildrens(getchildrens(subCategories,subCategory.getId()));
                }
                subCategoryVOs.add(subCategoryVO);
            }
        }

        final String  temp = secondSelectedId;
        Set<Integer> forthCategorys = new HashSet<>();
        List<String> categoryConditions = new ArrayList<>();
        if(StringUtils.isEmpty(fourthSelectedId)) {
            Set<Integer> thirdCategorys =    subCategories.stream().filter(s-> String.valueOf(s.getParentId()).equals(temp) ).map(s->s.getId()).collect(Collectors.toSet());//三级类
            //Map<Integer,List<SubCategory>> map = subCategories.stream().filter(s -> thirdCategorys.contains(s.getParentId())).collect(Collectors.groupingBy(SubCategory::getParentId));
            Set set =  subCategories.stream().filter(s->thirdCategorys.contains(s.getParentId())).map(s->s.getId()).collect(Collectors.toSet()) ;//根据3级别类查找4级类
            String s = "";
            /*for(List<SubCategory> list :map.values()){
               s +=  StringUtils.join(list.stream().map(x->x.getId()).collect(Collectors.toList()), ",");
            }*/
            categoryConditions.add(StringUtils.join(set,","));
        }else{
            String[] ids = fourthSelectedId.split("[,;]");
            forthCategorys = new HashSet(Arrays.asList(ids));
            //categoryConditions = new ArrayList(fourthSelectedId.split(",") Arr);
            categoryConditions = Arrays.asList(fourthSelectedId.split(";"));
        }
        PageParameter pageParameter = new PageParameter(1, 40);
        FileQueryVO fileQueryVO = new FileQueryVO();
        if(!CollectionUtils.isEmpty(categoryConditions) ){
            fileQueryVO.setPage(pageParameter);
            fileQueryVO.setCategoryConditions(categoryConditions);
            List<Picture> pictures =  pictureMapper.selectByPage(fileQueryVO);
            int totalPage = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                    / pageParameter.getPageSize();
            int currentPage = pageParameter.getCurrentPage();
            model.addAttribute("currentPage",currentPage);
            model.addAttribute("totalPage",totalPage);
            model.addAttribute("totalCount",pageParameter.getTotalCount());
            model.addAttribute("pictures",pictures);
        }
        model.addAttribute("subCategoryVOs",subCategoryVOs);
        model.addAttribute("firstSelectedId",firstSelectedId);
        model.addAttribute("secondSelectedId",secondSelectedId);
        model.addAttribute("thirdSelectedId",thirdSelectedId);
        model.addAttribute("fourthSet",forthCategorys);
        return "admin/showCategory";
    }

    private List<SubCategoryVO> getchildrens(List<SubCategory> subCategories, int parentId) {
        List<SubCategoryVO> subCategoryVOs = new ArrayList<>();
        SubCategoryVO subCategoryVO;
        for (SubCategory subCategory : subCategories) {
            if (subCategory.getParentId() == parentId) {
                subCategoryVO = new SubCategoryVO();
                subCategoryVO.setId(String.valueOf(subCategory.getId()));
                subCategoryVO.setName(subCategory.getName());
                subCategoryVO.setParentId(subCategory.getParentId());
                subCategoryVO.setSortId(subCategory.getSortId());
                subCategoryVO.setAllFlag(subCategory.getAllFlag());
                subCategoryVO.setCategoryLevel(subCategory.getCategoryLevel());
                subCategoryVOs.add(subCategoryVO);
            }
        }
        return subCategoryVOs;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody ResultVO delete(HttpServletRequest request, @PathVariable int id) {
        UserSession userSession = (UserSession) request.getSession()
            .getAttribute(Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!checkPermisson(userSession)) {
            resultVO.setMsg("无权限操作");
        } else {
            subCategoryMapper.deleteByPrimaryKey(id);
            resultVO.setCode(0);
        }
        return resultVO;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(HttpServletRequest request,
                      @Valid @ModelAttribute SubCategoryVO subCategoryVO) {
        UserSession userSession = (UserSession) request.getSession()
            .getAttribute(Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!checkPermisson(userSession)) {
            resultVO.setMsg("无权限操作");
        }
        SubCategory subCategory = new SubCategory();
        BeanUtils.copyProperties(subCategoryVO, subCategory);
        subCategory.setId(null);
        subCategory.setAllFlag("N");
        subCategoryMapper.insert(subCategory);
        return "redirect:/admin/subCategory/showCategory";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(HttpServletRequest request,
                         @Valid @ModelAttribute SubCategoryVO subCategoryVO) {
        UserSession userSession = (UserSession) request.getSession()
            .getAttribute(Constants.USER_SESSION);
        ResultVO resultVO = new ResultVO();
        if (!checkPermisson(userSession)) {
            resultVO.setMsg("无权限操作");
        } else {
            SubCategory subCategory = new SubCategory();
            subCategory.setName(subCategoryVO.getName());
            subCategory.setId(Integer.parseInt(subCategoryVO.getId()));
            subCategoryMapper.updateByPrimaryKeySelective(subCategory);
        }
        return "redirect:/admin/subCategory/showCategory";
    }

    private boolean checkPermisson(UserSession userSession) {
        if (userSession.getUser().getUserType() != 2 && userSession.getUser().getUserType() != 3) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    public @ResponseBody ResultVO sort(HttpServletRequest request, String param) {
        ResultVO resultVO = new ResultVO();
        String[] array = param.split(";");
        for (String s : array) {
            SubCategory subCategory = new SubCategory();
            String[] subArray = s.split(":");
            subCategory.setId(Integer.parseInt(subArray[0]));
            subCategory.setSortId(Integer.parseInt(subArray[1]));
            subCategoryMapper.updateByPrimaryKeySelective(subCategory);
        }
        resultVO.setCode(0);
        return resultVO;
    }

    @RequestMapping(value = "/validName", method = RequestMethod.POST)
    public @ResponseBody ResultVO validName(String name){
        ResultVO resultVO = new ResultVO();
        SubCategory temp = subCategoryMapper.selectByName(name);
        if (temp != null) {
            resultVO.setMsg("类别名称已经存在，请使用其它类别名称");
        } else {
            resultVO.setCode(0);
        }
        return resultVO;
    }

}
