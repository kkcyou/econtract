package com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/7 0:07
 */
@Data
public class RiskAlertPageReqVO extends PageParam {

    /**
     * 模糊查询：合同名称/合同编号/相对方
     */
    private String searchText;

    /**
     * 查询时间开始时间
     */
    @Schema(description = "查询时间开始时间", example = "2023-08-01 01:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8")
    private Date startTime;

    /**
     * 查询时间范围结束时间
     */
    @Schema(description = "查询时间范围结束时间", example = "2023-08-02 01:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8")
    private Date endTime;


}
