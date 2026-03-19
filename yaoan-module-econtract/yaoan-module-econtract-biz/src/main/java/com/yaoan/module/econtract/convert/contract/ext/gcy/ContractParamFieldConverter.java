package com.yaoan.module.econtract.convert.contract.ext.gcy;


import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractAcceptanceVo;
import com.yaoan.module.econtract.controller.admin.contract.xmlvo.AcceptanceInformation;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.save.OrderContractCreateReqV2Vo;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractCreateReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.OrderContractParamFieldDO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


/**
 * @description:合同关联支付计划
 * @author: zhc
 * @date: 2024-03-18
 */
@Mapper
public interface ContractParamFieldConverter {
    ContractParamFieldConverter INSTANCE = Mappers.getMapper(ContractParamFieldConverter.class);

    @Mapping(source = "id", target = "id", ignore = true)
    @Mapping(target = "spotCheckProportion", expression = "java(StringToDouble(gpMallContractCreateReqVO.getSpotCheckProportion()))")
    OrderContractParamFieldDO toDO(OrderContractCreateReqV2Vo gpMallContractCreateReqVO);

    @Mapping(source = "id", target = "id", ignore = true)
    @Mapping(target = "spotCheckProportion", expression = "java(StringToDouble(gpxContractCreateReqVO.getSpotCheckProportion()))")
    OrderContractParamFieldDO toDO(GPXContractCreateReqVO gpxContractCreateReqVO);

    List<ContractAcceptanceVo> toVOS(List<OrderContractParamFieldDO> orderContractParamFieldDOS);

    @Mapping(target = "acceptanceDate", expression = "java(orderContractParamFieldDO.getAcceptanceDate()==null?null:orderContractParamFieldDO.getAcceptanceDate().getTime())")
    ContractAcceptanceVo toVO(OrderContractParamFieldDO orderContractParamFieldDO);

    List<AcceptanceInformation> contractAcceptanceInformation(List<OrderContractParamFieldDO> dos);
    @Mapping(target = "isInviteSupplier", expression = "java(been.getIsInviteSupplier() != null && been.getIsInviteSupplier() == 1)")
    @Mapping(target = "isInviteExpert", expression = "java(been.getIsInviteExpert() != null && been.getIsInviteExpert() == 1)")
    @Mapping(target = "isInviteServiceObject", expression = "java(been.getIsInviteServiceObject() != null && been.getIsInviteServiceObject() == 1)")
    @Mapping(target = "isProfessionalDetection", expression = "java(been.getIsProfessionalDetection() != null && been.getIsProfessionalDetection() == 1)")
    @Mapping(target = "isSpotCheck", expression = "java(been.getIsSpotCheck() != null && been.getIsSpotCheck() == 1)")
    @Mapping(target = "isSampleReference", expression = "java(been.getIsSampleReference() != null && been.getIsSampleReference() == 1)")
    @Mapping(target = "isDestructiveCheck", expression = "java(been.getIsDestructiveCheck() != null && been.getIsDestructiveCheck() == 1)")
    AcceptanceInformation contractAcceptance(OrderContractParamFieldDO been);


    default Double StringToDouble(String str) {
        if (StringUtils.isNotEmpty(str)) {
            return Double.valueOf(str);
        }
        return null;
    }
}
