package com.yaoan.module.econtract.convert.warningmonitor;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yaoan.module.econtract.controller.admin.warningmonitor.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningmonitor.WarningMonitorDO;

/**
 * 预警监控项配置表（new预警） Convert
 *
 * @author admin
 */
@Mapper
public interface WarningMonitorConvert {

    WarningMonitorConvert INSTANCE = Mappers.getMapper(WarningMonitorConvert.class);

    WarningMonitorDO convert(WarningMonitorCreateReqVO bean);

    WarningMonitorDO convert(WarningMonitorUpdateReqVO bean);

    WarningMonitorRespVO convert(WarningMonitorDO bean);


    PageResult<WarningMonitorRespVO> convertPage(PageResult<WarningMonitorDO> page);

    List<WarningMonitorExcelVO> convertList02(List<WarningMonitorDO> list);

    List<WarningMonitorRespVO> listDo2Resp(List<WarningMonitorDO> monitorDOList);
}
