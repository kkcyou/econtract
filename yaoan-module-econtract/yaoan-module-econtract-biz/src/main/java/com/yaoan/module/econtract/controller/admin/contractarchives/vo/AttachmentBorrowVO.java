package com.yaoan.module.econtract.controller.admin.contractarchives.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;


@Data
public class AttachmentBorrowVO {

    private String id;

    @Schema(description = "借阅人", requiredMode = Schema.RequiredMode.REQUIRED, example = "26904")
    private String creator;

    @Schema(description = "部门")
    private String deptName;
    /**
     * 借阅时间
     */
    @Schema(description = "借阅时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date submitTime;
    /**
     * 归还时间
     */
    @Schema(description = "归还时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date returnTime;

    /**
     * 剩余时间
     */
    @Schema(description = "剩余时间")
    private String remainTime;

    @Schema(description = "借阅状态名称")
    private String borrowStatusName;

}