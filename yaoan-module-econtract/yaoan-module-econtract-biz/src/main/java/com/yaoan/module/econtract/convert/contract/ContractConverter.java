package com.yaoan.module.econtract.convert.contract;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.contract.dto.GPXContractCreateReqDTO;
import com.yaoan.module.econtract.api.contract.dto.GetTokenDTO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BpmContractRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.BorrowBpmReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.register.vo.ContractRegisterListApproveRespVO;
import com.yaoan.module.econtract.controller.admin.change.vo.ContractChangeListApproveRespVO;
import com.yaoan.module.econtract.controller.admin.change.vo.ContractChangeOneRespVO;
import com.yaoan.module.econtract.controller.admin.change.vo.ContractChangePageRespVO;
import com.yaoan.module.econtract.controller.admin.change.vo.ContractChangeSaveReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.contract.xmlvo.GeneralInformation;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.query.AcceptanceContractRespVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.save.OrderContractCreateReqV2Vo;
import com.yaoan.module.econtract.controller.admin.outward.contract.VO.*;
import com.yaoan.module.econtract.controller.admin.payment.deferred.vo.ContractInfoV2RespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.ContractInfoRespVO;
import com.yaoan.module.econtract.controller.admin.paymentPlan.vo.PaymentPlanInfoRespVO;
import com.yaoan.module.econtract.controller.admin.performance.v2.vo.ContractPerformV2RespVO;
import com.yaoan.module.econtract.controller.admin.workbench.v2.vo.ContractDataRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.task.WorkBenchTaskListRespVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangeDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SimpleContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.TradingContractExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.UploadContractOrderExtDO;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Mapper
public interface ContractConverter {
    ContractConverter INSTANCE = Mappers.getMapper(ContractConverter.class);

    //    @Mapping(target = "contractContent", expression = "java(contractCreateReqVO.getContractContent().getBytes())")
    @Mapping(target = "contractContent", expression = "java(contractCreateReqVO.getContractContent() != null ? contractCreateReqVO.getContractContent().getBytes() : new byte[0])")
    @Mapping(target = "fileName", expression = "java(contractCreateReqVO.getName() + \".pdf\")")
    //@Mapping(target = "location", source = "contractSignAddress" )
    @Mapping(target = "location", expression = "java(contractCreateReqVO.getLocation() != null ? contractCreateReqVO.getLocation(): contractCreateReqVO.getContractSignAddress())")
    ContractDO toEntity(ContractCreateReqVO contractCreateReqVO);

    @Mapping(target = "contractContent", expression = "java(apiCreateReqVO.getContractContent() != null ? apiCreateReqVO.getContractContent().getBytes() : new byte[0])")
    @Mapping(target = "validity0", expression = "java(apiCreateReqVO.getEffectDate())")
    @Mapping(target = "validity1", expression = "java(apiCreateReqVO.getExpiryDate())")
    @Mapping(target = "name", expression = "java(apiCreateReqVO.getContractName())")
    @Mapping(target = "code", expression = "java(apiCreateReqVO.getContractCode())")
    ContractDO toEntity(ApiCreateReqVO apiCreateReqVO);

    @Mapping(target = "contractContent", expression = "java(contractCreateBaseReqVO.getContractContent() != null ? contractCreateBaseReqVO.getContractContent().getBytes() : new byte[0])")
    ContractDO toEntity(ContractCreateBaseReqVO contractCreateBaseReqVO);

    ContractDO convert(ContractPageReqVO contractPageReqVO);

    ContractDO convert(ContractBaseVO contractBaseVO);

    ContractDO convert(ContractUpdateReqVO contractUpdateReqVO);

    ContractDO convert(ContractPageRespVO contractPageRespVO);

    //    @Mapping(target = "contractContent", expression = "java(entityToString(contractDO.getContractContent()))")
    @Mapping(target = "contractContent", expression = "java(contractDO.getContractContent() == null ? null : entityToString(contractDO.getContractContent()))")
    ContractRespVO convert(ContractDO contractDO);

    @Mapping(target = "contractContent", expression = "java(contractDO.getContractContent() == null ? null : entityToString(contractDO.getContractContent()))")
    @Mapping(target = "effectDate" , expression = "java(contractDO.getValidity0())")
    @Mapping(target = "expiryDate" , expression = "java(contractDO.getValidity1())")
    ApiRespVO convertV4(ContractDO contractDO);

    @Mapping(target = "contractContent", expression = "java(contractDO.getContractContent() == null ? null : entityToString(contractDO.getContractContent()))")
    @Mapping(target = "effectDate" , expression = "java(contractDO.getValidity0())")
    @Mapping(target = "expiryDate" , expression = "java(contractDO.getValidity1())")
    ApiInfoRespVO convertV5(ContractDO contractDO);

    ContractCheckRespVO convertV2(ContractDO contractDO);

    ContractPageRespVO convert(PageResult<ContractDO> page);

    //付款管理列表转换list
    PageResult<PaymentSchedulePageRespVO> convertPaymentPage(PageResult<ContractDO> page);

    PageResult<ContractPageRespVO> convertPage(PageResult<ContractDO> page);

    PageResult<LoanPageRespVO> convertPageV1(PageResult<ContractDO> page);

    PageResult<ApiPageRespVO> convertPageV2(PageResult<ContractDO> page);

    @Mapping(target = "effectDate" , expression = "java(contractDO.getValidity0())")
    @Mapping(target = "expiryDate" , expression = "java(contractDO.getValidity1())")
    ApiPageRespVO convertV6(ContractDO contractDO);

    @Mapping(source = "status", target = "statusName", qualifiedByName = "integerToString")
    ContractPageRespVO convertV3(ContractDO contractDO);

    @Named("integerToString")
    default String integerToString(Integer status) {
        ContractStatusEnums enums = ContractStatusEnums.getInstance(status);
        if (ObjectUtil.isNotNull(enums)) {
            return enums.getDesc();
        }
        return "";
    }

    PageResult<ContractRespVO> convertPage2(PageResult<ContractDO> page);

    List<ContractDO> converList(List<ApiStatusReqVO> list);

    List<ContractListRespVO> convertPage3(List<ContractDO> list);


    /**
     * ---------------------------------- 付款申请使用 ----------------------------------
     */
    ContractInfoRespVO convertDO2Info(ContractDO contractDO);

    @Mapping(source = "id", target = "contractId")
    @Mapping(source = "name", target = "contractName")
    @Mapping(source = "code", target = "contractCode")
    PaymentPlanInfoRespVO tovo(ContractDO contractDO);

    List<BpmContractRespVO> convertBpmDO2Resp(List<ContractDO> doList);

    SimpleContractDO changeSave2DO(ContractChangeSaveReqVO vo);


    List<ContractChangeListApproveRespVO> convertBpmDO2RespList(List<ContractDO> doList);

    ContractChangePageRespVO do2Resp(ContractDO entity);

    List<ContractChangePageRespVO> do2RespList(List<ContractDO> entity);

    List<ContractChangePageRespVO> simpleDO2RespList(List<SimpleContractDO> entity);

    PageResult<ContractChangePageRespVO> do2RespPage(PageResult<SimpleContractDO> contractDOList);

    List<ContractRegisterListApproveRespVO> convertRegisterBpmDO2RespList(List<ContractDO> doList);

    List<ContractRegisterListApproveRespVO> convertRegisterBpmDO2RespList2(List<SimpleContractDO> doList);

    @Mapping(source = "id", target = "contractId")
    BorrowBpmReqVO convertV1(ContractDO bean);

    @Mapping(source = "id", target = "contractId")
    List<BorrowBpmReqVO> convertList(List<ContractDO> bean);

    ContractChangeOneRespVO changeDO2Resp(BpmContractChangeDO changeDO);

    GetTokenRespVO toGetTokenRespVO(GetTokenDTO dto);

    ErrorInfosRespVO toErrorInfosRespVO(GetTokenDTO dto);


    @Mapping(target = "contractSignAddress", source = "location")
    @Mapping(target = "buyerOrgName", source = "partAName")
    @Mapping(target = "buyerOrgGuid", source = "partAId")
    @Mapping(target = "supplierGuid", source = "partBId")
    @Mapping(target = "supplierName", source = "partBName")
    @Mapping(target = "contractSignTime", source = "signDate")
    @Mapping(target = "modelId", source = "templateId")
    @Mapping(target = "totalMoney", expression = "java(contractDO.getAmount() == null ? null :  BigDecimal.valueOf(contractDO.getAmount()))")
    ContractDataVo toContractDataVo(ContractDO contractDO);

    @Mapping(target = "contractContent", expression = "java(uploadContractCreateReqVO.getContractContent() != null ? uploadContractCreateReqVO.getContractContent().getBytes() : new byte[0])")
    @Mapping(target = "platform", expression = "java(uploadContractCreateReqVO.getContractFrom() != null ? uploadContractCreateReqVO.getContractFrom():uploadContractCreateReqVO.getPlatform())")
    @Mapping(target = "amount", expression = "java(uploadContractCreateReqVO.getTotalMoney() != null ? uploadContractCreateReqVO.getTotalMoney().doubleValue():null)")
    @Mapping(target = "orderGuid", source = "orderId")
    @Mapping(target = "location", source = "contractSignAddress")
    @Mapping(target = "secrecyClause", source = "clauseSecret")
    ContractDO uploadVOtoUploadDO(UploadContractCreateReqVO uploadContractCreateReqVO);

    @Mapping(target = "contractContent", expression = "java(uploadContractCreateReqVO.getContractContent() != null ? uploadContractCreateReqVO.getContractContent().getBytes() : new byte[0])")
    @Mapping(target = "platform", expression = "java(uploadContractCreateReqVO.getContractFrom() != null ? uploadContractCreateReqVO.getContractFrom():uploadContractCreateReqVO.getPlatform())")
    @Mapping(target = "amount", expression = "java(uploadContractCreateReqVO.getTotalMoney() != null ? uploadContractCreateReqVO.getTotalMoney().doubleValue():null)")
    @Mapping(target = "orderGuid", source = "orderId")
    @Mapping(target = "location", source = "contractSignAddress")
    @Mapping(target = "secrecyClause", source = "clauseSecret")
    ContractDO fileuploadVOtoUploadDO(FileUploadContractCreateReqVO uploadContractCreateReqVO);

    //    @Mapping(target = "contractSignTime", expression = "java(orderContractDO.getContractSignTime()==null?null:setDate2String(orderContractDO.getContractSignTime()))")
//    @Mapping(target = "startDate", expression = "java(orderContractDO.getStartDate()==null?null:setDate2String(orderContractDO.getStartDate()))")
//    @Mapping(target = "endDate", expression = "java(orderContractDO.getEndDate()==null?null:setDate2String(orderContractDO.getEndDate()))")
    @Mapping(target = "totalMoney", expression = "java(orderContractDO.getAmount() == null ? null :  BigDecimal.valueOf(orderContractDO.getAmount()))")
    @Mapping(target = "orderId", source = "orderGuid")
    @Mapping(target = "supplierName", source = "partBName")
    @Mapping(target = "supplierId", source = "partBId")
    @Mapping(target = "buyerOrgId", source = "partAId")
    @Mapping(target = "buyerOrgName", source = "partAName")
    @Mapping(target = "contractSignAddress", source = "location")
    @Mapping(target = "clauseSecret", source = "secrecyClause")
    UploadContractCreateReqVO doConvert2UploadVo(ContractDO orderContractDO);



    default String setDate2String(Date time) {
        if (ObjectUtil.isNotNull(time)) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = inputFormat.format(time);
            return format;
        }
        return "";
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

    PageResult<ContractPerformV2RespVO> page2RespVO(PageResult<ContractDO> contractPerformanceDOPageResult);

    PageResult<ContractChangePageRespVO> converPageRespVO(PageResult<ContractDO> page);

    ContractInfoV2RespVO convertDO2InfoV2(ContractDO contractDO);

//    @Mapping(target = "contractContent", expression = "java(contractCreateReqVO.getContractContent() != null ? contractCreateReqVO.getContractContent().getBytes() : new byte[0])")
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "performanceCompleteDate", ignore = true)
    @Mapping(target = "expirationDate", ignore = true)
    @Mapping(target = "riskDate", ignore = true)
    @Mapping(target = "signDate", ignore = true)
    @Mapping(target = "pauseDate", ignore = true)
    @Mapping(target = "amount",expression = "java(bean.getTotalMoney().doubleValue())")
    @Mapping(target = "platform", expression = "java(bean.getContractFrom()==null?null:bean.getContractFrom())")
    ContractDO req2D(OrderContractCreateReqV2Vo bean);

    /**
     * ----------------------------------文件格式转换---------------------------------
     */
    default String entityToString(byte[] contractContent) {
        return StringUtils.toEncodedString(contractContent, StandardCharsets.UTF_8);
    }

    ContractPageRespVO ext2Resp(ContractOrderExtDO contractOrderExtDO);

    @Mapping(target = "amount", expression = "java(contractDO.getTotalMoney() != null ? contractDO.getTotalMoney().doubleValue():null)")
    ContractDO ext2DO( UploadContractOrderExtDO contractDO);

    @Mapping(target = "mainContractName", source = "name")
    @Mapping(target = "mainContractCode", source = "code")
    @Mapping(target = "mainContractId", source = "id")
    ContractChangeOneRespVO convertDO2Resp(ContractDO contractPageRespVO);


    @Mapping(target = "amount", expression = "java(contractDO.getTotalMoney() != null ? contractDO.getTotalMoney().doubleValue():null)")
    ContractDO tradingExt2DO(TradingContractExtDO contractDO);


    PageResult<WorkBenchTaskListRespVO> convert2BenchDraft(PageResult<ContractDO> contractDOPageResult);

    List<WorkBenchTaskListRespVO> cBenchDraftList(List<ContractDO> list);

    @Mapping(target = "processInstanceId", source = "agentId")
    @Mapping(target = "contractId", source = "id")
    WorkBenchTaskListRespVO cBenchDraft(ContractDO c);


    List<ContractOrderExtDO> do2ExtList(List<ContractDO> contractDOList);

    @Mapping(target = "totalMoney", expression = "java(bean.getAmount() == null ? null : new BigDecimal(String.valueOf(bean.getAmount())))")
    @Mapping(target = "contractSignTime", source = "signDate")
    ContractOrderExtDO do2Ext(ContractDO bean);

    @Mapping(target = "contractContent", expression = "java(contractRespVO.getContractContent() != null ? contractRespVO.getContractContent().getBytes() : new byte[0])")
    ContractDO convert2Resp(ContractRespVO contractRespVO);

    PageResult<ContractDataRespVO> do2DataResp(PageResult<ContractDO> contractDOPageResult);

    @Mapping(target = "contractGuid", source = "id")
    @Mapping(target = "buyPlanGuid", source = "buyPlanId")
    @Mapping(target = "contractNO", source = "code")
    @Mapping(target = "contractName", source = "name")
    @Mapping(target = "effectiveDate", source = "startDate")
    @Mapping(target = "grossAmount", source = "totalMoney")
    @Mapping(target = "paymentMethod", source = "contractPayType")
    @Mapping(target = "performAddress", source = "contractSignAddress")
//    @Mapping(target = "performMethod",source = "")履行方式
//    @Mapping(target = "qualityRequirement",source = "")质量要求
    @Mapping(target = "breachResponsibility", source = "violateAppointDutyReplenish")
    @Mapping(target = "disputeResolutionMethod", source = "disputesProcessType")
//    @Mapping(target = "signingWebsite",source = "")签约网址
    @Mapping(target = "signingTime", source = "contractSignTime")
    @Mapping(target = "isPerformanceMoney", expression = "java(tradingContractDO.getIsPerformanceMoney() != null && tradingContractDO.getIsPerformanceMoney() == 1)")
    @Mapping(target = "isRetentionMoney", expression = "java(tradingContractDO.getIsRetentionMoney() != null && tradingContractDO.getIsRetentionMoney() == 1)")
    @Mapping(target = "secret", expression = "java(tradingContractDO.getSecret() != null && tradingContractDO.getSecret() == 1)")
    @Mapping(target = "reserveStatus", expression = "java(toBoolean(tradingContractDO.getReserveStatus()))")
    @Mapping(target = "clauseSecret", expression = "java(toBoolean(tradingContractDO.getClauseSecret()))")
    @Mapping(target = "isAdvanceCharge", expression = "java(toBoolean(tradingContractDO.getIsAdvanceCharge()))")
    @Mapping(target = "purMethod", source = "purchaseMethod")
    @Mapping(target = "propertyOwnership", source = "knowledgePropertyOwner")
    GeneralInformation convertGeneralInformation(ContractOrderExtDO tradingContractDO);

    default Boolean toBoolean(Integer value) {
        return value != null && value == 1;
    }

    @Mapping(target = "contractGuid", source = "id")
    @Mapping(target = "contractNO", source = "code")
    @Mapping(target = "contractName", source = "name")
    @Mapping(target = "effectiveDate", source = "validity0")
    @Mapping(target = "performAddress", source = "location")
    @Mapping(target = "signingTime", source = "contractSignTime")
    GeneralInformation convertGeneralInformationV2(ContractDO contractDO);

    AcceptanceContractRespVO acceptDO2Resp(ContractDO contractDO);

    PageResult<TransferContractAllRespVO> toTransferContractAllRespVO(PageResult<ContractDO> pageResult);

    PageResult<SimpleContractListRespVO> convertPageDO2SimpleResp(PageResult<ContractDO> contractDOPageResult);
}
