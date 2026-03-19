package com.yaoan.module.econtract.service.workbench;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.date.LocalDateTimeUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.econtract.controller.admin.amount.vo.ContractAmountRespVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.patrol.vo.SignInfoDTO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.alert.WorkBenchMsgAlertRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.fastdraft.FastViaModelRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.statistic.*;
import com.yaoan.module.econtract.controller.admin.workbench.vo.task.BigWorkBenchTaskRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.task.WorkBenchTaskCountRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.task.WorkBenchTaskListRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.task.WorkBenchTaskReqVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.trend.TrendRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.trend.WorkBenchSignTrendRespVO;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contractInvoiceManage.ContractInvoiceManageDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ModelCategory;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.dataobject.signet.BpmContractSignetDO;
import com.yaoan.module.econtract.dal.dataobject.signet.ContractSignetDO;
import com.yaoan.module.econtract.dal.mysql.bpm.archive.BpmContractArchivesMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contract.BpmContractMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contractborrow.ContractBorrowBpmMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.contractinvoicemanage.ContractInvoiceManageMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.deferred.PaymentDeferredApplyMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.modelcategory.ModelCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplicationMapper;
import com.yaoan.module.econtract.dal.mysql.signet.BpmContractSignetMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import com.yaoan.module.econtract.enums.templatecategory.RootTemplateCategoryEnums;
import liquibase.pro.packaged.L;
import liquibase.repackaged.org.apache.commons.lang3.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.module.econtract.enums.ActivityConfigurationEnum.*;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/4 11:12
 */
@Slf4j
@Service
public class WorkBenchServiceImpl implements WorkBenchService {
    public final static String TREND_START_DATE = "start_date";
    public final static String TREND_END_DATE = "end_date";
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractOrderExtMapper contractOrderExtMapper;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private PaymentApplicationMapper paymentApplicationMapper;
    @Resource
    private PaymentDeferredApplyMapper paymentDeferredApplyMapper;
    @Resource
    private ContractInvoiceManageMapper contractInvoiceManageMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private BpmContractArchivesMapper bpmContractArchivesMapper;
    @Resource
    private ContractBorrowBpmMapper borrowBpmMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ModelCategoryMapper modelCategoryMapper;
    @Resource
    private BpmContractSignetMapper bpmContractSignetMapper;

    /**
     * @param size
     * @return {@link List }<{@link FastViaModelRespVO }>
     */
    @Override
    public List<FastViaModelRespVO> draftFastViaModel(Integer size) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        //当前人使用最多的三个模板
        List<FastViaModelRespVO> firstPart = new ArrayList<FastViaModelRespVO>();
        List<ContractDO> modelList = contractMapper.selectUsedMost(loginUserId, size);
        if (CollectionUtil.isNotEmpty(modelList)) {
            List<String> modelIds = modelList.stream().map(ContractDO::getTemplateId).filter(Objects::nonNull).collect(Collectors.toList());
            List<Model> models = modelMapper.selectList(Model::getId, modelIds);
            Map<String, Model> modelMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(models)) {
                modelMap = CollectionUtils.convertMap(models, Model::getId);
            }
            for (ContractDO contractDO : modelList) {

                Model model = modelMap.get(contractDO.getTemplateId());
                if (ObjectUtil.isNotNull(model)) {
                    FastViaModelRespVO item = new FastViaModelRespVO().setId(contractDO.getTemplateId());
                    item.setName(model.getName());
                    firstPart.add(item);
                }

            }
        }
        //最新创建的模板
        List<FastViaModelRespVO> secondPart = new ArrayList<FastViaModelRespVO>();
        List<Model> modelList2 = modelMapper.selectlatest(size);
        if (CollectionUtil.isNotEmpty(modelList2)) {
            for (Model model : modelList2) {
                secondPart.add(new FastViaModelRespVO().setId(model.getId()).setName(model.getName()));
            }
        }
        firstPart.addAll(secondPart);

        return firstPart.subList(0, size);
    }

    private StatisticMoneyRespVO enhanceMoney(List<ContractDO> contractDOList, List<ContractDO> govContracts) {
        /**
         * 总合同 （ 已签订）
         */
        // 查询全部合同相关的全部履约计划
        List<String> contractIds = contractDOList.stream().map(ContractDO::getId).collect(Collectors.toList());
        List<PaymentScheduleDO> totalPaymentScheduleDOS = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, contractIds);
        totalPaymentScheduleDOS.forEach(item->{
            if (ObjectUtil.isNotEmpty(item.getStagePaymentAmount())){
                item.setAmount(BigDecimal.valueOf(item.getStagePaymentAmount()));
            }
        });
        // 筛选出付款类型的全部履约的金额
        BigDecimal sumContractMoney = totalPaymentScheduleDOS.stream().filter(item -> AmountTypeEnums.PAY.getCode().equals(item.getAmountType())).map(PaymentScheduleDO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
//        Double totalAmount = contractDOList.stream().map(ContractDO::getAmount).filter(Objects::nonNull)  // 过滤掉 null 值
//                .mapToDouble(Double::doubleValue).sum();
//        List<PaymentScheduleDO> totalPaymentScheduleDOS = new ArrayList<PaymentScheduleDO>();
//        totalPaymentScheduleDOS = paymentScheduleMapper.select4TotalBench();
//        BigDecimal sumContractMoney = new BigDecimal(String.valueOf(totalAmount)).setScale(2, RoundingMode.HALF_UP);


        BigDecimal totalPaidMoney = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalUnpaidMoney = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        if (CollectionUtil.isNotEmpty(totalPaymentScheduleDOS)) {
            // 筛选出全部已付款的履约金额
            totalPaidMoney = totalPaymentScheduleDOS.stream().filter(schedule -> (Objects.equals(PaymentScheduleStatusEnums.DONE.getCode(), schedule.getStatus()) || Objects.equals(PaymentScheduleStatusEnums.CLOSE.getCode(), schedule.getStatus())) && ObjectUtil.isNotNull(schedule.getPaidAmount()) && AmountTypeEnums.PAY.getCode().equals(schedule.getAmountType())).map(PaymentScheduleDO::getPaidAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        // 计算出全部未付款的履约金额
        totalUnpaidMoney = sumContractMoney.subtract(totalPaidMoney).setScale(2, RoundingMode.HALF_UP);

        /**
         * 政府采购类 （已签订）
         */
        GovMoneyRespVO govMoneyRespVO = new GovMoneyRespVO();
        BigDecimal totalGovernmentPurchaseMoney = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        // 根据政府采购类型合同筛选出全部履约计划
        List<String> govContractIds = govContracts.stream().map(ContractDO::getId).collect(Collectors.toList());
        List<PaymentScheduleDO> scheduleDOList = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, govContractIds);
        totalGovernmentPurchaseMoney = scheduleDOList.stream().filter(item -> AmountTypeEnums.PAY.getCode().equals(item.getAmountType())).map(PaymentScheduleDO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);

        BigDecimal governmentPaidMoney = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal governmentUnpaidMoney = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        if (CollectionUtil.isNotEmpty(scheduleDOList)) {
            governmentPaidMoney = scheduleDOList.stream().filter(schedule -> (Objects.equals(PaymentScheduleStatusEnums.DONE.getCode(), schedule.getStatus()) || Objects.equals(PaymentScheduleStatusEnums.CLOSE.getCode(), schedule.getStatus()))  && ObjectUtil.isNotNull(schedule.getPaidAmount())).map(PaymentScheduleDO::getPaidAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        governmentUnpaidMoney = totalGovernmentPurchaseMoney.subtract(governmentPaidMoney).setScale(2, RoundingMode.HALF_UP);
        govMoneyRespVO.setGovernmentPurchaseMoney(totalGovernmentPurchaseMoney).setGovernmentPayedMoney(governmentPaidMoney).setGovernmentUnpaidMoney(governmentUnpaidMoney);

        /**
         * 非政府采购类（已签订）
         */
        NonGovMoneyRespVO nonGovMoneyRespVO = new NonGovMoneyRespVO();
        BigDecimal totalNonGovernmentPurchaseMoney = sumContractMoney.subtract(totalGovernmentPurchaseMoney).setScale(2, RoundingMode.HALF_UP);
        BigDecimal nonGovernmentPaidMoney = totalPaidMoney.subtract(governmentPaidMoney).setScale(2, RoundingMode.HALF_UP);
        BigDecimal nonGovernmentUnpaidMoney = totalNonGovernmentPurchaseMoney.subtract(nonGovernmentPaidMoney).setScale(2, RoundingMode.HALF_UP);
        nonGovMoneyRespVO.setNonGovernmentPurchaseMoney(totalNonGovernmentPurchaseMoney).setNonGovernmentPayedMoney(nonGovernmentPaidMoney).setNonGovernmentUnpaidMoney(nonGovernmentUnpaidMoney);

        return new StatisticMoneyRespVO().setSumContractMoney(sumContractMoney).setGovMoneyRespVO(govMoneyRespVO).setNonGovMoneyRespVO(nonGovMoneyRespVO);
    }

    private StatisticRecMoneyRespVO enhanceRecMoney(List<ContractDO> contractDOList) {
        StatisticRecMoneyRespVO ret = new StatisticRecMoneyRespVO();
        List<String> contractIds = contractDOList.stream().map(ContractDO::getId).collect(Collectors.toList());
        List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, contractIds);
        BigDecimal sumAmt = paymentScheduleDOS.stream().filter(item -> AmountTypeEnums.RECEIPT.getCode().equals(item.getAmountType())).map(PaymentScheduleDO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        ret.setSumContractRecMoney(sumAmt);
        // 已确认收款的金额
        BigDecimal cfmAmt = paymentScheduleDOS.stream().filter(item -> PaymentScheduleStatusEnums.DONE.getCode().equals(item.getStatus()) || PaymentScheduleStatusEnums.CLOSE.getCode().equals(item.getStatus())).map(PaymentScheduleDO::getPaidAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        ret.setContractRecMoney(cfmAmt.setScale(2, RoundingMode.HALF_UP)).setContractUnRecMoney(ret.getSumContractRecMoney().subtract(cfmAmt));
        return ret;
    }

    private StatisticCountRespVO enhanceCount(List<ContractDO> contractDOList, List<ContractDO> govContracts) {
        /**
         * 总合同 （ 已签订）
         */
        long sumContract = 0L;
        sumContract = contractDOList.size();
        /**
         * 政府采购类 （已签订）
         */
        long governmentPurchase = 0L;
        if (CollectionUtil.isNotEmpty(govContracts)) {
            governmentPurchase = govContracts.size();
        }
        /**
         * 非政府采购类（已签订）
         */
        long nonGovernmentPurchase = 0L;
        nonGovernmentPurchase = sumContract - governmentPurchase;
        return new StatisticCountRespVO().setSumContract(sumContract).setGovernmentPurchase(governmentPurchase).setNonGovernmentPurchase(nonGovernmentPurchase);
    }

    /**
     * 单位端_工作台_统计待办任务数量
     *
     * @return {@link WorkBenchTaskCountRespVO }
     */
    @Override
    public WorkBenchTaskCountRespVO countToDoTaskList() {

        //合同审批
        Long approveCount = 0L;
        List<ContractProcessInstanceRelationInfoRespDTO> draftInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(CONTRACT_DRAFT_APPROVE.getDefinitionKey());
        approveCount = contractMapper.count4Bench(draftInstanceRelationInfoRespDTOList.stream().map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId).collect(Collectors.toList()));

        //合同签订
        Long signCount = 0L;
        List<ContractProcessInstanceRelationInfoRespDTO> bothInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ECMS_CONTRACT_BOTH.getDefinitionKey());
        signCount = contractMapper.countSign4Bench(bothInstanceRelationInfoRespDTOList.stream().map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId).collect(Collectors.toList()));

        //付款审批
        Long paymentCount = 0L;
        List<ContractProcessInstanceRelationInfoRespDTO> paymentInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(PAYMENT_APPLICATION_APPROVE.getDefinitionKey());
        paymentCount = paymentApplicationMapper.count4Bench(paymentInstanceRelationInfoRespDTOList.stream().map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId).collect(Collectors.toList()));

        //延期计划审批
//        Long deferredCount = 0L;
//        List<ContractProcessInstanceRelationInfoRespDTO> deferredInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(PAYMENT_PLAN_DEFERRED_APPLICATION.getDefinitionKey());
//        deferredCount = paymentDeferredApplyMapper.count4Bench(deferredInstanceRelationInfoRespDTOList.stream().map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId).collect(Collectors.toList()));

        //收款审批
        Long invoiceCount = 0L;
        List<ContractProcessInstanceRelationInfoRespDTO> invoiceInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ECMS_CONTRACT_INVOICE.getDefinitionKey());
        invoiceCount = contractInvoiceManageMapper.count4Bench(invoiceInstanceRelationInfoRespDTOList.stream().map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId).collect(Collectors.toList()));

        //归档审批
//        Long archiveCount = 0L;
//        List<ContractProcessInstanceRelationInfoRespDTO> archiveInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(CONTRACT_ARCHIVE_APPLY.getDefinitionKey());
//        archiveCount = bpmContractArchivesMapper.count4Bench(archiveInstanceRelationInfoRespDTOList.stream().map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId).collect(Collectors.toList()));

        //借阅审批
//        Long borrowCount = 0L;
//        List<ContractProcessInstanceRelationInfoRespDTO> borrowInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(CONTRACT_BORROW_SUBMIT_APPROVE.getDefinitionKey());
//        borrowCount = borrowBpmMapper.count4Bench(borrowInstanceRelationInfoRespDTOList.stream().map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId).collect(Collectors.toList()));

        WorkBenchTaskCountRespVO countRespVO = new WorkBenchTaskCountRespVO().setApproveCount(approveCount).setSignCount(signCount).setPaymentCount(paymentCount).setInvoiceCount(invoiceCount);
        return countRespVO;
    }

    /**
     * 单位端_工作台_审批列表
     *
     * @param reqVO
     * @return {@link BigWorkBenchTaskRespVO }
     */
    @Override
    public PageResult<WorkBenchTaskListRespVO> toDoTaskList(WorkBenchTaskReqVO reqVO) {
        //列表
        PageResult<WorkBenchTaskListRespVO> respVOList = new PageResult<>();
        ActivityConfigurationEnum activityConfigurationEnum = ActivityConfigurationEnum.getInstanceByDefKey(reqVO.getProcessDefKey());
        if (ObjectUtil.isNotNull(activityConfigurationEnum)) {
            List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(activityConfigurationEnum.getDefinitionKey());
            Map<String, ContractProcessInstanceRelationInfoRespDTO> toDoTaskMap = new HashMap<>();
            List<String> instanceIds = processInstanceRelationInfoRespDTOList.stream().map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(processInstanceRelationInfoRespDTOList)) {
                toDoTaskMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
            }
            switch (activityConfigurationEnum) {
                case CONTRACT_DRAFT_APPROVE:
                    respVOList = listDraft(instanceIds, toDoTaskMap, reqVO);
                    break;
                case ECMS_CONTRACT_BOTH:
                    respVOList = listSign(instanceIds, toDoTaskMap, reqVO);

                    break;
                case PAYMENT_APPLICATION_APPROVE:
                    respVOList = listPayment(instanceIds, toDoTaskMap, reqVO);

                    break;
                case PAYMENT_PLAN_DEFERRED_APPLICATION:
                    respVOList = listDeferred(instanceIds, toDoTaskMap, reqVO);

                    break;
                case ECMS_CONTRACT_INVOICE:
                    respVOList = listInvoice(instanceIds, toDoTaskMap, reqVO);

                    break;
                case SEAL_APPLICATION_APPROVE:
                    respVOList = listSeal(instanceIds, toDoTaskMap, reqVO);

                    break;
//                case CONTRACT_ARCHIVE_APPLY:
//                    respVOList = listArchive(instanceIds, toDoTaskMap, reqVO);
//
//                    break;
//                case CONTRACT_BORROW_SUBMIT_APPROVE:
//                    respVOList = listBorrow(instanceIds, toDoTaskMap, reqVO);

//                    break;

                default:
                    break;
            }
        }

        return respVOList;
    }

    private PageResult<WorkBenchTaskListRespVO> listSeal(List<String> instanceIds, Map<String, ContractProcessInstanceRelationInfoRespDTO> toDoTaskMap, WorkBenchTaskReqVO reqVO) {
        reqVO.setInstanceIdList(instanceIds);
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectSealPage(reqVO);
        PageResult<WorkBenchTaskListRespVO> pageResult = ContractConverter.INSTANCE.convert2BenchDraft(contractDOPageResult);
        if (CollectionUtil.isNotEmpty(pageResult.getList())) {
            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            Map<String, ContractType> contractTypeMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(contractTypeList)) {
                contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            }
            List<String> contractIdList = contractDOPageResult.getList().stream().map(ContractDO::getId).collect(Collectors.toList());
            List<BpmContractSignetDO> signetDOList = bpmContractSignetMapper.selectList(
                    new LambdaQueryWrapperX<BpmContractSignetDO>()
                            .in(BpmContractSignetDO::getBusinessId, contractIdList)
            );
            Map<String,BpmContractSignetDO> signetDOMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(contractDOPageResult.getList())){
                signetDOMap = CollectionUtils.convertMap(signetDOList, BpmContractSignetDO::getBusinessId);
            }
            for (WorkBenchTaskListRespVO respVO : pageResult.getList()) {
                ContractType contractType = contractTypeMap.get(respVO.getContractType());
                if (ObjectUtil.isNotNull(contractType)) {
                    respVO.setContractTypeName(contractType.getName());
                }
                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(infoRespDTO)) {
                    respVO.setTaskId(infoRespDTO.getTaskId());
                }
                BpmContractSignetDO signetDO = signetDOMap.get(respVO.getContractId());
                if(ObjectUtil.isNotNull(signetDO)){
                    respVO.setBpmContractSignetId(signetDO.getId());
                }
            }
        }
        return pageResult;

    }

    private PageResult<WorkBenchTaskListRespVO> listBorrow(List<String> instanceIds, Map<String, ContractProcessInstanceRelationInfoRespDTO> toDoTaskMap, WorkBenchTaskReqVO reqVO) {
        reqVO.setInstanceIdList(instanceIds);
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectBorrowPage4Bench(reqVO);
        PageResult<WorkBenchTaskListRespVO> pageResult = ContractConverter.INSTANCE.convert2BenchDraft(contractDOPageResult);
        if (CollectionUtil.isNotEmpty(pageResult.getList())) {
            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            Map<String, ContractType> contractTypeMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(contractTypeList)) {
                contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            }
            for (WorkBenchTaskListRespVO respVO : pageResult.getList()) {
                ContractType contractType = contractTypeMap.get(respVO.getContractType());
                if (ObjectUtil.isNotNull(contractType)) {
                    respVO.setContractTypeName(contractType.getName());
                }
                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(infoRespDTO)) {
                    respVO.setTaskId(infoRespDTO.getTaskId());
                }
            }
        }
        return pageResult;
    }

    private PageResult<WorkBenchTaskListRespVO> listArchive(List<String> instanceIds, Map<String, ContractProcessInstanceRelationInfoRespDTO> toDoTaskMap, WorkBenchTaskReqVO reqVO) {
        reqVO.setInstanceIdList(instanceIds);
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectArchivePage4Bench(reqVO);
        PageResult<WorkBenchTaskListRespVO> pageResult = ContractConverter.INSTANCE.convert2BenchDraft(contractDOPageResult);
        if (CollectionUtil.isNotEmpty(pageResult.getList())) {
            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            Map<String, ContractType> contractTypeMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(contractTypeList)) {
                contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            }
            for (WorkBenchTaskListRespVO respVO : pageResult.getList()) {
                ContractType contractType = contractTypeMap.get(respVO.getContractType());
                if (ObjectUtil.isNotNull(contractType)) {
                    respVO.setContractTypeName(contractType.getName());
                }
                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(infoRespDTO)) {
                    respVO.setTaskId(infoRespDTO.getTaskId());
                }
            }
        }
        return pageResult;
    }

    private PageResult<WorkBenchTaskListRespVO> listInvoice(List<String> instanceIds, Map<String, ContractProcessInstanceRelationInfoRespDTO> toDoTaskMap, WorkBenchTaskReqVO reqVO) {
        reqVO.setInstanceIdList(instanceIds);
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectInvoicePage4Bench(reqVO);
        PageResult<WorkBenchTaskListRespVO> pageResult = ContractConverter.INSTANCE.convert2BenchDraft(contractDOPageResult);
        if (CollectionUtil.isNotEmpty(pageResult.getList())) {
            List<ContractInvoiceManageDO> contractInvoiceManageDOS = contractInvoiceManageMapper.selectList(new LambdaQueryWrapperX<ContractInvoiceManageDO>().in(ContractInvoiceManageDO::getProcessInstanceId, instanceIds));
            Map<String, ContractInvoiceManageDO> contractInvoiceManageDOMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(contractInvoiceManageDOS)) {
                contractInvoiceManageDOMap = CollectionUtils.convertMap(contractInvoiceManageDOS, ContractInvoiceManageDO::getProcessInstanceId);
            }
            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            Map<String, ContractType> contractTypeMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(contractTypeList)) {
                contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            }
            for (WorkBenchTaskListRespVO respVO : pageResult.getList()) {
                ContractType contractType = contractTypeMap.get(respVO.getContractType());
                if (ObjectUtil.isNotNull(contractType)) {
                    respVO.setContractTypeName(contractType.getName());
                }
                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(infoRespDTO)) {
                    respVO.setTaskId(infoRespDTO.getTaskId());
                }
                ContractInvoiceManageDO contractInvoiceManageDO = contractInvoiceManageDOMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(contractInvoiceManageDO)) {
                    respVO.setTitle(contractInvoiceManageDO.getTitle());
                    respVO.setPayeeId(contractInvoiceManageDO.getId());
                }
            }
        }
        return pageResult;
    }

    private PageResult<WorkBenchTaskListRespVO> listDeferred(List<String> instanceIds, Map<String, ContractProcessInstanceRelationInfoRespDTO> toDoTaskMap, WorkBenchTaskReqVO reqVO) {
        reqVO.setInstanceIdList(instanceIds);
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectDeferredPage4Bench(reqVO);
        PageResult<WorkBenchTaskListRespVO> pageResult = ContractConverter.INSTANCE.convert2BenchDraft(contractDOPageResult);
        if (CollectionUtil.isNotEmpty(pageResult.getList())) {
            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            Map<String, ContractType> contractTypeMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(contractTypeList)) {
                contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            }
            for (WorkBenchTaskListRespVO respVO : pageResult.getList()) {
                ContractType contractType = contractTypeMap.get(respVO.getContractType());
                if (ObjectUtil.isNotNull(contractType)) {
                    respVO.setContractTypeName(contractType.getName());
                }
                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(infoRespDTO)) {
                    respVO.setTaskId(infoRespDTO.getTaskId());
                }
            }
        }
        return pageResult;
    }

    private PageResult<WorkBenchTaskListRespVO> listSign(List<String> instanceIds, Map<String, ContractProcessInstanceRelationInfoRespDTO> toDoTaskMap, WorkBenchTaskReqVO reqVO) {
        reqVO.setInstanceIdList(instanceIds);
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectSignPage4Bench(reqVO);
        PageResult<WorkBenchTaskListRespVO> pageResult = ContractConverter.INSTANCE.convert2BenchDraft(contractDOPageResult);
        if (CollectionUtil.isNotEmpty(pageResult.getList())) {
            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            Map<String, ContractType> contractTypeMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(contractTypeList)) {
                contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            }
            for (WorkBenchTaskListRespVO respVO : pageResult.getList()) {
                ContractType contractType = contractTypeMap.get(respVO.getContractType());
                if (ObjectUtil.isNotNull(contractType)) {
                    respVO.setContractTypeName(contractType.getName());
                }
                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(infoRespDTO)) {
                    respVO.setTaskId(infoRespDTO.getTaskId());
                }
            }
        }
        return pageResult;
    }

    private PageResult<WorkBenchTaskListRespVO> listPayment(List<String> instanceIds, Map<String, ContractProcessInstanceRelationInfoRespDTO> toDoTaskMap, WorkBenchTaskReqVO reqVO) {
        reqVO.setInstanceIdList(instanceIds);
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectPaymentPage4Bench(reqVO);
        PageResult<WorkBenchTaskListRespVO> pageResult = ContractConverter.INSTANCE.convert2BenchDraft(contractDOPageResult);
        if (CollectionUtil.isNotEmpty(pageResult.getList())) {
            List<PaymentApplicationDO> paymentApplicationDOS = paymentApplicationMapper.selectList(new LambdaQueryWrapperX<PaymentApplicationDO>().in(PaymentApplicationDO::getProcessInstanceId, instanceIds));
            Map<String, PaymentApplicationDO> paymentApplicationDOMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(paymentApplicationDOS)) {
                paymentApplicationDOMap = CollectionUtils.convertMap(paymentApplicationDOS, PaymentApplicationDO::getProcessInstanceId);
            }
            List<String> applicationIds = paymentApplicationDOS.stream().map(PaymentApplicationDO::getId).collect(Collectors.toList());
            Map<String, List<PaymentScheduleDO>> plansByContarctIdMap = new HashMap<>();
            List<PaymentScheduleDO> plans = paymentScheduleMapper.selectListByApplicationIds(applicationIds);
            if (CollectionUtil.isNotEmpty(plans)) {
                plansByContarctIdMap = plans.stream().collect(Collectors.groupingBy(PaymentScheduleDO::getContractId));
            }

            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            Map<String, ContractType> contractTypeMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(contractTypeList)) {
                contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            }
            for (WorkBenchTaskListRespVO respVO : pageResult.getList()) {
                ContractType contractType = contractTypeMap.get(respVO.getContractType());
                if (ObjectUtil.isNotNull(contractType)) {
                    respVO.setContractTypeName(contractType.getName());
                }
                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(infoRespDTO)) {
                    respVO.setTaskId(infoRespDTO.getTaskId());
                }
                if (ObjectUtil.isNotNull(paymentApplicationDOMap.get(respVO.getProcessInstanceId()))) {
                    PaymentApplicationDO paymentApplicationDO = paymentApplicationDOMap.get(respVO.getProcessInstanceId());
                    respVO.setTitle(paymentApplicationDO.getTitle());
                    respVO.setCurrentPayAmount(paymentApplicationDO.getCurrentPayAmount());
                    respVO.setPayeeId(paymentApplicationDO.getId());
                }
                List<String> buyPlanIds = new ArrayList<String>();
                List<PaymentScheduleDO> scheduleDOList = plansByContarctIdMap.get(respVO.getContractId());
                if (CollectionUtil.isNotEmpty(scheduleDOList)) {
                    buyPlanIds = scheduleDOList.stream().map(PaymentScheduleDO::getId).collect(Collectors.toList());
                }
                respVO.setBuyPlanIds(buyPlanIds);
            }
        }
        return pageResult;
    }

    private PageResult<WorkBenchTaskListRespVO> listDraft(List<String> instanceIds, Map<String, ContractProcessInstanceRelationInfoRespDTO> toDoTaskMap, WorkBenchTaskReqVO reqVO) {
        reqVO.setInstanceIdList(instanceIds);
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectPage4Bench(reqVO);
        PageResult<WorkBenchTaskListRespVO> pageResult = ContractConverter.INSTANCE.convert2BenchDraft(contractDOPageResult);
        if (CollectionUtil.isNotEmpty(pageResult.getList())) {
            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            Map<String, ContractType> contractTypeMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(contractTypeList)) {
                contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            }
            for (WorkBenchTaskListRespVO respVO : pageResult.getList()) {
                ContractType contractType = contractTypeMap.get(respVO.getContractType());
                if (ObjectUtil.isNotNull(contractType)) {
                    respVO.setContractTypeName(contractType.getName());
                }
                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(infoRespDTO)) {
                    respVO.setTaskId(infoRespDTO.getTaskId());
                }
            }
        }
        return pageResult;
    }

    /**
     * 单位端_工作台_消息通知
     *
     * @param size
     * @return <{@link List }<{@link WorkBenchMsgAlertRespVO }>>
     */
    @Override
    public List<WorkBenchMsgAlertRespVO> msgAlert(Integer size) {
        //预警提醒：您签订的【合同名称】第【期数】期付款计划，距截止时间【年】/【月】/【日】还剩【剩余天数】天。

        //逾期提醒：您签订的【合同名称】第【期数】期付款截止日期【年】/【月】/【日】已过。请尽快处理款项。


        return null;
    }

    /**
     * 单位端_工作台_合同签订趋势
     *
     * @return <{@link WorkBenchSignTrendRespVO }>
     */
    @Override
    @DataPermission(enable = false)
    public WorkBenchSignTrendRespVO signTrend() {

        Map<String, Date> dateMap = getDatePeriod();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // 算出十二个月内的总合同金额
        List<TrendRespVO> sumMoney = intensifySum(dateMap, formatter);
        if (CollectionUtil.isEmpty(sumMoney)) {
            // 如果查询结果为空，一律设置默认值
            List<TrendRespVO> trendRespVOList = new ArrayList<>();
            // 解析日期范围
            Date startDate = dateMap.get(TREND_START_DATE);
            Date endDate = dateMap.get(TREND_END_DATE);
            // 转换为 LocalDate 对象，便于月份计算
            LocalDate startLocalDate = toLocalDate(startDate);
            LocalDate endLocalDate = toLocalDate(endDate);
            Map<String, List<ContractOrderExtDO>> result = new LinkedHashMap<>();
            // 遍历从 start_date 到 end_date 的每个月
            LocalDate currentMonth = startLocalDate.withDayOfMonth(1); // 设置为当前月份的第一天
            while (!currentMonth.isAfter(endLocalDate)) {
                String monthKey = currentMonth.getYear() + "-" + String.format("%02d", currentMonth.getMonthOfYear());
                trendRespVOList.add(new TrendRespVO().setMonthIndex(monthKey).setMoney(new BigDecimal("0.00")).setCount(0L));
                // 移动到下一个月
                currentMonth = currentMonth.plusMonths(1);
            }
            return new WorkBenchSignTrendRespVO().setSumMoneyRespVOList(trendRespVOList).setGovernmentMoneyRespVOList(trendRespVOList).setNonGovernmentMoneyRespVOList(trendRespVOList);
        }

        // 政府类金额
        List<TrendRespVO> govMoney = intensifyGov(dateMap, formatter);

        // 非政府类金额
        List<TrendRespVO> nonGovMoney = getNonGovernmentMoney(sumMoney, govMoney);

        return new WorkBenchSignTrendRespVO().setSumMoneyRespVOList(sumMoney).setGovernmentMoneyRespVOList(govMoney).setNonGovernmentMoneyRespVOList(nonGovMoney);
    }

    @Override
    @DataPermission(enable = false)
    public ContractStatisticCountRespVO contractStatistics(Integer flag) {
        ContractStatisticCountRespVO respVO = new ContractStatisticCountRespVO();
        String startTime = null;
        String endTime = null;
        String upTime = null;

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 当日
        if (flag == 1) {
            startTime = currentDate.toString() + " 00:00:00";
            endTime = currentDate.plusDays(1).toString() + " 00:00:00";
            //前一日
            upTime = currentDate.minusDays(1).toString() + " 00:00:00";

        } else if (flag == 2) {
            // 获取当月的第一天和下月第一天
            startTime = currentDate.dayOfMonth().withMinimumValue().toString() + " 00:00:00";
            endTime = currentDate.plusMonths(1).withDayOfMonth(1).toString() + " 00:00:00";
            //上月第一天和当月第一天
            upTime = currentDate.minusMonths(1).dayOfMonth().withMinimumValue().toString() + " 00:00:00";

        } else if (flag == 3) {
            // 获取当前年份的第一天和最后一天
            startTime = currentDate.dayOfYear().withMinimumValue().toString() + " 00:00:00";
            endTime = currentDate.plusYears(1).withDayOfYear(1).toString() + " 00:00:00";
            // 获取去年年份的第一天和当年第一天
            upTime = currentDate.minusYears(1).withDayOfYear(1).toString() + " 00:00:00";

        }
        //合同总数 政府采购类 非政府采购类（已签订）
        //所有合同
        List<ContractDO> contractDOList = contractMapper.selectAllSignedByTime(startTime, endTime);

        List<ContractDO> upContractDOList = contractMapper.selectAllSignedByTime(upTime, startTime);

        if (CollectionUtil.isEmpty(contractDOList)) {
            List<ContractTypeCountVO> nonGovernmentCountVOList = new ArrayList<>();
            List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().select(ContractType::getId, ContractType::getName, ContractType::getParentId));
            contractTypes.forEach(contractType -> {
                ContractTypeCountVO contractTypeCountVO = new ContractTypeCountVO(contractType.getName(), 0L);
                nonGovernmentCountVOList.add(contractTypeCountVO);
            });
            return new ContractStatisticCountRespVO().setGovernmentRateFlag(0).setOnGovernmentRateFlag(0).setNonGovernmentCountVOList(nonGovernmentCountVOList);
        }
        List<String> allIds = contractDOList.stream().map(ContractDO::getId).collect(Collectors.toList());
        //政府采购类合同
        List<ContractOrderExtDO> contractExtDOList = contractOrderExtMapper.selectAllSignedByTime(startTime, endTime);
        contractExtDOList = contractExtDOList.stream().filter(item -> allIds.contains(item.getId())).collect(Collectors.toList());
        List<String> ids = contractExtDOList.stream().map(ContractOrderExtDO::getId).collect(Collectors.toList());
        List<ContractDO> onGovernmentList = contractDOList.stream().filter(item -> !ids.contains(item.getId())).collect(Collectors.toList());
        //数量统计
        long sumContract = 0L;
        sumContract = contractDOList.size();
        //政府采购类 （已签订）
        long governmentPurchase = 0L;
        if (CollectionUtil.isNotEmpty(contractExtDOList)) {
            governmentPurchase = contractExtDOList.size();
        }
        //非政府采购类（已签订）
        Long nonGovernmentPurchase = 0L;
        nonGovernmentPurchase = sumContract - governmentPurchase;

        double governmentRate = 0L;
        double onGovernmentRate = 0L;
        int governmentRateFlag = 0;
        int onGovernmentRateFlag = 0;
        if (CollectionUtil.isNotEmpty(upContractDOList)) {
            List<String> upAllIds = upContractDOList.stream().map(ContractDO::getId).collect(Collectors.toList());
            List<ContractOrderExtDO> upContractExtDOList = contractOrderExtMapper.selectAllSignedByTime(upTime, startTime);
            upContractExtDOList = upContractExtDOList.stream().filter(item -> upAllIds.contains(item.getId())).collect(Collectors.toList());
            //政府采购上年合同数量
            long upGovernmentPurchase = upContractExtDOList.size();
            //和本年度作比较
            if ((upGovernmentPurchase > governmentPurchase)&& upGovernmentPurchase != 0) {
                //下降率
                governmentRate = (double) (upGovernmentPurchase - governmentPurchase) / upGovernmentPurchase;
                governmentRateFlag = 2;
            } else if ((upGovernmentPurchase < governmentPurchase)&& upGovernmentPurchase != 0) {
                //上涨率
                governmentRate = (double) (governmentPurchase - upGovernmentPurchase) / upGovernmentPurchase;
                governmentRateFlag = 1;
            }
            //非政采上年合同数量
            long upNonGovernmentPurchase = (long) upContractDOList.size() - (long) upContractExtDOList.size();
            if ((upNonGovernmentPurchase > nonGovernmentPurchase)&& upGovernmentPurchase != 0) {
                onGovernmentRate = (double) (upNonGovernmentPurchase - nonGovernmentPurchase) / upNonGovernmentPurchase;
                onGovernmentRateFlag = 2;
            } else if ((upNonGovernmentPurchase < nonGovernmentPurchase)&& upGovernmentPurchase != 0) {
                onGovernmentRate = (double) (nonGovernmentPurchase - upNonGovernmentPurchase) / upNonGovernmentPurchase;
                onGovernmentRateFlag = 1;
            }
        } else {
            //上一年政采合同数量为0
            if (governmentPurchase == 0) {
                governmentRate = 0;
            } else {
                governmentRate = 100;
                governmentRateFlag = 1;
            }

            //非政采上年合同数量
            long upNonGovernmentPurchase = onGovernmentList.size();
            if ((upNonGovernmentPurchase > nonGovernmentPurchase)&& upNonGovernmentPurchase != 0) {
                onGovernmentRate = (double) (upNonGovernmentPurchase - nonGovernmentPurchase) / upNonGovernmentPurchase;
                onGovernmentRateFlag = 2;
            } else if ((upNonGovernmentPurchase < nonGovernmentPurchase)&& upNonGovernmentPurchase != 0) {
                onGovernmentRate = (double) (nonGovernmentPurchase - upNonGovernmentPurchase) / upNonGovernmentPurchase;
                onGovernmentRateFlag = 1;
            }

        }
        respVO.setGovernmentRate(governmentRate).setOnGovernmentRate(onGovernmentRate).setGovernmentRateFlag(governmentRateFlag).setOnGovernmentRateFlag(onGovernmentRateFlag);
        //政府采购的只有货物工程服务三类
        Long goodsCount = 0L;
        Long engineerCount = 0L;
        Long serviceCount = 0L;
        for (ContractOrderExtDO item : contractExtDOList) {
            if (ObjectUtil.isNotEmpty(item.getProjectCategoryCode()) && item.getProjectCategoryCode().equals(RootTemplateCategoryEnums.GOODS.getType())) {
                goodsCount++;
            } else if (ObjectUtil.isNotEmpty(item.getProjectCategoryCode()) && item.getProjectCategoryCode().equals(RootTemplateCategoryEnums.SERVICE.getType())) {
                serviceCount++;
            } else if (ObjectUtil.isNotEmpty(item.getProjectCategoryCode()) && item.getProjectCategoryCode().equals(RootTemplateCategoryEnums.ENGINEER.getType())) {
                engineerCount++;
            }
        }
        List<ContractTypeCountVO> contractTypeCountVOS = new ArrayList<>();
        contractTypeCountVOS.add(new ContractTypeCountVO(RootTemplateCategoryEnums.GOODS.getKey(), goodsCount));
        contractTypeCountVOS.add(new ContractTypeCountVO(RootTemplateCategoryEnums.SERVICE.getKey(), serviceCount));
        contractTypeCountVOS.add(new ContractTypeCountVO(RootTemplateCategoryEnums.ENGINEER.getKey(), engineerCount));

        respVO.setSumContract(sumContract).setGovernmentPurchase(governmentPurchase).setNonGovernmentPurchase(nonGovernmentPurchase).setGovernmentCountVOList(contractTypeCountVOS);
        //非政府采购需要根据合同类型来判断
        // 获取所有合同类型，并构建父子映射
        Map<String, Set<String>> parentToChildrenMap = new HashMap<>();
        Map<String, String> contractTypeNameMap = new HashMap<>();

        contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().select(ContractType::getId, ContractType::getName, ContractType::getParentId)).forEach(type -> {
            parentToChildrenMap.computeIfAbsent(type.getParentId(), k -> new HashSet<>()).add(type.getId());
            contractTypeNameMap.put(type.getId(), type.getName());
        });

        // 统计每个合同类型的直接合同数量
        Map<String, Long> contractCountMap = onGovernmentList.stream().filter(contract -> contract.getContractType() != null).collect(Collectors.groupingBy(ContractDO::getContractType, Collectors.counting()));

        // 递归累加子级数量，缓存计算结果
        Map<String, Long> totalCountCache = new HashMap<>();

        // 计算一级合同类型的合同数量（顶级 `parentId` 为 `"0"`）
        List<ContractTypeCountVO> collect = parentToChildrenMap.getOrDefault("0", Collections.emptySet()).stream().map(id -> new ContractTypeCountVO(contractTypeNameMap.get(id), computeTotalCount(id, parentToChildrenMap, contractCountMap, totalCountCache))).collect(Collectors.toList());

        respVO.setNonGovernmentCountVOList(collect);

        return respVO;
    }

    // 递归计算合同数量
    private long computeTotalCount(String contractTypeId, Map<String, Set<String>> parentToChildrenMap, Map<String, Long> contractCountMap, Map<String, Long> totalCountCache) {
        // 检查缓存中是否存在已计算的结果
        if (totalCountCache.containsKey(contractTypeId)) {
            return totalCountCache.get(contractTypeId);
        }
        // 获取当前类型的合同数量，默认为0
        long count = contractCountMap.getOrDefault(contractTypeId, 0L);
        // 遍历子类型并递归计算总数
        for (String childId : parentToChildrenMap.getOrDefault(contractTypeId, Collections.emptySet())) {
            count += computeTotalCount(childId, parentToChildrenMap, contractCountMap, totalCountCache);
        }
        // 将计算结果存入缓存
        totalCountCache.put(contractTypeId, count);
        return count;
    }

    @Override
    public List<ContractAmountStatisticsRespVO> contractAmountStatistics(Integer flag) {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        String startYear = currentDate.dayOfYear().withMinimumValue().toString() + " 00:00:00";
        String endYear = currentDate.plusYears(1).withDayOfYear(1).toString() + " 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //查询今年的签署完成全部合同
        LambdaQueryWrapper<ContractDO> wrapper = new LambdaQueryWrapperX<ContractDO>().select(ContractDO::getId, ContractDO::getAmount, ContractDO::getCreateTime).in(ContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode(), ContractStatusEnums.PERFORMANCE_CLOSURE.getCode(), ContractStatusEnums.PERFORMING.getCode(), ContractStatusEnums.PERFORMANCE_COMPLETE.getCode(), ContractStatusEnums.PERFORMANCE_RISK.getCode(), ContractStatusEnums.CONTRACT_CHANGE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(), ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode()).ge(ContractDO::getCreateTime, LocalDateTime.parse(startYear, formatter)).lt(ContractDO::getCreateTime, LocalDateTime.parse(endYear, formatter));
        List<ContractDO> contractDOS = contractMapper.selectList(wrapper);
        if (CollectionUtil.isNotEmpty(contractDOS)) {
            List<String> ids = contractDOS.stream().map(ContractDO::getId).collect(Collectors.toList());
            List<PaymentScheduleDO> paymentScheduleDOS = new ArrayList<>();
            if (flag.equals(AmountTypeEnums.PAY.getCode())) {
                paymentScheduleDOS = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>().in(PaymentScheduleDO::getContractId, ids).eq(PaymentScheduleDO::getAmountType, AmountTypeEnums.PAY.getCode()));
            } else if (flag.equals(AmountTypeEnums.RECEIPT.getCode())) {
                paymentScheduleDOS = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>().in(PaymentScheduleDO::getContractId, ids).eq(PaymentScheduleDO::getAmountType, AmountTypeEnums.RECEIPT.getCode()));
            }
            // 初始化 12 个月的默认数据**
            Map<Integer, ContractAmountStatisticsRespVO> monthlyData = getIntegerContractAmountStatisticsRespVOMap(paymentScheduleDOS);
            // **转换 Map 为 List**

            return new ArrayList<>(monthlyData.values());
        }
        return Collections.emptyList();
    }

    @Override
    public List<TemplateExpirationReminderRespVO> templateExpirationReminder() {
        //查询模板
        List<Model> models = modelMapper.selectList(new LambdaQueryWrapperX<Model>().select(Model::getId, Model::getName, Model::getCode, Model::getCategoryId, Model::getEffectEndTime).eq(Model::getEffective, 1).eq(Model::getTimeEffectModel, 0));
        // 查询模版分类
        List<Integer> categoryIds = models.stream().map(Model::getCategoryId).collect(Collectors.toList());
        Map<Integer, String> categoryMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(categoryIds)) {
            List<ModelCategory> modelCategories = modelCategoryMapper.selectList(new LambdaQueryWrapperX<ModelCategory>().select(ModelCategory::getId, ModelCategory::getName).in(ModelCategory::getId, categoryIds));
            if (CollectionUtil.isNotEmpty(modelCategories)) {
                categoryMap = modelCategories.stream().collect(Collectors.toMap(ModelCategory::getId, ModelCategory::getName));
            }
        }
        List<TemplateExpirationReminderRespVO> respVOList = new ArrayList<>();
        // 遍历模板，获取还差三天过期的模板
        for (Model model : models) {
            TemplateExpirationReminderRespVO respVO = new TemplateExpirationReminderRespVO();
            if (model.getEffectEndTime() != null) {
                LocalDateTime effectEndTime = model.getEffectEndTime();
                LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);
                if (effectEndTime.isBefore(threeDaysFromNow)) {
                    respVO.setName(model.getName());
                    respVO.setCode(model.getCode());
                    respVO.setCategoryName(categoryMap.get(model.getCategoryId()));
                    respVO.setExpirationTime(effectEndTime);
                    respVOList.add(respVO);
                }
            }
        }
        return respVOList;
    }

    private Map<Integer, ContractAmountStatisticsRespVO> getIntegerContractAmountStatisticsRespVOMap(List<PaymentScheduleDO> paymentScheduleDOS) {
        Map<Integer, ContractAmountStatisticsRespVO> monthlyData = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            ContractAmountStatisticsRespVO vo = new ContractAmountStatisticsRespVO();
            vo.setMonth(month);
            vo.setAmount(BigDecimal.ZERO);
            vo.setPaidAmount(BigDecimal.ZERO);
            monthlyData.put(month, vo);
        }
        // **遍历数据，按月份累加金额**
        for (PaymentScheduleDO item : paymentScheduleDOS) {
            int month = item.getCreateTime().getMonthValue();
            ContractAmountStatisticsRespVO vo = monthlyData.get(month);

            // **累加应收/应付金额，单位转换为万元**
            if (item.getAmount() != null) {
                vo.setAmount(vo.getAmount().add(item.getAmount().divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP)));
            }

            // **累加已支付金额，单位转换为万元**
            if (PaymentScheduleStatusEnums.DONE.getCode().equals(item.getStatus()) && item.getAmount() != null) {
                vo.setPaidAmount(vo.getPaidAmount().add(item.getAmount().divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP)));
            }
        }
        return monthlyData;
    }

    private List<TrendRespVO> getNonGovernmentMoney(List<TrendRespVO> sumMoney, List<TrendRespVO> govMoney) {
        List<TrendRespVO> result = new ArrayList<>();

        for (TrendRespVO sumItem : sumMoney) {
            // 查找 govMoney 中是否存在与 sumItem 的 monthIndex 相同的元素
            Optional<TrendRespVO> matchingGovItem = govMoney.stream().filter(govItem -> govItem.getMonthIndex().equals(sumItem.getMonthIndex())).findFirst();

            if (matchingGovItem.isPresent()) {
                // 如果找到了匹配的项，计算差值
                BigDecimal sumT = BigDecimal.ZERO;
                BigDecimal govT = BigDecimal.ZERO;
                long sumCount = 0L;
                long govCount = 0L;

                if (ObjectUtil.isNotNull(sumItem.getMoney())) {
                    sumT = sumItem.getMoney().setScale(2, RoundingMode.HALF_UP);
                    sumCount = sumItem.getCount();
                } else {
                    //若这个月全部为空，这直接轮空
                    BigDecimal nonGovMoney = BigDecimal.ZERO;
                    result.add(new TrendRespVO().setMoney(nonGovMoney).setCount(sumCount).setMonthIndex(sumItem.getMonthIndex()));
                    continue;
                }
                if (ObjectUtil.isNotNull(matchingGovItem.get().getMoney())) {
                    govT = matchingGovItem.get().getMoney().setScale(2, RoundingMode.HALF_UP);
                    govCount = matchingGovItem.get().getCount();
                }
                BigDecimal nonGovMoney = sumT.subtract(govT).setScale(2, RoundingMode.HALF_UP);
                Long nonGovCount = sumCount - govCount;
                result.add(new TrendRespVO().setMoney(nonGovMoney).setCount(nonGovCount).setMonthIndex(sumItem.getMonthIndex()));
            }
        }

        return result;
    }

    private List<TrendRespVO> intensifyGov(Map<String, Date> dateMap, DateTimeFormatter formatter) {
        List<ContractOrderExtDO> orderExtDOList = contractOrderExtMapper.select4Trend(dateMap);
        if (CollectionUtil.isEmpty(orderExtDOList)) {
            return Collections.emptyList();
        }
        Map<String, List<ContractOrderExtDO>> groupedGovernmentByMonthList = orderExtDOList.stream().collect(Collectors.groupingBy(item -> item.getContractSignTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter)));

        sortDate4Contract(groupedGovernmentByMonthList);
        // 调用方法补充缺失月份
        Map<String, List<ContractOrderExtDO>> result = fillMissingMonths(dateMap, groupedGovernmentByMonthList);
        return enhanceResult(result);
    }

    private List<TrendRespVO> intensifySum(Map<String, Date> dateMap, DateTimeFormatter formatter) {
        //总合同
        List<ContractDO> contractDOList = contractMapper.select4Trend(dateMap);
        if (CollectionUtil.isEmpty(contractDOList)) {
            return Collections.emptyList();
        }
        List<ContractOrderExtDO> sumContractDOList = ContractConverter.INSTANCE.do2ExtList(contractDOList);
        Map<String, List<ContractOrderExtDO>> groupedSumContractByMonthList = sumContractDOList.stream().collect(Collectors.groupingBy(item -> item.getContractSignTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter)));
        sortDate4Contract(groupedSumContractByMonthList);
        // 调用方法补充缺失月份
        Map<String, List<ContractOrderExtDO>> result = fillMissingMonths(dateMap, groupedSumContractByMonthList);
        return enhanceResult(result);
    }

    private List<TrendRespVO> enhanceResult(Map<String, List<ContractOrderExtDO>> result) {
        List<TrendRespVO> resultList = new ArrayList<TrendRespVO>();
        for (Map.Entry<String, List<ContractOrderExtDO>> entry : result.entrySet()) {
            TrendRespVO respVO = new TrendRespVO().setMonthIndex(entry.getKey());
            BigDecimal sum = BigDecimal.ZERO;
            long size = 0L;
            if (CollectionUtil.isNotEmpty(entry.getValue())) {
                sum = entry.getValue().stream().map(ContractOrderExtDO::getTotalMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                size = entry.getValue().size();
            }
            resultList.add(respVO.setMoney(sum).setCount(size));

        }
        return resultList;
    }

    private void sortDate4Contract(Map<String, List<ContractOrderExtDO>> groupedByMonthList) {
        // 按照键的大小（日期字符串 "yyyy-MM"）排序
        groupedByMonthList = groupedByMonthList.entrySet().stream().sorted(Map.Entry.comparingByKey()) // 按照 Map 的 key 排序（默认是升序）
                .collect(Collectors.toMap(Map.Entry::getKey,          // 键
                        Map.Entry::getValue,        // 值
                        (e1, e2) -> e1,             // 合并函数（这里不需要合并，因为每个键是唯一的）
                        LinkedHashMap::new          // 保持插入顺序（LinkedHashMap 维护顺序）
                ));
    }

    Map<String, Date> getDatePeriod() {
        Map<String, Date> rsMap = new HashMap<String, Date>();
        Date startDate;
        Date endDate;
        int thisYear = new LocalDate().getYear();
        int thisMonth = new LocalDate().getMonthOfYear();
        if (Calendar.UNDECIMBER == thisMonth) {
            // 使用 Calendar 来创建日期对象
            Calendar calendar = Calendar.getInstance();
            calendar.set(thisYear, Calendar.JANUARY, 1, 0, 0, 0);
            startDate = calendar.getTime();

        } else {
            // 使用 Calendar 来创建日期对象
            Calendar calendar = Calendar.getInstance();
            calendar.set(thisYear - 1, thisMonth, 1, 0, 0, 0);
            startDate = calendar.getTime();

        }

        rsMap.put(TREND_START_DATE, startDate);
        rsMap.put(TREND_END_DATE, new Date());

        return rsMap;
    }

    public Map<String, List<ContractOrderExtDO>> fillMissingMonths(Map<String, Date> dateMap, Map<String, List<ContractOrderExtDO>> groupedSumContractByMonthList) {

        // 解析日期范围
        Date startDate = dateMap.get(TREND_START_DATE);
        Date endDate = dateMap.get(TREND_END_DATE);

        // 转换为 LocalDate 对象，便于月份计算
        LocalDate startLocalDate = toLocalDate(startDate);
        LocalDate endLocalDate = toLocalDate(endDate);

        Map<String, List<ContractOrderExtDO>> result = new LinkedHashMap<>();

        // 遍历从 start_date 到 end_date 的每个月
        LocalDate currentMonth = startLocalDate.withDayOfMonth(1); // 设置为当前月份的第一天
        while (!currentMonth.isAfter(endLocalDate)) {
            String monthKey = currentMonth.getYear() + "-" + String.format("%02d", currentMonth.getMonthOfYear());

            // 如果当前月份在 groupedSumContractByMonthList 中有数据，直接添加
            // 否则填充为 null
            result.put(monthKey, groupedSumContractByMonthList.getOrDefault(monthKey, null));

            // 移动到下一个月
            currentMonth = currentMonth.plusMonths(1);
        }

        return result;
    }

    private LocalDate toLocalDate(Date date) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        // 从 LocalDateTime 转换为 org.joda.time.LocalDate
        LocalDate jodaLocalDate = new LocalDate(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth());

        return jodaLocalDate;
    }

    /**
     * @return {@link WorkbenchStatisticRespVO }
     */
    @Override
    @DataPermission(enable = false)
    public WorkbenchStatisticRespVO workbenchStatistic(String type) {
        Calendar calendar = Calendar.getInstance();
        Date beginDate;
        Date endDate = new Date();
        switch (type) {
            case "month": // 本月
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                // 设置小时、分钟、秒和毫秒为 0
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                beginDate = calendar.getTime();
                break;
            case "premonth": // 上月
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                // 设置小时、分钟、秒和毫秒为 0
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                endDate = calendar.getTime();

                calendar.add(Calendar.MONTH, -1);
                beginDate = calendar.getTime();
                break;
            case "year": // 本年
            default:
                //默认按年查询
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, Calendar.JANUARY);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                // 设置小时、分钟、秒和毫秒为 0
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                beginDate = calendar.getTime();
        }

        //合同总数 政府采购类 非政府采购类（已签订）
        //所有合同
        List<ContractDO> contractDOList = contractMapper.selectAllSigned(beginDate, endDate);
        if (CollectionUtil.isEmpty(contractDOList)) {
            return new WorkbenchStatisticRespVO();
        }
        // 收款的合同
        // 付款的合同
//        List<String> allIds = contractDOList.stream().map(ContractDO::getId).collect(Collectors.toList());
        //政府采购类合同
        List<ContractDO> govContracts = contractDOList.stream().filter(item -> ContractUploadTypeEnums.ORDER_DRAFT.getCode().equals(item.getUpload()) || ContractUploadTypeEnums.THIRD_PARTY.getCode().equals(item.getUpload())).collect(Collectors.toList());

//        List<ContractOrderExtDO> contractExtDOList = contractOrderExtMapper.selectAllSigned();
//        contractExtDOList = contractExtDOList.stream().filter(item -> allIds.contains(item.getId())).collect(Collectors.toList());

        //数量统计
        StatisticCountRespVO countRespVO = enhanceCount(contractDOList, govContracts);
        //付款金额统计
        StatisticMoneyRespVO moneyRespVO = enhanceMoney(contractDOList, govContracts);
        //收款金额统计
        StatisticRecMoneyRespVO recMoneyRespVO = enhanceRecMoney(contractDOList);
        return new WorkbenchStatisticRespVO().setCountRespVO(countRespVO).setMoneyRespVO(moneyRespVO).setRecMoneyRespVO(recMoneyRespVO);
    }

    public static void main(String[] args) {

        LocalDate startTime = null;
        LocalDate endTime = null;
        String s = null;
        String e = null;
        String upStartTime = null;
        String upEndTime = null;
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 当日

        LocalDate startTime1 = currentDate;
        s = startTime1.toString() + " 00:00:00";
        e = currentDate.toString() + " 23:59:59";
        upStartTime = currentDate.minusDays(1).toString() + " 00:00:00";
        upEndTime = currentDate.toString() + " 00:00:00";

        // 获取当月的第一天和最后一天
        LocalDate startTime2 = currentDate.dayOfMonth().withMinimumValue();
        LocalDate endTime1 = currentDate.dayOfMonth().withMaximumValue();
        LocalDate nextMonthFirstDay = currentDate.plusMonths(1).withDayOfMonth(1);
        String s1 = startTime2.toString() + " 00:00:00";
        String e1 = endTime1.toString() + " 23:59:59";
        String upStartTime1 = currentDate.minusMonths(1).dayOfMonth().withMinimumValue().toString() + " 00:00:00";
        String upEndTime1 = currentDate.minusMonths(1).dayOfMonth().withMaximumValue().toString() + " 00:00:00";
        // 获取当前年份的第一天和最后一天
        LocalDate startTime3 = currentDate.dayOfYear().withMinimumValue();
        LocalDate endTime2 = currentDate.plusYears(1).withDayOfYear(1);
        String s2 = startTime3.toString() + " 00:00:00";
        String e2 = endTime2.toString() + " 23:59:59";
        // 获取去年年份的第一天和当年第一天
        String upStartTime2 = currentDate.minusYears(1).withDayOfYear(1).toString() + " 00:00:00";
        String upEndTime2 = currentDate.dayOfYear().withMaximumValue().toString() + " 00:00:00";

        System.out.println(s);
        System.out.println(e);

        System.out.println(upStartTime);
        System.out.println(upEndTime);
        System.out.println("-------");
        System.out.println(s1);
        System.out.println(e1);
        System.out.println(upStartTime1);
        System.out.println(upEndTime1);
        System.out.println("-------");
        System.out.println(s2);
        System.out.println(e2);


        System.out.println(upStartTime2);
        System.out.println(upEndTime2);
    }
}



