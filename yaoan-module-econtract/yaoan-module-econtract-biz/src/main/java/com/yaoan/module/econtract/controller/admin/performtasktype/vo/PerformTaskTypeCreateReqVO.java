package com.yaoan.module.econtract.controller.admin.performtasktype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/1 13:39
 */
@Data
public class PerformTaskTypeCreateReqVO {

    /**
     * 履约任务类型编码
     */
    @NotNull(message = "编码不能为空")
    @Schema(description = "履约任务类型编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "履约任务类型编码")
    private String code;

    /**
     * 履约任务类型名称
     */
    @NotNull(message = "名称不能为空")
    @Schema(description = "履约任务类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "履约任务类型名称")
    private String name;

}
