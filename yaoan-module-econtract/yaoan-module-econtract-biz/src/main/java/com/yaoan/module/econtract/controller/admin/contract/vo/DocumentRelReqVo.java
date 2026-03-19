package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "Document Rel Req VO")
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class DocumentRelReqVo {

    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractId;

    /**
     *附件名称
     */
    @Schema(description = "附件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    /**
     *附件地址id
     */
    @Schema(description = "附件地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long addId;

    /**
     *备注
     */
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;
}
