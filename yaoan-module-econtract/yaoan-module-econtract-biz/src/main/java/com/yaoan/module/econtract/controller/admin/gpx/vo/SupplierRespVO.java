package com.yaoan.module.econtract.controller.admin.gpx.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/31 13:58
 */
@Data
public class SupplierRespVO {

    /**
     * 供应商id
     */
    private String supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    private LocalDateTime updateTime;
}
