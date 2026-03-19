package com.yaoan.module.econtract.dal.dataobject.bpm.change;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 合同变动审批流程申请表
 * </p>
 *
 * @author Pele
 * @since 2024-01-24
 */
@Data
@TableName("ecms_bpm_contract_change")
public class BpmContractChangeDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = -5705549117877121566L;

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
     * 变更协议的合同id标识
     */
    @TableField("contract_id")
    private String contractId;
    /**
     * 变更协议的名称
     */
    private String contractName;

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

    /**
     * 文件地址id
     */
    private Long fileAddId;

    /**
     * 合同金额
     */
    private Double amount;

    /**
     * 条款文本变更前内容
     */
    @TableField("original_clause_text")
    private String originalClauseText;

    /**
     * 条款文本变更后内容
     */
    @TableField("updated_clause_text")
    private String updatedClauseText;

    /**
     * 变更内容勾选
     */
    @TableField("is_content_change")
    private String isContentChange;

    /**
     * 原始的合同状态
     */
    @TableField("proto_status")
    private Integer protoStatus;

}
