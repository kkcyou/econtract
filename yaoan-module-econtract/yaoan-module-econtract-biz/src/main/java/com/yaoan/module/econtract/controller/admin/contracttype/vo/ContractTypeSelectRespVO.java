package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistRespVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewChecklistDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/13 11:45
 */
@Data
public class ContractTypeSelectRespVO {

    /**
     * 合同类型id
     * */
    private String id;
    /**
     * 合同类型名称
     */
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotNull(message = "合同类型名称不可为空")
    private String name;

    /**
     * 合同类型编码
     * */
    @Schema(description = "合同类型编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String code;

    /**
     * 合同类型前缀
     */
    @Schema(description = "合同类型前缀", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotNull(message = "合同类型前缀不可为空")
    private String typePrefix;

    /**
     * 合同类型父类id
     */
    @Schema(description = "合同类型父类id", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotNull(message = "合同类型父类id不可为空")
    private String parentId;

    /**
     * 父类名称
     */
    @Schema(description = "父类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String parentName;

    /**
     * 备注
     */
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String remark;
    /**
     * 编号生成规则ID
     */
    @Schema(description = "编号生成规则ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String codeRuleId;
    /**
     * 编号生成规则名称
     */
    @Schema(description = "编号生成规则名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String codeRuleName;


    /**
     * 起草审批流程
     */
    @Schema(description = "起草审批流程", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String draftApprovalProcess;
    @Schema(description = "起草审批流程名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String draftApprovalProcessName;

    /**
     * 登记流程
     */
    @Schema(description = "登记流程", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String registerProcess;
    @Schema(description = "登记流程名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String registerProcessName;

    /**
     * 变更流程
     */
    @Schema(description = "变更流程", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String changeProcess;
    @Schema(description = "变更流程名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String changeProcessName;

    /**
     * 收款流程
     */
    @Schema(description = "收款流程", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String collectionProcess;
    @Schema(description = "收款流程名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String collectionProcessName;

    /**
     * 付款流程
     */
    @Schema(description = "付款流程", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String paymentProcess;
    @Schema(description = "付款流程名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String paymentProcessName;

    /**
     * 借阅流程
     */
    @Schema(description = "借阅流程", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String borrowProcess;
    @Schema(description = "借阅流程名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String borrowProcessName;

    /**
     * 创建者
     */
    @Schema(description = "创建者", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Long creator;

    /**
     * 创建者昵称
     */
    @Schema(description = "创建者昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String creatorName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 是否预留 0 否 1 是
     */
    private Integer isReserve;

    /**
     * 是否需要用印
     */
    @Schema(description = "是否需要用印", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer needSignet;

    /**
     * 关联审查清单
     */
    private List<ReviewCheckListVO> reviewCheckList;


}
