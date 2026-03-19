package com.yaoan.module.econtract.controller.admin.contractrisk.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.BusinessFileVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Schema(description = "管理后台 - 合同风险 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractRiskRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10580")
    @ExcelProperty("id")
    private String id;

    private String contractId;
    private String contractName;
    private String contractCode;
    private String contractTypeName;
    private String creator;
    private String creatorName;

    private String handleId;

    /**
     * 风险处理状态 未处理0 已处理1 
     */
    private Integer status;
    
    @Schema(description = "风险类型", example = "2")
    @ExcelProperty("风险类型")
    private String riskType;
    /**
     * 风险原因/争议内容
     */
    private String riskReason;

    /**
     * 计划重新开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date restartDate;
    @Schema(description = "处理人")
    @ExcelProperty("处理人")
    private String handleUser;

    @Schema(description = "处理日期")
    @ExcelProperty("处理日期")
    private LocalDateTime handleTime;

    @Schema(description = "处理结果")
    @ExcelProperty("处理结果")
    private String handleResult;

    @Schema(description = "处理后合同状态", example = "1")
    @ExcelProperty("处理后合同状态")
    private Integer handleResultStatus;

    /**
     * 附件文件
     */
    private List<BusinessFileVO> fileVOList;


   

}