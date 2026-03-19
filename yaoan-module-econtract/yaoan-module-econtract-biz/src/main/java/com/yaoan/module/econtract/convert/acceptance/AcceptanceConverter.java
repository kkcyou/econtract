package com.yaoan.module.econtract.convert.acceptance;

import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.AcceptanceRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.out.AcceptanceRecordRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.query.AcceptancePlanRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.save.AcceptanceCreateReqVO;
import com.yaoan.module.econtract.dal.dataobject.acceptance.AcceptanceDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025/4/22 11:57
 */
@Mapper
public interface AcceptanceConverter {
    AcceptanceConverter INSTANCE = Mappers.getMapper(AcceptanceConverter.class);

    AcceptanceDO req2Do(AcceptanceCreateReqVO reqVO);

    List<AcceptancePlanRespVO> req2DoList(List<AcceptanceDO> acceptanceDOS);

    List<AcceptanceRecordRespVO> do2RespList(List<AcceptanceDO> acceptanceRespVOList);
}
