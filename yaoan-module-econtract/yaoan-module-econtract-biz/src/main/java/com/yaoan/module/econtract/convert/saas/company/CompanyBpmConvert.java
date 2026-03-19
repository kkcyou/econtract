package com.yaoan.module.econtract.convert.saas.company;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.bpm.company.dto.BpmCompanyDTO;
import com.yaoan.module.econtract.controller.admin.bpm.saas.company.vo.BpmCompanyPageRespVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.saas.company.CompanyBpmDO;
import com.yaoan.module.system.api.dept.dto.CompanyRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-28 16:56
 */
@Mapper
public interface CompanyBpmConvert {
    CompanyBpmConvert INSTANCE = Mappers.getMapper(CompanyBpmConvert.class);

    @Mapping(source = "creditCode", target = "companyCreditNo")
    @Mapping(source = "name", target = "companyName")
    @Mapping(target = "id", ignore = true)
    CompanyBpmDO dto2Do(CompanyRespDTO dto);


    BpmCompanyDTO do2Dto(CompanyBpmDO companyBpmDO);

    PageResult<BpmCompanyPageRespVO> pageDo2Resp(PageResult<CompanyBpmDO> bpmDOPageResult);

    List<BpmCompanyPageRespVO> listDo2Resp(List<CompanyBpmDO> bpmDOPageResult);

    BpmCompanyPageRespVO do2Resp(CompanyBpmDO bean);

}
