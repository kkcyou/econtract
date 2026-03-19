package com.yaoan.module.econtract.dal.mysql.reviewitembasis;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.reviewitembasis.vo.ReviewItemBasisExportReqVO;
import com.yaoan.module.econtract.controller.admin.reviewitembasis.vo.ReviewItemBasisPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.reviewitembasis.ReviewItemBasisDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 合同审查规则依据 Mapper
 *
 * @author admin
 */
@Mapper
public interface ReviewItemBasisMapper extends BaseMapperX<ReviewItemBasisDO> {

    default PageResult<ReviewItemBasisDO> selectPage(ReviewItemBasisPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ReviewItemBasisDO>()
                .betweenIfPresent(ReviewItemBasisDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ReviewItemBasisDO::getId));
    }

    default List<ReviewItemBasisDO> selectList(ReviewItemBasisExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ReviewItemBasisDO>()
                .betweenIfPresent(ReviewItemBasisDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ReviewItemBasisDO::getId));
    }

}
