package com.yaoan.module.econtract.controller.admin.bpm.archive.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "合同归档审批申请创建 Request VO")
@Data
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveBpmReqVO {
    /**
     * 归档id
     */
    @Schema(description = "归档id")
    private String archiveId;

    /**
     * 补充备注
     */
    @Schema(description = "补充备注")
    private String reason;

    /**
     * 审批类型 0:归档 - 1:补充
     */
    @Schema(description = "审批类型 0:归档 - 1:补充")
    private Integer type;
}