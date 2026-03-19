package com.yaoan.module.econtract.convert.bpm.performance.suspend;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.demo.dto.DemoRespDTO;
import com.yaoan.module.econtract.controller.admin.bpm.performance.vo.PerformanceRespVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoCreateReqVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoRespVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.performance.v2.vo.PerformQueryRespVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.performance.suspend.BpmPerformance;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.demo.EcmsDemo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * @Author：doujl
 * @Description:  转换类
 * @Date: 2023年07月05日14:30:38
 */
@Mapper
public interface PerformanceConverter {

    PerformanceConverter INSTANCE = Mappers.getMapper(PerformanceConverter.class);

    PageResult<PerformanceRespVO> convertPage(PageResult<BpmPerformance> page);


    PerformQueryRespVO d2R(PaymentScheduleDO paymentScheduleDO);
}
