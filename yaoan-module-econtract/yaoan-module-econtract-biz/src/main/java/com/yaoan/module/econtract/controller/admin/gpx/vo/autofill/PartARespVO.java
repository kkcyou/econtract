package com.yaoan.module.econtract.controller.admin.gpx.vo.autofill;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/6/2 22:52
 */
@Data
public class PartARespVO {

    /**
     * 甲方(公章)
     */
    private String partyACompany;

    /**
     * 法定代表人
     */
    private String lawName;
    /**
     * 甲方纳税人识别号
     */
    private String partyATaxpayerId;

    /**
     * 甲方地址
     */
    private String partyAAddress;

    /**
     * 甲方联系方式
     */
    private String partyAContact;
}
