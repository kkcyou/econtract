package com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/20 15:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_order_ext")
public class TradingContractExtDO  extends BaseDO {
    /**
     * ------------------------------合同基本信息------------------------------------------------
     */

    /**
     * id-主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同文件内容
     */
    @Schema(description = "合同文件内容")
    private byte[] contractContent;

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
     * 合同签订日期
     */
    @Schema(description = "合同签订日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date contractSignTime;

    /**
     * 合同签订地址
     */
    @Schema(description = "合同签订地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractSignAddress;

    /**
     * 合同分类
     */
    @Schema(description = "合同分类", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String category;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String categoryName;

    /**
     * 合同开始时间（起草合同时，由用户在模板中填写）
     */
    @Schema(description = "合同开始时间（起草合同时，由用户在模板中填写）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date startDate;

    /**
     * 合同结束时间（起草合同时，由用户在模板中填写）
     */
    @Schema(description = "合同结束时间（起草合同时，由用户在模板中填写）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date endDate;

    /**
     * 关联的模板id-此处范本id
     */
    @Schema(description = "关联的模板id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String templateId;

    /***
     * 是否涉密采购(1:是,0:否)
     */
    @Schema(description = "是否涉密采购", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer secret;

    /**
     * 备注
     */
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;
    /**
     * ------------------------------合同采购内容------------------------------------------------
     */

    /**
     * ------------------------------交付时间地点方式------------------------------------------------
     */

    /**
     * 交付时间---1
     */
    @Schema(description = "交付时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deliveryTime;

    /**
     * 交付地点---1
     */
    @Schema(description = "交付地点", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String deliveryAdd;

    /**
     * 交付条件---1
     */
    @Schema(description = "交付条件", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String deliveryCondition;

    /**
     * 甲方指派联系人 -甲方代表- 采购人指派联系人 对应 value13   采购人代表
     */
    @Schema(description = "甲方指派联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerLink;

    /**
     * 甲方联系人电话--1
     */
    @Schema(description = "甲方联系人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerPhone;

    /**
     * 乙方指派联系人-供应商指派联系人-乙方（供应商）代表 对应 value12
     */
    @Schema(description = "乙方指派联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierLink;

    /**
     * 乙方联系人电话---1
     */
    @Schema(description = "乙方联系人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierLinkMobile;

    /**
     * ------------------------------产品交付与验收------------------------------------------------
     */

    /**
     * 验收---1
     */
    @Schema(description = "验收", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String acceptance;

    /**
     * ------------------------------合同附加信息------------------------------------------------
     */

    /**
     * 甲方委托代理人---1
     */
    @Schema(description = "甲方委托代理人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerAgent;

    /**
     * 供应商负责人 ，乙方委托代理人对应 对应 value14
     */
    @Schema(description = "供应商负责人，乙方委托代理人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierProxy;

    /**
     * 甲方电子邮箱
     */
    @Schema(description = "甲方电子邮箱", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerEmail;

    /**
     * 乙方电子邮箱
     */
    @Schema(description = "乙方电子邮箱", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierEmail;

    /**
     * 甲方邮政编码
     */
    @Schema(description = "甲方邮政编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgPostalCode;

    /**
     * 乙方邮政编码
     */
    @Schema(description = "乙方邮政编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierPostalCode;

    /**
     * 货物的运输方式
     */
    @Schema(description = "货物的运输方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String goodsTransportType;

    /**
     * 货物运输合理损耗及计算方法---1
     */
    @Schema(description = "货物运输合理损耗及计算方法", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String goodsTransportLoss;

    /**
     * 乙方收到书面异议后处理天数（收到书面异议后几天内解决）
     */
    @Schema(description = "乙方收到书面异议后处理天数（收到书面异议后几天内解决）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer supDealDaysByGetBooks;

    /**
     * 培训时间和地点---1
     */
    @Schema(description = "培训时间和地点", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String trainTimeAndPlace;
    @Schema(description = "货物保修起止时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String warranty;
//    /**
//     * 货物保修开始时间
//     */
//    @Schema(description = "货物保修开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
//    private Date goodsWarrantyStartDate;
//
//    /**
//     * 货物保修结束时间
//     */
//    @Schema(description = "货物保修结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
//    private Date goodsWarrantyEndDate;
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
     * 自筹资金
     */
    @Schema(description = "自筹资金", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double selfRaisedFunds;

    /**
     * 保证金比例---1
     */
    @Schema(description = "保证金比例", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double depositRatio;

    /**
     * 可解除合同天数
     */
    @Schema(description = "可解除合同天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer canCancelDays;

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
    private String remark9;

    /**
     * ------------------------------支付售后及违约------------------------------------------------
     */

    /**
     * 付款时间及条件---1
     */
    @Schema(description = "付款时间及条件", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String paymentTimeAndCondition;

    /**
     * 合同付款方式:一次付清的，分期付款
     */
    @Schema(description = "合同付款方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer contractPayType;

    /**
     * 质量保证期---1
     */
    @Schema(description = "质量保证期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String qualityGuaranteePeriod;

    /**
     * 售后服务---1
     */
    @Schema(description = "售后服务", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String afterSaleService;

    /**
     * 违约责任补充
     */
    @Schema(description = "违约责任补充", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String violateAppointDutyReplenish;

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

    /***
     * 争议处理方式 对应：value32, 向仲裁委员会申请仲裁解决，向人民法院提交诉讼解决
     */
    @Schema(description = "争议处理方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String disputesProcessType;

    /***
     * 合同份数 对应：value31
     */
    @Schema(description = "合同份数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer contractNum;

    /**
     * ------------------------------签约方及附件------------------------------------------------
     */

    /**
     * 甲方法定（或授权）代表人
     */
    @Schema(description = "甲方法定（或授权）代表人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerLegalPerson;

    /**
     * 甲方开户名称---1
     */
    @Schema(description = "甲方开户名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerBankAccountName;

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
     * 甲方纳税人识别号---1
     */
    @Schema(description = "甲方纳税人识别号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgTaxpayerNum;

    /**
     * 甲方地址---1
     */
    @Schema(description = "甲方地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgAddress;

    /**
     * 甲方联系方式---1
     */
    @Schema(description = "甲方联系方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgContact;

    /**
     * 甲方传真
     */
    @Schema(description = "甲方传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgFax;


    /**
     * 项目id，以此查询项目信息
     */
    @Schema(description = "项目id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectGuid;

    /**
     * 在此交易平台中包的唯一识别码
     */
    @Schema(description = "在此交易平台中包的唯一识别码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bidGuid;

    /**
     * 计划id
     */
    @Schema(description = "计划id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanId;

    /**
     * 采购单位id
     */
    @Schema(description = "采购单位id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgId;

    /**
     * -------------------------------黑龙江电子交易--------------------------------
     */

    /**
     * 包id
     */
    @Schema(description = "包id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanPackageId;

    /**
     * -------------------------------OrderContractDO 已有字段--------------------------------
     */
    /**
     * 合同类型
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer contractType;

    /**
     * 完成交易生成合同的交易平台的代码/合同来源
     * {@link PlatformEnums}
     */
    @Schema(description = "完成交易生成合同的交易平台的代码/合同来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private String platform;

    /**
     * 采购人名称
     */
    @Schema(description = "采购单位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgName;

    /**
     * 供应商id
     */
    @Schema(description = "供应商id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierId;

    /**
     * 供应商开户行名称
     */
    @Schema(description = "供应商开户行名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankName;

    /**
     * 合同金额
     */
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal totalMoney;

    /**
     * 签署状态
     */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierName;

    /**
     * 合同履约结束日期(精确到日，格式：YYYY-MM-DD)
     */
    @Schema(description = "合同履约结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date performEndDate;

    /**
     * 合同履约开始日期(精确到日，格式：YYYY-MM-DD)
     */
    @Schema(description = "合同履约开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date performStartDate;

    /**
     * 流程实例id
     */
    @Schema(description = "流程实例id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String processInstanceId;

    /**
     * 备案状态 ContractIsBakStatusEnums
     */
    @Schema(description = "备案状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isBak;

    /**
     * 采购人区划名称
     */
    @Schema(description = "采购人区划名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionName;


    /**
     * 框架协议编号
     */
    @Schema(description = "框架协议编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String agreementCode;

    /**
     * 合同文件pdf 地址id
     */
    @Schema(description = "合同文件pdf地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long pdfFileId;

    /**
     * 计划来源编码
     */
    private String sourceCode;

    /**
     * 采购人区划编码
     */
    @Schema(description = "采购人区划编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionCode;

    /**
     * 指定审核的代理机构的 userId
     */
    @Schema(description = "指定审核的代理机构的 userId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String agentGuid;

    /**
     * 外商投资类型
     */
    @Schema(description = "外商投资类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer foreignInvestmentType;

    /**
     * 外资国别类型
     */
    @Schema(description = "外资国别类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer countryType;

    /**
     * 参数json
     */
    @Schema(description = "参数json", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark11;
    /**
     * 合同分类：A：货物  B：工程  C：服务
     */
    @Schema(description = "合同分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectCategoryCode;

    /**
     * 供应商签章时间
     */
    @Schema(description = "供应商签章时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date supSinTime;
    /**
     * 采购人签章时间
     */
    @Schema(description = "供应商签章时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date orgSinTime;

    /**
     * 订单id
     */
    @Schema(description = "供应商签章时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderId;
    /**
     * 起草人
     */
    @Schema(description = "起草人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer contractDrafter;

    /**
     * 合同文件地址
     */
    @Schema(description = "合同文件地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String pdfFilePath;
    //制造商名称
    private String propertyServiceName;
    //制造商规模
    private String propertyServiceType;
    //制造商区划编码
    private String propertyServiceAddress;
    /**
     * 相对方id
     */
    @Schema(description = "相对方id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String relativeId;
}
