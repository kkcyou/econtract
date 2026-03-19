package com.yaoan.module.econtract.controller.admin.term.vo.termparam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Description;

import javax.validation.constraints.NotEmpty;

/**
 * 合同参数Vo
 *
 * @author doujl
 */
@Data
@Description("合同参数Vo")
public class ParamVO {

    /**
     * 合同参数id
     */
    @NotEmpty(message = "合同参数id不可为空")
    @Schema(description = "合同参数id")
    private String paramId;

    /**
     * 合同参数序号
     */
    @NotEmpty(message = "合同参数序号不可为空")
    @Schema(description = "合同参数序号")
    private Integer paramNum;
}
