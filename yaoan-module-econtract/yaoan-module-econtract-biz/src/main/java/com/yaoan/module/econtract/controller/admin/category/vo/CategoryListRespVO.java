package com.yaoan.module.econtract.controller.admin.category.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author doujiale
 */
@Data
public class CategoryListRespVO {
    @Schema(description = "分类id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "参数id不能为空")
    private Integer id;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分类名称不能为空")
    private String name;

    @Schema(description = "分类编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分类编码不能为空")
    private String code;

    @Schema(description = "父分类id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "父分类id不能为空")
    private Integer parentId;
    @Schema(description = "租户Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tenantId;
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;

    private Long creator;
    /**
     * 创建人名称
     */
    private String creatorName;
    
    @Schema(title = "子级信息")
    private List<CategoryListRespVO> children;
}
