package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Schema(description = "contract更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractUpdateReqVO extends ContractBaseVO{
    private static final long serialVersionUID = 1522517226932094577L;

    @Schema(description = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    /**
     * 主文件地址id
     */
    @Schema(description = "主文件地址id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long fileAddId;

    /**
     * 签署状态
     * 0-待发送 - 新增
     * 1-被退回
     * 2-已发送
     * 3-待确认
     * 4-待签署
     * 5-已拒签
     * 6-签署完成
     * 7-逾期未签署
     */
    @Schema(description = "签署状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    /**
     * 附件集合
     */
    @Schema(description = "附件集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<AttachmentRelCreateReqVO> attachmentList;

    /**
     * 签署方id集合
     */
    @Schema(description = "签署方id集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SignatoryRelReqVO> signatoryList;
}
