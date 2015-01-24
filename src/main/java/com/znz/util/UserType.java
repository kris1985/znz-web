package com.znz.util;

import lombok.Getter;

/**
 * Created by Administrator on 2015/1/25.
 */
public enum UserType {

    NORMAL(1,"普通会员"),
    ADMIN(2,"管理员");

    @Getter
    private Integer type;
    @Getter
    private String typeName;


    UserType(Integer type,String typeName){
        this.type = type;
        this.typeName = typeName;
    }

    // 普通方法
    public static String getTypeName(int index) {
        for (UserType c : UserType.values()) {
            if (c.getType() == index) {
                return c.typeName;
            }
        }
        return null;
    }
}
