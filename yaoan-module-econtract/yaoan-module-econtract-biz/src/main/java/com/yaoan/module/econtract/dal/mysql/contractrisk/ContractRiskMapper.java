package com.yaoan.module.econtract.dal.mysql.contractrisk;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contractrisk.vo.ContractRiskPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractrisk.ContractRiskDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 合同风险 Mapper
 *
 * @author lls
 */
@Mapper
public interface ContractRiskMapper extends BaseMapperX<ContractRiskDO> {

    default PageResult<ContractRiskDO> selectPage(ContractRiskPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ContractRiskDO>()
                .eqIfPresent(ContractRiskDO::getRiskType, reqVO.getRiskType())
                .eqIfPresent(ContractRiskDO::getHandleUser, reqVO.getHandleUser())
                .betweenIfPresent(ContractRiskDO::getHandleTime, reqVO.getHandleTime())
                .eqIfPresent(ContractRiskDO::getHandleResult, reqVO.getHandleResult())
                .eqIfPresent(ContractRiskDO::getHandleResultStatus, reqVO.getHandleResultStatus())
                .eqIfPresent(ContractRiskDO::getContractId,reqVO.getContractId())
                .eqIfPresent(ContractRiskDO::getStatus,reqVO.getStatus())
                .betweenIfPresent(ContractRiskDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(ContractRiskDO::getCreateTime));
    }

}