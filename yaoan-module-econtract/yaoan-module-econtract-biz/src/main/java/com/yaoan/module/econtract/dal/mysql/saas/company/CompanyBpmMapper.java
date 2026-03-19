package com.yaoan.module.econtract.dal.mysql.saas.company;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.bpm.saas.company.vo.BpmCompanyPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.saas.company.CompanyBpmDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-28 16:51
 */
@Mapper
public interface CompanyBpmMapper extends BaseMapperX<CompanyBpmDO> {
    default PageResult<CompanyBpmDO> selectPage(BpmCompanyPageReqVO reqVO) {
        LambdaQueryWrapperX<CompanyBpmDO> wrapperX = new LambdaQueryWrapperX<CompanyBpmDO>()
                .likeIfPresent(CompanyBpmDO::getSubmitterName, reqVO.getSubmitterName())
                .betweenIfPresent(CompanyBpmDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(CompanyBpmDO::getMobile, reqVO.getMobile())
                .eqIfPresent(CompanyBpmDO::getInviteMethod, reqVO.getInviteMethod())
                .eqIfPresent(CompanyBpmDO::getRealName, reqVO.getRealName())
                .likeIfPresent(CompanyBpmDO::getUserIdCard, reqVO.getUserIdCard())
                .inIfPresent(CompanyBpmDO::getProcessInstanceId, reqVO.getInstanceIdList())
                .eqIfPresent(CompanyBpmDO::getResult,reqVO.getTaskResult())
                .orderByDesc(CompanyBpmDO::getUpdateTime);

        return selectPage(reqVO, wrapperX);

    }
}
