package com.yaoan.module.econtract.controller.admin.warning.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class ContractWarningQueryReqVO extends PageParam {

    @Schema(description = "区划编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String regionCode;

    @Schema(description = "采购单位id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgId;

    @Schema(description = "合同来源", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String platform;

    @Schema(description = "超期天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long overDays;

    @Schema(description = "超期类型,0:超期未签订,1:超期已签订", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String overdueType;

    @Schema(description = "开始日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;
    @Schema(description = "结束日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;



}
