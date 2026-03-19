package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2023/10/19 10:58
 */
@Data
public class TemplateCreateSubmitReqVO extends TemplateCreateVo {
    /**
     * 审批说明
     */
    @Schema(description = "审批说明")
    private String approveIntroduction;

}
