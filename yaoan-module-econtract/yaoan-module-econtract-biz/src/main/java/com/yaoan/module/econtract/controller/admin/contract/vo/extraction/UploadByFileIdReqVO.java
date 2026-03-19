package com.yaoan.module.econtract.controller.admin.contract.vo.extraction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class UploadByFileIdReqVO {

    /**
     * 文件ID
     */
    @Schema(description = "文件ID ", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long fileId;

}
