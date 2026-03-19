package com.yaoan.module.econtract.controller.admin.contractrisk.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.BusinessFileVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/27 17:57
 */
@Data
public class ContractRiskUpdateReqVO {
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10580")
    private String id;

    /**
     * 履约计划id
     */
    private String planId;
    /**
     * 合同id
     */
    private String contractId;

    /**
     * 处理id
     */
    private String handleId;
    /**
     * 风险原因/争议内容
     */
    private String riskReason;

    /**
     * 计划重新开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date restartDate;


    /**
     * 风险处理状态 未处理0 已处理1
     */
    private Integer status;

    @Schema(description = "风险类型", example = "2")
    private Integer riskType;

    @Schema(description = "处理人")
    @NotNull(message = "处理人不能为空")
    private String handleUser;

    @Schema(description = "处理日期")
    @NotNull(message = "处理日期不能为空")
    private LocalDateTime handleTime;

    @Schema(description = "处理结果")
    @NotNull(message = "处理结果不能为空")
    private String handleResult;

    @NotNull(message = "处理结果状态不能为空")
    @Schema(description = "处理后合同状态", example = "1")
    private Integer handleResultStatus;

    /**
     * 附件文件
     */
    private List<BusinessFileVO> fileVOList;
}
