package com.yaoan.module.econtract.convert.warningcfg;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.controller.admin.warningcfg.vo.WarningCfgCreateReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.WarningCfgExcelVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.WarningCfgRespVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.WarningCfgUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.create.WarningCfgBase4CfgCreateReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.get.WarningCfgBase4CfgRespVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.page.WarningPageRespVO;
import com.yaoan.module.econtract.dal.dataobject.warningcfg.WarningCfgDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 预警检查配置表(new预警) Convert
 *
 * @author admin
 */
@Mapper
public interface WarningCfgConvert {

    WarningCfgConvert INSTANCE = Mappers.getMapper(WarningCfgConvert.class);

    WarningCfgDO convert(WarningCfgCreateReqVO bean);

    WarningCfgDO convert(WarningCfgUpdateReqVO bean);

    WarningCfgRespVO convert(WarningCfgDO bean);

    List<WarningCfgRespVO> convertList(List<WarningCfgDO> list);

    PageResult<WarningCfgRespVO> convertPage(PageResult<WarningCfgDO> page);

    List<WarningCfgExcelVO> convertList02(List<WarningCfgDO> list);

    WarningCfgDO convertReq2Do(WarningCfgBase4CfgCreateReqVO cfgBase4CfgCreateReqVO);

    WarningCfgBase4CfgRespVO do2Resp(WarningCfgDO cfgDO);

    PageResult<WarningPageRespVO> pageDo2Resp(PageResult<WarningCfgDO> doPage);
}
