package com.yaoan.module.econtract.controller.admin.contract.vo.extraction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class UploadReqVO {

    /**
     * 文件ID
     */
    @Schema(description = "文件ID ", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long fileId;

    /**
     * 文件
     */
    @Schema(description = "文件 ", requiredMode = Schema.RequiredMode.REQUIRED)
    private MultipartFile file;


    @Schema(description = "业务类型，参考 FileUploadPathEnum", example = "1")
    private Integer code;

}
