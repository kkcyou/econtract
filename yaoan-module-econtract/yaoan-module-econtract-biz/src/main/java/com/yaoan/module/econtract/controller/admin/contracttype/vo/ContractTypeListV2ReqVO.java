package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/23 13:38
 */
@Data
@Schema(description = "合同类型")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractTypeListV2ReqVO extends PageParam {

    /**
     * 合同类型名称
     */
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private String name;

    /**
     * 合同类型状态
     */
    @Schema(description = "合同类型状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private Integer typeStatus;
    /**
     * 合同类型id集合
     */
    @Schema(description = "合同类型id集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private List<String> ids;

}
