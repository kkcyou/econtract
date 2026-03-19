package com.yaoan.module.econtract.controller.admin.contractrisk.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.BusinessFileVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Schema(description = "管理后台 - 合同风险新增/修改 Request VO")
@Data
public class ContractRiskSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10580")
    private String id;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 风险原因/争议内容
     */
    private String riskReason;

    /**
     * 计划重新开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date restartDate;

    @Schema(description = "风险类型", example = "2")
    private Integer riskType;


    /**
     * 附件文件
     */
    private List<BusinessFileVO> fileVOList;

}