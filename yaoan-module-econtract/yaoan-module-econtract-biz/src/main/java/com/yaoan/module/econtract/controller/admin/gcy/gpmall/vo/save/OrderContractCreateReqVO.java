package com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.save;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.contract.vo.PaymentScheduleVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.StagePaymentVO;
import com.yaoan.module.econtract.controller.admin.model.vo.TermsDetailsVo;
import com.yaoan.module.econtract.service.gcy.gpmall.vo.ContractPaymentPlanVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Schema(description = "框架协议/电子卖场合同创建请求参数VO")
@Data
@ToString(callSuper = true)
public class OrderContractCreateReqVO {
    private static final long serialVersionUID = -4532116919770058260L;

    /**
     * id-主键
     */
    @Schema(description = "id-主键", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;
    /**
     * 合同编码
     */
    @NotBlank(message = "合同编码不可为空")
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    /**
     * 合同名称
     */
    @NotBlank(message = "合同名称不可为空")
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    /**
     * 采购计划备案书/标准书编号
     */
    @Schema(description = "采购计划备案书/标准书编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanCode;

    /**
     * 供应商id
     */
    @NotBlank(message = "供应商id不可为空")
    @Schema(description = "供应商id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierId;
    /**
     * 供应商名称
     */
    @NotBlank(message = "供应商名称不可为空")
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierName;

    /**
     * 供应商负责人
     */
    @Schema(description = "供应商负责人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
     * 供应商开户行名称
     */
    @Schema(description = "供应商开户行名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankName;
    /**
     * 供应商开户行账号
     */
    @Schema(description = "供应商开户行账号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankAccount;
    /**
     * 采购人id
     */
    @NotBlank(message = "采购人/采购单位id不可为空")
    @Schema(description = "采购人/采购单位id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgId;
    /**
     * 采购人名称
     */
    @NotBlank(message = "采购单位名称不可为空")
    @Schema(description = "采购人/采购单位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgName;
    /**
     * 采购人负责人。甲方联系人
     */
    @Schema(description = "采购人负责人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
     * 合同签订地址
     */
    @Schema(description = "合同签订地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractSignAddress;
    /**
     * 合同签订日期
     */
    @Schema(description = "合同签订日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractSignTime;
    /**
     * 采购合同项目付款方式方式:国库集中支付，单位直接支付，国库集中支付和单位直接支付 对应 value4
     */
    @Schema(description = "采购合同项目付款方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String paymentMethod;
    /**
     * 合同金额
     */
//    @NotNul       l(message = "合同金额不可为空")
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal totalMoney;
    /**
     * 合同金额大写
     */
    @Schema(description = "合同金额大写", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String shiftMoney;

    /**
     * 备注
     */
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;

    /**
     * 合同文件内容
     */
    @NotNull(message = "合同文件内容不可为空")
    @Schema(description = "合同文件内容")
    private byte[] contractContent;

    /**
     * 关联的模板id
     */
//    @NotNull(message = "关联的模板id不可为空")
    @Schema(description = "关联的模板id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String templateId;


    /**
     * send：是否发送 0：未发送（保存）  1：发送  2：确认
     */
//    @NotNull(message = "是否发送标识不可为空")
    @Schema(description = "是否发送", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer send;


    /**
     * 订单编号
     */
    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderCode;
    /**
     * 订单id
     */
//    @NotBlank(message = "订单id不可为空")
    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderId;
    /**
     * 交易方式:1:直接选定、2:二次竞价、3:顺序轮候 列表给
     */
    @Schema(description = "交易方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String dealWay;


    /**
     * 签署状态
     */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer status;

    /**
     * 采购人区划编码
     */
    @Schema(description = "采购人区划编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionCode;
    /**
     * 采购人区划名称
     */
    @Schema(description = "采购人区划名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionName;
    /**
     * 1:普通合同   默认 2：联系单生成 3：月结合同
     * 合同类型
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer contractType;

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


    //=========第六期新增字段========
    /**
     * 合同是否备案 0：未备案  1：已备案
     */
    @Schema(description = "合同是否备案", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer isExport;
    private String isExport;
    /**
     * 合同是否公示 0：否  1：是
     */
    @Schema(description = "合同是否公示", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer isPublicity;
    private String isPublicity;
    /**
     * 合同是可以签章 0：否  1：是
     */
    @Schema(description = "合同是可以签章", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer isSign;
    private String isSign;
    /**
     * 合同允许采购人选择签章 0：否  1：是
     */
    @Schema(description = "合同允许采购人选择签章", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer isSignChoose;
    private String isSignChoose;
    /**
     * 是否中小企业融资  0：否  1：是
     */
    @Schema(description = "是否中小企业融资", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String isSmallCompany;
//    private Integer isSmallCompany;

    //=========模板新增字段========
    /**
     * 保修期 几年 对应 value11
     */
    @Schema(description = "保修期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String warrantyPeriod;
//    private Integer warrantyPeriod;
    /**
     * 工作日个数 对应 value23  机动车保险服务合同模板对应-value13  网上询价合同模板对应-value26    物业管理服务合同模板对应-value16，甲方应支付货款的工作日个数
     */
    @Schema(description = "工作日个数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer workingDayNum;
    private String workingDayNum;
    /**
     * 甲方指派联系人 -- 采购人指派联系人 、甲方联系人对应 value13   采购人代表。甲方代表
     */
    @Schema(description = "甲方指派联系人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerLink;
    /**
     * 乙方指派联系人-供应商指派联系人 ，乙方联系人对应 value12 供应商（乙方）代表
     */
    @Schema(description = "乙方指派联系人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierLink;

    //====电子反拍合同模板特有====
    /***
     * 其他项目交易名称
     */
    @Schema(description = "其他项目交易名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String otherBussinessName;
    /***
     * 其他项目交易内容
     */
    @Schema(description = "其他项目交易内容", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String otherBussinessValue;
    /**
     * 是否包安装：containInstall 0:否 1：是
     */
    @Schema(description = "是否包安装", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer containInstall;
    private String containInstall;
    /***
     * 合同份数 对应：value31
     */
    @Schema(description = "合同份数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer contractNum;
    private String contractNum;
    /***
     * 争议处理方式 对应：value32, 向仲裁委员会申请仲裁解决，向人民法院提交诉讼解决
     */
    @Schema(description = "争议处理方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String disputesProcessType;
    /**
     * 延长售后服务年限 对应value25--不需要延长，延长1年，延长2年，延长3年，延长4年，延长5年
     */
    @Schema(description = "延长售后服务年限", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String extendedWarrantyDuration;
    /**
     * 付款方式方式:银行转账，货到付款 对应 value4
     */
    @Schema(description = "支付方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String paymentMethod2;

    //====车辆加油服务采购合同模板特有====
    /**
     * 加油业务办理方式  对应-value24
     */
    @Schema(description = "加油业务办理方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String refuelingType;
    /**
     * 优惠内容  对应-value25
     */
    @Schema(description = "优惠内容", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String onSaleContent;
    /**
     * 甲方承担充值金额的违约金比率/甲方支付金额违约比例  -对应value26
     */
    @Schema(description = "甲方承担充值金额的违约金比率", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerDeditRatio;
    /**
     * 乙方承担充值金额的违约金比率 /乙方逾期交货总额违约比例 -对应value27
     */
    @Schema(description = "乙方承担充值金额的违约金比率", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierDeditRatio;

    //====汽车官合同模板特有=====
    /**
     * 保修里程数 对应value12
     */
    @Schema(description = "保修里程数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double warrantyMileage;

    //====物业管理服务合同模板特有======
    /**
     * 物业名称 对应value6
     */
    @Schema(description = "物业名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String propertyServiceName;
    /**
     * 物业类型 对应 value12
     */
    @Schema(description = "物业类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String propertyServiceType;
    /**
     * 物业位置 对应 value13
     */
    @Schema(description = "物业位置", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String propertyServiceAddress;
    /**
     * 建筑面积 对应 value5
     */
    @Schema(description = "建筑面积", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double areaStructure;
    /**
     * 物业管理费 对应 value9
     */
    @Schema(description = "物业管理费", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double propertyServiceFee;
    /**
     * 人员数量：value22
     */
    @Schema(description = "人员数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String personNum;
//    private Integer personNum;
    /**
     * 人员金额：contractExpress.value11
     */
    @Schema(description = "人员金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double personAmount;
    /**
     * 交货地址
     */
    @Schema(description = "交货地点", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String goodsDeliveryaddress;

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
     * 日历天数（服务时间，交货时间）
     */
    @Schema(description = "日历天数（服务时间，交货时间）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String goodsTime;
//    private Integer goodsTime;
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
     * 事故发生后天数
     */
    @Schema(description = "事故发生后天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String accidentDays;
//    private Integer accidentDays;
    /**
     * 不可抗力事件延续天数
     */
    @Schema(description = "不可抗力事件延续天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String continuationDays;
//    private Integer continuationDays;
    /**
     * 是否节能产品
     */
    @Schema(description = "是否节能产品", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String isEnergySavingProducts;
    /**
     * 是否环保产品
     */
    @Schema(description = "是否环保产品", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String isGreenProduct;
    /**
     * 是否节水产品
     */
    @Schema(description = "是否节水产品", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String isWaterSavingProduct;

    /**
     * 供应商所在区域
     */
    @Schema(description = "供应商所在区域", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierLocation;
    /**
     * 供应商所在区域编码
     */
    @Schema(description = "供应商所在区域编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierLocationCode;
    /**
     * 供应商银行行号
     */
    @Schema(description = "供应商银行行号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankNo;
    /**
     * 计划支付日期
     */
    @Schema(description = "计划支付日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date payPlanDate;

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
     * 不可抗力事件处理补充
     */
    @Schema(description = "不可抗力事件处理补充", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String forceMajeureEventReplenish;
    /**
     * 违约责任补充
     */
    @Schema(description = "违约责任补充", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String violateAppointDutyReplenish;
    /**
     * 乙方逾期超过约定的工作日个数
     */
    @Schema(description = "乙方逾期超过约定的工作日个数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer supplierWorkingDayNum2;
    private String supplierWorkingDayNum2;
    /**
     * 乙方支付合同总值违约比例
     */
    @Schema(description = "乙方支付合同总值违约比例", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierPayRatio;
    /**
     * 乙方进行合格验收工作日个数
     */
    @Schema(description = "乙方进行合格验收工作日个数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer supplierWorkingDayNum1;
    private String supplierWorkingDayNum1;
    /**
     * 甲方支付拒收贷款总值违约比例
     */
    @Schema(description = "甲方支付拒收贷款总值违约比例", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerRefusePayRatio;
    /**
     * 甲方支付金额违约比例
     */
    @Schema(description = "甲方支付金额违约比例", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerPayRatio;


    /**
     * 合同文件id
     */
    @Schema(description = "合同文件id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long pdfFileId;

    /**
     * 付款计划信息集合
     */
    @Schema(description = "付款计划信息集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<PaymentScheduleVO> paymentScheduleVOList;
    /**
     * 采购标的信息--ContractGoodsMapper
     */
    @Schema(description = "采购标的信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SuperviseGoodsVO> goodsList;
    /**
     * 合同有效期-开始时间
     */
    @Schema(description = "合同有效期-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date validity0;

    /**
     * 合同有效期-结束时间
     */
    @Schema(description = "合同有效期-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date validity1;
    /**
     * 合同分类  1：货物，2：工程 3：服务
     */
    @Schema(description = "合同分类", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractCategory;
//    private Integer contractCategory;
    /**
     * 签署截止日期
     */
    @Schema(description = "签署截止日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expirationDate;
    /**
     * 任务编号
     */
    @Schema(description = "任务编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String taskId;

    @Schema(description = "审批意见", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reason;


    //黑龙江
    @Schema(description = "服务时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date serverDate;
    /**
     * 服务地点
     */
    @Schema(description = "服务地点", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String serverAddress;
    /**
     * 甲方电子邮箱
     */
    @Schema(description = "甲方电子邮箱", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerEmail;
    /**
     * 甲方开户银行
     */
    @Schema(description = "甲方开户银行", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgBankName;
    /**
     * 甲方开户银行账号
     */
    @Schema(description = "甲方开户银行账号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgBankAccount;
    /**
     * 甲方账号名称
     */
    @Schema(description = "甲方账号名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgBankAccountName;
    /**
     * 甲方邮政编码
     */
    @Schema(description = "甲方邮政编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgPostalCode;
    /**
     * 乙方账号名称
     */
    @Schema(description = "乙方账号名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierBankAccountName;
    /**
     * 乙方邮政编码
     */
    @Schema(description = "乙方邮政编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierPostalCode;
    /**
     * 乙方电子邮箱
     */
    @Schema(description = "乙方电子邮箱", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierEmail;
    /**
     * 交货时间（ 日期）
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Schema(description = "交货时间（ 日期）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date goodsDeliveryTime;
    /**
     * 验收标准补充说明
     */
    @Schema(description = "验收标准补充说明", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplement1;
    /**
     * 合同补充说明
     */
    @Schema(description = "付款方式补充说明", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String Supplement2;
    /**
     * 采购计划号、计划明细唯一识别码
     */
    @Schema(description = "采购计划号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanBillGuid;
    /**
     * 货物的运输方式
     */
    @Schema(description = "货物的运输方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String goodsTransportType;
    /**
     * 验收异议解决工作日个数
     */
    @Schema(description = "验收异议解决工作日个数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String acceptanceProblemDealDays;
//    private Integer acceptanceProblemDealDays;
//    /**
//     * 货物保修开始时间
//     */
//    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
//    @Schema(description = "货物保修开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Date goodsWarrantyStartDate;
//    /**
//     * 货物保修结束时间
//     */
//    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
//    @Schema(description = "货物保修结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Date goodsWarrantyEndDate;
    /**
     * 本合同期限起止时间
     */
    private String contractTerm;
    /**
     * 货物保修起止时间
     */
    @Schema(description = "货物保修起止时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String warranty;
    /**
     * 资金性质
     */
    @Schema(description = "资金性质", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String capitalNature;
    /**
     * 货物质量
     */
    @Schema(description = "可解除合同天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer canCancelDays;
    private String canCancelDays;
    /**
     * 货物质量
     */
    @Schema(description = "招标编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bidCode;

    /**
     * 黑龙江采购方式 (如 比价采购)
     */
    @Schema(description = "采购方式 (如 比价采购)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purchaseMethod2;
    /**
     * 项目名称
     */
    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectName;
//    /**
//     * 项目编号
//     */
//    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String projectCode;
    /**
     * 工程地点
     */
    @Schema(description = "工程地点", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectAddress;

    /**
     * 承包范围
     */
    @Schema(description = "承包范围", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractingExtent;
    /**
     * 承包方式
     */
    @Schema(description = "承包方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractingSystem;

    /**
     * 本工程竣工起始时间
     */
//    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
    @Schema(description = "本工程竣工起始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String workStartDate;
    private String workDate;
    /**
     * 本工程竣工终止时间
     */
//    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
//    @Schema(description = "本工程竣工终止时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Date workEndDate;
    /**
     * 工程竣工起止天数
     */
    @Schema(description = "工程竣工起止天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer workDays;
    private String workDays;
    /**
     * 工程质量描述
     */
    @Schema(description = "工程质量描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark10;
    /**
     * 服务项目及要求补充
     */
    @Schema(description = "服务项目及要求补充", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark9;
    /**
     * 本合同期限起始时间
     */
    @Schema(description = "合同期限起止天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer contractDays;
    private String contractDays;
    /**
     * 采购结果有效期年限
     */
    @Schema(description = "采购结果有效期年限", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer purchaseResultValidity;
    private String purchaseResultValidity;
    /**
     * 自筹资金
     */
    @Schema(description = "自筹资金", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double selfRaisedFunds;

    /**
     * 阶段支付信息
     */
    @Schema(description = "阶段支付信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<StagePaymentVO> payMentInfo;
    /**
     * 发票类型
     */
    @Schema(description = "发票类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String invoiceType;
    /**
     * 双方权利义务和质量保证补充说明
     */
    @Schema(description = "双方权利义务和质量保证补充说明", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark1;
    /**
     * 知识产权归属方
     */
    @Schema(description = "知识产权归属方", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String knowledgePropertyOwner;
    /**
     * 知识产权归属补充说明
     */
    @Schema(description = "知识产权归属补充说明", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark2;
    /**
     * 乙方支付违约金金额（保密条款）
     */
    @Schema(description = "乙方违约金金额（保密条款）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String deditAmount;
    /**
     * 验收补充说明
     */
    @Schema(description = "验收补充说明", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark3;
    /**
     * 验收补充说明
     */
    @Schema(description = "验收补充说明", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark4;
    /**
     * 乙方赔偿违约金比例（未按合同提供服务）
     */
    @Schema(description = "乙方赔偿违约金比例（未按合同提供服务）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierCompensationRatio;
    /**
     * 乙方违约服务款限定比例（未按合同提供服务）
     */
    @Schema(description = "乙方违约服务款限定比例（未按合同提供服务）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierDeditLimitRatio;
    /**
     * 乙方未按合同提供服务超期天数
     */
    @Schema(description = "乙方未按合同提供服务超期天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer supplierNoServiceOverDays;
    private String supplierNoServiceOverDays;
    /**
     * 乙方应支付违约金金额（未按合同提供服务）
     */
    @Schema(description = "乙方应支付违约金金额（未按合同提供服务）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierPayDeditAmount;
    /**
     * 甲方赔偿违约金比例（延期付服务款）
     */
    @Schema(description = "甲方赔偿违约金比例（延期付服务款）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgCompensationRatio;
    /**
     * 甲方逾期付款违约金累计支付费用限定比例（延期付服务款）
     */
    @Schema(description = "甲方逾期付款违约金累计支付费用限定比例（延期付服务款）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgDeditLimitRatio;
    /**
     * 双方违约金赔付比例（不能履行或擅自解除合同）
     */
    @Schema(description = "双方违约金赔付比例（不能履行或擅自解除合同）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String compensationRatio;
    /**
     * 延迟履约方不可抗力发生后应出示证明文件天数
     */
    @Schema(description = "延迟履约方不可抗力发生后应出示证明文件天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String accidentStartDays;
//    private Integer accidentStartDays;
    /**
     * 延迟履约方不可抗力结束后应出示证明文件天数
     */
    @Schema(description = "延迟履约方不可抗力结束后应出示证明文件天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String accidentEndDays;
//    private Integer accidentEndDays;
    /**
     * 仲裁委员会类型
     */
    @Schema(description = "仲裁委员会类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String arbitrationCommissionType;
    /**
     * 人民法院类型
     */
    @Schema(description = "人民法院类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String courtType;
    /**
     * 投标人承诺具体事项
     */
    @Schema(description = "投标人承诺具体事项", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark5;
    /**
     * 售后服务具体事项
     */
    @Schema(description = "售后服务具体事项", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark6;
    /**
     * 保修期责任
     */
    @Schema(description = "保修期责任", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark7;
    /**
     * 其他具体事项
     */
    @Schema(description = "其他具体事项", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark8;
    /**
     * 甲方开工前提供材料天数
     */
    @Schema(description = "甲方开工前提供材料天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer orgMaterialsDays;
    private String orgMaterialsDays;
    /**
     * 甲方开工前提供材料份数
     */
    @Schema(description = "甲方开工前提供材料份数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgMaterialsNum;
//    private Integer orgMaterialsNum;
    /**
     * 甲方驻工地代表
     */
    @Schema(description = "甲方驻工地代表", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgResidentRepresentative;
    /**
     * 工程监理公司（甲方）
     */
    @Schema(description = "工程监理公司（甲方）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgPrejectSupCompany;
    /**
     * 总监理工程师（ 甲方）
     */
    @Schema(description = "总监理工程师（ 甲方）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgChiefSupervisoryEngineer;
    /**
     * 交给乙方的监理合同副本份数
     */
    @Schema(description = "交给乙方的监理合同副本份数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer contractCopyNum;
    private String contractCopyNum;
    /**
     * 乙方应向甲方提交施工相关材料天数
     */
    @Schema(description = "乙方应向甲方提交施工相关材料天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer supMaterialsDays;
    private String supMaterialsDays;
    /**
     * 工程项目班子成员（ 乙方）
     */
    @Schema(description = "工程项目班子成员（ 乙方）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectTeamMember;
    /**
     * 项目经理姓名（ 乙方）
     */
    @Schema(description = "项目经理姓名（ 乙方）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectManagerName;
    /**
     * 项目经理级别（ 乙方）
     */
    @Schema(description = "项目经理级别（ 乙方）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectManagerLevel;
    /**
     * 项 目经理职务 (职称)（ 乙方）
     */
    @Schema(description = "项目经理职务 (职称)（ 乙方）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectManagerPositio;
    /**
     * 技术负责人姓名（ 乙方）
     */
    @Schema(description = "技术负责人姓名（ 乙方）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String technicalDirectorName;
    /**
     * 技术负责人职称（ 乙方）
     */
    @Schema(description = "技术负责人职称（ 乙方）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String technicalDirectorTitle;
    /**
     * 甲方接到通知后处理天数（接到通知x日内进行处理）
     */
    @Schema(description = "甲方接到通知后处理天数（接到通知x日内进行处理）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgDealDaysByWarrantyInform;
//    private Integer orgDealDaysByWarrantyInform;
    /**
     * 乙方收到书面异议后处理天数（收到书面异议后几天内解决）
     */
    @Schema(description = "乙方收到书面异议后处理天数（收到书面异议后几天内解决）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer supDealDaysByGetBooks;
    private String supDealDaysByGetBooks;
    /**
     * 乙方提供相关材料的截止时间（在 xxx时间前）
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Schema(description = "乙方提供相关材料的截止时间（在 xxx时间前）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date supSubmitMaterialEndTime;
    /**
     * 乙方提供的竣工图纸截止时间（在 xxxx前）
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Schema(description = "乙方提供的竣工图纸截止时间（在 xxxx前）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date supSubmitDrawingEndTime;
    /**
     * 乙方提供的竣工图纸份数
     */
    @Schema(description = "乙方提供的竣工图纸份数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer supSubmitDrawingEndNum;
    private String supSubmitDrawingEndNum;
    /**
     * 甲方预留质量保证金占本合同合计金额的比例
     */
    @Schema(description = "甲方预留质量保证金占本合同合计金额的比例", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String retentionAndContractRatio;
    /**
     * 质量保证金补充说明
     */
    @Schema(description = "质量保证金补充说明", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark11;
    /**
     * 工程质量保修期年限
     */
    @Schema(description = "工程质量保修期年限", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer projectWarrantyYears;
    private String projectWarrantyYears;
    /**
     * 乙方收到保修通知后进行保修天数（收到通知后x天内进行保修）
     */
    @Schema(description = "乙方收到保修通知后进行保修天数（收到通知后x天内进行保修）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer supDealDaysByWarrantyInform;
    private String supDealDaysByWarrantyInform;
    /**
     * 乙方收到维修通知后进行维修天数（收到通知后x天内进行维修）
     */
    @Schema(description = "乙方收到维修通知后进行维修天数（收到通知后x天内进行维修）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer supDealDaysByMaintainInform;
    private String supDealDaysByMaintainInform;
    /**
     * 甲方停工一天应支付金额
     */
    @Schema(description = "甲方停工一天应支付金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double orgPayAmountByStopOneDay;
    /**
     * 资金类型
     */
    @Schema(description = "资金类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String amountType;
    /**
     * 甲方逾期一天应支付的滞纳金比例
     */
    @Schema(description = "甲方逾期一天应支付的滞纳金比例", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgOverduePayAountRatio;
    /**
     * 乙方逾期竣工一天应支付违约金额
     */
    @Schema(description = "乙方逾期竣工一天应支付违约金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double supPayAmountByStopOneDay;
    /**
     * 其他违约行为应付违约金比例
     */
    @Schema(description = "其他违约行为应付违约金比例", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String otherPayRatio;
    /**
     * 支付计划
     */
    @Schema(description = "支付计划", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ContractPaymentPlanVo> paymentPlanList;

    /**
     * 合同来源（电子卖场：gpmall-5.3，电子交易（项目采购）：，框采平台：gp-gpfa）
     */
//    @NotBlank(message = "合同来源不能为空")
    @Schema(description = "合同来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractFrom;
    /**
     * 合同文本是否包含涉密条款(1:是,0:否)
     */
    @Schema(description = "合同文本是否包含涉密条款(1:是,0:否)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer clauseSecret;
    private String clauseSecret;

    /**
     * 合同签订日期
     */
    @Schema(description = "合同签订日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String datenow;
    /**
     * 采购组织形式(参见选项字典【Kind】定义)
     */
    @Schema(description = "采购组织形式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyOrgForm;
    @Schema(description = "是否缴纳履约保证金")
    private Integer isPerformanceMoney;
    //履约保证金缴纳方式
//1 银行转账
//2 支票（汇票、本票）
//3 保函（保险）
    private String performanceMoneyType;
    //履约保证金金额
    private Double performanceMoney;
    /**
     * 条款列表
     */
    private List<TermsDetailsVo> terms;
}
