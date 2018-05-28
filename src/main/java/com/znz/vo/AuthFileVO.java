package com.znz.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/5.
 */

@Getter
@Setter
public class AuthFileVO {

    private String authId;
    private String authName;
    private int checked;
    private String checkBox;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthFileVO that = (AuthFileVO) o;

        return authId.equals(that.authId);
    }

    @Override
    public int hashCode() {
        return authId.hashCode();
    }


    public static void main(String[] args) {
        System.out.println("2#f#d".replaceAll("#","."));
        System.out.println("2#f#d".replaceAll("\\.","#"));
    }
}
