package com.yaoan.module.econtract.dal.mysql.contractPerformanceAcceptance;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.ContractPerformanceAcceptancePageReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.SimpleContractDO;
import com.yaoan.module.econtract.dal.dataobject.contractPerformanceAcceptance.ContractPerformanceAcceptanceDO;
import dm.jdbc.util.StringUtil;
import org.apache.ibatis.annotations.Mapper;

import static com.yaoan.framework.web.core.util.WebFrameworkUtils.getLoginUserId;


/**
 * 验收 Mapper
 *
 * @author lls
 */
@Mapper
public interface ContractPerformanceAcceptanceMapper extends BaseMapperX<ContractPerformanceAcceptanceDO> {

    default PageResult<ContractPerformanceAcceptanceDO> selectPage(ContractPerformanceAcceptancePageReqVO reqVO) {
        MPJLambdaWrapper<ContractPerformanceAcceptanceDO> mpjLambdaWrapper = new MPJLambdaWrapper();
//        lambdaQueryWrapperX.eqIfPresent(ContractPerformanceAcceptanceDO::getCode, reqVO.getCode())
//                .eqIfPresent(ContractPerformanceAcceptanceDO::getTitle, reqVO.getTitle())
//                .betweenIfPresent(ContractPerformanceAcceptanceDO::getAcceptanceStartTime, reqVO.getAcceptanceStartTime())
//                .betweenIfPresent(ContractPerformanceAcceptanceDO::getAcceptanceEndTime, reqVO.getAcceptanceEndTime())
//                .eqIfPresent(ContractPerformanceAcceptanceDO::getAcceptanceUser, reqVO.getAcceptanceUser())
//                .eqIfPresent(ContractPerformanceAcceptanceDO::getRemark, reqVO.getRemark())
        mpjLambdaWrapper.in(ContractPerformanceAcceptanceDO::getStatus, reqVO.getStatus());
//                .eqIfPresent(ContractPerformanceAcceptanceDO::getAcceptanceRemark, reqVO.getAcceptanceRemark())
//                .betweenIfPresent(ContractPerformanceAcceptanceDO::getCreateTime, reqVO.getCreateTime());
        mpjLambdaWrapper.selectAll(ContractPerformanceAcceptanceDO.class);
        mpjLambdaWrapper.leftJoin(SimpleContractDO.class,SimpleContractDO::getId,ContractPerformanceAcceptanceDO::getContractId);
        if(StringUtil.isNotEmpty(reqVO.getCode())){
            mpjLambdaWrapper.and(w -> w.like(SimpleContractDO::getCode, reqVO.getCode())
                    .or()
                    .like(SimpleContractDO::getName, reqVO.getCode()));
        }
        //我发起的
        Long userId = getLoginUserId();
        if(reqVO.getAcceptanceUserType()==1){
            mpjLambdaWrapper.eq(ContractPerformanceAcceptanceDO::getCreator,userId);
        }else if(reqVO.getAcceptanceUserType()==2){
            mpjLambdaWrapper.eq(ContractPerformanceAcceptanceDO::getAcceptanceUser,userId);
        }
        mpjLambdaWrapper.orderByDesc(ContractPerformanceAcceptanceDO::getUpdateTime);
        return selectPage(reqVO,mpjLambdaWrapper);
    }

}