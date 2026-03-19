package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class TaskIdRespVO {

    @Schema(description = "任务id")
    private String taskId;
    @Schema(description = "文件id")
    private Long fileId;
    @Schema(description = "文件地址")
    private String fileUrl;
}
