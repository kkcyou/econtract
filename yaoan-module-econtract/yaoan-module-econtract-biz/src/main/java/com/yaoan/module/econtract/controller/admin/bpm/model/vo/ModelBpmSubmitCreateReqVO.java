package com.yaoan.module.econtract.controller.admin.bpm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author Pele
 */
@Schema(description = "模板审批申请创建 Request VO")
@Data
@ToString(callSuper = true)
public class ModelBpmSubmitCreateReqVO {

    /**
     * 模板id
     * */
    @NotBlank(message = "模板id不能为空")
    @Schema(description = "模板id")
    private String modelId;

    /**
     * 审批说明
     * */
    @Schema(description = "审批说明")
    private String approveIntroduction;

}
