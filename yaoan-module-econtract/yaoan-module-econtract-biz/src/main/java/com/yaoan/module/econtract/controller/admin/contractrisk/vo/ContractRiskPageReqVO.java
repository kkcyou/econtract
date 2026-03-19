package com.yaoan.module.econtract.controller.admin.contractrisk.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;


@Schema(description = "管理后台 - 合同风险分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractRiskPageReqVO extends PageParam {

    @Schema(description = "风险类型", example = "2")
    private Integer riskType;


    private String contractId;

    private String handleId;

    /**
     * 风险处理状态 未处理0 已处理1 
     */
    private Integer status;
    
    @Schema(description = "处理人")
    private Long handleUser;

    @Schema(description = "处理日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] handleTime;

    @Schema(description = "处理结果")
    private String handleResult;

    @Schema(description = "处理后合同状态", example = "1")
    private Integer handleResultStatus;

   

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}