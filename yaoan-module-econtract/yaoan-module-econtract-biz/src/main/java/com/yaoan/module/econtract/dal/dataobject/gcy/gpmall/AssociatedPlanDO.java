package com.yaoan.module.econtract.dal.dataobject.gcy.gpmall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @description: 关联计划信息
 * @author: zhc
 * @date: 2024-02-18 19:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gcy_associated_plan")
public class AssociatedPlanDO extends BaseDO {

    private static final long serialVersionUID = -7809596720386425140L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 计划id
     */
    private String buyPlanId;

    /**
     * 计划名称
     */
    private String buyPlanName;

    /**
     * 计划编号
     */
    private String buyPlanCode;

    /**
     * 计划金额
     */
    private BigDecimal buyPlanMoney;

//    /**
//     * 可签约金额
//     */
//    private BigDecimal signMoney;

    /**
     * 实施形式
     */
    private String implementationForm;
    /**
     * 采购方式
     */
    private String purchaseMethod;

    /**
     * 合同id
     */
    private String contractId;
    /**
     * 计划来源 NO_PLAN("0"，"自建计划")，SUPERVISE_PLAN"1"，"监管计划");
     */
    @Schema(description = "计划来源(参见选项字典【BuyPlanSource】定义)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanSource;
    /**
     * 计划来源名称
     */
    @Schema(description = "计划来源名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanSourceName;
    /**
     * 是否进口产品采购(1:是,0:否)
     */
    @Schema(description = "是否进口产品采购", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isImports;
    /***
     * 是否涉密采购(1:是,0:否)
     */
    @Schema(description = "是否涉密采购", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer secret;
    /**
     * 采购组织形式
     */
    private String kind;
}
