package com.yaoan.module.econtract.api.contract.dto;

import lombok.Data;

/**
 * 包状态修改 - 能否起草状态 hidden
 */
@Data
public class PackageUpdateDTO {
    private String packageGuid;
    /**
     * 是否隐藏（一般项目采购多供应商场景，新加）
     * */
    private Integer hidden;
}
