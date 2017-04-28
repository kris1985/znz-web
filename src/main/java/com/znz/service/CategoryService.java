package com.znz.service;

import com.znz.dao.SubCategoryMapper;
import com.znz.model.SubCategory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */
@Component
public class CategoryService {

    @Resource
    private SubCategoryMapper subCategoryMapper;

    public Integer getSecondCategory(Integer fourthCagotyCode){
       SubCategory thirdCategory = subCategoryMapper.selectByPrimaryKey(fourthCagotyCode);//三级栏目
        if(thirdCategory == null){
            return null;
        }
        SubCategory secondCategory =  subCategoryMapper.selectByPrimaryKey(thirdCategory.getParentId());
        if(secondCategory == null){
            return null;
        }
        return secondCategory.getId();
    }



}
