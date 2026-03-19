package com.yaoan.module.econtract.convert.contract.ext.gcy;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.module.econtract.controller.admin.contract.vo.UploadContractCreateReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.UploadContractOrderExtDO;
import com.yaoan.module.econtract.enums.gcy.gpmall.ContractAuditStatusEnums;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/13 17:23
 */
@Mapper
public interface UploadContractOrderExtConverter {
    UploadContractOrderExtConverter INSTANCE = Mappers.getMapper(UploadContractOrderExtConverter.class);


    @Mapping(target = "startDate", expression = "java(vo.getStartDate()==null?null: setDate(vo.getStartDate()))")
    @Mapping(target = "performStartDate", expression = "java(vo.getStartDate()==null?null: setDate(vo.getStartDate()))")
    @Mapping(target = "performEndDate", expression = "java(vo.getEndDate()==null?null: setDate(vo.getEndDate()))")
    @Mapping(target = "endDate", expression = "java(vo.getEndDate()==null?null: setDate(vo.getEndDate()))")
    @Mapping(target = "contractSignTime", expression = "java(vo.getContractSignTime()==null?null:setDate(vo.getContractSignTime()))")
    @Mapping(target = "contractContent", expression = "java(vo.getContractContent() != null ? vo.getContractContent().getBytes() : new byte[0])")
    UploadContractOrderExtDO uploadVOtoUploadDO(UploadContractCreateReqVO vo);

    default byte[] setContent(UploadContractCreateReqVO vo){
        if(StringUtils.isEmpty(vo.getContractContent())){
            return null;
        }
        return vo.getContractContent().getBytes(StandardCharsets.UTF_8);
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

//    default String statusName(Integer status) {
//        if (ObjectUtil.isEmpty(status)) {
//            return "";
//        }
//        String info = "";
//        ContractAuditStatusEnums enums = ContractAuditStatusEnums.getInstance(status);
//
//        if (ObjectUtil.isNotNull(enums)) {
//            info = enums.getInfo();
//        }
//
//        return info;
//    }
    default String setDate2String(Date time) {
        if (ObjectUtil.isNotNull(time)) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = inputFormat.format(time);
            return format;
        }
        return "";
    }

    default  String setContractContent(byte[] contractContent){
        if(ObjectUtil.isNull(contractContent)){
            return null;
        }
        return new String(contractContent, StandardCharsets.UTF_8);
    }
    @Mapping(target = "contractSignTime", expression = "java(orderContractDO.getContractSignTime()==null?null:setDate2String(orderContractDO.getContractSignTime()))")
    @Mapping(target = "startDate", expression = "java(orderContractDO.getStartDate()==null?null:setDate2String(orderContractDO.getStartDate()))")
    @Mapping(target = "endDate", expression = "java(orderContractDO.getEndDate()==null?null:setDate2String(orderContractDO.getEndDate()))")
    UploadContractCreateReqVO doConvert2UploadVo(UploadContractOrderExtDO orderContractDO);

}
