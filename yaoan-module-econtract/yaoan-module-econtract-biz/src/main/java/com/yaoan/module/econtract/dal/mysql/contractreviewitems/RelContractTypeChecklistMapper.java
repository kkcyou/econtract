package com.yaoan.module.econtract.dal.mysql.contractreviewitems;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relcontracttypechecklist.RelContractTypeChecklistPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.RelContractTypeChecklistDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 合同类型-审查清单关联 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface RelContractTypeChecklistMapper extends BaseMapperX<RelContractTypeChecklistDO> {

    default PageResult<RelContractTypeChecklistDO> selectPage(RelContractTypeChecklistPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RelContractTypeChecklistDO>()
                .eqIfPresent(RelContractTypeChecklistDO::getContractType, reqVO.getContractType())
                .eqIfPresent(RelContractTypeChecklistDO::getReviewListId, reqVO.getReviewListId())
                .betweenIfPresent(RelContractTypeChecklistDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RelContractTypeChecklistDO::getId));
    }

}