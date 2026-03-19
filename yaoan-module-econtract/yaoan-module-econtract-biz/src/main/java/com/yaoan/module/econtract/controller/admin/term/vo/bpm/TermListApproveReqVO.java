package com.yaoan.module.econtract.controller.admin.term.vo.bpm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/10 16:22
 */
@Data
public class TermListApproveReqVO  extends CommonBpmAutoPageReqVO {

    private static final long serialVersionUID = 6772935111959260393L;
    /**
     * 流程实例
     */
    List<String> instanceIdList;
    /**
     * 条款类型
     */
    private String termType;

    /**
     * 状态 0未发布 1已发布
     */
    private String status;

    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 分类id
     */
    private Integer categoryId;
    private List<Integer> categoryIds;

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

    private String code;

    private String name;

    /**
     * 模板id
     */
    private String modelId;

    /**
     * 所属行业
     */
    private String tradeType;
    private String tradeTypeName;
    /**
     * 条款依据
     */
    private String termAccord;
    /**
     * 条款库类别 0公共库，1单位库，2其他
     */
    private Integer termLibrary;
}
