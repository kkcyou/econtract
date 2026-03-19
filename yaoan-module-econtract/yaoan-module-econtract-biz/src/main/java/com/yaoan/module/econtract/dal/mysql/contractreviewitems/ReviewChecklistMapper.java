package com.yaoan.module.econtract.dal.mysql.contractreviewitems;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewChecklistDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审查清单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ReviewChecklistMapper extends BaseMapperX<ReviewChecklistDO> {

    default PageResult<ReviewChecklistDO> selectPage(ReviewChecklistPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ReviewChecklistDO>()
                .likeIfPresent(ReviewChecklistDO::getReviewListName, reqVO.getReviewListName())
                .eqIfPresent(ReviewChecklistDO::getReviewListCode, reqVO.getReviewListCode())
                .eqIfPresent(ReviewChecklistDO::getReviewListType, reqVO.getReviewListType())
                .eqIfPresent(ReviewChecklistDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ReviewChecklistDO::getNotes, reqVO.getNotes())
                .betweenIfPresent(ReviewChecklistDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ReviewChecklistDO::getCreateTime));
    }

}