package com.yaoan.module.econtract.service.payment.paymentapplication;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.datapermission.core.rule.dept.DeptDataPermissionRule;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.bpm.api.task.dto.v2.BpmTaskApproveReqDTO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.contract.vo.BpmTaskApproveReqVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.out.AcceptanceRecordRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.LedgerPageReqV2VO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceDetailRespVO;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceDetailSaveReqVO;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceRespVO;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceSaveReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.StatisticsAmountV2RespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.BigPaymentApplicationListBpmRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication.PaymentApplicationListReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication.PaymentApplicationListRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationSaveReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.*;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.statisticsamount.StatisticsAmountRespVO;
import com.yaoan.module.econtract.controller.admin.relative.vo.RelativeByUserRespVO;
import com.yaoan.module.econtract.convert.acceptance.AcceptanceConverter;
import com.yaoan.module.econtract.convert.businessfile.BusinessFileConverter;
import com.yaoan.module.econtract.convert.config.SystemConfigDTOConverter;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.convert.payment.PaymentApplicationConverter;
import com.yaoan.module.econtract.dal.dataobject.acceptance.AcceptanceDO;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.dataobject.contract.*;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplScheRelDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.deferred.PaymentDeferredApplyDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.mysql.acceptance.AcceptanceMapper;
import com.yaoan.module.econtract.dal.mysql.businessfile.BusinessFileMapper;
import com.yaoan.module.econtract.dal.mysql.contract.*;
import com.yaoan.module.econtract.dal.mysql.contractPerformanceAcceptance.ContractPerformanceAcceptanceMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.deferred.PaymentDeferredApplyMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplScheRelMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplicationMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums;
import com.yaoan.module.econtract.enums.payment.*;
import com.yaoan.module.econtract.service.businessfile.BusinessFileService;
import com.yaoan.module.econtract.service.ledger.LedgerService;
import com.yaoan.module.econtract.service.paymentPlan.PaymentService;
import com.yaoan.module.econtract.service.relative.RelativeService;
import com.yaoan.module.econtract.util.AmountUtil;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.flowable.FlowableUtil;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.dept.DeptApi;
import com.yaoan.module.system.api.dept.dto.DeptRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;
import static com.yaoan.module.infra.enums.ErrorCodeConstants.DIY_ERROR;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/21 17:18
 */
@Slf4j
@Service
public class PaymentApplicationServiceImpl implements PaymentApplicationService {
    @Resource
    private PaymentApplicationMapper paymentApplicationMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private RelativeService relativeService;
    @Resource
    private PaymentApplScheRelMapper paymentApplScheRelMapper;
    @Resource
    private SystemConfigApi systemConfigApi;

    @Resource
    private PaymentService paymentService;

    @Resource
    private LedgerService ledgerService;
    @Resource
    private BusinessFileService businessFileService;
    @Resource
    private BusinessFileMapper businessFileMapper;
    @Resource
    private ContractPerformanceAcceptanceMapper contractPerformanceAcceptanceMapper;
    @Resource
    private PaymentDeferredApplyMapper paymentDeferredApplyMapper;
    @Resource
    private ContractPerformanceLogMapper contractPerformanceLogMapper;
    public static final String AUTO_REASON = "自动草稿箱";

    @Resource
    private PaymentInvoiceMapper invoiceMapper;

    @Resource
    private PaymentInvoiceDetailMapper invoiceDetailMapper;
    @Resource
    private SimpleContractMapper simpleContractMapper;
    @Autowired
    private AcceptanceMapper acceptanceMapper;

    /**
     * 分页查询
     */
    @Override
    public PageResult<PaymentApplicationListRespVO> listPaymentApplication(PaymentApplicationListReqVO vo) {
        PageResult<PaymentApplicationDO> paymentApplicationDOList = paymentApplicationMapper.selectPage(vo);
        PageResult<PaymentApplicationListRespVO> respVOPageResult = PaymentApplicationConverter.INSTANCE.convertPageDO2Resp(paymentApplicationDOList);
        return enhancePage(respVOPageResult);
    }

    @Override
    @DataPermission(includeRules = {DeptDataPermissionRule.class})
    public PageResult<PaymentApplicationListRespVO> getPaymentApplicationPage(PaymentApplicationListReqVO paymentPlanRepVO) {
        PageResult<PaymentApplicationDO> paymentApplicationDOPageResult = paymentApplicationMapper.selectPage(paymentPlanRepVO);
        if (paymentApplicationDOPageResult.getTotal() == 0) {
            return PageResult.empty();
        }

        PageResult<PaymentApplicationListRespVO> resultPageRespVO = PaymentApplicationConverter.INSTANCE.convertPageDO2Resp(paymentApplicationDOPageResult);
        List<String> contractIds = resultPageRespVO.getList().stream().map(PaymentApplicationListRespVO::getContractId).collect(Collectors.toList());
        LambdaQueryWrapperX<SimpleContractDO> lambdaQueryWrapperX1 = new LambdaQueryWrapperX();
        lambdaQueryWrapperX1.in(SimpleContractDO::getId, contractIds);
        List<SimpleContractDO> simpleContractDOList = simpleContractMapper.selectList(lambdaQueryWrapperX1);
        Map<String, SimpleContractDO> stringSimpleContractDOMap = CollectionUtils.convertMap(simpleContractDOList, SimpleContractDO::getId);

        resultPageRespVO.getList().forEach(payPerformancePageRespVO -> {
            SettlementMethodEnums settlementMethodEnums = SettlementMethodEnums.getInstance(payPerformancePageRespVO.getSettlementMethod());
            if (ObjectUtil.isNotNull(settlementMethodEnums)) {
                payPerformancePageRespVO.setSettlementMethodStr(settlementMethodEnums.getInfo());
            }
            SimpleContractDO simpleContractDO = stringSimpleContractDOMap.get(payPerformancePageRespVO.getContractId());
            if (simpleContractDO != null) {
                payPerformancePageRespVO.setContractCode(simpleContractDO.getCode());
                payPerformancePageRespVO.setContractName(simpleContractDO.getName());
                payPerformancePageRespVO.setContractAmount(simpleContractDO.getAmount());
                payPerformancePageRespVO.setPaymentRatio(payPerformancePageRespVO.getCurrentPayAmount().divide(BigDecimal.valueOf(simpleContractDO.getAmount()), 2, RoundingMode.HALF_UP).doubleValue());
            }
            payPerformancePageRespVO.setConfirmFileList(businessFileMapper.selectByBusinessId(payPerformancePageRespVO.getId()));

        });

        return resultPageRespVO;
    }

    /**
     * 分页的增强
     */
    private PageResult<PaymentApplicationListRespVO> enhancePage(PageResult<PaymentApplicationListRespVO> respVOPageResult) {
        Long loginId = getLoginUserId();
        List<PaymentApplicationListRespVO> list = respVOPageResult.getList();
        List<String> instanceList = list.stream().map(PaymentApplicationListRespVO::getProcessInstanceId).collect(Collectors.toList());
        List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

        Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        //待处理的任务
        List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
        Map<String, BpmTaskAllInfoRespVO> toDoTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        if (CollectionUtil.isNotEmpty(instanceList)) {
            taskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(loginId, instanceList);
            taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
            taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

            toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTime(taskAllInfoRespVOList);
            toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
        }
        for (PaymentApplicationListRespVO respVO : list) {
            PaymentTypeEnums paymentTypeEnums = PaymentTypeEnums.getInstance(respVO.getPaymentType());
            if (ObjectUtil.isNotNull(paymentTypeEnums)) {
                respVO.setPaymentTypeStr(paymentTypeEnums.getInfo());
            }
            SettlementMethodEnums settlementMethodEnums = SettlementMethodEnums.getInstance(respVO.getSettlementMethod());
            if (ObjectUtil.isNotNull(settlementMethodEnums)) {
                respVO.setSettlementMethodStr(settlementMethodEnums.getInfo());
            }
            CommonFlowableReqVOResultStatusEnums resultStatusEnums = CommonFlowableReqVOResultStatusEnums.getInstance(respVO.getResult());
            if (ObjectUtil.isNotNull(resultStatusEnums)) {
                respVO.setResultStr(resultStatusEnums.getInfo());
            }
            BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(respVO.getResult());
            if (ObjectUtil.isNotNull(resultEnum)) {
                if (BpmProcessInstanceResultEnum.PROCESS == resultEnum) {
                    //审批中的任务分为 被退回 和审批中
                    BpmTaskAllInfoRespVO task = taskMap.get(respVO.getProcessInstanceId());
                    if (ObjectUtil.isNotEmpty(task)) {
                        respVO.setTaskId(task.getTaskId());
                    }
                } else {
                    respVO.setResultStr(resultEnum.getDesc());
                }
                //如果是被退回的申请，回显任务id
                if (BpmProcessInstanceResultEnum.BACK == resultEnum) {
                    BpmTaskAllInfoRespVO rejectedTask = taskMap.get(respVO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(rejectedTask)) {
                        respVO.setTaskId(rejectedTask.getTaskId());
                    }
                }
                if (BpmProcessInstanceResultEnum.APPROVE != resultEnum) {
                    BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = toDoTaskMap.get(respVO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
                        respVO.setAssigneeId(bpmTaskAllInfoRespVO.getAssigneeUserId());
                    }
                }
            }
        }
        return respVOPageResult;
    }

    /**
     * 金额统计
     * 华杰pm同步，一旦计划生成，计划的金额就不会改变。
     */
    @Override
    public StatisticsAmountRespVO statisticsAmount(PaymentApplicationListBpmReqVO vo) {
        BigDecimal totalPayedAmount = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<PaymentApplicationDO> paymentApplicationDOList = paymentApplicationMapper.selectStatisticsAmountList(vo);
        if (CollectionUtil.isNotEmpty(paymentApplicationDOList)) {
//            totalPayedAmount = paymentApplicationDOList.stream()
//                    .filter(paymentApplicationDO -> paymentApplicationDO.getPayDate() != null) // 过滤出 payDate 不为空的元素
//                    .map(PaymentApplicationDO::getCurrentPayAmount) // 提取 currentPayAmount 字段
//                    .reduce(BigDecimal.ZERO, BigDecimal::add); // 使用 reduce 求和
            for (PaymentApplicationDO applicationDO : paymentApplicationDOList) {
                if (ObjectUtil.isNotNull(applicationDO.getPayDate())) {
                    totalPayedAmount = totalPayedAmount.add(applicationDO.getCurrentPayAmount());
                }
            }


            totalAmount = paymentApplicationDOList.stream()
                    .map(PaymentApplicationDO::getCurrentPayAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        BigDecimal totalUnpaidAmount = totalAmount.subtract(totalPayedAmount);

        return new StatisticsAmountRespVO()
                .setTotalAmount(ObjectUtil.isNotNull(totalAmount) ? totalAmount : new BigDecimal(String.valueOf(BigDecimal.ZERO)))
                .setPayedAmount(ObjectUtil.isNotNull(totalPayedAmount) ? totalPayedAmount : new BigDecimal(String.valueOf(BigDecimal.ZERO)))
                .setUnpaidAmount(ObjectUtil.isNotNull(totalUnpaidAmount) ? totalUnpaidAmount : new BigDecimal(String.valueOf(BigDecimal.ZERO)));
    }

    /**
     * 查看付款详情
     */
    @Override
    public PaymentApplicationRespVO getPaymentApplication(PaymentApplicationReqVO vo) {
        PaymentApplicationRespVO resultRespVO = new PaymentApplicationRespVO();
        PaymentApplicationDO paymentApplicationDO = paymentApplicationMapper.selectById(vo.getId());
        if (ObjectUtil.isNotNull(paymentApplicationDO)) {
            vo.setContractId(paymentApplicationDO.getContractId());
        }
        // 基本信息
        PaymentApplicationBaseInfoRespVO baseInfoRespVO = enhanceBaseInfo(vo, paymentApplicationDO);

        List<PaymentPlanRespVO> planRespVOList = new ArrayList<PaymentPlanRespVO>();
        //付款计划
        planRespVOList = enhancePaymentPlan(vo);
        ContractDO contractDO = contractMapper.selectById(vo.getContractId());

        // 合同信息
        ContractInfoRespVO contractInfoRespVO = enhanceContractInfo(vo,contractDO);
        // 收款方信息
        PayeeInfoRespVO payeeInfoRespVO = enhancePayeeInfo(vo, paymentApplicationDO);
        // region [发票信息]
        List<PaymentInvoiceDO> PaymentInvoiceDOS = invoiceMapper.selectList(PaymentInvoiceDO::getPayId, vo.getId());
        List<String> invIds = PaymentInvoiceDOS.stream().map(PaymentInvoiceDO::getId).collect(Collectors.toList());
        List<PaymentInvoiceDetailDO> PaymentInvoiceDetailDOS = invoiceDetailMapper.selectList(PaymentInvoiceDetailDO::getInvoiceId, invIds);
        Map<String, List<PaymentInvoiceDetailDO>> detailMaps = PaymentInvoiceDetailDOS.stream().collect(Collectors.groupingBy(PaymentInvoiceDetailDO::getInvoiceId));
        List<PaymentInvoiceRespVO> paymentInvoiceRespVOS = BeanUtils.toBean(PaymentInvoiceDOS, PaymentInvoiceRespVO.class);
        for (PaymentInvoiceRespVO paymentInvoiceRespVO : paymentInvoiceRespVOS) {
            List<PaymentInvoiceDetailDO> paymentInvoiceDetail = detailMaps.get(paymentInvoiceRespVO.getId());
            if (paymentInvoiceDetail != null) {
                List<PaymentInvoiceDetailRespVO> paymentInvoiceDetailRespVOS = new ArrayList<>();
                paymentInvoiceDetail.forEach(item -> {
                    paymentInvoiceDetailRespVOS.add(BeanUtils.toBean(item, PaymentInvoiceDetailRespVO.class));
                });
                paymentInvoiceRespVO.setInvoiceDetailList(paymentInvoiceDetailRespVOS);
            }
        }
        // endregion
        //附件
        List<BusinessFileVO> filesRespVOList = new ArrayList<>();
        List<BusinessFileDO> fileDOList = businessFileMapper.selectList(BusinessFileDO::getBusinessId, vo.getId());
        filesRespVOList = BusinessFileConverter.INSTANCE.d2R(fileDOList);

        //验收记录
        enhanceAcceptance(resultRespVO,vo,contractDO);

        return resultRespVO.setPaymentApplicationBaseInfoRespVO(baseInfoRespVO)
                .setPaymentPlanRespVOList(planRespVOList)
                .setContractInfoRespVO(contractInfoRespVO)
                .setPayeeInfoRespVO(payeeInfoRespVO)
                .setAttachmentId(baseInfoRespVO.getFileId())
                .setFileRespVOList(filesRespVOList).setInvoiceRespVOS(paymentInvoiceRespVOS);
    }

    private void enhanceAcceptance(PaymentApplicationRespVO resultRespVO, PaymentApplicationReqVO vo,ContractDO contractDO) {
        List<AcceptanceDO> acceptanceDOList = acceptanceMapper.selectList(AcceptanceDO::getContractId,vo.getContractId());
        if(CollectionUtil.isEmpty(acceptanceDOList)) {
            return;
        }
        List<AcceptanceRecordRespVO> acceptanceRespVOList = AcceptanceConverter.INSTANCE.do2RespList(acceptanceDOList);
        List<String> planIds = acceptanceDOList.stream().map(AcceptanceDO::getPlanId).collect(Collectors.toList());
        List<PaymentScheduleDO> scheduleDOList = paymentScheduleMapper.selectList(PaymentScheduleDO::getId,planIds);
        Map<String,  PaymentScheduleDO > scheduleDOMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(scheduleDOList)) {
            scheduleDOMap = CollectionUtils.convertMap(scheduleDOList, PaymentScheduleDO::getId);
        }
        for (AcceptanceRecordRespVO respVO : acceptanceRespVOList) {
            PaymentScheduleDO scheduleDO = scheduleDOMap.get(respVO.getPlanId());
            if(ObjectUtil.isNotEmpty(scheduleDO)) {
                respVO.setSort(scheduleDO.getSort());
                respVO.setAmount(scheduleDO.getAmount());
                //结算类型
                AmountTypeEnums amountTypeEnums = AmountTypeEnums.getInstance(scheduleDO.getAmountType());
                if (ObjectUtil.isNotNull(amountTypeEnums)) {
                    respVO.setAmountTypeName(amountTypeEnums.getInfo());
                }
                //款项名称
                MoneyTypeEnums moneyTypeEnums = MoneyTypeEnums.getInstance(scheduleDO.getMoneyType());
                if (ObjectUtil.isNotNull(moneyTypeEnums)) {
                    respVO.setMoneyTypeName(moneyTypeEnums.getInfo());
                }
                respVO.setStatus(scheduleDO.getStatus());
                PaymentScheduleStatusEnums scheduleStatusEnums = PaymentScheduleStatusEnums.getInstance(scheduleDO.getStatus());
                respVO.setStatusName(ObjectUtil.isNotNull(scheduleStatusEnums)?scheduleStatusEnums.getInfo():"");
            }

            if (ObjectUtil.isNotNull(contractDO)) {
                //相对方
                respVO.setRelativeName(contractDO.getPartBName());
            }
        }
        resultRespVO.setAcceptanceRespVOList(acceptanceRespVOList);
    }

    private PayeeInfoRespVO enhancePayeeInfo(PaymentApplicationReqVO vo, PaymentApplicationDO paymentApplicationDO) {
        //如果是编辑，就从上一次申请草稿中保存的数据取值
        if (ObjectUtil.isNotNull(paymentApplicationDO)) {
            return new PayeeInfoRespVO().setName(paymentApplicationDO.getPayeeName())
                    .setBankName(paymentApplicationDO.getPayeeBankName()).setBankAccount(paymentApplicationDO.getPayeeAccount());
        }

        //如果是保存，就从相对方里取值
        List<Relative> relativeList = (List<Relative>) relativeMapper.selectListByContractId(vo.getContractId());
        if (CollectionUtil.isEmpty(relativeList)) {
            return new PayeeInfoRespVO();
        }
        Relative relative = relativeList.get(0);
        return new PayeeInfoRespVO().setName(relative.getCompanyName())
                .setBankName(relative.getBankName())
                .setBankAccount(relative.getBankAccount());
    }

    private ContractInfoRespVO enhanceContractInfo(PaymentApplicationReqVO vo,ContractDO contractDO) {
        if (ObjectUtil.isNull(contractDO)) {
            return new ContractInfoRespVO();
        }
        ContractInfoRespVO respVO = ContractConverter.INSTANCE.convertDO2Info(contractDO);
        ContractType contractType = contractTypeMapper.selectById(contractDO.getContractType());
        if (ObjectUtil.isNotNull(contractType)) {
            respVO.setContractType(contractType.getName());
        }
        //查询合同下所有付款申请
        List<PaymentApplicationDO> allPaymentApplicationDOS = paymentApplicationMapper.selectList(new LambdaQueryWrapperX<PaymentApplicationDO>()
                .eq(PaymentApplicationDO::getContractId, vo.getContractId()));
        // 已发起支付的申请金额
        BigDecimal payAmount = allPaymentApplicationDOS.stream().map(PaymentApplicationDO::getCurrentPayAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        //将本次已付金额相加
        respVO.setPayedAmount(payAmount);
        // 筛选出支付未完成的申请
        BigDecimal payingAmt = allPaymentApplicationDOS.stream().filter(item -> IfNumEnums.NO.getCode().equals(item.getStatus())).map(PaymentApplicationDO::getCurrentPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        respVO.setPayingAmount(payingAmt);
        // 付款进度
        if (ObjectUtil.isNotEmpty(payAmount) && contractDO.getAmount() > 0) {
            respVO.setPayRate(payAmount.divide(BigDecimal.valueOf(contractDO.getAmount()), 4, RoundingMode.HALF_UP));
        }
        return respVO;
    }

    private List<PaymentPlanRespVO> enhancePaymentPlan(PaymentApplicationReqVO vo) {
        List<PaymentScheduleDO> paymentScheduleDOList;

        if (CollectionUtil.isNotEmpty(vo.getPlanIdList())) {
            paymentScheduleDOList = paymentScheduleMapper.selectList(PaymentScheduleDO::getId, vo.getPlanIdList());
        } else {
            //首次申请的回显
            if (ObjectUtil.isNull(vo.getId())){
                paymentScheduleDOList = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, vo.getContractId(), PaymentScheduleDO::getAmountType, AmountTypeEnums.PAY.getCode());
            } else {
                paymentScheduleDOList = paymentScheduleMapper.selectPlanForApplication(vo.getId());
            }
            if (CollectionUtil.isNotEmpty(paymentScheduleDOList)){
                CollectionUtil.sort(paymentScheduleDOList, Comparator.comparing(PaymentScheduleDO::getSort));
            }
        }
        List<String> payeeIds = paymentScheduleDOList.stream().map(PaymentScheduleDO::getPayee).collect(Collectors.toList());
        List<Relative> relatives = relativeMapper.selectList(Relative::getId, payeeIds);
        Map<String, String> relativesMap = relatives.stream().collect(
                Collectors.toMap(Relative::getId, Relative::getName,
                        (v1, v2) -> v2));
        if (CollectionUtil.isEmpty(paymentScheduleDOList)) {
            return Collections.emptyList();
        }
        List<PaymentPlanRespVO> paymentPlanRespVOS = PaymentApplicationConverter.INSTANCE.convertListSchedule2Resp(paymentScheduleDOList);
        List<String> planIds = paymentPlanRespVOS.stream().map(PaymentPlanRespVO::getId).collect(Collectors.toList());
        // 查询出这些计划已发起支付的金额
        List<PaymentApplScheRelDO> paymentApplScheRelDOS = paymentApplScheRelMapper.selectList(PaymentApplScheRelDO::getScheduleId, planIds);
        Map<String,List<PaymentApplScheRelDO>> relDOMap = new HashMap<>();
        if(CollUtil.isNotEmpty(paymentApplScheRelDOS)){
            relDOMap = CollectionUtils.convertMultiMap(paymentApplScheRelDOS,PaymentApplScheRelDO::getScheduleId);
        }
        List<AcceptanceDO> acceptanceDOList = acceptanceMapper.selectList(AcceptanceDO::getPlanId, planIds);
        Map<String,List<AcceptanceDO>> acceptanceMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(acceptanceDOList)){
            acceptanceMap = CollectionUtils.convertMultiMap(acceptanceDOList,AcceptanceDO::getPlanId);
        }

        // 计算每个计划剩余可支付金额, 并查出该计划当前申请中的支付金额
        for (PaymentPlanRespVO paymentPlanRespVO : paymentPlanRespVOS) {
            BigDecimal payAmt = paymentApplScheRelDOS.stream().filter(item -> paymentPlanRespVO.getId().equals(item.getScheduleId()) && ObjectUtil.isNotEmpty(item.getCurrentPayAmount())).map(PaymentApplScheRelDO::getCurrentPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            paymentPlanRespVO.setUnPayAmount(paymentPlanRespVO.getAmount().subtract(payAmt));
            if (ObjectUtil.isNotEmpty(vo.getId())){
                paymentApplScheRelDOS.stream().filter(item -> paymentPlanRespVO.getId().equals(item.getScheduleId()) && vo.getId().equals(item.getApplicationId()) && ObjectUtil.isNotEmpty(item.getCurrentPayAmount())).map(PaymentApplScheRelDO::getCurrentPayAmount).findFirst().ifPresent(paymentPlanRespVO::setCurrentPayAmount);
            }

            if ( ObjectUtil.isNotEmpty(paymentPlanRespVO.getCurrentPayAmount()) && ObjectUtil.isNotEmpty(vo.getCurrentPayAmount()) &&  vo.getCurrentPayAmount().compareTo(BigDecimal.ZERO) > 0 && paymentPlanRespVO.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                paymentPlanRespVO.setPaymentRatio(paymentPlanRespVO.getAmount().divide(vo.getCurrentPayAmount(), 2, RoundingMode.HALF_UP).doubleValue());
            }
            paymentPlanRespVO.setPayeeName(relativesMap.get(paymentPlanRespVO.getPayee()));

            //（1）需验收且验收通过=可选；
            //（2）需要验收且未验收通过=请先完成验收；
            //（3）已验收且已发起付款后的=不可选；
            //（4）不需验收计划 = 本次金额不大于剩余结算金额；
            enhancePaymentPlan4Acceptance(paymentPlanRespVO,acceptanceMap,relDOMap,vo);

        }

        return paymentPlanRespVOS;
    }

    /**
     * （1）需验收且验收通过未发起付款=可选；
     * （2）需要验收且未验收通过=请先完成验收；
     * （3）已验收且已发起付款后的=不可选；
     * （4）不需验收计划 = 本次金额不大于剩余结算金额；
     * */
    private void enhancePaymentPlan4Acceptance(PaymentPlanRespVO paymentPlanRespVO, Map<String,List<AcceptanceDO>> acceptanceMap, Map<String, List<PaymentApplScheRelDO>> relDOMap, PaymentApplicationReqVO vo) {
        int acceptanceFlag = 0;
        List<Integer> statusList = new ArrayList<>();
        statusList.add(PaymentScheduleStatusEnums.TO_DO.getCode());
        statusList.add(PaymentScheduleStatusEnums.TO_PUBLISH.getCode());
        if (IfNumEnums.YES.getCode().equals(paymentPlanRespVO.getNeedAcceptance()) && IfNumEnums.YES.getCode().equals(paymentPlanRespVO.getIsAcceptance()) && statusList.contains(paymentPlanRespVO.getStatus())) {
            acceptanceFlag = 1;
        } else if (IfNumEnums.YES.getCode().equals(paymentPlanRespVO.getNeedAcceptance()) && IfNumEnums.NO.getCode().equals(paymentPlanRespVO.getIsAcceptance())) {
            acceptanceFlag = 2;
        } else if (IfNumEnums.YES.getCode().equals(paymentPlanRespVO.getIsAcceptance()) && !statusList.contains(paymentPlanRespVO.getStatus())) {

            List<PaymentApplScheRelDO> relDOList = relDOMap.get(paymentPlanRespVO.getId());
            if(CollUtil.isNotEmpty(relDOList) && StringUtils.isNotBlank(paymentPlanRespVO.getId()) && relDOList.get(0).getApplicationId().equals(vo.getId())){
                //退回到草稿的验收计划，可选
                acceptanceFlag=1;
            }else {
                acceptanceFlag = 3;
            }
        }else if(IfNumEnums.NO.getCode().equals(paymentPlanRespVO.getNeedAcceptance())){
            acceptanceFlag = 4;
        }
        //已验收金额
        if (IfNumEnums.YES.getCode().equals(paymentPlanRespVO.getIsAcceptance())) {
            List<AcceptanceDO> acceptanceDOList = acceptanceMap.get(paymentPlanRespVO.getId());
            if (CollectionUtil.isNotEmpty(acceptanceDOList)) {
                BigDecimal currentPayMoneySum = acceptanceDOList.stream().map(AcceptanceDO::getCurrentPayMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                paymentPlanRespVO.setAcceptanceAmount(currentPayMoneySum);
            }
        }
        paymentPlanRespVO.setAcceptanceFlag(acceptanceFlag);
    }

    private PaymentApplicationBaseInfoRespVO enhanceBaseInfo(PaymentApplicationReqVO vo, PaymentApplicationDO applicationDO) {
        PaymentApplicationBaseInfoRespVO resultRespVO = new PaymentApplicationBaseInfoRespVO();
        if (ObjectUtil.isNotNull(applicationDO)) {
            resultRespVO = PaymentApplicationConverter.INSTANCE.convertDO2InfoResp(applicationDO);
            resultRespVO.setId(applicationDO.getId());
        } else {
            AdminUserRespDTO userRespDTO = adminUserApi.getUser(getLoginUserId());
            resultRespVO = PaymentApplicationConverter.INSTANCE.convertVO2BaseInfo(vo);
            if (ObjectUtil.isNotNull(userRespDTO)) {
                DeptRespDTO deptRespDTO = deptApi.getDept(userRespDTO.getDeptId());
                if (deptRespDTO != null) {
                    resultRespVO.setApplicantDept(deptRespDTO.getName());
                }
                resultRespVO.setApplicantId(String.valueOf(userRespDTO.getId())).setApplicantName(userRespDTO.getNickname());
            }
        }
        //实时计算出对应合同所属的支付计划状态为已支付的金额的总和，返给前端进行计算
        // 按合同查询所有付款申请
        List<PaymentApplicationDO> paymentApplicationDOS = paymentApplicationMapper.selectList(new LambdaQueryWrapperX<PaymentApplicationDO>().eq(PaymentApplicationDO::getContractId, vo.getContractId()));
        // 查询出所有的付款金额
        BigDecimal payedAmt = paymentApplicationDOS.stream().filter(item-> !item.getId().equals(vo.getId())).map(PaymentApplicationDO::getCurrentPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (ObjectUtil.isNotNull(applicationDO) && applicationDO.getCurrentPayAmount().compareTo(BigDecimal.ZERO) > 0){
            resultRespVO.setAfterPayedAmount(payedAmt.add(applicationDO.getCurrentPayAmount()));
        } else {
            resultRespVO.setAfterPayedAmount(payedAmt);
        }
        return resultRespVO.setPayedAmount(payedAmt);
    }

    /**
     * 自动生成 付款编码：提交时自动生成，
     * 生成规则：公司名称首字母缩写+年月日+时间戳
     */
    private String buildPaymentApplicationCode() {
        RelativeByUserRespVO relative = relativeService.queryRelativeByLoginUser();
        if (ObjectUtil.isNotNull(relative)) {
            return EcontractUtil.getCodeAutoByTimestamp(relative.getCompanyName());
        }
        return "queryRelativeByLoginUser-error";
    }

    /**
     * 付款申请 - 保存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String savePaymentApplication(PaymentApplicationSaveReqVO vo) {
        checkScheduleForSave(vo);
        PaymentApplicationDO paymentApplicationDO = PaymentApplicationConverter.INSTANCE.convertSaveVO2DO(vo);
        enhanceSaveDO(paymentApplicationDO, vo);

        if (ObjectUtil.isNull(vo.getId())) {
            paymentApplicationMapper.insert(paymentApplicationDO);
        } else {
            paymentApplicationMapper.updateById(paymentApplicationDO);
        }
        String applyId = paymentApplicationDO.getId();
        vo.setId(applyId);
        //绑定付款申请和计划的关系
        insertPlans(vo);
        // 发票数据保存
        insertInvoices(vo);
        //关联业务附件关系
        businessFileService.deleteByBusinessId(paymentApplicationDO.getId());
        businessFileService.createBatchBusinessFile(paymentApplicationDO.getId(), vo.getFileList());
        // 先删除再保存
        contractPerformanceLogMapper.delete(ContractPerformanceLogDO::getBillId, paymentApplicationDO.getId());
        if (vo.getBuyPlanIds() != null && vo.getBuyPlanIds().size() > 0) {
            for (PaymentPlanAmtReqVO planId : vo.getBuyPlanIds()) {
                ContractPerformanceLogDO contractPerformanceLogDO = new ContractPerformanceLogDO();
                contractPerformanceLogDO.setBusinessId(planId.getId());
                contractPerformanceLogDO.setBillId(paymentApplicationDO.getId());
                contractPerformanceLogDO.setModuleName("付款");
                contractPerformanceLogDO.setOperateName("发起了");
                contractPerformanceLogDO.setOperateContent("付款申请");
                contractPerformanceLogMapper.insert(contractPerformanceLogDO);
            }
        }
        return applyId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveCollectionApplication(PaymentApplicationSaveReqVO vo) {
        checkScheduleForSave(vo);
        PaymentApplicationDO paymentApplicationDO = PaymentApplicationConverter.INSTANCE.convertSaveVO2DO(vo);
        enhanceSaveDO(paymentApplicationDO, vo);
        paymentApplicationDO.setCollectionType(1);
        paymentApplicationDO.setResult(6);
        List<PaymentScheduleDO> payedScheduleDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                .eqIfPresent(PaymentScheduleDO::getContractId, paymentApplicationDO.getContractId())
                .eqIfPresent(PaymentScheduleDO::getStatus, PaymentScheduleStatusEnums.PAYED.getCode()));
        BigDecimal afterPayedAmount = payedScheduleDOList.stream()
                .map(PaymentScheduleDO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        afterPayedAmount = afterPayedAmount.add(paymentApplicationDO.getCurrentPayAmount());
        paymentApplicationDO.setAfterPayedAmount(afterPayedAmount);
        paymentApplicationDO.setPayDate(new Date());

        BigDecimal currentPayAmount = vo.getBuyPlanIds().stream().map(PaymentPlanAmtReqVO::getCurrentPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        paymentApplicationDO.setCurrentPayAmount(currentPayAmount);
        if (ObjectUtil.isNotEmpty(vo.getPayRate()) && ObjectUtil.isNotNull(currentPayAmount) && ObjectUtil.isNotNull(paymentApplicationDO.getContractAmount()) && paymentApplicationDO.getContractAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal divide = currentPayAmount.divide(paymentApplicationDO.getContractAmount(), 2, RoundingMode.HALF_UP);
            paymentApplicationDO.setPayRate(divide);
        }
        paymentApplicationMapper.insert(paymentApplicationDO);
        String applyId = paymentApplicationDO.getId();
        vo.setId(applyId);
        insertPlans(vo);
        // 发票数据保存
        insertInvoices(vo);
        List<String> planIds = vo.getBuyPlanIds().stream().map(PaymentPlanAmtReqVO::getId).collect(Collectors.toList());
        List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.selectBatchIds(planIds);
        paymentService.pushPayInfo(planIds);
        if (CollUtil.isNotEmpty(paymentScheduleDOList)) {
            for (PaymentScheduleDO scheduleDO : paymentScheduleDOList) {
                //支付计划：审批状态
                scheduleDO.setApplyStatus(PaymentScheduleApplyStatusEnums.APPLY_SUCCESS.getCode());
            }
            paymentScheduleMapper.updateBatch(paymentScheduleDOList);
            //得到这批支付计划中sort最大的值
            Optional<Integer> maxSort = paymentScheduleDOList.stream()
                    .map(PaymentScheduleDO::getSort)
                    .max(Integer::compareTo);
            Integer defaultValue = 0;
            //如果maxSort为空，取值为0
            Integer maxSortValue = maxSort.orElseGet(() -> defaultValue);
            //更新合同的支付计划sort标识
            contractMapper.updateById(new ContractDO().setId(paymentScheduleDOList.get(0).getContractId()).setCurrentScheduleSort(Integer.valueOf(maxSortValue)));

            //将收款申请和支付计划同步：已收款状态
            paymentService.pushPayInfo(planIds);
            paymentApplicationMapper.updateById(paymentApplicationDO.setPayDate(new Date()));

        }
        return applyId;
    }

    /**
     * 支付计划与草稿关联
     * 将相应的计划更新
     */
    private void insertPlans(PaymentApplicationSaveReqVO vo) {
        paymentApplScheRelMapper.delete(PaymentApplScheRelDO::getApplicationId, vo.getId());
        if (CollectionUtil.isNotEmpty(vo.getBuyPlanIds())) {
            List<PaymentApplScheRelDO> relDOList = new ArrayList<PaymentApplScheRelDO>();
            for (PaymentPlanAmtReqVO scheduleId : vo.getBuyPlanIds()) {
                PaymentApplScheRelDO relDO = new PaymentApplScheRelDO();
                relDO.setApplicationId(vo.getId());
                relDO.setCurrentPayAmount(scheduleId.getCurrentPayAmount());
                relDO.setScheduleId(scheduleId.getId());
                relDOList.add(relDO);
            }
            paymentApplScheRelMapper.insertBatch(relDOList);
        }
    }

    private void insertInvoices(PaymentApplicationSaveReqVO vo) {

        List<PaymentInvoiceDO> deleteInvDo = invoiceMapper.selectList(PaymentInvoiceDO::getPayId, vo.getId());
        if (CollectionUtil.isNotEmpty(deleteInvDo)) {
            List<String> deleteInvIds = deleteInvDo.stream().map(PaymentInvoiceDO::getId).collect(Collectors.toList());
            invoiceDetailMapper.delete(PaymentInvoiceDetailDO::getInvoiceId, deleteInvIds);
            invoiceMapper.deleteBatchIds(deleteInvIds);
        }

        List<PaymentInvoiceSaveReqVO> invoiceList = vo.getInvoiceList();
        List<PaymentInvoiceDetailSaveReqVO> invoiceDetailList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(invoiceList)) {
            invoiceList.forEach(invoiceInfo -> {
                String invoId = IdUtil.fastSimpleUUID();
                invoiceInfo.setId(invoId);
                invoiceInfo.setPayId(vo.getId());
                List<PaymentInvoiceDetailSaveReqVO> detailList = invoiceInfo.getInvoiceDetailList();
                detailList.forEach(detail -> {
                    detail.setInvoiceId(invoId);
                });
                invoiceDetailList.addAll(detailList);
            });

            List<PaymentInvoiceDO> PaymentInvoiceDOList = BeanUtils.toBean(vo.getInvoiceList(), PaymentInvoiceDO.class);
            invoiceMapper.insertBatch(PaymentInvoiceDOList);
            List<PaymentInvoiceDetailDO> PaymentInvoiceDetailDOList = BeanUtils.toBean(invoiceDetailList, PaymentInvoiceDetailDO.class);
            invoiceDetailMapper.insertBatch(PaymentInvoiceDetailDOList);
        }
    }

    /**
     * 校验是否重复申请支付计划
     * 校验是否存在之前的支付计划还没通过审批
     */
    private void checkScheduleForSubmit(PaymentApplicationSaveReqVO vo) {
        String applicationId = vo.getId();
        //申请的计划ids中是否包含，已经申请过的计划
        if (StringUtils.isNotBlank(applicationId)) {

            //查询申请相关的支付计划
            List<PaymentScheduleDO> plans = paymentScheduleMapper.selectPlanForApplication(applicationId);
            if (CollectionUtil.isNotEmpty(plans)) {
                //查询 已申请 和 申请通过 的支付计划
                List<PaymentScheduleDO> appliedStatusPlans = plans.stream()
                        .filter(plan -> PaymentScheduleApplyStatusEnums.APPLY.getCode().equals(plan.getApplyStatus())
                                || PaymentScheduleApplyStatusEnums.APPLY_SUCCESS.getCode().equals(plan.getApplyStatus()))
                        .collect(Collectors.toList());
                for (PaymentScheduleDO plan : appliedStatusPlans) {
                    if (!PaymentScheduleApplyStatusEnums.NO_APPLY.getCode().equals(plan.getApplyStatus())) {
                        List<Integer> appliedPlanSortList = appliedStatusPlans.stream().map(PaymentScheduleDO::getSort).collect(Collectors.toList());
                        Collections.sort(appliedPlanSortList);
                        StringBuilder str = new StringBuilder();
                        int t = 0;
                        for (Integer id : appliedPlanSortList) {
                            str.append(String.valueOf(id));
                            t++;
                            if (appliedPlanSortList.size() > t) {
                                str.append(" 、 ");
                            }
                        }
                        throw exception(PAYMENT_SCHEDULE_NOT_PAYED_FOR_SINGLE_ERROR, str.toString());
                    }
                }
            }


        }

    }

    /**
     * 校验是否重复申请支付计划
     * 校验是否存在之前的支付计划还没通过审批
     */
    private void checkScheduleForSave(PaymentApplicationSaveReqVO vo) {
        //申请的计划ids中是否包含，已经申请过的计划
/*        if (CollectionUtil.isNotEmpty(vo.getBuyPlanIds())) {
            // 查询状态为 已申请 和 申请通过 的计划的数量
//            Long count = paymentScheduleMapper.selectCountAppliedStatus(vo);
            //查询 已申请 和 申请通过 的支付计划
            List<PaymentScheduleDO> appliedStatusPlans = paymentScheduleMapper.selectAppliedStatus(vo);

            if (CollectionUtil.isNotEmpty(appliedStatusPlans)) {
                List<Integer> appliedPlanIds = appliedStatusPlans.stream().map(PaymentScheduleDO::getSort).collect(Collectors.toList());
                Collections.sort(appliedPlanIds);
                StringBuilder str = new StringBuilder();
                int t = 0;
                for (Integer id : appliedPlanIds) {
                    str.append(String.valueOf(id));
                    t++;
                    if (appliedPlanIds.size() < t) {
                        str.append(" , ");
                    }
                }
                throw exception(PAYMENT_SCHEDULE_NOT_PAYED_FOR_SINGLE_ERROR, str.toString());
            }
        }*/
        //此次计划之前的计划，是否存在计划未通过审批
//        if (CollectionUtil.isNotEmpty(vo.getBuyPlanIds())) {
//            List<PaymentScheduleDO> scheduleDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>().in(PaymentScheduleDO::getId, vo.getBuyPlanIds()));
//            if (CollectionUtil.isNotEmpty(scheduleDOList)) {
//                if (CollectionUtil.isNotEmpty(scheduleDOList)) {
//                    //如果是最前几个，直接通过校验
//                    for (PaymentScheduleDO paymentScheduleDO : scheduleDOList) {
//                        if (1 == paymentScheduleDO.getSort()) {
//                            return;
//                        }
//                    }
//                }
//            }
//        }

        List<PaymentScheduleDO> paymentScheduleDOList;

        if (CollectionUtil.isNotEmpty(vo.getBuyPlanIds())) {
            Map<String, BigDecimal> map = vo.getBuyPlanIds().stream().filter(item-> ObjectUtil.isNotEmpty(item.getCurrentPayAmount())).collect(
                    Collectors.toMap(PaymentPlanAmtReqVO::getId, PaymentPlanAmtReqVO::getCurrentPayAmount,
                            (v1, v2) -> v2));

            List<String> planIds = vo.getBuyPlanIds().stream().map(PaymentPlanAmtReqVO::getId).collect(Collectors.toList());
            paymentScheduleDOList = paymentScheduleMapper.selectList(PaymentScheduleDO::getId, planIds);
            List<PaymentApplScheRelDO> paymentApplScheRelDOS = paymentApplScheRelMapper.selectList(PaymentApplScheRelDO::getScheduleId, planIds);
            List<PaymentPlanRespVO> paymentPlanRespVOS = PaymentApplicationConverter.INSTANCE.convertListSchedule2Resp(paymentScheduleDOList);
            // 计算每个计划剩余可支付金额, 并查出该计划当前申请中的支付金额
            for (PaymentPlanRespVO paymentPlanRespVO : paymentPlanRespVOS) {
                BigDecimal payAmt = paymentApplScheRelDOS.stream().filter(item -> paymentPlanRespVO.getId().equals(item.getScheduleId()) && ObjectUtil.isNotEmpty(item.getCurrentPayAmount())).map(PaymentApplScheRelDO::getCurrentPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                if (ObjectUtil.isNotEmpty(paymentPlanRespVO.getAmount())) {
                    BigDecimal unPayAmt = paymentPlanRespVO.getAmount().subtract(payAmt);
                    BigDecimal payingAmt = map.get(paymentPlanRespVO.getId());
                    if (ObjectUtil.isNotEmpty(payingAmt) && payingAmt.compareTo(unPayAmt) > 0) {
                        throw exception(SYSTEM_ERROR, "申请支付金额大于可支付金额");
                    }
                }
            }
        } else {
            throw exception(SYSTEM_ERROR, "计划不能为空");
        }
        if (CollectionUtil.isEmpty(paymentScheduleDOList)) {
            throw exception(SYSTEM_ERROR, "计划不存在");
        }
    }

    /**
     * 校验支付计划是否有没审批通过的
     */
    private void checkMinSchedule(List<PaymentScheduleDO> minScheduleDOS) {
        if (CollectionUtil.isNotEmpty(minScheduleDOS)) {
            for (PaymentScheduleDO minScheduleDO : minScheduleDOS) {
                PaymentScheduleApplyStatusEnums enums = PaymentScheduleApplyStatusEnums.getInstance(minScheduleDO.getStatus());
                if (PaymentScheduleApplyStatusEnums.APPLY_SUCCESS != enums) {
                    throw exception(PAYMENT_SCHEDULE_NOT_PAYED_ERROR);
                }
            }
        }
    }

    /**
     * 获得map里最小的key值
     */
    private Integer getMinKey(Map<Integer, PaymentScheduleDO> scheduleMap) {
        Integer key = null;
        for (Map.Entry<Integer, PaymentScheduleDO> entry : scheduleMap.entrySet()) {
            key = entry.getKey();
            PaymentScheduleDO value = entry.getValue();
            Integer minKey = null;
            PaymentScheduleDO minPaymentScheduleDO = null;
            if (minKey == null || key < minKey) {
                minKey = key;
                minPaymentScheduleDO = value;
            }
        }
        return key;
    }

    private void enhanceSaveDO(PaymentApplicationDO paymentApplicationDO, PaymentApplicationSaveReqVO vo) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        AdminUserRespDTO userRespDTO = adminUserApi.getUser(loginUserId);
        paymentApplicationDO.setApplicantId(String.valueOf(loginUserId));
        paymentApplicationDO.setApplicantName(userRespDTO.getNickname());
        DeptRespDTO deptRespDTO = deptApi.getDept(userRespDTO.getDeptId());
        if (deptRespDTO != null) {
            paymentApplicationDO.setApplicantDept(deptRespDTO.getName());
        }
        ContractDO t = contractMapper.getAmount(vo.getContractId());
        if (ObjectUtil.isNotNull(t)) {
            BigDecimal contractAmount = new BigDecimal(t.getAmount().toString());
            paymentApplicationDO.setContractAmount(contractAmount);
        }
        BigDecimal currentPayAmount = vo.getBuyPlanIds().stream().filter(item->ObjectUtil.isNotEmpty(item.getCurrentPayAmount())).map(PaymentPlanAmtReqVO::getCurrentPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (ObjectUtil.isNull(currentPayAmount) ||  currentPayAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw exception(DIY_ERROR, "本次支付申请金额不能为0");
        }
        paymentApplicationDO.setCurrentPayAmount(currentPayAmount);
        paymentApplicationDO.setCurrentPayAmountCapitalize(AmountUtil.trsferCapital(currentPayAmount.doubleValue()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updatePaymentApplication(PaymentApplicationUpdateReqVO vo) {
        PaymentApplicationDO applicationDO = PaymentApplicationConverter.INSTANCE.convertUpdateVO2DO(vo);
        applicationDO.setId(vo.getId());
        //更新计划于申请的关联关系
        if (CollectionUtil.isNotEmpty(vo.getBuyPlanIds())) {
            //清除申请相关计划绑定
            paymentApplScheRelMapper.delete(new LambdaQueryWrapperX<PaymentApplScheRelDO>().eq(PaymentApplScheRelDO::getApplicationId, vo.getId()));
            //重新绑定申请与计划的关联
            List<PaymentApplScheRelDO> relDOList = new ArrayList<PaymentApplScheRelDO>();
            for (String buyPlanId : vo.getBuyPlanIds()) {
                PaymentApplScheRelDO applScheRelDO = new PaymentApplScheRelDO();
                applScheRelDO.setScheduleId(buyPlanId);
                applScheRelDO.setApplicationId(vo.getId());
                relDOList.add(applScheRelDO);
            }
            paymentApplScheRelMapper.insertBatch(relDOList);
        }
        paymentApplicationMapper.updateById(applicationDO);
        //关联业务附件关系
        businessFileService.deleteByBusinessId(vo.getId());
        businessFileService.createBatchBusinessFile(vo.getId(), vo.getFileList());

        return "success";
    }

    @Override
    public String deletePaymentApplications(IdReqVO vo) {
        List<PaymentApplScheRelDO> paymentApplScheRelDOS = paymentApplScheRelMapper.selectList(PaymentApplScheRelDO::getApplicationId, vo.getIdList());
        if (ObjectUtil.isNotEmpty(paymentApplScheRelDOS)) {
            List<String> relIds = paymentApplScheRelDOS.stream().map(PaymentApplScheRelDO::getId).collect(Collectors.toList());
            List<String> planIds = paymentApplScheRelDOS.stream().map(PaymentApplScheRelDO::getScheduleId).collect(Collectors.toList());
            List<PaymentScheduleDO> paymentScheduleDOList = new ArrayList<>();
            for (String planId : planIds) {
                PaymentScheduleDO paymentScheduleDO = new PaymentScheduleDO();
                paymentScheduleDO.setId(planId);
                paymentScheduleDO.setStatus(PaymentScheduleStatusEnums.TO_DO.getCode());
                paymentScheduleDO.setApplyStatus(PaymentScheduleApplyStatusEnums.NO_APPLY.getCode());
                paymentScheduleDOList.add(paymentScheduleDO);
            }
            paymentScheduleMapper.updateBatch(paymentScheduleDOList);
            paymentApplScheRelMapper.deleteBatchIds(relIds);
        }
        paymentApplicationMapper.deleteBatchIds(vo.getIdList());
        return "success";
    }

    /**
     * 发起审批 流程
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitApprovePaymentApplication(PaymentApplicationSaveReqVO reqVO) {
        Boolean isSave = ObjectUtil.isEmpty(reqVO.getId());
        //前面的计划如果没有完成，则不可发起该计划（该校验已作废，现在申请可同时发起多个计划的支付，也可以多次对不按顺序的同一个计划支付）
//        List<PaymentScheduleDO> oldPlans = paymentScheduleMapper.selectOldPlans(reqVO.getContractId(), reqVO.getSort());
//        if (CollectionUtil.isNotEmpty(oldPlans)) {
//            List<Integer> todoPlanIds = oldPlans.stream().map(PaymentScheduleDO::getSort).sorted().collect(Collectors.toList());
//            String sortStr = todoPlanIds.stream()
//                    .map(String::valueOf)  // 将每个整数转换为字符串
//                    .collect(Collectors.joining(",")); // 用逗号连接
//            throw exception(DIY_ERROR, "第 " + sortStr + " 期计划还未完成");
//        }
        //校验验收申请
        List<String> planIds = reqVO.getBuyPlanIds().stream().map(PaymentPlanAmtReqVO::getId).collect(Collectors.toList());

        List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(PaymentScheduleDO::getId, planIds);
        if(CollectionUtil.isNotEmpty(paymentScheduleDOS)) {
            paymentScheduleDOS.forEach(paymentScheduleDO -> {
                if(IfNumEnums.YES.getCode().equals(paymentScheduleDO.getNeedAcceptance()) && IfNumEnums.NO.getCode().equals(paymentScheduleDO.getIsAcceptance())) {
                    throw exception(DIY_ERROR,"请先完成验收后再发起付款申请。");
                }
            });
        }
        // 履约中的计划也可以再次申请，因此暂时关闭以下校验
//        if (CollectionUtil.isNotEmpty(paymentScheduleDOS)) {
//            for (PaymentScheduleDO paymentScheduleDO : paymentScheduleDOS) {
//                if (!PaymentScheduleStatusEnums.TO_DO.getCode().equals(paymentScheduleDO.getStatus())) {
//                    log.error("请先发布履约计划:" + paymentScheduleDO.getId());
//                    throw exception(DIY_ERROR, "请先发布履约计划");
//                }
//            }
//        }
        //校验是否存在付款延期申请
        List<PaymentDeferredApplyDO> paymentDeferredApplyDOS = paymentDeferredApplyMapper.selectList(PaymentDeferredApplyDO::getPlanId, planIds);
        if (ObjectUtil.isNotEmpty(paymentDeferredApplyDOS)) {
            paymentDeferredApplyDOS.forEach(paymentDeferredApplyDO -> {
                if (!paymentDeferredApplyDO.getResult().equals(2)) {
                    throw exception(ErrorCodeConstants.PERFORM_TASK_NOT_PAYMENT_DELAY);
                }
            });
        }
        String key = ActivityConfigurationEnum.PAYMENT_APPLICATION_APPROVE.getDefinitionKey();
        if (ObjectUtil.isNotEmpty(reqVO.getContractId())) {
            ContractDO contractDO = contractMapper.selectById(reqVO.getContractId());
            if (ObjectUtil.isNotNull(contractDO)) {
                if (contractDO.getIsLocked().equals(1)) {
                    throw exception(ErrorCodeConstants.CONTRACT_IS_LOCKED, reqVO.getContractId());
                }
                //获取流程key
//                if (ObjectUtil.isNotEmpty(contractDO.getContractType())) {
//                    ContractType contractType = contractTypeMapper.selectById(contractDO.getContractType());
//                    if (ObjectUtil.isNotEmpty(contractType) && ObjectUtil.isNotEmpty(contractType.getPaymentProcess())) {
//                        key = contractType.getPaymentProcess();
//                    }
//                }
            }
        }
        // 一期未支付完也能再次支付
//        checkScheduleForSubmit(reqVO);

        Long loginUserId = getLoginUserId();

        String applicationId;
        //如果不存在，则需要先保存。
        applicationId = savePaymentApplication(reqVO);
        //1.插入请求单
        //校验是否发起过审批
        PaymentApplicationDO paymentApplicationDO = paymentApplicationMapper.selectById(applicationId);
        if (ObjectUtil.isNull(paymentApplicationDO)) {
            throw exception(SYSTEM_ERROR, "付款申请不存在。");
        }

        //更新付款计划信息
        List<PaymentScheduleDO> scheduleDOList = paymentScheduleMapper.selectPlanForApplication(applicationId);
        if (CollectionUtil.isNotEmpty(scheduleDOList)) {
            scheduleDOList.forEach(paymentScheduleDO -> paymentScheduleDO
                    .setStatus(PaymentScheduleStatusEnums.DOING.getCode())
                    .setApplyStatus(PaymentScheduleApplyStatusEnums.APPLY.getCode())
            );
            paymentScheduleMapper.updateBatch(scheduleDOList);
        }

        // 审批中的申请不能再次申请，但不能根据result判断
//        CommonFlowableReqVOResultStatusEnums resultStatusEnums = CommonFlowableReqVOResultStatusEnums.getInstance(paymentApplicationDO.getResult());
//        if (CommonFlowableReqVOResultStatusEnums.TO_SEND != resultStatusEnums  ) {
//            throw exception(ErrorCodeConstants.PAYMENT_APPLICATION_SEND_EXIST_ERROR);
//        }

        // 新增的逻辑，生成编码，创建流程，提交则直接提交
        if(isSave){
            //生成付款申请编码
            paymentApplicationMapper.updateById(paymentApplicationDO.setPaymentApplyCode(buildPaymentApplicationCode())
                    //同步最新的延期状态
                    .setIsDeferred(reqVO.getIsDeferred())
            );
            // 2 发起申请 BPM
            // 2.1 流程变量
            Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
            processInstanceVariables.put("id", paymentApplicationDO.getId());
            // 2.2 流程实例id
            String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                    new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(key).setVariables(processInstanceVariables).setBusinessKey(applicationId));
            //更新付款申请信息
            paymentApplicationMapper.updateById(new PaymentApplicationDO()
                    .setResult(CommonFlowableReqVOResultStatusEnums.APPROVING.getResultCode())
                    .setFlowStatus(StatusEnums.APPROVING.getCode())
                    .setApplyTime(LocalDateTime.now())
                    .setId(applicationId)
                    .setProcessInstanceId(processInstanceId)
                    .setApplyTime(LocalDateTime.now())
            );
            //是否直接通过
            if (ObjectUtil.isNotEmpty(reqVO.getIsSubmit()) && 1 == reqVO.getIsSubmit()) {
                //添加taskId
                if (ObjectUtil.isNotEmpty(processInstanceId)) {
                    List<String> processInstanceIds = Arrays.asList(processInstanceId);
                    List<BpmTaskAllInfoRespVO> instanceIds = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(getLoginUserId(), processInstanceIds);
                    if (ObjectUtil.isNotEmpty(instanceIds)) {
                        for (BpmTaskAllInfoRespVO bpmTaskRespDTO : instanceIds) {
                            BpmTaskApproveReqVO taskApproveReqVO = new BpmTaskApproveReqVO().setTaskId(bpmTaskRespDTO.getTaskId()).setReason(AUTO_REASON);
                            try {
                                bpmTaskApi.approveTask(getLoginUserId(), new BpmTaskApproveReqDTO().setId(taskApproveReqVO.getTaskId()).setReason(AUTO_REASON));
                            } catch (Exception e) {
                                log.error("approveTask异常:"+e.getMessage(), e);
                                throw exception(DIY_ERROR, "请稍后重试或联系管理员处理。");
                            }
                        }

                    }
                }
            }
        } else {
            // 如果是修改且进行提交，则直接提交
            // 如果存在taskId,说明已经存在工作流，如果标识为提交则直接提交
            if (ObjectUtil.isNotEmpty(reqVO.getIsSubmit()) && 1 == reqVO.getIsSubmit()&&ObjectUtil.isNotNull(reqVO.getTaskId())){
                paymentApplicationMapper.updateById(paymentApplicationDO.setIsDeferred(reqVO.getIsDeferred())
                );
                bpmTaskApi.approveTask(getLoginUserId(), new BpmTaskApproveReqDTO().setId(reqVO.getTaskId()));
            }
        }
        return applicationId;
    }

    private BigPaymentApplicationListBpmRespVO enhanceBpmPage(PageResult<PaymentApplicationDO> doPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap) {

        List<PaymentApplicationDO> doList = doPageResult.getList();
        List<PaymentApplicationListBpmRespVO> respVOList = PaymentApplicationConverter.INSTANCE.convertBpmDO2Resp(doList);
        if (CollectionUtil.isNotEmpty(doList)) {


            //流程信息（业务和流程同表）
            List<String> doIdList = doList.stream().map(PaymentApplicationDO::getId).collect(Collectors.toList());
            Map<String, PaymentApplicationDO> bpmDOMap = CollectionUtils.convertMap(doList, PaymentApplicationDO::getId);
            List<String> instanceList = doList.stream().map(PaymentApplicationDO::getProcessInstanceId).collect(Collectors.toList());

            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> taskEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> taskDoneEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();

            Long loginUserId = getLoginUserId();
            List<BpmTaskAllInfoRespVO> originalTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskEndTimeAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            List<BpmTaskAllInfoRespVO> taskDoneEndTimeAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            //待处理的任务
            Map<String, BpmTaskAllInfoRespVO> toDoTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> finalTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            if (CollectionUtil.isNotEmpty(instanceList)) {
                originalTaskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(loginUserId), instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(originalTaskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTimeByUserId(originalTaskAllInfoRespVOList, getLoginUserId());
                toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                taskDoneEndTimeAllInfoRespVOList = EcontractUtil.distinctDoneTaskLatestEndTime(originalTaskAllInfoRespVOList);
                taskDoneEndTimeMap = CollectionUtils.convertMap(taskDoneEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
                finalTaskMap = CollectionUtils.convertMap(EcontractUtil.distinctTask(bpmTaskApi.getAllTaskIdByProcessInstanceIds(instanceList)), BpmTaskAllInfoRespVO::getProcessInstanceId);
            }
            //有结束时间的流程任务
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskEndTimeAllInfoRespVOList = EcontractUtil.distinctTaskHaveEndTime(originalTaskAllInfoRespVOList);
                taskEndTimeMap = CollectionUtils.convertMap(taskEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }
            List<String> applicationIds = respVOList.stream().map(PaymentApplicationListBpmRespVO::getId).collect(Collectors.toList());
            Map<String, List<PaymentScheduleDO>> plansByContarctIdMap = new HashMap<String, List<PaymentScheduleDO>>();
            List<PaymentScheduleDO> plans = paymentScheduleMapper.selectListByApplicationIds(applicationIds);
            if (CollectionUtil.isNotEmpty(plans)) {
                plansByContarctIdMap = plans.stream()
                        .collect(Collectors.groupingBy(PaymentScheduleDO::getContractId));
            }
            //合同状态
            List<String> contractIds = respVOList.stream().map(PaymentApplicationListBpmRespVO::getContractId).collect(Collectors.toList());
            List<ContractDO> contractDOList = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().select(ContractDO::getId, ContractDO::getStatus).in(ContractDO::getId, contractIds));
            Map<String, ContractDO> contractDOMap = new HashMap<>();
                    if(CollectionUtil.isNotEmpty(contractDOList)) {
                        contractDOMap =  CollectionUtils.convertMap(contractDOList, ContractDO::getId);
                    }

            for (PaymentApplicationListBpmRespVO respVO : respVOList) {
                List<String> buyPlanIds = new ArrayList<String>();
                List<PaymentScheduleDO> scheduleDOList = plansByContarctIdMap.get(respVO.getContractId());
                if (CollectionUtil.isNotEmpty(scheduleDOList)) {
                    buyPlanIds = scheduleDOList.stream().map(PaymentScheduleDO::getId).collect(Collectors.toList());
                }
                respVO.setBuyPlanIds(buyPlanIds);
                //审批状态
                BpmTaskAllInfoRespVO toDoTaskAllInfoRespVO = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(toDoTaskAllInfoRespVO)) {
                    respVO.setFlowableStatus(FlowableUtil.getFlowableStatus(loginUserId, toDoTaskAllInfoRespVO.getAssigneeUserId()));
                } else {
                    respVO.setFlowableStatus(FlowableStatusEnums.DONE.getCode());
                }
                // 状态返回
                BpmTaskAllInfoRespVO nowTaskAllInfoRespVO = finalTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(nowTaskAllInfoRespVO)) {
                    respVO.setStatusName(nowTaskAllInfoRespVO.getName());
                }

                SettlementMethodEnums settlementMethodEnums = SettlementMethodEnums.getInstance(respVO.getSettlementMethod());
                if (ObjectUtil.isNotNull(settlementMethodEnums)) {
                    respVO.setSettlementMethodName(settlementMethodEnums.getInfo());
                }
                PaymentTypeEnums paymentTypeEnums = PaymentTypeEnums.getInstance(respVO.getPaymentType());
                if (ObjectUtil.isNotNull(paymentTypeEnums)) {
                    respVO.setPaymentTypeName(paymentTypeEnums.getInfo());
                }
                BpmProcessInstanceResultEnum bpmProcessInstanceResultEnum = BpmProcessInstanceResultEnum.getInstance(respVO.getResult());
                if (ObjectUtil.isNotNull(bpmProcessInstanceResultEnum)) {
                    respVO.setResultStr(bpmProcessInstanceResultEnum.getDesc());
                }
                //流程任务
                ContractProcessInstanceRelationInfoRespDTO processInstanceRelationInfoRespDTO = instanceRelationInfoRespDTOMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(processInstanceRelationInfoRespDTO)) {
                    respVO.setTaskId(processInstanceRelationInfoRespDTO.getTaskId());
                }
                //历史任务信息（所有审批人）
                BpmTaskAllInfoRespVO historyTask = taskEndTimeMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(historyTask)) {
                    respVO.setHisTaskResult(historyTask.getResult());
                }
                //全部审批列表的审批状态(找不到当前登录人的待办任务，既是已办任务)
                BpmTaskAllInfoRespVO toDoTask = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(toDoTask)) {
                    respVO.setAssigneeId(toDoTask.getAssigneeUserId());
                    respVO.setFlowableStatus(FlowableUtil.getFlowableStatus(loginUserId, toDoTask.getAssigneeUserId()));

                }
                //审批状态(全部里)
                else {
                    respVO.setFlowableStatus(FlowableStatusEnums.DONE.getCode());
                }
                //已审批任务的状态赋值（当前人对该审批的最近的一次操作结果）
                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = instanceRelationInfoRespDTOMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(infoRespDTO)) {
                    respVO.setDoneTaskResult(infoRespDTO.getProcessResult());
                }
                //合同状态
                ContractDO contractDO = contractDOMap.get(respVO.getContractId());
                if(ObjectUtil.isNotNull(contractDO)) {
                    respVO.setContractStatus(contractDO.getStatus());
                }
            }
            PageResult<PaymentApplicationListBpmRespVO> pageResult = new PageResult<PaymentApplicationListBpmRespVO>();
            pageResult.setList(respVOList).setTotal(doPageResult.getTotal());
            BigPaymentApplicationListBpmRespVO result = new BigPaymentApplicationListBpmRespVO().setPageResult(pageResult);
            //获取配置
            result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE)));
            return result;
        }
        BigPaymentApplicationListBpmRespVO result = new BigPaymentApplicationListBpmRespVO()
                .setPageResult(new PageResult<PaymentApplicationListBpmRespVO>()
                        .setList(Collections.emptyList())
                        .setTotal(doPageResult.getTotal())
                );
        result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE)));
        return result;
    }

    /**
     * 审批-待办-列表（审批人查看 ）
     */
    @Override
    public BigPaymentApplicationListBpmRespVO getBpmToDoTaskPage(Long loginUserId, PaymentApplicationListBpmReqVO pageVO) {
        //查询合同类型表，取起草流程key
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
                .eq(ContractType::getTypeStatus, 1));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getPaymentProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        // 查询待办任务
//        if (ObjectUtil.isNotEmpty(definitionKeys)) {
//            processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(definitionKeys.get(1));
//        } else {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.PAYMENT_APPLICATION_APPROVE.getDefinitionKey());
//        }
        bpmTaskApi.getToDoTaskInfoByDefinitionKey(definitionKeys.get(0), definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));
        bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.PAYMENT_PLAN_DEFERRED_APPLICATION.getDefinitionKey());

        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<PaymentApplicationDO> doPageResult = paymentApplicationMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public BigPaymentApplicationListBpmRespVO getBpmDoneTaskPage(Long loginUserId, PaymentApplicationListBpmReqVO pageVO) {
        Integer taskResult = StatusConstants.TEMP_INTEGER;
        // 查询待办任务
        if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
            taskResult = pageVO.getTaskResult();
        }
        //查询合同类型表，取起草流程key
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
                .eq(ContractType::getTypeStatus, 1));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getPaymentProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        //获得已处理任务数据(已过滤掉已取消的任务),可筛选审批状态
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(definitionKeys.get(0), taskResult, definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));

        } else {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.PAYMENT_APPLICATION_APPROVE.getDefinitionKey(), taskResult);
        }
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        // 排除当前人待处理的流程
        // 查询待办任务
        List<ContractProcessInstanceRelationInfoRespDTO> toDoInstanceIdList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            toDoInstanceIdList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(definitionKeys.get(0), definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));
        } else {
            toDoInstanceIdList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.PAYMENT_APPLICATION_APPROVE.getDefinitionKey());
        }
        List<String> todoProcIds = toDoInstanceIdList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        instanceIdList.removeAll(todoProcIds);
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<PaymentApplicationDO> doPageResult = paymentApplicationMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public BigPaymentApplicationListBpmRespVO getBpmAllTaskPage(Long loginUserId, PaymentApplicationListBpmReqVO pageVO) {
        //查询合同类型表，取起草流程key
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().select(ContractType::getDraftApprovalProcess));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getPaymentProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        // 查询所有任务
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(definitionKeys.get(0), definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));
        } else {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.PAYMENT_APPLICATION_APPROVE.getDefinitionKey());
        }
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<PaymentApplicationDO> doPageResult = paymentApplicationMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public StatisticsAmountRespVO statisticsApproveAmount(PaymentApplicationListBpmReqVO vo) {
        int flag = vo.getFlag();
        ApprovePageFlagEnums approvePageFlagEnums = ApprovePageFlagEnums.getInstance(flag);
        switch (approvePageFlagEnums) {
            case ALL:
                return statisticsAll(vo);
            case DONE:
                return statisticsDone(vo);
            case TO_DO:
                return statisticsToDo(vo);
            default:
                return new StatisticsAmountRespVO();
        }
    }

    @Override
    public StatisticsAmountV2RespVO statisticsV2() {

        BigDecimal receiptAmount = new BigDecimal(0);
        BigDecimal doneReceiptAmount = new BigDecimal(0);
        BigDecimal toReceiptAmount = new BigDecimal(0);


        BigDecimal payAmount = new BigDecimal(0);
        BigDecimal donePayAmount = new BigDecimal(0);
        BigDecimal toDoPayAmount = new BigDecimal(0);

        BigDecimal purchaseAmount = new BigDecimal(0);
        BigDecimal engPurchaseAmount = new BigDecimal(0);
        BigDecimal servPurchaseAmount = new BigDecimal(0);
        BigDecimal goodsPurchaseAmount = new BigDecimal(0);
        LedgerPageReqV2VO vo = new LedgerPageReqV2VO();
        List<Integer> statusList = new ArrayList<>();
        //只查询已签署完成的合同
        statusList.add(ContractStatusEnums.SIGN_COMPLETED.getCode());
        vo.setStatus(statusList);
        Map amountTotalMap = ledgerService.getTotal(vo);
        receiptAmount = new BigDecimal(String.valueOf(amountTotalMap.get("sumCollect")));
        doneReceiptAmount = new BigDecimal(String.valueOf(amountTotalMap.get("collected")));
        toReceiptAmount = new BigDecimal(String.valueOf(amountTotalMap.get("uncollected")));

        payAmount = new BigDecimal(String.valueOf(amountTotalMap.get("sumPay")));
        donePayAmount = new BigDecimal(String.valueOf(amountTotalMap.get("paid")));
        toDoPayAmount = new BigDecimal(String.valueOf(amountTotalMap.get("unpaid")));

//        //所有申请
//        List<PaymentApplicationDO> paymentApplicationDOList = paymentApplicationMapper.selectList(new LambdaQueryWrapperX<PaymentApplicationDO>()
//                .select(PaymentApplicationDO::getCurrentPayAmount
//                        , PaymentApplicationDO::getApplicantId, PaymentApplicationDO::getCollectionType, PaymentApplicationDO::getResult));
//        //所有付款申请
//        List<PaymentApplicationDO> payList = paymentApplicationDOList.stream().filter(p -> 0 == p.getCollectionType()).collect(Collectors.toList());
//        payAmount = payList.stream()
//                .map(PaymentApplicationDO::getCurrentPayAmount)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        List<PaymentApplicationDO> payedPayList = payList.stream().filter(
//                p -> Objects.equals(BpmProcessInstanceResultEnum.APPROVE.getResult(), p.getResult())).collect(Collectors.toList());
//        if (CollectionUtil.isNotEmpty(payedPayList)) {
//            donePayAmount = payedPayList.stream()
//                    .map(PaymentApplicationDO::getCurrentPayAmount)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//            toDoPayAmount = payAmount.subtract(donePayAmount).setScale(2, RoundingMode.HALF_UP);
//        }
//
//
//        //所有收款申请
//        List<PaymentApplicationDO> payeeList = paymentApplicationDOList.stream().filter(p -> 1 == p.getCollectionType()).collect(Collectors.toList());
//        receiptAmount = payeeList.stream()
//                .map(PaymentApplicationDO::getCurrentPayAmount)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        List<PaymentApplicationDO> doneReceiptList = payeeList.stream().filter(
//                p -> Objects.equals(BpmProcessInstanceResultEnum.APPROVE.getResult(), p.getResult())).collect(Collectors.toList());
//        if (CollectionUtil.isNotEmpty(doneReceiptList)) {
//            doneReceiptAmount = doneReceiptList.stream()
//                    .map(PaymentApplicationDO::getCurrentPayAmount)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//            toReceiptAmount = receiptAmount.subtract(doneReceiptAmount).setScale(2, RoundingMode.HALF_UP);
//        }


        //所有采购合同金额
        List<ContractDO> contractDOList = contractMapper.select4Purchase();
        if (CollectionUtil.isNotEmpty(contractDOList)) {
            Double amount = contractDOList.stream()
                    .mapToDouble(ContractDO::getAmount)
                    .sum();
            purchaseAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
        }
        List<ContractDO> engContractDOList = contractMapper.select4ContractType("工程");
        if (CollectionUtil.isNotEmpty(engContractDOList)) {
            Double amount = engContractDOList.stream()
                    .mapToDouble(ContractDO::getAmount)
                    .sum();
            engPurchaseAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
        }
        List<ContractDO> servContractDOList = contractMapper.select4ContractType("服务");
        if (CollectionUtil.isNotEmpty(servContractDOList)) {
            Double amount = servContractDOList.stream()
                    .mapToDouble(ContractDO::getAmount)
                    .sum();
            servPurchaseAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
        }
        List<ContractDO> goodsContractDOList = contractMapper.select4ContractType("货物");
        if (CollectionUtil.isNotEmpty(goodsContractDOList)) {
            Double amount = goodsContractDOList.stream()
                    .mapToDouble(ContractDO::getAmount)
                    .sum();
            goodsPurchaseAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
        }

        return new StatisticsAmountV2RespVO()
                .setPayAmount(payAmount)
                .setDonePayAmount(donePayAmount)
                .setToDoPayAmount(toDoPayAmount)
                .setReceiptAmount(receiptAmount)
                .setDoneReceiptAmount(doneReceiptAmount)
                .setToReceiptAmount(toReceiptAmount)
                .setPurchaseAmount(purchaseAmount)
                .setServPurchaseAmount(servPurchaseAmount)
                .setGoodsPurchaseAmount(goodsPurchaseAmount)
                .setEngPurchaseAmount(engPurchaseAmount)
                ;
    }

    /**
     * ABANDON
     */
    private StatisticsAmountRespVO statisticsToDo(PaymentApplicationListBpmReqVO vo) {
        //查询合同类型表，取流程key
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
                .eq(ContractType::getTypeStatus, 1));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getCollectionProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        // 查询待办任务
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(definitionKeys.get(0), definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));
        } else {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.PAYMENT_APPLICATION_APPROVE.getDefinitionKey());
        }
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .collect(Collectors.toList());
        List<PaymentApplicationDO> toDaoList = paymentApplicationMapper.statisticsToDo(vo, instanceIdList);

        return new StatisticsAmountRespVO();
    }

    private StatisticsAmountRespVO statisticsDone(PaymentApplicationListBpmReqVO vo) {
        return new StatisticsAmountRespVO();
    }

    private StatisticsAmountRespVO statisticsAll(PaymentApplicationListBpmReqVO vo) {
        return new StatisticsAmountRespVO();
    }

}
