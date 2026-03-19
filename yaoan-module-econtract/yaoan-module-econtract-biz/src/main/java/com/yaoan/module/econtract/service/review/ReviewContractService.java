package com.yaoan.module.econtract.service.review;


import com.yaoan.module.econtract.controller.admin.review.vo.*;

public interface ReviewContractService {

    void create(ReviewContractCreateReqVO reviewContractCreateReqVO);

    ReviewContractRespVO selectByTypeId(ReviewContractReqVO reqVO);

    ReviewConfigLogRespVO getConfigLog(ReviewConfigLogReqVO reqVO);
}
