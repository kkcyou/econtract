package com.yaoan.module.econtract.dal.mysql.bpm.performance.suspend;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.bpm.performance.vo.PerformancePageReqVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.dataobject.bpm.performance.suspend.BpmPerformance;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 履约中止申请表 Mapper 接口`
 * </p>
 *
 * @author doujiale
 * @since 2023-09-05
 */
@Mapper
public interface BpmPerformanceMapper extends BaseMapperX<BpmPerformance> {

    default PageResult<BpmPerformance> selectPage(PerformancePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BpmPerformance>()
                .orderByDesc(BpmPerformance::getCreateTime)
                .eqIfPresent(BpmPerformance::getResult, reqVO.getResult())
                .eqIfPresent(BpmPerformance::getContractType, reqVO.getContractType())
                .likeIfPresent(BpmPerformance::getContractName, reqVO.getSearchText())
                .betweenIfPresent(BpmPerformance::getCreateTime, reqVO.getCreateTime())
                .inIfPresent(BpmPerformance::getProcessInstanceId, reqVO.getProcessInstanceIds()));
    }

}
