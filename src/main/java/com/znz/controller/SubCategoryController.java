package com.znz.controller;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.alibaba.fastjson.JSON;
import com.znz.dao.PictureCategoryMapper;
import com.znz.dao.PictureMapper;
import com.znz.dao.SubCategoryMapper;
import com.znz.model.Picture;
import com.znz.model.SubCategory;
import com.znz.model.UserAuth;
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
    public String showCategory(HttpServletRequest request,QueryParam queryParam, Model model){
        if(queryParam.getCurrentPage() == null){
            queryParam.setCurrentPage(1);
        }
        if(queryParam.getPageSize() == null){
            queryParam.setPageSize(40);
        }
        HttpSession session = request.getSession();
        List<SubCategory> subCategories = subCategoryMapper.selectAll(null);
        if(! (Boolean) session.getAttribute(Constants.ADMIN_FLAG)){  //不是admin
            UserSession userSession = (UserSession)session.getAttribute(Constants.USER_SESSION);
           if(CollectionUtils.isEmpty(userSession.getUserAuths())){
               userSession.setUserAuths(new ArrayList<>());
            }
            List<Integer> auths =userSession.getUserAuths().stream().map(s->Integer.parseInt(s.getAuthId())).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(auths)){
                subCategories = subCategories.stream().filter(s-> s.getCategoryLevel()!=0 || auths.contains(s.getId())).collect(Collectors.toList());
            }else {
                subCategories = new ArrayList<>();
            }
        }
        List<SubCategoryVO> subCategoryVOs = new ArrayList<>();
        if(!CollectionUtils.isEmpty(subCategories) ){
            SubCategoryVO subCategoryVO ;
            for(SubCategory subCategory:subCategories){
                if(queryParam.getFirstSelectedId()==null && subCategory.getCategoryLevel() == 0){
                    queryParam.setFirstSelectedId(String.valueOf(subCategory.getId()));
                }
                if(queryParam.getSecondSelectedId()==null && String.valueOf(subCategory.getParentId()).equals(queryParam.getFirstSelectedId()) ){
                    queryParam.setSecondSelectedId(String.valueOf(subCategory.getId()));
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

        final String  temp = queryParam.getSecondSelectedId();
        Set<Integer> forthCategorys = new HashSet<>();
        List<Set<Integer>> categoryConditions = new ArrayList<>();
        if(StringUtils.isEmpty(queryParam.getFourthSelectedId())) {
            Set<Integer> thirdCategorys =    subCategories.stream().filter(s-> String.valueOf(s.getParentId()).equals(temp) ).map(s->s.getId()).collect(Collectors.toSet());//三级类
            Set<Integer> set =  subCategories.stream().filter(s->thirdCategorys.contains(s.getParentId())).map(s->s.getId()).collect(Collectors.toSet()) ;//根据3级别类查找4级类
            categoryConditions.add(set);
        }else{
            forthCategorys = new HashSet(Arrays.asList( queryParam.getFourthSelectedId().split("[,;]")));
            String[] ids = queryParam.getFourthSelectedId().split("[;]");
            Set<Integer> set ;
            for(String x:ids){
                set = new HashSet<>();
                String[] arr = x.split(",");
                for(String item:arr){
                    if(StringUtils.isNumeric(item)){
                        set.add(Integer.parseInt(item));
                    }
                }
                if(!CollectionUtils.isEmpty(set)){
                    categoryConditions.add(set);
                }

            }

        }
        PageParameter pageParameter = new PageParameter(queryParam.getCurrentPage(), queryParam.getPageSize());
        FileQueryVO fileQueryVO = new FileQueryVO();
        model.addAttribute("currentPage",queryParam.getCurrentPage());
        model.addAttribute("totalPage",0);
        if(!CollectionUtils.isEmpty(categoryConditions) && categoryConditions.stream().allMatch(s->s.size()>0)){
            fileQueryVO.setPage(pageParameter);
            fileQueryVO.setCategoryConditions(categoryConditions);
            List<Picture> pictures =  pictureMapper.selectByPage(fileQueryVO);
            int totalPage = (pageParameter.getTotalCount() + pageParameter.getPageSize() - 1)
                    / pageParameter.getPageSize();
            model.addAttribute("totalPage",totalPage);
            model.addAttribute("totalCount",pageParameter.getTotalCount());
            model.addAttribute("pictures",pictures);
        }
        model.addAttribute("subCategoryVOs",subCategoryVOs);
        model.addAttribute("firstSelectedId",queryParam.getFirstSelectedId());
        model.addAttribute("secondSelectedId",queryParam.getSecondSelectedId());
        model.addAttribute("thirdSelectedId",queryParam.getThirdSelectedId());
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
        subCategoryVOs.stream().sorted(Comparator.comparing(SubCategoryVO::getSortId));
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
