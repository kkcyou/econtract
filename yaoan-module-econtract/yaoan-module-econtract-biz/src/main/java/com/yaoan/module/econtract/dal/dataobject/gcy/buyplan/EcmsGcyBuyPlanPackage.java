package com.yaoan.module.econtract.dal.dataobject.gcy.buyplan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 分包信息表
 * </p>
 *
 * @author doujiale
 * @since 2024-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gcy_buy_plan_package")
public class EcmsGcyBuyPlanPackage extends BaseDO {

    private static final long serialVersionUID = -2504376452326941004L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 采购计划唯一识别码
     */
    private String buyPlanGuid;

    /**
     * 单位
     */
    private String orgGuid;


    /**
     * 计划备案/核准文号
     */
    private String buyPlanCode;

    /**
     * 计划名称
     */
    private String buyPlanName;

    /**
     * 是否是多个分包
     */
    private Boolean isManyPackage;

    /**
     * 是否可起草
     */
    private Boolean status;

    /**
     * 分包的唯一识别码
     */
    private String buyPlanPackageGuid;

    /**
     * 分包编号
     */
    private Integer packageNumber;

    /**
     * 分包名称
     */
    private String packageName;

    /**
     * 分包预算
     */
    private Double packageMoney;

    /**
     * 最高限价（Null为不单独设定最高限价，预算价即为最高限价）
     */
    private Double limitAmount;

    /**
     * 采购分类(参见选项字典【PurCatalogType】定义)
     */
    private String purCatalogType;

    /**
     * 采购方式编码(参见采购方式定义)
     */
    private String purMethod;

    /**
     * 定价方式(参见选项字典【SettlementMode】定义)
     */
    private String settlementMode;

    /**
     * 评审方式(参见选项字典【EvaluateMethod】定义)
     */
    private String evaluateMethod;

    /**
     * 是否允许联合体投标(1:是,0:否)
     */
    private Integer jointBiddingStatus;

    /**
     * 是否允许合同分包(1:是,0:否)
     */
    private Integer subContractStatus;

    /**
     * 是否专门面向中小企业采购(1:是,0:否)
     */
    private Integer reserveStatus;

    /**
     * 预留形式(参见选项字典【ReserveType】定义)
     */
    private String reserveType;

    /**
     * 面向特定规模供应商采购的范围(参见选项字典【SupplierReserve】定义)
     */
    private String supplierReserve;

    /**
     * 面向中小企业预留金额
     */
    private Double reserveMoney;

    /**
     * 单一来源公示唯一标识码
     */
    private String singleNoticeGuid;

    /**
     * 是否推荐专家
     */
    private Integer recommendExpert;

    /**
     * 推荐专家的理由
     */
    private String recommendExpertReason;

}
