package com.yaoan.module.system.convert.econtractorg;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.system.api.user.dto.EcontractOrgDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yaoan.module.system.controller.admin.econtractorg.vo.*;
import com.yaoan.module.system.dal.dataobject.econtractorg.EcontractOrgDO;

/**
 * 电子合同单位信息 Convert
 *
 * @author admin
 */
@Mapper
public interface EcontractOrgConvert {

    EcontractOrgConvert INSTANCE = Mappers.getMapper(EcontractOrgConvert.class);

    EcontractOrgDO convert(EcontractOrgSaveReqVO bean);
    EcontractOrgDO convertDTO2DO(EcontractOrgDTO bean);
    EcontractOrgSaveReqVO convertDTO2SaveVO(EcontractOrgDTO bean);

    EcontractOrgRespVO convert(EcontractOrgDO bean);
    EcontractOrgDTO convertDTO(EcontractOrgDO bean);

    List<EcontractOrgRespVO> convertList(List<EcontractOrgDO> list);

    PageResult<EcontractOrgRespVO> convertPage(PageResult<EcontractOrgDO> page);

    List<EcontractOrgExcelVO> convertList02(List<EcontractOrgDO> list);

}
