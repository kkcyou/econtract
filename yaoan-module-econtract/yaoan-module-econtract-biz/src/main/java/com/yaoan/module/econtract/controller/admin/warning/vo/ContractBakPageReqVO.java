package com.yaoan.module.econtract.controller.admin.warning.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 辅助计算DTO
 * @author doujiale
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractBakPageReqVO extends ContractBakNumInfoReqVO {

    private static final long serialVersionUID = 2059791669598774204L;
    /**
     * 筛选类型：0:超期未备案,1:超期已备案
     */
    @Schema(description = "筛选类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String queryType;
    /**
     * 超期时间(天)
     */
    @Schema(description = "超期天数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer overDays;
    @Schema(description = "区划编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String regionCode;
    @Schema(description = "采购单位id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgId;
    @Schema(description = "合同来源", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String platform;
    @Schema(description = "开始日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;
    @Schema(description = "结束日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;
}
