package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author wsh
 */
@Schema(description = "管理后台 - 合同审查规则 Excel 导出 Request VO，参数和 ContractReviewItemsPageReqVO 是一致的")
@Data
public class ContractReviewItemsExportReqVO {

    @Schema(description = "审查内容")
    private String reviewContent;

    @Schema(description = "所属条款")
    private Integer termId;

    @Schema(description = "适用范围(适用的合同分类)")
    private String contractTypes;

    @Schema(description = "风险不利方")
    private Integer riskParty;

    @Schema(description = "风险等级")
    private Integer riskLevel;

    @Schema(description = "法律依据")
    private String legalBasis;

}
