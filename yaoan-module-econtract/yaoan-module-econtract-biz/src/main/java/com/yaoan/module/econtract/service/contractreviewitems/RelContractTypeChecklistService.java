package com.yaoan.module.econtract.service.contractreviewitems;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relcontracttypechecklist.RelContractTypeChecklistPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relcontracttypechecklist.RelContractTypeChecklistSaveReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistRespVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.RelContractTypeChecklistDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 合同类型-审查清单关联 Service 接口
 *
 * @author 芋道源码
 */
public interface RelContractTypeChecklistService {

    /**
     * 创建合同类型-审查清单关联
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createRelContractTypeChecklist(@Valid RelContractTypeChecklistSaveReqVO createReqVO);

    /**
     * 更新合同类型-审查清单关联
     *
     * @param updateReqVO 更新信息
     */
    void updateRelContractTypeChecklist(@Valid RelContractTypeChecklistSaveReqVO updateReqVO);

    /**
     * 删除合同类型-审查清单关联
     *
     * @param id 编号
     */
    void deleteRelContractTypeChecklist(String id);

    /**
     * 获得合同类型-审查清单关联
     *
     * @param id 编号
     * @return 合同类型-审查清单关联
     */
    List<ReviewChecklistRespVO> getRelContractTypeChecklist(String contractType,  String projectCategoryCode);

    /**
     * 获得合同类型-审查清单关联分页
     *
     * @param pageReqVO 分页查询
     * @return 合同类型-审查清单关联分页
     */
    PageResult<RelContractTypeChecklistDO> getRelContractTypeChecklistPage(RelContractTypeChecklistPageReqVO pageReqVO);

}