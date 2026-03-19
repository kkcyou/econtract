package com.yaoan.module.system.controller.admin.auth.shucai.vo;

import lombok.Data;

import java.util.List;

/**
 * UserInfoRespVO
 */
@Data
public class UserInfoRespVo {
    /**
     * 用户的CA信息
     */
    private String caUniCode;
    /**
     * 用户的认证类型
     */
    private long identityType;
    /**
     * 用户的登录类型（1：站内登录，2：站外登录）
     */
    private long loginType;
    /**
     * 用户的系统类型
     */
    private long systemType;
    /**
     * 用户登录账号
     */
    private String userAccount;
    /**
     * 用户邮箱地址
     */
    private String userEmail;
    /**
     * 用户的唯一标识
     */
    private String userId;
    /**
     * 用户身份证号
     */
    private String userIdCard;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户状态，1为可用
     */
    private long userStatus;
    /**
     * 用户手机号
     */
    private String userTel;
    /**
     * 用户的业务类型
     */
    private List<UserTypeInfo> userTypeInfos;
    /**
     * 用户当前的业务类型
     */
    private Object userTypeNow;
}
