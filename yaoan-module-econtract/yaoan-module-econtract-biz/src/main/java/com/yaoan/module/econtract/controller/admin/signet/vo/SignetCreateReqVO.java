package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Schema(description = "Signet CreateReq VO")
@Data
@ToString(callSuper = true)
public class SignetCreateReqVO {
    /**
     * 印章id
     */
    @Schema(description = "印章id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 印章名称
     */
    @Schema(description = "印章名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "印章名称不能为空")
    private String sealName;

    /**
     * 印章编号
     */
    @Schema(description = "印章编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "印章编号不能为空")
    private String sealCode;

    /**
     * 印章管理员id
     */
    @Schema(description = "印章管理员id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "印章管理员不能为空")
    private Long sealAdminId;

//    /**
//     * 印章类型
//     */
//    @Schema(description = "印章类型", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "印章类型不能为空")
//    private Integer sealType;

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
     * 是否长期有效（0：否，1：是）
     */
    @Schema(description = "是否长期有效", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isPermanent;

    /**
     * 印章有效期开始时间
     */
    @Schema(description = "印章有效期开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date sealStartDate;

    /**
     * 印章有效期结束时间
     */
    @Schema(description = "印章有效期开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date sealEndDate;

    /**
     * 印章规格id
     */
    @Schema(description = "印章规格", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "印章规格不能为空")
    private Integer specsId;

    /**
     * PIN码
     */
    @Schema(description = "PIN码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "PIN码不能为空")
    private String pinCode;

    /**
     * 用印审批流程key
     */
    @Schema(description = "用印审批流程key", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用印审批流程key不能为空")
    private String sealProcess;
}
