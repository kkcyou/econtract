package com.yaoan.module.system.controller.admin.auth.shucai.vo;

public class UserTypeInfo {
    /**
     * 用户的业务类型
     */
    private String commonType;
    /**
     * 用户的业务类型状态（1：可以，2：禁用）
     */
    private String status;
    /**
     * 用户的系统类型
     */
    private String systemType;
    /**
     * 旧版的用户业务类型标识（已弃用）
     */
    private String userTypeId;
}