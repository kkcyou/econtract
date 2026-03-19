package com.yaoan.module.econtract.controller.admin.param.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author doujiale
 */
@Data
public class BaseParamVO {
    @Schema(description = "参数名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "参数名称不能为空")
    private String name;

    @Schema(description = "参数编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "参数编码不能为空")
    private String code;

    @Schema(description = "参数分类id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "参数分类id不能为空")
    private Integer categoryId;

    @Schema(description = "参数类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "参数类型不能为空")
    private String type;

    @Schema(description = "输入提示", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "输入提示不能为空")
    private String placeholder;

    @Schema(description = "字数长度要求", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotBlank(message = "字数长度要求不能为空")
    private String maxLength;

    @Schema(description = "参数描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String content;


    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "合同类型不能为空")
    private String contractType;


}
