package com.yaoan.module.econtract.service.contractrisk;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractrisk.vo.ContractRiskPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractrisk.vo.ContractRiskRespVO;
import com.yaoan.module.econtract.controller.admin.contractrisk.vo.ContractRiskSaveReqVO;
import com.yaoan.module.econtract.controller.admin.contractrisk.vo.ContractRiskUpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractrisk.ContractRiskDO;

import javax.validation.Valid;

/**
 * 合同风险 Service 接口
 *
 * @author lls
 */
public interface ContractRiskService {

    /**
     * 创建合同风险
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createContractRisk(@Valid ContractRiskSaveReqVO createReqVO);

    /**
     * 更新合同风险
     *
     * @param updateReqVO 更新信息
     */
    void updateContractRisk(@Valid ContractRiskUpdateReqVO updateReqVO);

    /**
     * 更新合同风险
     *
     * @param updateReqVO 更新信息
     */
    void updateContractRisk4Handle(@Valid ContractRiskUpdateReqVO updateReqVO);

    /**
     * 删除合同风险
     *
     * @param id 编号
     */
    void deleteContractRisk(String id);

    /**
     * 获得合同风险
     *
     * @param id 编号
     * @return 合同风险
     */
    ContractRiskRespVO getContractRisk(String id);

    /**
     * 获得合同风险分页
     *
     * @param pageReqVO 分页查询
     * @return 合同风险分页
     */
    PageResult<ContractRiskDO> getContractRiskPage(ContractRiskPageReqVO pageReqVO);

    /**
     * 关闭履约
     */
    void closePerformance(String id);

    /**
     * 校验完成履约
     */
    void check4completePerformance(String id);

    /**
     * 完成履约
     */
    void completePerformance(String id);

    String ignoreRisk(String id);
}