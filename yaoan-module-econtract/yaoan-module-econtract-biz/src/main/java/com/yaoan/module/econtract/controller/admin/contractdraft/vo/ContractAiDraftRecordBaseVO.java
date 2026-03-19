package com.yaoan.module.econtract.controller.admin.contractdraft.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 合同智能起草记录 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class ContractAiDraftRecordBaseVO {

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
    private List<AITemplateInfo> templateInfoUse;

    @Schema(description = "模板信息列表（JSON数组）")
    private List<ContractTypeShortReqVO> templateInfoShowList;

    @Schema(description = "主体")
    private String mainBody;

    @Schema(description = "标的信息")
    private String target;

    @Schema(description = "价款与支付信息")
    private String priceAndPayment;

    @Schema(description = "履约信息")
    private String transaction;

    @Schema(description = "生成的合同内容")
    private String contractContent;

    @Schema(description = "合同生成方式")
    private String contractGenerateType;

    @Schema(description = "选择的模板")
    private String templateSelect;

}
