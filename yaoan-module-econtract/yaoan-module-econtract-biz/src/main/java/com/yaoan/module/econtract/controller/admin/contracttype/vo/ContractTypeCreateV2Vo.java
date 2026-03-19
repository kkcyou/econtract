package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @description:
 * @author: Pele
 * @date: 2023/8/23 13:48
 */
@Data
@Schema(description = "合同类型")
@ToString(callSuper = true)
public class ContractTypeCreateV2Vo {

    /**
     * 合同类型名称
     */
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotNull(message = "合同类型名称不可为空")
    private String name;

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
     * 合同类型状态（0：关闭，1：开启）
     */
    @Schema(description = "合同类型状态（0：关闭，1：开启）", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private Integer typeStatus;

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
     * 起草审批流程
     */
    @Schema(description = "起草审批流程", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String draftApprovalProcess;

    /**
     * 登记流程
     */
    @Schema(description = "登记流程", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String registerProcess;

    /**
     * 变更流程
     */
    @Schema(description = "变更流程", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String changeProcess;

    /**
     * 收款流程
     */
    @Schema(description = "收款流程", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String collectionProcess;

    /**
     * 付款流程
     */
    @Schema(description = "付款流程", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String paymentProcess;

    /**
     * 借阅流程
     */
    @Schema(description = "借阅流程", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String borrowProcess;

    /**
     * 是否需要用印
     */
    @Schema(description = "是否需要用印", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean needSignet;

    /**
     * 审查清单ids
     */
    @Schema(description = "审查清单ids", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> checkListIds;

}
