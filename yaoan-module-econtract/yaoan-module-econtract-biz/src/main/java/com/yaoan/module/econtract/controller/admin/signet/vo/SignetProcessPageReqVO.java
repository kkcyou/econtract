package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Schema(description = "Signet DetailsResp VO")
@Data
@ToString(callSuper = true)
public class SignetProcessPageReqVO {

    /**
     * 印章名称
     */
    @Schema(description = "印章名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealName;

    /**
     * 审批状态 0:草稿，1：我发起的
     */
    @Schema(description = "审批状态 0:草稿，1：我发起的", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer result;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractName;



}
