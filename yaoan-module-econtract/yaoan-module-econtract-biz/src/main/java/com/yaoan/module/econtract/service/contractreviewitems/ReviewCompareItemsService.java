package com.yaoan.module.econtract.service.contractreviewitems;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewcompareitems.ReviewCompareItemsPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewcompareitems.ReviewCompareItemsSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewCompareItemsDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 审查比对检测项 Service 接口
 *
 * @author 芋道源码
 */
public interface ReviewCompareItemsService {

    /**
     * 创建审查比对检测项
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createReviewCompareItems(@Valid ReviewCompareItemsSaveReqVO createReqVO);

    /**
     * 更新审查比对检测项
     *
     * @param updateReqVO 更新信息
     */
    void updateReviewCompareItems(@Valid ReviewCompareItemsSaveReqVO updateReqVO);

    /**
     * 删除审查比对检测项
     *
     * @param id 编号
     */
    void deleteReviewCompareItems(String id);

    /**
     * 获得审查比对检测项
     *
     * @param id 编号
     * @return 审查比对检测项
     */
    ReviewCompareItemsDO getReviewCompareItems(String id);

    /**
     * 获得审查比对检测项分页
     *
     * @param pageReqVO 分页查询
     * @return 审查比对检测项分页
     */
    PageResult<ReviewCompareItemsDO> getReviewCompareItemsPage(ReviewCompareItemsPageReqVO pageReqVO);

    List<ReviewCompareItemsDO> getAllReviewCompareItems();

}