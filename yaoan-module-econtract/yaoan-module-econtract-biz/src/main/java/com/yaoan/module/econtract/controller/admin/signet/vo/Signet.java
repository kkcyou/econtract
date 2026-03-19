package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Schema(description = "Signet DetailsResp VO")
@Data
@ToString(callSuper = true)
public class Signet {

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
     * 印章编号
     */
    @Schema(description = "印章编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealCode;

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
    private Integer empowerIsPermanent;

    /**
     * 授权开始时间
     */
    @Schema(description = "授权开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date empowerStartDate;

    /**
     * 授权结束时间
     */
    @Schema(description = "授权结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date empowerEndDate;

    /**
     * 授权人
     */
    @Schema(description = "授权人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String licensor;

    /**
     * 授权时间
     */
    @Schema(description = "授权时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date empowerDate;

    /**
     *  授权状态（0：失效，1：正常）
     */
    @Schema(description = " 授权状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer empowerStatus;
    /**
     *  授权状态名称
     */
    @Schema(description = " 授权状态名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String empowerStatusName;

    /**
     * 用印文件数
     */
    @Schema(description = "用印文件数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long empowerCount;


}
