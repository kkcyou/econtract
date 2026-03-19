package com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 内蒙-电子卖场、框架协议合同实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_order_ext")
public class UploadContractOrderExtDO extends BaseDO {
    private static final long serialVersionUID = -4578042813519175214L;

    /**
     * id-主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 1:普通合同   默认 2：联系单生成 3：月结合同
     * 合同类型
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer contractType;
    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
    /**
     * 计划来源编码
     */
    private String sourceCode;
    /**
     * 合同名称 对应  value1
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 签署状态
     */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
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
     * 流程实例id
     */
    @Schema(description = "流程实例id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String processInstanceId;







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
    private String contractPayType;




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
     * 项目所属分类 (A、B、C)
     */
    @Schema(description = "项目所属分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectCategoryCode;

    /**
     * 项目所属分类名称 (货物、工程、服务)
     */
    @Schema(description = "项目所属分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectCategoryName;


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
     * 黑龙江采购方式 (如 比价采购)
     */
    @Schema(description = "采购方式 (如 比价采购)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purchaseMethod2;

    /**
     * 包id
     */
    @Schema(description = "包id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanPackageId;

    /**
     * 计划id
     */
    @Schema(description = "计划id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanId;

    /**
     * 合同起草方
     */
    @Schema(description = "合同起草方", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer contractDrafter;

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
    private String remark9;
    /***
     * 是否涉密采购(1:是,0:否)
     */
    @Schema(description = "是否涉密采购", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer secret;

    private String buyerLink;


    /**
     * 供应商传真
     */
    private String supplierFax;
    /**
     * 供应商所在区域  给区划编码没有给 " "
     */
    private String supplierLocation;


    /**
     * 供应商规模
     * 1:大型企业2:中型企业  3小型企业:4微型企业9其他
     */
    private String supplierSize;
    /**
     * 供应商特殊性质   1:监狱企业2:残疾人福利性单位9: 其他
     */
    private String supplierFeatures;

    /**
     * 采购单位传真
     */
    //履约保证金缴纳方式
    //1	银行转账
    //2	支票（汇票、本票）
    //3	保函（保险）
    private String performanceMoneyType;
    //履约保证金金额
    private Double performanceMoney;
    private String orgFax;
    private Integer clauseSecret;
    private String allowMortgage;
    private Integer isPerformanceMoney;
    private Integer isRetentionMoney;
    //制造商名称
    private String propertyServiceName;
    //制造商规模
    private String propertyServiceType;
    //制造商区划编码
    private String propertyServiceAddress;

    /**
     * 合同来源类型
     * 0=电子合同
     * 1=上传合同
     * {@link com.yaoan.module.econtract.enums.contract.ContractSourceTypeEnums}
     */
    private Integer contractSourceType;
}
