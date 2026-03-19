package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/23 13:43
 */
@Data
@Schema(description = "合同类型列表响应VO")
@ToString(callSuper = true)
public class ContractTypePageV2RespVo {
    /**
     * 合同类型ID
     */
    @Schema(description = "合同类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String id;

    /**
     * 合同类型名称
     */
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String name;

    /**
     * 合同类型编码
     */
    @Schema(description = "合同类型编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String code;

    /**
     * 父类id
     */
    @Schema(description = "父类id", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String parentId;

    /**
     * 合同类型前缀
     */
    @Schema(description = "合同类型前缀", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotNull(message = "合同类型前缀不可为空")
    private String typePrefix;

    /**
     * 合同类型状态（0：关闭，1：开启）
     */
    @Schema(description = "合同类型状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer typeStatus;

    /**
     * 合同类型状态名称
     */
    @Schema(description = "合同类型状态名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String typeStatusName;

    /**
     * 编号生成规则ID
     */
    @Schema(description = "编号生成规则ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String codeRuleId;

    /**
     * 编号生成规则名称
     */
    @Schema(description = "编号生成规则名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String codeRuleName;

    /**
     * 流程配置数量
     */
    @Schema(description = "流程配置数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer processNum;

    /**
     * 创建者
     */
    @Schema(description = "创建者", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String creator;

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
     * 对应平台id
     */
    private String platId;
    /**
     * 是否需要用印
     */
    @Schema(description = "是否需要用印", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer needSignet;

    public ContractTypePageV2RespVo(String id, int count) {
        this.id = id;
        this.processNum = count;
    }
    private List<ContractTypePageV2RespVo> children = new ArrayList<>();
}
