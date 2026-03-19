package com.yaoan.module.econtract.convert.contract;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.contract.dto.gpx.GPXPaymentPlanDTO;
import com.yaoan.module.econtract.api.contract.dto.gpx.StagePaymentDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.PaymentPlanDTO;
import com.yaoan.module.econtract.controller.admin.change.vo.changerisk.ChangeRiskPlanRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.contract.vo.ledger.PayRecordRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.AcceptanceRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.query.AcceptancePlanRespVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.upload.UploadContractPaymentPlanVo;
import com.yaoan.module.econtract.controller.admin.paymentPlan.vo.ContractPaymentPlanRespVO;
import com.yaoan.module.econtract.controller.admin.paymentPlan.vo.PaymentPlanRespVO;
import com.yaoan.module.econtract.controller.admin.paymentPlan.vo.PerformV2SaveReqVO;
import com.yaoan.module.econtract.controller.admin.supervise.vo.PaymentPlanVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangePaymentDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SimpleContractDO;
import com.yaoan.module.econtract.service.gcy.gpmall.vo.ContractPaymentPlanVo;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Mapper
public interface PaymentScheduleConverter {
    PaymentScheduleConverter INSTANCE = Mappers.getMapper(PaymentScheduleConverter.class);

    PaymentScheduleDO toEntity(PaymentScheduleVO paymentScheduleVO);

    List<PaymentScheduleVO> toPaymentScheduleVO(List<PaymentScheduleDO> paymentScheduleVDOS);

    PaymentSchedulePageRespVO toVO(PaymentScheduleDO paymentScheduleDO);

    List<PaymentScheduleRespVO> toList(List<PaymentScheduleDO> list);

    PageResult<PaymentPlanRespVO> toPageVOV2(PageResult<PaymentScheduleDO> pageResult);

    PageResult<ContractPaymentPlanRespVO> toPageVO(PageResult<PaymentScheduleDO> pageResult);

    List<PaymentPlanRespVO> toVOS(List<PaymentScheduleDO> paymentScheduleDOS);

    PageResult<PaymentSchedulePageRespVO> pageDO2Resp(PageResult<PaymentScheduleDO> paymentScheduleDOS);

    List<PaymentSchedulePageRespVO> listDO2Resp(List<PaymentScheduleDO> list);

    List<PaymentScheduleRespVO> toRespVOList(List<PaymentScheduleDO> list);

    List<PaymentScheduleRespVO> toRespVOList2(List<BpmContractChangePaymentDO> list);

    @Mapping(target = "amount", expression = "java(entity.getAmount() == null ? null : double2BigDecimal(entity.getAmount()))")
    PaymentSchedulePageRespVO pageDO2ContractResp(SimpleContractDO entity);


    List<PaymentSchedulePageRespVO> pageDO2ContractRespList(List<SimpleContractDO> contractDOPageResult);

    PageResult<PaymentSchedulePageRespVO> pageDO2ContractRespPage(PageResult<SimpleContractDO> contractDOPageResult);

    PageResult<PayPerformancePageRespVO> paymentPageDO2VO(PageResult<PaymentScheduleDO> paymentScheduleDOPageResult);

    List<PayPerformancePageRespVO> paymentDOList2VOList(List<PaymentScheduleDO> paymentScheduleDOList);


    List<PaymentScheduleDO> toPaymentScheduleDOS(List<ContractPaymentPlanVo> contractPaymentPlanVos);

    List<PaymentScheduleDO> toPaymentScheduleDOS3(List<PaymentScheduleVO> contractPaymentPlanVos);

    @Mapping(target = "paymentTime", expression = "java(contractPaymentPlanVo.getPayDate() == null ? null : setDate(contractPaymentPlanVo.getPayDate()))")
    @Mapping(target = "paymentRatio", expression = "java(contractPaymentPlanVo.getPayProportion() == null ? null : double2BigDecimal(contractPaymentPlanVo.getPayProportion()))")
    @Mapping(target = "terms", source = "payTerm")
    @Mapping(target = "sort", source = "periods")
    @Mapping(target = "amount", expression = "java(contractPaymentPlanVo.getMoney() == null ? null : double2BigDecimal(contractPaymentPlanVo.getMoney()))")
    PaymentScheduleDO toPaymentScheduleDO(ContractPaymentPlanVo contractPaymentPlanVo);

    @Mapping(target = "money", expression = "java(bean.getAmount() == null ? null : bigDecimal2Double(bean.getAmount()))")
    @Mapping(target = "payDate", expression = "java(bean.getPaymentTime() == null ? null : setDate2String(bean.getPaymentTime() ))")
    @Mapping(target = "payProportion", expression = "java(bigDecimal2Double(bean.getPaymentRatio()))")
    @Mapping(target = "payTerm", source = "terms")
    @Mapping(target = "periods", source = "sort")
    ContractPaymentPlanVo toPlanVo(PaymentScheduleDO bean);
    List<ContractPaymentPlanVo> toPlanVoList(List<PaymentScheduleDO> contractPaymentPlanDOList);

    List<StagePaymentVO> toPaymentVO(List<PaymentScheduleDO> contractPaymentPlanDOList);

    List<PaymentScheduleDO> stage2PaymentScheduleDOS2(List<StagePaymentVO> payMentInfo);

    @Mapping(target = "amount", expression = "java(stagePaymentVO.getStagePaymentAmount() == null ? null : double2BigDecimal(stagePaymentVO.getStagePaymentAmount()))")
    @Mapping(target = "paymentTime", expression = "java(stagePaymentVO.getPaymentDate() == null ? null : setDate(stagePaymentVO.getPaymentDate()))")
    @Mapping(target = "terms", source = "stagePaymentResult")
    PaymentScheduleDO toStagePaymentVO(StagePaymentVO stagePaymentVO);

    List<PaymentScheduleDO> toChangePaymentList(List<BpmContractChangePaymentDO> doList);

    PaymentScheduleDO toChangePayment(BpmContractChangePaymentDO pay);

    @Mapping(target = "remindType", expression = "java(performV2SaveReqVO.getRemindType() == null ? null : trimString(performV2SaveReqVO.getRemindType()) )")
    PaymentScheduleDO perf2PaymentScheduleDO(PerformV2SaveReqVO performV2SaveReqVO);

    @Mapping(target = "isRemind", expression = "java(string2Integer(paymentScheduleDO.getIsRemind()))")
    PayPerformanceDetailRespVO PaymentScheduleDO2perfVO(PaymentScheduleDO paymentScheduleDO);

    default LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    default Long setDateStrTOlong(String datestr) {
        if (StringUtils.isBlank(datestr) || datestr.length() != 10) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(datestr); // 将字符串解析为Date对象
            Long longDate = date.getTime(); // 将Date对象转换为long类型的毫秒值
            return longDate;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    default Integer string2Integer(String str) {
        if(StringUtils.isBlank(str)){
            return null;
        }
        return  Integer.valueOf(str);
    }

    default Date setDate(String datestr) {
        Date date = null;
        if (StringUtils.isNotBlank(datestr)) {

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                if (datestr.length() == 11) {
                    inputFormat = new SimpleDateFormat("yyyy年MM月dd日");
                } else if (datestr.length() == 10) {
                    inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                } else if (datestr.length() == 19) {
                    inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                } else {
                    return null;
                }
                date = inputFormat.parse(datestr);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return date;
    }

    default String trimString(List<String> value) {
        return StrUtil.join(",", value); // 使用 StrUtil 进行字符串处理
    }

    default BigDecimal getAmount(ContractPaymentPlanVo vo) {
        if (ObjectUtil.isNotNull(vo.getMoney())) {
            return BigDecimal.valueOf(vo.getMoney());
        }
        return BigDecimal.ZERO;
    }

    @Mapping(target = "paymentTime", expression = "java(vo.getPayDate() == null ? null : setDate(vo.getPayDate()))")
    @Mapping(target = "paymentRatio", expression = "java(vo.getPayProportion() == null ? null : double2BigDecimal(vo.getPayProportion()))")
    @Mapping(target = "terms", source = "payTerm")
    @Mapping(target = "sort", source = "periods")
    @Mapping(target = "amount", expression = "java(vo.getMoney() == null ? null : double2BigDecimal(vo.getMoney()))")

    @Mapping(target = "stagePaymentAmount", source = "money")
    @Mapping(target = "paymentDate", source = "payDate")
    PaymentScheduleDO uploadPaymentToPlanDO(UploadContractPaymentPlanVo vo);

    List<PaymentScheduleDO> listUploadPayment2PlanDO(List<UploadContractPaymentPlanVo> dos);


    List<PaymentScheduleDO> listUploadStage2PlanDO(List<StagePaymentVO> list);

    default String setDate2String(Date time) {
        if (ObjectUtil.isNotNull(time)) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = inputFormat.format(time);
            return format;
        }
        return "";
    }


    @Mapping(target = "money", source = "amount")
    @Mapping(target = "payProportion", source = "paymentRatio")
    @Mapping(target = "payDate", source = "paymentTime")
    @Mapping(target = "payTerm", source = "terms")
    @Mapping(target = "periods", source = "sort")
    PaymentPlanDTO PaymentToDTO(PaymentScheduleDO paymentScheduleDO);

    List<PaymentPlanDTO> PaymentListToDTO(List<PaymentScheduleDO> paymentScheduleDOS);


    @Mapping(target = "payDate", expression = "java(enhancePayDate(planDO))")
    @Mapping(target = "money", expression = "java(enhanceMoney(planDO))")
    @Mapping(target = "payProportion", expression = "java(enhancePayProportion(planDO))")
    @Mapping(target = "payTerm", source = "terms")
    @Mapping(target = "periods", source = "sort")
    UploadContractPaymentPlanVo uploadDO2UploadVO(PaymentScheduleDO planDO);

    List<UploadContractPaymentPlanVo> toPaymentUploadVO(List<PaymentScheduleDO> contractPaymentPlanDOList);

    default String enhancePayDate(PaymentScheduleDO planDO) {
        if (ObjectUtil.isNull(planDO)) {
            return null;
        }
        if (ObjectUtil.isNotNull(planDO.getPaymentTime())) {
            return setDate2String(planDO.getPaymentTime());
        }
        if (StringUtils.isNotBlank(planDO.getPaymentDate())) {
            return planDO.getPaymentDate();
        }
        return null;
    }

    default Double enhanceMoney(PaymentScheduleDO planDO) {
        if (ObjectUtil.isNull(planDO)) {
            return null;
        }
        if (ObjectUtil.isNotNull(planDO.getAmount())) {
            return planDO.getAmount().doubleValue();
        }
        if (ObjectUtil.isNotNull(planDO.getStagePaymentAmount())) {
            return planDO.getStagePaymentAmount();
        }
        return null;
    }

    default Double enhancePayProportion(PaymentScheduleDO planDO) {
        if (ObjectUtil.isNull(planDO)) {
            return null;
        }
        if (ObjectUtil.isNotNull(planDO.getPaymentRatio())) {
            return planDO.getPaymentRatio().doubleValue();
        }
        return null;
    }


    StagePaymentVO updateStage2VO(PaymentScheduleDO bean);

    List<StagePaymentVO> toUploadStageVO(List<PaymentScheduleDO> contractPaymentPlanDOList);

    @Mapping(target = "money", expression = "java(bigDecimal2Double(bean.getAmount()))")
    @Mapping(target = "payDate", source = "paymentTime")
    @Mapping(target = "payeeId", source = "payee")
    @Mapping(target = "payProportion", expression = "java(bigDecimal2Double(bean.getPaymentRatio()))")
    @Mapping(target = "payTerm", source = "terms")
    @Mapping(target = "periods", source = "sort")
    PaymentPlanVO toPaymentPlanVO(PaymentScheduleDO bean);

    List<PaymentPlanVO> toPaymentVOs(List<PaymentScheduleDO> scheduleDOList);

    default BigDecimal double2BigDecimal(Double input) {
        return ObjectUtil.isNull(input) ? null : new BigDecimal(String.valueOf(input)).setScale(2, RoundingMode.HALF_UP);
    }

    default Double bigDecimal2Double(BigDecimal input) {
        return ObjectUtil.isNull(input) ? null : input.doubleValue();
    }

    StagePaymentDTO gpxDO2StageDTO(PaymentScheduleDO bean);
    List<StagePaymentDTO> gpxDO2StageDTOList(List<PaymentScheduleDO> paymentScheduleDOList);


    @Mapping(target = "money", expression = "java(bean.getAmount() == null ? null : bigDecimal2Double(bean.getAmount()))")
    @Mapping(target = "payDate",source="paymentTime")
    @Mapping(target = "payProportion", expression = "java(bigDecimal2Double(bean.getPaymentRatio()))")
    @Mapping(target = "payTerm", source = "terms")
    @Mapping(target = "periods", source = "sort")
    GPXPaymentPlanDTO gpxDO2PaymentPlanDTO(PaymentScheduleDO bean);
    List<GPXPaymentPlanDTO> gpxDO2PaymentPlanDTOList(List<PaymentScheduleDO> paymentScheduleDOList);

    List<ContractXMLVO.Phase> toXMLVOList(List<PaymentScheduleDO> paymentScheduleDOList);

//    @Mapping(target = "amount", expression = "java(paymentScheduleDO.getAmount() != null && paymentScheduleDO.getAmount().doubleValue() > 0  ?  paymentScheduleDO.getAmount().doubleValue():paymentScheduleDO.getStagePaymentAmount() )")
//    @Mapping(target = "description", expression = "java(paymentScheduleDO.getTerms() != null && paymentScheduleDO.getTerms().isEmpty() ? paymentScheduleDO.getStagePaymentResult() : paymentScheduleDO.getTerms())")
    @Mapping(target = "dueDate", expression = "java(getPaymentDate(paymentScheduleDO))")
    @Mapping(target = "phaseID", expression = "java(String.valueOf(paymentScheduleDO.getSort()))")
    @Mapping(target = "currency", constant = "CNY")
    ContractXMLVO.Phase toXMLVO(PaymentScheduleDO paymentScheduleDO);
    default String getPaymentDate(PaymentScheduleDO paymentScheduleDO) {
        if (ObjectUtil.isNotNull(paymentScheduleDO.getPaymentTime())) {
            return DateUtil.format(paymentScheduleDO.getPaymentTime(), "yyyy-MM-dd");
        }
        if (StringUtils.isNotBlank(paymentScheduleDO.getPaymentDate())) {
            return paymentScheduleDO.getPaymentDate();
        }
        return null;
    }

    PageResult<AcceptanceRespVO> accepDO2RespPage(PageResult<PaymentScheduleDO> pageResult);

    AcceptancePlanRespVO accepDO2Resp(PaymentScheduleDO paymentScheduleDO);

    List<PayRecordRespVO> listDO2Resp4Ledger(List<PaymentScheduleDO> paymentScheduleDOList);

    List<ChangeRiskPlanRespVO> tochangeRespVOList(List<PaymentScheduleDO> paymentScheduleDOList);
}

