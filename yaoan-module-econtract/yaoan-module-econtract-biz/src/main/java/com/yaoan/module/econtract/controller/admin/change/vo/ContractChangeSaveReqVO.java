package com.yaoan.module.econtract.controller.admin.change.vo;

import com.yaoan.module.econtract.controller.admin.contractarchives.vo.AttachmentVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.ChangeElementDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description: 保存补充协议 ReqVO
 * @author: Pele
 * @date: 2024/1/24 16:47
 */
@Data
public class ContractChangeSaveReqVO {

    /**
     * 变更id
     */
    private String id;

    /**
     * 合同编码
     */
    private String contractCode;
    /**
     * 合同编码
     */
    private String contractName;

    /**
     * 变动名称
     */
    private String name;

    /**
     * 签署截止日期
     */
    private Date expirationDate;

    /**
     * 附件id
     */
    private Long fileAddId;

    /**
     * 对应的流程编号
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;

    /**
     * 合同文件pdf 地址
     */
    private Long pdfFileId;

    /**
     * 主合同id（变动合同独有）
     */
    private String mainContractId;
    /**
     * 变动类型（1=变更，2=补充，3=解除）
     * {@link com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums}
     */
    private Integer changeType;
    /**
     * 申请原因
     */
    private String reason;

    /**
     * 支付计划集合
     */
    private List<BpmContractChangePaymentVO> changePaymentList;

    /**
     * 条款文本变更前内容
     */
    private String originalClauseText;

    /**
     * 条款文本变更后内容
     */
    private String updatedClauseText;

    /**
     * 关键要素集合
     */
    private List<ChangeElementDO> changeElementList;

    /**
     * 是否内容变更
     */
    private Integer isTermChange;
    /**
     * 是否表单数据变更
     */
    private Integer isElementChange;
    /**
     * 是否履约计划变更
     */
    private Integer isPaymentChange;

    /**
     * 提交标识
     */
    private Integer isSubmit;

    /**
     * 审批意见
     */
    private String advice;
    /**
     * 附件文件
     */
    @Schema(description = "附件文件")
    private List<AttachmentVO> files;

}
