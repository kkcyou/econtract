package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "合同类型 Excel")
@Data
public class ContractTypeExportReqVO {

    /**
     * 合同类型名称
     */
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String name;

    /**
     * 合同类型状态
     */
    @Schema(description = "合同类型分类id", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private Integer typeStatus;

}
