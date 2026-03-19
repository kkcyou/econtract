package com.yaoan.module.bpm.framework.flowable.core.candidate.strategy;

import com.yaoan.framework.common.util.string.StrUtils;
import com.yaoan.module.bpm.framework.flowable.core.candidate.BpmTaskCandidateStrategy;
import com.yaoan.module.bpm.framework.flowable.core.enums.BpmTaskCandidateStrategyEnum;
import com.yaoan.module.system.api.dept.DeptApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static com.yaoan.framework.common.util.collection.CollectionUtils.convertSet;

/**
 * 部门的成员 {@link BpmTaskCandidateStrategy} 实现类
 *
 * @author kyle
 */
@Component
public class BpmTaskCandidateDeptMemberStrategy extends BpmTaskCandidateAbstractStrategy {

    private final DeptApi deptApi;

    public BpmTaskCandidateDeptMemberStrategy(AdminUserApi adminUserApi, DeptApi deptApi) {
        super(adminUserApi);
        this.deptApi = deptApi;
    }

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.DEPT_MEMBER;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> deptIds = StrUtils.splitToLongSet(param);
        deptApi.validateDeptList(deptIds);
    }

    @Override
    public Set<Long> calculateUsers(String param) {
        Set<Long> deptIds = StrUtils.splitToLongSet(param);
        List<AdminUserRespDTO> users = adminUserApi.getUserListByDeptIds(deptIds);
        return convertSet(users, AdminUserRespDTO::getId);
    }

}