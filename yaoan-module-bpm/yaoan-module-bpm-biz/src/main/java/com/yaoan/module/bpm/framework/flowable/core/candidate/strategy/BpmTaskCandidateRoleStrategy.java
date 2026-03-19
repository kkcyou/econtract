package com.yaoan.module.bpm.framework.flowable.core.candidate.strategy;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.common.util.string.StrUtils;
import com.yaoan.module.bpm.framework.flowable.core.candidate.BpmTaskCandidateStrategy;
import com.yaoan.module.bpm.framework.flowable.core.enums.BpmTaskCandidateStrategyEnum;
import com.yaoan.module.system.api.permission.PermissionApi;
import com.yaoan.module.system.api.permission.RoleApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.yaoan.framework.common.enums.UtilConstants.MAP_KEY_SEPARATOR;
import static com.yaoan.framework.common.enums.UtilConstants.MAP_VALUE_SEPARATOR;
import static com.yaoan.module.bpm.framework.flowable.core.enums.BpmnModelConstants.USER_TASK_MAP_COMPANY_ID;
import static com.yaoan.module.bpm.framework.flowable.core.enums.BpmnModelConstants.USER_TASK_MAP_PARAM;

/**
 * 角色 {@link BpmTaskCandidateStrategy} 实现类
 *
 * @author kyle
 */
@Component
public class BpmTaskCandidateRoleStrategy extends BpmTaskCandidateAbstractStrategy {

    @Resource
    private RoleApi roleApi;
    @Resource
    private PermissionApi permissionApi;

    public BpmTaskCandidateRoleStrategy(AdminUserApi adminUserApi) {
        super(adminUserApi);
    }

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.ROLE;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> roleIds = StrUtils.splitToLongSet(param);
        roleApi.validRoleList(roleIds);
    }

    @Override
    public Set<Long> calculateUsers(String param) {
        Set<Long> userIds = new HashSet<>();
        //只有角色策略可解析出公司id
        //先解析出公司id和角色
        Map<String, String> map = StrUtils.splitToMap(param, MAP_KEY_SEPARATOR, MAP_VALUE_SEPARATOR);
        String roleListStr = map.get(USER_TASK_MAP_PARAM);
        String companyId = map.get(USER_TASK_MAP_COMPANY_ID);
        Set<Long> roleIds = StrUtils.splitToLongSet(roleListStr);

        // 则优先选用同公司的用户
        userIds = permissionApi.getUserRoleIdListByRoleIdsAndCompanyId(roleIds, Long.valueOf(companyId));
        if (CollectionUtil.isEmpty(userIds)) {
            // 如果该角色不存在发起人同公司的用户
            // 则选用非同公司的用户
            userIds = permissionApi.getUserRoleIdListByRoleIdsV2(roleIds);
        }

        return userIds;
    }

}