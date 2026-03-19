package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/23 14:11
 */
@Data
@Schema(description = "合同类型 更新VO")
@ToString(callSuper = true)
public class ContractTypeUpdateVo {

    /**
     * 合同类型ID
     */
    @Schema(description = "合同类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String id;

    /**
     * 合同类型名称
     */
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String name;

    /**
     * 合同类型分类
     */
    @Schema(description = "合同类型分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String typeCategory;
    /**
     * 编号生成规则Id
     */
    @Schema(description = "编号生成规则Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long codeRuleId;
}
