package com.yaoan.module.econtract.service.contractaidraftrecord;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.*;
import com.yaoan.module.econtract.controller.admin.contracttype.vo.ContractTypeSelectReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractaidraftrecord.ContractAiDraftRecordDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 合同智能起草记录 Service 接口
 *
 * @author doujiale
 */
public interface ContractAiDraftRecordService {

    /**
     * 创建合同智能起草记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createContractAiDraftRecord(@Valid ContractAiDraftRecordCreateReqVO createReqVO);

    /**
     * 更新合同智能起草记录
     *
     * @param updateReqVO 更新信息
     */
    void updateContractAiDraftRecord(@Valid ContractAiDraftRecordUpdateReqVO updateReqVO);

    /**
     * 删除合同智能起草记录
     *
     * @param id 编号
     */
    void deleteContractAiDraftRecord(Long id);

    /**
     * 获得合同智能起草记录
     *
     * @param id 编号
     * @return 合同智能起草记录
     */
    ContractAiDraftRecordDO getContractAiDraftRecord(Long id);

    /**
     * 获得合同智能起草记录列表
     *
     * @param ids 编号
     * @return 合同智能起草记录列表
     */
    List<ContractAiDraftRecordDO> getContractAiDraftRecordList(Collection<Long> ids);

    /**
     * 获得合同智能起草记录分页
     *
     * @param pageReqVO 分页查询
     * @return 合同智能起草记录分页
     */
    PageResult<ContractAiDraftRecordDO> getContractAiDraftRecordPage(ContractAiDraftRecordPageReqVO pageReqVO);


    List<ContractType> selectContractTypeList(ContractTypeSelectReqVO vo);

    List<AITemplateInfo> selectModelListByContractTypeId(String id);
}
