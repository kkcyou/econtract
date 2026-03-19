package com.yaoan.module.econtract.controller.admin.term.vo.termlabel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Description;

import javax.validation.constraints.NotEmpty;

/**
 * 条款标签Vo
 *
 * @author doujl
 */
@Data
@Description("条款标签Vo")
public class LabelVO {

    /**
     * 标签id
     */
    @NotEmpty(message = "标签id不可为空")
    @Schema(description = "标签id")
    private String labelId;

    /**
     * 标签名称
     */
    @NotEmpty(message = "标签名称不可为空")
    @Schema(description = "标签名称")
    private String labelName;
}
