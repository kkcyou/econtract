package com.yaoan.module.econtract.controller.admin.bpm.performance.suspend.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author doujiale
 */
@Schema(description = "履约中止申请创建 Request VO")
@Data
@ToString(callSuper = true)
public class BpmSuspendCreateReqVO {

    @NotBlank(message = "履约数据标识不能为空")
    @Schema(description = "履约数据标识")
    private String id;

    @Schema(description = "原因", example = "xxxx,需要中止")
    private String reason;


}
