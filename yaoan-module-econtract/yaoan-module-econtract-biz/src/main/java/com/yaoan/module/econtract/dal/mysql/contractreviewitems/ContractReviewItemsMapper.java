package com.yaoan.module.econtract.dal.mysql.contractreviewitems;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsExportReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ContractReviewItemsDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 合同审查规则 Mapper
 *
 * @author admin
 */
@Mapper
public interface ContractReviewItemsMapper extends BaseMapperX<ContractReviewItemsDO> {

    default PageResult<ContractReviewItemsDO> selectPage(ContractReviewItemsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ContractReviewItemsDO>()
                .likeIfPresent(ContractReviewItemsDO::getReviewContent, reqVO.getReviewContent())
                .inIfPresent(ContractReviewItemsDO::getTermId, reqVO.getTermId())
                .eqIfPresent(ContractReviewItemsDO::getRiskLevel, reqVO.getRiskLevel())
                .inIfPresent(ContractReviewItemsDO::getId, reqVO.getRuleIds())
                .orderByDesc(ContractReviewItemsDO::getCreateTime));
    }

    default List<ContractReviewItemsDO> selectList(ContractReviewItemsExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ContractReviewItemsDO>()
                .likeIfPresent(ContractReviewItemsDO::getReviewContent, reqVO.getReviewContent())
                .eqIfPresent(ContractReviewItemsDO::getRiskParty, reqVO.getRiskParty())
                .eqIfPresent(ContractReviewItemsDO::getRiskLevel, reqVO.getRiskLevel())
                .orderByDesc(ContractReviewItemsDO::getCreateTime));
    }

}
