package com.yaoan.module.econtract.controller.admin.relative.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import javax.validation.constraints.NotNull;

/**
 * @author doujiale
 */
@Data
@Schema(description = "新增相对方信息")
@ToString(callSuper = true)
public class ContactReqVO {
    @Schema(description = "相对方id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "相对方id不能为空")
    private String id;

    @Schema(description = "联系人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactName;

    @Schema(description = "联系人手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactTel;

    @Schema(description = "联系人邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactEmail;

    @Schema(description = "联系人备注", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactRemark;


    @Schema(description = "联系人电子签约平台账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactAccount;

    @Schema(description = "相对方公司id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long relativeCompanyId;

}
