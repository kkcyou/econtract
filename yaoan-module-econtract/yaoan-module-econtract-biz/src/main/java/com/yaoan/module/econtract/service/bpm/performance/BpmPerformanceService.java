package com.yaoan.module.econtract.service.bpm.performance;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.performance.vo.PerformancePageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.performance.vo.PerformanceRespVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
public interface BpmPerformanceService {

    PageResult<PerformanceRespVO> getApprovePage(PerformancePageReqVO reqVO);
}
