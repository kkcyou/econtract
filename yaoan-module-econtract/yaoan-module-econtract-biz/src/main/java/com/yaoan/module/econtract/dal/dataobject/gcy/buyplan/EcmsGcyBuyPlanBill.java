package com.yaoan.module.econtract.dal.dataobject.gcy.buyplan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 计划采购清单明细表
 * </p>
 *
 * @author doujiale
 * @since 2024-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gcy_buy_plan_bill")
public class EcmsGcyBuyPlanBill extends BaseDO {


    private static final long serialVersionUID = 6531403562960489759L;
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
     * 总金额(元)
     */
    private Double totalPrice;

    /**
     * 采购数量
     */
    private Integer num;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 单价(元) (总价/数量作为最高限价，在小数点后6位截尾)。
     */
    private Double price;

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

}
