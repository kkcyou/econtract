package com.yaoan.module.econtract.controller.admin.signet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "Signet DetailsReq VO")
@Data
@ToString(callSuper = true)
public class SignetSignReqVO {

    /**
     * 印章id
     */
    @Schema(description = "印章id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "印章id不能为空")
    private String id;
}
