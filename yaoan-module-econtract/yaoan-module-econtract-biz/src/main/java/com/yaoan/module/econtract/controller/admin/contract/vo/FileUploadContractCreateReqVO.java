package com.yaoan.module.econtract.controller.admin.contract.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;
import com.yaoan.module.econtract.enums.contract.ContractKindEnums;
import com.yaoan.module.econtract.service.gcy.gpmall.vo.ContractPaymentPlanVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Schema(description = "上传合同请求参数VO")
@Data
@ToString(callSuper = true)
public class FileUploadContractCreateReqVO extends ContractCreateReqVO {
    private static final long serialVersionUID = -4532116919770058260L;

    @Schema(description = "项目id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String projectGuid;

    /**
     * 供应商负责人地址
     */
    @Schema(description = "供应商负责人地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String registeredAddress;
    private String platform;


    /**
     * 采购人负责人地址
     */
    @Schema(description = "采购人负责人地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String deliveryAddress;


    /**
     * 合同签订日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(description = "合同签订日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date contractSignTime;

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
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal totalMoney;

    /**
     * 备注
     */
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;


    /**
     * 订单编号
     */
    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderCode;

//    /**
//     * 1:普通合同   默认 2：联系单生成 3：月结合同
//     * 合同类型
//     */
//    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private Integer contractType;

    /**
     * 合同开始时间/本合同期限起始时间（起草合同时，由用户在模板中填写）
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(description = "合同开始时间（起草合同时，由用户在模板中填写）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date startDate;
    /**
     * 合同结束时间/本合同期限终止时间（起草合同时，由用户在模板中填写）
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(description = "合同结束时间（起草合同时，由用户在模板中填写）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date endDate;


    //=========模板新增字段========

    /**
     * 付款方式方式:银行转账，货到付款 对应 value4
     */
    @Schema(description = "支付方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String paymentMethod2;


//    /**
//     * 上传的合同文件
//     */
//    @Schema(description = "上传的合同文件", requiredMode = Schema.RequiredMode.REQUIRED)
//    private MultipartFile file;


//    /**
//     * 任务编号
//     */
//    @Schema(description = "任务编号", requiredMode = Schema.RequiredMode.REQUIRED)
//    private String taskId;
//
//    @Schema(description = "审批意见", requiredMode = Schema.RequiredMode.REQUIRED)
//    private String reason;

    /**
     * 订单id
     */
//    @NotBlank(message = "订单id不可为空")
    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderId;
    /**
     * 付款计划
     */
    @Schema(description = "支付计划", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ContractPaymentPlanVo> paymentPlanList;

    @Schema(description = "阶段支付计划", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<StagePaymentVO> stagePaymentList;

    @Schema(description = "阶段支付计划与支付计划", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PaymentScheduleVO> paymentScheduleVOList;


    /***
     * 是否涉密采购(1:是,0:否)
     */
    @Schema(description = "是否涉密采购", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer secret;

    private GPMallPageRespVO gpmallPageRespVO;

    @Schema(description = "项目所属分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectCategoryCode;

//    /**
//     * 供应商数据集合
//     */
//    @Schema(description = "供应商数据集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
//    private List<TradingSupplierVO> tradingSupplierVOList;

    @Schema(description = "在此交易平台中包的唯一识别码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bidGuid;

    @Schema(description = "计划id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanId;

//    private PlanInfo planInfo;

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
    private  String supplierFeatures;

    /**
     * 采购单位传真
     */
    private  String orgFax;


    //履约保证金缴纳方式
    //1	银行转账
    //2	支票（汇票、本票）
    //3	保函（保险）
    private String performanceMoneyType;
    //履约保证金金额
    private Double performanceMoney;
    private Integer clauseSecret;
    private String allowMortgage;
    private Integer isPerformanceMoney;
    private Integer isRetentionMoney;
    /**
     * 合同文件pdf 地址
     */
    private Long pdfFileId;
    private String category;

    private Integer editType;


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
