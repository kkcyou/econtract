package com.yaoan.module.econtract.service.performance.log;

import com.yaoan.module.econtract.controller.admin.performance.log.vo.ContractPerfLogSaveReqVO;
import com.yaoan.module.econtract.convert.performance.log.ContractPerformanceLogConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractPerformanceLogDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractPerformanceLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/9 16:08
 */
@Service
public class ContractPerformanceLogServiceImpl implements ContractPerformanceLogService {
    @Resource
    private ContractPerformanceLogMapper contractPerformanceLogMapper;

    @Override
    public void save(ContractPerfLogSaveReqVO dto) {
        ContractPerformanceLogDO logDO = ContractPerformanceLogConverter.INSTANCE.req2D(dto);
        contractPerformanceLogMapper.insert(logDO);
    }
}
