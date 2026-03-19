package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewRulesRespVO {

    @Schema(description = "审查内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reviewContent;

    @Schema(description = "审查id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reviewId;

}
