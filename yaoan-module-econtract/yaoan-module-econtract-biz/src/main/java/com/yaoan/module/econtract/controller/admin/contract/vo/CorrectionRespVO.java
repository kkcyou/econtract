package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class CorrectionRespVO {

    @Schema(description = "文件id")
    private Long fileId;

    @Schema(description = "文件地址")
    private String fileUrl;

    @Schema(description = "审查地址")
    private String correctionUrl;
}
