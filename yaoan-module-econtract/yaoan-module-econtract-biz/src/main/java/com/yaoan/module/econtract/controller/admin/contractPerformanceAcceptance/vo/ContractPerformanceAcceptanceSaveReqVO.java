package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo;

import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "管理后台 - 验收新增/修改 Request VO")
@Data
public class ContractPerformanceAcceptanceSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "7848")
    private String id;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "标题")
    private String title;

    /**
     * 合同id
     */
    private String contractId;
    /**
     * 计划id
     */
    private String planId;

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
    

    @Schema(description = "验收申请附件列表")
    private List<BusinessFileDO> applyFileList;

    private  Integer sort;

}