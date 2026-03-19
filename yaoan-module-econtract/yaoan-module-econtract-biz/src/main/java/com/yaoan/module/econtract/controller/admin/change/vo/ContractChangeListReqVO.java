package com.yaoan.module.econtract.controller.admin.change.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;



@Data
public class ContractChangeListReqVO extends PageParam {

    /**
     * 变动合同名称
     */
    @Schema(description = "变动合同名称")
    private String name;

    /**
     * 变动合同编号
     */
    @Schema(description = "变动合同编号")
    private String code;

    /**
     * 审批状态
     */
    @Schema(description = "审批状态")
    private Integer result;

    /**
     * 变动类型（1=变更，2=补充，3=解除）
     * {@link com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums}
     */
    @Schema(description = "变动类型")
    private Integer changeType;

    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同编码
     */
    private String contractCode;


}
