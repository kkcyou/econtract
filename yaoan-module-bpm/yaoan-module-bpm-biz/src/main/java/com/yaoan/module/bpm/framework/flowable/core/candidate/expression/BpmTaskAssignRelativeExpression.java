package com.yaoan.module.bpm.framework.flowable.core.candidate.expression;

import com.yaoan.module.bpm.service.task.BpmProcessInstanceService;
import com.yaoan.module.econtract.api.relative.RelativeApi;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: 分配给相对方下属的用户
 * @author: Pele
 * @date: 2025/3/13 14:58
 */
@Component
public class BpmTaskAssignRelativeExpression {

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
    public Set<Long> calculateUsers(DelegateExecution execution, int signatureType) {
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        String contractId = processInstance.getBusinessKey();
        List<Long> userId = relativeApi.calculateUsers4RelativeExpression(contractId, signatureType);
        return new HashSet<>(userId);
    }

    /**
     * 获取relativeId
     *
     * @param execution 流程执行实体
     * @param containCreatorFlag  0: 不包含发起人 1: 包含发起人
     * @return relativeId
     */
    public Set<Long> calculateRelatives(DelegateExecution execution, Integer containCreatorFlag) {
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        String contractId = processInstance.getBusinessKey();
        List<Long> userId = relativeApi.calculateRelativesExpression(contractId, containCreatorFlag);
        return new HashSet<>(userId);
    }

    /**
     * 计算审批的依次候选人
     *
     * @param execution 流程执行实体
     * @return 发起人
     */
    public List<Long> calculateUsers4Sort(DelegateExecution execution, Integer containCreatorFlag) {
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        String contractId = processInstance.getBusinessKey();
        List<Long> userIds = relativeApi.calculateUsers4SortedRelativeExpression(contractId,containCreatorFlag);
//        return  new LinkedHashSet<>(userId);
        return userIds;
    }
}
