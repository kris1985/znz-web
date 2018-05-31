package com.znz.util;

import com.znz.model.SubCategory;
import com.znz.vo.CategoryInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public  static boolean isSortByName(String firstSelectedId){
        if("3610".equals(firstSelectedId)){
            return true;
        }
        return false;
    }

    public  static String getNumbers(String content) {
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        String a = "品牌_Nike_2018年05月18日_0132_13_17.jpg";
        Matcher m = p.matcher(a);
        if(content==null){
            return "0";
        }
        String ret =  m.replaceAll("").trim();
        return ret;
    }

    public static void main(String[] args) {

    }
}
