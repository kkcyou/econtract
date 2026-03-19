package com.yaoan.module.econtract.controller.admin.change.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ContractChangeListRespVO {
    /**
     * 变动合同id
     */
    private String id;

    /**
     * 变动合同名称
     */
    private String changeName;

    /**
     * 变动合同编号
     */
    private String changeCode;

    /**
     *  合同id
     */
    private String mainContractId;
    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同编码
     */
    private String contractCode;

    /**
     * 合同当前状态
     */
    private Integer contractStatus;

    /**
     * 合同当前状态名称
     */
    private String contractStatusName;


    /**
     * 审批状态
     */
    private Integer result;

    /**
     * 审批状态
     */
    private String resultName;

    /**
     * 变动类型（1=变更，2=补充，3=解除）
     * {@link com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums}
     */
    private Integer changeType;


    @Schema(description = "变动类型名称")
    private String changeTypeName;

    /**
     * 申请人id
     */
    private String creator;

    /**
     * 申请人姓名
     */
    private String creatorName;

    /**
     * 申请时间
     */
    private LocalDateTime createTime;
}

