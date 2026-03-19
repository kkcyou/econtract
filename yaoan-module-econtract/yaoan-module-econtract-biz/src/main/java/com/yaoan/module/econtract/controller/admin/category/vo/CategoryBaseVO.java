package com.yaoan.module.econtract.controller.admin.category.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author doujiale
 */
@Data
public class CategoryBaseVO {
    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分类名称不能为空")
    private String name;

    @Schema(description = "分类编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分类编码不能为空")
    private String code;

    @Schema(description = "父分类id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "父分类id不能为空")
    private Integer parentId;

    @Schema(title = "分类类型")
    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Long creator;
    /**
     * 创建人名称
     */
    private String creatorName;
}
