package com.yaoan.module.econtract.controller.admin.gpx.vo.file;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/7/4 16:05
 */
@Data
public class SaveFileAndCompanyReqVO {

    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 供应商id
     */
    private String supplierId;

    /**
     * 采购单位id
     */
    private String orgId;
}
