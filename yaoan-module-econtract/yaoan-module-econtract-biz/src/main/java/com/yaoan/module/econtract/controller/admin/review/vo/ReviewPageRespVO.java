package com.yaoan.module.econtract.controller.admin.review.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Review PageResp VO")
@Data
public class ReviewPageRespVO {
    /**
     * 主键
     */
    @Schema(description = "审查清单id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    /**
     * 审查清单名称
     */
    @Schema(description = "审查清单名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    /**
     * 创建者
     */
    @Schema(description = "创建者", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String creator;

    /**
     * 更新者
     */
    @Schema(description = "更新者", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String updater;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime updateTime;


    /**
     * 合同类型列表
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<ReviewContractVO> typeList;



}
