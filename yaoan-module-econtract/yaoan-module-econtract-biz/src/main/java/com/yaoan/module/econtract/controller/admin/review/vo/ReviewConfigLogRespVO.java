package com.yaoan.module.econtract.controller.admin.review.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "ReviewConfigLogReqVO")
@Data
public class ReviewConfigLogRespVO {

    /**
     * 角色
     */
    @Schema(description = "角色", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String role;

    /**
     * 名称
     */
    @Schema(description = "名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    /**
     * 审查名称旧
     */
    @Schema(description = "审查名称旧", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String reviewName0;

    /**
     * 审查名称新
     */
    @Schema(description = "审查名称新", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String reviewName1;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime updateTime;



}

