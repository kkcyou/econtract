package com.yaoan.module.econtract.service.review;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.review.vo.*;

public interface ReviewService {
    PageResult<ReviewPageRespVO> getReviewList(ReviewPageReqVO vo);

    void create(ReviewCreateReqVO reviewCreateReqVO);

    ReviewRespVO selectById(ReviewReqVO reviewReqVO);

    void update(ReviewUpdateReqVO reviewUpdateReqVO);

    void deleteByIdList(ReviewIdListVO idListVO);
}
