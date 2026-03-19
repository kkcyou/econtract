package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo;

import com.yaoan.module.econtract.controller.admin.reviewitembasis.vo.ReviewItemBasisBaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 合同审查规则 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 * @author wsh
 */
@Data
public class ContractReviewItemsBaseVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "审查内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "审查内容不能为空")
    private String reviewContent;

    @Schema(description = "所属条款", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "所属条款不能为空")
    private Integer termId;
    @Schema(description = "所属条款名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String termName;

    @Schema(description = "适用范围(适用的合同分类)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "适用范围(适用的合同分类)不能为空")
    private List<ReviewContractTypeVO> contractTypes;

    @Schema(description = "风险不利方", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "风险不利方不能为空")
    private Integer riskParty;
    @Schema(description = "风险不利方", requiredMode = Schema.RequiredMode.REQUIRED)
    private String riskPartyName;

    @Schema(description = "风险等级", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "风险等级不能为空")
    private Integer riskLevel;
    @Schema(description = "风险等级")
    private String riskLevelName;

    @Schema(description = "法律依据")
    private List<ReviewItemBasisBaseVO> legalBasis;

    @Schema(description = "备注")
    private String notes;

    @Schema(description = "状态")
    private Integer reviewStatus;

}
