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
@Schema(description = "管理后台 - 合同审查规则列表 Request VO")
@Data
@ToString(callSuper = true)
public class ContractReviewItemsListReqVO {

    @Schema(description = "审查内容")
    private String reviewContent;

    @Schema(description = "所属条款")
    private Integer termId;

    @Schema(description = "适用范围(适用的合同分类)")
    private List<Integer> contractTypesIds;

    @Schema(description = "风险等级标识")
    private Integer riskLevelFlag;

    @Schema(description = "清单id")
    private String reviewListId;
}
