package com.yaoan.module.econtract.service.review.lmpl;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.review.vo.ReviewPointsDataReqVO;
import com.yaoan.module.econtract.controller.admin.review.vo.ReviewPointsDataRespVO;
import com.yaoan.module.econtract.convert.review.ReviewConverter;
import com.yaoan.module.econtract.dal.dataobject.review.PointsDataDO;
import com.yaoan.module.econtract.dal.mysql.review.PointsDataMapper;
import com.yaoan.module.econtract.service.review.PointsDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
@Slf4j
public class PointsDataServiceImpl implements PointsDataService {
@Resource
private PointsDataMapper pointsDataMapper;
    @Override
    public PageResult<ReviewPointsDataRespVO> getPointsList(ReviewPointsDataReqVO reviewPointsDataReqVO) {
        PageResult<PointsDataDO> pageResult = pointsDataMapper.getList(reviewPointsDataReqVO);
        return ReviewConverter.INSTANCE.reviewPointsDataDOResp(pageResult);
    }


}
