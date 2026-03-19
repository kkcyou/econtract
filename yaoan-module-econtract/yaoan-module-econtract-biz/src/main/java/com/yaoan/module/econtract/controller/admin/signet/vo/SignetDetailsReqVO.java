package com.yaoan.module.econtract.controller.admin.signet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Schema(description = "Signet DetailsReq VO")
@Data
@ToString(callSuper = true)
public class SignetDetailsReqVO {

    /**
     * 印章id
     */
    @Schema(description = "印章id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "印章id不能为空")
    private String sealId;

    /**
     * 业务id
     */
    @Schema(description = "业务id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String businessId;

    /**
     * 印章状态（0:已停用，1：已启用，2：已注销，3：已过期）
     */
    @Schema(description = "印章状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer sealStatus;

    /**
     * 印章范围（0：合同，1：模版）
     */
    @Schema(description = "印章范围", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String range;



}
