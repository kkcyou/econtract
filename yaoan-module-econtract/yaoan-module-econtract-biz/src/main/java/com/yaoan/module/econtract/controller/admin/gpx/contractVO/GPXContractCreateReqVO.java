package com.yaoan.module.econtract.controller.admin.gpx.contractVO;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.api.contract.dto.ContractFileDTO;
import com.yaoan.module.econtract.controller.admin.contract.vo.StagePaymentVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.PurchaseVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.TradingSupplierVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.GPXContractQuotationRelReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.TermsDetailsVo;
import com.yaoan.module.econtract.controller.admin.supervise.vo.PaymentPlanVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Schema(description = "电子交易创建修改添加审批VO")
@Data
@ToString(callSuper = true)
public class GPXContractCreateReqVO {
    private static final long serialVersionUID = 0xc11aafac749171ecL;

    /**
     * ------------------------------合同基本信息------------------------------------------------
     */

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
     * 合同内容
     */
    @Schema(description = "合同内容", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String content;

    /**
     * 合同签订日期
     */
    @Schema(description = "合同签订日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractSignTime;

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
    private String startDate;

    /**
     * 合同结束时间（起草合同时，由用户在模板中填写）
     */
    @Schema(description = "合同结束时间（起草合同时，由用户在模板中填写）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String endDate;

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
     * 合同采购内容
     */
    @Schema(description = "合同采购内容", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<PurchaseVO> purchaseVOList;

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
     * 供应商数据集合
     */
    @Schema(description = "供应商数据集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<TradingSupplierVO> tradingSupplierVOList;

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
     * 供应商开户名
     */
    @Schema(description = "支付开户名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String payPlanbAccount;

    /**
     * 供应商开户行名称
     */
    @Schema(description = "供应商开户行名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankName;

    /**
     * 供应商银行行账号
     */
    @Schema(description = "供应商开户行账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankAccount;

    /**
     * 供应商收款金额（元）---1
     */
    @Schema(description = "供应商收款金额（元）", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal supplierPayAmount;

    /**
     * 纳税人识别号---1
     */
    @Schema(description = "纳税人识别号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierTaxpayerNum;

    /**
     * 供应商所在区域
     */
    @Schema(description = "供应商所在区域", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierLocation;

    /**
     * 供应商负责人地址 对应 value19
     */
    @Schema(description = "供应商负责人地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String registeredAddress;

    /**
     * 供应商联系电话
     */
    @Schema(description = "供应商联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierContact;

    /**
     * 供应商传真
     */
    @Schema(description = "供应商传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierFax;

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
     * 附件idList
     */
    @Schema(description = "附件idList", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<ContractFileDTO> contractFileDTOList;

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
     * 是否发送 1=发送
     */
    @Schema(description = "是否发送", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer send;

    /**
     * 支付计划信息--ContractPaymentPlanMapper
     */
    @Schema(description = "支付计划信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PaymentPlanVO> paymentPlanList;

    /**
     * 阶段支付信息
     */
    @Schema(description = "阶段支付信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<StagePaymentVO> payMentInfo;

    /**
     * 参数json
     */
    @Schema(description = "参数json", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark11;
    /**
     * 采购计划id
     */
    private String planId;
    /**
     * 采购包id
     */
    private String packageId;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 供应商idList
     */
    private List<String> supplierIdList;
    /**
     * 计划来源编码
     */
    private String sourceCode;

    /**
     * 区域编码
     */
    private String zoneCode;
    /**
     * 区域名称
     */
    private String zoneName;
    /**
     * 合同金额
     */
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal totalMoney;

    @Schema(description = "合同签订地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String signAddress;

    private List<GPXContractQuotationRelReqVO> quotationRelReqVOList;

    /**
     * 合同履约开始日期(精确到日，格式：YYYY-MM-DD)
     */
    @Schema(description = "合同履约开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date performStartDate;
    /**
     * 合同履约结束日期(精确到日，格式：YYYY-MM-DD)
     */
    @Schema(description = "合同履约结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date performEndDate;


    /**
     * 验收组织方式
     */
    @Schema(description = "验收组织方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acceptanceType;
    /**
     *验收主体---验收组织方式为委托第三方验收时该项必填
     */
    @Schema(description = "验收主体", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String performMainBody;
    /**
     * 是否邀请本项目其他供应商
     */
    @Schema(description = "是否邀请本项目其他供应商", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isInviteSupplier;
    /**
     * 是否邀请验收评审专家
     */
    @Schema(description = "是否邀请验收评审专家", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isInviteExpert;
    /**
     * 是否邀请服务对象
     */
    @Schema(description = "是否邀请服务对象", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isInviteServiceObject;
    /**
     * 是否邀请第三方检测机构
     */
    @Schema(description = "是否邀请第三方检测机构", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isProfessionalDetection;
    /**
     * 是否进行抽查检测(默认:0)
     */
    @Schema(description = "是否进行抽查检测", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isSpotCheck;
    /**
     * 抽查检测比例----是否进行抽查检测为1时该项必填
     */
    @Schema(description = "抽查检测比例", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String spotCheckProportion;
//    private Double spotCheckProportion;
    /**
     * 是否存在破坏性检测(默认:0)
     */
    @Schema(description = "是否存在破坏性检测", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isDestructiveCheck;
    /**
     * 被破坏的检测产品的处理方式----是否存在破坏性检测为1时该项必填
     */
    @Schema(description = "被破坏的检测产品的处理方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String destructiveCheckMethod;
    /**
     * 组织验收的其他事项----非必填
     */
    @Schema(description = "组织验收的其他事项", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String otherPreparations;
//    /**
//     * 预计自供应商提出之日起N日内进行验收----与履约验收时间2选1
//     */
//    @Schema(description = "预计自供应商提出之日起N日内进行验收", requiredMode = Schema.RequiredMode.REQUIRED)
//    private Integer infewdays;
    /**
     * 履约验收时间
     */
    @JsonFormat( pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @Schema(description = "履约验收时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date acceptanceDate;
    /**
     * 验收方法
     */
    @Schema(description = "验收方法", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractAcpMethod;
    /**
     * 分期/分项验收工作安排---验收方法为分段验收或分期验收时该项必填
     */
    @Schema(description = "期/分项验收工作安排", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String acceptanceWorkArrange;
    /**
     * 验收内容
     */
    @Schema(description = "验收内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acceptanceContent;
    /**
     * 是否以采购活动中供应商提供的样品作为参考
     */
    @Schema(description = "是否以采购活动中供应商提供的样品作为参考", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isSampleReference;
    /**
     * 验收标准
     */
    @Schema(description = "验收标准", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acceptanceCriteria;
    /**
     * 履约验收程序
     */
    @Schema(description = "履约验收程序", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acceptanceProcedure;
    /**
     * 履约验收其他事项--非必填
     */
    @Schema(description = "履约验收其他事项", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String otherContent;
    //制造商名称
    private String propertyServiceName;
    //制造商规模
    private String propertyServiceType;
    //制造商区划编码
    private String propertyServiceAddress;

    /**
     * 条款列表
     */
    private List<TermsDetailsVo> terms;
    public Boolean isAddOrderContractParamFieldDO() {
        // 将字段放入数组
        Object[] fields = {acceptanceType,performMainBody,isInviteSupplier,isInviteExpert,isInviteServiceObject,isProfessionalDetection,
                isSpotCheck,spotCheckProportion,isDestructiveCheck,destructiveCheckMethod,otherPreparations,acceptanceDate,
                contractAcpMethod,acceptanceWorkArrange,acceptanceContent,isSampleReference,acceptanceCriteria,acceptanceProcedure,otherContent};
        // 使用 Stream API 检查是否有任何字段非空
        boolean flag = Arrays.stream(fields).anyMatch(s -> ObjectUtil.isNotEmpty(s));
//        boolean anyFieldSet = Arrays.stream(fields).anyMatch(Optional::isPresent);
        if (flag) {
            return true;
        } else {
            return false;
        }
    }



}
