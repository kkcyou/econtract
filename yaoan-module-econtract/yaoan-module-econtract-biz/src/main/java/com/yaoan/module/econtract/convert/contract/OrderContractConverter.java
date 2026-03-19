package com.yaoan.module.econtract.convert.contract;

import cn.hutool.core.util.ObjectUtil;

import com.yaoan.module.econtract.api.contract.dto.ContractDTO;

import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractMVO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractVo;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractDataVo;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.save.OrderContractCreateReqV2Vo;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.save.OrderContractCreateReqVO;

import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractRespVO;

import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.enums.gcy.gpmall.ContractAuditStatusEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Mapper
public interface OrderContractConverter {
    OrderContractConverter INSTANCE = Mappers.getMapper(OrderContractConverter.class);

    @Mapping(target = "startDate", expression = "java(vo.getStartDate()==null?null: setDate(vo.getStartDate()))")
    @Mapping(target = "endDate", expression = "java(vo.getEndDate()==null?null: setDate(vo.getEndDate()))")
    @Mapping(target = "contractSignTime", expression = "java(vo.getContractSignTime()==null?setDate(vo.getDatenow()):setDate(vo.getContractSignTime()))")
    @Mapping(target = "platform", expression = "java(vo.getContractFrom()==null?null:vo.getContractFrom())")
    @Mapping(target = "workingDayNum", expression = "java(StringToInteger(vo.getWorkingDayNum()))")
    @Mapping(target = "isSmallCompany", expression = "java(StringToInteger(vo.getIsSmallCompany()))")
    @Mapping(target = "isSignChoose", expression = "java(StringToInteger(vo.getIsSignChoose()))")
    @Mapping(target = "doSign", expression = "java(StringToInteger(vo.getIsSign()))")
    @Mapping(target = "isPublicity", expression = "java(StringToInteger(vo.getIsPublicity()))")
//    @Mapping(target = "isExport", expression = "java(StringToInteger(vo.getIsExport()))")
    @Mapping(target = "containInstall", expression = "java(StringToInteger(vo.getContainInstall()))")
    @Mapping(target = "contractNum", expression = "java(StringToInteger(vo.getContractNum()))")
    @Mapping(target = "personNum", expression = "java(StringToInteger(vo.getPersonNum()))")
    @Mapping(target = "goodsTime", expression = "java(StringToInteger(vo.getGoodsTime()))")
    @Mapping(target = "accidentDays", expression = "java(StringToInteger(vo.getAccidentDays()))")
    @Mapping(target = "continuationDays", expression = "java(StringToInteger(vo.getContinuationDays()))")
    @Mapping(target = "supplierWorkingDayNum2", expression = "java(StringToInteger(vo.getSupplierWorkingDayNum2()))")
    @Mapping(target = "supplierWorkingDayNum1", expression = "java(StringToInteger(vo.getSupplierWorkingDayNum1()))")
    @Mapping(target = "contractCategory", expression = "java(StringToInteger(vo.getContractCategory()))")
    @Mapping(target = "acceptanceProblemDealDays", expression = "java(StringToInteger(vo.getAcceptanceProblemDealDays()))")
    @Mapping(target = "canCancelDays", expression = "java(StringToInteger(vo.getCanCancelDays()))")
    @Mapping(target = "workDays", expression = "java(StringToInteger(vo.getWorkDays()))")
    @Mapping(target = "contractDays", expression = "java(StringToInteger(vo.getContractDays()))")
    @Mapping(target = "purchaseResultValidity", expression = "java(StringToInteger(vo.getPurchaseResultValidity()))")
    @Mapping(target = "supplierNoServiceOverDays", expression = "java(StringToInteger(vo.getSupplierNoServiceOverDays()))")
    @Mapping(target = "accidentStartDays", expression = "java(StringToInteger(vo.getAccidentStartDays()))")
    @Mapping(target = "accidentEndDays", expression = "java(StringToInteger(vo.getAccidentEndDays()))")
    @Mapping(target = "orgMaterialsDays", expression = "java(StringToInteger(vo.getOrgMaterialsDays()))")
    @Mapping(target = "orgMaterialsNum", expression = "java(StringToInteger(vo.getOrgMaterialsNum()))")
    @Mapping(target = "contractCopyNum", expression = "java(StringToInteger(vo.getContractCopyNum()))")
    @Mapping(target = "supMaterialsDays", expression = "java(StringToInteger(vo.getSupMaterialsDays()))")
    @Mapping(target = "orgDealDaysByWarrantyInform", expression = "java(StringToInteger(vo.getOrgDealDaysByWarrantyInform()))")
    @Mapping(target = "supDealDaysByGetBooks", expression = "java(StringToInteger(vo.getSupDealDaysByGetBooks()))")
    @Mapping(target = "supSubmitDrawingEndNum", expression = "java(StringToInteger(vo.getSupSubmitDrawingEndNum()))")
    @Mapping(target = "projectWarrantyYears", expression = "java(StringToInteger(vo.getProjectWarrantyYears()))")
    @Mapping(target = "supDealDaysByWarrantyInform", expression = "java(StringToInteger(vo.getSupDealDaysByWarrantyInform()))")
    @Mapping(target = "supDealDaysByMaintainInform", expression = "java(StringToInteger(vo.getSupDealDaysByMaintainInform()))")
    @Mapping(target = "clauseSecret", expression = "java(StringToInteger(vo.getClauseSecret()))")
    ContractOrderExtDO toDO(OrderContractCreateReqVO vo);
    @Mapping(target = "startDate", expression = "java(vo.getStartDate()==null?null: setDate(vo.getStartDate()))")
    @Mapping(target = "endDate", expression = "java(vo.getEndDate()==null?null: setDate(vo.getEndDate()))")
    @Mapping(target = "contractSignTime", expression = "java(vo.getContractSignTime()==null?setDate(vo.getDatenow()):setDate(vo.getContractSignTime()))")
    @Mapping(target = "platform", expression = "java(vo.getContractFrom()==null?null:vo.getContractFrom())")
    @Mapping(target = "workingDayNum", expression = "java(StringToInteger(vo.getWorkingDayNum()))")
    @Mapping(target = "isSmallCompany", expression = "java(StringToInteger(vo.getIsSmallCompany()))")
    @Mapping(target = "isSignChoose", expression = "java(StringToInteger(vo.getIsSignChoose()))")
    @Mapping(target = "doSign", expression = "java(StringToInteger(vo.getIsSign()))")
    @Mapping(target = "isPublicity", expression = "java(StringToInteger(vo.getIsPublicity()))")
//    @Mapping(target = "isExport", expression = "java(StringToInteger(vo.getIsExport()))")
    @Mapping(target = "containInstall", expression = "java(StringToInteger(vo.getContainInstall()))")
    @Mapping(target = "contractNum", expression = "java(StringToInteger(vo.getContractNum()))")
    @Mapping(target = "personNum", expression = "java(StringToInteger(vo.getPersonNum()))")
    @Mapping(target = "goodsTime", expression = "java(StringToInteger(vo.getGoodsTime()))")
    @Mapping(target = "accidentDays", expression = "java(StringToInteger(vo.getAccidentDays()))")
    @Mapping(target = "continuationDays", expression = "java(StringToInteger(vo.getContinuationDays()))")
    @Mapping(target = "supplierWorkingDayNum2", expression = "java(StringToInteger(vo.getSupplierWorkingDayNum2()))")
    @Mapping(target = "supplierWorkingDayNum1", expression = "java(StringToInteger(vo.getSupplierWorkingDayNum1()))")
    @Mapping(target = "contractCategory", expression = "java(StringToInteger(vo.getContractCategory()))")
    @Mapping(target = "acceptanceProblemDealDays", expression = "java(StringToInteger(vo.getAcceptanceProblemDealDays()))")
    @Mapping(target = "canCancelDays", expression = "java(StringToInteger(vo.getCanCancelDays()))")
    @Mapping(target = "workDays", expression = "java(StringToInteger(vo.getWorkDays()))")
    @Mapping(target = "contractDays", expression = "java(StringToInteger(vo.getContractDays()))")
    @Mapping(target = "purchaseResultValidity", expression = "java(StringToInteger(vo.getPurchaseResultValidity()))")
    @Mapping(target = "supplierNoServiceOverDays", expression = "java(StringToInteger(vo.getSupplierNoServiceOverDays()))")
    @Mapping(target = "accidentStartDays", expression = "java(StringToInteger(vo.getAccidentStartDays()))")
    @Mapping(target = "accidentEndDays", expression = "java(StringToInteger(vo.getAccidentEndDays()))")
    @Mapping(target = "orgMaterialsDays", expression = "java(StringToInteger(vo.getOrgMaterialsDays()))")
    @Mapping(target = "orgMaterialsNum", expression = "java(StringToInteger(vo.getOrgMaterialsNum()))")
    @Mapping(target = "contractCopyNum", expression = "java(StringToInteger(vo.getContractCopyNum()))")
    @Mapping(target = "supMaterialsDays", expression = "java(StringToInteger(vo.getSupMaterialsDays()))")
    @Mapping(target = "orgDealDaysByWarrantyInform", expression = "java(StringToInteger(vo.getOrgDealDaysByWarrantyInform()))")
    @Mapping(target = "supDealDaysByGetBooks", expression = "java(StringToInteger(vo.getSupDealDaysByGetBooks()))")
    @Mapping(target = "supSubmitDrawingEndNum", expression = "java(StringToInteger(vo.getSupSubmitDrawingEndNum()))")
    @Mapping(target = "projectWarrantyYears", expression = "java(StringToInteger(vo.getProjectWarrantyYears()))")
    @Mapping(target = "supDealDaysByWarrantyInform", expression = "java(StringToInteger(vo.getSupDealDaysByWarrantyInform()))")
    @Mapping(target = "supDealDaysByMaintainInform", expression = "java(StringToInteger(vo.getSupDealDaysByMaintainInform()))")
    @Mapping(target = "clauseSecret", expression = "java(StringToInteger(vo.getClauseSecret()))")
    ContractOrderExtDO toDO(OrderContractCreateReqV2Vo vo);
    default Integer StringToInteger(String str) {
        if (StringUtils.isNotEmpty(str)) {
            return Integer.valueOf(str);
        }
        return null;
    }

    @Mapping(target = "startDate", expression = "java(bean.getStartDate()==null?null: setDate(bean.getStartDate()))")
    @Mapping(target = "endDate", expression = "java(bean.getEndDate()==null?null: setDate(bean.getEndDate()))")
    ContractOrderExtDO toEntity(GPXContractCreateReqVO bean);

    GPXContractRespVO toRespVO(ContractOrderExtDO orderContractDO);


    //    @Mapping(target = "performEndDate", expression = "java(orderContractDO.getPerformEndDate()==null?null:orderContractDO.getPerformEndDate())")
//    @Mapping(target = "performStartDate", expression = "java(orderContractDO.getPerformStartDate()==null?null:orderContractDO.getPerformStartDate())")
    @Mapping(target = "contractGuid", source = "id")
    @Mapping(target = "contractCode", source = "code")
    @Mapping(target = "contractName", source = "name")
    @Mapping(target = "orgGuid", source = "buyerOrgId")
    @Mapping(target = "orgName", source = "buyerOrgName")
    @Mapping(target = "orgLinkman", expression = "java(orderContractDO.getBuyerLegalPerson()==null?orderContractDO.getBuyerProxy():orderContractDO.getBuyerLegalPerson())")
    @Mapping(target = "orgAddress", source = "deliveryAddress")
    @Mapping(target = "orgTelphone", source = "buyerLinkMobile")
    @Mapping(target = "supplierAddress", source = "registeredAddress")
    @Mapping(target = "supplierTelphone", source = "supplierLinkMobile")
    @Mapping(target = "signDate", expression = "java(orderContractDO.getContractSignTime()==null?orderContractDO.getOrgSinTime():orderContractDO.getContractSignTime())")
    @Mapping(target = "signAddress", source = "contractSignAddress")
    @Mapping(target = "purMethod", source = "purchaseMethod")
    @Mapping(target = "bidResultMoney", expression = "java(orderContractDO.getTotalMoney()==null?null:orderContractDO.getTotalMoney().doubleValue())")
    @Mapping(target = "reserveStatus", expression = "java(orderContractDO.getIsReserve()==null?0:orderContractDO.getIsReserve())")
//    @Mapping(target = "bidOpenTime", ignore = true)
//    @Mapping(target = "bidResultDate", ignore = true)
//    @Mapping(target = "supplierSize", expression = "java(orderContractDO.getSupplierSize()==null?"1":orderContractDO.getSupplierSize())")
    @Mapping(target = "performStartDate", expression = "java(orderContractDO.getPerformStartDate()==null?orderContractDO.getOrgSinTime():orderContractDO.getPerformStartDate())")
    @Mapping(target = "performEndDate", expression = "java(orderContractDO.getPerformEndDate()==null?setPerformEndDate(orderContractDO):orderContractDO.getPerformEndDate())")
    ContractVo convert2Vo(ContractOrderExtDO orderContractDO);

    default Date setPerformEndDate(ContractOrderExtDO orderContractDO) {
        Date performEndDate = orderContractDO.getPerformEndDate();
        if (ObjectUtil.isEmpty(performEndDate)) {
            // 创建一个Calendar实例并设置为当前Date的时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(orderContractDO.getContractSignTime() == null ? orderContractDO.getOrgSinTime() : orderContractDO.getContractSignTime());
            // 使用add方法增加一年
            calendar.add(Calendar.YEAR, 1);
            performEndDate = calendar.getTime();
        }
        return performEndDate;
    }
    default String timeToString(LocalDateTime winBidTime) {
        if (ObjectUtil.isNotEmpty(winBidTime)) {
            return winBidTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return "";
    }

    default Date toDate(LocalDateTime updateTime) {
        return Date.from(updateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Asia/Shanghai")).toInstant());
    }



    @Mapping(source = "id", target = "contractGuid")
    @Mapping(source = "code", target = "contractCode")
    @Mapping(source = "name", target = "contractName")
    @Mapping(source = "status", target = "auditStatus")
    @Mapping(source = "buyerOrgId", target = "buyerOrgGuid")
    @Mapping(source = "supplierId", target = "supplierGuid")
    @Mapping(source = "contractSignAddress", target = "signAddress")
    @Mapping(target = "isReserve", expression = "java(orderContractDO.getIsReserve()==null?null:orderContractDO.getIsReserve().toString())")
    ContractDTO toContractDTO(ContractOrderExtDO orderContractDO);



    //------------------------------ status名字赋值 ----------------------------
    default String statusName(Integer status) {
        if (ObjectUtil.isEmpty(status)) {
            return "";
        }
        String info = "";
        ContractAuditStatusEnums enums = ContractAuditStatusEnums.getInstance(status);

        if (ObjectUtil.isNotNull(enums)) {
            info = enums.getInfo();
        }

        return info;
    }

    default Date setDate(String datestr) {
        Date date = null;
        if (StringUtils.isNotBlank(datestr)) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy年MM月dd日");
            try {
                if (datestr.length() == 10) {
                    inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                }
                date = inputFormat.parse(datestr);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return date;
    }

    default String setDate2String(Date time) {
        if (ObjectUtil.isNotNull(time)) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = inputFormat.format(time);
            return format;
        }
        return "";
    }

    default String setLocalDate2String(LocalDateTime time) {
        if (ObjectUtil.isNotNull(time)) {
            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String format = time.format(inputFormat);
            return format;
        }
        return "";
    }
    @Mapping(target = "buyplanGuid", source = "buyPlanGuid")
    ContractMVO convertV2M(ContractVo contractVo);

}
