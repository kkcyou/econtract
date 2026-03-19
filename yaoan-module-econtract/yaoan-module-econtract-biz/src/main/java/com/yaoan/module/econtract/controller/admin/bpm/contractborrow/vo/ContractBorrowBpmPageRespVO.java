package com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.BorrowRecordRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/10/8 21:28
 */
@Data
public class ContractBorrowBpmPageRespVO {

    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 提交人
     */
    @Schema(description = "提交人")
    private String submitter;

    /**
     * 借阅时间
     */
    @Schema(description = "借阅时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date submitTime;

    /**
     * 提交时间
     */
    @Schema(description = "提交时间")
    private LocalDateTime createTime;

    /**
     * 流程结果Id
     */
    @Schema(description = "流程结果Id")
    private Integer result;

    /**
     * 审核状态
     */
    @Schema(description = "审核状态")
    private String approveStatus;

    /**
     * 审核时间
     */
    @Schema(description = "审核时间")
    private LocalDateTime updateTime;

    /**
     * 流程实例id
     */
    @Schema(description = "流程实例id")
    private String processInstanceId;

    /**
     * 流程任务id
     */
    @Schema(description = "流程任务id")
    private String taskId;

    private String creator;

    /**
     * 被分派到任务的人
     * */
    private Long assigneeId;

    /**
     * 合同列表
     * */
    private List<BorrowRecordRespVO> borrowRecordRespVO;

    /**
     * 借阅id
     * */
    private String borrowId;
    /**
     * 借阅标题名称
     */
    private String borrowName;
}
