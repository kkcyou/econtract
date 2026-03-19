package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.api.contract.dto.ContractDTO;
import com.yaoan.module.econtract.api.gcy.order.GoodsVO;
import com.yaoan.module.econtract.controller.admin.model.vo.TermsDetailsVo;
import com.yaoan.module.econtract.service.gcy.gpmall.vo.ContractPaymentPlanVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 合同信息对象
 *
 * @author zhc
 * @since 2023-12-05
 */
@Data
public class ContractDataVo {

 /**
  *合同有效期-开始时间
  */
 @Schema(description = "合同有效期-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
 @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
 private Date validity0;

 /**
  *合同有效期-结束时间
  */
 @Schema(description = "合同有效期-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
 @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
 private Date validity1;

 /**
  *金额类型： 0 支出 1 收入
  */
 @Schema(description = "金额类型： 0支出 1收入", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
 private Integer amountType;
 /**
  * 归档
  * 0 - 未归档
  * 1 - 已归档
  */
 @Schema(description = "归档", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
 private Integer document;


 /**
  * 合同有效期
  */
 private Long validity;

 /**
  * 涉密条款 1是 0否
  */
 private Integer secrecyClause;
 /**
  * 合同参数信息集合
  */
 private List<ContractParameterVO> contractParameterVOList;

 /**
  * 合同章信息
  */
 private List<ContractSealVO> contractSealVOList;

 /**
  * 付款信息集合
  */
 private List<PaymentScheduleVO> paymentScheduleVOList;

 /**
  * 合同采购内容集合
  */
 private List<ContractPurchaseReqVO> contractPurchaseReqVOList;

 /**
  * 合同签订方信息集合
  */
 private List<ContractSignatoryReqVO> contractSignatoryReqVOList;
   /**
     * 商品标的对象列表
     */
    @Schema(description = "商品标的对象列表", requiredMode = Schema.RequiredMode.REQUIRED)
    List<GoodsVO> goodsVOS;

//    /**
//     * 退回草稿信息列表
//     */
//    @Schema(description = "退回草稿信息列表", requiredMode = Schema.RequiredMode.REQUIRED)
//    List<ReturnInfoVO> returnInfoList;

    /**
     * 合同信息对象列表
     */
    @Schema(description = "合同信息对象列表", requiredMode = Schema.RequiredMode.REQUIRED)
    ContractDTO contractDTO;
    /**
     * 可变参数
     */
    @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
    Map<String, Object> resultMap;
    /**
     * 模板id
     */
    @Schema(description = "模板id", requiredMode = Schema.RequiredMode.REQUIRED)
    String modelId;

    /**
     * 采购人负责人/采购单位代表 对应 value22    物业管理服务合同模板对应-value23
     */
    @Schema(description = "采购人负责人/采购单位代表/甲方联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerProxy;

    /**
     * 采购人负责人地址 对应 value17
     */
    @Schema(description = "采购人负责人地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deliveryAddress;
    /**
     * 采购人负责人电话 对应 value18
     */
    @Schema(description = "采购人负责人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerLinkMobile;
    /**
     * 供应商负责人 对应 value14
     */
    @Schema(description = "供应商负责人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierProxy;

    /**
     * 供应商负责人地址 对应 value19
     */
    @Schema(description = "供应商负责人地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String registeredAddress;

    /**
     * 供应商负责人电话-对应 value20
     */
    @Schema(description = "供应商负责人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierLinkMobile;



    /**
     * 甲方指派联系人 -甲方代表- 采购人指派联系人 对应 value13   采购人代表
     */
    @Schema(description = "甲方指派联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerLink;
    /**
     * 乙方指派联系人-乙方代表-供应商指派联系人-乙方（供应商）代表 对应 value12 供应商代表
     */
    @Schema(description = "乙方指派联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierLink;

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
     * 签署截止日期
     */
    @Schema(description = "签署截止日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expirationDate;
    /**
     * 合同分类
     */
    private Integer contractCategory;
    /**
     * 用户类型   0:系统管理员,1:采购单位,2:供应商,3:代理机构,4:采购监管机构,5:财政业务部门,6:评审专家,7:金融机构用户
     */
    @Schema(description = "用户类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer userType;
    /**
     * 服务时间
     */
    @Schema(description = "服务时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date serverDate;


    /**
     * 服务地点
     */
    @Schema(description = "服务地点", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String serverAddress;
    /**
     * 补充说明1
     */
    @Schema(description = "补充说明1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplement1;
    /**
     * 补充说明2
     */
    @Schema(description = "补充说明2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplement2;

    /**
     * 合同付款方式:一次付清的，分期付款
     */
    @Schema(description = "合同付款方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer contractPayType;
    /**
     * 分期付款次数(如果合同付款方式是一次付清的，分期付款次数为:1)
     */
    @Schema(description = "分期付款次数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer contractPayCount;
    /**
     * 付款计划
     */
    @Schema(description = "支付计划", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ContractPaymentPlanVo> paymentPlanList;
    /**
     * 阶段支付信息
     */
    @Schema(description = "阶段支付信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<StagePaymentVO> payMentInfo;
    @Schema(description = "项目所属分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectCategoryCode;
 /**
  * 附件集合
  */
 @Schema(description = "附件集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
 private List<AttachmentRelCreateReqVO> attachmentList;

 /**
  * 签署方id集合
  */
 @Schema(description = "签署方id集合", requiredMode = Schema.RequiredMode.REQUIRED)
 private List<SignatoryRelReqVO> signatoryList;

    /**
     * 合同文件内容
     */
    @Schema(description = "合同文件内容")
    private byte[] contractContent;
    /**
     * 合同签订地址
     */
    @Schema(description = "合同签订地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractSignAddress;
    /**
     * 合同签订日期
     */
    @Schema(description = "合同签订日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date contractSignTime;
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
    private String id;

    /**
     * 合同类型【见字典项】，默认传1即可
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractType;
    /**
     * 合同描述 备注
     */
    private String contractDescription;

    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "合同编码不能为空")
    private String code;
    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "合同名称不能为空")
    private String name;
    /**
     * 合同状态
     */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "合同状态不能为空")
    private Integer status;
    private Integer upload;
    private Integer signType;
    private String deposit;
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
    private Integer supplierSize;
    /**
     * 供应商特殊属性
     */
    @Schema(description = "供应商特殊属性")
    private Integer supplierFeatures;

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

    /**
     * 交货地址
     */
    @Schema(description = "交货地点", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String goodsDeliveryaddress;

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
     * 采购人传真
     */
    @Schema(description = "采购人传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orglinkFax;
    /**
     * 采购人区划编码
     */
    @Schema(description = "采购人区划编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionCode;

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
    @Schema(description = "计划id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanId;
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
    @Schema(description = "合同履约开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private String performStartDate;
    /**
     * 合同履约结束日期(精确到日，格式：YYYY-MM-DD)
     */
    @Schema(description = "合同履约结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private String performEndDate;
    /**
     * 订单编号
     */
    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderCode;
    /**
     * 合同开始时间/本合同期限起始时间（起草合同时，由用户在模板中填写）
     */
    @Schema(description = "合同开始时间（起草合同时，由用户在模板中填写）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String startDate;
    /**
     * 合同结束时间/本合同期限终止时间（起草合同时，由用户在模板中填写）
     */
    @Schema(description = "合同结束时间（起草合同时，由用户在模板中填写）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String endDate;
    /**
     * 供应商统一社会信用代码
     */
    private String supplierCode;
    /**
     * 供应商传真
     */
    private String supplierFax;
    /**
     * 供应商所在区域  给区划编码没有给 " "
     */
    private String supplierLocation;

    /**
     * 采购单位传真
     */
    private  String orgFax;

    private List<OrderRespVO> orderList;
    /**
     * 条款列表
     */
    private List<TermsDetailsVo> terms;
    //制造商名称
    private String propertyServiceName;
    //制造商规模
    private String propertyServiceType;
    //制造商区划编码
    private String propertyServiceAddress;
}
