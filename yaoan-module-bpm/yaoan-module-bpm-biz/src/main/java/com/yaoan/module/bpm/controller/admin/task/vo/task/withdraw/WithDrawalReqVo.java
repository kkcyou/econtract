package com.yaoan.module.bpm.controller.admin.task.vo.task.withdraw;

import com.yaoan.module.bpm.enums.WithdrawalFlagEnums;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/22 16:55
 */
@Data
public class WithDrawalReqVo {

    private String taskId;
    private String reason;

    /**
     * 撤回的业务标识
     * {@link WithdrawalFlagEnums}
     * */
    private Integer businessFlag;

}
