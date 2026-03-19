package com.yaoan.module.econtract.controller.admin.relative.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author doujiale
 */
@Data
@Schema(description = "修改相对方信息")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RelativeUpdateReqVO extends BaseRelativeVO {
    @Schema(description = "相对方id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "相对方id不能为空")
    private String id;

    /**
     * 相对方公司id
     */
    private Long relativeCompanyId;

}
