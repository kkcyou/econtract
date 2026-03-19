package com.yaoan.module.econtract.api.gcy.zcd.dto;

import lombok.Data;

/**
 * @description: 供应商统一社会信用代码查看回款账号信息
 * @author: doujiale
 * @date: 2024/11/27 14:04
 */
@Data
public class BankAccountInfoDTO {

    /**
     * 公告唯一标识
     */
    private String bidCode;
    /**
     * 标段唯一标识
     */
    private String bidItemCode;
    /**
     * 项目编号
     */
    private String projectCode;
    /**
     * 包/标段编号
     */
    private String packageCode;
    /**
     * 供应商统一社会信用代码
     */
    private String secondRoleCode;
    /**
     * 中标项目名称
     */
    private String projectName;
    /**
     * 供应商名称
     */
    private String secondRoleName;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 回款账号
     */
    private String bankAccount;
    /**
     * 开户行
     */
    private String openBankName;


}
