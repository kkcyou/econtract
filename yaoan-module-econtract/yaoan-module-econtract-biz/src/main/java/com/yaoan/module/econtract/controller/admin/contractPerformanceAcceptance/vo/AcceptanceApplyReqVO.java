package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo;

import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "管理后台 - 确认验收")
@Data
public class AcceptanceApplyReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "7848")
    @NotNull(message = "id不能为空")
    private String id;

    private String acceptanceId;
    
    @Schema(description = "验收开始时间")
    private LocalDate acceptanceStartTime;

    @Schema(description = "验收结束时间")
    private LocalDate acceptanceEndTime;

    @Schema(description = "验收负责人")
    private Long acceptanceUser;

    @Schema(description = "备注", example = "你说的对")
    private String remark;
    
    @Schema(description = "验收状态 申请0 验收通过1 验收不通过2", example = "2")
    private Integer status;

    @Schema(description = "验收备注", example = "你说的对")
    private String acceptanceRemark;
    
    @Schema(description = "验收附件列表")
    private List<BusinessFileDO> acceptanceFileList;


}