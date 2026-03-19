package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.yaoan.module.econtract.controller.admin.contractarchives.vo.AttachmentVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@Data
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SealBpmReqVO {
    /**
     * 用印申请id
     */
    private String id;
    /**
     * 印章id
     */
    @Schema(description = "印章id")
    private String sealId;

    /**
     * 合同id
     */
    @Schema(description = "合同id")
    private String contractId;

    /**
     * 印章名称
     */
    @Schema(description = "印章名称")
    private String sealName;

    /**
     * 申请事由
     */
    @Schema(description = "申请事由")
    private String reason;
    /**
     * 附件集合
     */
    @Schema(description = "附件集合", example = "30")
    private List<AttachmentVO> attachmentIds;

    /**
     * 提交标识
     */
    private Integer isSubmit;

    /**
     * 审批意见
     */
    private String advice;
}