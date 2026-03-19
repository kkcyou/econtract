package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.checkerframework.checker.initialization.qual.UnderInitialization;

import javax.validation.constraints.NotNull;


/**
 * @description:
 * @author: Pele
 * @date: 2023/8/23 13:48
 */
@Data
@Schema(description = "合同类型")
@ToString(callSuper = true)
public class ContractTypeCreateVo {

    /**
     * 合同类型名称
     */
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotNull(message = "合同类型名称不可为空")
    private String name;

    /**
     * 合同类型编号
     */
    @Schema(description = "合同类型编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotNull(message = "合同类型编号不可为空")
    private String code;

    /**
     * 合同类型分类
     */
    @Schema(description = "合同类型分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotNull(message = "合同类型分类不可为空")
    private String typeCategory;

    /**
     * 编号生成规则Id
     */
    @Schema(description = "编号生成规则Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long codeRuleId;
}
