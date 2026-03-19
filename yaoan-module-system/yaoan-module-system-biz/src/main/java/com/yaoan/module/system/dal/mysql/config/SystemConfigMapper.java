package com.yaoan.module.system.dal.mysql.config;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.system.dal.dataobject.config.SystemConfigDO;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/22 17:41
 */
@Mapper
public interface SystemConfigMapper extends BaseMapperX<SystemConfigDO> {

    default SystemConfigDO getPermissionForApproveScanAnnotations() {
        return selectOne(new LambdaQueryWrapperX<SystemConfigDO>()
                .eq(SystemConfigDO::getCKey, SystemConfigKeyEnums.APPROVER_PERMISSION_SCAN_ANNOTATION.getKey())
        );
    }

    default SystemConfigDO getPermissionForCopyScanAnnotations() {
        return selectOne(new LambdaQueryWrapperX<SystemConfigDO>()
                .eq(SystemConfigDO::getCKey, SystemConfigKeyEnums.CARBON_COPY_PERMISSION_SCAN_ANNOTATION.getKey())
        );
    }
}
