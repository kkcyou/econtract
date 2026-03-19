package com.yaoan.module.econtract.controller.admin.signet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "用章申请代办数量返回 VO")
@Data
@ToString(callSuper = true)
public class SignetAgentNumRespVO {
    private static final long serialVersionUID = 447098114046669377L;

    /**
     * 政府采购合同代办数量
     */
    @Schema(description = "政府采购合同代办数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long orgSignetAgentNum=0L;

    /**
     * 用章申请代办数量
     */
    @Schema(description = "用章申请代办数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long signetAgentNum=0L;
}
