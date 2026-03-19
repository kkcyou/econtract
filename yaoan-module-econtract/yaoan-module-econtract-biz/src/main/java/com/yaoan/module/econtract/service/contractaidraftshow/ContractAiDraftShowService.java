package com.yaoan.module.econtract.service.contractaidraftshow;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowCreateReqVO;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowRespShortVO;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowUpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractaidraftshow.ContractAiDraftShowDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 合同模板推荐 Service 接口
 *
 * @author doujiale
 */
public interface ContractAiDraftShowService {

    /**
     * 创建合同模板推荐
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    ContractAiDraftShowRespShortVO createContractAiDraftShow(@Valid ContractAiDraftShowCreateReqVO createReqVO);

    /**
     * 更新合同模板推荐
     *
     * @param updateReqVO 更新信息
     */
    void updateContractAiDraftShow(@Valid ContractAiDraftShowUpdateReqVO updateReqVO);

    /**
     * 删除合同模板推荐
     *
     * @param id 编号
     */
    void deleteContractAiDraftShow(Long id);

    /**
     * 获得合同模板推荐
     *
     * @param id 编号
     * @return 合同模板推荐
     */
    ContractAiDraftShowDO getContractAiDraftShow(Long id);
}
