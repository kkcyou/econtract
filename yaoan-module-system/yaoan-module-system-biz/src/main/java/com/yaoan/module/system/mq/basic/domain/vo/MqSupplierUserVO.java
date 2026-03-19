package com.yaoan.module.system.mq.basic.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class MqSupplierUserVO implements Serializable {

    //平台那边的表主键
    private String id;
    //用户id
    private String oauthId;
    //供应商id
    private String supplierId;
    //用户类型
    private Integer userType;
    //登录名
    private String accountName;
    //用户名
    private String userName;
    //密码
    private String password;

    private String mobile;

    private String email;

    private String idCard;

    private String mobilePhone;

}
