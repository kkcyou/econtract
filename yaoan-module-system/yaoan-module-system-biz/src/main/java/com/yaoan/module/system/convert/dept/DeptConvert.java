package com.yaoan.module.system.convert.dept;

import com.yaoan.module.system.api.dept.dto.DeptRespDTO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptRespVO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptSimpleRespVO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptUpdateReqVO;
import com.yaoan.module.system.dal.dataobject.dept.CompanyDO;
import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DeptConvert {

    DeptConvert INSTANCE = Mappers.getMapper(DeptConvert.class);

    List<DeptRespVO> convertList(List<DeptDO> list);

    List<DeptSimpleRespVO> convertList02(List<DeptDO> list);

    DeptRespVO convert(DeptDO bean);

    DeptDO convert(DeptCreateReqVO bean);

    DeptDO convert(DeptUpdateReqVO bean);

    List<DeptRespDTO> convertList03(List<DeptDO> list);

    DeptRespDTO convert03(DeptDO bean);


    @Mapping(source = "id", target = "companyId")
    DeptDO company2Dept(CompanyDO companyDO);
}
