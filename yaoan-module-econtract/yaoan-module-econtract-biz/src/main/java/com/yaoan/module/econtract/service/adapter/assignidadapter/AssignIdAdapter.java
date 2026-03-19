package com.yaoan.module.econtract.service.adapter.assignidadapter;

import com.yaoan.module.bpm.api.task.dto.SimpleTaskDTO;

import java.util.List;
import java.util.Map;

/**
 * @description: 获得待办任务的被分派人的适配器
 * @author: Pele
 * @date: 2024/2/5 10:28
 */
public interface AssignIdAdapter {
    /**
     * 通过流程实例ids，获得被分派人id
     * <流程实例id，被分派人id>
     * */
    Map<String, String> getUserIdList(List<String> instanceList);
}
