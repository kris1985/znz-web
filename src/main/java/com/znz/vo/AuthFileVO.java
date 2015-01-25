package com.znz.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/1/25.
 */
@Data
public class AuthFileVO {

    private String authName;
    private int checked;
    private String checkBox;

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(null == obj) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        AuthFileVO user = (AuthFileVO) obj;
        if(!authName.equals(user.authName)) {
            return false;
        }
        return true;
    }


    public static void  main(String args[]){

        AuthFileVO vo = new AuthFileVO();
        vo.setAuthName("TEST");

        AuthFileVO vo1 = new AuthFileVO();
        vo1.setAuthName("TEST");
        vo1.setCheckBox("ww");

        List<AuthFileVO> list = new ArrayList<AuthFileVO>();
        list.add(vo);

        System.out.println(list.contains(vo1));
    }
}
