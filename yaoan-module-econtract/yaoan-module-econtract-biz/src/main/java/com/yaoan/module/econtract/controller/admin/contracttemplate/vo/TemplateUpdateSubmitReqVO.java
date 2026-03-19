package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 编辑范本_直接发送审批VO
 * @author: Pele
 * @date: 2023/10/24 16:23
 */
@Data
public class TemplateUpdateSubmitReqVO extends TemplateUpdateReqVO {
    /**
     * 审批说明
     */
    @Schema(description = "审批说明")
    private String approveIntroduction;
}
