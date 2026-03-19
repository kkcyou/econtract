package com.yaoan.module.econtract.service.contractreviewitems;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistRespVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewChecklistDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 审查清单 Service 接口
 *
 * @author 芋道源码
 */
public interface ReviewChecklistService {

    /**
     * 创建审查清单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createReviewChecklist(@Valid ReviewChecklistSaveReqVO createReqVO);

    /**
     * 更新审查清单
     *
     * @param updateReqVO 更新信息
     */
    void updateReviewChecklist(@Valid ReviewChecklistSaveReqVO updateReqVO);

    /**
     * 删除审查清单
     *
     * @param id 编号
     */
    void deleteReviewChecklist(String id);

    /**
     * 获得审查清单
     *
     * @param id 编号
     * @return 审查清单
     */
    ReviewChecklistRespVO getReviewChecklist(String id);

    /**
     * 获得审查清单分页
     *
     * @param pageReqVO 分页查询
     * @return 审查清单分页
     */
    PageResult<ReviewChecklistDO> getReviewChecklistPage(ReviewChecklistPageReqVO pageReqVO);

    List<ReviewChecklistRespVO> getReviewChecklistList();

    void updateReviewChecklistStatus(@Valid ReviewChecklistSaveReqVO updateReqVO);
}