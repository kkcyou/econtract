package com.yaoan.module.econtract.service.contractarchives;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.*;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;

import javax.validation.Valid;

/**
 * 合同档案 Service 接口,用于处理归档服务
 *
 * @author lls
 */
public interface ContractArchivesService {

    /**
     * 创建合同档案
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createContractArchives(@Valid ContractArchivesSaveReqVO createReqVO);

    /**
     * 更新合同档案
     *
     * @param updateReqVO 更新信息
     */
    void updateContractArchives(@Valid ContractArchivesSaveReqVO updateReqVO);

    /**
     * 删除合同档案
     *
     * @param id 编号
     */
    void deleteContractArchives(String id);

    /**
     * 获得合同档案
     *
     * @param vo 编号
     * @return 合同档案
     */
    ContractArchivesRespVO getContractArchives(ContractArchivesReqVO vo) throws Exception;

    /**
     * 获得合同档案分页
     *
     * @param pageReqVO 分页查询
     * @return 合同档案分页
     */
    PageResult<ContractArchivesRespVO> getContractArchivesPage(ContractArchivesPageReqVO pageReqVO);

    /**
     * 补充合同档案
     *
     * @param createReqVO 补充信息
     * @return 编号
     */
    String supplementContractArchives(@Valid ContractArchivesSupplementReqVO createReqVO);

    PageResult<ContractArchivesRespVO> archivePage(ContractArchivesPageReqVO pageReqVO);
}