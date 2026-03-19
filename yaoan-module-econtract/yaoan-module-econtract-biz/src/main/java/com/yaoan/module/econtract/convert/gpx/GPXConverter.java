package com.yaoan.module.econtract.convert.gpx;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.contract.dto.GPXContractCreateReqDTO;
import com.yaoan.module.econtract.api.contract.dto.gpx.GPXContractQuotationRelDTO;
import com.yaoan.module.econtract.api.contract.dto.gpx.PurchaseDTO;
import com.yaoan.module.econtract.api.contract.dto.gpx.TradingSupplierDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractBillVo;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractProjectVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.SupplierCombinationInfoVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.GPXContractQuotationRelReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.GPXContractQuotationRelRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.GPXListRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.PlanDetailInfoRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.autofill.PackageDetailRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.autofill.PackageRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.autofill.PartARespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.draft.*;
import com.yaoan.module.econtract.controller.admin.warning.vo.ContractBakProjectVO;
import com.yaoan.module.econtract.controller.admin.warning.vo.ContractWarningQueryPackageRespVO;
import com.yaoan.module.econtract.controller.admin.warning.vo.PackageInfoVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.trading.PurchaseDO;
import com.yaoan.module.econtract.dal.dataobject.contract.trading.TradingSupplierDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.*;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/18 17:12
 */
@Mapper
public interface GPXConverter {
    GPXConverter INSTANCE = Mappers.getMapper(GPXConverter.class);

    @Mapping(target = "planAmount", source = "planBudget")
    PlanInfo toInfoVO(PlanInfoDO planInfoDO);


    @Mapping(target = "projectGuid", source = "id")
    GPXProjectDO cp(ProjectInfo vo);

    List<GPXProjectDO> listProjectReq2DO(List<ProjectInfo> list);

    @Mapping(target = "supplierIds", expression = "java(getSupplierIds(vo))")
    @Mapping(target = "packageGuid", source = "id")
    PackageInfoDO c1(PackageInfo vo);

    @Mapping(target = "detailId", source = "id")
    PackageDetailInfoDO c2(PackageDetailInfo vo);

    default String getSupplierIds(PackageInfo vo) {
        return vo.getSupplierList() == null ? null : vo.getSupplierList()
                .stream().map(SupplierInfo::getSupplierId).collect(Collectors.toList()).toString();

    }

    List<PackageDetailInfoDO> listBillR2D(List<PackageDetailInfo> billInfoList);


    List<PackageInfoDO> listPackR2D(List<PackageInfo> packageInfoList);

    PageResult<GPXListRespVO> pageD2R(PageResult<PackageInfoDO> page);

    List<PackageDetailInfoDO> listDetalR2D(List<PackageDetailInfo> packageDetailInfoList);

    List<SupplierInfoDO> listSupR2D(List<SupplierInfo> supplierList);

    List<PlanInfoDO> listPlanR2D(List<PlanInfo> planList);

    @Mapping(target = "detailId", source = "id")
    PackageDetailParamDO cPackageDetailParamR2D(PackageDetailParam packageDetailParam);

    List<PackageDetailParamDO> cPackageDetailParamR2DList(List<PackageDetailParam> packageDetailParam);

    SupplierInfoDO cSupR2D(SupplierInfo supplierInfo);

    List<SupplierCombinationInfoDO> listsupComR2D(List<SupplierCombinationInfo> supplierCombinationInfoList);

    List<BidConfirmQuotationDetailDO> listBidConfirmQuaR2D(List<BidConfirmQuotationDetail> bidConfirmQuotationDetailList);

    @Mapping(target = "supplierGuid",source = "supplierId")
    BidConfirmQuotationDetailDO cBidR2D(BidConfirmQuotationDetail vo);

    List<BatchPlanInfoDO> listBatchPlanR2D(List<BatchPlanInfo> batchPlanInfoList);

    BatchPlanInfoDO cBatchPlanR2D(BatchPlanInfo vo);

    @Mapping(target = "planId", source = "id")
    PlanInfoDO cPlanR2D(PlanInfo planInfo);

    PlanDetailInfoDO cPlanDetailR2D(PlanDetailInfo planDetailInfo);

    List<PlanDetailInfoDO> cPlanDetailR2DList(List<PlanDetailInfo> planDetailInfo);

    List<GPXListRespVO> listProD2Res(List<PackageInfoDO> packageInfoDOList);

    @Mapping(target = "winBidTimeBack", expression = "java(timeToString(e.getWinBidTime()))")
    GPXListRespVO cProD2R(PackageInfoDO e);

    default String timeToString(Date time) {
        if (ObjectUtil.isNotEmpty(time)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(time);
            return ObjectUtil.isNotEmpty(formattedDate) ? formattedDate : "";
        }
        return "";
    }

    PackageRespVO packD2Rep(GPXProjectDO projectDO);

    List<PackageDetailRespVO> packDetailD2Resp(List<PackageDetailInfoDO> packageDetailInfoDOList);

    PackageDetailRespVO sPackDetailD2Resp(PackageDetailInfoDO d);

    PartARespVO cSupplyD2Resp(SupplierInfoDO supplierInfoDO);

    GPXContractProjectVO convertProject(PackageInfoDO packageInfoDO);



    List<SupplierCombinationInfoVO> supplierCombinationList2VO(List<SupplierCombinationInfoDO> supplierList);
    @Mapping(target = "buyPlanCode", source = "projectName")
    @Mapping(target = "purchaseUnitName", source = "projectTypeName")
    ContractWarningQueryPackageRespVO cPlanD2R(PackageInfoDO planInfoDO);
    PageResult<ContractWarningQueryPackageRespVO> cPageD2R(PageResult<PackageInfoDO> page);

    PackageInfoVO toPackageInfoVO(PackageInfoDO packageInfoDO);

    ContractBakProjectVO toContractBakProjectVO (PackageInfoDO packageInfoDO);

    @Mapping(target = "quotationId", source = "id")
    GPXContractQuotationRelRespVO quoD2R(BidConfirmQuotationDetailDO quotationDetailDO);

    List<PlanDetailInfoRespVO> listPlanDetailD2R(List<PlanDetailInfoDO> planDetailInfoDOList);

    PlanDetailInfoRespVO planDetailD2R(PlanDetailInfoDO i);

    List<GPXContractQuotationRelDO> listConQuoR2D(List<GPXContractQuotationRelReqVO> quotationRelReqVOList);

    GPXContractQuotationRelDO conQuoR2D(GPXContractQuotationRelReqVO i);

    List<GPXContractQuotationRelRespVO> listConQuaD2R(List<GPXContractQuotationRelDO> quotationRelDOList);

    GPXContractQuotationRelRespVO ConQuaD2R(GPXContractQuotationRelDO i);

    @Mapping(target = "content", expression = "java(content2String(contractOrderExtDO.getContractContent()))")
    @Mapping(target = "packageId",source = "buyPlanPackageId")
    @Mapping(target = "planId",source = "buyPlanId")
    @Mapping(target = "projectId",source = "projectGuid")
    GPXContractCreateReqDTO gpxDO2DTO(ContractOrderExtDO contractOrderExtDO);

    List<GPXContractQuotationRelDTO> quoDO2DTO(List<GPXContractQuotationRelDO> quotationRelDOList);

    PurchaseDTO purchaseDO2DTO(PurchaseDO bean);
    List<PurchaseDTO> purchaseDO2DTOList(List<PurchaseDO> purchaseDOList);

    TradingSupplierDTO tradingSupplierDO2DTO(TradingSupplierDO bean);
    List<TradingSupplierDTO> tradingSupplierDO2DTOList(List<TradingSupplierDO> tradingSupplierDOList);

    default String content2String(byte[] termContent) {
        return StringUtils.toEncodedString(termContent, StandardCharsets.UTF_8);
    }

    List<ContractBillVo> convertContractBillVOS(List<GPXContractQuotationRelDO> contractQuotationRelDOList);
    @Mapping(target = "goodsName",source = "detailName")
    @Mapping(target = "brand",source = "brandName")
    @Mapping(target = "unit",source = "brandName")
    ContractBillVo convertContractBillVO(GPXContractQuotationRelDO contractQuotationRelDOList);
}
