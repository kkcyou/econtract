package com.yaoan.module.econtract.convert.review;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.review.vo.*;
import com.yaoan.module.econtract.dal.dataobject.review.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReviewConverter {
    ReviewConverter INSTANCE = Mappers.getMapper(ReviewConverter.class);

    List<ReviewPointsVO> ReviewPointsVoToEntity(List<ReviewPointsDO> bean);

    List<ReviewContractVO> ReviewVoContractTypeToEntity(List<ReviewContractDO> bean);

    ReviewContractDO reviewContractCreateToEntity(ReviewContractCreateReqVO bean);

    ReviewContractRespVO reviewContractToEntity(ReviewContractDO bean);

    ReviewConfigLogRespVO reviewConfigLogToEntity(ReviewConfigLogDO bean);

    ReviewRespVO reviewToEntity(ReviewListDO bean);

    PageResult<ReviewPageRespVO> reviewListDOResp(PageResult<ReviewListDO> page);


    List<ReviewPointsTypeRespVO> reviewPointsTypeDOResp(List<PointsTypeDO> page);

    PageResult<ReviewPointsDataRespVO> reviewPointsDataDOResp(PageResult<PointsDataDO> page);


}
