package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author wsh
 */
@Schema(description = "管理后台 - 合同审查规则分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractReviewItemsPageReqVO extends PageParam {

    @Schema(description = "审查内容")
    private String reviewContent;

    @Schema(description = "所属条款")
    private List<String> termId;

    @Schema(description = "风险等级标识")
    private Integer riskLevel;

    @Schema(description = "规则ids")
    private List<String> ruleIds;

    @Schema(description = "清单id")
    private String reviewListId;

    @Schema(description = "适用范围(适用的合同分类)")
    private List<Integer> contractTypesIds;
}
