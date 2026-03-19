package com.yaoan.module.econtract.dal.mysql.bpm.contract;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BpmContractPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 合同审批流程申请表 Mapper 接口
 * </p>
 *
 * @author doujiale
 * @since 2023-10-10
 */
@Mapper
public interface BpmContractMapper extends BaseMapperX<BpmContract> {

    default PageResult<BpmContract> selectPage(BpmContractPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BpmContract>()
                .orderByDesc(BpmContract::getCreateTime)
                .eqIfPresent(BpmContract::getResult, reqVO.getResult())
                .eqIfPresent(BpmContract::getContractType, reqVO.getContractType())
                .likeIfPresent(BpmContract::getContractName, reqVO.getSearchText())
                .betweenIfPresent(BpmContract::getCreateTime, reqVO.getCreateTime())
                .inIfPresent(BpmContract::getProcessInstanceId, reqVO.getProcessInstanceIds()));
    }
}
