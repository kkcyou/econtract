package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

@Data
public class RelativeRespVO {
    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 单位名称
     */
    private String companyName;

    /**
     * 信用代码
     */
    private String creditCode;
}
