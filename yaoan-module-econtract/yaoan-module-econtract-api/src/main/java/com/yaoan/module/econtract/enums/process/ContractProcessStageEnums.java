package com.yaoan.module.econtract.enums.process;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2023/11/8 18:22
 */
@Getter
public enum ContractProcessStageEnums {
    /**
     * 工作流—签署阶段
     */
    CONTRACT_FLOW_STAGE_SIGN("sign", "合同签署提醒"),

    /**
     * 工作流—确认阶段
     */
    CONTRACT_FLOW_STAGE_CONFIRM("confirm", "合同确认提醒"),

    /**
     * 工作流—审批阶段
     */
    CONTRACT_FLOW_STAGE_APPROVE_LAW_WORKS("law_works", "合同审批提醒"),
    CONTRACT_FLOW_STAGE_APPROVE_DEPARTMENT_LEADER("department_leader", "合同审批提醒"),

    /**
     * 工作流—起草阶段
     */
    CONTRACT_FLOW_STAGE_CREATE("create", "合同起草提醒");;

    private final String code;
    private final String info;

    ContractProcessStageEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ContractProcessStageEnums getInstance(String code) {
        for (ContractProcessStageEnums value : ContractProcessStageEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
