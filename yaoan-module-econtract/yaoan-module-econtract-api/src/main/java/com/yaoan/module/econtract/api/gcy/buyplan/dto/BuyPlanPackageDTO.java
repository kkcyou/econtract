package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: doujl
 * @date: 2023/11/28 11:46
 */
@Data
public class BuyPlanPackageDTO implements Serializable {


    private static final long serialVersionUID = -2447331664732892613L;
    /**
     * 采购计划唯一识别码
     */
    private String buyPlanGuid;

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

    private List<BuyPlanBillDTO> itemList;

    private List<SingleNoticeSupplierDTO> supplierList;


}
