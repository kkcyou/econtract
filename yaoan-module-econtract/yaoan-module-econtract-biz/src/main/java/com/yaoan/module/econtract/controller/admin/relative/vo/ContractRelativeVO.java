package com.yaoan.module.econtract.controller.admin.relative.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author doujiale
 */
@Data
@Schema(description = "返回合同签约方详细信息")
@ToString(callSuper = true)
public class ContractRelativeVO  {

    @Schema(description = "签署方名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String companyName;

    @Schema(description = "统一社会信用代码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creditCode;

    @Schema(description = "联系人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "电子签约平台账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String account;
    @Schema(description = "签署方地位", requiredMode = Schema.RequiredMode.REQUIRED)
    private String signatoryPosition;
    @Schema(description = "主体类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String entityType;
    @Schema(description = "主体类型值", requiredMode = Schema.RequiredMode.REQUIRED)
    private String entityTypeName;


}
