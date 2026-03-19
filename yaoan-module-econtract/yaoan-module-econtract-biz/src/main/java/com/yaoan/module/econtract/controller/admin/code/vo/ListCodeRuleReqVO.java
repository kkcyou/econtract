package com.yaoan.module.econtract.controller.admin.code.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Schema(description = "ListCodeRuleReqVO")
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class ListCodeRuleReqVO  extends PageParam {
    /**
     * 名称
     */
    @Schema(description = "名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String name;

    /**
     * 状态
     */
    @Schema(description = "状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    Integer status;

    /**
     * 是否预留 0 否 1 是
     */
    @Schema(description = "是否预留", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Integer isReserve;

    /**
     * 编号id集合
     */
    @Schema(description = "编号id集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private List<String> ids;
    
    
}
