package com.yaoan.module.econtract.service.warningcfg.query.stragefactory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yaoan.framework.mybatis.core.query.QueryWrapperX;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.service.warningcfg.query.DOQueryStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaymentScheduleQueryStrategy implements DOQueryStrategy<PaymentScheduleDO> {
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper; // 注入对应的Mapper
    @Override
    public Class<PaymentScheduleDO> getDOClass() {
        return PaymentScheduleDO.class;
    }

    @Override
    public QueryWrapperX<PaymentScheduleDO> createQueryWrapper() {
        return new QueryWrapperX<PaymentScheduleDO>();
    }

    @Override
    public BaseMapper<PaymentScheduleDO> getMapper() {
        return paymentScheduleMapper;
    }
}