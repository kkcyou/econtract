package com.yaoan.module.econtract.service.workbench.v2;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.workbench.v2.vo.ContractDataReqVO;
import com.yaoan.module.econtract.controller.admin.workbench.v2.vo.ContractDataRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.v2.vo.ContractPerformReqVO;

/**
 * @description:
 * @author: Pele
 * @date: 2025/2/11 10:22
 */
public interface WorkBenchV2Service {

    /**
     * 单位端_工作台_合同数据接口
     * 当前人起草的合同状态（11,13）
     * 当前人发起的用印、付款、收款的所有状态没到通过的所有合同
     *
     * @return {@link PageResult }<{@link ContractDataRespVO }>
     */
    PageResult<ContractDataRespVO> workbenchContractData( ContractDataReqVO vo);

    /**
     * 单位端_工作台_合同履约接口
     * 根据一级合同类型找到所有状态的合同
     *
     * @param vo
     * @return {@link CommonResult }<{@link PageResult }<{@link ContractDataRespVO }>>
     */
    PageResult<ContractDataRespVO> contractPerform(ContractPerformReqVO vo);
}
