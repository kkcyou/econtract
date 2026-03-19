package com.yaoan.module.econtract.controller.admin.performtasktype.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/1 13:29
 */
@Data
public class PerformTaskTypeListReqVo extends PageParam {
    /**
     *履约类型
     * */
    @Schema(description = "履约类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String performTypeId;
    /**
     *创建时间起始
     * */
    @Schema(description = "创建时间起始", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone="GMT+8")
    private Date createTimeStart;
    /**
     *创建时间终止
     * */
    @Schema(description = "创建时间终止", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone="GMT+8")
    private Date createTimeEnd;
    /**
     *创建者昵称
     * */
    @Schema(description = "搜索内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String searchText;



}
