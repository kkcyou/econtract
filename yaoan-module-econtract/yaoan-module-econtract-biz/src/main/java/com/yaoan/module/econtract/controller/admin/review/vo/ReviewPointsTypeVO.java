package com.yaoan.module.econtract.controller.admin.review.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Schema(description = "ReviewPointsTypeRespVO")
@Data
@ToString(callSuper = true)
public class ReviewPointsTypeVO {
    /**
     * 主键
     */
    @Schema(description = "审查点id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    /**
     * 审查点名称
     */
    @Schema(description = "审查点名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    /**
     * 审查点id
     */
    @Schema(description = "父类id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String parentId;



}
