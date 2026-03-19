package com.yaoan.module.econtract.service.performance.contractPerformance;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerfPageReqVO;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerfReqVO;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerformanceRespVO;
import com.yaoan.module.econtract.controller.admin.performance.v2.vo.*;
import com.yaoan.module.econtract.convert.bpm.performance.suspend.PerformanceConverter;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.convert.payment.PaymentApplicationConverter;
import com.yaoan.module.econtract.convert.performance.contractPerformance.ContractPerfConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.dal.mysql.performance.perforTask.PerforTaskMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ContractStatusEnums.*;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Slf4j
@Service
public class ContractPerfServiceImpl implements ContractPerfService {
    private static final Integer CARD_DAYS = 7;
    @Resource
    private ContractPerforMapper contractPerforMapper;
    @Resource
    private PerforTaskMapper perforTaskMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private ContractPerfConverter contractPerfConverter;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractMapper contractMapper;

    @Override
    public PageResult<ContractPerformanceRespVO> queryAllContractPerf(ContractPerfPageReqVO contractPerfPageReqVO) {
        PageResult<ContractPerformanceDO> contractPerformanceDOPageResult = contractPerforMapper.queryAllContractPerf(contractPerfPageReqVO);
        PageResult<ContractPerformanceRespVO> vo = contractPerfConverter.toVO(contractPerformanceDOPageResult);

        //查询合同类型
        List<String> contractTypeIds = vo.getList().stream().map(ContractPerformanceRespVO::getContractTypeId).collect(Collectors.toList());
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().inIfPresent(ContractType::getId, contractTypeIds));
        Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypes, ContractType::getId);
        //查询履约任务信息
        if (CollUtil.isNotEmpty(vo.getList())) {
            //设置合同类型
            for (ContractPerformanceRespVO contractPerformanceRespVO : vo.getList()) {
                contractPerformanceRespVO.setContractTypeName(contractTypeMap.get(contractPerformanceRespVO.getContractTypeId()) == null ? null : contractTypeMap.get(contractPerformanceRespVO.getContractTypeId()).getName());
                contractPerformanceRespVO.setContractStatusName(setContractStatusName(contractPerformanceRespVO.getContractStatus()));
                //设置履约完成状况
                setFinishInfo(contractPerformanceRespVO);
            }
        }
        return vo;
    }

    @Override
    public String createContractPerf(ContractPerfReqVO contractPerfReqVO) {
        if (ObjectUtil.isNotEmpty(contractPerfReqVO.getValidity1()) && ObjectUtil.isNotEmpty(contractPerfReqVO.getValidity0())) {
            //未生效 合同状态为“待履约”，建立履约任务后，履约任务均为“未开始”。
            if (new Date().before(contractPerfReqVO.getValidity0())) {
                contractPerfReqVO.setContractStatus(ContractPerfEnums.WAIT_PERFORMANCE.getCode());
            }
            //在有效期内：进入履约管理，合同状态为“待建立履约任务”，建立履约任务后，履约任务时间排在第一个的，为履约中，其他为待履约
            //如果有合同结束时间
            if (new Date().after(contractPerfReqVO.getValidity0()) && new Date().before(contractPerfReqVO.getValidity1())) {
                contractPerfReqVO.setContractStatus(ContractPerfEnums.WAIT_CREATE_PERFORMANCE.getCode());
            }
        } else {
            //如果没有合同结束时间
            contractPerfReqVO.setContractStatus(ContractPerfEnums.WAIT_CREATE_PERFORMANCE.getCode());
        }
        ContractPerformanceDO entiry = contractPerfConverter.todo(contractPerfReqVO);
        //校验同一合同下是否只有一个履约
        nameContract(entiry.getContractId());
        contractPerforMapper.insert(entiry);
        return entiry.getId();
    }

    private void setFinishInfo(ContractPerformanceRespVO contractPerformanceRespVO) {
        List<PerfTaskDO> perfTaskDOS = perforTaskMapper.selectList(PerfTaskDO::getContractPerfId, contractPerformanceRespVO.getId());
        int totalsize = perfTaskDOS.size();
        int num = 0;
        for (PerfTaskDO perfTaskDO : perfTaskDOS) {
            if (PerfTaskEnums.PERFORMANCE_FINISH.getCode().equals(perfTaskDO.getTaskStatus()) ||
                    PerfTaskEnums.OVER_TIME_FINISH.getCode().equals(perfTaskDO.getTaskStatus())) {
                num++;
            }
        }
        contractPerformanceRespVO.setFinishInfo(num + "/" + totalsize);
    }

    public void nameContract(String contractId) {
        boolean flag = contractPerforMapper.selectList(new LambdaQueryWrapperX<ContractPerformanceDO>()
                .eq(ContractPerformanceDO::getContractId, contractId)).size() > 0;
        if (flag) {
            throw exception(ErrorCodeConstants.PERFORMANCE_EXIST);
        }
    }

    @Override
    public String setContractStatusName(Integer status) {
        String name;
        ContractPerfEnums instance = ContractPerfEnums.getInstance(status);
        if (ObjectUtil.isNull(instance)) {
            throw exception(ErrorCodeConstants.DIY_ERROR, "履约状态码不正确");
        }
        switch (instance) {
            case WAIT_CREATE_PERFORMANCE:
                name = ContractPerfEnums.WAIT_CREATE_PERFORMANCE.getDesc();
                break;
            case WAIT_PERFORMANCE:
                name = ContractPerfEnums.WAIT_PERFORMANCE.getDesc();
                break;
            case IN_PERFORMANCE:
                name = ContractPerfEnums.IN_PERFORMANCE.getDesc();
                break;
            case PERFORMANCE_FINISH:
                name = ContractPerfEnums.PERFORMANCE_FINISH.getDesc();
                break;
            case PERFORMANCE_PAUSE:
                name = ContractPerfEnums.PERFORMANCE_PAUSE.getDesc();
                break;
            case PERFORMANCE_END:
                name = ContractPerfEnums.PERFORMANCE_END.getDesc();
                break;
            case PERFORMANCE_OVER_TIME:
                name = ContractPerfEnums.PERFORMANCE_OVER_TIME.getDesc();
                break;
            case OVER_TIME_PAUSE:
                name = ContractPerfEnums.OVER_TIME_PAUSE.getDesc();
                break;
            case OVER_TIME_END:
                name = ContractPerfEnums.OVER_TIME_END.getDesc();
                break;
            case OVER_TIME_FINISH:
                name = ContractPerfEnums.OVER_TIME_FINISH.getDesc();
                break;
            case TERMINAT_APPROVE:
                name = ContractPerfEnums.TERMINAT_APPROVE.getDesc();
                break;
            case CONTRACT_PAUSE:
                name = ContractPerfEnums.CONTRACT_PAUSE.getDesc();
                break;
            case TERMINATE_SIGNIND:
                name = ContractPerfEnums.TERMINATE_SIGNIND.getDesc();
                break;
            case TERMINATED:
                name = ContractPerfEnums.TERMINATED.getDesc();
                break;
            case FREEZED:
                name = ContractPerfEnums.FREEZED.getDesc();
                break;
            case FREEZE_SIGNIND:
                name = ContractPerfEnums.FREEZE_SIGNIND.getDesc();
                break;
            default:
                throw new IllegalArgumentException("履约状态码不正确");
        }
        return name;

    }

    @Override
    public BigContractPerformV2RespVO list(ContractPerformV2ReqVO reqVO) {
        BigContractPerformV2RespVO result = new BigContractPerformV2RespVO().setPageResult(new PageResult<ContractPerformV2RespVO>().setTotal(0L));
        List<Integer> statusList = new ArrayList<Integer>();
        if (Objects.equals(ContractStatusEnums.PERFORMANCE_RISK.getCode(), reqVO.getStatus())) {
            statusList.add(ContractStatusEnums.PERFORMANCE_RISK.getCode());
            statusList.add(ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode());
            statusList.add(ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode());
            statusList.add(ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode());
            statusList.add(PERFORMANCE_RISK_OVERDUE.getCode());
        }
        //先找到履约截止时间
        List<String> filteredContractIds = new ArrayList<String>();
        PageResult<ContractDO> contractPerformanceDOPageResult = contractMapper.select4Performance(reqVO, filteredContractIds, statusList);
        PageResult<ContractPerformV2RespVO> pageResult = ContractConverter.INSTANCE.page2RespVO(contractPerformanceDOPageResult);
        result = enhanceStatistics(pageResult, reqVO);
        if (CollectionUtil.isEmpty(pageResult.getList())) {
            return result;
        }
        List<String> contractIds = pageResult.getList().stream().map(ContractPerformV2RespVO::getId).collect(Collectors.toList());
        //查询合同类型
        List<String> contractTypeIds = pageResult.getList().stream().map(ContractPerformV2RespVO::getContractType).collect(Collectors.toList());
        Map<String, ContractType> contractTypeMap = new HashMap<>();
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().inIfPresent(ContractType::getId, contractTypeIds));
        if (CollectionUtil.isNotEmpty(contractTypes)) {
            contractTypeMap = CollectionUtils.convertMap(contractTypes, ContractType::getId);
        }
        List<PaymentScheduleDO> scheduleDOList = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, contractIds);
        Map<String, List<PaymentScheduleDO>> paymentScheduleDOMap = new HashMap<String, List<PaymentScheduleDO>>();
        //最早的未履约计划
        Map<String, PaymentScheduleDO> earliestToDoPlanMap = new HashMap<String, PaymentScheduleDO>();
        //合同相关的最晚的计划
        Map<String, PaymentScheduleDO> lastToDoPlanMap = new HashMap<String, PaymentScheduleDO>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (CollectionUtil.isNotEmpty(scheduleDOList)) {
            paymentScheduleDOMap = CollectionUtils.convertMultiMap(scheduleDOList, PaymentScheduleDO::getContractId);
            // 筛选未履约和履约中的数据
            earliestToDoPlanMap = scheduleDOList.stream()
                    .filter(schedule -> Objects.equals(schedule.getStatus(), PaymentScheduleStatusEnums.UNPAID.getCode()) || Objects.equals(schedule.getStatus(), PaymentScheduleStatusEnums.PAYED.getCode()))
                    .collect(Collectors.groupingBy(PaymentScheduleDO::getContractId))
                    .entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().stream()
//                                    .min(Comparator.comparing(PaymentScheduleDO::getPaymentTime))
//                                    .orElse(null)
                                    .min(Comparator.comparing(schedule -> {
                                        if (ObjectUtil.isNull(schedule)) {
                                            return null;
                                        }
                                        // 如果 getPaymentTime 为 null，尝试使用 paymentDate
                                        if (schedule.getPaymentTime() != null) {
                                            return schedule.getPaymentTime();
                                        } else {
                                            try {
                                                // 如果 paymentDate 为 null，返回一个默认值
                                                String paymentDate = schedule.getPaymentDate();
                                                if (paymentDate != null && !paymentDate.isEmpty()) {
                                                    return sdf.parse(paymentDate);
                                                }
                                            } catch (Exception e) {
                                                // 处理日期格式解析异常，如果解析失败则返回 null
                                                e.printStackTrace();
                                            }
                                        }
                                        return null; // 如果都为空，则返回 null
                                    }))
                                    .orElse(null)
                    ));
            lastToDoPlanMap = scheduleDOList.stream()
                    .collect(Collectors.groupingBy(PaymentScheduleDO::getContractId))
                    .entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().stream()
//                                    .min(Comparator.comparing(PaymentScheduleDO::getPaymentTime))
//                                    .orElse(null)
                                    .min(Comparator.comparing(schedule -> {
                                        if (ObjectUtil.isNull(schedule)) {
                                            return null;
                                        }
                                        //计划时间判断
                                        // 如果 getPaymentTime 为 null，尝试使用 paymentDate
                                        if (schedule.getPaymentTime() != null) {
                                            return schedule.getPaymentTime();
                                        } else {
                                            try {
                                                // 如果 paymentDate 为 null，返回一个默认值
                                                String paymentDate = schedule.getPaymentDate();
                                                if (paymentDate != null && !paymentDate.isEmpty()) {

                                                    return sdf.parse(paymentDate);
                                                }
                                            } catch (Exception e) {
                                                // 处理日期格式解析异常，如果解析失败则返回 null
                                                e.printStackTrace();
                                            }
                                        }
                                        return null; // 如果都为空，则返回 null
                                    }))
                                    .orElse(null)
                    ));

        }
        //查询履约任务信息
        //设置合同类型
        for (ContractPerformV2RespVO respVO : pageResult.getList()) {
            ContractType contractType = contractTypeMap.get(respVO.getContractType());
            //合同状态
            ContractStatusEnums statusEnums = ContractStatusEnums.getInstance(respVO.getStatus());
            if (ObjectUtil.isNotNull(statusEnums)) {
                respVO.setStatusName(statusEnums.getDesc());
            }
            //合同类型
            if (ObjectUtil.isNotNull(contractType)) {
                respVO.setContractTypeName(contractType.getName());
            }
            //履约截止时间(合同最晚的待支付计划)
            PaymentScheduleDO lastPlan = lastToDoPlanMap.get(respVO.getId());
            if (ObjectUtil.isNotNull(lastPlan)) {
                LocalDateTime localDateTime = getRealTime4Plan(lastPlan);
                if(ObjectUtil.isNotNull(localDateTime)){
                Date paymentTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());;
                String format = sdf.format(paymentTime)+" 23:59:59";
                respVO.setPerfTime(DateUtil.parse(format));
                }
            }
            //设置履约完成状况
            List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleDOMap.get(respVO.getId());
            if (CollectionUtil.isNotEmpty(paymentScheduleDOList)) {
                respVO.setFinishInfo(getFinishInfoV2(paymentScheduleDOList));
            }
            //履约中
            if (Objects.equals(ContractStatusEnums.PERFORMING.getCode(), reqVO.getStatus())) {
                enhancePerforming(respVO, earliestToDoPlanMap);
            }
            //履约风险
            if (CollectionUtil.isNotEmpty(scheduleDOList)) {
                if (Objects.equals(ContractStatusEnums.PERFORMANCE_RISK.getCode(), respVO.getStatus())
                        || Objects.equals(PERFORMANCE_RISK_OVERDUE.getCode(), respVO.getStatus())
                        || Objects.equals(ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode(), respVO.getStatus())
                        || Objects.equals(ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode(), respVO.getStatus())
                        || Objects.equals(ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(), respVO.getStatus())
                ) {
                    enhancePerformRisk(respVO, reqVO, earliestToDoPlanMap);
                }
                PaymentScheduleDO paymentScheduleDO = earliestToDoPlanMap.get(respVO.getId());
                respVO.setAmount(getRealAmount4Plan(paymentScheduleDO));
            }
        }
        result.setPageResult(pageResult);
        return result;
    }

    /**
     * 统计赋值
     * */
    private BigContractPerformV2RespVO enhanceStatistics(PageResult<ContractPerformV2RespVO> pageResult, ContractPerformV2ReqVO reqVO) {
        BigContractPerformV2RespVO result = new BigContractPerformV2RespVO();
        ContractPerformV2CountRespVO v2CountRespVO = new ContractPerformV2CountRespVO();
        ContractStatusEnums statusEnums = ContractStatusEnums.getInstance(reqVO.getStatus());
        Long signCompleted = 0L;
        Long performing = 0L;
        Long performanceRisk = 0L;
        Long performanceComplete = 0L;
        Long performanceClosure = 0L;
        List<Integer> statusList = new ArrayList<Integer>();
        statusList.add(ContractStatusEnums.PERFORMANCE_RISK.getCode());
        statusList.add(ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode());
        statusList.add(ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode());
        statusList.add(ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode());
        statusList.add(PERFORMANCE_RISK_OVERDUE.getCode());
        if (statusEnums != SIGN_COMPLETED) {
            //待发送列表不查upload=3或4的
            List<Integer> uploadTypeList=new ArrayList<>();
            uploadTypeList.add(ContractUploadTypeEnums.ORDER_DRAFT.getCode());
            uploadTypeList.add(ContractUploadTypeEnums.THIRD_PARTY.getCode());
            signCompleted = contractMapper.selectCount(new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getStatus, SIGN_COMPLETED.getCode()).notIn(ContractDO::getUpload,uploadTypeList));

        } else {
            signCompleted = pageResult.getTotal();
        }

        if (statusEnums != PERFORMING) {
            performing = contractMapper.selectCount(new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getStatus, PERFORMING.getCode()));
        } else {
            performing = pageResult.getTotal();
        }

        if (statusEnums != PERFORMANCE_RISK) {
            performanceRisk = contractMapper.selectCount(new LambdaQueryWrapperX<ContractDO>().in(ContractDO::getStatus, statusList));
        } else {
            performanceRisk = pageResult.getTotal();
        }

        if (statusEnums != PERFORMANCE_COMPLETE) {
            performanceComplete = contractMapper.selectCount(new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getStatus, PERFORMANCE_COMPLETE.getCode()));
        } else {
            performanceComplete = pageResult.getTotal();
        }

        if (statusEnums != PERFORMANCE_CLOSURE) {
            performanceClosure = contractMapper.selectCount(new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getStatus, PERFORMANCE_CLOSURE.getCode()));
        } else {
            performanceClosure = pageResult.getTotal();
        }

        v2CountRespVO.setSignCompleted(signCompleted)
                .setPerforming(performing)
                .setPerformanceRisk(performanceRisk)
                .setPerformanceComplete(performanceComplete)
                .setPerformanceClosure(performanceClosure)
        ;

        return result.setV2CountRespVO(v2CountRespVO);
    }


    /**
     * 若距离当前合同履约中的收款/付款/验收日期，还有7天开始，卡片中增加显示文本：履约截止时间还剩N天     N为剩余时间
     */
    private void enhancePerforming(ContractPerformV2RespVO respVO, Map<String, PaymentScheduleDO> earliestToDoPlanMap) {
        // 算出距离天数
        LocalDateTime today = LocalDateTime.now();
        PaymentScheduleDO paymentScheduleDO = earliestToDoPlanMap.get(respVO.getId());
        if (ObjectUtil.isNotNull(paymentScheduleDO)) {
            LocalDateTime paymentDateTime = getRealTime4Plan(paymentScheduleDO);
            // 计算天数差
            Long daysDifference = Math.abs(ChronoUnit.DAYS.between(today, paymentDateTime));
            //履约中也存在超期情况
            respVO.setRemarkDays(daysDifference.intValue());
            respVO.setSort(paymentScheduleDO.getSort());
//            if (CARD_DAYS >= daysDifference.intValue()) {
//                respVO.setRemarkDays(daysDifference.intValue());
//                respVO.setSort(paymentScheduleDO.getSort());
//            }
        }
    }


    /**
     * 逾期风险：若当前日期，已超过合同履约中的收款/付款/验收日期，卡片中增加显示文本：已逾期N天     N为逾期时间
     * 履约暂停：当前日期，已超过合同履约中的收款/付款/验收日期，卡片中增加显示文本：已暂停N天     N为暂停时间
     * 履约延期：当前日期，已超过合同履约中的收款/付款/验收日期，卡片中增加显示文本：已延期N天     N为延期时间
     */
    private void enhancePerformRisk(ContractPerformV2RespVO respVO, ContractPerformV2ReqVO reqVO, Map<String, PaymentScheduleDO> earliestToDoPlanMap) {
        ContractStatusEnums enums = ContractStatusEnums.getInstance(respVO.getStatus());
        if (ObjectUtil.isNotNull(enums)) {
            switch (enums) {
                // 逾期风险：若当前日期，已超过合同履约中的收款/付款/验收日期，卡片中增加显示文本：已逾期N天     N为逾期时间
                case PERFORMANCE_RISK_OVERDUE:
                    // 履约延期：当前日期，已超过合同履约中的收款/付款/验收日期，卡片中增加显示文本：已延期N天     N为延期时间
                case PERFORMANCE_RISK_EXTENSION:
                    // 算出距离天数
                    respVO.setRemarkDays(getDayDistance(earliestToDoPlanMap, respVO));
                    // 履约暂停：当前日期，已超过合同履约中的收款/付款/验收日期，卡片中增加显示文本：已暂停N天     N为暂停时间
                    break;
                case PERFORMANCE_RISK_PAUSE:
                    LocalDateTime localDateTime1 = respVO.getPauseDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    long daysDifference = Math.abs(ChronoUnit.DAYS.between(LocalDateTime.now(), localDateTime1));
                    respVO.setRemarkDays((int) daysDifference);
                    break;
                case PERFORMING:
                    // 算出距离天数
                    respVO.setRemarkDays(getDayDistance(earliestToDoPlanMap, respVO));
                    break;
                default:
                    break;
            }

        }

    }


    private Integer getDayDistance(Map<String, PaymentScheduleDO> earliestToDoPlanMap, ContractPerformV2RespVO respVO) {
        Long daysDifference = 0L;
        // 算出距离天数
        LocalDateTime today = LocalDateTime.now();
        PaymentScheduleDO paymentScheduleDO = earliestToDoPlanMap.get(respVO.getId());
        if (ObjectUtil.isNotNull(paymentScheduleDO)) {
            LocalDateTime paymentDateTime = getRealTime4Plan(paymentScheduleDO);
            // 计算天数差
            daysDifference = Math.abs(ChronoUnit.DAYS.between(paymentDateTime, today));
            //支付期数
            respVO.setSort(paymentScheduleDO.getSort());
        }
        return daysDifference.intValue();
    }

    private String getFinishInfoV2(List<PaymentScheduleDO> paymentScheduleDOList) {
        Integer sum = paymentScheduleDOList.size();
        Integer finished = (int) paymentScheduleDOList.stream()
                .filter(paymentScheduleDO -> Objects.equals(paymentScheduleDO.getStatus(), PaymentScheduleStatusEnums.DONE.getCode()))
                .count();
        String result = finished + "/" + sum;
        return result;
    }

    @Override
    public String save(PerformV2SaveReqVO reqVO) {
        PaymentScheduleDO paymentScheduleDO = PaymentApplicationConverter.INSTANCE.r2D(reqVO);
        paymentScheduleMapper.insert(paymentScheduleDO);
        return paymentScheduleDO.getId();
    }

    @Override
    public PerformQueryRespVO queryById(String id) {
        PaymentScheduleDO paymentScheduleDO = paymentScheduleMapper.selectById(id);
        if (ObjectUtil.isNull(paymentScheduleDO)) {
            throw exception(SYSTEM_ERROR, "该履约计划不存在");
        }
        PerformQueryRespVO respVO = PerformanceConverter.INSTANCE.d2R(paymentScheduleDO);

        return null;
    }
    //计划日期校验
    private LocalDateTime getRealTime4Plan(PaymentScheduleDO paymentScheduleDO ){
        if (ObjectUtil.isNotNull(paymentScheduleDO)) {
            //计划日期校验
            LocalDateTime paymentDateTime = null;
            if(ObjectUtil.isNotNull(paymentScheduleDO.getPaymentTime())){
                paymentDateTime = paymentScheduleDO.getPaymentTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
            }else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try{
                    paymentDateTime = sdf.parse(paymentScheduleDO.getPaymentDate()).toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                }catch (Exception e){
                    log.error(e.getMessage());
                    e.printStackTrace();
                    throw exception(DIY_ERROR,"日期转换异常");
                }
            }
            return paymentDateTime;

        }
        return null;
    }

    private BigDecimal getRealAmount4Plan(PaymentScheduleDO paymentScheduleDO){
        if(ObjectUtil.isNotNull(paymentScheduleDO)){
            //计划金额判断
            if(ObjectUtil.isNull(paymentScheduleDO.getAmount())){
                return new BigDecimal(String.valueOf(paymentScheduleDO.getStagePaymentAmount())).setScale(2, RoundingMode.HALF_UP);
            }else {
                return paymentScheduleDO.getAmount();
            }
        }
        return BigDecimal.ZERO;

    }

}
