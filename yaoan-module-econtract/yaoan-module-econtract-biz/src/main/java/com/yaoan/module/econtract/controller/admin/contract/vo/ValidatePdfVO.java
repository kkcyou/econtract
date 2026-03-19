package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ValidatePdfVO {
    @Schema(description = "上传的合同文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private MultipartFile file;
    private String xml;
    private String type;

    private Long fileId;
}
