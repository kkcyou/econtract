package com.yaoan.module.bpm.framework.flowable.core.candidate.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.bpm.framework.flowable.core.candidate.BpmTaskCandidateStrategy;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.api.relative.RelativeApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link BpmTaskCandidateStrategy} 抽象类
 *
 * @author jason
 */
public abstract class BpmTaskCandidateAbstractStrategy implements BpmTaskCandidateStrategy {

    protected AdminUserApi adminUserApi;

    public BpmTaskCandidateAbstractStrategy(AdminUserApi adminUserApi) {
        this.adminUserApi = adminUserApi;
    }

    @Override
    public void removeDisableUsers(Set<Long> users) {
        if (CollUtil.isEmpty(users)) {
            return;
        }
        AtomicReference<Map<Long, AdminUserRespDTO>> userMap = new AtomicReference<>();
        Map relativeMap = new HashMap<>();
        //（相对方逻辑）免租户
        DataPermissionUtils.executeIgnore(()->{
            TenantUtils.executeIgnore(()->{
                RelativeApi relativeApi = SpringUtil.getBean(RelativeApi.class);
                List<Long> existRelativeVirtualIds = relativeApi.getExistRelativeVirtualIds(users);
                relativeMap.put("existRelativeVirtualIds", existRelativeVirtualIds);
                Map<Long, AdminUserRespDTO>  t= adminUserApi.getUserMap(users);
                userMap.set(t);
            });
        });
        users.removeIf(id -> {
            AdminUserRespDTO user = userMap.get().get(id);
            List<Long> existRelativeVirtualIds = new ArrayList<>();
            if (relativeMap.get("existRelativeVirtualIds") != null){
                existRelativeVirtualIds = (List<Long>) relativeMap.get("existRelativeVirtualIds");
            }
            return (user == null || !CommonStatusEnum.ENABLE.getStatus().equals(user.getStatus()) )&& !existRelativeVirtualIds.contains(id);
        });
    }

}
