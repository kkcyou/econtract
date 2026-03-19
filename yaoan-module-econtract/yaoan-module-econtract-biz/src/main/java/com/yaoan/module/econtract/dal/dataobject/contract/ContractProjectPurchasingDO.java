package com.yaoan.module.econtract.dal.dataobject.contract;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目采购数据
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("project_purchasing")
public class ContractProjectPurchasingDO extends TenantBaseDO {
    private static final long serialVersionUID = 7115001740386094479L;

    /**
     * 主键
     */
    private String id;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 合同编号
     */
    private String code;

    /**
     * 采购人名称
     */
    private String purchaserName;

    /**
     * 采购人
     */
    private String purchaser;

    /**
     * 采购包号
     */
    private Integer packageNumber;

    /**
     * 采购包名
     */
    private String packageName;

    /**
     * 采购包预算
     */
    private Long packageBudget;

    /**
     * 采购方式，公开招标，邀请招标竞争性谈判，单一来源采购，询价，国务院政府采购监督管理部门认定的其他采购方式
     */
    private String payType;

    /**
     * 项目类型，工程，货物，服务
     */
    private String projectType;

    /**
     * 合同金额
     */
    private Integer amount;

    /**
     * 代理机构
     */
    private String agency;

    /**
     * 签订日期
     */
    private Data signTime;

    /**
     * 所在地
     */
    private String address;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 起草单位
     */
    private String draftingUnit;

    /**
     * 删除标识
     */
    private Long isDeleted;
}