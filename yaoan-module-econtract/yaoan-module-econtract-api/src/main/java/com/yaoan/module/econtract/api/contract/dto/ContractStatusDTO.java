package com.yaoan.module.econtract.api.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ContractStatusDTO {

    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "合同id不能为空")
    private String contractGuid;

    /**
     * 合同状态
     */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "合同状态不能为空")
    private Integer status;

    /**
     * 操作人
     */
    private String operatorStr;
}
