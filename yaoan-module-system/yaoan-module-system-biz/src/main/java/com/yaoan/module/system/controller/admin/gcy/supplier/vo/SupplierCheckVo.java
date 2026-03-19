package com.yaoan.module.system.controller.admin.gcy.supplier.vo;

import javax.validation.constraints.NotEmpty;

/**
 * SupplierCheckVO
 */
@lombok.Data
public class SupplierCheckVo {
    /**
     * 信用代码
     */
    @NotEmpty(message = "信用代码不能为空")
    private String orgCode;
    /**
     * 供应商名称
     */
    @NotEmpty(message = "供应商名称不能为空")
    private String supplyCn;
}