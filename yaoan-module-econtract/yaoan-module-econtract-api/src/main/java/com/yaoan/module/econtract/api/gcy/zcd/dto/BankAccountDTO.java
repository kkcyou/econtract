package com.yaoan.module.econtract.api.gcy.zcd.dto;

import lombok.Data;

/**
 * @description: 供应商统一社会信用代码查看回款账号信息
 * @author: doujiale
 * @date: 2024/11/27 14:04
 */
@Data
public class BankAccountDTO {
    /**
     * 公告唯一标识
     * （此时项目编号和标段编号为空)
     */
    private String bidCode;

    /**
     * 标段唯一标识(此时项目编号和标段编号为空)
     */
    private String bidItemCode;

    /**
     * 项目编号
     * （公告/标段唯一标识为空）
     */
    private String projectCode;

    /**
     * 包/标段编号
     * （公告/标段唯一标识为空）
     */
    private String packageCode;

    /**
     * 供应商统一社会信用代码
     */
    private String secondRoleCode;

    /**
     * token
     */
    private String access_token;



}
