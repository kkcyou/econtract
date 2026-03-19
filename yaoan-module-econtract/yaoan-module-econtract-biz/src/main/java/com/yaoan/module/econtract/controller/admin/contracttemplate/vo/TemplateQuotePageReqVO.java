package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Schema(description = "TemplateQuote PageReq VO")
@Data
public class TemplateQuotePageReqVO extends PageParam {

    /**
     * 范本id
     */
    @NotBlank(message = "范本参数不可为空")
    String templateId;
}