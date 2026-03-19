package com.yaoan.module.econtract.dal.dataobject.contracttype;

import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pele
 * @TableName ecms_contract_type
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_type_new")
public class ContractType extends TenantBaseDO implements Serializable {

    private static final long serialVersionUID = -686808733435075795L;

    /**
     * 合同类型ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同类型名称
     */
    private String name;

    /**
     * 合同类型编号
     */
    private String code;

    /**
     * 类型前缀
     */
    private String typePrefix;

    /**
     * 父类id
     */
    private String parentId;

    /**
     * 合同类型状态（0：关闭，1：开启）
     */
    private Integer typeStatus;

    /**
     * 编号生成规则ID
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String codeRuleId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 起草审批流程
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String draftApprovalProcess;

    /**
     * 登记流程
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String registerProcess;

    /**
     * 变更流程
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String changeProcess;

    /**
     * 收款流程
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String collectionProcess;

    /**
     * 付款流程
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String paymentProcess;

    /**
     * 借阅流程
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String borrowProcess;


    /**
     * 第三方系统合同类型id
     * 例如：黑龙江电子合同合同类型id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String platId;

    /**
     * 是否需要用印
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Boolean needSignet;

    @Setter
    @Getter
    @TableField(exist = false)
    private List<ContractType> subContractTypes = new ArrayList<>();

}