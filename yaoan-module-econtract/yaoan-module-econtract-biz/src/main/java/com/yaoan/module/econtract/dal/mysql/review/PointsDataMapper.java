package com.yaoan.module.econtract.dal.mysql.review;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.review.vo.ReviewPointsDataReqVO;
import com.yaoan.module.econtract.dal.dataobject.review.PointsDataDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PointsDataMapper extends BaseMapperX<PointsDataDO> {

    default PageResult<PointsDataDO> getList(ReviewPointsDataReqVO reviewPointsDataReqVO){
        return selectPage(reviewPointsDataReqVO, new LambdaQueryWrapperX<PointsDataDO>()
                //根据父类id进行查询
                .eqIfPresent(PointsDataDO::getParentId, reviewPointsDataReqVO.getId())
                .orderByDesc(PointsDataDO::getId));
    };

}
