package com.yaoan.module.econtract.service.review;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.review.vo.ReviewPointsDataReqVO;
import com.yaoan.module.econtract.controller.admin.review.vo.ReviewPointsDataRespVO;

public interface PointsDataService {


    PageResult<ReviewPointsDataRespVO> getPointsList(ReviewPointsDataReqVO reviewPointsDataReqVO);

}
