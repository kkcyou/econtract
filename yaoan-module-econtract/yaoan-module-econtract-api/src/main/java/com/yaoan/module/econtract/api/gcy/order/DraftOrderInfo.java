package com.yaoan.module.econtract.api.gcy.order;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/4 15:01
 */
@Data
public class DraftOrderInfo {

    /**
     * 模板id（曜安模版标识）
     */
    @Schema(description = "模板id（曜安模版标识）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String modelId;

    /**
     * 订单id
     */
    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderGuid;

    /**
     * 订单编号
     */
    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderCode;

    /**
     * 订单状态
     */
    @Schema(description = "订单状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderStatus;

    /**
     * 订单状态 - 起草
     */
    @Schema(description = "订单状态 - 起草", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String status;

    /**
     * 订单状态名称
     */
    @Schema(description = "订单状态名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderStatusStr;

    /**
     * 订单总额
     */
    @Schema(description = "订单总额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderTotalAmount;
    /**
     * 成交百分比
     */
    @Schema(description = "成交百分比", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String transactionRatio;


    /**
     * 下单时间--后面对接完框彩，定点协议会去了
     */
    @Schema(description = "下单时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String payTime;
    /**
     * 下单时间
     */
    @Schema(description = "下单时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderCreateTime;
    /**
     * 中标公告时间
     */
    @Schema(description = "中标公告时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bidNoticeDate;


    /**
     * 采购单位
     */
    @Schema(description = "采购单位", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purchaserOrg;

    /**
     * 区划编号
     */
    @Schema(description = "区划编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionCode;

    /**
     * 区划名称
     */
    @Schema(description = "区划名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionFullName;

    /**
     * 区划id
     */
    @Schema(description = "区划id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionGuid;

    /**
     * 供应商id
     */
    @Schema(description = "供应商id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierGuid;

    /**
     * 供应商联系人
     */
    @Schema(description = "供应商联系人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierLinkman;

    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierName;

    /**
     * 交易方式 (使用卖场订单来源枚举)
     */
    @Schema(description = "交易方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String tradingType;

    /**
     * 交易方式名称 (使用卖场订单来源枚举)
     */
    @Schema(description = "交易方式名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String tradingTypeName;

    /**
     * 计划编号
     */
    @Schema(description = "计划编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanCode;

    /**
     * 开户银行账号
     */
    @Schema(description = "开户银行账号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankName;

    /**
     * 开户银行
     */
    @Schema(description = "开户银行", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankAccount;

    /**
     * 项目所属分类（A、B、C）
     */
//    @NotBlank(message = "项目所属分类不能为空")
    @Schema(description = "项目所属分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectCategoryCode;

    /**
     * 项目信息
     */
    @Schema(description = "项目信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private ProjectVO projectVO;

    /**
     * 商品信息
     */
    @Schema(description = "商品信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<GoodsVO> goodsVOList;

    /**
     * 附件信息
     */
    @Schema(description = "附件信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<FileAttachmentVO> fileAttachmentVOList;

    /**
     * 配件信息
     */
    @Schema(description = "配件信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<OrderAccessoryEntity> allAccessoryList;

    /**
     * 工程项目信息
     */
    @Schema(description = "工程项目信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private EngineeringProjectVO engineeringProjectVO;

    /**
     * 采购单位id
     */
    @Schema(description = "采购单位id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purchaserOrgGuid;
    /**
     * 计划名称
     */
    @Schema(description = "计划名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanName;

    /**
     * 计划金额
     */
    @Schema(description = "计划金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal buyPlanAmount;

//    /**
//     * 采购方式
//     */
//    @Schema(description = "采购方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String purchaseMethod;
    /**
     * 场馆标识
     */
    @Schema(description = "场馆标识", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String platform;
    /**
     * 场馆名称
     */
    @Schema(description = "场馆名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String platformName;


    //=========2024-02-18-11:13:12 新增========

    /**
     * 合同来源（电子卖场：gpmall-5.3，电子交易（项目采购）：gpms-gpx-5.3，框采平台：gp-gpfa）
     */
    @NotBlank(message = "合同来源不能为空")
    @Schema(description = "合同来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractFrom;

    /**
     * 关联计划信息
     */
    @NotNull(message = "关联计划信息不能为空")
    @Schema(description = "关联计划信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private AssociatedPlanVO associatedPlanVO;

    /**
     * 合同起草方
     */
//    @NotNull(message = "合同起草方不能为空")
    @Schema(description = "合同起草方", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer contractDrafter;
    /**
     * 供应商社会信用代码/组织机构代码/个人身份证/供应商的纳税人识别号
     */
    @Schema(description = "供应商社会信用代码/组织机构代码/个人身份证/供应商的纳税人识别号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierCode;
    /**
     * 统一社会信用代码/采购人纳税识别号
     */
    @Schema(description = "统一社会信用代码/采购人纳税识别号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
     * 采购人传真
     */
    @Schema(description = "供应商传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierlinkFax;

//    /**
//     * 采购计划唯一识别码
//     */
//    @Schema(description = "采购计划唯一识别码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String buyPlanGuid;
    /**
     * 项目唯一识别码
     */
//
//    @Schema(description = "项目唯一识别码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String projectGuid;
    /**
     * 配件总金额
     */
    @Schema(description = "配件总金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String accessoryMoney;
    /**
     * 选配总金额
     */
    @Schema(description = "选配总金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String optionMoney;
    /*
     * 采购单位联系人
     */
    @Schema(description = "采购单位联系人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgLinkman;
    /**
     * 数据有效状态<0  在途  0完结  >0失效
     */
    @Schema(description = "数据有效状态<0  在途  0完结  >0失效", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String dataState;
    /*8
     * 合同信息对象
     */
    @Schema(description = "合同信息对象", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private ContractDataVo contractDataVo;

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
     * 公司id
     */
    private Long companyId;
}
