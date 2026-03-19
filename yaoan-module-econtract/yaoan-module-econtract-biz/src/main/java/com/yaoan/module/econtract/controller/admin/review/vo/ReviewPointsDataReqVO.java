package com.yaoan.module.econtract.controller.admin.review.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "ReviewPointsTypeReqVO")
@Data
public class ReviewPointsDataReqVO extends PageParam {
    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private String id;

}
