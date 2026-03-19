package com.yaoan.module.econtract.convert.roleworkbenchrel;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelCreateReqVO;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelExcelVO;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelRespVO;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelUpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.roleworkbenchrel.RoleWorkbenchRelDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * 角色工作台关联 Convert
 *
 * @author admin
 */
@Mapper
public interface RoleWorkbenchRelConvert {

    RoleWorkbenchRelConvert INSTANCE = Mappers.getMapper(RoleWorkbenchRelConvert.class);

    RoleWorkbenchRelDO convert(RoleWorkbenchRelCreateReqVO bean);

    RoleWorkbenchRelDO convert(RoleWorkbenchRelUpdateReqVO bean);

    RoleWorkbenchRelRespVO convert(RoleWorkbenchRelDO bean);

    List<RoleWorkbenchRelRespVO> convertList(List<RoleWorkbenchRelDO> list);

    PageResult<RoleWorkbenchRelRespVO> convertPage(PageResult<RoleWorkbenchRelDO> page);

    List<RoleWorkbenchRelExcelVO> convertList02(List<RoleWorkbenchRelDO> list);

}
