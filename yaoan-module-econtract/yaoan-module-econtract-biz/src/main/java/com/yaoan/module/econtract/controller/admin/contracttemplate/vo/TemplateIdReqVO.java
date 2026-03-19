package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/15 9:49
 */
@Data
public class TemplateIdReqVO {
    /**
     * 范本id
     */
    @NotBlank(message = "范本参数不可为空")
    String templateId;
}
