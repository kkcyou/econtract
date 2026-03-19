package com.yaoan.module.econtract.controller.admin.term.vo.termparam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Description;

/**
 * 查询条款时关联参数vo
 *
 * @author doujl
 */
@Data
@Description("条款关联参数实体")
public class TermParamVO {


    @Schema(description = "主键")
    private String id;

    @Schema(description = "条款id")
    private String termId;

    @Schema(description = "参数名称")
    private String paramName;

    @Schema(description = "参数类型")
    private String paramType;

    @Schema(description = "参数编码")
    private String paramCode;

    @Schema(description = "参数值")
    private String paramValue;

    @Schema(description = "合同参数编号")
    private Integer paramNum;

    @Schema(description = "字典类型")
    private String dictType;
}
