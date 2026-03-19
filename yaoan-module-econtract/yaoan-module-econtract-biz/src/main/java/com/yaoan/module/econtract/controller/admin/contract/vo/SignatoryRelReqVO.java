package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import liquibase.pro.packaged.B;
import lombok.*;

@Schema(description = "SignatoryRel Req VO")
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SignatoryRelReqVO {

    /**
     * 签署方id
     */
    @Schema(description = "签署方id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String signatoryId;

    /**
     * 签署方主体名称
     */
    @Schema(description = "签署方主体名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String signatoryName;

    /**
     * 1. 甲方 2. 乙方 3. 丙方 4. 丁方
     */
    @Schema(description = "签署方主体角色", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer type;

    /**
     * 是否是发起人
     */
    @Schema(description = "是否是发起人")
    private Boolean initiator;

    /**
     * 用户id
     */
    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long userId;

    /**
     * 签署顺序
     */
    private Integer sort;
}
