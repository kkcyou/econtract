package com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/10/8 21:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractBorrowBpmPageReqVO extends CommonBpmAutoPageReqVO {
    private static final long serialVersionUID = -6502867530027504156L;
    /** 合同名称、创建人姓名 */
    @Schema(description = "合同名称、创建人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String searchText;
/**
 * 合同类型
 * */
    @Schema(description = "合同名称、创建人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractType;

    /**
     * 查询创建时间范围的起始时间
     */
    @Schema(description = "查询创建时间范围的起始时间", example = "2023-08-01 01:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startCreateTime;

    /**
     * 查询创建时间范围的结束时间
     */
    @Schema(description = "查询创建时间范围的结束时间", example = "2023-08-01 03:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endCreateTime;

    /**
     * 查询借阅时间范围的起始时间
     */
    @Schema(description = "查询借阅时间范围的起始时间", example = "2023-08-01 01:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startBorrowTime;

    /**
     * 查询借阅时间范围的结束时间
     */
    @Schema(description = "查询借阅时间范围的结束时间", example = "2023-08-01 03:00:00")
    @JsonFormat(timezone="GMT+8")
    private Date endBorrowTime;

    /**
     * 预计归还时间开始
     */
    @Schema(description = "预计归还时间开始", example = "2023-08-01 01:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startReturnTime;

    /**
     * 预计归还时间结束
     */
    @Schema(description = "预计归还时间结束", example = "2023-08-01 03:00:00")
    @JsonFormat(timezone="GMT+8")
    private Date endReturnTime;

/** 审批状态 */
    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private String approveStatus;

    private List<String> processInstanceIds;
    /**
     * 借阅人名称
     */
    private String applicantName;
    private  List<String> userList;
    /**
     * 借阅人id
     */
    private String applicantId;

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
     * 流程实例
     */
    List<String> instanceIdList;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称")
    private String contractName;

    /**
     * 合同编码
     */
    @Schema(description = "合同编码")
    private String contractCode;

    /**
     * 剩余时间
     */
    @Schema(description = "剩余时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date remainTime;
    /**
     * 档案名称
     */
    @Schema(description = "档案名称")
    private String archiveName;
    /**
     * 档号
     */
    @Schema(description = "档号")
//    private String archiveCode;

    /**
     * 项目编码
     */
    private String proCode;
    private String proName;


}
