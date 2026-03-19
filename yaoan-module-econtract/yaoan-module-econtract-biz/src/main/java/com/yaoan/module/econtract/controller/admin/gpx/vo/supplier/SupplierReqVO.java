package com.yaoan.module.econtract.controller.admin.gpx.vo.supplier;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/6/11 16:31
 */
@Data
public class SupplierReqVO {
    /**
     * 供应商ids
     */
    private List<String> supplierIds;

    /**
     * 供应商名称
     */
    private String supplierName;

}
