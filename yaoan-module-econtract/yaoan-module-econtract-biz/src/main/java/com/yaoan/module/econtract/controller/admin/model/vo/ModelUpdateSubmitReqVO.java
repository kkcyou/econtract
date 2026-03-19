package com.yaoan.module.econtract.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 编辑模板-直接发起审批
 * @author: Pele
 * @date: 2023/10/24 16:02
 */
@Data
public class ModelUpdateSubmitReqVO extends ModelUpdateVO{
    /**
     * 审批说明
     * */
    @Schema(description = "审批说明")
    private String approveIntroduction;

}
