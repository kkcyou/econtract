package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author wsh
 */
@Schema(description = "管理后台 - 合同审查规则列表 Request VO")
@Data
@ToString(callSuper = true)
public class ClauseGroupRespVO {

    @Schema(description = "条款id")
    private Integer termId;

    @Schema(description = "条款名称")
    private String termName;

    @Schema(description = "该条款下的规则列表")
    private List<ContractReviewItemsBaseVO> rules;

    @Schema(description = "规则ids")
    private List<String> ruleIds;
}
