package com.yaoan.module.econtract.service.contractPerformanceAcceptance;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.AcceptanceApplyReqVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.ContractPerformanceAcceptancePageReqVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.ContractPerformanceAcceptanceRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.ContractPerformanceAcceptanceSaveReqVO;

import javax.validation.Valid;


/**
 * 验收 Service 接口
 *
 * @author lls
 */
public interface ContractPerformanceAcceptanceService {

    /**
     * 创建验收
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createContractPerformanceAcceptance(@Valid ContractPerformanceAcceptanceSaveReqVO createReqVO);

    /**
     * 更新验收
     *
     * @param updateReqVO 更新信息
     */
    void updateContractPerformanceAcceptance(@Valid ContractPerformanceAcceptanceSaveReqVO updateReqVO);

    /**
     * 删除验收
     *
     * @param id 编号
     */
    void deleteContractPerformanceAcceptance(String id);

    /**
     * 获得验收
     *
     * @param id 编号
     * @return 验收
     */
    ContractPerformanceAcceptanceRespVO getContractPerformanceAcceptance(String id);
    
    /**
     * 获得验收分页
     *
     * @param pageReqVO 分页查询
     * @return 验收分页
     */
    PageResult<ContractPerformanceAcceptanceRespVO> getContractPerformanceAcceptancePage(ContractPerformanceAcceptancePageReqVO pageReqVO);


    String acceptancePerformance (@Valid AcceptanceApplyReqVO createReqVO);
}