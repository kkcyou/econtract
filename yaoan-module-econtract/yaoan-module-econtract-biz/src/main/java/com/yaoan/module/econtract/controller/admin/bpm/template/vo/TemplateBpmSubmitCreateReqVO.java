package com.yaoan.module.econtract.controller.admin.bpm.template.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/14 14:57
 */
@Schema(description = "范本审批申请创建 Request VO")
@Data
@ToString(callSuper = true)
public class TemplateBpmSubmitCreateReqVO {
    @NotBlank(message = "范本id不能为空")
    @Schema(description = "范本id")
    private String id;

    /**
     * 审批说明
     * */
    @Schema(description = "审批说明")
    private String approveIntroduction;


}
