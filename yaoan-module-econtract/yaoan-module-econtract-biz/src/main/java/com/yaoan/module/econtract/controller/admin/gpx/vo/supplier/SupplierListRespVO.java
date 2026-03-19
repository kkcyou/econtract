package com.yaoan.module.econtract.controller.admin.gpx.vo.supplier;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/6/11 16:44
 */
@Data
public class SupplierListRespVO {

    /**
     * 供应商id
     */
    private String supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 联系人姓名
     */
    private String personName;
    /**
     * 供应商手机号码
     */
    private String supplierTelephone;


    /**
     * 供应商联系地址
     */
    private String supplierAddress;


}
