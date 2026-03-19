package com.yaoan.module.econtract.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 15:58
 */
@Data
public class ModelBaseVO {

    @Schema(description = "模板编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
//    @NotBlank(message = "模板编码不能为空") 自动生成
    @Size(max = 30, message = "模板编码长度不能超过100个字符")
    private String code;

    @Schema(description = "模板名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotBlank(message = "模板名称不能为空")
    @Size(max = 30, message = "模板名称长度不能超过100个字符")
    private String name;



}
