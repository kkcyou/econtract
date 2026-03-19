package com.yaoan.module.econtract.enums.common;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * {@link com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum}
 * @date: 2023/12/28 14:37
 */
@Getter
public enum CommonFlowableReqVOResultStatusEnums {
    /**
     * 通用列表工作流result标识 枚举类
     */
    NOT_START(-1, "NOT_START","未开始"),
    TO_SEND(0, "TO_SEND", "草稿"),
    APPROVING(1, "APPROVING", "审批中"),
    SUCCESS(2, "SUCCESS", "审批通过"),
    CANCEL(4, "TO_SEND", "草稿"),
    //进入审批流的任务，只可能first_level退到update节点变为退回
    REJECTED(5, "TO_SEND", "被退回"),
    ;

    private final Integer resultCode;
    private final String frontCode;
    private final String info;


    CommonFlowableReqVOResultStatusEnums(Integer resultCode, String frontCode, String info) {
        this.resultCode = resultCode;
        this.frontCode = frontCode;
        this.info = info;
    }

    public static CommonFlowableReqVOResultStatusEnums getInstance(Integer resultCode) {
        for (CommonFlowableReqVOResultStatusEnums value : CommonFlowableReqVOResultStatusEnums.values()) {
            if (value.getResultCode().equals(resultCode)) {
                return value;
            }
        }
        return null;
    }

    public static CommonFlowableReqVOResultStatusEnums getFrontInstance(String frontCode) {
        for (CommonFlowableReqVOResultStatusEnums value : CommonFlowableReqVOResultStatusEnums.values()) {
            if (value.getFrontCode().equals(frontCode)) {
                return value;
            }
        }
        return null;
    }
}
