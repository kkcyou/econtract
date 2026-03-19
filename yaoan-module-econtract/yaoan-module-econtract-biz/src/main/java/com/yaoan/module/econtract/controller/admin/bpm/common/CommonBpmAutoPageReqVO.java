package com.yaoan.module.econtract.controller.admin.bpm.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/16 16:47
 */
@Data
public class CommonBpmAutoPageReqVO extends PageParam {

    private static final long serialVersionUID = 2899402342955116423L;
    /**
     * 流程实例
     */
    List<String> instanceIdList;
    /**
     * 条款类型
     */
    private String termType;
    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 申请人id
     */
    private String applicantId;
    /**
     * 申请人名字
     */
    private String applicantName;
    /**
     * 创建时间0
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime0;
    /**
     * 创建时间1
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime1;

    /**
     * 审批时间0
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approveTime0;
    /**
     * 审批时间1
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approveTime1;

    /**
     * 请求时间0
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime0;
    /**
     * 审批时间1
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime1;

    /**
     * {@link ApprovePageFlagEnums}
     * ALL(0, "全部"),
     * DONE(1, "已审批"),
     * TO_DO(2, "未审批"),
     */
    private Integer flag;

    /**
     * 审批状态
     */
    private Integer result;

    /**
     * 前端发送的状态码
     * {@link CommonFlowableReqVOResultStatusEnums}
     * TO_SEND(0, "TO_SEND", "草稿"),
     * APPROVING(1, "APPROVING", "审批中"),
     * TO_DO(2, "SUCCESS", "审批通过"),
     * REJECTED(5, "TO_SEND", "被退回"),
     */
    private String frontCode;

    /**
     * 创建人名称
     */
    private String creatorName;
    /**
     * 变动合同名称
     */
    private String name;
    /**
     * 变动合同编码
     */
    private String code;
    /**
     * 变动类型（1=变更，2=补充，3=解除）
     * {@link com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums}
     */
    private Integer changeType;

    private List<Integer> changeTypes;
    private List<String> userList;

    /**
     * 搜索任务状态的字段
     */
    private Integer taskResult;

    /**
     * 合同变动和状态变动标识，0：合同变动，1：状态变动
     */
    private Integer isStatusChange;

    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同编码
     */
    private String contractCode;
    /**
     * 是否政采
     */
    private Integer isGov;
}
