package com.yaoan.module.econtract.controller.admin.warning.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 辅助计算DTO
 * @author doujiale
 */
@Data
public class ContractBakNumInfoReqVO extends PageParam {

    private static final long serialVersionUID = -4248890309662319787L;
    /**
     * 合同来源
     */
    @Schema(description = "合同来源", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String platform;

    /**
     * 采购人id
     */
    @Schema(description = "采购人id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgId;

    /**
     * 区划编号
     */
    @Schema(description = "区划编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String regionCode;

    @Schema(description = "开始日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;
    @Schema(description = "结束日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

}
