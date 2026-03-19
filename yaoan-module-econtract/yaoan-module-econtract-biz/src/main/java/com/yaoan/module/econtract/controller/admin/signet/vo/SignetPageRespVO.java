package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "Signet PageResp VO")
@Data
@ToString(callSuper = true)
public class SignetPageRespVO {
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

//    /**
//     * 印章类型
//     */
//    @Schema(description = "印章类型", requiredMode = Schema.RequiredMode.REQUIRED)
//    private Integer sealType;
//    /**
//     * 印章类型名称
//     */
//    @Schema(description = "印章类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
//    private String sealTypeName;

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
     * 印章状态
     */
    @Schema(description = "印章状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sealStatus;

    /**
     * 印章状态名称
     */
    @Schema(description = "印章状态名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealStatusName;

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
