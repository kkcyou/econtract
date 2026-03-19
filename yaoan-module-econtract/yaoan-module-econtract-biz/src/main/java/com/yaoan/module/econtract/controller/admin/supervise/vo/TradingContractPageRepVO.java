package com.yaoan.module.econtract.controller.admin.supervise.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TradingContractPageRepVO extends PageParam {
    /**
     * 计划id
     */
    @Schema(description = "计划id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanId;
    /**
     * 合同状态
     */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
    /**
     * 采购单位名称
     */
    @Schema(description = "采购单位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgName;

    /**
     * 采购单位id
     */
    @Schema(description = "采购单位id", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> orgIdList;

    /**
     * 项目类型名称
     */
    private String projectTypeName;

    /**
     * 所属的项目guid
     */
    private String projectGuid;

    private String code;
    private String supplierName;
    private String name;
    private String packageName;
}
