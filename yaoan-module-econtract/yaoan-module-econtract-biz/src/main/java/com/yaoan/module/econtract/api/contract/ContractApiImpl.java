package com.yaoan.module.econtract.api.contract;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.bpm.api.task.dto.TaskForWarningReqDTO;
import com.yaoan.module.bpm.enums.WithdrawalFlagEnums;
import com.yaoan.module.econtract.api.contract.dto.*;
import com.yaoan.module.econtract.api.contract.dto.gpx.*;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.convert.contract.PaymentScheduleConverter;
import com.yaoan.module.econtract.convert.contract.ext.gcy.ContractOrderExtConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractFileConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractFileMapper;
import com.yaoan.module.econtract.convert.gpx.GPXConverter;
import com.yaoan.module.econtract.convert.term.ContractTermConverter;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangeDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangePaymentDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractPerformanceLogDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.trading.PurchaseDO;
import com.yaoan.module.econtract.dal.dataobject.contract.trading.TradingSupplierDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.ContractFileDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.rel.ContractOrderRelDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.GPXContractQuotationRelDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplScheRelDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.relativeContact.RelativeContact;
import com.yaoan.module.econtract.dal.dataobject.term.ContractTermDO;
import com.yaoan.module.econtract.dal.dataobject.terminate.TerminateContractDO;
import com.yaoan.module.econtract.dal.mysql.bpm.contract.BpmContractMapper;
import com.yaoan.module.econtract.dal.mysql.change.BpmContractChangeMapper;
import com.yaoan.module.econtract.dal.mysql.change.BpmContractChangePaymentMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractPerformanceLogMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.PurchaseMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.TradingSupplierMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.rel.ContractOrderRelMapper;
import com.yaoan.module.econtract.dal.mysql.gpx.GPXContractQuotationRelMapper;
import com.yaoan.module.econtract.dal.mysql.ledger.LedgerMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplicationMapper;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.dal.mysql.performance.perforTask.PerforTaskMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.relativeContact.RelativeContactMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.dal.mysql.term.ContractTermMapper;
import com.yaoan.module.econtract.dal.mysql.terminate.TerminateContractMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import com.yaoan.module.econtract.framework.core.event.warning.EcmsWarningEvent;
import com.yaoan.module.econtract.framework.core.event.warning.EcmsWarningEventPublisher;
import com.yaoan.module.econtract.util.gcy.Sm4Utils;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.EcontractOrgApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.service.notify.NotifySendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DATA_ERROR;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;

/**
 * ContractApi 实现类
 *
 * @author doujl
 */
@Service
@Slf4j
public class ContractApiImpl implements ContractApi {

    @Resource
    private ContractMapper contractMapper;
    @Resource
    private LedgerMapper ledgerMapper;
    @Resource
    private ContractPerforMapper contractPerforMapper;
    @Resource
    PerforTaskMapper perforTaskMapper;
    @Resource
    private TerminateContractMapper terminateContractMapper;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private BpmContractChangeMapper bpmContractChangeMapper;
    @Resource
    private BpmContractChangePaymentMapper changePaymentMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractProcessApi contractProcessApi;
    @Resource
    private BpmContractMapper bpmContractMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Value("${feign.client.contract.client_id}")
    private String clientId;
    @Value("${feign.client.contract.client_secret}")
    private String clientSecret;
    @Resource
    private ContractOrderRelMapper contractOrderRelMapper;
    @Resource
    private ContractTermMapper contractTermMapper;
    @Resource
    private ContractOrderExtMapper contractOrderExtMapper;
    @Resource
    private PaymentApplicationMapper paymentApplicationMapper;
    @Resource
    private ContractPerformanceLogMapper contractPerformanceLogMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private GPXContractQuotationRelMapper gpxContractQuotationRelMapper;
    @Resource
    private PurchaseMapper purchaseMapper;
    @Resource
    private TradingSupplierMapper trxSupplierMapper;
    @Resource
    private ContractFileMapper contractFileMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private ContractApi contractApi;
    @Resource
    private CompanyApi companyApi;

    @Resource
    private NotifySendService notifySendService;
    @Resource
    private RelativeContactMapper relativeContactMapper;
    @Resource
    private EcontractOrgApi econtractOrgApi;
    @Resource
    private EcmsWarningEventPublisher warningEventPublisher;
    @Override
    @DataPermission(enable = false)
    public void notifyContractStatus(String processInstanceId, ContractStatusEnums statusEnums) {
        //（相对方逻辑）免租户
        AtomicReference<ContractDO> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                ContractDO contractDO = contractMapper.selectOne(ContractDO::getProcessInstanceId, processInstanceId);

                if (ObjectUtil.isNotNull(contractDO)) {

                    //合同确认加入确认时间
                    if (ContractStatusEnums.TO_BE_CONFIRMED == statusEnums) {
                        contractDO.setConfirmTime(LocalDateTime.now());
                    }

//        if (ContractStatusEnums.TO_BE_SIGNED == statusEnums) {
//            Ledger ledger = ledgerMapper.selectOne(Ledger::getCode, contractDO.getCode());
//            if (ledger == null) {
//                ledgerMapper.insert(new Ledger().setContractStatus(statusEnums.getCode()).setName(contractDO.getName()).setCode(contractDO.getCode()).setContractType(contractDO.getContractType()).setSignTime(LocalDateTime.now()));
//            }
//        }
//
//        if (statusEnums.getCode() > ContractStatusEnums.TO_BE_SIGNED.getCode()) {
//            Ledger ledger = ledgerMapper.selectOne(Ledger::getCode, contractDO.getCode());
//            if (ledger != null) {
//                ledger.setContractStatus(statusEnums.getCode());
//                ledgerMapper.updateById(ledger);
//            }
//
//        }

                    if (ContractStatusEnums.SIGN_COMPLETED == statusEnums) {
                        contractDO.setSignDate(new Date());
                        contractDO.setIsSign(IfNumEnums.YES.getCode());

                        // 更新 relative_signatory_rel 表中 relative_signatory_rel.is_sign = 1
                        setRelativeSign(contractDO.getId(), IfNumEnums.YES.getCode(), "SIGN");
                    }

                    if (ContractStatusEnums.TERMINATE_SIGNIND == statusEnums) {
                        //修改履约状态-终止合同签署中
                        String contractId = terminateContractMapper.selectOne(TerminateContractDO::getProcessInstanceId, processInstanceId).getContractId();
                        ContractPerformanceDO contractPerformanceDO = contractPerforMapper.selectOne(ContractPerformanceDO::getContractId, contractId);
                        contractPerformanceDO.setContractStatus(ContractPerfEnums.TERMINATE_SIGNIND.getCode());
                        contractPerforMapper.updateById(contractPerformanceDO);
                    }

                    if (ContractStatusEnums.TERMINATED == statusEnums) {
                        //修改履约状态-终止合同签署完成
                        String contractId = terminateContractMapper.selectOne(TerminateContractDO::getProcessInstanceId, processInstanceId).getContractId();
                        ContractPerformanceDO contractPerformanceDO = contractPerforMapper.selectOne(ContractPerformanceDO::getContractId, contractId);
                        contractPerformanceDO.setContractStatus(ContractPerfEnums.TERMINATED.getCode());
                        contractPerforMapper.updateById(contractPerformanceDO);
                    }

                    if (ContractStatusEnums.SIGN_COMPLETED == statusEnums) {
                        //签署完成修改扩展表的签署完成时间
                        if (contractDO.getUpload() == ContractUploadTypeEnums.ORDER_DRAFT.getCode() || contractDO.getUpload() == ContractUploadTypeEnums.THIRD_PARTY.getCode()) {
                            //
                            ContractOrderExtDO contractOrderExtDO = new ContractOrderExtDO();
                            contractOrderExtDO.setId(contractDO.getId()).setStatus(ContractStatusEnums.SIGN_COMPLETED.getCode()).setContractSignTime(new Date());
                            contractOrderExtMapper.updateById(contractOrderExtDO);
                        }

                        //修改归档状态为0-待归档
                        contractDO.setDocument(0);
                        Long count = contractPerforMapper.selectCount(new LambdaQueryWrapperX<ContractPerformanceDO>()
                                .eqIfPresent(ContractPerformanceDO::getContractId, contractDO.getId())
                                .eqIfPresent(ContractPerformanceDO::getCreator, String.valueOf(WebFrameworkUtils.getLoginUserId())));
                        if (count == 0) {
                            //将签署完成的合同信息放入合同履约表
                            inserContractPerfor(contractDO, String.valueOf(WebFrameworkUtils.getLoginUserId()));
                            inserContractPerfor(contractDO, contractDO.getCreator());

                        }
                    }

                    //修改主合同状态
                    contractDO.setStatus(statusEnums.getCode());
                    contractMapper.updateById(contractDO);

                    //免租户结尾
                }
            });

        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void contractChange(String processInstanceId) {
        //根据流程实例id获取变动信息
        BpmContractChangeDO bpmContractChangeDO = bpmContractChangeMapper.selectOne(BpmContractChangeDO::getProcessInstanceId, processInstanceId);
        if (ObjectUtil.isEmpty(bpmContractChangeDO)) {
            log.error("合同变动审批流程不存在，流程id: {}", processInstanceId);
            throw exception(DIY_ERROR, "请确认流程是否已启动");

        }
        List<BpmContractChangePaymentDO> bpmContractChangePaymentDOS = changePaymentMapper.selectList(BpmContractChangePaymentDO::getChangeId, bpmContractChangeDO.getId());
        //更新合同信息
        ContractDO contractDO = new ContractDO();
        contractDO.setId(bpmContractChangeDO.getMainContractId());
        contractDO.setCode(bpmContractChangeDO.getChangeCode());
        contractDO.setName(bpmContractChangeDO.getChangeName());
        contractDO.setExpirationDate(bpmContractChangeDO.getChangeExpirationDate());
        //变更不改变合同信息，只改变计划信息
        //contractMapper.updateById(contractDO);
        if (ObjectUtil.isNotEmpty(bpmContractChangePaymentDOS)) {
            //对变动计划进行分组  新增的计划一组，修改的计划另一组
            List<BpmContractChangePaymentDO> addBpmContractChangePaymentDOS = new ArrayList();
            List<BpmContractChangePaymentDO> updateBpmContractChangePaymentDOS = new ArrayList();
            for (BpmContractChangePaymentDO bpmContractChangePaymentDO : bpmContractChangePaymentDOS) {
                if (bpmContractChangePaymentDO.getPaymentId() != null) {
                    updateBpmContractChangePaymentDOS.add(bpmContractChangePaymentDO);
                } else {
                    addBpmContractChangePaymentDOS.add(bpmContractChangePaymentDO);
                }
            }
            LambdaQueryWrapperX<PaymentScheduleDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<PaymentScheduleDO>().eq(PaymentScheduleDO::getContractId, bpmContractChangeDO.getMainContractId());

            //删除已被删除的计划
            if (updateBpmContractChangePaymentDOS.size() > 0) {
                List<String> updateIds = updateBpmContractChangePaymentDOS.stream().map(BpmContractChangePaymentDO::getPaymentId).filter(Objects::nonNull).collect(Collectors.toList());
                lambdaQueryWrapperX.notIn(PaymentScheduleDO::getId, updateIds);
            }
            paymentScheduleMapper.delete(lambdaQueryWrapperX);
            //对新增的计划进行处理
            if (addBpmContractChangePaymentDOS.size() > 0) {
                List<PaymentScheduleDO> changePaymentList = PaymentScheduleConverter.INSTANCE.toChangePaymentList(addBpmContractChangePaymentDOS);
                paymentScheduleMapper.insertBatch(changePaymentList);
            }
            //对修改的计划进行处理
            if (updateBpmContractChangePaymentDOS.size() > 0) {
                List<PaymentScheduleDO> changePaymentList = PaymentScheduleConverter.INSTANCE.toChangePaymentList(updateBpmContractChangePaymentDOS);
                Map<String, List<BpmContractChangePaymentDO>> planByContractIdMap = updateBpmContractChangePaymentDOS.stream().filter(Objects::nonNull)
                        .collect(Collectors.groupingBy(BpmContractChangePaymentDO::getId));
                for (PaymentScheduleDO paymentScheduleDO : changePaymentList) {
                    paymentScheduleDO.setId(planByContractIdMap.get(paymentScheduleDO.getId()).get(0).getPaymentId());
                }
                paymentScheduleMapper.updateBatch(changePaymentList);
            }
        }

        //解除申请的合同 待执行计划(没有产生申请的计划)都会冻结
        List<PaymentApplScheRelDO> applScheRelDOS = new ArrayList<>();
        if(ContractChangeTypeEnums.TERMINATE.getCode().equals(bpmContractChangeDO.getChangeType())){

        }
//        contractMapper.updateById(new ContractDO().setIsLocked(0).setId(bpmContractChangeDO.getMainContractId()));
        //将合同改回原来的状态
        contractMapper.updateById(new ContractDO().setStatus(bpmContractChangeDO.getProtoStatus()).setId(bpmContractChangeDO.getMainContractId()));
        if (ContractChangeTypeEnums.TERMINATE.getCode().equals(bpmContractChangeDO.getChangeType()) || ContractChangeTypeEnums.CANCELLATION.getCode().equals(bpmContractChangeDO.getChangeType())) {
            List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, bpmContractChangeDO.getMainContractId());
            if (ObjectUtil.isNotEmpty(paymentScheduleDOS)) {
                List<ContractPerformanceLogDO> contractPerformanceLogDOS = new ArrayList<>();
                paymentScheduleDOS.forEach(item -> {
                    //变动类型为解除时，需对已履行的部分进行结算，未履行的部分进行终止处理，合同履约状态为关闭状态
                    if (ContractChangeTypeEnums.TERMINATE.getCode().equals(bpmContractChangeDO.getChangeType())) {
                        if (!PaymentScheduleStatusEnums.DONE.getCode().equals(item.getStatus())) {
                            item.setStatus(PaymentScheduleStatusEnums.CLOSE.getCode());
                        }
                        //变动状态类型为作废时，需对全部履约计划均为关闭处理,合同履约状态为关闭状态，付款申请删除
                    } else if (ContractChangeTypeEnums.CANCELLATION.getCode().equals(bpmContractChangeDO.getChangeType())) {
                        item.setStatus(PaymentScheduleStatusEnums.CLOSE.getCode());
                    }
                    ContractPerformanceLogDO contractPerformanceLogDO = new ContractPerformanceLogDO();
                    contractPerformanceLogDO.setBusinessId(item.getId());
                    contractPerformanceLogDO.setBillId(item.getId());
                    contractPerformanceLogDO.setModuleName("履约");
                    contractPerformanceLogDO.setOperateName("修改了");
                    contractPerformanceLogDO.setOperateContent("履约计划");
                    contractPerformanceLogDOS.add(contractPerformanceLogDO);
                });
                paymentScheduleMapper.updateBatch(paymentScheduleDOS);
                contractPerformanceLogMapper.insertBatch(contractPerformanceLogDOS);
            }
            paymentApplicationMapper.delete(new LambdaQueryWrapperX<PaymentApplicationDO>().eq(PaymentApplicationDO::getContractId, bpmContractChangeDO.getMainContractId()));
            contractMapper.updateById(new ContractDO().setStatus(ContractStatusEnums.PERFORMANCE_CLOSURE.getCode()).setId(bpmContractChangeDO.getMainContractId()));
        }
        if (ContractChangeTypeEnums.TERMINATED.getCode().equals(bpmContractChangeDO.getChangeType())) {
            contractMapper.updateById(new ContractDO().setStatus(ContractStatusEnums.PERFORMANCE_CLOSURE.getCode()).setId(bpmContractChangeDO.getMainContractId()));
        }
        if (ContractChangeTypeEnums.CANCEL.getCode().equals(bpmContractChangeDO.getChangeType())) {
            contractMapper.updateById(new ContractDO().setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode()).setId(bpmContractChangeDO.getMainContractId()));
        }
    }

    /**
     * 合同审批通过发送审批结果到电子合同
     *
     * @param processInstanceId
     */
    @Override
    public void productApproveSendEcms(String processInstanceId, String contractId) {
        //根据系统参数判断是否同步黑龙江
        String cvalue = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if ("n".equals(cvalue)) {
            return;
        }
        if (contractId == null || contractId.isEmpty()) {
            BpmContract bpmContract = bpmContractMapper.selectOne(BpmContract::getProcessInstanceId, processInstanceId);
            if (ObjectUtil.isNull(bpmContract)) {
                log.error("合同审批流程不存在,流程id:{},请检查流程状态或联系管理员", processInstanceId);
                throw exception(DIY_ERROR, "请检查流程状态或联系管理员");
            }
            contractId = bpmContract.getContractId();
        }
        ContractDO contractDO = contractMapper.selectById(contractId);
        ContractOrderExtDO contractOrderExtDO = contractOrderExtMapper.selectById(contractId);
        //String contractId = contractDO.getId();
        if (ObjectUtil.isNull(contractOrderExtDO) || ObjectUtil.isNull(contractDO)) {
            throw exception(DIY_ERROR, contractId + "合同数据缺失");
        }
        //只有来自第三方系统和基于订单起草的合同才同步至黑龙江
        if (contractDO.getUpload() != ContractUploadTypeEnums.THIRD_PARTY.getCode() && contractDO.getUpload() != ContractUploadTypeEnums.ORDER_DRAFT.getCode()) {
            log.error("请确认合同状态是否符合发送条件，只有来自第三方系统和基于订单起草的合同才同步至黑龙江");
            throw exception(DIY_ERROR, "该合同不可发送，请确认合同状态是否符合发送条件");
        }
        if (ContractUploadTypeEnums.THIRD_PARTY.getCode().equals(contractDO.getUpload())) {
            if (PlatformEnums.GPMS_GPX.getCode().equals(contractOrderExtDO.getPlatform())) {
                //交易的合同由于下一岗就是签署，所以此处特殊处理
                Integer status = ContractStatusEnums.TO_BE_SIGNED.getCode();
                if (contractApi.isNeedSignet(null, contractDO.getId())) {
                    //如果配置用印申请参数，则需要走用印申请
                    status = ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode();
                }
                contractDO.setStatus(status);
                contractMapper.updateById(contractDO);
                //黑龙江起草的合同，终审后不同步黑龙江，直接进入签署流程。进入签署流程的逻辑在BpmContractApiImpl中
                return;
            }
        }
        // 获取电子合同token
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("client_id", clientId);
        bodyParam.put("client_secret", clientSecret);
        String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
        JSONObject jsonObject = JSONObject.parseObject(token);
        if (jsonObject.get("error") != null) {
            throw exception(DIY_ERROR, jsonObject.getString("error_description"));
        }
        // 判断推送合同还是推送审批状态
        String result = "";
//        try {
        //如果是单位端起草的合同
        if (ContractUploadTypeEnums.ORDER_DRAFT.getCode().equals(contractDO.getUpload())) {
            // 交易的合同
            if (PlatformEnums.GPMS_GPX.getCode().equals(contractOrderExtDO.getPlatform())) {
                GPXContractCreateReqDTO gpxContractCreateReqDTO = GPXConverter.INSTANCE.gpxDO2DTO(contractOrderExtDO);

                List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, contractDO.getId());
                if (CollectionUtil.isNotEmpty(paymentScheduleDOList)) {
                    //如果是阶段计划的
                    if (StringUtils.isNotBlank(paymentScheduleDOList.get(0).getStageName())) {
                        List<StagePaymentDTO> stagePaymentDTOList = PaymentScheduleConverter.INSTANCE.gpxDO2StageDTOList(paymentScheduleDOList);
                        gpxContractCreateReqDTO.setPayMentInfo(stagePaymentDTOList);
                    } else {
                        //支付计划信息--ContractPaymentPlanMapper
                        List<GPXPaymentPlanDTO> paymentPlanDTOList = PaymentScheduleConverter.INSTANCE.gpxDO2PaymentPlanDTOList(paymentScheduleDOList);
                        gpxContractCreateReqDTO.setPaymentPlanList(paymentPlanDTOList);
                    }
                }


                //报价明细List
                List<GPXContractQuotationRelDO> quotationRelDOList = gpxContractQuotationRelMapper.selectList(GPXContractQuotationRelDO::getContractId, contractId);
                if (CollectionUtil.isNotEmpty(quotationRelDOList)) {
                    List<GPXContractQuotationRelDTO> quotationRelDTOList = GPXConverter.INSTANCE.quoDO2DTO(quotationRelDOList);
                    gpxContractCreateReqDTO.setQuotationRelReqVOList(quotationRelDTOList);
                }

                //合同采购内容List
                List<PurchaseDO> purchaseDOList = purchaseMapper.selectList(PurchaseDO::getContractId, contractId);
                if (CollectionUtil.isNotEmpty(purchaseDOList)) {
                    List<PurchaseDTO> purchaseDTOList = GPXConverter.INSTANCE.purchaseDO2DTOList(purchaseDOList);
                    gpxContractCreateReqDTO.setPurchaseVOList(purchaseDTOList);
                }

                //供应商数据集合List
                List<TradingSupplierDO> tradingSupplierDOList = trxSupplierMapper.selectList(TradingSupplierDO::getContractId, contractId);
                if (CollectionUtil.isNotEmpty(tradingSupplierDOList)) {
                    List<TradingSupplierDTO> tradingSupplierDTOList = GPXConverter.INSTANCE.tradingSupplierDO2DTOList(tradingSupplierDOList);
                    gpxContractCreateReqDTO.setTradingSupplierVOList(tradingSupplierDTOList);
                }
                //附件idList
                List<ContractFileDO> fileDOList = contractFileMapper.selectList(ContractFileDO::getContractId, contractId);
                if (CollectionUtil.isNotEmpty(fileDOList)) {
                    List<ContractFileDTO> contractFileDTOList = ContractFileConverter.INSTANCE.toDTOS(fileDOList);
                    gpxContractCreateReqDTO.setContractFileDTOList(contractFileDTOList);
                }
                gpxContractCreateReqDTO.setCreator(contractOrderExtDO.getCreator());
                gpxContractCreateReqDTO.setContractDrafterStr(adminUserApi.getUser(getLoginUserId()).getNickname());

                EncryptDTO encryptDTO = null;
                try {
                    encryptDTO = Sm4Utils.convertParam(gpxContractCreateReqDTO.setUpload(ContractUploadTypeEnums.ORDER_DRAFT.getCode()));
                } catch (Exception e) {
                    log.error("构建传输数据异常！contractId= " + contractId);
                    throw exception(DIY_ERROR, "请稍后重试或联系技术支持。");
                }
                result = contractProcessApi.pushGPXContractToEcontract(jsonObject.getString("access_token"), encryptDTO);
                JSONObject resultJson = JSONObject.parseObject(result);
                if (!"0".equals(resultJson.getString("code"))) {
                    throw exception(DIY_ERROR, result);
                }
                log.info("电子合同返回同步结果" + result);
                //同步合同状态-已发送
                log.info("同步合同状态-已发送");
                // contractDO.setStatus(ContractStatusEnums.SENT.getCode());
                //交易的合同由于下一岗就是签署，所以此处特殊处理
                Integer status = ContractStatusEnums.TO_BE_SIGNED.getCode();
                if (contractApi.isNeedSignet(null, contractDO.getId())) {
                    //如果配置用印申请参数，则需要走用印申请
                    status = ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode();
                }
                contractDO.setStatus(status);
                contractMapper.updateById(contractDO);
                contractOrderExtDO.setStatus(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_SURE.getCode());
                contractOrderExtMapper.updateById(contractOrderExtDO);
                return;
            } else {

                //生成新订单
                if (StringUtils.isEmpty(contractOrderExtDO.getOrderId())) {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    String formattedDateTime = "DD" + now.format(formatter);
                    contractOrderExtDO.setOrderId(formattedDateTime);
                    contractOrderExtMapper.updateById(contractOrderExtDO);
                }
                ContractOrderExtDTO contractOrderExtDTO = ContractOrderExtConverter.INSTANCE.toContractOrderExtDTO(contractOrderExtDO);
                contractOrderExtDTO.setFromProduct(true);

                //支付计划
                List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, contractOrderExtDTO.getId());
                if (CollectionUtil.isNotEmpty(paymentScheduleDOS)) {
                    if (StringUtils.isNotBlank(paymentScheduleDOS.get(0).getStageName())) {
                        //阶段计划
                        contractOrderExtDTO.setPayMentInfo(PaymentScheduleConverter.INSTANCE.gpxDO2StageDTOList(paymentScheduleDOS));
                    } else {
                        //其他的支付计划
                        contractOrderExtDTO.setPaymentPlanList(PaymentScheduleConverter.INSTANCE.PaymentListToDTO(paymentScheduleDOS));
                    }
                }

                //订单合并情况
                List<ContractOrderRelDO> contractOrderRelDOS = contractOrderRelMapper.selectList(ContractOrderRelDO::getContractId, contractId);
                if (CollectionUtil.isNotEmpty(contractOrderRelDOS) && contractOrderRelDOS.size() > 1) {
                    contractOrderExtDTO.setOrderIds(contractOrderRelDOS.stream().map(ContractOrderRelDO::getOrderId).collect(Collectors.toList()));
                } else if (CollectionUtil.isNotEmpty(contractOrderRelDOS) && contractOrderRelDOS.size() == 1) {
                    contractOrderExtDTO.setOrderId(contractOrderRelDOS.get(0).getOrderId());
                }
                contractOrderExtDTO.setCreator(contractOrderExtDO.getCreator());

                // 合同条款信息
                List<ContractTermDO> contractTermDOS = contractTermMapper.selectList(ContractTermDO::getContractId, contractId);
                if (CollectionUtil.isNotEmpty(contractTermDOS)) {
                    contractOrderExtDTO.setTerms(ContractTermConverter.INSTANCE.convertDTOList(contractTermDOS));
                }

                //上传附件
                if (contractOrderExtDTO.getPdfFileId() != 0 && contractOrderExtDTO.getRemark9() != null && "99".equals(contractOrderExtDTO.getRemark9())) {
                    uploadFileToThirdParty(contractOrderExtDTO, jsonObject.getString("access_token"));
                }
                EncryptDTO encryptDTO = null;
                try {
                    encryptDTO = Sm4Utils.convertParam(contractOrderExtDTO.setIsSendFlag(3));
                } catch (Exception e) {
                    throw exception(DIY_ERROR, "构建传输数据异常！contractId= " + contractId);
                }
                result = contractProcessApi.pushContractToEcontract(jsonObject.getString("access_token"), encryptDTO);
                log.info("电子合同返回结果" + result);
                JSONObject resultJson = JSONObject.parseObject(result);
                if (!"0".equals(resultJson.getString("code"))) {
                    throw exception(DIY_ERROR, result);
                }
            }
            log.info("同步合同状态-已发送");
            //同步合同状态-已发送
            contractDO.setStatus(ContractStatusEnums.SENT.getCode());
            contractMapper.updateById(contractDO);
            contractOrderExtDO.setStatus(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_TOBECONFIRMED.getCode());
            contractOrderExtMapper.updateById(contractOrderExtDO);
        }
        //如果是黑龙江起草的合同 且不是交易的合同
        if (ContractUploadTypeEnums.THIRD_PARTY.getCode().equals(contractDO.getUpload())) {
            if (PlatformEnums.GPMS_GPX.getCode().equals(contractOrderExtDO.getPlatform())) {
                //交易的处理放在上方终审处理中，否则状态会被覆盖
//                    //交易的合同由于下一岗就是签署，所以此处特殊处理
//                    Integer status = ContractStatusEnums.TO_BE_SIGNED.getCode();
//                    if(systemConfigApi.ifNeedSignet() ){
//                        //如果配置用印申请参数，则需要走用印申请
//                        status = ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode();
//                    }
//                    contractMapper.updateById(new ContractDO().setId(contractDO.getId()).setStatus(status));
                return;
            } else {
                //其他类型的合同
                EncryptDTO encryptDTO = null;
                try {
                    encryptDTO = Sm4Utils.convertParam(new ContractProcessDTO().setContractId(contractId));
                } catch (Exception e) {
                    throw exception(DIY_ERROR, "构建传输数据异常！contractId= " + contractId);
                }
                result = contractProcessApi.sendContract(jsonObject.getString("access_token"), encryptDTO);
                contractDO.setStatus(ContractStatusEnums.SENT.getCode());
                contractMapper.updateById(contractDO);
                contractOrderExtDO.setStatus(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_SURE.getCode());
                contractOrderExtMapper.updateById(contractOrderExtDO);
            }

        }
        log.info("电子合同返回结果" + result);
        JSONObject resultJson = JSONObject.parseObject(result);
        if (!"0".equals(resultJson.getString("code"))) {
            throw exception(DIY_ERROR, resultJson.getString("message"));
        }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw exception(ErrorCodeConstants.DIY_ERROR, jsonObject.getString("error_description"));
//        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void productSignSendEcms(String contractId) {
        //根据系统参数判断是否同步黑龙江
        String cvalue = systemConfigApi.getConfigByKey("is_send_heilongjiang");
        if ("n".equals(cvalue)) {
            return;
        }
        ContractDO contractDO = contractMapper.selectById(contractId);
        if (ObjectUtil.isNull(contractDO)) {
            return;
        }
//        List<ContractDO> contractDOList= contractMapper.selectList(new LambdaQueryWrapper<ContractDO>().eq(ContractDO::getProcessInstanceId, processInstanceId));
//        if(contractDOList.size() == 0){
//            return;
//        }
//        ContractDO contractDO = contractDOList.get(0);
        //只有来自第三方系统和基于订单起草的合同才同步至黑龙江
        if (contractDO.getUpload() != ContractUploadTypeEnums.THIRD_PARTY.getCode() && contractDO.getUpload() != ContractUploadTypeEnums.ORDER_DRAFT.getCode()) {
            return;
        }
        //黑龙江终审调用接口也会操作以下表，会锁表
        //采购人已签署
        //contractMapper.updateById(new ContractDO().setId(contractId).setIsSign(1));
        //添加采购人签章时间  
        //contractOrderExtMapper.updateById(new ContractOrderExtDO().setId(contractDO.getId()).setOrgSinTime(new Date()));
        //获取电子合同token
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("client_id", clientId);
        bodyParam.put("client_secret", clientSecret);
        String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
        JSONObject jsonObject = JSONObject.parseObject(token);
        if (jsonObject.get("error") != null) {
            log.error("oauthCenterToken：token获取异常");
            throw exception(DIY_ERROR, jsonObject.getString("请联系管理员查看具体错误信息"));
        }
        //上传文件到单位端
        MultipartFile multipartFile = null;
        try {
            FileDTO fileDTO = fileApi.selectById(contractDO.getPdfFileId());
            byte[] fileContent = fileApi.getFileContentById(contractDO.getPdfFileId());
            System.out.println("【推送通用端】获取文件数据" + fileContent.length);
            multipartFile = new MockMultipartFile("file", fileDTO.getName(), "application/pdf", fileContent);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("构建传输数据异常！contractId= " + contractId);
            throw exception(DIY_ERROR, "请稍后重试或联系技术支持。");
        }
        try {
            ContractFileDTO contractFileDTO = new ContractFileDTO();
            contractFileDTO.setFile(multipartFile);
            contractFileDTO.setCode(7);
            String uploadInfo = contractProcessApi.contractUpload(jsonObject.getString("access_token"), contractFileDTO);
            System.out.println("推送通用端上传文件返回:" + uploadInfo);
            JSONObject filejsonObject = JSONObject.parseObject(uploadInfo);
            String data = filejsonObject.get("data").toString();
            JSONObject fileInfo = filejsonObject.parseObject(data);
            Long id = Long.valueOf(fileInfo.get("fileId").toString());
            String url = fileInfo.get("url").toString();

            //同步合同状态和文件id
            ContractSendDTO contract = new ContractSendDTO();
            contract.setId(contractId);
            contract.setPdfFileId(id);
            contract.setPdfFilePath(url);
            contract.setOrgSinTime(new Date());
            contract.setUpdater(adminUserApi.getUser(getLoginUserId()).getNickname());
            EncryptDTO encryptDTO = Sm4Utils.convertParam(contract);
            String result = contractProcessApi.pushSignContract(jsonObject.getString("access_token"), encryptDTO);
            System.out.println("单位端推送签署后合同状态返回:" + result);
            JSONObject resultJson = JSONObject.parseObject(result);
            if (!"0".equals(resultJson.getString("code"))) {
                log.error(result);
                throw exception(DIY_ERROR, "请稍后重试或联系管理员。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【签署同步】推送数据异常{}", e.getMessage());
            throw exception(DIY_ERROR, "请稍后重试或联系管理员。");
        }

    }

    public void uploadFileToThirdParty(ContractOrderExtDTO contractDO, String token) {
        //上传文件到单位端
        MultipartFile multipartFile = null;
        try {
            FileDTO fileDTO = fileApi.selectById(contractDO.getPdfFileId());
            byte[] fileContent = fileApi.getFileContentById(contractDO.getPdfFileId());
            System.out.println("【推送通用端】获取文件数据" + fileContent.length);
            multipartFile = new MockMultipartFile("file", fileDTO.getName(), "application/pdf", fileContent);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【推送通用端】获取文件数据异常,请稍后重试或联系管理员。" + e.getMessage());
            throw exception(DIY_ERROR, "请稍后重试或联系管理员。");
        }
        try {
            ContractFileDTO contractFileDTO = new ContractFileDTO();
            contractFileDTO.setFile(multipartFile);
            contractFileDTO.setCode(7);
            String uploadInfo = contractProcessApi.contractUpload(token, contractFileDTO);
            System.out.println("推送通用端上传文件返回:" + uploadInfo);
            JSONObject filejsonObject = JSONObject.parseObject(uploadInfo);
            String data = filejsonObject.get("data").toString();
            JSONObject fileInfo = filejsonObject.parseObject(data);
            Long id = Long.valueOf(fileInfo.get("fileId").toString());
            String url = fileInfo.get("url").toString();
            contractDO.setPdfFileId(id);
            contractDO.setPdfFilePath(url);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传附件到通用端系统异常" + e.getMessage());
            throw exception(DIY_ERROR, "请检查网络或文件状态后重试。");
        }
    }

    @Override
    public void updateSign(String processInstanceId) {
        ContractDO contractDO = contractMapper.selectOne(ContractDO::getProcessInstanceId, processInstanceId);
        if (ObjectUtil.isNotEmpty(contractDO)) {
            //采购人签署时在合同表添加采购人签署的标识
            contractMapper.update(null, new LambdaUpdateWrapper<ContractDO>().eq(ContractDO::getId, contractDO.getId()).set(ContractDO::getIsSign, true).set(ContractDO::getStatus, ContractStatusEnums.TO_BE_SIGNED.getCode()));
            // 修改相对方合同关联表，每个相对方的签署状态为未签署，确认状态为未确认
            List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractDO.getId());
            signatoryRelDOS.forEach(item -> {
                item.setIsSign(IfNumEnums.NO.getCode());
            });
            signatoryRelMapper.updateBatch(signatoryRelDOS);
        }
    }

    @Override
    public void updateRelativeSignOrConfirm(String processInstanceId, String type) {
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                // 判断流程中是否存在退回操作，如果有则指定流程返回待发送节点，修改合同状态。
                ContractDO contractDO = contractMapper.selectOne(ContractDO::getProcessInstanceId, processInstanceId);
                // 更新 relative_signatory_rel 表中 relative_signatory_rel.is_sign = 1
                if (ObjectUtil.isNotEmpty(contractDO)) {
                    setRelativeSign(contractDO.getId(), IfNumEnums.YES.getCode(), type);
                }
            });
        });

    }

    @Override
    public void updateSignReject(String processInstanceId) {
        ContractDO contractDO = contractMapper.selectOne(ContractDO::getProcessInstanceId, processInstanceId);
        if (ObjectUtil.isNotEmpty(contractDO)) {
            //采购人签署时在合同表添加采购人签署的标识
            contractMapper.update(null, new LambdaUpdateWrapper<ContractDO>().eq(ContractDO::getId, contractDO.getId())/*.set(ContractDO::getIsSign, IfNumEnums.NO.getCode())*/.set(ContractDO::getStatus, ContractStatusEnums.SIGN_REJECTED.getCode()));
        }
    }
    @Override
    public void updateConfirmReject(String processInstanceId){
        ContractDO contractDO = contractMapper.selectOne(ContractDO::getProcessInstanceId, processInstanceId);
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        List<RelativeContact> relativeContacts = relativeContactMapper.selectList(RelativeContact::getUserId,loginUser.getId());
        if(CollectionUtil.isEmpty(relativeContacts)){
            throw exception(DATA_ERROR);
        }
        RelativeContact relativeContact = relativeContacts.get(0);

        if (ObjectUtil.isNotEmpty(contractDO)) {
            //采购人签署时在合同表添加采购人签署的标识
            signatoryRelMapper.update(null, new LambdaUpdateWrapper<SignatoryRelDO>()
                    .eq(SignatoryRelDO::getContractId, contractDO.getId())
                    .eq(SignatoryRelDO::getSignatoryId, relativeContact.getRelativeId())
                    .set(SignatoryRelDO::getIsConfirm, IfNumEnums.RJ.getCode())
            );
            // 确认拒绝 和 签署拒绝 都是 已拒绝 状态
            contractMapper.update(null, new LambdaUpdateWrapper<ContractDO>().eq(ContractDO::getId, contractDO.getId()).set(ContractDO::getStatus, ContractStatusEnums.SIGN_REJECTED.getCode()));

        }
    }
    @Override
    public void updateRelativeSignReject(String processInstanceId, String type) {
        // 因为合同的相对方可能不属于采购方租户，所以需要忽略租户才能查询到合同
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                ContractDO contractDO = contractMapper.selectOne(ContractDO::getProcessInstanceId, processInstanceId);
                // 更新 relative_signatory_rel 表中 relative_signatory_rel.is_sign = 0'
                if (ObjectUtil.isNotEmpty(contractDO)) {
                    setRelativeSign(contractDO.getId(), IfNumEnums.RJ.getCode(), type);
                }
            });
        });
    }

    @Override
    public void rejectByRelativeSign(String processInstanceId) {
        // 修改合同状态为被退回
        notifyContractStatus(processInstanceId, ContractStatusEnums.BE_SENT_BACK);
    }


    @Override
    public Boolean isNeedSignet(String processInstanceId, String contractId) {
        boolean result = false;
        ContractDO contractDO = new ContractDO();
        if (ObjectUtil.isNotEmpty(contractId)) {
            contractDO = contractMapper.selectById(contractId);
        } else if (ObjectUtil.isNotEmpty(processInstanceId)) {
            contractDO = contractMapper.selectOne(ContractDO::getProcessInstanceId, processInstanceId);
        } else {
            throw exception(ErrorCodeConstants.EMPTY_REQUIRE_PARAM_ERROR);
        }
        if (ObjectUtil.isNotEmpty(contractDO)) {
            //获取合同类型中的是否需要用印
            ContractType contractType = contractTypeMapper.selectById(contractDO.getContractType());
            if (ObjectUtil.isNotEmpty(contractType)) {
                if (contractType.getNeedSignet()) {
                    result = true;
                }
            }
        } else {
            throw exception(ErrorCodeConstants.CONTRACT_NAME_NOT_EXISTS);
        }
        return result;
    }


    private void inserContractPerfor(ContractDO contractDO, String creator) {
        //将签署完成的合同信息放入合同履约表
        ContractPerformanceDO contractPerformance = new ContractPerformanceDO();
        if (StringUtils.isNotEmpty(creator)) {
            Long userId = Long.parseLong(creator);
            AdminUserRespDTO user = adminUserApi.getUser(userId);
            if (ObjectUtil.isNotNull(user)) {
                contractPerformance.setDeptId(user.getDeptId());
            }
        }

        contractPerformance.setContractCode(contractDO.getCode());
        contractPerformance.setContractId(contractDO.getId());
        contractPerformance.setContractName(contractDO.getName());
        contractPerformance.setSignFinishTime(new Date());
        contractPerformance.setContractTypeId(contractDO.getContractType());
        //合同履约状态为待建立履约
        contractPerformance.setContractStatus(ContractPerfEnums.WAIT_CREATE_PERFORMANCE.getCode());
        contractPerformance.setCreator(creator);
        //新增合同履约信息

        contractPerforMapper.insert(contractPerformance);
        //新增履约任务

    }


    @Override
    public List<Long> calculateUsers4OrgSup(String processInstanceId) {

        return null;
    }

    @Override
    public void notifyContractRejectByRelative(String processInstanceId) {
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                ContractDO contractDO = contractMapper.selectOne(ContractDO::getProcessInstanceId, processInstanceId);
                if (ObjectUtil.isNotEmpty(contractDO)){
                    warningEventPublisher.setEvent(new EcmsWarningEvent(this).setFlag("1").setBussinessId(contractDO.getId()));
                }
            });
        });
    }


    @Override
    public void isContainsSigned(String processInstanceId) {
        ContractDO contractDO = contractMapper.selectOne(ContractDO::getProcessInstanceId, processInstanceId);
        if (ObjectUtil.isNotEmpty(contractDO)) {
            List<RelativeContact> relativeContacts = relativeContactMapper.selectList(RelativeContact::getUserId, getLoginUser().getId());
            String relativeId = "";
            //如果相对方与用户关系列表查不到该用户所属相对方，就查相对方表
            if (CollectionUtil.isEmpty(relativeContacts)) {
                List<Relative> relatives = relativeMapper.selectList(Relative::getContactId, getLoginUser().getId());
                if (CollectionUtil.isNotEmpty(relatives)) {
                    relativeId = relatives.get(0).getId();
                }
            } else {
                relativeId = relativeContacts.get(0).getRelativeId();
            }
            List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractDO.getId(), SignatoryRelDO::getIsSign, IfNumEnums.YES.getCode());
            if (CollectionUtil.isNotEmpty(signatoryRelDOS)) {
                List<String> relativeIds = signatoryRelDOS.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(relativeIds)) {
                    relativeIds.remove(relativeId); //移除操作人所属相对方
                    List<Relative> relativeList = relativeMapper.selectList(Relative::getId, relativeIds);
                    if (CollectionUtil.isNotEmpty(relativeList)) {
                        List<String> relativeNames = relativeList.stream().map(Relative::getName).collect(Collectors.toList());
                        throw exception(DIY_ERROR, "成员方" + relativeNames + "公司已签署，不可发起退回操作");
                    }
                }
            }
        }
    }

    @Override
    public Map<Long, Long> getRelativeNameByUserIdMap(String contractId) {
        Map<Long, Long> result = new HashMap<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                List<SignatoryRelDO> signatoryRelDOList = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractId);

                Map<String, Long> stringLongMap = signatoryRelDOList.stream().filter(item -> ObjectUtil.isNotNull(item.getContactId())).collect(Collectors.toMap(SignatoryRelDO::getSignatoryId, SignatoryRelDO::getContactId));
                // 相对方id集合
                List<String> relativeIds = signatoryRelDOList.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(relativeIds)) {
                    List<Relative> relativeDOList = relativeMapper.selectList(Relative::getId, relativeIds);
                    List<RelativeContact> relativeContacts = relativeContactMapper.selectList(RelativeContact::getRelativeId, relativeIds);
                    if (CollectionUtil.isNotEmpty(relativeDOList)) {
                        Map<Long,String> relativeNameMap = relativeDOList.stream().filter(item -> ObjectUtil.isNotNull(item.getVirtualId())).collect(Collectors.toMap(Relative::getVirtualId, Relative::getId));
                        relativeNameMap.forEach((k,v)->{
                            result.put(k, stringLongMap.get(v));
                        });
//                result.putAll(relativeNameMap);
                    }
//            if (CollectionUtil.isNotEmpty(relativeContacts)) {
//                Map<Long, String> userRelativeIdMap = relativeContacts.stream().filter(item -> ObjectUtil.isNotNull(item.getUserId())).collect(Collectors.toMap(RelativeContact::getUserId, RelativeContact::getRelativeId));
//                List<String> relativeUserIds = relativeContacts.stream().filter(item -> ObjectUtil.isNotNull(item.getUserId())).map(RelativeContact::getRelativeId).collect(Collectors.toList());
//                List<Relative> relatives = relativeMapper.selectList(Relative::getId, relativeUserIds);
//                Map<String, String> relativeUserMap = relatives.stream().collect(Collectors.toMap(Relative::getId, Relative::getName));
//                Map<Long, String> retMap = new HashMap<>();
//                if (CollectionUtil.isNotEmpty(userRelativeIdMap)) {
//                    userRelativeIdMap.forEach((k, v) -> {
//                        retMap.put(k, relativeUserMap.get(v));
//                    });
//                    result.putAll(retMap);
//                }
//            }
                }
            });
        });
        return result;
    }

    @Override
    public EcontractOrgDTO getEcontractOrgFromZCById(String orgId) {
        // 获取电子合同token
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("client_id", clientId);
        bodyParam.put("client_secret", clientSecret);
        String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
        JSONObject jsonObject = JSONObject.parseObject(token);
        if (jsonObject.get("error") != null) {
            throw exception(DIY_ERROR, jsonObject.getString("error_description"));
        }
        com.yaoan.module.econtract.api.contract.dto.EcontractOrgDTO result = contractProcessApi.getEcontractOrgById(jsonObject.getString("access_token"), orgId);

        if (ObjectUtil.isNotEmpty(result)) {
            econtractOrgApi.saveContractOrg(BeanUtils.toBean(result, com.yaoan.module.system.api.user.dto.EcontractOrgDTO.class));
        }
        log.info("电子合同返回同步结果" + result);
        return result;
    }

    @Override
    public void withdrawal(String processInstanceId, Integer flag) {
        WithdrawalFlagEnums flagEnums = WithdrawalFlagEnums.getInstance(flag);
        if (ObjectUtil.isNull(flagEnums)) {
            throw exception(DIY_ERROR, "数据异常，请联系管理员");
        }
        switch (flagEnums) {
            case CLOSING:
                handleClose(processInstanceId);
                break;
            default:
                break;
        }

    }

    @Override
    public void warningNotify(String processInstanceId) {
        warningEventPublisher.setEvent(new EcmsWarningEvent(this).setFlag("1").setTaskParams(new TaskForWarningReqDTO().setProcessInstanceId(processInstanceId)));
    }

    @Resource
    private BpmContractChangeMapper contractChangeMapper;

    private void handleClose(String processInstanceId) {
        List<BpmContractChangeDO> list = contractChangeMapper.selectList(BpmContractChangeDO::getProcessInstanceId, processInstanceId);
        if (CollectionUtil.isEmpty(list)) {
            throw exception(DATA_ERROR);
        }
        BpmContractChangeDO bpmContractChangeDO = list.get(0);
        ContractDO contractDO = contractMapper.selectOne(new LambdaQueryWrapperX<ContractDO>()
                .eq(ContractDO::getId, bpmContractChangeDO.getMainContractId())
                .select(ContractDO::getId, ContractDO::getOldStatus, ContractDO::getStatus)
        );
        if (ObjectUtil.isNull(contractDO)) {
            throw exception(DATA_ERROR);
        }
        List<PaymentScheduleDO> scheduleDOList = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, bpmContractChangeDO.getMainContractId());
        if (CollectionUtil.isEmpty(scheduleDOList)) {
            throw exception(DATA_ERROR);
        }
        //从"解除中"退回草稿箱
        //恢复合同状态
        if (ObjectUtil.isNotNull(contractDO.getOldStatus())) {
            contractMapper.updateById(contractDO.setStatus(contractDO.getOldStatus()));
        }
        //恢复计划状态
        //计划状态：未开始
        if (CollectionUtil.isNotEmpty(scheduleDOList)) {
            scheduleDOList.forEach(scheduleDO -> {
                scheduleDO.setStatus(PaymentScheduleStatusEnums.TO_DO.getCode());
            });
            paymentScheduleMapper.updateBatch(scheduleDOList);
        }
    }


    private void setRelativeSign(String contractId, Integer signFlag, String type) {

        Boolean rejectFlag = false;
        List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractId);
        List<Relative> loginUserRelatives = new ArrayList<>();
        if (IfNumEnums.RJ.getCode().equals(signFlag)){
            signFlag = IfNumEnums.YES.getCode();
            rejectFlag = true;
            //筛选出待处理得相对方
            List<String> relaIds = signatoryRelDOS.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
            loginUserRelatives = relativeMapper.selectList(Relative::getId, relaIds);
        } else {
            // 查出当前登录人所属相对方
            loginUserRelatives = relativeMapper.selectList4Relative(getLoginUser().getId());
        }
        ContractDO contractDO = contractMapper.selectById(contractId);
        if (String.valueOf(getLoginUserId()).equals(contractDO.getCreator()) || CollectionUtil.isEmpty(loginUserRelatives)){
            if ("CONFIRM".equals(type)) {
            } else {
                contractDO.setIsSign(signFlag);
            }
        }
        if (rejectFlag){
            contractDO.setIsSign(signFlag).setStatus(ContractStatusEnums.SIGN_REJECTED.getCode());
        }
        contractMapper.updateById(contractDO);

        // 所有相对方统一处理
        List<String> userRelativeIds = loginUserRelatives.stream().map(Relative::getId).collect(Collectors.toList());
        // 循环出合同中在当前登录人相对方集合中的相对方数据
        if (CollectionUtil.isNotEmpty(userRelativeIds)){
            for (SignatoryRelDO signatoryRelDO : signatoryRelDOS) {
                if (userRelativeIds.contains(signatoryRelDO.getSignatoryId())) {
                    if ("CONFIRM".equals(type)) {
                        SignatoryRelDO updateDO = new SignatoryRelDO().setId(signatoryRelDO.getId()).setIsConfirm(signFlag);
                        if (!rejectFlag) {
                            updateDO.setContactId(getLoginUserId());
                        }
                        signatoryRelMapper.updateById(updateDO);
                    } else {
                        signatoryRelMapper.updateById(new SignatoryRelDO().setId(signatoryRelDO.getId()).setIsSign(signFlag));
                    }
                }
            }
        }
    }
}
