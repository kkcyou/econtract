package com.yaoan.module.econtract.controller.admin.term.vo.termlabel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author doujl
 */
@Data
@Schema(description = "条款关联标签实体")
public class TermLabelVO {

    /**
     * 主键
     */
    @Schema(description = "主键")
    private String id;

    /**
     * 标签名称
     */
    @Schema(description = "标签名称")
    private String labelName;

    /**
     * 条款id
     */
    @Schema(description = "条款id")
    private String termId;

}
