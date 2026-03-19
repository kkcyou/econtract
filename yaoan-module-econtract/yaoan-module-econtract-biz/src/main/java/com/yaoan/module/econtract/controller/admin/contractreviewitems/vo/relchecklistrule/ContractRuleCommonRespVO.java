package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractRuleCommonRespVO {

    @Schema(description = "审查id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reviewId;

    @Schema(description = "审查内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reviewContent;

    @Schema(description = "风险等级", requiredMode = Schema.RequiredMode.REQUIRED)
    private String riskLevel;

    @Schema(description = "风险不利方", requiredMode = Schema.RequiredMode.REQUIRED)
    private String riskParty;

    @Schema(description = "法律依据")
    private List<ReviewItemBasisBaseCommonRespVO> legalBasis;

}
