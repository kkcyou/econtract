package com.yaoan.module.econtract.api.bpm.performance.log;

import com.yaoan.module.econtract.api.bpm.performance.log.dto.ContractPerfLogSaveDTO;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/9 16:14
 */
public interface ContractPerfLogApi {

    void save(ContractPerfLogSaveDTO dto);
}
