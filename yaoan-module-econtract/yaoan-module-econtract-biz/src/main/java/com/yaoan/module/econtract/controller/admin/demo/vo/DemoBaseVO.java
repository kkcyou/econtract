package com.yaoan.module.econtract.controller.admin.demo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class DemoBaseVO {

    @Schema(description = "demo名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotBlank(message = "demo名称不能为空")
    @Size(max = 30, message = "部门名称长度不能超过30个字符")
    private String name;


}
