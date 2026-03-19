package com.yaoan.module.econtract.convert.risk;

import com.yaoan.module.econtract.controller.admin.contractrisk.vo.ContractRiskRespVO;
import com.yaoan.module.econtract.dal.dataobject.contractrisk.ContractRiskDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/25 18:37
 */
@Mapper
public interface ContractRiskConverter {
    ContractRiskConverter INSTANCE = Mappers.getMapper(ContractRiskConverter.class);

    ContractRiskRespVO do2Resp(ContractRiskDO contractRiskDO);
}
