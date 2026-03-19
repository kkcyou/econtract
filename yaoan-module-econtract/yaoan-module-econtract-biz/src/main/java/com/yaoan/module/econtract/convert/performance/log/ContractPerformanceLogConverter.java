package com.yaoan.module.econtract.convert.performance.log;

import com.yaoan.module.econtract.api.bpm.performance.log.dto.ContractPerfLogSaveDTO;
import com.yaoan.module.econtract.controller.admin.performance.log.vo.ContractPerfLogSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractPerformanceLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/9 16:19
 */
@Mapper
public interface ContractPerformanceLogConverter {
    ContractPerformanceLogConverter INSTANCE = Mappers.getMapper(ContractPerformanceLogConverter.class);


    ContractPerfLogSaveReqVO dto2Req(ContractPerfLogSaveDTO dto);

    ContractPerformanceLogDO req2D(ContractPerfLogSaveReqVO dto);
}
