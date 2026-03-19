package com.yaoan.module.econtract.controller.admin.param.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author doujiale
 */
@Data
public class ParamListRespVO {
    @Schema(description = "参数分类id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "参数分类id不能为空")
    private Integer id;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分类名称不能为空")
    private String name;

    @Schema(description = "父分类id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "父分类id不能为空")
    private Integer parentId;

    @Schema(title = "参数信息")
    private List<Map<String, Object>> params;


}
