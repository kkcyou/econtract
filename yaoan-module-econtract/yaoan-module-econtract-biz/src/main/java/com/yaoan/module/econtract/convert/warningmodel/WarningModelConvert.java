package com.yaoan.module.econtract.convert.warningmodel;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yaoan.module.econtract.controller.admin.warningmodel.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningmodel.WarningModelDO;

/**
 * 预警模块来源（new预警） Convert
 *
 * @author admin
 */
@Mapper
public interface WarningModelConvert {

    WarningModelConvert INSTANCE = Mappers.getMapper(WarningModelConvert.class);

    WarningModelDO convert(WarningModelCreateReqVO bean);

    WarningModelDO convert(WarningModelUpdateReqVO bean);

    WarningModelRespVO convert(WarningModelDO bean);

    List<WarningModelRespVO> convertList(List<WarningModelDO> list);

    PageResult<WarningModelRespVO> convertPage(PageResult<WarningModelDO> page);

    List<WarningModelExcelVO> convertList02(List<WarningModelDO> list);

}
