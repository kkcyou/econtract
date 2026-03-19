package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Schema(description = "Signet DetailsResp VO")
@Data
@ToString(callSuper = true)
public class SignetDetailsRespVO {

    /**
     * 印章ID
     */
    @Schema(description = "印章ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;


    /**
     * 印章名称
     */
    @Schema(description = "印章名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealName;

    /**
     * 印章编号
     */
    @Schema(description = "印章编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealCode;

    /**
     * 印章管理员id
     */
    @Schema(description = "印章管理员id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long sealAdminId;

    /**
     * 印章管理员名称
     */
    @Schema(description = "印章管理员名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealAdminName;

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
     * 印章规格编号
     */
    @Schema(description = "印章规格编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    /**
     * 印章规格id
     */
    @Schema(description = "印章规格id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer specsId;

    /**
     * 印章规格形状
     */
    @Schema(description = "印章规格形状", requiredMode = Schema.RequiredMode.REQUIRED)
    private String shape;

    /**
     * 长方形/椭圆/正方形的长度（mm）
     */
    private Double high;

    /**
     * 长方形/椭圆/正方形的宽度（mm）
     */
    private Double width;

    /**
     * PIN码
     */
    @Schema(description = "PIN码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String pinCode;

    /**
     * 用印审批流程key
     */
    @Schema(description = "用印审批流程key", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealProcess;

    /**
     * 用印审批流程名称
     */
    @Schema(description = "用印审批流程名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealProcessName;
}
