package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "AttachmentRel CreateReq VO")
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class AttachmentRelCreateReqVO {
    private static final long serialVersionUID = 4348441633189721199L;
    /**
     * 附件名称
     */
    @Schema(description = "附件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String attachmentName;

    /**
     *附件文件地址Id
     */
    @Schema(description = "附件文件地址Id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long attachmentAddId;

    /**
     *附件类型
     */
    @Schema(description = "附件类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String attachmentType;

    //-----------------------------------系统对接新增------------------------------------

    /**
     * url
     */
    @Schema(description = "url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String url;
}
