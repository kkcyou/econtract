package com.yaoan.module.bpm.framework.flowable.core.candidate;

import cn.hutool.core.map.MapUtil;
import com.yaoan.module.bpm.framework.flowable.core.enums.BpmTaskCandidateStrategyEnum;
import com.yaoan.module.bpm.framework.flowable.core.util.BpmnModelUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.yaoan.framework.common.enums.UtilConstants.MAP_KEY_SEPARATOR;
import static com.yaoan.framework.common.enums.UtilConstants.MAP_VALUE_SEPARATOR;
import static com.yaoan.module.bpm.framework.flowable.core.enums.BpmnModelConstants.USER_TASK_MAP_COMPANY_ID;
import static com.yaoan.module.bpm.framework.flowable.core.enums.BpmnModelConstants.USER_TASK_MAP_PARAM;

/**
 * BPM 任务的候选人的策略接口
 * <p>
 * 例如说：分配审批人
 *
 * @author 芋道源码
 */
public interface BpmTaskCandidateStrategy {

    /**
     * 对应策略
     *
     * @return 策略
     */
    BpmTaskCandidateStrategyEnum getStrategy();

    /**
     * 校验参数
     *
     * @param param 参数
     */
    void validateParam(String param);

    /**
     * 是否一定要输入参数
     *
     * @return 是否
     */
    default boolean isParamRequired() {
        return true;
    }

    /**
     * 基于候选人参数，获得任务的候选用户们
     *
     * @param param 执行任务
     * @return 用户编号集合
     */
    default Set<Long> calculateUsers(String param) {
        return Collections.emptySet();
    }

    /**
     * 基于执行任务，获得任务的候选用户们
     *
     * @param execution 执行任务S
     * @return 用户编号集合
     */
//    default Set<Long> calculateUsers(DelegateExecution execution, String param) {
//        Set<Long> users = calculateUsers(param);
//        removeDisableUsers(users);
//        return users;
//    }

    /**
     * 基于执行任务，获得任务的候选用户们
     * 该分配规则适用于产品和段位段的所有审批流
     * 规则：
     * 1，优先分配给发起人的同公司的符合模型角色的用户
     * 2，如果没有发起人同公司符合角色的用户，则分配非同公司的符合角色的用户（因为可能是集团的特有角色，所以发起人公司没有这角色）
     * @param execution
     * @param param
     * @return {@link Set }<{@link Long }>
     */
    default Set<Long> calculateUsers(DelegateExecution execution, String param) {
        // 如果公司id不为空，则要按照公司id找审批人
        String companyId = BpmnModelUtils.parseStarterCompanyId(execution.getCurrentFlowElement());
        if (StringUtils.isNotBlank(companyId)) {
            Map<String, String> map = new HashMap<>();
            map.put(USER_TASK_MAP_COMPANY_ID, companyId);
            map.put(USER_TASK_MAP_PARAM, param);
            String mapStr = MapUtil.join(map, MAP_KEY_SEPARATOR, MAP_VALUE_SEPARATOR);

            Set<Long> users = calculateUsers(mapStr);
            removeDisableUsers(users);
            return users;
        }
        // 如果公司id为空，则要只按租户找审批人
        Set<Long> users = calculateUsers(param);
        removeDisableUsers(users);
        return users;
    }

    /**
     * 基于流程实例，获得任务的候选用户们
     * <p>
     * 目的：用于获取未执行节点的候选用户们
     *
     * @param startUserId     流程发起人编号
     * @param processInstance 流程实例编号
     * @param activityId      活动 Id (对应 Bpmn XML id)
     * @param param           节点的参数
     * @return 用户编号集合
     */
    default Set<Long> calculateUsers(Long startUserId, ProcessInstance processInstance, String activityId, String param) {
        Set<Long> users = calculateUsers(param);
        removeDisableUsers(users);
        return users;
    }

    /**
     * 移除被禁用的用户
     *
     * @param users 用户 Ids
     */
    void removeDisableUsers(Set<Long> users);

}
