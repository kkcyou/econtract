package com.yaoan.module.bpm.enums.task;

import cn.hutool.core.util.ArrayUtil;
import com.yaoan.framework.common.util.object.ObjectUtils;
import com.yaoan.module.bpm.enums.definition.BpmFieldPermissionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 电子合同流程实例的结果
 *
 * @author doujl
 */
@Getter
@AllArgsConstructor
public enum ContractProcessInstanceResultEnum {

    SEND(0, "发送", "send"),

    CONFIRM(1, "确认", "confirm"),

    // ========== 流程任务独有的状态 ==========

    REJECT(2, "退回", "reject");

    /**
     * 结果
     */
    private final Integer result;
    /**
     * 描述
     */
    private final String desc;
    /**
     * 指令
     */
    private final String command;

    public static ContractProcessInstanceResultEnum valueOf(Integer result) {
        return ArrayUtil.firstMatch(item -> item.getResult().equals(result), values());
    }
}
