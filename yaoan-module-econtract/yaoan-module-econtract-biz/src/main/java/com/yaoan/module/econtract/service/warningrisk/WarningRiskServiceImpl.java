package com.yaoan.module.econtract.service.warningrisk;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.econtract.controller.admin.warningrisk.vo.*;
import com.yaoan.module.econtract.convert.warningrisk.RiskConverter;
import com.yaoan.module.econtract.dal.dataobject.borrow.BorrowContractDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow.ContractBorrowBpmDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.warningdata.WarningDataDO;
import com.yaoan.module.econtract.dal.dataobject.warningleveltype.WarningLevelTypeDO;
import com.yaoan.module.econtract.dal.dataobject.warningruleconfig.WarningRuleConfigDO;
import com.yaoan.module.econtract.dal.dataobject.warningruleconfigdetail.WarningRuleConfigDetailDO;
import com.yaoan.module.econtract.dal.dataobject.warningruleexpression.WarningRuleExpressionDO;
import com.yaoan.module.econtract.dal.mysql.borrow.BorrowContractMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contractborrow.ContractBorrowBpmMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.warningdata.WarningDataMapper;
import com.yaoan.module.econtract.dal.mysql.warningleveltype.WarningLevelTypeMapper;
import com.yaoan.module.econtract.dal.mysql.warningruleconfig.WarningRuleConfigMapper;
import com.yaoan.module.econtract.dal.mysql.warningruleconfigdetail.WarningRuleConfigDetailMapper;
import com.yaoan.module.econtract.dal.mysql.warningruleexpression.WarningRuleExpressionMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import com.yaoan.module.system.dal.dataobject.notify.NotifyTemplateDO;
import com.yaoan.module.system.dal.mysql.notify.NotifyTemplateMapper;
import com.yaoan.module.system.service.notify.NotifySendService;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.CODE_EXIST_ERROR;

/**
 * 预警结果 Service 实现类
 *
 * @author lls
 */
@Service
@Validated
public class WarningRiskServiceImpl implements WarningRiskService {

    private static final String LESS_THAN = "<";
    private static final String EQUALS = "=";
    private static final String GREATER_THAN = ">";

    @Resource
    private WarningDataMapper warningDataMapper;

    @Resource
    private WarningRuleConfigDetailMapper warningRuleConfigDetailMapper;

    @Resource
    private WarningRuleConfigMapper warningRuleConfigMapper;
    @Resource
    private WarningRuleExpressionMapper warningRuleExpressionMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private NotifySendService notifySendService;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private BorrowContractMapper borrowContractMapper;
    @Resource
    private ContractBorrowBpmMapper contractBorrowBpmMapper;
    @Resource
    private WarningLevelTypeMapper warningLevelTypeMapper;
    @Resource
    private NotifyTemplateMapper notifyTemplateMapper;


    @Override
    public void updateWarningData(WarningRiskEditReqVO updateReqVO) {
        // 校验存在
        validateWarningDataExists(updateReqVO.getId());
        // 更新
        WarningRuleConfigDO updateObj = BeanUtils.toBean(updateReqVO, WarningRuleConfigDO.class);
        warningRuleConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarningData(String id) {
        // 校验存在
        validateWarningDataExists(id);
        // 删除
        warningRuleConfigMapper.deleteById(id);
    }

    private void validateWarningDataExists(String id) {
        if (warningRuleConfigMapper.selectById(id) == null) {
//            throw exception(WARNING_DATA_NOT_EXISTS);
        }
    }

    @Override
    public WarningRiskRespVO getWarningData(String id) {
        //获取风险基本信息
        WarningRuleConfigDO warningRuleConfigDO = warningRuleConfigMapper.selectById(id);
        WarningRiskRespVO riskRespVO = RiskConverter.INSTANCE.toRiskRespVO(warningRuleConfigDO);
        //获取风险配置信息
        List<WarningRuleConfigDetailDO> warningRuleConfigDetailDOList = warningRuleConfigDetailMapper.selectList(new LambdaUpdateWrapper<WarningRuleConfigDetailDO>().eq(WarningRuleConfigDetailDO::getWarningRuleId, id));
        List<WarningRuleConfigDetailSaveReqVO> detailSaveReqVOList = RiskConverter.INSTANCE.toDetailSaveReqVOList(warningRuleConfigDetailDOList);
        //获取提醒内容
        List<Long> ids = warningRuleConfigDetailDOList.stream().map(WarningRuleConfigDetailDO::getPushTemplateId).collect(Collectors.toList());
        List<NotifyTemplateDO> notifyTemplateDOS = notifyTemplateMapper.selectList(NotifyTemplateDO::getId, ids);
        Map<Long, NotifyTemplateDO> notifyTemplateDOMap = notifyTemplateDOS.stream().collect(Collectors.toMap(NotifyTemplateDO::getId, Function.identity()));
        //获取提醒类型名称
        List<String> warningTypeList = warningRuleConfigDetailDOList.stream().map(WarningRuleConfigDetailDO::getWarningType).collect(Collectors.toList());
        List<WarningRuleExpressionDO> warningRuleExpressionDOS = warningRuleExpressionMapper.selectList(WarningRuleExpressionDO::getId, warningTypeList);
        Map<String, WarningRuleExpressionDO> warningRuleExpressionDOMap = warningRuleExpressionDOS.stream().collect(Collectors.toMap(WarningRuleExpressionDO::getId, Function.identity()));
        detailSaveReqVOList.forEach(item -> {
            if(ObjectUtil.isNotEmpty(item.getPushTemplateId())){
                item.setPushTemplateName(notifyTemplateDOMap.get(item.getPushTemplateId()).getContent());
            }
           item.setWarningName(warningRuleExpressionDOMap.get(item.getWarningType()).getName());
                }
        );
        riskRespVO.setWarningRuleConfigDetailList(detailSaveReqVOList);
        return riskRespVO;
    }

    @Override
    public PageResult<WarningRuleConfigDO> getWarningDataPage(WarningRiskPageReqVO pageReqVO) {
        if(ObjectUtil.isNotEmpty(pageReqVO.getIds())){
            PageResult<WarningRuleConfigDO> pageResult = new PageResult<>();
            return pageResult.setList(warningRuleConfigMapper.selectList(WarningRuleConfigDO::getId, pageReqVO.getIds()));
        }
        return warningRuleConfigMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional
    public String editEnable(WarningRiskEditReqVO editReqVO) {

        if (StringUtils.isEmpty(editReqVO.getId())) {
            throw new RuntimeException("id不能为空");
        }

        //修改预警配置表
        LambdaUpdateWrapper<WarningRuleConfigDO> updateWrapperP = new LambdaUpdateWrapper<>();
        updateWrapperP.set(WarningRuleConfigDO::getEnable, editReqVO.getEnable())
                .eq(WarningRuleConfigDO::getId, editReqVO.getId());

        warningRuleConfigMapper.update(null, updateWrapperP);


        //修改规则明细表
        LambdaUpdateWrapper<WarningRuleConfigDetailDO> updateWrapperP2 = new LambdaUpdateWrapper<>();
        updateWrapperP2.set(WarningRuleConfigDetailDO::getEnable, editReqVO.getEnable())
                .eq(WarningRuleConfigDetailDO::getWarningRuleId, editReqVO.getId());

        warningRuleConfigDetailMapper.update(null, updateWrapperP2);

        //todo 后期可用枚举配置
        return "true";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrUpdateWarningRule(WarningRuleConfigSaveReqVO reqVO) {
        if (ObjectUtil.isNotEmpty(reqVO)) {
            if (ObjectUtil.isNotEmpty(reqVO.getId())) {
                WarningRuleConfigDO ruleConfigDO = RiskConverter.INSTANCE.toRuleConfigDO(reqVO);
                warningRuleConfigMapper.updateById(ruleConfigDO);
                List<WarningRuleConfigDetailDO> ruleConfigDetailDOList = RiskConverter.INSTANCE.toRuleConfigDetailDOList(reqVO.getWarningRuleConfigDetailList());
                ruleConfigDetailDOList.forEach(detail -> detail.setWarningRuleId(ruleConfigDO.getId()));
                warningRuleConfigDetailMapper.updateBatch(ruleConfigDetailDOList);
                return ruleConfigDO.getId();
            } else {
                WarningRuleConfigDO ruleConfigDO = RiskConverter.INSTANCE.toRuleConfigDO(reqVO);
                warningRuleConfigMapper.insert(ruleConfigDO);
                List<WarningRuleConfigDetailDO> ruleConfigDetailDOList = RiskConverter.INSTANCE.toRuleConfigDetailDOList(reqVO.getWarningRuleConfigDetailList());
                ruleConfigDetailDOList.forEach(detail -> detail.setWarningRuleId(ruleConfigDO.getId()));
                warningRuleConfigDetailMapper.insertBatch(ruleConfigDetailDOList);
                return ruleConfigDO.getId();
            }
        }

        return "";
    }

    @Override
    public void getCreditRiskStatus(String id) {
        //获取相对方信用情况
        int creditRiskStatus = 80;
        //获取相对方经营情况
        int businessStatus = 0;
        //获取相对方资信风险规则
        WarningRuleConfigDO warningRuleConfigDO = warningRuleConfigMapper.selectOne(WarningRuleConfigDO::getWarningRuleCode, WarningRuleEnums.XDFZXFX.getCode());
        List<WarningRuleConfigDetailDO> warningRuleConfigDOS = warningRuleConfigDetailMapper.selectList(WarningRuleConfigDetailDO::getWarningRuleId, warningRuleConfigDO.getId());
        List<WarningRuleExpressionDO> warningRuleExpressionDOS = warningRuleExpressionMapper.selectList();
        List<WarningLevelTypeDO> warningLevelTypeDOS = warningLevelTypeMapper.selectList();
        Map<Integer,WarningLevelTypeDO> warningLevelTypeMap = warningLevelTypeDOS.stream().collect(Collectors.toMap(WarningLevelTypeDO::getLevel, Function.identity()));
        Map<String, WarningRuleExpressionDO> map = warningRuleExpressionDOS.stream().collect(Collectors.toMap(WarningRuleExpressionDO::getId, Function.identity()));
        for (WarningRuleConfigDetailDO detail : warningRuleConfigDOS) {
            //信用情况
            if (map.get(detail.getWarningType()).getCode().equals("004")) {
                if (creditRiskStatus < detail.getWarningDays()) {
                   throw exception(ErrorCodeConstants.COUNTERPART_CREDIT_ERROR,warningLevelTypeMap.get(detail.getWarningLevel()).getName(),creditRiskStatus);
                }
            }
            //经营情况
            if (map.get(detail.getWarningType()).getCode().equals("005")) {
                if (businessStatus != 0) {
                    throw exception(ErrorCodeConstants.COUNTERPART_OPERATE_ERROR);
                }
            }
        }
    }

    @Override
    public List<WarningLevelTypeRespVO> getLevelTypeList() {
        List<WarningLevelTypeDO> warningLevelTypeDOS = warningLevelTypeMapper.selectList();
         return RiskConverter.INSTANCE.toLevelTypeList(warningLevelTypeDOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void riskAlertReminder() throws Exception {
        //合同签署风险提醒
        contractSignRisk();
        //合同付款风险提醒
        contractPayRisk();
        //合同收款风险提醒
        contractCollectionRisk();
        //合同归档风险提醒
        contractDocumentRisk();
        //借阅归还风险提醒
        borrowReturnRisk();

    }



    private void contractSignRisk() {
        List<WarningDataDO> warningDataDOList = new ArrayList<>();
        // 获取合同表未签署完成的合同
        List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>()
                .in(ContractDO::getStatus, ContractStatusEnums.TO_BE_SENT.getCode(), ContractStatusEnums.BE_SENT_BACK.getCode(),
                        ContractStatusEnums.SENT.getCode(), ContractStatusEnums.TO_BE_CONFIRMED.getCode(),
                        ContractStatusEnums.TO_BE_SIGNED.getCode(), ContractStatusEnums.SIGN_REJECTED.getCode())
                .isNotNull(ContractDO::getExpirationDate)
                .select(ContractDO::getId, ContractDO::getName, ContractDO::getExpirationDate, ContractDO::getCreator, ContractDO::getCompanyId, ContractDO::getTenantId));
        if (ObjectUtil.isNotEmpty(contractDOS)) {
            // 获取风险预警规则配置
            Result result = getResult(WarningRuleEnums.HTQSFX.getCode());
            LocalDate now = LocalDate.now();
            for (ContractDO contractDO : contractDOS) {
                TenantUtils.execute( contractDO.getTenantId() ,()->{
                    if(ObjectUtil.isNotEmpty(contractDO.getExpirationDate())){
                        WarningDataDO warningDataDO = new WarningDataDO();
                        Date expirationDate = contractDO.getExpirationDate();
                        LocalDate localDate = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        long daysDifference = ChronoUnit.DAYS.between(now, localDate);
                        // 处理预警提醒
                        handleWarning(daysDifference, contractDO, warningDataDO, result.detailMap, result.warningRuleConfigDO, warningDataDOList);
                    }
                });
            }
            warningDataMapper.insertBatch(warningDataDOList);
        }
    }


    private void contractPayRisk() {
        List<WarningDataDO> warningDataDOList = new ArrayList<>();
        //获取未支付的付款计划
        MPJLambdaWrapper<PaymentScheduleDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.leftJoin(ContractDO.class, ContractDO::getId, PaymentScheduleDO::getContractId)
                .notIn(PaymentScheduleDO::getStatus, PaymentScheduleStatusEnums.DONE.getCode(), PaymentScheduleStatusEnums.CLOSE.getCode())
                .eq(PaymentScheduleDO::getAmountType, AmountTypeEnums.PAY.getCode())
                .eq(PaymentScheduleDO::getIsRemind, 0)
                .select(PaymentScheduleDO::getContractId, PaymentScheduleDO::getSort, PaymentScheduleDO::getPaymentTime, PaymentScheduleDO::getCreator, PaymentScheduleDO::getId, PaymentScheduleDO::getTenantId);
        List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(wrapper);
        if (ObjectUtil.isNotEmpty(paymentScheduleDOS)) {
            //获取合同名称
            Map<String, ContractDO> contractMap = getStringContractDOMap(paymentScheduleDOS.stream().map(PaymentScheduleDO::getContractId));
            //获取风险预警规则配置
            Result result = getResult(WarningRuleEnums.HTFKFX.getCode());
            Map<String, WarningRuleConfigDetailDO> detailMap = result.detailMap;
            //根据当前时间和付款截止时间是否在预警范围内，返回预警提醒
            LocalDate now = LocalDate.now();
            for (PaymentScheduleDO paymentScheduleDO : paymentScheduleDOS) {
                TenantUtils.execute( paymentScheduleDO.getTenantId() ,()->{
                    WarningDataDO warningDataDO = new WarningDataDO();
                    if(!ObjectUtil.isEmpty(paymentScheduleDO.getPaymentTime())){
                        Date expirationDate = paymentScheduleDO.getPaymentTime();
                        LocalDate localDate = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        long between = ChronoUnit.DAYS.between(now, localDate);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                        String formattedDate = dateFormat.format(expirationDate);
                        //预期提醒
                        if (between == detailMap.get(LESS_THAN).getWarningDays()) {
                            Map<String, Object> templateParams = new HashMap<>();
                            templateParams.put("contractName", contractMap.get(paymentScheduleDO.getContractId()) != null ? contractMap.get(paymentScheduleDO.getContractId()).getName() : paymentScheduleDO.getContractId());
                            templateParams.put("days", detailMap.get(LESS_THAN).getWarningDays());
                            templateParams.put("sort", paymentScheduleDO.getSort());
                            templateParams.put("paymentDeadlineDate", formattedDate);
                            Long l = notifySendService.sendSingleNotifyToAdmin(Long.valueOf(paymentScheduleDO.getCreator()), WarningRulesNotifyTemplateEnums.PAYMENT_EXPECTED_REMINDER.getCode(), templateParams);
                            fillData(null, paymentScheduleDO, null, warningDataDO, detailMap, l, LESS_THAN, null, contractMap, result.warningRuleConfigDO, warningDataDOList);
                        } else if (between == detailMap.get(EQUALS).getWarningDays()) {
                            Map<String, Object> templateParams = new HashMap<>();
                            templateParams.put("contractName", contractMap.get(paymentScheduleDO.getContractId()) != null ? contractMap.get(paymentScheduleDO.getContractId()).getName() : paymentScheduleDO.getContractId());
                            templateParams.put("sort", paymentScheduleDO.getSort());
                            Long l = notifySendService.sendSingleNotifyToAdmin(Long.valueOf(paymentScheduleDO.getCreator()), WarningRulesNotifyTemplateEnums.PAYMENT_EXPIRE_REMINDER.getCode(), templateParams);
                            fillData(null, paymentScheduleDO, null, warningDataDO, detailMap, l, LESS_THAN, null, contractMap, result.warningRuleConfigDO, warningDataDOList);
                        } else if (between < 0) {
                            if (Math.abs(between) == detailMap.get(GREATER_THAN).getWarningDays()) {
                                Map<String, Object> templateParams = new HashMap<>();
                                templateParams.put("contractName", contractMap.get(paymentScheduleDO.getContractId()) != null ? contractMap.get(paymentScheduleDO.getContractId()).getName() : paymentScheduleDO.getContractId());
                                templateParams.put("days", detailMap.get(GREATER_THAN).getWarningDays());
                                templateParams.put("sort", paymentScheduleDO.getSort());
                                templateParams.put("paymentDeadlineDate", formattedDate);
                                Long l = notifySendService.sendSingleNotifyToAdmin(Long.valueOf(paymentScheduleDO.getCreator()), WarningRulesNotifyTemplateEnums.PAYMENT_OVERDUE_REMINDER.getCode(), templateParams);
                                fillData(null, paymentScheduleDO, null, warningDataDO, detailMap, l, GREATER_THAN, null, contractMap, result.warningRuleConfigDO, warningDataDOList);
                            }
                        }
                    }
                });
            }
            warningDataMapper.insertBatch(warningDataDOList);
        }
    }


    private void contractCollectionRisk() {
        List<WarningDataDO> warningDataDOList = new ArrayList<>();
        //获取未支付的收款计划
        MPJLambdaWrapper<PaymentScheduleDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.leftJoin(ContractDO.class, ContractDO::getId, PaymentScheduleDO::getContractId)
                .notIn(PaymentScheduleDO::getStatus, PaymentScheduleStatusEnums.DONE.getCode(), PaymentScheduleStatusEnums.CLOSE.getCode())
                .eq(PaymentScheduleDO::getAmountType, AmountTypeEnums.PAY.getCode())
                .eq(PaymentScheduleDO::getIsRemind, 0)
                .select(PaymentScheduleDO::getContractId, PaymentScheduleDO::getSort, PaymentScheduleDO::getPaymentTime, PaymentScheduleDO::getCreator, PaymentScheduleDO::getId, PaymentScheduleDO::getTenantId);
        List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(wrapper);
        if (ObjectUtil.isNotEmpty(paymentScheduleDOS)) {
            //获取合同名称
            Map<String, ContractDO> contractMap = getStringContractDOMap(paymentScheduleDOS.stream().map(PaymentScheduleDO::getContractId));
            //获取风险预警规则配置
            WarningRuleConfigDO warningRuleConfigDO = warningRuleConfigMapper.selectOne(WarningRuleConfigDO::getWarningRuleCode, WarningRuleEnums.HTSKFX.getCode());
            List<WarningRuleConfigDetailDO> warningRuleConfigDetailDOList = warningRuleConfigDetailMapper.selectList(WarningRuleConfigDetailDO::getWarningRuleId, warningRuleConfigDO.getId());
            Map<String, WarningRuleConfigDetailDO> detailMap = warningRuleConfigDetailDOList.stream()
                    .collect(Collectors.toMap(WarningRuleConfigDetailDO::getWarningTypeExpression, item -> item));
            //根据当前时间和付款截止时间是否在预警范围内，返回预警提醒
            LocalDate now = LocalDate.now();
            for (PaymentScheduleDO paymentScheduleDO : paymentScheduleDOS) {
                TenantUtils.execute( paymentScheduleDO.getTenantId() ,()->{
                    if(ObjectUtil.isNotEmpty(paymentScheduleDO.getPaymentTime())){
                        WarningDataDO warningDataDO = new WarningDataDO();
                        Date expirationDate = paymentScheduleDO.getPaymentTime();
                        LocalDate localDate = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        long between = ChronoUnit.DAYS.between(now, localDate);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                        String formattedDate = dateFormat.format(expirationDate);
                        //预期提醒
                        if (between == detailMap.get(LESS_THAN).getWarningDays()) {
                            Map<String, Object> templateParams = new HashMap<>();
                            templateParams.put("contractName", contractMap.get(paymentScheduleDO.getContractId()) != null ? contractMap.get(paymentScheduleDO.getContractId()).getName() : paymentScheduleDO.getContractId());
                            templateParams.put("days", detailMap.get(LESS_THAN).getWarningDays());
                            templateParams.put("sort", paymentScheduleDO.getSort());
                            templateParams.put("paymentDeadlineDate", formattedDate);
                            Long l = notifySendService.sendSingleNotifyToAdmin(Long.valueOf(paymentScheduleDO.getCreator()), WarningRulesNotifyTemplateEnums.COLLECTION_EXPECTED_REMINDER.getCode(), templateParams);
                            fillData(null, paymentScheduleDO, null, warningDataDO, detailMap, l, LESS_THAN, null, contractMap, warningRuleConfigDO, warningDataDOList);
                        } else if (between == detailMap.get(EQUALS).getWarningDays()) {
                            Map<String, Object> templateParams = new HashMap<>();
                            templateParams.put("contractName", contractMap.get(paymentScheduleDO.getContractId()) != null ? contractMap.get(paymentScheduleDO.getContractId()).getName() : paymentScheduleDO.getContractId());
                            templateParams.put("sort", paymentScheduleDO.getSort());
                            Long l = notifySendService.sendSingleNotifyToAdmin(Long.valueOf(paymentScheduleDO.getCreator()), WarningRulesNotifyTemplateEnums.COLLECTION_EXPIRE_REMINDER.getCode(), templateParams);
                            fillData(null, paymentScheduleDO, null, warningDataDO, detailMap, l, EQUALS, null, contractMap, warningRuleConfigDO, warningDataDOList);
                        } else if (between < 0) {
                            if (Math.abs(between) == detailMap.get(GREATER_THAN).getWarningDays()) {
                                Map<String, Object> templateParams = new HashMap<>();
                                templateParams.put("contractName", contractMap.get(paymentScheduleDO.getContractId()) != null ? contractMap.get(paymentScheduleDO.getContractId()).getName() : paymentScheduleDO.getContractId());
                                templateParams.put("days", detailMap.get(GREATER_THAN).getWarningDays());
                                templateParams.put("sort", paymentScheduleDO.getSort());
                                templateParams.put("paymentDeadlineDate", formattedDate);
                                Long l = notifySendService.sendSingleNotifyToAdmin(Long.valueOf(paymentScheduleDO.getCreator()), WarningRulesNotifyTemplateEnums.COLLECTION_OVERDUE_REMINDER.getCode(), templateParams);

                                fillData(null, paymentScheduleDO, null, warningDataDO, detailMap, l, GREATER_THAN, null, contractMap, warningRuleConfigDO, warningDataDOList);
                            }
                        }
                    }
                });
            }
            warningDataMapper.insertBatch(warningDataDOList);
        }
    }

    private void contractDocumentRisk() {
        List<WarningDataDO> warningDataDOList = new ArrayList<>();
        //查询履约完成的合同并且未归档的合同
        List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>()
                .isNotNull(ContractDO::getPerformanceCompleteDate).isNull(ContractDO::getArchiveTime).select(ContractDO::getId, ContractDO::getName, ContractDO::getPerformanceCompleteDate, ContractDO::getCreator, ContractDO::getTenantId));
        if (ObjectUtil.isNotEmpty(contractDOS)) {
            //获取风险预警规则配置
            WarningRuleConfigDO warningRuleConfigDO = warningRuleConfigMapper.selectOne(WarningRuleConfigDO::getWarningRuleCode, WarningRuleEnums.HTGDFX.getCode());
            List<WarningRuleConfigDetailDO> warningRuleConfigDetailDOList = warningRuleConfigDetailMapper.selectList(WarningRuleConfigDetailDO::getWarningRuleId, warningRuleConfigDO.getId());
            Map<String, WarningRuleConfigDetailDO> detailMap = warningRuleConfigDetailDOList.stream()
                    .collect(Collectors.toMap(WarningRuleConfigDetailDO::getWarningTypeExpression, item -> item));
            //根据当前时间和履约完成时间是否在预警范围内，返回预警提醒
            LocalDate now = LocalDate.now();
            for (ContractDO contractDO : contractDOS) {
                TenantUtils.execute( contractDO.getTenantId() ,()->{
                    if (ObjectUtil.isNotEmpty(contractDO.getPerformanceCompleteDate())) {
                        WarningDataDO warningDataDO = new WarningDataDO();
                        Date expirationDate = contractDO.getPerformanceCompleteDate();
                        LocalDate localDate = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        long between = ChronoUnit.DAYS.between(now, localDate);
                        if (between == detailMap.get(EQUALS).getWarningDays()) {
                            Map<String, Object> templateParams = new HashMap<>();
                            templateParams.put("contractName", contractDO.getName());
                            Long l = notifySendService.sendSingleNotifyToAdmin(Long.valueOf(contractDO.getCreator()), WarningRulesNotifyTemplateEnums.FILE_REMINDER.getCode(), templateParams);
                            fillData(contractDO, null, null, warningDataDO, detailMap, l, EQUALS, null, null, warningRuleConfigDO, warningDataDOList);
                        } else if (between < 0) {
                            Map<String, Object> templateParams = new HashMap<>();
                            templateParams.put("contractName", contractDO.getName());
                            Long l = notifySendService.sendSingleNotifyToAdmin(Long.valueOf(contractDO.getCreator()), WarningRulesNotifyTemplateEnums.FILE_REMINDER.getCode(), templateParams);
                            fillData(contractDO, null, null, warningDataDO, detailMap, l, GREATER_THAN, null, null, warningRuleConfigDO, warningDataDOList);
                        }
                    }
                });
            }
            warningDataMapper.insertBatch(warningDataDOList);
        }
    }

    private void borrowReturnRisk() {
        List<WarningDataDO> warningDataDOList = new ArrayList<>();
        //查询为归还的纸质合同
        List<ContractBorrowBpmDO> borrowContractDOS = contractBorrowBpmMapper.selectList(new LambdaQueryWrapperX<ContractBorrowBpmDO>()
                .isNull(ContractBorrowBpmDO::getActualReturnTime).eq(ContractBorrowBpmDO::getBorrowType, 1));
        if (ObjectUtil.isNotEmpty(borrowContractDOS)) {
            List<String> borrowIds = borrowContractDOS.stream().map(ContractBorrowBpmDO::getBorrowId).collect(Collectors.toList());
            //获取预计归还时间
            List<BorrowContractDO> contractBorrowBpmDOS = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(borrowIds)) {
                contractBorrowBpmDOS = borrowContractMapper.selectList(BorrowContractDO::getBorrowId, borrowIds);
            }
            Map<String, BorrowContractDO> borrowMap = CollectionUtils.convertMap(contractBorrowBpmDOS, BorrowContractDO::getBorrowId);
            Map<String, ContractDO> contractMap = getStringContractDOMap(contractBorrowBpmDOS.stream().map(BorrowContractDO::getContractId));
            //获取风险预警规则配置
            WarningRuleConfigDO warningRuleConfigDO = warningRuleConfigMapper.selectOne(WarningRuleConfigDO::getWarningRuleCode, WarningRuleEnums.JYGHFX.getCode());
            List<WarningRuleConfigDetailDO> warningRuleConfigDetailDOList = warningRuleConfigDetailMapper.selectList(WarningRuleConfigDetailDO::getWarningRuleId, warningRuleConfigDO.getId());
            Map<String, WarningRuleConfigDetailDO> detailMap = warningRuleConfigDetailDOList.stream()
                    .collect(Collectors.toMap(WarningRuleConfigDetailDO::getWarningTypeExpression, item -> item));
            //根据当前时间和预计归还时间是否在预警范围内，返回预警提醒
            LocalDate now = LocalDate.now();
            for (ContractBorrowBpmDO borrowBpmDO : borrowContractDOS) {
                TenantUtils.execute( borrowBpmDO.getTenantId() ,()->{
                    if (ObjectUtil.isNotEmpty(borrowBpmDO.getReturnTime())) {
                        WarningDataDO warningDataDO = new WarningDataDO();
                        Date expirationDate = borrowBpmDO.getReturnTime();
                        LocalDate localDate = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        long between = ChronoUnit.DAYS.between(now, localDate);
                        if (between == detailMap.get(EQUALS).getWarningDays()) {
                            Map<String, Object> templateParams = new HashMap<>();
                            templateParams.put("borrowName", borrowBpmDO.getBorrowName());
                            Long l = notifySendService.sendSingleNotifyToAdmin(Long.valueOf(borrowBpmDO.getCreator()), WarningRulesNotifyTemplateEnums.BORROW_EXPIRE_REMINDER.getCode(), templateParams);
                            fillData(null, null, borrowBpmDO, warningDataDO, detailMap, l, EQUALS, borrowMap, contractMap, warningRuleConfigDO, warningDataDOList);
                        } else if (between < 0) {
                            if (Math.abs(between) == detailMap.get(GREATER_THAN).getWarningDays()) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                                String formattedDate = dateFormat.format(expirationDate);
                                Map<String, Object> templateParams = new HashMap<>();
                                templateParams.put("borrowName",borrowBpmDO.getBorrowName());
                                templateParams.put("days", detailMap.get(GREATER_THAN).getWarningDays());
                                templateParams.put("returnDate", formattedDate);
                                Long l = notifySendService.sendSingleNotifyToAdmin(Long.valueOf(borrowBpmDO.getCreator()), WarningRulesNotifyTemplateEnums.BORROW_OVERDUE_REMINDER.getCode(), templateParams);
                                fillData(null, null, borrowBpmDO, warningDataDO, detailMap, l, GREATER_THAN, borrowMap, contractMap, warningRuleConfigDO, warningDataDOList);
                            }
                        }
                    }
                });
            }
            warningDataMapper.insertBatch(warningDataDOList);
        }
    }

    private Result getResult(String warningRuleEnums) {
        WarningRuleConfigDO warningRuleConfigDO = warningRuleConfigMapper.selectOne(WarningRuleConfigDO::getWarningRuleCode, warningRuleEnums);
        List<WarningRuleConfigDetailDO> warningRuleConfigDetailDOList = warningRuleConfigDetailMapper.selectList(WarningRuleConfigDetailDO::getWarningRuleId, warningRuleConfigDO.getId());
        Map<String, WarningRuleConfigDetailDO> detailMap = warningRuleConfigDetailDOList.stream()
                .collect(Collectors.toMap(WarningRuleConfigDetailDO::getWarningTypeExpression, item -> item));
        return new Result(warningRuleConfigDO, detailMap);
    }

    private static class Result {
        public final WarningRuleConfigDO warningRuleConfigDO;
        public final Map<String, WarningRuleConfigDetailDO> detailMap;

        public Result(WarningRuleConfigDO warningRuleConfigDO, Map<String, WarningRuleConfigDetailDO> detailMap) {
            this.warningRuleConfigDO = warningRuleConfigDO;
            this.detailMap = detailMap;
        }
    }

    private void handleWarning(long daysDifference, ContractDO contractDO, WarningDataDO warningDataDO, Map<String, WarningRuleConfigDetailDO> detailMap, WarningRuleConfigDO warningRuleConfigDO, List<WarningDataDO> warningDataDOList) {
        String warningType = null;
        Long notifyId = null;
        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("contractName", contractDO.getName());
        if (daysDifference == detailMap.get(LESS_THAN).getWarningDays()) {
            templateParams.put("days", detailMap.get(LESS_THAN).getWarningDays());
            notifyId = notifySendService.sendSingleNotifyToAdmin(Long.valueOf(contractDO.getCreator()), WarningRulesNotifyTemplateEnums.SIGN_EXPECTED_REMINDER.getCode(), templateParams);
            warningType = LESS_THAN;
        } else if (daysDifference == detailMap.get(EQUALS).getWarningDays()) {
            notifyId = notifySendService.sendSingleNotifyToAdmin(Long.valueOf(contractDO.getCreator()), WarningRulesNotifyTemplateEnums.SIGN_EXPIRE_REMINDER.getCode(), templateParams);
            warningType = EQUALS;
        } else if (daysDifference < 0 && Math.abs(daysDifference) == detailMap.get(GREATER_THAN).getWarningDays()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            String formattedDate = dateFormat.format(contractDO.getExpirationDate());
            templateParams.put("days", detailMap.get(GREATER_THAN).getWarningDays());
            templateParams.put("expirationDate", formattedDate);
            notifyId = notifySendService.sendSingleNotifyToAdmin(Long.valueOf(contractDO.getCreator()), WarningRulesNotifyTemplateEnums.SIGN_OVERDUE_REMINDER.getCode(), templateParams);
            warningType = GREATER_THAN;
        }

        if (warningType != null && notifyId != null) {
            fillData(contractDO, null, null, warningDataDO, detailMap, notifyId, warningType, null, null, warningRuleConfigDO, warningDataDOList);
        }
    }

    private Map<String, ContractDO> getStringContractDOMap(Stream<String> paymentScheduleDOS) {
        List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>()
                .inIfPresent(ContractDO::getId, paymentScheduleDOS.collect(Collectors.toList())).select(ContractDO::getId, ContractDO::getName));
        return CollectionUtils.convertMap(contractDOS, ContractDO::getId);
    }

    private void fillData(ContractDO contractDO, PaymentScheduleDO paymentScheduleDO, ContractBorrowBpmDO borrowBpmDO, WarningDataDO warningDataDO,
                          Map<String, WarningRuleConfigDetailDO> detailMap, Long notifyId, String flag, Map<String, BorrowContractDO> borrowMap,
                          Map<String, ContractDO> contractMap, WarningRuleConfigDO warningRuleConfigDO, List<WarningDataDO> warningDataDOList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        WarningRuleConfigDetailDO detail = detailMap.get(flag);
        warningDataDO.setWarningLevel(detail.getWarningLevel());
        warningDataDO.setWarningResult(notifyId.toString());
        warningDataDO.setPushType(detail.getPushType());
        if (ObjectUtil.isNotEmpty(contractDO)) {
            warningDataDO.setPushUser(contractDO.getCreator());
            warningDataDO.setCompanyId(contractDO.getCompanyId());
            String formattedDate = dateFormat.format(contractDO.getExpirationDate() != null ? contractDO.getExpirationDate() : contractDO.getPerformanceCompleteDate());
            warningDataDO.setWarningDataValue(formattedDate);
            warningDataDO.setWarningDataId(contractDO.getId());
        } else if (ObjectUtil.isNotEmpty(paymentScheduleDO)) {
            warningDataDO.setPushUser(paymentScheduleDO.getCreator());
            warningDataDO.setCompanyId(contractMap.get(paymentScheduleDO.getContractId()) != null ? contractMap.get(paymentScheduleDO.getContractId()).getCompanyId() : null);
            String formattedDate = dateFormat.format(paymentScheduleDO.getPaymentTime());
            warningDataDO.setWarningDataValue(formattedDate);
            warningDataDO.setWarningDataId(paymentScheduleDO.getId());
        } else if (ObjectUtil.isNotEmpty(borrowBpmDO)) {
            warningDataDO.setPushUser(borrowBpmDO.getCreator());
            warningDataDO.setCompanyId(borrowMap.get(borrowBpmDO.getBorrowId()).getContractId() != null ? contractMap.get(borrowMap.get(borrowBpmDO.getBorrowId()).getContractId()).getCompanyId() : null);
            String formattedDate = dateFormat.format(borrowBpmDO.getReturnTime());
            warningDataDO.setWarningDataValue(formattedDate);
            warningDataDO.setWarningDataId(borrowBpmDO.getId());
        }
        warningDataDO.setWarningId(warningRuleConfigDO.getId());
        warningDataDO.setWarningDate(LocalDateTime.now());
        warningDataDOList.add(warningDataDO);
    }
}