package com.yaoan.module.system.mq.basic.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 接受接口传来的数据 辅助类
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class SupplierVO implements Serializable {
    private String id;
    private String supplyCn;
    private String supplyAddr;
    private String supplyEmail;
    private String orgCode;
    private String personName;
    private String personMobile;
    private String legalPerson;
    private String legalMobile;
    private String supplyFax;
    private String personEmail;
    private String personAddr;
    private String bankName;
    private String bankAccount;
    private String legalIdcardno;
    private String registeredAddress;
    /**
     * 第三方系统毕加索的orgid
     */
    private String bijiasuoOrgId;
    /**
     * 第三方系统ID，山东此字段存的是东软的ct_out_id，映射到卖场的t_s_supplier表的interfacecode字段
     */
    private String outSystemId;

}
