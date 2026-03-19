package com.yaoan.module.econtract.service.performance.log;

import com.yaoan.module.econtract.api.bpm.performance.log.dto.ContractPerfLogSaveDTO;
import com.yaoan.module.econtract.controller.admin.performance.log.vo.ContractPerfLogSaveReqVO;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/9 16:08
 */
public interface ContractPerformanceLogService {

    /**
     * 保存日志
     */
    void save(ContractPerfLogSaveReqVO dto);
}
