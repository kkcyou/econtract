package com.yaoan.module.econtract.controller.admin.bpm.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author doujiale
 */
@Schema(description = "合同草拟审批申请创建 Request VO")
@Data
@ToString(callSuper = true)
public class BpmContractCreateReqVO {

    @NotBlank(message = "合同数据标识不能为空")
    @Schema(description = "合同数据标识")
    private String id;

    @Schema(description = "原因", example = "xxxx,需要审批")
    private String reason;


}
