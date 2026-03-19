package com.yaoan.module.econtract.dal.mysql.payment.paymentapplication;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplScheRelDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/5 16:24
 */
@Mapper
public interface PaymentApplScheRelMapper extends BaseMapperX<PaymentApplScheRelDO> {


}
