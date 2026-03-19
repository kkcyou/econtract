package com.yaoan.module.econtract.convert.performance.contractPerformance;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerfReqVO;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerformanceRespVO;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Mapper(componentModel = "spring")
public interface ContractPerfConverter {
    ContractPerfConverter INSTANCE = Mappers.getMapper(ContractPerfConverter.class);
    PageResult<ContractPerformanceRespVO> toVO(PageResult<ContractPerformanceDO> contractPerformanceDO);
    ContractPerformanceDO todo(ContractPerfReqVO c);
}


