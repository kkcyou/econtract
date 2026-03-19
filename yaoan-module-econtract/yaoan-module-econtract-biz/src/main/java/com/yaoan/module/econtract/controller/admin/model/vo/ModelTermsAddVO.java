package com.yaoan.module.econtract.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Description;

/**
 * 2021/1/6 10:49
 *
 * @author doujiale
 **/
@Data
@Description("合同条款Vo")
public class ModelTermsAddVO {
    /**
     * 条款排序
     */
    @Schema(description = "条款排序")
    private Integer termNum;
    /**
     * 条款id
     */
    @Schema(description = "条款id")
    private String termId;

    /**
     * title
     */
    @Schema(description = "title")
    private String title;


    /**
     * name
     */
    @Schema(description = "name")
    private String name;

    /**
     * 能否编辑 0能编辑  1不能编辑
     */
    private Integer editable;
}
