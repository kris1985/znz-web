package com.znz.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Created by Administrator on 2015/1/25.
 */
@Data
public class UserAddVO {

    private String userName;

    private String pwd;

    private String company;

    private int limitIpFlag;

    private String limitIps;

    private int accessFlag;

    private int maxDownloadTimes;

    private String phone;

    private int userId;

    private List<String> auths;

    private Integer recommendFlag;

    private String device;

    private Integer userType;

    private WatermarkVO watermarkVO;
}
