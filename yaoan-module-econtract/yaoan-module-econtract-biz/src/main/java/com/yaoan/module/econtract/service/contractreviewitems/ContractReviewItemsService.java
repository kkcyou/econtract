package com.yaoan.module.econtract.service.contractreviewitems;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.*;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ContractReviewItemsDO;

import java.util.*;
import javax.validation.*;
/**
 * 合同审查规则 Service 接口
 *
 * @author admin
 */
public interface ContractReviewItemsService {

    /**
     * 创建合同审查规则
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createContractReviewItems(@Valid ContractReviewItemsBaseVO createReqVO);

    /**
     * 更新合同审查规则
     *
     * @param updateReqVO 更新信息
     */
    void updateContractReviewItems(@Valid ContractReviewItemsBaseVO updateReqVO);

    /**
     * 删除合同审查规则
     *
     * @param id 编号
     */
    void deleteContractReviewItems(String id);

    /**
     * 获得合同审查规则
     *
     * @param id 编号
     * @return 合同审查规则
     */
    ContractReviewItemsBaseVO getContractReviewItems(String id);

    /**
     * 获得合同审查规则列表
     *
     * @param vo 列表查询
     * @return 合同审查规则列表
     */
    List<ClauseGroupRespVO> getContractReviewItemsList(ContractReviewItemsListReqVO vo);

    /**
     * 获得合同审查规则分页
     *
     * @param pageReqVO 分页查询
     * @return 合同审查规则分页
     */
    PageResult<ContractReviewItemsRespVO> getContractReviewItemsPage(ContractReviewItemsPageReqVO pageReqVO);

    /**
     * 获得合同审查规则列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 合同审查规则列表
     */
    List<ContractReviewItemsDO> getContractReviewItemsExcelList(ContractReviewItemsExportReqVO exportReqVO);

    void updateReviewStatus(ContractReviewItemsUpdateStatusReqVO updateReqVO);

    PageResult<ContractReviewItemsRespVO> getContractReviewItemsPageV2(ContractReviewItemsPageReqVO pageVO);
}
