package com.yaoan.module.econtract.convert.gcy.gpmall;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.gcy.order.*;
import com.yaoan.module.econtract.controller.admin.contract.vo.OrderRespVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order.GroupedDraftOrderInfoVO;
import com.yaoan.module.econtract.controller.admin.order.vo.*;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.*;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageInfoDO;
import com.yaoan.module.econtract.dal.dataobject.statistics.StatisticsDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * @description:
 * @author: Pele
 * @date: 2023/12/4 12:08
 */
@Mapper
public interface GPMallOrderConverter {
    GPMallOrderConverter INSTANCE = Mappers.getMapper(GPMallOrderConverter.class);

    List<GPMallPageRespVO> listOrderDO2Resp(List<DraftOrderInfoDO> draftOrderInfoDOPageResult);

    PageResult<GroupedDraftOrderInfoVO> convertOrderOrgRespPage(PageResult<DraftOrderInfoDO> draftOrderInfoDOPageResult);

    @Mapping(target = "list", expression = "java(toSingletonList(draftOrderInfoDO))")
    GroupedDraftOrderInfoVO convertOrderDO2GroupedResp(DraftOrderInfoDO draftOrderInfoDO);

    default List<GPMallPageRespVO> toSingletonList(DraftOrderInfoDO draftOrderInfoDO) {
        if ( draftOrderInfoDO == null ) {
            return new ArrayList<>();
        }

        GPMallPageRespVO gPMallPageRespVO = new GPMallPageRespVO();

        gPMallPageRespVO.setOrderType( draftOrderInfoDO.getProjectCategoryCode() );
        gPMallPageRespVO.setId( draftOrderInfoDO.getId() );
        gPMallPageRespVO.setOrderGuid( draftOrderInfoDO.getOrderGuid() );
        gPMallPageRespVO.setOrderCode( draftOrderInfoDO.getOrderCode() );
        gPMallPageRespVO.setOrderStatus( draftOrderInfoDO.getOrderStatus() );
        gPMallPageRespVO.setOrderStatusStr( draftOrderInfoDO.getOrderStatusStr() );
        gPMallPageRespVO.setOrderTotalAmount( draftOrderInfoDO.getOrderTotalAmount() );
        gPMallPageRespVO.setPurchaserOrgGuid( draftOrderInfoDO.getPurchaserOrgGuid() );
        gPMallPageRespVO.setPurchaserOrg( draftOrderInfoDO.getPurchaserOrg() );
        gPMallPageRespVO.setRegionFullName( draftOrderInfoDO.getRegionFullName() );
        gPMallPageRespVO.setRegionGuid( draftOrderInfoDO.getRegionGuid() );
        if ( draftOrderInfoDO.getPayTime() != null ) {
            gPMallPageRespVO.setPayTime( Date.from( draftOrderInfoDO.getPayTime().toInstant( ZoneOffset.UTC ) ) );
        }
        gPMallPageRespVO.setSupplierName( draftOrderInfoDO.getSupplierName() );
        gPMallPageRespVO.setSupplierGuid( draftOrderInfoDO.getSupplierGuid() );
        gPMallPageRespVO.setModelId( draftOrderInfoDO.getModelId() );
        gPMallPageRespVO.setBuyPlanCode( draftOrderInfoDO.getBuyPlanCode() );
        gPMallPageRespVO.setBuyPlanName( draftOrderInfoDO.getBuyPlanName() );
        gPMallPageRespVO.setBuyPlanAmount( draftOrderInfoDO.getBuyPlanAmount() );
        gPMallPageRespVO.setContractFrom( draftOrderInfoDO.getContractFrom() );
        gPMallPageRespVO.setStatus( draftOrderInfoDO.getStatus() );
        gPMallPageRespVO.setTradingType( draftOrderInfoDO.getTradingType() );
        gPMallPageRespVO.setTradingTypeName( draftOrderInfoDO.getTradingTypeName() );
        gPMallPageRespVO.setIsDraft( draftOrderInfoDO.getIsDraft() );

        gPMallPageRespVO.setPayTimeBack(ObjectUtil.isNotEmpty(draftOrderInfoDO.getPayTime())
                ? draftOrderInfoDO.getPayTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : "");


        ArrayList<GPMallPageRespVO> draftOrderInfoDOS = new ArrayList<>();
        draftOrderInfoDOS.add(gPMallPageRespVO);
        return draftOrderInfoDOS;
    }


    @Mapping(target = "payTime", expression = "java(string2localDateTime(info.getPayTime()))")
    DraftOrderInfoDO convertDTO2DO(DraftOrderInfo info);

    default LocalDateTime string2localDateTime(String str) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
            return dateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    List<OrderAccessoryDO> accessoryVOList2DOList(List<OrderAccessoryEntity> allAccessoryList);

    List<FileAttachmentDO> fileAttachmentVOList2DOList(List<FileAttachmentVO> fileAttachmentVOList);

    EngineeringProjectDO engineeringProjectVO2DO(EngineeringProjectVO engineeringProjectVO);

    List<GoodsDO> goodsVOList2DOList(List<GoodsVO> goodsVOList);

    List<GoodsVO> toGoodsVOS(List<GoodsDO> goodsDOS);

    ProjectDO projectVO2DO(ProjectVO projectVO);
    PageResult<GPMallPageRespVO> convertOrderPageDO2RespPage(PageResult<DraftOrderInfoDO> draftOrderInfoDOPageResult);

    List<GoodsRespVO> conertGoodsListDO2Resp(List<GoodsDO> goodsDOList);

    OrderBaseInfoRespVO toBaseInfoRespVO(DraftOrderInfoDO orderInfoDO);

    OrderAutoInfoRespVO convert2Auto(DraftOrderInfoDO orderInfoDO);

    ThirdOrderAutoInfoRespVO convert2ThirdAuto(DraftOrderInfoDO orderInfoDO);

    PageResult<GPMallPageV2RespVO> convertOrderPageDO2RespPageV2(PageResult<DraftOrderInfoDO> draftOrderInfoDOPageResult);

    List<GPMallPageV2RespVO> listOrderPageDO2RespPageV2(List<DraftOrderInfoDO> draftOrderInfoDOPageResult);

//    GPMallPageV2RespVO c1(DraftOrderInfoDO entity);

    OrderAutoInfoV2RespVO convert2AutoV2(DraftOrderInfoDO orderInfoDO);

    //    @Mapping(target = "payTime", expression = "java(draftOrderInfoDO.getPayTime()==null?null:draftOrderInfoDO.getPayTime())")
    @Mapping(target = "orderType", source = "projectCategoryCode")
    @Mapping(target = "payTimeBack", expression = "java(timeToString(draftOrderInfoDO.getPayTime()))")
    GPMallPageRespVO convertOrderDO2Resp(DraftOrderInfoDO draftOrderInfoDO);
    default String timeToString(LocalDateTime winBidTime) {
        if (ObjectUtil.isNotEmpty(winBidTime)) {
            return winBidTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return "";
    }

    default LocalDateTime judgePlatform(LocalDateTime bidNoticeDate, LocalDateTime payTime) {
        if (ObjectUtil.isNotEmpty(bidNoticeDate)) {
            return bidNoticeDate;
        }
        if (ObjectUtil.isNotEmpty(payTime)) {
            return payTime;
        }
        return null;
    }

    @Mapping(target = "orderId", source = "orderGuid")
    @Mapping(target = "platform", source = "contractFrom")
    @Mapping(target = "orgId", source = "purchaserOrgGuid")
    @Mapping(target = "orgName", source = "purchaserOrg")
    @Mapping(target = "supplierId", source = "supplierGuid")
    @Mapping(target = "totalAmount", source = "orderTotalAmount")
    @Mapping(target = "winBidTime", expression = "java(judgePlatform(info.getBidNoticeDate(),info.getPayTime()))")
    @Mapping(target = "regionName", source = "regionFullName")
    StatisticsDO toStatistics(DraftOrderInfoDO info);

    List<StatisticsDO> toStatisticsList(List<DraftOrderInfoDO> finalDraftOrderInfoDOList);

    OrderRespVO do2Resp9(DraftOrderInfoDO bean);
    List<OrderRespVO> listDo2Resp9(List<DraftOrderInfoDO> draftOrderInfoDOS);



    @Mapping(target = "packageId", source = "packageGuid")
    @Mapping(target = "packageAmount", source = "amount")
    @Mapping(target = "orgId", source = "purchaserOrgIds")
    @Mapping(target = "regionCode", source = "zoneCode")
    @Mapping(target = "regionName", source = "zoneName")
    @Mapping(target = "totalAmount", source = "winBidAmount")
    @Mapping(target = "winBidTime", source = "winBidTime")
    @Mapping(target = "purchaseMethodCode", source = "purchaseMethodCode")
    @Mapping(target = "purchaseMethodName", source = "purchaseMethodName")
    @Mapping(target = "projectCategoryCode", source = "projectGuid")
    @Mapping(target = "projectCategoryName", source = "projectType")
    StatisticsDO packageToStatistics (PackageInfoDO info);
    List<StatisticsDO> packageToStatisticsList(List<PackageInfoDO> packageInfoDOList);

    DraftOrderInfo do2VO4Temp(DraftOrderInfoDO bean);
    List<DraftOrderInfo> do2VO4TempList(List<DraftOrderInfoDO> draftOrderInfoDOS);

}
