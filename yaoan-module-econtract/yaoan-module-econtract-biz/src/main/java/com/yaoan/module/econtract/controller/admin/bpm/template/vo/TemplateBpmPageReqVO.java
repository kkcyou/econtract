package com.yaoan.module.econtract.controller.admin.bpm.template.vo;

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
 * @description:
 * @author: Pele
 * @date: 2023/9/14 14:58
 */
@Schema(description = "范本审批页面展示 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateBpmPageReqVO extends PageParam {

    private static final long serialVersionUID = 5271127022621789944L;

    @Schema(description = "范本名称、创建人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String searchText;

    /**
     * 查询创建时间范围的起始时间
     */
    @Schema(description = "查询创建时间范围的起始时间", example = "2023-08-01 01:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startCreateTime;

    /**
     * 查询创建时间范围的结束时间
     */
    @Schema(description = "查询创建时间范围的结束时间", example = "2023-08-01 03:00:00")
    @JsonFormat(timezone="GMT+8")
    private Date endCreateTime;

    /**
     * 查询审批通过时间范围的起始时间
     */
    @Schema(description = "查询审批通过时间范围的起始时间", example = "2022-08-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startApproveSuccessTime;

    /**
     * 查询审批通过时间范围的结束时间
     */
    @Schema(description = "查询审批通过时间范围的结束时间", example = "2023-08-01 00:00:00")
    @JsonFormat(timezone="GMT+8")
    private Date endApproveSuccessTime;


    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private String approveStatus;

    @Schema(description = "结果id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer result;

    private List<String> processInstanceIds;

}
