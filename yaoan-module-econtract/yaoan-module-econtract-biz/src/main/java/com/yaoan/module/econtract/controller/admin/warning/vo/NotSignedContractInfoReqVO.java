package com.yaoan.module.econtract.controller.admin.warning.vo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NotSignedContractInfoReqVO {
    /**
     * 包id
     */
    @Schema(description = "包id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanPackageId;

    /**
     * 订单id
     */
    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String OrderId;

}
