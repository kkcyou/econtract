package com.yaoan.module.system.convert.systemuserrel;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.SystemuserRelCreateReqVO;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.SystemuserRelExcelVO;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.SystemuserRelRespVO;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.SystemuserRelUpdateReqVO;
import com.yaoan.module.system.dal.dataobject.systemuserrel.SystemuserRelDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


/**
 * 系统对接用户关系 Convert
 *
 * @author lls
 */
@Mapper
public interface SystemuserRelConvert {

    SystemuserRelConvert INSTANCE = Mappers.getMapper(SystemuserRelConvert.class);

    SystemuserRelDO convert(SystemuserRelCreateReqVO bean);

    SystemuserRelDO convert(SystemuserRelUpdateReqVO bean);

    SystemuserRelRespVO convert(SystemuserRelDO bean);

    List<SystemuserRelRespVO> convertList(List<SystemuserRelDO> list);

    PageResult<SystemuserRelRespVO> convertPage(PageResult<SystemuserRelDO> page);

    List<SystemuserRelExcelVO> convertList02(List<SystemuserRelDO> list);

}
