package com.yaoan.module.econtract.dal.dataobject.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.contract.ContractKindEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract")
public class ContractDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = 1525804559753301321L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同编码
     */
    private String code;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 供应商id
     */
    private String supplierId;

    /**
     * 代理机构id
     */
    private String agentId;
    /**
     * 采购单位id
     */
    private String buyerOrgId;

    /**
     * 合同文件内容
     */
    private byte[] contractContent;

    /**
     * 履约完成时间
     */
    private Date performanceCompleteDate;
    /**
     * 签署截止日期
     */
    private Date expirationDate;

    /**
     * 合同分类
     */
    private Integer contractCategory;

    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 合同描述 备注
     */
    private String contractDescription;

    /**
     * 签署文件名称
     */
    private String fileName;

    /**
     * 主文件地址id
     */
    private Long fileAddId;

    /**
     * 签署状态
     * 0-待发送 - 新增
     * 1-被退回
     * 2-已发送
     * 3-待确认
     * 4-待签署
     * 5-已拒签
     * 6-签署完成
     * 7-逾期未签署
     * 8-合同终止签署中
     * 9-合同终止
     * 10-合同变更
     * 11-待送审
     * 12-审核中
     * 13审核未通过
     * {@link com.yaoan.module.econtract.enums.ContractStatusEnums}
     */
    private Integer status;

    /**
     * 对应的流程编号
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;

    /**
     * 发送时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    /**
     * 确认时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmTime;


    /**
     * 合同的履约风险天数
     * 按合同逾期计划中最早的一期的时间算
     */
    private Date riskDate;

    //------------------------------------------合同补录---------------------------------------
    /**
     * 上传合同
     * 默认
     * 0 - 模板起草
     * 1 - 上传文件起草
     * 2 - 合同补录 上传文件
     * 3 - 依据已成交的采购项目或订单起草
     * 4 - 第三方系统
     * {@link ContractUploadTypeEnums}
     */
    private Integer upload;

    /**
     * 签署类型
     * 0 - 电子签署
     * 1 - 纸质签署
     */
    private Integer signType;

    /**
     * 合同存放处
     */
    private String deposit;

    /**
     * 签署日期
     */
    private Date signDate;

    /**
     * 合同有效期-开始时间
     */
    private Date validity0;

    /**
     * 合同有效期-结束时间
     */
    private Date validity1;

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
     * 合同金额
     */
    private Double amount;

    /**
     * 归档
     * 0 - 未归档
     * 1 - 已归档
     * 2- 归档中
     */
    private Integer document;

    /**
     * 是否是模板上传 保存模板id
     */
    private String templateId;

    /**
     * 合同文件pdf 地址
     */
    private Long pdfFileId;
    /**
     * 初始合同文件pdf 地址
     */
    private Long oldPdfFileId;

    /**
     * 合同借阅文件地址
     */
    private Long borrowFileId;

    //------------新增第五期需求-内蒙------------

    /**
     * 签约地点
     */
    private String location;

    /**
     * 合同有效期
     */
    private Long validity;

    /**
     * 涉密条款 1是 0否
     */
    private Integer secrecyClause;

    /**
     * 推送数据id
     */
    private String businessesId;

    /**
     * 推送数据类型
     */
    private Integer businessesType;
    //--------------------合同变动-----------------------

    /**
     * 当前支付计划的sort排序标识
     */
    private Integer currentScheduleSort;
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
     * 变动原因/申请原因
     */
    private String changeReason;

    /**
     * 主合同id（变动合同独有）
     */
    private String mainContractId;

    /**
     * 变动类型（1=变更，2=补充，3=解除）
     * {@link com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums}
     */
    private Integer changeType;

    //-----------------合同归档-------------------------
    /**
     * 归档人
     */
    private String archiveUser;
    /**
     * 归档时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime archiveTime;

    /**
     * 变动协议数量
     */
    private Integer changeCount;

    /**
     * 是否借阅 0-否 1-是
     */
    private Integer borrow;

    //---------------合同台账-----------------------
    /**
     * 甲方名称
     */
    private String partAName;
    /**
     * 乙方名称
     */
    private String partBName;
    /**
     * 甲方id
     */
    private String partAId;
    /**
     * 乙方id
     */
    private String partBId;
    /**
     * 订单唯一编码
     */
    private String orderGuid;

    /**
     * 签署顺序
     */
    private String signOrder;
    /**
     * 完成交易生成合同的交易平台的代码/合同来源
     * {@link PlatformEnums}
     */
    @Schema(description = "完成交易生成合同的交易平台的代码/合同来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private String platform;
    /**
     * 框架协议编号
     */
    @Schema(description = "协议编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String agreementCode;
    /**
     * 供应商开户行账号
     */
    @Schema(description = "供应商开户行账号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankAccount;
    /**
     * 供应商开户行名称
     */
    @Schema(description = "供应商开户行名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bankName;
    /**
     * 采购计划备案书/标准书编号
     */
    @Schema(description = "采购计划备案书/标准书编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanCode;
    /**
     * 甲方指派联系人 -- 采购人指派联系人 、甲方联系人对应 value13   采购人代表。甲方代表
     */
    @Schema(description = "甲方指派联系人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerLink;
    /**
     * 采购人负责人电话
     */
    @Schema(description = "采购人负责人电话", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerLinkMobile;

    /**
     * 采购人负责人。甲方联系人
     */
    @Schema(description = "采购人负责人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerProxy;
    /**
     * 交货地址
     */
    @Schema(description = "交货地点", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String goodsDeliveryaddress;
    /**
     * 采购人负责人地址
     */
    @Schema(description = "采购人负责人地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String deliveryAddress;
    /**
     * 供应商负责人地址
     */
    @Schema(description = "供应商负责人地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String registeredAddress;
    /**
     * 合同金额大写
     */
    @Schema(description = "合同金额大写", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String shiftMoney;

    /**
     * 乙方指派联系人-供应商指派联系人 ，乙方联系人对应 value12 供应商（乙方）代表
     */
    @Schema(description = "乙方指派联系人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierLink;
    /**
     * 供应商负责人电话
     */
    @Schema(description = "供应商负责人电话", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierLinkMobile;
    /**
     * 供应商负责人
     */
    @Schema(description = "供应商负责人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierProxy;

//    /**
//     * 包ID
//     */
//    @Schema(description = "包ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private String bidGuid;
    /**
     * 计划id
     */
    @Schema(description = "计划id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanId;
    /**
     * 合同文本是否包含涉密条款(1:是,0:否)
     */
    //已存在涉密条款字段 secrecyClause
//    @Schema(description = "合同文本是否包含涉密条款(1:是,0:否)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer clauseSecret;
    //履约保证金缴纳方式
    //1	银行转账
    //2	支票（汇票、本票）
    //3	保函（保险）
    private String performanceMoneyType;
    //履约保证金金额
    private Double performanceMoney;
    private String allowMortgage;
    private Integer isPerformanceMoney;
    private Integer isRetentionMoney;
    /**
     * 合同开始时间（起草合同时，由用户在模板中填写） 对应-value7
     */
    @Schema(description = "合同开始时间（起草合同时，由用户在模板中填写）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;
    /**
     * 合同结束时间（起草合同时，由用户在模板中填写） -对应-value8
     */
    @Schema(description = "合同结束时间（起草合同时，由用户在模板中填写）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date endDate;
    /**
     * 合同签订日期
     */
    @Schema(description = "合同签订日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date contractSignTime;
    private String category;
    /**
     * 采购单位传真
     */
    private String orgFax;
//    /**
//     * 供应商统一社会信用代码
//     */
//    private String supplierCode;
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
    /***
     * 是否涉密采购(1:是,0:否)
     */
    @Schema(description = "是否涉密采购", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer secret;


    /**
     * 编辑类型
     * 0 = rtf
     * 1 = office
     */
    private Integer editType;
    /**
     * 暂停时间
     */
    private Date pauseDate;
    @Schema(description = "合同履约开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private String performStartDate;
    /**
     * 合同履约结束日期(精确到日，格式：YYYY-MM-DD)
     */
    @Schema(description = "合同履约结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private String performEndDate;
    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 锁定标识，0：未锁定，1：已锁定
     */
    private Integer isLocked;

    /**
     * 合同来源类型
     * 0=电子合同
     * 1=上传合同
     */
    private Integer contractSourceType;
    /**
     * 币种
     */
    private String currencyType;

    /**
     * 是否备案 0未备案 1备案中 2已备案
     */
    private Integer isFilings;

    /**
     * 采购人是否签署
     */
    private Integer isSign;

    /**
     * 是否线下(默认0)
     * {@link IfNumEnums}
     */
    private Integer isOffline;
    /**
     * 是否线下签署(默认0)
     * {@link IfNumEnums}
     */
    private Integer isOfflineSign;

    /**
     * 智能审查文件id
     */
    private String uploadFileAiId;

    /**
     * 申请之前的状态
     * */
    private Integer oldStatus;

    /**
     * 是否顺序签署
     */
    private Integer isSequential;

    /**
     * 相对方流程的规则设置（0=会签，1=依次签）
     * */
    private String flowSortRule;

    /**
     * 关联主合同id（广西大学招标要求）
     * 为了关联：主协议、补充协议、变更协议等
     */
    private String relativeMainContractId;

    /**
     * 合同类别
     * {@link ContractKindEnums}
     * 主合同、补充协议、框架协议
     */
    private Integer contractKind;



}

