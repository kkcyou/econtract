package com.yaoan.module.econtract.controller.admin.model.vo;

import com.yaoan.module.econtract.controller.admin.bpm.model.vo.ModelBpmSubmitCreateReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 直接保存发送审批VO
 * @author: Pele
 * @date: 2023/10/19 10:24
 */
@Data
public class ModelCreateSubmitReqVO extends ModelCreateReqVO{

    /**
     * 审批说明
     * */
    @Schema(description = "审批说明")
    private String approveIntroduction;
}
