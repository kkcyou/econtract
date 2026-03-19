package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;


@Data
public class CorrectionReqVO {

    @Schema(description = "文件id")
    private Long fileId;

    @Schema(description = "文件附件")
    @NotNull(message = "文件附件不能为空")
    private MultipartFile file;

    @Schema(description = "文件地址")
    private String path;

    @Schema(description = "本地文件地址")
    private String localPath;
}
