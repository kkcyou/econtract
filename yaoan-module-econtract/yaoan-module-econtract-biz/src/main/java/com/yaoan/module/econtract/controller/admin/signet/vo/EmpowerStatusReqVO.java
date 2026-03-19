package com.yaoan.module.econtract.controller.admin.signet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ToString(callSuper = true)
public class EmpowerStatusReqVO {
    /**
     * 授权信息id
     */
    @Schema(description = "授权信息id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "授权信息id不能为空")
    private String id;

    /**
     * 授权状态
     */
    @Schema(description = "授权状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "授权状态不能为空")
    private Integer empowerStatus;

}
