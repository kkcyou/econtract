package com.yaoan.module.econtract.dal.dataobject.gcy.gpmall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.module.econtract.enums.order.GCYOrderStatusEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * @description: 卖场订单信息实体类
 * @author: Pele
 * @date: 2023/12/3 19:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gcy_electronic_mall_order")
public class DraftOrderInfoDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = -7487889920418455906L;
    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 模板id（曜安模版标识）
     */
    private String modelId;

    /**
     * 订单编码
     */
    private String orderGuid;

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 订单状态名称
     */
    private String orderStatusStr;

    /**
     * 订单总额
     */
    private BigDecimal orderTotalAmount;
    /**
     * 成交百分比
     */
    private String transactionRatio;

    /**
     * 下单时间
     */
    private LocalDateTime payTime;
    /**
     * 下单时间
     */
    @Schema(description = "下单时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime orderCreateTime;

    /**
     * 采购单位
     */
    private String purchaserOrg;

    /**
     * 采购单位id
     */
    private String purchaserOrgGuid;

    /**
     * 区划编号
     */
    private String regionCode;

    /**
     * 区划名称
     */
    private String regionFullName;

    /**
     * 区划id
     */
    private String regionGuid;

    /**
     * 供应商id
     */
    private String supplierGuid;

    /**
     * 供应商联系人
     */
    private String supplierLinkman;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 交易方式 (使用卖场订单来源枚举)
     */
    private String tradingType;

    /**
     * 交易方式名称 (使用卖场订单来源枚举)
     */
    private String tradingTypeName;

    /**
     * 计划编号-如果此订单没使用计划就无此字段
     */
    private String buyPlanCode;

    /**
     * 计划名称-如果此订单没使用计划就无此字段
     */
    private String buyPlanName;

    /**
     * 计划金额-如果此订单没使用计划就无此字段
     */
    private BigDecimal buyPlanAmount;

    /**
     * 开户银行账号
     */
    private String bankName;

    /**
     * 开户银行
     */
    private String bankAccount;

//    /**
//     * 采购方式
//     */
//    private String purchaseMethod;

    /**
     * 订单状态（0=待起草，1=已起草合同）
     * {@link GCYOrderStatusEnums}
     */
    private String status;

    /**
     * 场馆标识
     */
    private String platform;
    /**
     * 场馆名称
     */
    private String platformName;

    //=========2024-02-18-11:13:12 新增========

    /**
     * 合同来源（电子卖场：gpmall-5.3，电子交易（项目采购）：gpms-gpx-5.3，框采平台：gp-gpfa）
     */
    private String contractFrom;

    /**
     * 合同起草方：采购人（1）/供应商（2）。默认为供应商
     */
    private Integer contractDrafter;
    /**
     * 供应商社会信用代码/组织机构代码/个人身份证/供应商的纳税人识别号
     */
    @Schema(description = "供应商社会信用代码/组织机构代码/个人身份证/供应商的纳税人识别号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String	supplierCode;
    /**
     * 采购人统一社会信用代码/采购人纳税识别号
     */
    @Schema(description = "采购人统一社会信用代码/采购人纳税识别号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgCode;
    /**
     * 采购人开户名称
     */
    @Schema(description = "采购人开户名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgAccountName;
    /**
     * 供应商开户名称
     */
    @Schema(description = "供应商开户名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierAccountName;
    /**
     * 供应商对应外商投资类型
     */
    @Schema(description = "供应商对应外商投资类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String foreignInvestmentType;
    /**
     * 采购人传真
     */
    @Schema(description = "采购人传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orglinkFax;
    /**
     * 供应商传真
     */
    @Schema(description = "供应商传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierlinkFax;
//    @Schema(description = "计划来源(参见选项字典【BuyPlanSource】定义)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String buyPlanSource;
//    @Schema(description = "计划来源名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String buyPlanSourceName;
//    @Schema(description = "采购计划唯一识别码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String buyPlanGuid;
//    @Schema(description = "项目唯一识别码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String projectGuid;

    @Schema(description = "配件总金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal accessoryMoney;
    @Schema(description = "选配总金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal optionMoney;
    //    @Schema(description = "采购单位联系人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String orgLinkman;
    @Schema(description = "数据有效状态<0  在途  0完结  >0失效", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String dataState;
    /**
     * A:货物  B：工程 C：服务
     */
    private Long companyId;

    /**
     * 订单分类（
     * purchase=采购
     * sale=销售
     * rent=租赁
     * hr=人事
     * science=科研
     * other=其他
     * ）
     */
    private String orderCategory;
    /**
     * 项目所属分类（A、B、C）
     */
    private String projectCategoryCode;

    /**
     * 是否需要起草 0否 1是
     */
    @Schema(description = "是否需要起草", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer isDraft;
    /**
     * 是否重新生成合同 0否 1是
     */
    @Schema(description = "是否重新生成合同", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer IsRebuildContract;
    /**
     * 中标公告时间
     */
    @Schema(description = "中标公告时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime bidNoticeDate;
}
