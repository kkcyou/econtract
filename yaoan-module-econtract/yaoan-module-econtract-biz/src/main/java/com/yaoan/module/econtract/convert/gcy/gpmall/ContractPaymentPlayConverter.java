package com.yaoan.module.econtract.convert.gcy.gpmall;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.PaymentPlanDTO;
import com.yaoan.module.econtract.controller.admin.contract.xmlvo.PaymentInformation;
import com.yaoan.module.econtract.controller.admin.supervise.vo.PaymentPlanVO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;


/**
 * @description:合同关联支付计划
 * @author: zhc
 * @date: 2024-03-18
 */
@Mapper
public interface ContractPaymentPlayConverter {
    ContractPaymentPlayConverter INSTANCE = Mappers.getMapper(ContractPaymentPlayConverter.class);
    @Mapping(target = "amount",expression = "java(double2BigDecimal(vo.getMoney()))")
    @Mapping(target = "paymentTime",source = "payDate")
    @Mapping(target = "terms",source = "payTerm")
    @Mapping(target = "sort",source = "periods")
    @Mapping(target = "paymentRatio",expression = "java(double2BigDecimal(vo.getPayProportion()))")
    PaymentScheduleDO toDO(PaymentPlanVO vo);
    List<PaymentScheduleDO> toDOS(List<PaymentPlanVO> paymentPlanVOS);

    @Mapping(target = "payDate", expression = "java(paymentPlan.getPaymentTime()==null?null:paymentPlan.getPaymentTime().getTime())")
    @Mapping(target = "periods",source = "sort")
    @Mapping(target = "payee",source = "payee")
    @Mapping(target = "money",source = "amount")
    @Mapping(target = "payTerm",source = "terms")
    @Mapping(target = "payProportion",source = "paymentRatio")
    PaymentPlanDTO toVo(PaymentScheduleDO paymentPlan);
    List<PaymentPlanDTO> toVoList(List<PaymentScheduleDO> contractPaymentPlanDOList);

    default BigDecimal double2BigDecimal(Double input) {
        return ObjectUtil.isNull(input) ? null : new BigDecimal(String.valueOf(input)).setScale(2, RoundingMode.HALF_UP);
    }

    default Double bigDecimal2Double(BigDecimal input) {
        return ObjectUtil.isNull(input) ? null : input.doubleValue();
    }

    List<PaymentInformation> toPaymentInformation(List<PaymentPlanDTO> been);

    @Mapping(target = "paymentAmount",source = "money")
    @Mapping(target = "paymentDate",expression = "java(map(been.getPayDate()))")
    @Mapping(target = "paymentTerms",source = "payTerm")
    PaymentInformation toPayment(PaymentPlanDTO been);

    default Date map(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
}
