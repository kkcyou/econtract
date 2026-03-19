package com.yaoan.module.system.controller.admin.dept.vo.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 单位列表 Request VO")
@Data
public class CompanyListReqVO {

    @Schema(description = "单位名称，模糊匹配", example = "芋道")
    private String name;

    @Schema(description = "展示状态，参见 CommonStatusEnum 枚举类", example = "1")
    private Integer status;

}
