package com.yaoan.module.econtract.controller.admin.bpm.archive.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PageRespVO {
    /**
     * id
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 归档id
     */
    private String archiveId;
    /**
     * 档案名称
     */
    private String name;
    /**
     * 档号
     */
    private String code;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 状态名称
     */
    private String statusName;
    /**
     * 单据类型 归档0 补充1
     */
    private int type;

    /**
     * 单据类型 归档0 补充1
     */
    private String typeName;
    /**
     * 补充备注
     */
    private String remark;
    /**
     * 创建人名称-工作流申请人名称
     */
    private String creatorName;
    /**
     * 申请时间-创建工作流时间
     */
    private LocalDateTime createTime;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 流程状态
     */
    private Integer result;

    /**
     * {@link com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums}
     */
    private String flowableStatus;
    /**
     * 审批状态名称
     */
    private String flowableStatusName;

    /**
     * 已办任务操作结果
     */
    private Integer doneTaskResult;
    /**
     * 合同id
     */
    private String contractId;

    /**
     * 项目编码
     */
    private String proCode;
    private String proName;
}
