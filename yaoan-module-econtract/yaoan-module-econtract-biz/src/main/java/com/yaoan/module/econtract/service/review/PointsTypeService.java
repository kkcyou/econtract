package com.yaoan.module.econtract.service.review;

import com.yaoan.module.econtract.controller.admin.review.vo.*;

import java.util.List;

public interface PointsTypeService {


    List<ReviewPointsTypeRespVO> getPointsList(ReviewPointsTypeReqVO reviewPointsTypeReqVO);
}
