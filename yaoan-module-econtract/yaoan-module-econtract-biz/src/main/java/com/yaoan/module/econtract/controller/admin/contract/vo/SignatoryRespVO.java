package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "Signatory Resp VO")
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class SignatoryRespVO {
    /**
     * 签署方id
     */
    @Schema(description = "签约方id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String signatoryId;

    /**
     * 签署方公司名称
     */
    @Schema(description = "签署方公司名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String signatory;

    /**
     *签署人名称
     */
    @Schema(description = "签署人名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String signName;

    /**
     * 签署顺序
     */
    private Integer sort;
}
