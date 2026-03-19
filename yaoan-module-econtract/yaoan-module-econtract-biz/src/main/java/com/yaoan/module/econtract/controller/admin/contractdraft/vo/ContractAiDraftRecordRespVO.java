package com.yaoan.module.econtract.controller.admin.contractdraft.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 合同智能起草记录 Response VO")
@Data
@ToString(callSuper = true)
public class ContractAiDraftRecordRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1529")
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;


    @Schema(description = "标题")
    private String title;

    @Schema(description = "使用的大模型", example = "芋艿")
    private String llmName;

    @Schema(description = "文件id", example = "22535")
    private String fileId;

    @Schema(description = "快照状态（0需求填写、1生成中、2已生成）", example = "1")
    private String snapshotStatus;

    @Schema(description = "完整快照内容")
    private String snapshot;

    @Schema(description = "关联的合同ID", example = "6810")
    private String contractId;

    @Schema(description = "合同名称", example = "赵六")
    private String contractName;

    @Schema(description = "合同类型（如：采购合同服务合同）", example = "1")
    private String contractType;

    @Schema(description = "模板信息列表（JSON数组）")
    private String templateInfo;

    @Schema(description = "其他模板信息列表（JSON数组）")
    private String templateInfoShow;
    /**
     * 生成的合同内容
     */
    @Schema(description = "生成的合同内容")
    private String contractContent;

    /**
     * 合同生成方式
     */
    @Schema(description = "合同生成方式")
    private String contractGenerateType;

    @Schema(description = "选择的模板")
    private String templateSelect;
}
