package com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 合同模板推荐创建 ContractAiDraftShowRespShortVO")
@Data
@ToString(callSuper = true)
public class ContractAiDraftShowRespShortVO {
    @Schema(description = "模板名称")
    private String templateName;

    @Schema(description = "模板ID")
    private Long templateiId;
}
