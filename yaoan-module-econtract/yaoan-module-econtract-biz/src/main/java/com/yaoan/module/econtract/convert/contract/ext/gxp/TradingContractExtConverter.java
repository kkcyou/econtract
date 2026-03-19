package com.yaoan.module.econtract.convert.contract.ext.gxp;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractRespVO;
import com.yaoan.module.econtract.controller.admin.supervise.vo.GPXContractPageVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.TradingContractExtDO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/2 16:51
 */
@Mapper
public interface TradingContractExtConverter {
    TradingContractExtConverter INSTANCE = Mappers.getMapper(TradingContractExtConverter.class);


    @Mapping(target = "startDate", expression = "java(bean.getStartDate()==null?null: setDate(bean.getStartDate()))")
    @Mapping(target = "endDate", expression = "java(bean.getEndDate()==null?null: setDate(bean.getEndDate()))")
    @Mapping(target = "contractSignTime", expression = "java(bean.getContractSignTime()==null?null: setDate(bean.getContractSignTime()))")
    @Mapping(target = "contractContent", expression = "java(bean.getContent()!= null ? bean.getContent().getBytes() : new byte[0])")
    @Mapping(target = "contractSignAddress", source = "bean.signAddress")
    TradingContractExtDO toEntity(GPXContractCreateReqVO bean);


    @Mapping(target = "contractSignTime", expression = "java(orderContractDO.getContractSignTime()==null?null:setDate2String(orderContractDO.getContractSignTime()))")
    @Mapping(target = "startDate", expression = "java(orderContractDO.getStartDate()==null?null:setDate2String(orderContractDO.getStartDate()))")
    @Mapping(target = "endDate", expression = "java(orderContractDO.getEndDate()==null?null:setDate2String(orderContractDO.getEndDate()))")
    GPXContractRespVO toRespVO(TradingContractExtDO orderContractDO);

    PageResult<GPXContractPageVO> toGPXPageRespVO(PageResult<TradingContractExtDO> pageResult);








    default Date setDate(String datestr) {
        Date date = null;
        if (StringUtils.isNotBlank(datestr)) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy年MM月dd日");
            try {
                if(datestr.length()==10){
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

    @Mapping(target = "contractSignTime", expression = "java(orderContractDO.getContractSignTime()==null?null:setDate2String(orderContractDO.getContractSignTime()))")
    @Mapping(target = "startDate", expression = "java(orderContractDO.getStartDate()==null?null:setDate2String(orderContractDO.getStartDate()))")
    @Mapping(target = "endDate", expression = "java(orderContractDO.getEndDate()==null?null:setDate2String(orderContractDO.getEndDate()))")
    GPXContractRespVO toRespVO4Sign(ContractOrderExtDO orderContractDO);
}
