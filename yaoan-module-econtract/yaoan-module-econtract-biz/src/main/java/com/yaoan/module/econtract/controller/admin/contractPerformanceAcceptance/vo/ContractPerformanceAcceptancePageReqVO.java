package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;


@Schema(description = "管理后台 - 验收分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractPerformanceAcceptancePageReqVO extends PageParam {

    @Schema(description = "编码")
    private String code;

    @Schema(description = "标题")
    private String title;

    /**
     * 合同id
     */
    private String contractId;
    /**
     * 计划id
     */
    private String planId;

    
    /**
     * 验收id 用于关联附件
     */
    private String acceptanceId;
    
    @Schema(description = "验收开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] acceptanceStartTime;

    @Schema(description = "验收结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] acceptanceEndTime;

    @Schema(description = "验收负责人")
    private Long acceptanceUser;

    @Schema(description = "备注", example = "你说的对")
    private String remark;
    

    @Schema(description = "验收状态 申请0 验收通过1 验收不通过2", example = "2")
    private List<Integer> status;

    @Schema(description = "验收备注", example = "你说的对")
    private String acceptanceRemark;

    @Schema(description = "验收类型 我发起的 1 我验收的2", example = "1")
    private Integer acceptanceUserType;


    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}