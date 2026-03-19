package com.yaoan.module.econtract.controller.admin.param.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.Date;

/**
 * 用于列表查询前端送值
 */
@Data
@Schema(description = "参数")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ParamPageReqVO extends PageParam {
    @Schema(description = "开始创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date startDate;

    @Schema(description = "创建结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date endDate;

    @Schema(description = "搜索字符串，用于模糊匹配参数名称，创建人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String searchText;

    @Schema(description = "参数分类id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer categoryId;

    @Schema(description = "参数名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creator;

    @Schema(description = "参数id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;
}
