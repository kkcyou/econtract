package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/13 11:55
 */
@Data
public class ContractTypeUpdateStatusReqVO {
    /**
     * 合同类型id
     */
    @Schema(description = "合同类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotNull(message = "合同类型id不可为空")
    private String id;

    /**
     * 合同类型状态
     */
    @Schema(description = "合同类型状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotNull(message = "合同类型状态不可为空")
    private Integer contractTypeStatus;

}
