package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentInfoVO {
    /**
     *归档人员名称
     */
    @Schema(description = "归档人员名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String documentName;

    /**
     *归档日期
     */
    @Schema(description = "归档日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime documentDate;

    /**
     *归档文件名称
     */
    @Schema(description = "归档文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String documentFileName;

    /**
     *归档文件地址id
     */
    @Schema(description = "归档文件地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long documentAddId;
}
