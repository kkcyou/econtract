package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: doujl
 * @date: 2023/11/28 11:46
 */
@Data
public class SingleNoticeSupplierDTO implements Serializable {

    private static final long serialVersionUID = 5458717836455364333L;
    /**
     * 供应商唯一识别码(自有交易平台使用，第三方平台为NULL)
     */
    private String supplierGuid;

    /**
     * 供应商社会信用代码
     */
    private String supplierLicense;

    /**
     * 供应商名称
     */
    private String supplierName;
}
