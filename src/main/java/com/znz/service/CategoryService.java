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

    public Integer getSecondCategoryBy4(Integer fourthCagotyCode){
       SubCategory fourthCategory = subCategoryMapper.selectByPrimaryKey(fourthCagotyCode);//四级栏目
        if(fourthCategory == null){
            return null;
        }
        return getSecondCategoryBy3(fourthCategory.getParentId());
    }

    public Integer getSecondCategoryBy3(Integer thirdCategoryCode){
        SubCategory thirdCategory = subCategoryMapper.selectByPrimaryKey(thirdCategoryCode);//三级栏目
        if(thirdCategory == null){
            return null;
        }
        SubCategory secondCategory =  subCategoryMapper.selectByPrimaryKey(thirdCategory.getParentId());//二级栏目
        if(secondCategory == null){
            return null;
        }
        return secondCategory.getId();
    }


    public Integer getPartionCodeBy2(Integer secondCategoryCode){
        SubCategory secondCategory = subCategoryMapper.selectByPrimaryKey(secondCategoryCode);//二级栏目
        if(secondCategory == null){
            return null;
        }
        return secondCategory.getPartionCode();
    }

    public Integer getPartionCodeBy3(Integer thirdCategoryCode){
        SubCategory thirdCategory = subCategoryMapper.selectByPrimaryKey(thirdCategoryCode);//三级栏目
        if(thirdCategory == null){
            return null;
        }
        return getPartionCodeBy2(thirdCategory.getParentId());
    }

    public Integer getPartionCodeBy4(Integer fourthCagotyCode){
        SubCategory thirdCategory = subCategoryMapper.selectByPrimaryKey(fourthCagotyCode);//四级栏目
        if(thirdCategory == null){
            return null;
        }
        return getPartionCodeBy3(thirdCategory.getParentId());
    }


    public Integer getParentId(Integer cagotyCode){
        SubCategory thirdCategory = subCategoryMapper.selectByPrimaryKey(cagotyCode);//四级栏目
        if(thirdCategory == null){
            return null;
        }
        return thirdCategory.getParentId();
    }



}
