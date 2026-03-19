package com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @description: 借阅记录req
 * @author: Pele
 * @date: 2023/10/9 15:01
 */
@Schema(description = "模板审批页面展示 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractBorrowRecordPageReqVO extends PageParam {

    private static final long serialVersionUID = 915178439299319874L;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称")
    private String contractName;

    /**
     * 合同编码
     */
    @Schema(description = "合同编码")
    private String contractCode;

    /**
     * 剩余时间
     */
    @Schema(description = "剩余时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date remainTime;


    @Schema(description = "档号")
    private String code;

    @Schema(description = "档案名称", example = "芋艿")
    private String name;

    @Schema(description = "全宗号")
    private String fondsNo;

    @Schema(description = "一级类别号")
    private String firstLevelNo;

    @Schema(description = "二级类别号")
    private String secondLevelNo;

    @Schema(description = "案卷号")
    private String volumeNo;

    @Schema(description = "归档人员名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String documentName;

    @Schema(description = "开始归档时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date beginArchiveTime;

    @Schema(description = "结束归档时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endArchiveTime;

    @Schema(description = "借阅状态0：已失效，1：审批中，2：待取档，3：借阅中，4：已归还")
    private Integer borrowStatus;

    @Schema(description = "查询借阅时间范围的起始时间", example = "2023-08-01 01:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startBorrowTime;

    @Schema(description = "查询借阅时间范围的结束时间", example = "2023-08-01 03:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endBorrowTime;

    @Schema(description = "预计归还时间开始", example = "2023-08-01 01:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startReturnTime;

    @Schema(description = "预计归还时间结束", example = "2023-08-01 03:00:00")
    @JsonFormat(timezone="GMT+8")
    private Date endReturnTime;

    @Schema(description = "实际归还时间开始", example = "2023-08-01 01:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startActualReturnTime;

    @Schema(description = "实际归还时间结束", example = "2023-08-01 03:00:00")
    @JsonFormat(timezone="GMT+8")
    private Date endActualReturnTime;

    /**
     * 借阅人名称
     */
    private String applicantName;

    /**
     * 借阅人名称
     */
    private List<Long> applicantIds;

    /**
     * 项目编码
     */
    private String proCode;
    private String proName;

}
