package com.znz.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.List;

/**
 * Created by Administrator on 2015/1/25.
 */
@Data
public class UserAddVO {

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank(message = "密码不能为空")
    private String pwd;

    @NotBlank(message = "公司名称不能为空")
    private String company;

    private Integer limitIpFlag;

    private String limitIps;

    private Integer accessFlag;

    private Integer maxDownloadTimes;

    private String phone;

    @NotEmpty(message = "请选择权限")
    private List<String> auths;
}
