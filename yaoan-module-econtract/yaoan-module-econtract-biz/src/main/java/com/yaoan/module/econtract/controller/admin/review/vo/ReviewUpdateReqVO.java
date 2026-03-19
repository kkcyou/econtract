package com.yaoan.module.econtract.controller.admin.review.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "ReviewUpdateReqVO")
@Data
@ToString(callSuper = true)
public class ReviewUpdateReqVO {

    /**
     * 审查清单id
     */
    @Schema(description = "审查清单", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 审查清单名称
     */
    @NotNull(message = "审查清单名称不能为空")
    @Schema(description = "审查清单名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 审查点排列
     */
    @Schema(description = "审查点排列", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pointsArray;

    /**
     * 备注
     */
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;


    /**
     * 审查点
     */
    @Schema(description = "审查点", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ReviewPointsVO> reviewPointsDOList;


}
