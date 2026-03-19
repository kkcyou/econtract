package com.yaoan.module.econtract.controller.admin.gpx.vo.file;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/7/4 17:12
 */
@Data
public class PermissionRespVO {
    /**
     * 是否有权限
     */
    private Boolean ifPermission;
    /**
     * 供应商id
     */
    private String supplierId;
    /**
     * 采购单位id
     */
    private String orgId;

}
