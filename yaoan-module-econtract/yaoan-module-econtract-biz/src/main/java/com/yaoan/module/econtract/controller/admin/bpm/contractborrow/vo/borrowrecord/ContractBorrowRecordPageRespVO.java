package com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description: 借阅记录resp
 * @author: Pele
 * @date: 2023/10/9 15:02
 */
@Data
public class ContractBorrowRecordPageRespVO extends FlowableParam {
    /**
     * 申请id
     */
    @Schema(description = "申请id")
    private String id;

    /**
     * 申请人
     */
    @Schema(description = "申请人")
    private String creator;

    /**
     * 借阅时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date submitTime;

    /**
     * 预计归还时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date returnTime;

    /**
     * 申请时间
     */
    @Schema(description = "申请时间")
    private LocalDateTime createTime;

    /**
     * 流程状态
     */
    @Schema(description = "审批状态")
    private Integer status;

    /**
     * 流程实例id
     */
    @Schema(description = "流程实例id")
    private String processInstanceId;

    /**
     * 审批时间
     */
    @Schema(description = "审批时间")
    private LocalDateTime approveTime;


    /**
     * 提交人
     */
    @Schema(description = "提交人")
    private String submitter;

    /**
     * 流程状态
     */
    @Schema(description = "流程状态")
    private Integer result;

    /**
     * 任务id
     */
    @Schema(description = "任务id")
    private String taskId;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称")
    private String contractName;

    /**
     * 合同id
     */
    @Schema(description = "合同id")
    private String contractId;
    /**
     * 借阅id
     */
    private String borrowId;

    /**
     * 档案id
     */
    private String archiveId;

    /**
     * 借阅类型 1=纸质文件 0=电子文件
     */
    private String borrowType;

    /**
     * 电子文件权限 : 1=带水印查看 2=无水印查看 3=带水印下载 4=无水印下载
     */
    private String borrowPermission;

    /**
     * 档案名称
     */
    @Schema(description = "档案名称")
    private String archiveName;

    /**
     * 档号
     */
    @Schema(description = "档号")
    private String archiveNo;

    /**
     * 项目编码
     */
    private String proCode;
    private String proName;

    /**
     * 借阅人
     */
    @Schema(description = "借阅人")
    private String borrower;

    /**
     * 全宗号
     */
    private String fondsNo;
    /**
     * 一级类别号
     */
    private String firstLevelNo;
    /**
     * 二级类别号
     */
    private String secondLevelNo;

    /**
     * 案卷号
     */
    private String volumeNo;

    @Schema(description = "档案载体 电子0 纸质1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String medium;

    @Schema(description = "档案载体 电子0 纸质1", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("档案载体 电子0 纸质1")
    private String mediumName;
}
