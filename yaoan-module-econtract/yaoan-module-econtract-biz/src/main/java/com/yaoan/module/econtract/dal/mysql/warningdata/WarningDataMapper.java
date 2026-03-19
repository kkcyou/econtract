package com.yaoan.module.econtract.dal.mysql.warningdata;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.warningrisk.vo.WarningRiskPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.warningdata.WarningDataDO;
import com.yaoan.module.econtract.dal.dataobject.warningruleconfig.WarningRuleConfigDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * 预警结果 Mapper
 *
 * @author lls
 */
@Mapper
public interface WarningDataMapper extends BaseMapperX<WarningDataDO> {



}