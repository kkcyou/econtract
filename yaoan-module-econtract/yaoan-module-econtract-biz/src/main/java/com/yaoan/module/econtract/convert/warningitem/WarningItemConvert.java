package com.yaoan.module.econtract.convert.warningitem;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.controller.admin.warningcfg.vo.create.WarningItem4CfgCreateReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.get.WarningItem4CfgRespVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.page.WarningItemPageRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yaoan.module.econtract.controller.admin.warningitem.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningitem.WarningItemDO;

/**
 * 预警事项表（new预警） Convert
 *
 * @author admin
 */
@Mapper
public interface WarningItemConvert {

    WarningItemConvert INSTANCE = Mappers.getMapper(WarningItemConvert.class);

    WarningItemDO convert(WarningItemCreateReqVO bean);

    WarningItemDO convert(WarningItemUpdateReqVO bean);

    WarningItemRespVO convert(WarningItemDO bean);

    List<WarningItemRespVO> convertList(List<WarningItemDO> list);

    PageResult<WarningItemRespVO> convertPage(PageResult<WarningItemDO> page);

    List<WarningItemExcelVO> convertList02(List<WarningItemDO> list);

    List<WarningItemDO> convertListR2D(List<WarningItem4CfgCreateReqVO> itemReqVOList);

    WarningItemDO req2DO(WarningItem4CfgCreateReqVO itemReqVO);

    WarningItem4CfgRespVO do2Resp(WarningItemDO itemDO);

    List<WarningItem4CfgRespVO> listDo2Resp(List<WarningItemDO> itemDOList);

    List<WarningItemPageRespVO> listDo2Resp2(List<WarningItemDO> itemDOList);
}
