package com.yaoan.module.econtract.controller.admin.model.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/21 10:12
 */
@Data
public class ModelSubmitReqVo {
    /**
     * 模板id
     */
    @NotNull(message = "id不能为空")
    private String id;

}
