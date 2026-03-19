package com.yaoan.module.econtract.controller.admin.relative.blacklistVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author doujiale
 */
@Data
@Schema(description = "新增相对方信息")
@ToString(callSuper = true)
public class BlacklistApplyReqVO {
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "相对方id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String relativeId;

    @Schema(description = "附件id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long fileId;

    @Schema(description = "附件名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileName;

    @Schema(description = "申请类型 移入0 移出1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer applyType;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer applyStatus;
    
    @Schema(description = "申请意见", requiredMode = Schema.RequiredMode.REQUIRED)
    private String applyMsg;

    @Schema(description = "审批类型 审批0 拒绝1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer auditType;


    @Schema(description = "审批意见", requiredMode = Schema.RequiredMode.REQUIRED)
    private String auditMsg;

}
