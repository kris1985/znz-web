package com.znz.util;

import com.znz.model.SubCategory;
import com.znz.vo.CategoryInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/20.
 */
public class CategoryUtil {

    public static List<CategoryInfo> getChildren(Integer id, List<SubCategory> allcategories) {
        List<CategoryInfo> categoryInfos = new ArrayList<>();
        CategoryInfo categoryInfo ;
        for(SubCategory subCategory:allcategories){
            if(subCategory.getParentId().equals(id)){
                categoryInfo = new CategoryInfo();
                categoryInfo.setId(subCategory.getId());
                categoryInfo.setName(subCategory.getName());
                categoryInfo.setSortId(subCategory.getSortId());
                if(subCategory.getCategoryLevel()!=3){
                    categoryInfo.setChildrens(getChildren(categoryInfo.getId(),allcategories));
                }
                categoryInfos.add(categoryInfo);
            }
        }
        return categoryInfos;
    }
}
