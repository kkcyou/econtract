package com.yaoan.module.econtract.controller.admin.warningrisk.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 预警结果 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WarningRiskRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "5786")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "规则编码")
    @ExcelProperty("规则编码")
    private String warningRuleCode;

    @Schema(description = "规则名称", example = "王五")
    @ExcelProperty("规则名称")
    private String warningRuleName;

    @Schema(description = "规则类型 事前提醒1 事中提醒2", example = "2")
    @ExcelProperty("规则类型 事前提醒1 事中提醒2")
    private Integer warningRuleType;

    @Schema(description = "规则描述", example = "随便")
    @ExcelProperty("规则描述")
    private String warningRuleRemark;

    @Schema(description = "所属业务", example = "19280")
    @ExcelProperty("所属业务")
    private String businessTypeId;

    @Schema(description = "触发字段")
    @ExcelProperty("触发字段")
    private String warningRuleField;

    @Schema(description = "是否启用配置，0关闭，1启用")
    @ExcelProperty("是否启用配置，0关闭，1启用")
    private String enable;

    @Schema(description = "逾期是否业务阻断，1阻断，0不阻断")
    @ExcelProperty("逾期是否业务阻断，1阻断，0不阻断")
    private Integer isBlocked;

    @Schema(description = "所属公司", example = "23789")
    @ExcelProperty("所属公司")
    private Long companyId;

    @Schema(description = "配置", example = "23789")
    private List<WarningRuleConfigDetailSaveReqVO> warningRuleConfigDetailList;

}