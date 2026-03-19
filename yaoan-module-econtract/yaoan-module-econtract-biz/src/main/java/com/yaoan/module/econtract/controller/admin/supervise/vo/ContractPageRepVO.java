package com.yaoan.module.econtract.controller.admin.supervise.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同分页列表请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractPageRepVO extends PageParam {
    /**
     * 计划名称
     */
    @Schema(description = "计划名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanName;
    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractCode;
    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractName;

    /**
     * 合同状态
     */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierName;
    /**
     * 包编号
     */
    @Schema(description = "包编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bidCode;
    /**
     * 包名称
     */
    @Schema(description = "包名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bidName;
    /**
     * 采购计划备案书/核准书编号
     */
    @Schema(description = "采购计划备案书/核准书编号")
    private String buyPlanCode;

    /**
     * 指定审核的代理机构的 userId
     * */
    @Schema(description = "指定审核的代理机构的 userId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String agentGuid;
}
