package com.yaoan.module.econtract.dal.mysql.contractreviewitems;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewcompareitems.ReviewCompareItemsPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewCompareItemsDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审查比对检测项 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ReviewCompareItemsMapper extends BaseMapperX<ReviewCompareItemsDO> {

    default PageResult<ReviewCompareItemsDO> selectPage(ReviewCompareItemsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ReviewCompareItemsDO>()
                .eqIfPresent(ReviewCompareItemsDO::getItemFirstLevel, reqVO.getItemFirstLevel())
                .eqIfPresent(ReviewCompareItemsDO::getItemSecondLevel, reqVO.getItemSecondLevel())
                .likeIfPresent(ReviewCompareItemsDO::getItemName, reqVO.getItemName())
                .betweenIfPresent(ReviewCompareItemsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ReviewCompareItemsDO::getId));
    }

}