package com.yaoan.module.bpm.framework.flowable.core.candidate.expression;

import com.yaoan.module.bpm.service.task.BpmProcessInstanceService;
import com.yaoan.module.econtract.api.relative.RelativeApi;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-8 15:27
 */
@Component
public class BpmTaskAssignSortedRelativeExpression {


    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private RelativeApi relativeApi;

    /**
     * 计算审批的候选人
     *
     * @param execution 流程执行实体
     * @return 发起人
     */
    public Set<Long> calculateUsers(DelegateExecution execution, Integer containCreatorFlag) {
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        String contractId = processInstance.getBusinessKey();
        List<Long> userId = relativeApi.calculateUsers4SortedRelativeExpression(contractId,containCreatorFlag);
        return new HashSet<>(userId);
    }

    public Set<Long> calculateRelatives(DelegateExecution execution, Integer containCreatorFlag) {
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        String contractId = processInstance.getBusinessKey();
        List<Long> userId = relativeApi.calculateRelativesExpression(contractId, containCreatorFlag);
        return new HashSet<>(userId);
    }
}
