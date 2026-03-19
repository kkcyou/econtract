package com.yaoan.module.bpm.controller.admin.task.vo.task.withdraw;

import lombok.Data;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/22 11:08
 */
@Data
public class RevokeProcessRespVo {

    private Boolean result;
    private String message;
    private HistoricTaskInstance LatestTask;
    private List<HistoricTaskInstance> LatestTaskList;

}
