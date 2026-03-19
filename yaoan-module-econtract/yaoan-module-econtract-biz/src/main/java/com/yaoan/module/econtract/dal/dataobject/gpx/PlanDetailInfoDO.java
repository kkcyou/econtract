package com.yaoan.module.econtract.dal.dataobject.gpx;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/29 15:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gpx_plan_detail")
public class PlanDetailInfoDO  extends BaseDO {
    private static final long serialVersionUID = -5476362972090768716L;
    /**
     * 主键id
     */
    @TableId(value = "id")
    private String id;
    /**
     * 包id
     */
    private String packageId;
    /**
     * 计划ID
     */
    private String planId;
    /**
     * 计划合同包号
     */
    private String planPackageNumber;
    /**
     * 品目编号
     */
    private String catalogueCode;
    /**
     * 品目名称
     */
    private String catalogueName;
    /**
     * 明细名称
     */
    private String deatilName;
    /**
     * 规格型号
     */
    private String model;
    /**
     * 数量
     */
    private BigDecimal detailNumber;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 货物计量单位
     */
    private String unit;
    /**
     * 预算（总价）
     */
    private BigDecimal budgetMoney;
    /**
     * 排序序号
     */
    private Integer detailSequence;
    /**
     * 是否涉及进口产品 0/1 否/是
     */
    private Integer isImported;
    /**
     * 是否核心产品 0/1 否/是
     */
    private Integer isCoreProduct;
    /**
     * 权重保留两位小数
     */
    private BigDecimal weight;
    /**
     * 最高限价
     */
    private BigDecimal limitAmount;
    /**
     * 商品库编码
     */
    private String detailCode;

    /**
     * 商品品牌
     */
    private String brand;
    /**
     * 技术参数
     */
    private String parameter;
    /**
     * 计划明细外部id
     */
    private String outsiteId;
    /**
     * 联合采购的虚拟计划明细最终落到的实际项目明细id
     */
    private String projectDetailId;
    /**
     * 统一编码
     */
    private String unifiedCode;
    /**
     * 是否专用产品
     */
    private String isPurpose;
    /**
     * 是否提供样品
     */
    private String isSample;
    /**
     * 专用产品参考或标准规范
     */
    private String purposeStandard;
    /**
     * 样品要求与相关事项
     */
    private String sampleRequirements;
  }
