package com.yaoan.module.bpm.framework.flowable.core.candidate.strategy;

import com.yaoan.framework.common.util.string.StrUtils;
import com.yaoan.module.bpm.dal.dataobject.definition.BpmUserGroupDO;
import com.yaoan.module.bpm.framework.flowable.core.candidate.BpmTaskCandidateStrategy;
import com.yaoan.module.bpm.framework.flowable.core.enums.BpmTaskCandidateStrategyEnum;
import com.yaoan.module.bpm.service.definition.BpmUserGroupService;
import com.yaoan.module.system.api.user.AdminUserApi;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.yaoan.framework.common.util.collection.CollectionUtils.convertSetByFlatMap;

/**
 * 用户组 {@link BpmTaskCandidateStrategy} 实现类
 *
 * @author kyle
 */
@Component
public class BpmTaskCandidateGroupStrategy extends BpmTaskCandidateAbstractStrategy {

    private final BpmUserGroupService userGroupService;

    public BpmTaskCandidateGroupStrategy(AdminUserApi adminUserApi, BpmUserGroupService userGroupService) {
        super(adminUserApi);
        this.userGroupService = userGroupService;
    }

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.USER_GROUP;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> groupIds = StrUtils.splitToLongSet(param);
        userGroupService.getUserGroupList(groupIds);
    }

    @Override
    public Set<Long> calculateUsers(String param) {
        Set<Long> groupIds = StrUtils.splitToLongSet(param);
        List<BpmUserGroupDO> groups = userGroupService.getUserGroupList(groupIds);
        return convertSetByFlatMap(groups, BpmUserGroupDO::getUserIds, Collection::stream);
    }

}