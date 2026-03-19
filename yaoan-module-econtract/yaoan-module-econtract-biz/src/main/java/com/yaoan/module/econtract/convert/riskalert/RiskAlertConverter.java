package com.yaoan.module.econtract.convert.riskalert;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo.RiskAlertPageRespVO;
import com.yaoan.module.econtract.dal.dataobject.riskalert.RiskAlertDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/7 11:19
 */
@Mapper
public interface RiskAlertConverter {

    RiskAlertConverter INSTANCE = Mappers.getMapper(RiskAlertConverter.class);

    PageResult<RiskAlertPageRespVO> convert2Resp(PageResult<RiskAlertDO> pageResult);
}
