package com.yaoan.module.bpm.framework.flowable.core.candidate.strategy;

import cn.hutool.core.convert.Convert;
import com.yaoan.module.bpm.framework.flowable.core.candidate.BpmTaskCandidateStrategy;
import com.yaoan.module.bpm.framework.flowable.core.enums.BpmTaskCandidateStrategyEnum;
import com.yaoan.module.bpm.framework.flowable.core.util.FlowableUtils;
import com.yaoan.module.system.api.user.AdminUserApi;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程表达式 {@link BpmTaskCandidateStrategy} 实现类
 *
 * @author 芋道源码
 */
@Component
public class BpmTaskCandidateExpressionStrategy extends BpmTaskCandidateAbstractStrategy {

    public BpmTaskCandidateExpressionStrategy(AdminUserApi adminUserApi) {
        super(adminUserApi);
    }

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.EXPRESSION;
    }

    @Override
    public void validateParam(String param) {
        // do nothing 因为它基本做不了校验
    }

    @Override
    public Set<Long> calculateUsers(DelegateExecution execution, String param) {
        Object result = FlowableUtils.getExpressionValue(execution, param);
//        Set<Long> users = Convert.toSet(Long.class, result);
        Set<Long> users;

        // Solving the problem that HashSet stores elements based on hash codes and does not guarantee the insertion order or any specific order.
        if (result instanceof Collection) {
            // 使用LinkedHashSet保持顺序
            users = new LinkedHashSet<>(((Collection<?>) result).stream()
                    .map(obj -> Convert.convert(Long.class, obj))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        } else {
            // 单独处理非集合类型
            users = Collections.singleton(Convert.convert(Long.class, result));
        }
        removeDisableUsers(users);
        return users;
    }

}