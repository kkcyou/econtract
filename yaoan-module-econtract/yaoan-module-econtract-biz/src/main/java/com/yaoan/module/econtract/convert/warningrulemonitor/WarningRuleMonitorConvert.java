package com.yaoan.module.econtract.convert.warningrulemonitor;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yaoan.module.econtract.controller.admin.warningrulemonitor.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningrulemonitor.WarningRuleMonitorRelDO;

/**
 * 预警规则与监控项关联关系表（new预警） Convert
 *
 * @author admin
 */
@Mapper
public interface WarningRuleMonitorConvert {

    WarningRuleMonitorConvert INSTANCE = Mappers.getMapper(WarningRuleMonitorConvert.class);

    WarningRuleMonitorRelDO convert(WarningRuleMonitorCreateReqVO bean);

    WarningRuleMonitorRelDO convert(WarningRuleMonitorUpdateReqVO bean);

    WarningRuleMonitorRespVO convert(WarningRuleMonitorRelDO bean);

    List<WarningRuleMonitorRespVO> convertList(List<WarningRuleMonitorRelDO> list);

    PageResult<WarningRuleMonitorRespVO> convertPage(PageResult<WarningRuleMonitorRelDO> page);

    List<WarningRuleMonitorExcelVO> convertList02(List<WarningRuleMonitorRelDO> list);

}
