package com.yaoan.module.econtract.api.performance.log;

import com.yaoan.module.econtract.api.bpm.performance.log.ContractPerfLogApi;
import com.yaoan.module.econtract.api.bpm.performance.log.dto.ContractPerfLogSaveDTO;
import com.yaoan.module.econtract.controller.admin.performance.log.vo.ContractPerfLogSaveReqVO;
import com.yaoan.module.econtract.convert.performance.log.ContractPerformanceLogConverter;
import com.yaoan.module.econtract.service.performance.log.ContractPerformanceLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/9 16:14
 */
@Service
public class ContractPerfLogApiImpl implements ContractPerfLogApi {
    @Resource
    private ContractPerformanceLogService contractPerformanceLogService;


    @Override
    public void save(ContractPerfLogSaveDTO dto) {
        ContractPerfLogSaveReqVO reqVO = ContractPerformanceLogConverter.INSTANCE.dto2Req(dto);
        contractPerformanceLogService.save(reqVO);
    }
}
