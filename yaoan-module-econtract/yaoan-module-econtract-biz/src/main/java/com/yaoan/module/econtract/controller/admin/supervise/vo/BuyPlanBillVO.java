package com.yaoan.module.econtract.controller.admin.supervise.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description:
 * @author: ZHC
 * @date: 2023/11/28 11:46
 */
@Data
public class BuyPlanBillVO implements Serializable {

    private static final long serialVersionUID = 2091565871779593327L;
    /**
     * 采购计划唯一识别码
     */
    private String buyPlanGuid;

    /**
     * 分包的唯一识别码
     */
    private String buyPlanPackageGuid;

    /**
     * 明细项唯一识别码(全局唯一)
     */
    private String billGuid;

    /**
     * 采购明细的序号
     */
    private Integer dOrder;

    /**
     * 商品/服务名称
     */
    private String goodsName;

    /**
     * 采购品目代码、采购品目名称(参见采购目录信息定义)
     */
    private String purCatalogCode;
    private String purCatalogName;



    /**
     * 采购数量
     */
    private Object num;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 单价(元) (总价/数量作为最高限价，在小数点后6位截尾)。
     */
    private Object price;

    /**
     * 是否采购进口产品(1:是,0:否)
     */
    private Integer isImports;

    /**
     * 是否核心产品(1:是,0:否)
     */
    private Integer coreProduct;

    /**
     * 是否强制采购节能产品(1:是,0:否)
     */
    private Integer efficient;

    /**
     * 是否强制采购节水产品(1:是,0:否)
     */
    private Integer waterSaving;

    /**
     * 是否优先采购环保产品(1:是,0:否)
     */
    private Integer environment;

    /**
     * 是否属于政府采购需求标准(2023年版)规范产品  (1:是,0:否)
     */
    private Integer infoInnovationProduct;

    /**
     * 采购标的所属国民经济分类(参见选项字典【IndustrialClass】定义)
     */
    private String industrialClass;
    private String industrialClassName;

    /**
     * 是否属于政府购买服务(1:是,0:否)
     */
    private Integer govService;

    /**
     * 政府购买服务指导性目录代码
     */
    private String govServiceCatalogCode;

    /**
     * 政府购买服务指导性目录名称
     */
    private String govServiceCatalogName;

    /**
     * 品目配置类别唯一标识
     */
    private String purCatalogConfigurationGuid;

    /**
     * 品目配置类别名称
     */
    private String purCatalogConfigurationName;

    /**
     * 规格参数/服务要求
     */
    private String spec;
    /**
     * 总金额(元)
     */
    private Double totalPrice;
    /**
     * 可使用金额
     */
    private BigDecimal canUseMoney;
    /**
     * 已使用金额
     */
    private BigDecimal usedMoney;




}
