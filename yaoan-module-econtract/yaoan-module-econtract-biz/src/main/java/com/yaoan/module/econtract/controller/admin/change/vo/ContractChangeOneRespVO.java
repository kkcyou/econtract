package com.yaoan.module.econtract.controller.admin.change.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yaoan.module.econtract.controller.admin.contract.vo.PaymentScheduleRespVO;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.AttachmentVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangePaymentDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.ChangeElementDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/4 15:08
 */
@Data
public class ContractChangeOneRespVO {

    /**
     * 合同审批流程表单主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 主合同合同id
     */
    @TableField("main_contract_id")
    private String mainContractId;

    /**
     * 主合同合同code
     */
    private String mainContractCode;

    /**
     * 主合同合同名称
     */
    private String mainContractName;

    /**
     * 变更协议的合同id标识
     */
    @TableField("contract_id")
    private String contractId;
    /**
     * 变更协议的名称
     */
    private String contractName;

    /**
     * 合同金额
     */
    private BigDecimal amount;
    /**
     * 合同状态
     */
    private String statusName;
    /**
     * 合同状态
     */
    private Integer status;
    /**
     * 币种
     */
    private String currencyType;

    /**
     * 合同审批流程表单主键（简版）
     */
    @TableField("change_code")
    private String changeCode;

    /**
     * 变更协议的变更类型（1=变更 2=补充 3=解除）（简版）
     */
    @TableField("change_type")
    private Integer changeType;

    /**
     * 变动类型名称
     */
    private String changeTypeName;


    /**
     * 变更协议名称（简版）
     */
    @TableField("change_name")
    private String changeName;

    /**
     * 变更协议的截止日期（简版）
     */
    @TableField("change_expiration_date")
    private Date changeExpirationDate;


    /**
     * 申请结果
     */
    @TableField("result")
    private Integer result;

    /**
     * 流程实例的编号
     */
    @TableField("process_instance_id")
    private String processInstanceId;
    private String reason;
    private Long userId;
    private String taskId;

    /**
     * 文件地址id
     */
    private Long fileAddId;
    private String fileAddName;
    /**
     * 申请人
     */
    private String submitterName;

    private LocalDateTime createTime;

    private String creator;


    /**
     * 变动后的支付计划集合
     */
    private List<PaymentScheduleRespVO> changePaymentList;

    /**
     * 原支付计划集合
     */
    private List<PaymentScheduleRespVO> paymentScheduleDOList;

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
    private List<ChangeElementVO> changeElementList;

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
     * 附件文件
     */
    @Schema(description = "附件文件")
    private List<AttachmentVO> files;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;


}
