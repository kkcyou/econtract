package com.yaoan.module.econtract.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Description;

import javax.validation.constraints.Size;

/**
 * @author doujl
 **/
@Data
@Description("条款详情VO")
public class TermsDetailsVo {
    /**
     * id
     */
    @Size(max = 64,message ="id过长")
    @Schema(description = "id")
    private String id;
    /**
     * 条款编码
     */
    @Size(max = 64,message ="条款编码过长")
    @Schema(description = "条款编码")
    private String termCode;
    /**
     * 条款名称
     */
    @Size(max = 64,message ="条款名称过长")
    @Schema(description = "条款名称")
    private String termName;
    /**
     * 条款类型(head合同封页，com合同条款，end合同结尾)
     */
    @Size(max = 16,message ="条款类型过长")
    @Schema(description = "条款类型")
    private String termType;
    /**
     * 条款内容
     */
    @Schema(description = "条款内容")
    private String termContent;
    /**
     * 条款序号
     */
    @Schema(description = "条款序号")
    private Integer termNum;

    /**
     * 是否展示序号
     */
    @Schema(description = "是否展示序号")
    private Boolean showSort;

    /**
     * 是否展示名称
     */
    private Boolean showName;

    /**
     * 是否必选
     */
    private Boolean isRequired;

    private String title;
    private String name;

    private String termComment;

    /**
     * 能否编辑 0能编辑  1不能编辑
     */
    private Integer editable;

    /**
     * 能否编辑 0能编辑  1不能编辑
     */
    private Boolean enableEdit;

    /**
     * 分类
     */
    private String termKind;

    /**
     * 分类
     */
    private String termKindName;

}
