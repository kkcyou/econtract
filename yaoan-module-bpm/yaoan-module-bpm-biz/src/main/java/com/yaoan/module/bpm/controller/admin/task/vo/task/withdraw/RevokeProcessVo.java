package com.yaoan.module.bpm.controller.admin.task.vo.task.withdraw;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/22 10:11
 */
@Data
public class RevokeProcessVo {
    private String processInstanceId;
    private String userId;

}
