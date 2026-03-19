package com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class BorrowTypeCountRespVO {

    /**
     * 档案借阅申请数（个）
     */
    @Schema(description = "档案借阅申请数（个）")
    private Integer archiveCount;

    /**
     * 已审批
     */
    @Schema(description = "已审批")
    private Integer doneCount;

    /**
     * 审批中
     */
    @Schema(description = "审批中")
    private Integer doingCount;

    /**
     * 档案借阅中数（个）
     * */
    @Schema(description = "档案借阅中数（个）")
    private Integer borrowCount;

    /**
     * 档案已归还数+档案已失效数（个）
     */
    @Schema(description = "档案已归还数+档案已失效数（个）")
    private Integer returnCount;

    /**
     * 已归还
     */
    @Schema(description = "已归还")
    private Integer returnDoneCount ;

    /**
     * 已失效
     */
    @Schema(description = "已失效")
    private Integer returnFailCount;


}
