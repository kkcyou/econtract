package com.yaoan.module.econtract.convert.workbenchmanage;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchCreateReqVO;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchExcelVO;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchRespVO;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchUpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.workbenchmanage.WorkbenchDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * 工作台管理 Convert
 *
 * @author lls
 */
@Mapper
public interface WorkbenchConvert {

    WorkbenchConvert INSTANCE = Mappers.getMapper(WorkbenchConvert.class);

    WorkbenchDO convert(WorkbenchCreateReqVO bean);

    WorkbenchDO convert(WorkbenchUpdateReqVO bean);

    WorkbenchRespVO convert(WorkbenchDO bean);

    List<WorkbenchRespVO> convertList(List<WorkbenchDO> list);

    PageResult<WorkbenchRespVO> convertPage(PageResult<WorkbenchDO> page);

    List<WorkbenchExcelVO> convertList02(List<WorkbenchDO> list);

}
