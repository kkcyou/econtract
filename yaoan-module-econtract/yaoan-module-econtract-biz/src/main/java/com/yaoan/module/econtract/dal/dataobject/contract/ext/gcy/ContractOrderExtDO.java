package com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/6 16:10
 */


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.ContractDrafterEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 内蒙-电子卖场、框架协议合同拓展信息实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_order_ext")
public class ContractOrderExtDO extends BaseDO {
    private static final long serialVersionUID = -4578042813519175214L;

    /**
     * id-主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 签署状态
     */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @TableField("`status`")
    private Integer status;
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @TableField("`name`")
    private String name;
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @TableField("`code`")
    private String code;
    /**
     * 合同金额
     */
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal totalMoney;
    /**
     * 合同金额大写
     */
    @Schema(description = "合同金额大写", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String shiftMoney;
    /**
     * 合同开始时间（起草合同时，由用户在模板中填写） 对应-value7
     */
    @Schema(description = "合同开始时间（起草合同时，由用户在模板中填写）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date startDate;
    /**
     * 合同结束时间（起草合同时，由用户在模板中填写） -对应-value8
     */
    @Schema(description = "合同结束时间（起草合同时，由用户在模板中填写）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date endDate;
    /**
     * 采购单位id
     */
    @Schema(description = "采购单位id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgId;
    /**
     * 采购人名称
     */
    @Schema(description = "采购单位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgName;
    /**
     * 采购人负责人/采购单位联系人 对应 value22    物业管理服务合同模板对应-value23
     */
    @Schema(description = "采购人负责人/采购单位联系人", requiredMode = Schema.RequiredMode.REQUIRED)
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
     * 采购计划备案书/标准书编号
     */
    @Schema(description = "采购计划备案书/标准书编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanCode;

    /**
     * 供应商id
     */
    @Schema(description = "供应商id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierId;
    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierName;

    /**
     * 供应商负责人 ，乙方委托代理人对应 对应 value14
     */
    @Schema(description = "供应商负责人，乙方委托代理人", requiredMode = Schema.RequiredMode.REQUIRED)
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
     * 供应商开户行名称
     */
    @Schema(description = "供应商开户行名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankName;
    /**
     * 供应商开户行账号
     */
    @Schema(description = "供应商开户行账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankAccount;

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
     * 发送时间
     */
    @Schema(description = "发送时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date sendTime;

    /**
     * 采购合同项目付款方式方式:国库集中支付，单位直接支付，国库集中支付和单位直接支付 对应 value4
     */
    @Schema(description = "采购合同项目付款方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String paymentMethod;

    /**
     * 付款方式:银行转账，货到付款 对应 value4
     */
    @Schema(description = "支付方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String paymentMethod2;
    /**
     * 采购方式
     */
    @Schema(description = "采购方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String purchaseMethod;

    /**
     * 累计已付金额
     */
    @Schema(description = "累计已付金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal payedAmount;


//    /**
//     * 实施形式，1：项目采购，2：框架协议采购，3：电子卖场
//     */
//    @Schema(description = "实施形式", requiredMode = Schema.RequiredMode.REQUIRED)
//    private String implementationForm;

    /**
     * 备注
     */
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;

    /**
     * 合同文件pdf 地址id
     */
    @Schema(description = "合同文件pdf地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long pdfFileId;
    /**
     * 合同文件地址
     */
    @Schema(description = "合同文件地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String pdfFilePath;
    /**
     * 合同文件内容
     */
//    @NotBlank(message = "合同文件内容不可为空")
    @Schema(description = "合同文件内容")
    private byte[] contractContent;

    /**
     * 关联的模板id
     */
    @Schema(description = "关联的模板id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String templateId;
    /**
     * 订单编号
     */
    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderCode;
    /**
     * 订单id
     */
    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderId;
    /**
     * 项目id，以此查询项目信息
     */
    @Schema(description = "项目id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectGuid;
    /**
     * 交易方式
     */
    @Schema(description = "交易方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String dealWay;

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
     * 确认时间-采购人
     */
    @Schema(description = "确认时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date sureTime;
    /**
     * 供应商签章时间
     */
    @Schema(description = "供应商签章时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date supSinTime;
    /**
     * 采购人签章时间
     */
    @Schema(description = "采购人签章时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date orgSinTime;

    /**
     * 流程实例id
     */
    @Schema(description = "流程实例id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String processInstanceId;


    //=========第六期新增字段========
//    /**
//     * 合同是否备案 0：未备案  1：已备案
//     */
//    @Schema(description = "合同是否备案", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer isExport;
    /**
     * 合同是否公示 0：否  1：是
     */
    @Schema(description = "合同是否公示", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer isPublicity;
    /**
     * /**
     * 是否需要签章（1：是，0或null：否）
     */
    @Schema(description = "合同是否需要签章", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer doSign;
    /**
     * 合同允许采购人选择签章 0：否  1：是
     */
    @Schema(description = "合同允许采购人选择签章", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer isSignChoose;
    /**
     * 是否中小企业融资  0：否  1：是
     */
    @Schema(description = "是否中小企业融资", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer isSmallCompany;

    //=========模板新增字段========
    /**
     * 保修期 几年 对应 value11
     */
//    @Schema(description = "保修期", requiredMode = Schema.RequiredMode.REQUIRED)
//    private Integer warrantyPeriod;
    /**
     * 工作日个数 对应 value23  机动车保险服务合同模板对应-value13  网上询价合同模板对应-value26    物业管理服务合同模板对应-value16
     */
    @Schema(description = "工作日个数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer workingDayNum;
    /**
     * 甲方法定（或授权）代表人
     */
    @Schema(description = "甲方法定（或授权）代表人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerLegalPerson;
    /**
     * 甲方指派联系人 -甲方代表- 采购人指派联系人 对应 value13   采购人代表
     */
    @Schema(description = "甲方指派联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerLink;
    /**
     * 乙方指派联系人-供应商指派联系人-乙方（供应商）代表 对应 value12
     */
    @Schema(description = "乙方指派联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierLink;

    //====电子反拍合同模板特有====
    /***
     * 其他项目交易名称
     */
//    @Schema(description = "其他项目交易名称", requiredMode = Schema.RequiredMode.REQUIRED)
//    private String otherBussinessName;
    /***
     * 其他项目交易内容
     */
//    @Schema(description = "其他项目交易内容", requiredMode = Schema.RequiredMode.REQUIRED)
//    private String otherBussinessValue;
    /**
     * 是否包安装：containInstall 0:否 1：是
     */
    @Schema(description = "是否包安装", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer containInstall;
    /***
     * 合同份数 对应：value31
     */
    @Schema(description = "合同份数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer contractNum;
    /***
     * 争议处理方式 对应：value32, 向仲裁委员会申请仲裁解决，向人民法院提交诉讼解决
     */
    @Schema(description = "争议处理方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String disputesProcessType;
    /**
     * 延长售后服务年限 对应value25--不需要延长，延长1年，延长2年，延长3年，延长4年，延长5年
     */
    @Schema(description = "延长售后服务年限", requiredMode = Schema.RequiredMode.REQUIRED)
    private String extendedWarrantyDuration;

    //====车辆加油服务采购合同模板特有====
    /**
     * 加油业务办理方式  对应-value24
     */
//    @Schema(description = "加油业务办理方式", requiredMode = Schema.RequiredMode.REQUIRED)
//    private String refuelingType;
    /**
     * 优惠内容  对应-value25
     */
//    @Schema(description = "优惠内容", requiredMode = Schema.RequiredMode.REQUIRED)
//    private String onSaleContent;
    /**
     * 甲方承担充值金额的违约金比率  -对应value26
     */
//    @Schema(description = "甲方承担充值金额的违约金比率", requiredMode = Schema.RequiredMode.REQUIRED)
//    private String buyerDeditRatio;
    /**
     * 乙方承担充值金额的违约金比率  -对应value27
     */
//    @Schema(description = "乙方承担充值金额的违约金比率", requiredMode = Schema.RequiredMode.REQUIRED)
//    private String supplierDeditRatio;

    //====汽车官合同模板特有=====
    /**
     * 保修里程数 对应value12
     */
//    @Schema(description = "保修里程数", requiredMode = Schema.RequiredMode.REQUIRED)
//    private Double warrantyMileage;

    //====物业管理服务合同模板特有======
    /**
     * 物业名称 对应value6
     */
    @Schema(description = "物业名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String propertyServiceName;
    /**
     * 物业类型 对应 value12
     */
    @Schema(description = "物业类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String propertyServiceType;
    /**
     * 物业位置 对应 value13
     */
    @Schema(description = "物业位置", requiredMode = Schema.RequiredMode.REQUIRED)
    private String propertyServiceAddress;
    /**
     * 建筑面积 对应 value5
     */
//    @Schema(description = "建筑面积", requiredMode = Schema.RequiredMode.REQUIRED)
//    private Double areaStructure;
    /**
     * 物业管理费 对应 value9
     */
//    @Schema(description = "物业管理费", requiredMode = Schema.RequiredMode.REQUIRED)
//    private Double propertyServiceFee;
    /**
     * 人员数量：value22
     */
    @Schema(description = "人员数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer personNum;
    /**
     * 人员金额：contractExpress.value11
     */
//    @Schema(description = "人员金额", requiredMode = Schema.RequiredMode.REQUIRED)
//    private Double personAmount;
    /**
     * 交货地址
     */
    @Schema(description = "交货地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String goodsDeliveryaddress;


    //====================2024/01/23 项目采购 电子交易新增======================//
    /**
     * 完成交易生成合同的交易平台的代码/合同来源
     * {@link PlatformEnums}
     */
    @Schema(description = "完成交易生成合同的交易平台的代码/合同来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private String platform;
    /**
     * 合同的唯一识别码
     */
    @Schema(description = "合同的唯一识别码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractGuid;

    /**
     * 采购计划唯一识别码
     */
    @Schema(description = "采购计划唯一识别码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanGuid;


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
     * 合同多方支付方式
     */
    @Schema(description = "合同多方支付方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String multiAccountPayType;

    /**
     * 采购单位传真
     */
    @Schema(description = "采购单位传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgFax;

    /**
     * 供应商传真
     */
    @Schema(description = "供应商传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierFax;
    /**
     * 是否面向中小企业采购(1:是,0:否)
     */
    @Schema(description = "是否面向中小企业采购", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isReserve;
    /**
     * 面向特定规模供应商采购
     */
    @Schema(description = "面向特定规模供应商采购", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierReserve;
    /**
     * 预留形式
     */
    @Schema(description = "预留形式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String reserveType;
    /**
     * (实际)面向中小企业预留金额
     */
    @Schema(description = "(实际)面向中小企业预留金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double reserveMoney;
    /**
     * 供应商规模
     */
    @Schema(description = "供应商规模", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer supplierSize;
    /**
     * 供应商的特殊性质
     */
    @Schema(description = "供应商的特殊性质", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer supplierFeatures;
    /**
     * 供应商所在区域
     */
    @Schema(description = "供应商所在区域", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierLocation;
    /**
     * 供应商所在区域编码
     */
    @Schema(description = "供应商所在区域编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierLocationCode;
    /**
     * 外商投资类型
     */
    @Schema(description = "外商投资类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer foreignInvestmentType;
    /**
     * 外资国别类型
     */
    @Schema(description = "外资国别类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String countryType;

    /**
     * 合同履约开始日期(精确到日，格式：YYYY-MM-DD)
     */
    @Schema(description = "合同履约开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date performStartDate;
    /**
     * 合同履约结束日期(精确到日，格式：YYYY-MM-DD)
     */
    @Schema(description = "合同履约结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date performEndDate;
    /**
     * 履约地点
     */
    @Schema(description = "履约地点", requiredMode = Schema.RequiredMode.REQUIRED)
    private String performAddress;
    /**
     * 合同其他补充事项
     */
    @Schema(description = "合同其他补充事宜", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String otherInfo;
    /**
     * 是否缴纳履约保证金(1:是,0:否)
     */
    @Schema(description = "是否缴纳履约保证金(1:是,0:否)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isPerformanceMoney;

    /**
     * 履约保证金收取比例
     */
    @Schema(description = "履约保证金收取比例", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double performanceMoneyRatio;

    /**
     * 履约保证金金额
     */
    @Schema(description = "履约保证金金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double performanceMoney;
    /**
     * 履约保证金缴纳方式
     */
    @Schema(description = "履约保证金缴纳方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String performanceMoneyType;
    /**
     * 是否缴纳质量保证金(1:是,0:否)
     */
    @Schema(description = "是否缴纳质量保证金(1:是,0:否)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isRetentionMoney;
    /**
     * 质量保证金金额
     */
    @Schema(description = "质量保证金金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double retentionMoney;
    /**
     * 质量保证金缴纳方式
     */
    @Schema(description = "质量保证金缴纳方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String retentionMoneyType;
    /**
     * 代理机构唯一识别码(自有交易平台使用，第三方平台为NULL) (项目采购为必填，电子卖场采购为NULL)
     */
    @Schema(description = "代理机构唯一识别码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String agentGuid;
    /**
     * 代理机构社会信用代码(项目采购为必填，电子市场采购为NULL)
     */
    @Schema(description = "代理机构社会信用代码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String agentCode;
    /**
     * 代理机构名称(项目采购为必填，电子市场采购为NULL)
     */
    @Schema(description = "代理机构名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String agentName;
    /**
     * 代理机构类型(参见常量【AgentType】定义) (项目采购为必填，电子市场采购为NULL)
     */
    @Schema(description = "代理机构类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String agentType;

    /**
     * 项目编号
     */
    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectCode;
    /**
     * 项目名称
     */
    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectName;
    /**
     * 在此交易平台中包的唯一识别码
     */
    @Schema(description = "在此交易平台中包的唯一识别码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bidGuid;
    /**
     * 包编号/招标编号
     */
    @Schema(description = "包编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bidCode;
    /**
     * 包名称
     */
    @Schema(description = "包名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bidName;
    /**
     * 统一交易标识码
     */
    @Schema(description = "统一交易标识码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String unifiedDealCode;
    /**
     * 开标时间(精确到秒，格式：YYYY-MM-DD HH:mm:ss)
     */
    @Schema(description = "开标时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date bidOpenTime;

    /**
     * 中标(成交)日期(精确到日，格式：YYYY-MM-DD)
     */
    @Schema(description = "中标(成交)日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date bidResultDate;
    /***
     * 中标(成交)金额(元)
     */
    @Schema(description = "中标(成交)金额(元)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double bidResultMoney;
    /***
     * 是否涉密采购(1:是,0:否)
     */
    @Schema(description = "是否涉密采购", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer secret;
    /**
     * 合同文本是否包含涉密条款(1:是,0:否)
     */
    @Schema(description = "合同文本是否包含涉密条款(1:是,0:否)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer clauseSecret;
    /**
     * 是否政府采购融资抵押 (1:是,0:否)
     */
    @Schema(description = "是否政府采购融资抵押", requiredMode = Schema.RequiredMode.REQUIRED)
    private String allowMortgage;
    /**
     * 是否预付款
     */
    @Schema(description = "是否预付款", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer isAdvanceCharge;
    /**
     * 预付款金额
     */
    @Schema(description = "预付款金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double advanceChargeMoney;
    /**
     * 项目所属分类 (A、B、C)
     */
    @Schema(description = "项目所属分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectCategoryCode;

    /**
     * 项目所属分类名称 (货物、工程、服务)
     */
    @Schema(description = "项目所属分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectCategoryName;

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
    @Schema(description = "框架协议编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String agreementCode;
    /**
     * 日历天数（服务时间，交货时间）
     */
    @Schema(description = "日历天数（服务时间，交货时间）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer goodsTime;
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
    private Integer accidentDays;
    /**
     * 不可抗力事件延续天数
     */
    @Schema(description = "不可抗力事件延续天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer continuationDays;
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
    private Integer supplierWorkingDayNum2;
    /**
     * 乙方支付合同总值违约比例
     */
    @Schema(description = "乙方支付合同总值违约比例", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierPayRatio;
    /**
     * 乙方进行合格验收工作日个数
     */
    @Schema(description = "乙方进行合格验收工作日个数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer supplierWorkingDayNum1;
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

    //=============无过程采购所新增字段=========
//    /**
//     * 分包的唯一识别码
//     */
//    @Schema(description = "分包的唯一识别码", requiredMode = Schema.RequiredMode.REQUIRED)
//    private String buyPlanPackageId;
    /**
     * 是否涉及政府购买服务，0：否，1：是
     */
    @Schema(description = "是否涉及政府购买服务", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer govService;
    /**
     * 是否涉及绿色建材采购，0：否，1：是
     */
    @Schema(description = "是否涉及绿色建材采购", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer greenMaterial;
    /**
     * 信息安全软件，0：否，1：是
     */
    @Schema(description = "信息安全软件", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer informationSecuritySoftware;
    /**
     * 是否采购机动车保险服务，0：否，1：是
     */
    @Schema(description = "是否采购机动车保险服务", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isCarInsuranceServer;
    /**
     * 是否存在预付款，0：否，1：是
     */
    @Schema(description = "是否存在预付款", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isPrepayment;
    /**
     * 是否专门面向中小企业采购，0：否，1：是
     */
    @Schema(description = "是否专门面向中小企业采购", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer reserveStatus;
    /**
     * 供应商拥有者性别
     */
    @Schema(description = "供应商拥有者性别", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierSex;
    /**
     * 计划名称
     */
    @Schema(description = "计划名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanName;
    /**
     * 采购单位编码
     */
    @Schema(description = "采购单位编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orgCode;
    /**
     * 备案状态 ContractIsBakStatusEnums
     */
    @Schema(description = "备案状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isBak;
    /**
     * 是否高校、科研院所科研设备采购 ，0：否，1：是
     */
    @Schema(description = "是否高校、科研院所科研设备采购 ", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isSchoolScientific;
    /**
     * 相对方id
     */
    @Schema(description = "相对方id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String relativeId;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    /**
     * 审批时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approveTime;

    /**
     * 发起方id
     */
    @Schema(description = "发起方id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sendId;
    /**
     * 当前支付计划的sort排序标识
     */
    private Integer currentScheduleSort;
    /**
     * 合同有效期-开始时间
     */
    private Date validity0;

    /**
     * 合同有效期-结束时间
     */
    private Date validity1;
    /**
     * 合同分类
     */
    private Integer contractCategory;
    /**
     * 签署截止日期
     */
    private Date expirationDate;
    /**
     * 结算类型
     * {@link AmountTypeEnums}
     * PAY(0, "付款"),
     * RECEIPT(1, "收款"),
     * NO_SETTLE(2, "不结算"),
     * DIRECTION(3, "收支双向"),
     */
    private Integer amountType;

    /**
     * 归档
     * 0 - 未归档
     * 1 - 已归档
     */
    private Integer document;
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
    //=======京东卖场模板新增字段=====
    /**
     * 采购计划唯一识别码/采购计划号
     */
    @Schema(description = "采购计划唯一识别码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanBillGuid;
    /**
     * 货物的运输方式
     */
    @Schema(description = "货物的运输方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String goodsTransportType;
    /**
     * 交货时间
     */
    @Schema(description = "交货时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date goodsDeliveryTime;

    /**
     * 验收异议解决工作日个数
     */
    @Schema(description = "验收异议解决工作日个数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer acceptanceProblemDealDays;
    //    /**
//     * 货物保修开始时间
//     */
//    @Schema(description = "货物保修开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
//    private Date goodsWarrantyStartDate;
//    /**
//     * 货物保修结束时间
//     */
//    @Schema(description = "货物保修结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
//    private Date goodsWarrantyEndDate;
    @Schema(description = "货物保修起止时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String warranty;
    /**
     * 本合同期限起止时间
     */
    private String contractTerm;
    /**
     * 资金性质
     */
    @Schema(description = "资金性质", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String capitalNature;
    /**
     * 可解除合同天数
     */
    @Schema(description = "可解除合同天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer canCancelDays;
    //==========================黑龙江=============

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
     * 验收标准补充说明
     */
    @Schema(description = "验收标准补充说明", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplement1;
    /**
     * 合同补充说明
     */
    @Schema(description = "付款方式补充说明", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplement2;

    /**
     * 黑龙江采购方式 (如 比价采购)
     */
    @Schema(description = "采购方式 (如 比价采购)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purchaseMethod2;

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
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Schema(description = "本工程竣工起始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String workDate;
    /**
     * 本工程竣工终止时间
     */
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
//    @Schema(description = "本工程竣工终止时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Date workEndDate;
    /**
     * 工程竣工起止天数
     */
    @Schema(description = "工程竣工起止天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer workDays;
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
    private Integer contractDays;
    /**
     * 采购结果有效期年限
     */
    @Schema(description = "采购结果有效期年限", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer purchaseResultValidity;
    /**
     * 自筹资金
     */
    @Schema(description = "自筹资金", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double selfRaisedFunds;

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
     * 履约保证金补充说明
     */
    @Schema(description = "履约保证金补充说明", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    private Integer supplierNoServiceOverDays;
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
    private Integer accidentStartDays;
    /**
     * 延迟履约方不可抗力结束后应出示证明文件天数
     */
    @Schema(description = "延迟履约方不可抗力结束后应出示证明文件天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer accidentEndDays;
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
    private Integer orgMaterialsDays;
    /**
     * 甲方开工前提供材料份数
     */
    @Schema(description = "甲方开工前提供材料份数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer orgMaterialsNum;
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
    private Integer contractCopyNum;
    /**
     * 乙方应向甲方提交施工相关材料天数
     */
    @Schema(description = "乙方应向甲方提交施工相关材料天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer supMaterialsDays;
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
    private Integer orgDealDaysByWarrantyInform;
    /**
     * 乙方收到书面异议后处理天数（收到书面异议后几天内解决）
     */
    @Schema(description = "乙方收到书面异议后处理天数（收到书面异议后几天内解决）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer supDealDaysByGetBooks;
    /**
     * 乙方提供相关材料的截止时间（在 xxx时间前）
     */
    @Schema(description = "乙方提供相关材料的截止时间（在 xxx时间前）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date supSubmitMaterialEndTime;
    /**
     * 乙方提供的竣工图纸截止时间（在 xxxx前）
     */
    @Schema(description = "乙方提供的竣工图纸截止时间（在 xxxx前）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date supSubmitDrawingEndTime;
    /**
     * 乙方提供的竣工图纸份数
     */
    @Schema(description = "乙方提供的竣工图纸份数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer supSubmitDrawingEndNum;
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
    private Integer projectWarrantyYears;
    /**
     * 乙方收到保修通知后进行保修天数（收到通知后x天内进行保修）
     */
    @Schema(description = "乙方收到保修通知后进行保修天数（收到通知后x天内进行保修）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer supDealDaysByWarrantyInform;
    /**
     * 乙方收到维修通知后进行维修天数（收到通知后x天内进行维修）
     */
    @Schema(description = "乙方收到维修通知后进行维修天数（收到通知后x天内进行维修）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer supDealDaysByMaintainInform;
    /**
     * 甲方停工一天应支付金额
     */
    @Schema(description = "甲方停工一天应支付金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double orgPayAmountByStopOneDay;
    /**
     * 资金类型
     */
    @Schema(description = "资金类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String moneyType;
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
     * 合同起草方
     * 合同起草方：采购人（1）/供应商（2）
     * {@link ContractDrafterEnums}
     */
    @Schema(description = "合同起草方", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer contractDrafter;

    @Schema(description = "修改标识 0：可修改合同，1：不可修改合同", requiredMode = Schema.RequiredMode.REQUIRED)
    @TableField("`modify`")
    private Integer modify;
    /**
     * 退回标识 0：未退回,1：确认阶段退回到草稿,2：签章阶段退回到草稿
     */
    @Schema(description = "退回标识 ", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer returnFlag;
    /**
     * 退回原因
     */
    @Schema(description = "退回原因", requiredMode = Schema.RequiredMode.REQUIRED)
    private String returnInfo;

    /**
     * 合同分类
     */
    @Schema(description = "合同分类", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String category;
    /**
     * 采购组织形式(参见选项字典【Kind】定义)
     */
    @Schema(description = "采购组织形式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyOrgForm;

    /**
     * 供应商的纳税人识别号
     */
    @Schema(description = "供应商的纳税人识别号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierCode;
    /**
     * 计划金额
     */
    @Schema(description = "计划金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal buyPlanMoney;

    /**
     * 合同来源类型
     * 0=电子合同
     * 1=上传合同
     * {@link com.yaoan.module.econtract.enums.contract.ContractSourceTypeEnums}
     */
    private Integer contractSourceType;
    private String sourceCode;
    /**
     * 甲方联系人电话--1
     */
    @Schema(description = "甲方联系人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerPhone;
    /**
     * 甲方地址---1
     */
    @Schema(description = "甲方地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgAddress;
    /**
     * 计划id
     */
    @Schema(description = "计划id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanId;
    /**
     * 包id
     */
    @Schema(description = "包id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanPackageId;
    /**
     * 甲方开户名称---1
     */
    @Schema(description = "甲方开户名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerBankAccountName;
    /**
     * 甲方纳税人识别号---1
     */
    @Schema(description = "甲方纳税人识别号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgTaxpayerNum;
    /**
     * 甲方联系方式---1
     */
    @Schema(description = "甲方联系方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgContact;
}
