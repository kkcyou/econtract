package com.yaoan.module.econtract.controller.admin.workbench.vo.alert;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/5 15:41
 */
@Data
public class WorkBenchMsgAlertRespVO {

    private String msg;

    private LocalDateTime createTime;

}
