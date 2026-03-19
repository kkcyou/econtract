package com.yaoan.module.econtract.dal.mysql.reviewresult;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.reviewresult.ReviewResultDO;
import org.apache.ibatis.annotations.Mapper;
import com.yaoan.module.econtract.controller.admin.reviewresult.vo.*;

/**
 * 智能审查结果 Mapper
 *
 * @author admin
 */
@Mapper
public interface ReviewResultMapper extends BaseMapperX<ReviewResultDO> {

    default PageResult<ReviewResultDO> selectPage(ReviewResultPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ReviewResultDO>()
                .eqIfPresent(ReviewResultDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ReviewResultDO::getResult, reqVO.getResult())
                .eqIfPresent(ReviewResultDO::getTitle, reqVO.getTitle())
                .eqIfPresent(ReviewResultDO::getRiskLevel, reqVO.getRiskLevel())
                .eqIfPresent(ReviewResultDO::getRiskWarning, reqVO.getRiskWarning())
                .eqIfPresent(ReviewResultDO::getVersion, reqVO.getVersion())
                .betweenIfPresent(ReviewResultDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ReviewResultDO::getId));
    }

    default List<ReviewResultDO> selectList(ReviewResultExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ReviewResultDO>()
                .eqIfPresent(ReviewResultDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ReviewResultDO::getResult, reqVO.getResult())
                .eqIfPresent(ReviewResultDO::getTitle, reqVO.getTitle())
                .eqIfPresent(ReviewResultDO::getRiskLevel, reqVO.getRiskLevel())
                .eqIfPresent(ReviewResultDO::getRiskWarning, reqVO.getRiskWarning())
                .eqIfPresent(ReviewResultDO::getVersion, reqVO.getVersion())
                .betweenIfPresent(ReviewResultDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ReviewResultDO::getId));
    }

}
