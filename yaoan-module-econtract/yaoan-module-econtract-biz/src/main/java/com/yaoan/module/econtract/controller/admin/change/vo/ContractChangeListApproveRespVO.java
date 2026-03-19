package com.yaoan.module.econtract.controller.admin.change.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/24 18:30
 */
@Data
public class ContractChangeListApproveRespVO extends FlowableParam {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 变更合同编码
     */
    private String code;

    /**
     * 变更合同名称
     */
    private String name;

    /**
     * 签署截止日期
     */
    private Date expirationDate;

    /**
     * 主文件地址id
     */
    private Long fileAddId;

    /**
     * 签署状态
     * 0-待发送 - 新增
     * 1-被退回
     * 2-已发送
     * 3-待确认
     * 4-待签署
     * 5-已拒签
     * 6-签署完成
     * 7-逾期未签署
     * 8-合同终止签署中
     * 9-合同终止
     * 10-合同变更
     * 11-待送审
     * 12-审核中
     * 13审核未通过
     */
    private Integer status;

    /**
     * 对应的流程编号
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    /**
     * 审批时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approveTime;

    /**
     * 变动原因/申请原因
     */
    private String changeReason;

    /**
     * 主合同id（变动合同独有）
     */
    private String mainContractId;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同编码
     */
    private String contractCode;


    /**
     * 变动类型（1=变更，2=补充，3=解除）
     * {@link com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums}
     */
    private Integer changeType;
    private String submitter;
    private Integer result;
    private String taskId;
    private String creator;
    private LocalDateTime updateTime;
    private String changeTypeName;
    private String resultName;
    private String statusName;
    /**
     * 变更前状态
     */
    private Integer protoStatus;
    /**
     * 变更前状态名称
     */
    private String protoStatusName;
    /**
     * 审批状态名称
     */
    private String resultStatusName;

    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 合同类型
     */
    private String contractType;
    /**
     * 合同类型名称
     */
    private String contractTypeName;
}
