package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewcompareitems;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 审查比对检测项新增/修改 Request VO")
@Data
public class ReviewCompareItemsSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10136")
    private String id;

    @Schema(description = "一级检测标题")
    private String itemFirstLevel;

    @Schema(description = "二级检测标题")
    private String itemSecondLevel;

    @Schema(description = "检测项名称", example = "张三")
    private String itemName;

}