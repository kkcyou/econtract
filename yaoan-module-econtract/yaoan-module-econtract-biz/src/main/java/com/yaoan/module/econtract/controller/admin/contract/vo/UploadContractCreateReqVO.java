package com.yaoan.module.econtract.controller.admin.contract.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.api.gcy.order.GoodsVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.upload.UploadContractPaymentPlanVo;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.TradingSupplierVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.PlanRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.draft.PlanInfo;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.ContractFileDO;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import com.yaoan.module.econtract.enums.contract.ContractSourceTypeEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Schema(description = "上传合同请求参数VO")
@Data
@ToString(callSuper = true)
public class UploadContractCreateReqVO {
    private static final long serialVersionUID = -4532116919770058260L;


    /**
     * id-主键
     */
    @Schema(description = "id-主键", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;
    /**
     * 合同编码
     */
//    @NotBlank(message = "合同编码不可为空")
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    /**
     * 计划来源编码
     */
    private String sourceCode;

    /**
     * 合同名称
     */
    @NotBlank(message = "合同名称不可为空")
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "项目id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectGuid;

    /**
     * 供应商id
     */
//    @NotBlank(message = "供应商id不可为空")
    @Schema(description = "供应商id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierId;
    /**
     * 供应商名称
     */
//    @NotBlank(message = "供应商名称不可为空")
    @Schema(description = "供应商名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierName;

    /**
     * 供应商负责人
     */
//    @NotBlank(message = "供应商负责人不可为空")
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
//    @NotBlank(message = "供应商负责人电话不可为空")
    @Schema(description = "供应商负责人电话", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierLinkMobile;


    /**
     * 采购人id
     */
//    @NotBlank(message = "采购人/采购单位id不可为空")
    @Schema(description = "采购人/采购单位id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgId;
    /**
     * 采购人名称
     */
//    @NotBlank(message = "采购单位名称不可为空")
    @Schema(description = "采购人/采购单位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgName;
    /**
     * 采购人负责人。甲方联系人
     */
    @Schema(description = "采购人负责人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerProxy;
    /**
     * 收货地址
     */
//    @NotBlank(message = "采购人 收货地址不可为空")
    @Schema(description = "收货地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String deliveryAddress;
    /**
     * 采购人负责人电话
     */
    @Schema(description = "采购人负责人电话", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerLinkMobile;
    /**
     * 合同签订地址
     */
//    @NotBlank(message = "合同签订地址不可为空")
    @Schema(description = "合同签订地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractSignAddress;
    /**
     * 合同签订日期
     */
//    @NotBlank(message = "合同签订日期不可为空")
    @Schema(description = "合同签订日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractSignTime;

    @Schema(description = "合同履约开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private String performStartDate;
    /**
     * 合同履约结束日期(精确到日，格式：YYYY-MM-DD)
     */
    @Schema(description = "合同履约结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private String performEndDate;
    /**
     * 合同金额
     */
    @NotNull(message = "合同金额不可为空")
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal totalMoney;

    /**
     * 备注
     */
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;
    /**
     * send：是否发送 0：未发送（保存）  1：发送  2：确认
     */
//    @NotNull(message = "是否发送标识不可为空")
    @Schema(description = "是否发送", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer send;


    /**
     * 实施形式，1：项目采购，2：框架协议采购，3：电子卖场
     */
//    @NotNull(message = "实施形式不可为空")
    @Schema(description = "实施形式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String platform;

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
     * 签署状态
     */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer status;


    /**
     * 合同开始时间/本合同期限起始时间（起草合同时，由用户在模板中填写）
     */
    @Schema(description = "合同开始时间（起草合同时，由用户在模板中填写）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    @NotBlank(message = "合同开始时间不可为空")
    private String startDate;
    /**
     * 合同结束时间/本合同期限终止时间（起草合同时，由用户在模板中填写）
     */
    @Schema(description = "合同结束时间（起草合同时，由用户在模板中填写）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    @NotBlank(message = "合同结束时间不可为空")
    private String endDate;


    //=========模板新增字段========

    /**
     * 付款方式方式:银行转账，货到付款 对应 value4
     */
    @Schema(description = "支付方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String paymentMethod2;


    /**
     * 合同文件id
     */
    @Schema(description = "合同文件id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long pdfFileId;

    private String pdfFileName;

    /**
     * 上传的合同文件
     */
//    @NotNull(message = "合同文件不可为空")
    @Schema(description = "上传的合同文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private MultipartFile file;


    /**
     * 合同分类
     */
    @Schema(description = "合同分类", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer contractCategory;

    /**
     * 任务编号
     */
    @Schema(description = "任务编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String taskId;

    @Schema(description = "审批意见", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reason;


    /**
     * 付款计划
     */
    @Schema(description = "付款计划", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "付款计划不可为空")
    private List<UploadContractPaymentPlanVo> paymentPlanList;

    @Schema(description = "付款计划", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<StagePaymentVO> stagePaymentList;

    /***
     * 是否涉密采购(1:是,0:否)
     */
    @Schema(description = "是否涉密采购", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer secret;

    private List<GPMallPageRespVO> gpmallPageRespVO;

    @Schema(description = "项目所属分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectCategoryCode;

    /**
     * 供应商数据集合
     */
    @Schema(description = "供应商数据集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<TradingSupplierVO> tradingSupplierVOList;

    /**
     * 采购人名称
     */
    private String purchaserName;

    /**
     * 采购人
     */
    private String purchaser;

    private String category;

    @Schema(description = "在此交易平台中包的唯一识别码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bidGuid;
    private String buyerLink;

    @Schema(description = "计划id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanId;

    private PlanInfo planInfo;
    @Schema(description = "供应商开户行账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankAccount;
    /**
     * 供应商开户银行账号
     */
    private String bankName;


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
    private String orgFax;

    /**
     * 附件集合
     */
    @Schema(description = "附件集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<ContractFileDO> contractFileDOList;

    //履约保证金缴纳方式
    //1	银行转账
    //2	支票（汇票、本票）
    //3	保函（保险）
    private String performanceMoneyType;
    //履约保证金金额
    private Double performanceMoney;
    private Integer clauseSecret;
    private String allowMortgage;
    /**
     * 是否缴纳履约保证金
     */
    private Integer isPerformanceMoney;
    /**
     * 是否缴纳质量保证金
     */
    private Integer isRetentionMoney;
    //制造商名称
    private String propertyServiceName;
    //制造商规模
    private String propertyServiceType;
    //制造商区划编码
    private String propertyServiceAddress;

    /**
     * 订单id
     */
    private List<String> orderIdList;

    /**
     * 来源类型
     * {@link ContractSourceTypeEnums}
     * 0=电子合同
     * 1=上传合同
     */
    private Integer contractSourceType;


    /**
     * 签署文件名称
     */
    @Schema(description = "签署文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;

    /**
     * 签署文件地址id
     */
    @Schema(description = "签署文件地址id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long fileAddId;


    /**
     * 合同文件内容
     */
    @Schema(description = "合同文件")
    private String contractContent;

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

    //上传合同 新增参数 ----------------------------

    /**
     * {@link ContractUploadTypeEnums}
     */
    @Schema(description = "创建合同方式： 0-草拟 1-上传", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer upload;

    /**
     * 签署类型： 0-电子签署 1-纸质签署
     */
    @Schema(description = "签署类型： 0-电子签署 1-纸质签署", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer signType;

    /**
     * 合同存放处
     */
    @Schema(description = "合同存放处", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String deposit;

    /**
     * 签署日期
     */
    @Schema(description = "签署日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date signDate;

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
     * 金额类型： 0 支出 1 收入
     */
    @Schema(description = "金额类型： 0支出 1收入", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer amountType;

    /**
     * 合同金额
     */
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double amount;

    /**
     * 归档
     * 0 - 未归档
     * 1 - 已归档
     */
    @Schema(description = "归档", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer document;

    /**
     * 关联的模板id
     */
    @Schema(description = "关联的模板id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String templateId;

    /**
     * 是否发起审批 1:发起
     */
    @Schema(description = "是否发起审批", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer initiateApproval;

//------------------------------------ 第五期内蒙 ---------------------------------------

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
     * 订单id
     */
    private String orderGuid;

    /**
     * 采购标的信息--ContractGoodsMapper
     */
    @Schema(description = "采购标的信息", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "采购标的信息不能为空")
    @Valid
    private List<GoodsVO> goodsList;
    /**
     * 合同来源（电子卖场：gpmall-5.3，电子交易（项目采购）：，框采平台：gp-gpfa）
     */
//    @NotBlank(message = "合同来源不能为空")
    @Schema(description = "合同来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractFrom;
    /**
     * 框架协议编号
     */
    @Schema(description = "协议编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String agreementCode;

    /**
     * 采购计划备案书/标准书编号
     */
    @Schema(description = "采购计划备案书/标准书编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanCode;


    /**
     * 交货地址
     */
    @Schema(description = "交货地点", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String goodsDeliveryaddress;

    /**
     * 合同金额大写
     */
    @Schema(description = "合同金额大写", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String shiftMoney;


    /**
     * 编辑类型
     */
    @Schema(description = "编辑类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer editType;


    /**
     * 签署截止日期
     */
    @Schema(description = "签署截止日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expirationDate;


    /**
     * 合同类型
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractType;

    /**
     * 合同描述
     */
    @Schema(description = "合同描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractDescription;

    /**
     * 关联计划
     */
    private AssociatedPlanRespVO associatedPlanVO;

    //    制造商规模
    private String propertyServiceTypeName;
    //    制造商所在区域
    private String propertyServiceAddressName;
    //    供应商所在区域
    private String supplierLocationName;
    //    供应商规模
    private String supplierSizeName;
    //    供应商特殊性质
    private String supplierFeaturesName;
    /**
     * 电子交易关联计划
     */
    private List<PlanRespVO> plans;

    /**
     * 智能审查文件id
     */
    @Schema(description = "智能审查文件id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String uploadFileAiId;
    /**
     * 相对方流程的规则设置（0=会签，1=依次签）
     * */
    private String flowSortRule;
}
