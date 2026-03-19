package com.yaoan.module.econtract.controller.admin.param.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量删除和校验是否有调用信息使用
 */
@Data
public class ReqIdsVO {

    @Schema(description = "ids列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ids列表")
    private List<String> ids;
}
