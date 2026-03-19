package com.yaoan.module.econtract.convert.warningparam;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yaoan.module.econtract.controller.admin.warningparam.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningparam.WarningParamDO;

/**
 * 预警消息模板参数(new预警) Convert
 *
 * @author admin
 */
@Mapper
public interface WarningParamConvert {

    WarningParamConvert INSTANCE = Mappers.getMapper(WarningParamConvert.class);

    WarningParamDO convert(WarningParamCreateReqVO bean);

    WarningParamDO convert(WarningParamUpdateReqVO bean);

    WarningParamRespVO convert(WarningParamDO bean);

    List<WarningParamRespVO> convertList(List<WarningParamDO> list);

    PageResult<WarningParamRespVO> convertPage(PageResult<WarningParamDO> page);

    List<WarningParamExcelVO> convertList02(List<WarningParamDO> list);

}
