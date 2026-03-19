package com.yaoan.module.econtract.convert.contract;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contract.vo.ProjectPurchasingRespVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractProjectPurchasingDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectPurchasingConverter {
    ProjectPurchasingConverter INSTANCE = Mappers.getMapper(ProjectPurchasingConverter.class);

//    PageResult<ProjectPurchasingRespVO> convertPage(PageResult<ContractProjectPurchasingDO> page);
}
