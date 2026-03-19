package com.yaoan.module.econtract.service.reviewresult;

import java.util.*;
import javax.validation.*;
import com.yaoan.module.econtract.controller.admin.reviewresult.vo.*;
import com.yaoan.module.econtract.dal.dataobject.reviewresult.ReviewResultDO;
import com.yaoan.framework.common.pojo.PageResult;

/**
 * 智能审查结果 Service 接口
 *
 * @author admin
 */
public interface ReviewResultService {

    /**
     * 创建智能审查结果
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createReviewResult(@Valid ReviewResultCreateReqVO createReqVO);

    /**
     * 更新智能审查结果
     *
     * @param updateReqVO 更新信息
     */
    void updateReviewResult(@Valid ReviewResultUpdateReqVO updateReqVO);

    /**
     * 删除智能审查结果
     *
     * @param id 编号
     */
    void deleteReviewResult(String id);

    /**
     * 获得智能审查结果
     *
     * @param id 编号
     * @return 智能审查结果
     */
    ReviewResultDO getReviewResult(String id);

    /**
     * 获得智能审查结果列表
     *
     * @param ids 编号
     * @return 智能审查结果列表
     */
    List<ReviewResultDO> getReviewResultList(Collection<String> ids);

    /**
     * 获得智能审查结果分页
     *
     * @param pageReqVO 分页查询
     * @return 智能审查结果分页
     */
    PageResult<ReviewResultRespVO> getReviewResultPage(ReviewResultPageReqVO pageReqVO);

    /**
     * 获得智能审查结果列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 智能审查结果列表
     */
    List<ReviewResultDO> getReviewResultList(ReviewResultExportReqVO exportReqVO);

}
