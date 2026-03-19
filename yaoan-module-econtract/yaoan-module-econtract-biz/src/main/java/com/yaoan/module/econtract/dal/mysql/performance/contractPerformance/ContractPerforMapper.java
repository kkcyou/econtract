package com.yaoan.module.econtract.dal.mysql.performance.contractPerformance;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerfPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * mapper接口
 *
 * @author doujl
 * @since 2023-07-24
 */
@Mapper
public interface ContractPerforMapper extends BaseMapperX<ContractPerformanceDO> {
    default PageResult<ContractPerformanceDO> queryAllContractPerf(ContractPerfPageReqVO contractPerfPageReqVO) {
        LambdaQueryWrapperX<ContractPerformanceDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.orderByDesc(ContractPerformanceDO::getCreateTime);
        if(StringUtils.isNotEmpty(contractPerfPageReqVO.getSearchText())){
            queryWrapperX //searchText模糊匹配合同编码，合同名称
                    .like(ContractPerformanceDO::getContractCode, contractPerfPageReqVO.getSearchText()).or()
                    .like(ContractPerformanceDO::getContractName, contractPerfPageReqVO.getSearchText());
        }
        queryWrapperX  //合同状态
                .eqIfPresent(ContractPerformanceDO::getContractStatus, contractPerfPageReqVO.getContractStatus())
                //合同类型
                .eqIfPresent(ContractPerformanceDO::getContractTypeId, contractPerfPageReqVO.getContractTypeId())
                //签署完成时间
                .betweenIfPresent(ContractPerformanceDO::getSignFinishTime, contractPerfPageReqVO.getStartSignTime()==null?null:contractPerfPageReqVO.getStartSignTime().toDateStr(), contractPerfPageReqVO.getEndSignTime()==null?null:contractPerfPageReqVO.getEndSignTime().toDateStr())
                //履约最终截止时间
                .betweenIfPresent(ContractPerformanceDO::getPerfTime, contractPerfPageReqVO.getStartFinishTime()==null?null:contractPerfPageReqVO.getStartFinishTime().toDateStr(), contractPerfPageReqVO.getEndFinishTime()==null?null:contractPerfPageReqVO.getEndFinishTime().toDateStr());
        return selectPage(contractPerfPageReqVO,queryWrapperX);
    }
    default List<ContractPerformanceDO> selectListByIds(List<String> contractPerfIds) {
  return  selectList(new LambdaQueryWrapperX<ContractPerformanceDO>().inIfPresent(ContractPerformanceDO::getId, contractPerfIds).select(ContractPerformanceDO::getId,ContractPerformanceDO::getContractName,ContractPerformanceDO::getContractCode));
    }

    default ContractPerformanceDO selectContractPerforById(String contractPerfId) {
        return  selectOne(new LambdaQueryWrapperX<ContractPerformanceDO>().eqIfPresent(ContractPerformanceDO::getId, contractPerfId).select(ContractPerformanceDO::getId,ContractPerformanceDO::getContractName,ContractPerformanceDO::getContractCode,ContractPerformanceDO::getContractStatus,ContractPerformanceDO::getPerfTime));
    }

    default ContractPerformanceDO selectContractPerformance(String contractId) {
        return  selectOne(new LambdaQueryWrapperX<ContractPerformanceDO>()
                .eqIfPresent(ContractPerformanceDO::getContractId, contractId)
                .eqIfPresent(ContractPerformanceDO::getCreator, WebFrameworkUtils.getLoginUserId())
                .select(ContractPerformanceDO::getId));
    }


}
