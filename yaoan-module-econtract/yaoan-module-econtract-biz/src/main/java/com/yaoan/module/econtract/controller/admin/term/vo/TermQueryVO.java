package com.yaoan.module.econtract.controller.admin.term.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Description;

import java.util.List;

/**
 * 电子合同条款模糊查询条件实体
 *
 * @author doujl
 */
@Data
@Description("电子合同条款模糊查询条件实体")
public class TermQueryVO {

    /**
     * 条款编码
     */
    @Schema(description = "条款编码")
    private String code;

    /**
     * 条款名称
     */
    @Schema(description = "条款名称")
    private String name;

    /**
     * 条款类型(head合同封页，com合同条款，end合同结尾)
     */
    @Schema(description = "条款类型")
    private String termType;

    /**
     * 条款分类-左边树形结构
     */
    @Schema(description = "条款分类id")
    private Integer categoryId;

    /**
     * 合同类型
     */
    @Schema(description = "合同类型")
    private String contractType;

    @Schema(description = "不需要返回的条款id列表")
    private List<String> ignoreTerms;

}
