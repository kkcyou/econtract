package com.yaoan.module.econtract.convert.warningnoticecfg;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.controller.admin.warningcfg.vo.create.WarningNotice4CfgCreateReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.get.WarningNotice4CfgRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yaoan.module.econtract.controller.admin.warningnoticecfg.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningnoticecfg.WarningNoticeCfgDO;

/**
 * 预警通知配置表（new预警） Convert
 *
 * @author admin
 */
@Mapper
public interface WarningNoticeCfgConvert {

    WarningNoticeCfgConvert INSTANCE = Mappers.getMapper(WarningNoticeCfgConvert.class);

    WarningNoticeCfgDO convert(WarningNoticeCfgCreateReqVO bean);

    WarningNoticeCfgDO convert(WarningNoticeCfgUpdateReqVO bean);

    WarningNoticeCfgRespVO convert(WarningNoticeCfgDO bean);

    List<WarningNoticeCfgRespVO> convertList(List<WarningNoticeCfgDO> list);

    PageResult<WarningNoticeCfgRespVO> convertPage(PageResult<WarningNoticeCfgDO> page);

    List<WarningNoticeCfgExcelVO> convertList02(List<WarningNoticeCfgDO> list);

    WarningNoticeCfgDO req2Do(WarningNotice4CfgCreateReqVO noticeReqVO);

    List<WarningNotice4CfgRespVO> listDo2Resp(List<WarningNoticeCfgDO> noticeCfgDOList);
}
