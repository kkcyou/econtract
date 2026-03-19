package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.contract.vo.PaymentPlan;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.PaymentPlanRespVO;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SimpleContractDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Schema(description = "管理后台 - 验收 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractPerformanceAcceptanceRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "7848")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "编码")
    @ExcelProperty("编码")
    private String code;

    @Schema(description = "标题")
    @ExcelProperty("标题")
    private String title;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 合同编码
     */
    private String contractCode;
    
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 计划id
     */
    private String planId;

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 期数
     */
    private Integer sort;
    
    /**
     * 款项比例
     */
    private BigDecimal paymentRatio;
    
    /**
     * 款项金额
     */
    private BigDecimal amount;

    /**
     * 验收id
     */
    private String acceptanceId;

    
    @Schema(description = "验收开始时间")
    @ExcelProperty("验收开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date acceptanceStartTime;

    @Schema(description = "验收结束时间")
    @ExcelProperty("验收结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date acceptanceEndTime;

    @Schema(description = "验收负责人")
    @ExcelProperty("验收负责人")
    private Long acceptanceUser;
    private String acceptanceUserName;
    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "附件id", example = "10788")
    @ExcelProperty("附件id")
    private Long fileId;

    @Schema(description = "附件名称", example = "张三")
    @ExcelProperty("附件名称")
    private String fileName;

    @Schema(description = "验收状态 申请0 验收通过1 验收不通过2", example = "2")
    @ExcelProperty("验收状态 申请0 验收通过1 验收不通过2")
    private Integer status;

    @Schema(description = "验收备注", example = "你说的对")
    @ExcelProperty("验收备注")
    private String acceptanceRemark;
    

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "验收附件列表")
    private List<BusinessFileDO> acceptanceFileList;

    @Schema(description = "验收申请附件列表")
    private List<BusinessFileDO> applyFileList;

    @Schema(description = "合同信息")
    private List<SimpleContractDO> contractList;
    /**
     * 本次支付计划
     */
    private PaymentScheduleDO paymentScheduleDO;
    private String creator;
    
    private String creatorName;
}