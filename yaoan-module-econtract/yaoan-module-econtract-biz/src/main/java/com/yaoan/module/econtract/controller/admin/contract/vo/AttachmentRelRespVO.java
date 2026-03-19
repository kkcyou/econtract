package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AttachmentRelRespVO {

    /**
     * 附件名称
     */
    @Schema(description = "附件名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String attachmentName;

    /**
     * 附件地址id
     */
    @Schema(description = "附件地址id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long attachmentAddId;

    /**
     * 附件类型
     */
    @Schema(description = "附件类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String attachmentType;
}
