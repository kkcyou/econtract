package com.yaoan.module.econtract.dal.mysql.category.backups;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.category.backups.ContractInfoBackupsDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 合同草拟备份数据表mapper
 */

@Mapper
public interface ContractInfoBackupsMapper extends BaseMapperX<ContractInfoBackupsDO> {
}
