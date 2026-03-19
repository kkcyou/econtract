package com.yaoan.module.econtract.dal.mysql.contract;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contract.vo.PaymentSchedulePageReqVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.AcceptancePageReqVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.ContractPerformanceAcceptancePageReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationSaveReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.PaymentPlanAmtReqVO;
import com.yaoan.module.econtract.controller.admin.paymentPlan.vo.PaymentPlanRepVO;
import com.yaoan.module.econtract.controller.admin.paymentRecord.vo.PaymentAmountRepVO;
import com.yaoan.module.econtract.dal.dataobject.acceptance.AcceptanceDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contractPerformanceAcceptance.ContractPerformanceAcceptanceDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplScheRelDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.common.IfEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleApplyStatusEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums.DONE;

@Mapper
public interface PaymentScheduleMapper extends BaseMapperX<PaymentScheduleDO> {
    default List<PaymentScheduleDO> selectPayList(List<String> contractIds) {
        List<PaymentScheduleDO> paymentScheduleDOS = selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                .in(PaymentScheduleDO::getContractId, contractIds)
                .eq(PaymentScheduleDO::getStatus, 1));
        return paymentScheduleDOS;
    }

    /**
     * 分页查询支付计划
     *
     * @param paymentPlanRepVO
     * @return
     */
    default PageResult<PaymentScheduleDO> selectPage(PaymentPlanRepVO paymentPlanRepVO) {
        //select * from
        //ecms_payment_schedule p LEFT JOIN ecms_contract c ON p.contract_id=c.id
        //where c.`name`LIKE'%HT%'AND c.`code`LIKE'%%'  AND  p.`payment_plan_name`LIKE'%首%'
        //AND  p.`status`LIKE'%0%'
        MPJQueryWrapper<PaymentScheduleDO> mpjQueryWrapper = new MPJQueryWrapper<PaymentScheduleDO>().selectAll(PaymentScheduleDO.class)
                .leftJoin("ecms_contract c on t.contract_id=c.id")
                //展示履约关闭、已发布（履约中）、履约完成、履约风险、履约争议、履约暂停、履约延期、履约逾期的合同对应计划
                .in("c.status",ContractStatusEnums.PERFORMANCE_CLOSURE.getCode()
                        ,ContractStatusEnums.SIGN_COMPLETED.getCode()
                        ,ContractStatusEnums.PERFORMING.getCode(),ContractStatusEnums.PERFORMANCE_COMPLETE.getCode(),
                        ContractStatusEnums.PERFORMANCE_RISK.getCode(),ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode() ,ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode()
                       ,ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(),ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode())
                //.eq("c.status", 6)
                //根据修改时间倒排
                .orderByDesc("t.create_time").orderByDesc("t.contract_id").orderByAsc("t.sort");
        mpjQueryWrapper.eq("c.deleted", 0);

        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getAmountType())) {
            mpjQueryWrapper.eq("t.amount_type", paymentPlanRepVO.getAmountType());
        }
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getContractId())) {
            mpjQueryWrapper.eq("t.contract_id", paymentPlanRepVO.getContractId());
        }
        //当前时间大于合同生效开始时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getCurrentDate())) {
            mpjQueryWrapper.lt("c.validity0", paymentPlanRepVO.getCurrentDate());
        }
        //当前时间小于合同生效结束时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getCurrentDate())) {
            mpjQueryWrapper.gt("c.validity1", paymentPlanRepVO.getCurrentDate());
        }
        //合同名称
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getContractName())) {
            mpjQueryWrapper.like("c.name", paymentPlanRepVO.getContractName());
        }
        //合同编码
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getContractCode())) {
            mpjQueryWrapper.like("c.code", paymentPlanRepVO.getContractCode());
        }
        //支付计划名称
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getPaymentPlanName())) {
            mpjQueryWrapper.like("t.name", paymentPlanRepVO.getPaymentPlanName());
        }
        //TODO 状态为什么LIKE？
        //支付状态
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getPaymentStatus())) {
            mpjQueryWrapper.like("t.status", paymentPlanRepVO.getPaymentStatus());
        }
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getStatus())) {
            mpjQueryWrapper.like("t.status", paymentPlanRepVO.getStatus());
        }
        //计划支付时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getStartPayTime()) || ObjectUtil.isNotEmpty(paymentPlanRepVO.getEndPayTime())) {
            mpjQueryWrapper.between("t.payment_time", paymentPlanRepVO.getStartPayTime(), paymentPlanRepVO.getEndPayTime());
        }
        //实际支付时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getStartActualPayTime()) || ObjectUtil.isNotEmpty(paymentPlanRepVO.getEndActualPayTime())) {
            mpjQueryWrapper.between("t.actual_pay_time", paymentPlanRepVO.getStartActualPayTime(), paymentPlanRepVO.getEndActualPayTime());
        }


        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getIdentifier())) {
            switch (paymentPlanRepVO.getIdentifier()){
                case 0:
                    break;
                case 1:
                    mpjQueryWrapper.lambda().eq(PaymentScheduleDO::getStatus, PaymentScheduleStatusEnums.UNPAID.getCode());
                    break;
                case 2:
                    mpjQueryWrapper.lambda().eq(PaymentScheduleDO::getStatus, PaymentScheduleStatusEnums.PAYED.getCode());
//                    mpjQueryWrapper.lambda().isNotNull(PaymentScheduleDO::getPaidAmount);
                    break;
                case 3:
                    mpjQueryWrapper.lambda().eq(PaymentScheduleDO::getStatus, PaymentScheduleStatusEnums.DONE.getCode());
                    break;
                case 4:
                    mpjQueryWrapper.lambda().eq(PaymentScheduleDO::getStatus, PaymentScheduleStatusEnums.CLOSE.getCode());
                    break;
                default:
                    break;
            }
        }
        return selectPage(paymentPlanRepVO, mpjQueryWrapper);
    }

    default List<PaymentScheduleDO> selectList(PaymentPlanRepVO paymentPlanRepVO) {
        //select * from
        //ecms_payment_schedule p LEFT JOIN ecms_contract c ON p.contract_id=c.id
        //where c.`name`LIKE'%HT%'AND c.`code`LIKE'%%'  AND  p.`payment_plan_name`LIKE'%首%'
        //AND  p.`status`LIKE'%0%'
        MPJQueryWrapper<PaymentScheduleDO> mpjQueryWrapper = new MPJQueryWrapper<PaymentScheduleDO>()
                .select("t.id","t.amount","t.stage_payment_amount","t.status")
                .leftJoin("ecms_contract c on t.contract_id=c.id")
                //展示履约关闭、已发布（履约中）、履约完成、履约风险、履约争议、履约暂停、履约延期、履约逾期的合同对应计划
                .in("c.status",ContractStatusEnums.PERFORMANCE_CLOSURE.getCode()
                        ,ContractStatusEnums.SIGN_COMPLETED.getCode()
                        ,ContractStatusEnums.PERFORMING.getCode(),ContractStatusEnums.PERFORMANCE_COMPLETE.getCode(),
                        ContractStatusEnums.PERFORMANCE_RISK.getCode(),ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode() ,ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode()
                        ,ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(),ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode())
                //.eq("c.status", 6)
                //根据修改时间倒排
                .orderByDesc("t.create_time").orderByDesc("t.contract_id").orderByAsc("t.sort");
        mpjQueryWrapper.eq("c.deleted", 0);

        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getAmountType())) {
            mpjQueryWrapper.eq("t.amount_type", paymentPlanRepVO.getAmountType());
        }
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getContractId())) {
            mpjQueryWrapper.eq("t.contract_id", paymentPlanRepVO.getContractId());
        }
        //当前时间大于合同生效开始时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getCurrentDate())) {
            mpjQueryWrapper.lt("c.validity0", paymentPlanRepVO.getCurrentDate());
        }
        //当前时间小于合同生效结束时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getCurrentDate())) {
            mpjQueryWrapper.gt("c.validity1", paymentPlanRepVO.getCurrentDate());
        }
        //合同名称
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getContractName())) {
            mpjQueryWrapper.like("c.name", paymentPlanRepVO.getContractName());
        }
        //合同编码
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getContractCode())) {
            mpjQueryWrapper.like("c.code", paymentPlanRepVO.getContractCode());
        }
        //支付计划名称
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getPaymentPlanName())) {
            mpjQueryWrapper.like("t.name", paymentPlanRepVO.getPaymentPlanName());
        }
        //支付状态
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getPaymentStatus())) {
            mpjQueryWrapper.like("t.status", paymentPlanRepVO.getPaymentStatus());
        }
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getStatus())) {
            mpjQueryWrapper.like("t.status", paymentPlanRepVO.getStatus());
        }
        //计划支付时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getStartPayTime()) || ObjectUtil.isNotEmpty(paymentPlanRepVO.getEndPayTime())) {
            mpjQueryWrapper.between("t.payment_time", paymentPlanRepVO.getStartPayTime(), paymentPlanRepVO.getEndPayTime());
        }
        //实际支付时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getStartActualPayTime()) || ObjectUtil.isNotEmpty(paymentPlanRepVO.getEndActualPayTime())) {
            mpjQueryWrapper.between("t.actual_pay_time", paymentPlanRepVO.getStartActualPayTime(), paymentPlanRepVO.getEndActualPayTime());
        }
        return selectList(mpjQueryWrapper);
    }

    /**
     * 分页查询支付计划
     *
     * @param paymentPlanRepVO
     * @return
     */
    default List<PaymentScheduleDO> selectAll(PaymentAmountRepVO paymentPlanRepVO) {
        MPJQueryWrapper<PaymentScheduleDO> mpjQueryWrapper = new MPJQueryWrapper<PaymentScheduleDO>().selectAll(PaymentScheduleDO.class)
                .leftJoin("ecms_contract c on t.contract_id=c.id")
                //条件与付款管理一致
                .leftJoin("ecms_signatory_rel s on s.contract_id = c.id")
                .eq("c.status", 6);
        mpjQueryWrapper.eq("c.deleted", 0);
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getAmountType())) {
            mpjQueryWrapper.eq("c.amount_type", paymentPlanRepVO.getAmountType());
        }
        //当前时间大于合同生效开始时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getCurrentDate())) {
            mpjQueryWrapper.lt("c.validity0", paymentPlanRepVO.getCurrentDate());
        }
        //当前时间小于合同生效结束时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getCurrentDate())) {
            mpjQueryWrapper.gt("c.validity1", paymentPlanRepVO.getCurrentDate());
        }
        //合同名称
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getContractName())) {
            mpjQueryWrapper.like("c.name", paymentPlanRepVO.getContractName());
        }
        //合同编码
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getContractCode())) {
            mpjQueryWrapper.like("c.code", paymentPlanRepVO.getContractCode());
        }
        //支付计划名称
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getPaymentPlanName())) {
            mpjQueryWrapper.like("t.name", paymentPlanRepVO.getPaymentPlanName());
        }
        //支付状态
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getPaymentStatus())) {
            mpjQueryWrapper.like("t.status", paymentPlanRepVO.getPaymentStatus());
        }
        //计划支付时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getStartPayTime()) || ObjectUtil.isNotEmpty(paymentPlanRepVO.getEndPayTime())) {
            mpjQueryWrapper.between("t.payment_time", paymentPlanRepVO.getStartPayTime(), paymentPlanRepVO.getEndPayTime());
        }
        //实际支付时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getStartActualPayTime()) || ObjectUtil.isNotEmpty(paymentPlanRepVO.getEndActualPayTime())) {
            mpjQueryWrapper.between("t.actual_pay_time", paymentPlanRepVO.getStartActualPayTime(), paymentPlanRepVO.getEndActualPayTime());
        }
        return selectList(mpjQueryWrapper);
    }

    /**
     * 申请金额统计
     */
    default List<PaymentScheduleDO> selectStatisticsAmountList(PaymentApplicationListBpmReqVO vo) {
        MPJQueryWrapper<PaymentScheduleDO> queryWrapper = new MPJQueryWrapper<PaymentScheduleDO>().selectAll(PaymentScheduleDO.class)
                .leftJoin("ecms_payment_application s on s.id = t.apply_id");
        queryWrapper.inSql("t.apply_id", " SELECT s.id FROM ecms_payment_application s WHERE s.deleted = 0 ");
        if (StringUtils.isNotEmpty(vo.getTitle())) {
            queryWrapper.like("s.title", vo.getTitle());
        }
        if (StringUtils.isNotEmpty(vo.getPaymentApplyCode())) {
            queryWrapper.like("s.payment_apply_code", vo.getPaymentApplyCode());
        }
        if (StringUtils.isNotEmpty(vo.getContractName())) {
            queryWrapper.like("s.contract_name", vo.getContractName());
        }
        if (StringUtils.isNotEmpty(vo.getApplicantName())) {
            queryWrapper.like("s.applicant_name", vo.getApplicantName());
        }
        if (ObjectUtil.isNotNull(vo.getApplyTime0())) {
            queryWrapper.between("s.apply_time", vo.getApplyTime0(), vo.getApplyTime1());
        }
        if (ObjectUtil.isNotNull(vo.getIsDeferred())) {
            queryWrapper.eq("s.Is_deferred", vo.getIsDeferred());
        }
        return selectList(queryWrapper);
    }

    ;

    /**
     * 校验是否重复申请支付计划
     * 查询状态为 已申请 和 申请通过 的计划的数量
     */
    default Long selectCountAppliedStatus(PaymentApplicationSaveReqVO vo) {
        List<String> planIds = vo.getBuyPlanIds().stream().map(PaymentPlanAmtReqVO::getId).collect(Collectors.toList());
        List<Integer> appliedStatusList = new ArrayList<Integer>();
        appliedStatusList.add(PaymentScheduleApplyStatusEnums.APPLY.getCode());
        appliedStatusList.add(PaymentScheduleApplyStatusEnums.APPLY_SUCCESS.getCode());
        return selectCount(new LambdaQueryWrapperX<PaymentScheduleDO>()
                .inIfPresent(PaymentScheduleDO::getId, planIds)
                .inIfPresent(PaymentScheduleDO::getApplyStatus, appliedStatusList));
    }

    /**
     * 校验是否重复申请支付计划
     * 查询状态为 已申请 和 申请通过 的支付计划
     */
    default List<PaymentScheduleDO> selectAppliedStatus(PaymentApplicationSaveReqVO vo) {
        List<String> planIds = vo.getBuyPlanIds().stream().map(PaymentPlanAmtReqVO::getId).collect(Collectors.toList());
        List<Integer> appliedStatusList = new ArrayList<Integer>();
        appliedStatusList.add(PaymentScheduleApplyStatusEnums.APPLY.getCode());
        appliedStatusList.add(PaymentScheduleApplyStatusEnums.APPLY_SUCCESS.getCode());
        return selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                .inIfPresent(PaymentScheduleDO::getId, planIds)
                .inIfPresent(PaymentScheduleDO::getApplyStatus, appliedStatusList));
    }

    /**
     * 通过申请id找到对应的支付计划
     */
    default List<PaymentScheduleDO> selectPlanForApplication(String applicationId) {
        MPJLambdaWrapper<PaymentScheduleDO> mpjLambdaWrapper = new MPJLambdaWrapper<PaymentScheduleDO>()
                .selectAll(PaymentScheduleDO.class);
        if (StringUtils.isNotBlank(applicationId)) {
            mpjLambdaWrapper.leftJoin(PaymentApplScheRelDO.class, PaymentApplScheRelDO::getScheduleId, PaymentScheduleDO::getId)
                    .eq(PaymentApplScheRelDO::getApplicationId, applicationId);
        }
        return selectList(mpjLambdaWrapper);
    }

    /**
     * 通过申请id找到对应的支付计划
     * 已付款状态的 支付计划
     */
    default List<PaymentScheduleDO> selectPayedPlan(String applicationId) {
        MPJLambdaWrapper<PaymentScheduleDO> mpjLambdaWrapper = new MPJLambdaWrapper<PaymentScheduleDO>()
                .selectAll(PaymentScheduleDO.class);
        if (StringUtils.isNotBlank(applicationId)) {
            mpjLambdaWrapper.leftJoin(PaymentApplScheRelDO.class, PaymentApplScheRelDO::getScheduleId, PaymentScheduleDO::getId)
                    .eq(PaymentApplScheRelDO::getApplicationId, applicationId)
                    .eq(PaymentScheduleDO::getStatus, PaymentScheduleStatusEnums.PAYED.getCode());
        }
        return selectList(mpjLambdaWrapper);
    }

    /**
     * 批量
     * 通过申请id找到对应的支付计划
     */
    default List<PaymentScheduleDO> selectPlanForApplicationBatch(Set<String> ids) {

        MPJLambdaWrapper<PaymentScheduleDO> mpjLambdaWrapper = new MPJLambdaWrapper<PaymentScheduleDO>()
                .selectAll(PaymentScheduleDO.class);
        if (CollectionUtil.isNotEmpty(ids)) {
            mpjLambdaWrapper.leftJoin(PaymentApplScheRelDO.class, PaymentApplScheRelDO::getScheduleId, PaymentScheduleDO::getId)
                    .in(PaymentApplScheRelDO::getApplicationId, ids);
        }
        return selectList(mpjLambdaWrapper);
    }

    default List<PaymentScheduleDO> selectStatistics(PaymentApplicationListBpmReqVO vo) {
        MPJLambdaWrapper<PaymentScheduleDO> mpjLambdaWrapper = new MPJLambdaWrapper<PaymentScheduleDO>()
                .selectAll(PaymentScheduleDO.class).orderByDesc(PaymentScheduleDO::getCreateTime);
        // 商品名称

        mpjLambdaWrapper.leftJoin(PaymentApplScheRelDO.class, PaymentApplScheRelDO::getScheduleId, PaymentScheduleDO::getId)
                .leftJoin(PaymentApplicationDO.class, PaymentApplicationDO::getId, PaymentApplScheRelDO::getApplicationId);

        if (ObjectUtil.isNotNull(vo.getIsDeferred())) {
            mpjLambdaWrapper.eq(PaymentApplicationDO::getIsDeferred, vo.getIsDeferred());
        }
        // 付款编号
        if (StringUtils.isNotBlank(vo.getPaymentApplyCode())) {
            mpjLambdaWrapper.like(PaymentApplicationDO::getPaymentApplyCode, vo.getPaymentApplyCode());
        }
        // 付款标题
        if (StringUtils.isNotBlank(vo.getTitle())) {
            mpjLambdaWrapper.like(PaymentApplicationDO::getTitle, vo.getTitle());
        }
        // 合同名称
        if (StringUtils.isNotBlank(vo.getContractName())) {
            mpjLambdaWrapper.like(PaymentApplicationDO::getContractName, vo.getContractName());
        }
        // 申请人
        if (StringUtils.isNotBlank(vo.getApplicantName())) {
            mpjLambdaWrapper.like(PaymentApplicationDO::getApplicantName, vo.getApplicantName());
        }
        // 申请时间
        if (ObjectUtil.isNotNull(vo.getApplyTime0())) {
            mpjLambdaWrapper.between(PaymentApplicationDO::getApplyTime, vo.getApplyTime0(), vo.getApplyTime1());
        }
        // 审批状态
        if (ObjectUtil.isNotNull(vo.getResult())) {
            mpjLambdaWrapper.like(PaymentApplicationDO::getResult, vo.getResult());
        }

        if (StringUtils.isNotEmpty(vo.getFrontCode())) {
            CommonFlowableReqVOResultStatusEnums enums = CommonFlowableReqVOResultStatusEnums.getFrontInstance(vo.getFrontCode());
            if (ObjectUtil.isNotNull(enums)) {
                switch (enums) {
                    case TO_SEND:
                        mpjLambdaWrapper.and(w -> w.eq(PaymentApplicationDO::getResult, CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode())
                                .or()
                                .eq(PaymentApplicationDO::getResult, CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode()));

                        break;
                    default:
                        mpjLambdaWrapper.eq(PaymentApplicationDO::getResult, enums.getResultCode());
                        break;
                }
            }
        }
        return selectList(mpjLambdaWrapper);

    }

    default PageResult<PaymentScheduleDO> listPaymentManagement(PaymentSchedulePageReqVO paymentPlanRepVO, List<String> userIds1, List<String> userIds2) {
        List<String> finalUserIdList = getAllList(userIds1, userIds2);
        MPJLambdaWrapper<PaymentScheduleDO> mpjQueryWrapper = new MPJLambdaWrapper<PaymentScheduleDO>()
                .selectAll(PaymentScheduleDO.class)
                .orderByDesc(PaymentScheduleDO::getUpdateTime);
        mpjQueryWrapper.leftJoin(ContractDO.class, ContractDO::getId, PaymentScheduleDO::getContractId)
                .leftJoin(SignatoryRelDO.class, SignatoryRelDO::getContractId, ContractDO::getId)
                .leftJoin(Relative.class, Relative::getId, SignatoryRelDO::getSignatoryId)
                //合同状态为签署完成
                .eq(ContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode())
        ;

        //当前时间大于合同生效开始时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getCurrentDate())) {
            mpjQueryWrapper.lt(ContractDO::getValidity0, paymentPlanRepVO.getCurrentDate());
        }
        //当前时间小于合同生效结束时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getCurrentDate())) {
            mpjQueryWrapper.gt(ContractDO::getValidity1, paymentPlanRepVO.getCurrentDate());
        }
        //合同名称编码
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getCode())) {
            mpjQueryWrapper.like(ContractDO::getCode, paymentPlanRepVO.getCode());
        }
        //合同名称
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getName())) {
            mpjQueryWrapper.like(ContractDO::getName, paymentPlanRepVO.getName());
        }
        //合同类型
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getContractType())) {
            mpjQueryWrapper.eq(ContractDO::getContractType, paymentPlanRepVO.getContractType());
        }
        //我方签约主体
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getPayerName())) {
            mpjQueryWrapper.in(ContractDO::getCreator, userIds1);
        }
        //相对方
        if (ObjectUtil.isNotNull(paymentPlanRepVO.getPayeeName())) {
            mpjQueryWrapper.in(Relative::getId, userIds2);
        }


        if (ObjectUtil.isNotNull(paymentPlanRepVO.getPayeeName()) && ObjectUtil.isNotEmpty(paymentPlanRepVO.getPayerName())) {
            mpjQueryWrapper.and(
                    w -> w.in(ContractDO::getCreator, paymentPlanRepVO.getPayerName())
                            .or()
                            .like(Relative::getName, paymentPlanRepVO.getPayeeName())
            );
        }

        return selectPage(paymentPlanRepVO, mpjQueryWrapper);
    }

    default List<String> getAllList(List<String> userIds1, List<String> userIds2) {
        // 使用 HashSet 存储并集
        Set<String> unionSet = new HashSet<>(userIds1);
        // 将 userIds2 中的元素添加到并集中
        unionSet.addAll(userIds2);
        // 将并集转换为 List 类型（可选）
        List<String> unionList = new ArrayList<>(unionSet);
        return unionList;
    }

    default List<PaymentScheduleDO> listPaymentManagement(PaymentAmountRepVO paymentPlanRepVO, List<String> userIds1, List<String> userIds2) {
        List<String> finalUserIdList = getAllList(userIds1, userIds2);
        MPJLambdaWrapper<PaymentScheduleDO> mpjQueryWrapper = new MPJLambdaWrapper<PaymentScheduleDO>()
                .selectAll(PaymentScheduleDO.class)
                .orderByDesc(PaymentScheduleDO::getUpdateTime);
        mpjQueryWrapper.leftJoin(ContractDO.class, ContractDO::getId, PaymentScheduleDO::getContractId)
                .leftJoin(SignatoryRelDO.class, SignatoryRelDO::getContractId, ContractDO::getId)
                .leftJoin(Relative.class, Relative::getId, SignatoryRelDO::getSignatoryId)
                //合同状态为签署完成
                .eq(ContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode())

        ;

        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getAmountType())) {
            mpjQueryWrapper.eq(ContractDO::getAmountType, paymentPlanRepVO.getAmountType());
        }
        //当前时间大于合同生效开始时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getCurrentDate())) {
            mpjQueryWrapper.lt(ContractDO::getValidity0, paymentPlanRepVO.getCurrentDate());
        }
        //当前时间小于合同生效结束时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getCurrentDate())) {
            mpjQueryWrapper.gt(ContractDO::getValidity1, paymentPlanRepVO.getCurrentDate());
        }
        //合同名称编码
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getCode())) {
            mpjQueryWrapper.like(ContractDO::getCode, paymentPlanRepVO.getCode());
        }
        //合同名称
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getName())) {
            mpjQueryWrapper.like(ContractDO::getName, paymentPlanRepVO.getName());
        }
        //合同类型
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getContractType())) {
            mpjQueryWrapper.eq(ContractDO::getContractType, paymentPlanRepVO.getContractType());
        }
        //我方签约主体
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getPayerName())) {
            mpjQueryWrapper.in(ContractDO::getCreator, userIds1);
        }
        //相对方
        if (ObjectUtil.isNotNull(paymentPlanRepVO.getPayeeName())) {
            mpjQueryWrapper.in(Relative::getId, userIds2);
        }


        if (ObjectUtil.isNotNull(paymentPlanRepVO.getPayeeName()) && ObjectUtil.isNotEmpty(paymentPlanRepVO.getPayerName())) {
            mpjQueryWrapper.and(
                    w -> w.in(ContractDO::getCreator, paymentPlanRepVO.getPayerName())
                            .or()
                            .like(Relative::getCompanyName, paymentPlanRepVO.getPayeeName())
            );
        }
        //相对方签约主体
        if (StringUtils.isNotBlank(paymentPlanRepVO.getPartBName())) {
            mpjQueryWrapper.like(ContractDO::getPartBName, paymentPlanRepVO.getPartBName());
        }
        return selectList(mpjQueryWrapper);

    }

    default List<PaymentScheduleDO> getSchedulesByAppId(String businessKey) {
        MPJLambdaWrapper<PaymentScheduleDO> mpjQueryWrapper = new MPJLambdaWrapper<PaymentScheduleDO>()
                .selectAll(PaymentScheduleDO.class)
                .orderByDesc(PaymentScheduleDO::getUpdateTime)
                .leftJoin(PaymentApplScheRelDO.class, PaymentApplScheRelDO::getScheduleId, PaymentScheduleDO::getId)
                .eq(PaymentApplScheRelDO::getApplicationId, businessKey);
        return selectList(mpjQueryWrapper);
    }


   default List<PaymentScheduleDO> select4ProcessContract(){
       // 获取当前日期
       Date today = new Date();

       // 创建 Calendar 实例，并设置为当前时间
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(today);

       // 减去一天
       calendar.add(Calendar.DAY_OF_MONTH, -1);

       // 获取前一天的日期
       Date yesterday = calendar.getTime();

       MPJLambdaWrapper<PaymentScheduleDO> mpjQueryWrapper = new MPJLambdaWrapper<PaymentScheduleDO>()
               .leftJoin(ContractDO.class,ContractDO::getId,PaymentScheduleDO::getContractId)
               .select(PaymentScheduleDO::getId,PaymentScheduleDO::getContractId,PaymentScheduleDO::getDeptId,PaymentScheduleDO::getPaymentTime,PaymentScheduleDO::getSort,PaymentScheduleDO::getTenantId)
               //履约中
               .eq(ContractDO::getStatus,ContractStatusEnums.PERFORMING.getCode())
               //计划未开始
               .eq(PaymentScheduleDO::getStatus,PaymentScheduleStatusEnums.TO_DO.getCode())
               //过期
               .le(PaymentScheduleDO::getPaymentTime,yesterday)
               ;
       return  selectList(mpjQueryWrapper);
   }

    default List<PaymentScheduleDO> selectListByApplicationIds(List<String> instanceList){
        MPJLambdaWrapper<PaymentScheduleDO> mpjQueryWrapper = new MPJLambdaWrapper<PaymentScheduleDO>()
                .selectAll(PaymentScheduleDO.class)
                .leftJoin(PaymentApplScheRelDO.class, PaymentApplScheRelDO::getScheduleId, PaymentScheduleDO::getId)
                .in(PaymentApplScheRelDO::getApplicationId, instanceList);
        return selectList(mpjQueryWrapper);
    }

   default List<PaymentScheduleDO> select4BenchGov(){
       MPJLambdaWrapper<PaymentScheduleDO> mpjQueryWrapper = new MPJLambdaWrapper<PaymentScheduleDO>()
               .select(PaymentScheduleDO::getId,PaymentScheduleDO::getStatus,PaymentScheduleDO::getAmount)
               .leftJoin(ContractOrderExtDO.class, ContractOrderExtDO::getId,PaymentScheduleDO::getContractId)
               .eq(ContractOrderExtDO::getStatus,ContractStatusEnums.SIGN_COMPLETED.getCode())
               ;
       return selectList(mpjQueryWrapper);
   }

    default List<PaymentScheduleDO> select4TotalBench(){
        MPJLambdaWrapper<PaymentScheduleDO> mpjQueryWrapper = new MPJLambdaWrapper<PaymentScheduleDO>()
                .select(PaymentScheduleDO::getId,PaymentScheduleDO::getStatus,PaymentScheduleDO::getAmount)
                .leftJoin(ContractDO.class, ContractDO::getId,PaymentScheduleDO::getContractId)
                .eq(ContractDO::getStatus,ContractStatusEnums.SIGN_COMPLETED.getCode())
                ;
        return selectList(mpjQueryWrapper);
    }


    default List<PaymentScheduleDO> enhance4ContractPerform(List<String> contractIds,List<Integer> payedStatus){
            return  selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                    .select(PaymentScheduleDO::getId,PaymentScheduleDO::getContractId,PaymentScheduleDO::getStatus,PaymentScheduleDO::getAmount,PaymentScheduleDO::getStagePaymentAmount)
                    .in(PaymentScheduleDO::getStatus,payedStatus)
                    .in(PaymentScheduleDO::getContractId,contractIds));
    }

    default List<PaymentScheduleDO> selectOldPlans(String contractId,Integer sort){
        LambdaQueryWrapperX<PaymentScheduleDO> wrapperX = new LambdaQueryWrapperX();
        wrapperX.lt(PaymentScheduleDO::getSort, sort)
                .eq(PaymentScheduleDO::getContractId,contractId)
                .ne(PaymentScheduleDO::getStatus,DONE.getCode())
        ;
      return selectList(wrapperX);
    }

    default PageResult<PaymentScheduleDO> selectPageV2(AcceptancePageReqVO pageReqVO) {
        MPJLambdaWrapper<PaymentScheduleDO> mpjQueryWrapper = new MPJLambdaWrapper<PaymentScheduleDO>()
                .selectAll(PaymentScheduleDO.class)
                .leftJoin(ContractDO.class, ContractDO::getId, PaymentScheduleDO::getContractId)
                .leftJoin(SignatoryRelDO.class, SignatoryRelDO::getContractId, ContractDO::getId)
                .leftJoin(Relative.class, Relative::getId, SignatoryRelDO::getSignatoryId)

                //完成签署的合同
                .eq(ContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode())
                //需要验收的计划
                .eq(PaymentScheduleDO::getNeedAcceptance, IfNumEnums.YES.getCode())
                //待发起 或 已发起
                .eq(PaymentScheduleDO::getIsAcceptance, pageReqVO.getIsAcceptance())
                .orderByDesc(PaymentScheduleDO::getUpdateTime)
                .distinct()
                ;
        //签署方
        if (StringUtils.isNotBlank(pageReqVO.getSignatureName())) {
            mpjQueryWrapper.and(w -> w
                    //相对方
                    .like(Relative::getName, pageReqVO.getSignatureName())
                    .or()
                    //发送方
                    .like(ContractDO::getPartAName, pageReqVO.getSignatureName()));
        }

        //合同编号
        if(StringUtils.isNotBlank(pageReqVO.getContractCode())) {
            mpjQueryWrapper.like(ContractDO::getCode,pageReqVO.getContractCode());
        }
        //合同名称
        if(StringUtils.isNotBlank(pageReqVO.getContractName())) {
            mpjQueryWrapper.like(ContractDO::getName,pageReqVO.getContractName());
        }

        return selectPage(pageReqVO, mpjQueryWrapper);
    }

    default List<PaymentScheduleDO> selectList4ledger(String contractId) {
        MPJLambdaWrapper<PaymentScheduleDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(PaymentScheduleDO.class);
        wrapper.leftJoin(PaymentApplScheRelDO.class, PaymentApplScheRelDO::getScheduleId, PaymentScheduleDO::getId);
        wrapper.leftJoin(PaymentApplicationDO.class, PaymentApplicationDO::getId, PaymentApplScheRelDO::getApplicationId);
        wrapper.notIn(PaymentScheduleDO::getAmountType, AmountTypeEnums.NO_SETTLE.getCode());
        wrapper.eq(PaymentScheduleDO::getContractId, contractId);
        wrapper.orderByAsc(PaymentScheduleDO::getSort);
        return selectList(wrapper);
    }
}
