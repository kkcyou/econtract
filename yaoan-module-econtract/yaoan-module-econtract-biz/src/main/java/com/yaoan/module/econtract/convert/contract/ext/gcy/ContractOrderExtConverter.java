package com.yaoan.module.econtract.convert.contract.ext.gcy;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.contract.dto.ContractDTO;
import com.yaoan.module.econtract.api.contract.dto.ContractDataDTO;
import com.yaoan.module.econtract.api.contract.dto.ContractOrderExtDTO;
import com.yaoan.module.econtract.controller.admin.contract.vo.UploadContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.save.OrderContractCreateReqV2Vo;
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

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/6 16:48
 */
@Mapper
public interface ContractOrderExtConverter {
    ContractOrderExtConverter INSTANCE = Mappers.getMapper(ContractOrderExtConverter.class);

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

    default String timeToString(LocalDateTime winBidTime) {
        if (ObjectUtil.isNotEmpty(winBidTime)) {
            return winBidTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return "";
    }


    default Date toDate(LocalDateTime updateTime) {
        return Date.from(updateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Asia/Shanghai")).toInstant());
    }
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
    default String setTotalMoney(BigDecimal totalMoney) {
        return totalMoney.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

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

    ContractOrderExtDTO toContractOrderExtDTO(ContractOrderExtDO extDO);
}
