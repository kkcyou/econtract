package com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 合同模板推荐 Excel 导出 Request VO，参数和 ContractAiDraftShowPageReqVO 是一致的")
@Data
public class ContractAiDraftShowExportReqVO {

    @Schema(description = "模板名称", example = "赵六")
    private String templateName;

    @Schema(description = "使用的大模型")
    private String model;

    @Schema(description = "模板内容")
    private String templateContent;

    @Schema(description = "合同名称", example = "赵六")
    private String contractName;

    @Schema(description = "使用次数")
    private Integer useNum;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
