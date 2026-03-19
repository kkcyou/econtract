package com.yaoan.module.econtract.controller.admin.term.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author doujiale
 */
@Schema(description = "term Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class TermPageReqVO extends PageParam {

    private static final long serialVersionUID = -5056797949597995107L;

    @Schema(description = "条款编码")
    private String code;

    @Schema(description = "条款名称")
    private String name;

    @Schema(description = "条款类型")
    private String termType;
    @Schema(description = "合同类型")
    private String contractType;
    /**
     * 状态 0未发布 1已发布
     */
    @Schema(description = "发布状态")
    private String status;
    /**
     * 条款分类-左边树形结构
     */
    @Schema(description = "条款分类id")
    private Integer categoryId;

    @Schema(description = "不需要返回的条款id列表")
    private List<String> ignoreTerms;

    private LocalDateTime ApproveTime0;

    /**
     * 条款库类别 0公共库，1单位库，2其他
     * {@link com.yaoan.module.econtract.enums.term.TermLibraryEnums}
     */
    private Integer termLibrary;

}
