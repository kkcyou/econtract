package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Schema(description = "Signet PageReq VO")
@Data
@ToString(callSuper = true)
public class SignetPageReqVO extends PageParam {
    private static final long serialVersionUID = -2695469046388079859L;
    /**
     * 印章名称
     */
    @Schema(description = "印章名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String sealName;

    /**
     * 印章类型
     */
    @Schema(description = "印章类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer sealType;

    /**
     * 印章状态（0:已停用，1：已启用，2：已注销，3：已过期）
     */
    @Schema(description = "印章状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer sealStatus;
}
