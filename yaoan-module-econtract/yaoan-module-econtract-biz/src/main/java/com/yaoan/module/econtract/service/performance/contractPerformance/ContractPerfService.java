package com.yaoan.module.econtract.service.performance.contractPerformance;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerfPageReqVO;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerfReqVO;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerformanceRespVO;
import com.yaoan.module.econtract.controller.admin.performance.v2.vo.*;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
public interface ContractPerfService {
    PageResult<ContractPerformanceRespVO> queryAllContractPerf(ContractPerfPageReqVO contractPerfPageReqVO);

    String createContractPerf(ContractPerfReqVO contractPerfReqVO);

    String setContractStatusName(Integer status);

    BigContractPerformV2RespVO list(ContractPerformV2ReqVO reqVO);

    /**
     * 新增履约计划
     */
    String save(PerformV2SaveReqVO reqVO);

    PerformQueryRespVO queryById(String id);
}
