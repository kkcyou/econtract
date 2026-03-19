package com.yaoan.module.econtract.controller.admin.signet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class SignetListRespVO {
    /**
     * 印章id
     */
    @Schema(description = "印章id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 印章名称
     */
    @Schema(description = "印章名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealName;

    /**
     * 印章图片id
     */
    @Schema(description = "印章图片", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long sealPictureId;
    /**
     * 印章图片地址
     */
    @Schema(description = "印章图片地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealPictureUrl;

    /**
     * PIN码
     */
    @Schema(description = "PIN码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String pinCode;

    /**
     * 印章编号
     */
    @Schema(description = "印章编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealCode;

    /**
     * 印章类型
     */
    @Schema(description = "印章类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sealType;
}
