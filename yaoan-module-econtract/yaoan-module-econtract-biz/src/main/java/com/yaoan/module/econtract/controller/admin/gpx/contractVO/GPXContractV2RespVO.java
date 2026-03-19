package com.yaoan.module.econtract.controller.admin.gpx.contractVO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.api.contract.dto.ContractFileDTO;
import com.yaoan.module.econtract.controller.admin.contract.vo.StagePaymentVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.PurchaseVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.TradingSupplierVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.GPXContractQuotationRelRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.PlanDetailInfoRespVO;
import com.yaoan.module.econtract.controller.admin.supervise.vo.PaymentPlanVO;
import com.yaoan.module.econtract.dal.dataobject.gpx.BidConfirmQuotationDetailDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025/2/6 15:31
 */
@Data
public class GPXContractV2RespVO {

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
     * 乙方指派联系人-供应商指派联系人-乙方（供应商）代表 对应 value12
     */
    @Schema(description = "乙方指派联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierLink;

    /**
     * 乙方联系人电话---1
     */
    @Schema(description = "乙方联系人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierPhone;
    /**
     * 甲方邮政编码
     */
    @Schema(description = "甲方邮政编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgPostalCode;

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
     * ------------------------------签约方及附件------------------------------------------------
     */
    /**
     * 采购单位名称
     */
    @Schema(description = "采购单位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyerOrgName;

    /**
     * 甲方法定（或授权）代表人
     */
    @Schema(description = "甲方法定（或授权）代表人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyerLegalPerson;

    /**
     * 采购单位名称-公章
     */
    @Schema(description = "采购单位名称-公章", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orgName;

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
    private String supplierSize;

    /**
     * 供应商的特殊性质
     */
    @Schema(description = "供应商的特殊性质", requiredMode = Schema.RequiredMode.REQUIRED)
    private String supplierFeatures;

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
    private String supplierLocationStr;

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
     * 供应商传真
     */
    @Schema(description = "供应商传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierFax;

    private String supplierLocationCode;
    /**
     * 外商投资类型
     */
    @Schema(description = "外商投资类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String foreignInvestmentType;

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
     * 包id
     */
    @Schema(description = "包id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanPackageId;

    /**
     * 计划id
     */
    @Schema(description = "计划id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanId;

    private Long pdfFileId;

    /**
     * 模板名称
     */
    @Schema(description = "模板名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String templateName;

    /**
     * 批注list
     */
    @Schema(description = "批注list", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<CommentCreateRespVO> commentCreateRespVOList;

    /**
     * 阶段支付信息
     */
    @Schema(description = "阶段支付信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<StagePaymentVO> payMentInfo;

    /**
     * 支付计划信息--ContractPaymentPlanMapper
     */
    @Schema(description = "支付计划信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PaymentPlanVO> paymentPlanList;

    /**
     * 参数json
     */
    @Schema(description = "参数json", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark11;

    /**
     * 合同金额
     */
    @Schema(description = "合同金额", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal totalMoney;

    private List<BidConfirmQuotationDetailDO> bidConfirmDetailList;

    /**
     * 报价明细（采购内容）
     */
    private List<GPXContractQuotationRelRespVO> quotationRelRespVOList;

    /**
     * 计划明细
     */
    private List<PlanDetailInfoRespVO> planDetailInfoRespVOList;


    private Integer supplierType;
    //制造商名称
    private String propertyServiceName;
    //制造商规模
    private String propertyServiceType;
    //制造商区划编码
    private String propertyServiceAddress;

}
