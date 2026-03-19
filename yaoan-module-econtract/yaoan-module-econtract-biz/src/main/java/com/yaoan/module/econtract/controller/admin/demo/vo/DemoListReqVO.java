package com.yaoan.module.econtract.controller.admin.demo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 部门列表 Request VO")
@Data
public class DemoListReqVO {

    @Schema(description = "部门名称，模糊匹配", example = "芋道")
    private String name;

}
