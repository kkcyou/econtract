package com.yaoan.module.econtract.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/29 20:27
 */
@Getter
public class DelReqVo {
    @Schema(description = "id列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "id列表")
    private List<String> ids;

}
