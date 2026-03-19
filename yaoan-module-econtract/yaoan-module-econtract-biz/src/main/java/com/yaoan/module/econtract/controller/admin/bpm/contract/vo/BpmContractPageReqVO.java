package com.yaoan.module.econtract.controller.admin.bpm.contract.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @author doujiale
 */
@Schema(description = "合同审批 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class BpmContractPageReqVO extends PageParam {

    private static final long serialVersionUID = 1496320046657963469L;

    @Schema(description = "合同类型标识", example = "类型ID")
    private String contractType;

    @Schema(description = "合同名称、提交人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String searchText;

    @Schema(description = "流程实例的结果-参见 bpm_process_instance_result", example = "2")
    private Integer result;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "提交时间")
    private LocalDateTime[] createTime;

    private List<String> processInstanceIds;


}
