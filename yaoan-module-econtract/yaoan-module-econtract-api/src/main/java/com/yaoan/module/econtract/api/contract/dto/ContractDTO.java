package com.yaoan.module.econtract.api.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同基本信息对象
 *
 * @author zhc
 * @since 2023-12-05
 */
@Data
public class ContractDTO {
    /**
     * 采购人区划名称
     */
    @Schema(description = "采购人区划名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionName;
    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "合同id不能为空")
    private String contractGuid;

    /**
     * 合同类型【见字典项】，默认传1即可
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "合同类型不能为空")
    private Integer contractType;

    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "合同编码不能为空")
    private String contractCode;
    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "合同名称不能为空")
    private String contractName;
    /**
     * 合同状态
     */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "合同状态不能为空")
    private Integer auditStatus;
    /**
     * 合同总金额
     */
    @Schema(description = "合同总金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "合同总金额不能为空")
    private BigDecimal totalMoney;
    /**
     * 合同金额大写
     */
    @Schema(description = "合同金额大写", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String shiftMoney;
    /**
     * 合同开始时间（起草合同时，由用户在模板中填写）
     */
    @Schema(description = "合同开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date startDate;
    /**
     * 合同结束时间（起草合同时，由用户在模板中填写）
     */
    @Schema(description = "合同结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date endDate;
    /**
     * 合同线下签订时间
     */
    @Schema(description = "合同线下签订时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date signDate;
    /**
     * 合同签订地址
     */
    @Schema(description = "合同签订地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String signAddress;

    /**
     * 供应商id
     */
    @Schema(description = "供应商id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierGuid;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierName;
    /**
     * 供应商代表
     */
    @Schema(description = "供应商代表", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierProxy;
    /**
     * 供应商负责人地址
     */
    @Schema(description = "供应商负责人地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String registeredAddress;

    /**
     * 供应商负责人电话
     */
    @Schema(description = "供应商负责人电话", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierLinkMobile;
    /**
     * 开户银行
     */
    @Schema(description = "开户银行", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开户银行不能为空")
    private String bankName;
    /**
     * 开户银行账号
     */
    @Schema(description = "开户银行账号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开户银行账号不能为空")
    private String bankAccount;
    /**
     * 供应商规模
     * 1 大规模
     */
    @Schema(description = "供应商规模")
    private String supplierSize;
    /**
     * 供应商特殊属性
     */
    @Schema(description = "供应商特殊属性")
    private String supplierFeatures;
    /**
     * 供应商所在区域
     */
    @Schema(description = "供应商所在区域")
    private String supplierLocation;
    /**
     * 支付方式【见字典项】默认传1即可
     */
    @Schema(description = "支付方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "支付方式不能为空")
    private Integer payType;
    /**
     * 分期付款次数
     */
    @Schema(description = "分期付款次数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer payCount;
    /**
     * 支付审核比率(百分率0～100)
     */
    @Schema(description = "支付审核比率", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal auditRatio;
    /**
     * 合同确认时间:单位/采购人确认时间
     */
    @Schema(description = "合同确认时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "合同确认时间不能为空")
    private Date sureTime;
    /**
     * 备注
     */
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;
    /**
     * 供应商签订时间
     */
    @Schema(description = "供应商签订时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date supSinTime;
    /**
     * 采购单位签订时间
     */
    @Schema(description = "采购单位签订时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date orgSinTime;
    /**
     * 合同文件地址
     */
    @Schema(description = "合同文件地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String pdfFilePath;
    /**
     * 是否需要签章（1：是，0或null：否）
     */
    @Schema(description = "是否需要签章", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer doSign;
    /**
     * 合同来源（电子卖场：gpmall-5.3，电子交易（项目采购）：，框采平台：gp-gpfa）
     */
    @NotBlank(message = "合同来源不能为空")
    @Schema(description = "合同来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractFrom;
    //=========2024-02-27 框彩新增===============
    //===框彩-直接选定模板/二次竞价==================
    /**
     * 框架协议名称
     */
    @Schema(description = "框架协议名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String agreementName;
    /**
     * 框架协议编号
     */
    @Schema(description = "协议编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String agreementCode;
    /**
     * 交货地址
     */
    @Schema(description = "交货地点", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String goodsDeliveryaddress;
    /**
     * 支付开户银行
     */
    @Schema(description = "支付开户银行", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String payPlanBank;
    /**
     * 支付开户名
     */
    @Schema(description = "支付开户名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String payPlanbAccount;
    /**
     * 支付银行账号
     */
    @Schema(description = "支付银行账号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String payPlanBankAccount;
    /**
     * 采购单位id
     */
    @Schema(description = "采购单位id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerOrgGuid;
    /**
     * 采购单位名称
     */
    @Schema(description = "采购单位名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerOrgName;
    /**
     * 采购单位代表
     */
    @Schema(description = "采购单位代表", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerProxy;
    /**
     * 采购人负责人地址
     */
    @Schema(description = "采购人负责人地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String deliveryAddress;
    /**
     * 采购人负责人电话
     */
    @Schema(description = "采购人负责人电话", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerLinkMobile;
    /**
     * 采购人传真
     */
    @Schema(description = "采购人传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orglinkFax;
    /**
     * 采购人区划编码
     */
    @Schema(description = "采购人区划编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionCode;

    //=====顺序轮候模板==================
    /**
     * 征集人
     */
    @Schema(description = "征集人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String ownerOrgName;

    /**
     * 入围供应商
     */
    @Schema(description = "入围供应商", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String becomeSupplierName;
    /**
     * 合同文件pdf 地址id
     */
    @Schema(description = "合同文件pdf地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long pdfFileId;

    /**
     * 是否缴纳履约保证金
     */
    @Schema(description = "是否缴纳履约保证金")
    private Integer isPerformanceMoney;
    /**
     * 是否缴纳质量保证金
     */
    @Schema(description = "是否缴纳质量保证金")
    private Integer isRetentionMoney;
    /**
     * 合同多方支付方式
     */
    @Schema(description = "合同多方支付方式")
    private String multiAccountPayType;
    /**
     * 采购标项Guid
     */
    @Schema(description = "采购标项Guid")
    private String buyPlanBillGuid;
    /**
     * 是否面向中小企业采购
     */
    @Schema(description = "是否面向中小企业采购")
    private String isReserve;
    /**
     * 包的唯一识别码
     */
    @Schema(description = "包的唯一识别码")
    private String bidGuid;
    /**
     * 开标时间
     */
    @Schema(description = "开标时间")
    private String bidOpenTime;
    /**
     * 中标（成交）时间
     */
    @Schema(description = "中标（成交）时间")
    private String bidResultDate;
    /**
     * 中标（成交）金额
     */
    @Schema(description = "中标（成交）金额")
    private Double bidResultMoney;
    /**
     * 是否涉密采购
     */
    @Schema(description = "是否涉密采购")
    private Integer secret;
    /**
     * 是否政府采购融资抵押
     */
    @Schema(description = "是否政府采购融资抵押")
    private String allowMortgage;
    /**
     * 采购方式
     */
    @Schema(description = "采购方式")
    private String purchaseMethod;
    /**
     * 采购分类==项目所属分类
     */
    @Schema(description = "采购分类", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purCatalogType;
    /**
     * 采购计划备案书/核准书编号
     */
    @Schema(description = "采购计划备案书/核准书编号")
    private String buyPlanCode;

    private Date datenow;
    /**
     * 成交百分比
     */
    private String transactionRatio;
    /**
     * 项目id
     */
    @NotBlank(message = "项目id不能为空")
    @Schema(description = "项目id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectGuid;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectName;
    /**
     * 项目编号
     */
    @NotBlank(message = "合项目编号不能为空")
    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectCode;
    /**
     * 采购组织形式
     */
    @Schema(description = "采购组织形式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String kind;
    /**
     * 是否给与小微企业评审优惠
     */
    @Schema(description = "是否给与小微企业评审优惠", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String isReserveDiscounts;
    /**
     *  中标（成交）采购标的制造商是否为中小企业
     */
    @Schema(description = "中标（成交）采购标的制造商是否为中小企业", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String isSmallBusiness;
    /**
     * A:货物  B：工程 C：服务
     */
    @Schema(description = "所属分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectCategoryCode;


}
