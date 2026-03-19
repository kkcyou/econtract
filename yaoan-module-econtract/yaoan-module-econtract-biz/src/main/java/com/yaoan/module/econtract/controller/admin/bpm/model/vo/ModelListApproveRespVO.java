package com.yaoan.module.econtract.controller.admin.bpm.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/16 16:51
 */
@Data
public class ModelListApproveRespVO extends FlowableParam {

    /**
     * 主键
     */
    @Schema(description = "主键")
    private String id;

    /**
     * 名称
     */
    @Schema(description = "模板编码")
    private String name;

    /**
     * 编号
     */
    @Schema(description = "编号")
    private String code;

    /**
     * 审核内容
     */
    @Schema(description = "审核内容")
    private String approveContent;

    /**
     * 提交人
     */
    @Schema(description = "提交人")
    private String submitter;

    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime submitTime;

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
    private LocalDateTime approveTime;

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


    /**
     *--------------------------------追加--------------------------------
     * */
    /**
     * 合同类型
     */
    private String contractType;
    /**
     * 合同类型名称
     */
    private String contractTypeName;

    /**
     * 模板生效时间
     */
    private LocalDateTime effectStartTime;

    /**
     * 模板生效结束时间
     */
    private LocalDateTime effectEndTime;
    /**
     * 时效模式
     */
    private Integer timeEffectModel;
    /**
     * 版本
     */
    private Double version;


    /**
     * 被分派到任务的人
     * */
    private Long assigneeId;
}
