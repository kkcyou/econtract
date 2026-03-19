package com.yaoan.module.econtract.convert.gcy.gpmall;

import com.yaoan.module.econtract.api.gcy.order.AssociatedPlanVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.AssociatedPlanRespVO;
import com.yaoan.module.econtract.dal.dataobject.gcy.buyplan.EcmsGcyBuyPlan;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.AssociatedPlanDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PlanInfoDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

/**
 * @description:
 * @author: zhc
 * @date: 2024-01-23 14:13
 */
@Mapper
public interface AssociatedPlanConverter {

    AssociatedPlanConverter INSTANCE = Mappers.getMapper(AssociatedPlanConverter.class);

    AssociatedPlanDO toAssociatedPlanDO(AssociatedPlanVO associatedPlanVO);

    AssociatedPlanVO toAssociatedPlan(AssociatedPlanDO associatedPlanDO);

    @Mapping(source = "implement", target = "implementationForm")
    @Mapping(target = "buyPlanMoney", expression = "java(setMoney(buyPlan.getMoney()))")
    @Mapping(source = "buyPlanGuid", target = "buyPlanId")
    AssociatedPlanVO toEcmsGcyBuyPlan(EcmsGcyBuyPlan buyPlan);

    default BigDecimal setMoney(Double totalMoney) {
        return BigDecimal.valueOf(totalMoney);
    }

    @Mapping(target = "buyPlanName", source = "planName")
    @Mapping(target = "buyPlanCode", source = "planCode")
    @Mapping(target = "buyPlanMoney", source = "planBudget")
    @Mapping(target = "implementationForm", source = "implementName")
    AssociatedPlanVO toGPXPlan(PlanInfoDO planInfoDO);

    AssociatedPlanRespVO do2RespVO(AssociatedPlanDO associatedPlanDO);
}
