package com.yaoan.module.econtract.convert.gcy.gpmall;

import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractBillVo;
import com.yaoan.module.econtract.controller.admin.contract.xmlvo.ContractObjectInformation;
import com.yaoan.module.econtract.controller.admin.supervise.vo.SuperviseGoodsVO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.ContractGoodsDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.GoodsDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PlanDetailInfoDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;


/**
 * @description:合同绑定合同明细（商品/标的）
 * @author: zhc
 * @date: 2024-03-18
 */
@Mapper
public interface ContractGoodsConverter {
    ContractGoodsConverter INSTANCE = Mappers.getMapper(ContractGoodsConverter.class);

    List<SuperviseGoodsVO> toVOS(List<ContractGoodsDO> contractGoodsDOS);

    List<ContractGoodsDO> toDOS(List<SuperviseGoodsVO> goodsVOS);

    @Mapping(source = "id", target = "contractBillGuid")
    @Mapping(source = "billGuid", target = "buyPlanBillGuid")
    @Mapping(source = "actualBuyUnit", target = "unit")
    @Mapping(source = "isImport", target = "isImports")
    @Mapping(source = "planBuyPrice", target = "planPrice")
    @Mapping(source = "planBuyNum", target = "planPurchaseNum")
    @Mapping(source = "contractTotalMoney", target = "totalPrice")
    @Mapping(source = "actualBuyPrice", target = "price")
    @Mapping(source = "actualBuyNum", target = "purchaseNum")
    @Mapping(source = "planTotalMoney", target = "planTotalPrice")
    ContractBillVo toVo(ContractGoodsDO contractGoodsDO);

    List<ContractBillVo> toVoList(List<ContractGoodsDO> contractGoods);
    @Mapping(source = "goodsBrandName", target = "brand")
    @Mapping(target = "totalPrice", expression = "java(goods.getTotalMoney()==null?null:goods.getTotalMoney().doubleValue())")
    @Mapping(target = "purchaseNum", expression = "java(goods.getQty()==null?null:Double.valueOf(goods.getQty()))")
    @Mapping(target = "price", expression = "java(goods.getGoodsOnePrice()==null?null:goods.getGoodsOnePrice().doubleValue())")
    @Mapping(target = "planPrice", expression = "java(goods.getBuyPlanPrice()==null?null:goods.getBuyPlanPrice().doubleValue())")
    @Mapping(target = "planPurchaseNum", expression = "java(goods.getBuyPlanNum()==null?null:goods.getBuyPlanNum())")
    @Mapping(target = "planTotalPrice", expression = "java(goods.getBuyPlanTotalMoney()==null?null:goods.getBuyPlanTotalMoney().doubleValue())")
    ContractBillVo toVO(GoodsDO goods);
    List<ContractBillVo> toVOS2(List<GoodsDO> goodsDOS);

    List<ContractBillVo> toVoListV1(List<PlanDetailInfoDO> bean);

    @Mapping(source = "id", target = "buyPlanBillGuid")
    @Mapping(source = "budgetMoney", target = "totalPrice")
    @Mapping(source = "isImported", target = "isImports")
//    @Mapping(source = "outsiteId", target = "buyPlanBillGuid")
    @Mapping(source = "catalogueCode", target = "purCatalogCode")
    @Mapping(source = "catalogueName", target = "goodsName")
    @Mapping(source = "detailNumber", target = "purchaseNum")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "unit", target = "unit")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "budgetMoney", target = "planTotalPrice", qualifiedByName = "bigDecimalToDouble")
    ContractBillVo toVoV1(PlanDetailInfoDO bean);

    @Named("bigDecimalToDouble")
    default Double bigDecimalToDouble(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }

    @Named("doubleToBigDecimal")
    default BigDecimal doubleToBigDecimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    List<ContractObjectInformation> toContractObjectInformations(List<ContractBillVo> bean);

    @Mapping(target = "contractObjectName", source = "goodsName")
    @Mapping(target = "contractObjectUnitPrice", source = "price")
    @Mapping(target = "contractObjectQuantity", source = "purchaseNum")
    @Mapping(target = "unitOfMeasurement", source = "unit")
    @Mapping(target = "contractObjectAmount", source = "totalPrice")
    @Mapping(target = "isImports", expression = "java(bean.getIsImports() != null && bean.getIsImports() == 1)")
    @Mapping(target = "govService", expression = "java(bean.getGovService() != null && bean.getGovService() == 1)")
    @Mapping(target = "billStatisticalInformation", source = "contractBillStatisticalVo")
    @Mapping(target = "billStatisticalInformation.efficient", expression = "java(contractBillStatisticalVo.getEfficient() != null && contractBillStatisticalVo.getEfficient() == 1)")
    @Mapping(target = "billStatisticalInformation.waterSaving", expression = "java(contractBillStatisticalVo.getWaterSaving() != null && contractBillStatisticalVo.getWaterSaving() == 1)")
    @Mapping(target = "billStatisticalInformation.environment", expression = "java(contractBillStatisticalVo.getEnvironment() != null && contractBillStatisticalVo.getEnvironment() == 1)")
    ContractObjectInformation toContractObjectInformation(ContractBillVo bean);

}
