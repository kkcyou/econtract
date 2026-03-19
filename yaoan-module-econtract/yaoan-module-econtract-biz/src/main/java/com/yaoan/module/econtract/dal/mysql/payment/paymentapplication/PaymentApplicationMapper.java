package com.yaoan.module.econtract.dal.mysql.payment.paymentapplication;

import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication.PaymentApplicationListReqVO;
import com.yaoan.module.econtract.controller.admin.paymentRecord.vo.PaymentAmountRepVO;
import com.yaoan.module.econtract.controller.admin.paymentRecord.vo.PaymentRecordRepVO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplScheRelDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.payment.CollectionTypeEnums;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @description: {@link CommonFlowableReqVOResultStatusEnums}
 * TO_SEND(0, "TO_SEND", "草稿"),前端发送TO_SEND，则会查出草稿0和被退回5状态的记录
 * APPROVING(1, "APPROVING", "审批中"),
 * SUCCESS(2, "SUCCESS", "审批通过"),
 * REJECTED(5, "TO_SEND", "被退回"),
 * @author: Pele
 * @date: 2023/12/21 14:55
 */
@Mapper
public interface PaymentApplicationMapper extends BaseMapperX<PaymentApplicationDO> {
    /**
     * 付款申请分页(申请人查看)
     */
    default PageResult<PaymentApplicationDO> selectPage(PaymentApplicationListReqVO vo) {
        LambdaQueryWrapperX<PaymentApplicationDO> wrapperX = new LambdaQueryWrapperX<PaymentApplicationDO>()
                .orderByDesc(PaymentApplicationDO::getUpdateTime)
                .orderByDesc(PaymentApplicationDO::getApplyTime)
                .eqIfPresent(PaymentApplicationDO::getIsDeferred, vo.getIsDeferred())
                .eqIfPresent(PaymentApplicationDO::getStatus, vo.getStatus())
                // 付款编号
                .likeIfPresent(PaymentApplicationDO::getPaymentApplyCode, vo.getPaymentApplyCode())
                // 付款标题
                .likeIfPresent(PaymentApplicationDO::getTitle, vo.getTitle())
                // 申请人
                .likeIfPresent(PaymentApplicationDO::getApplicantName, vo.getApplicantName())
                // 申请时间
                .betweenIfPresent(PaymentApplicationDO::getApplyTime, vo.getApplyTime0(), vo.getApplyTime1())
                // 审批状态
                .eqIfPresent(PaymentApplicationDO::getResult, CommonFlowableReqVOResultStatusEnums.getFrontInstance(vo.getFrontCode()).getResultCode());
        //只查询付款  收款不走工作流
//        if(ObjectUtil.isNotEmpty(vo.getCollectType())){
//
//        }
        if (StringUtils.isNotBlank(vo.getContractName())) {
            wrapperX.and(
                    w -> w.like(PaymentApplicationDO::getContractName, vo.getContractName())
                            .or()
                            .like(PaymentApplicationDO::getContractCode, vo.getContractName())
            );
        }

        wrapperX.eq(PaymentApplicationDO::getCollectionType, CollectionTypeEnums.PAYMENT.getCode());
        if (StringUtils.isNotEmpty(vo.getFrontCode())) {
            CommonFlowableReqVOResultStatusEnums enums = CommonFlowableReqVOResultStatusEnums.getFrontInstance(vo.getFrontCode());
            if (ObjectUtil.isNotNull(enums)) {
                switch (enums) {
                    case TO_SEND:
                        wrapperX.and(
                                w -> w.eq(PaymentApplicationDO::getResult, CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode())
                                        .or()
                                        .eq(PaymentApplicationDO::getResult, CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode())
                        );

                        break;
                    default:
                        wrapperX.eq(PaymentApplicationDO::getResult, enums.getResultCode());
                        break;
                }
            }
        }
        if (StringUtils.isNotEmpty(vo.getQueryKey())){
            wrapperX.and(it->{
                it.like(PaymentApplicationDO::getContractName,vo.getQueryKey()).or()
                        .like(PaymentApplicationDO::getContractCode,vo.getQueryKey());
            });
        }

        return selectPage(vo, wrapperX);
    }

    /**
     * 申请金额统计(已付款)
     */
    default List<PaymentApplicationDO> selectStatisticsAmountList(PaymentApplicationListBpmReqVO vo) {
        LambdaQueryWrapperX<PaymentApplicationDO> wrapperX = new LambdaQueryWrapperX<PaymentApplicationDO>()
                .orderByDesc(PaymentApplicationDO::getApplyTime)
                .eqIfPresent(PaymentApplicationDO::getIsDeferred, vo.getIsDeferred())
                // 付款编号
                .likeIfPresent(PaymentApplicationDO::getPaymentApplyCode, vo.getPaymentApplyCode())
                // 付款标题
                .likeIfPresent(PaymentApplicationDO::getTitle, vo.getTitle())
                // 合同名称
                .likeIfPresent(PaymentApplicationDO::getContractName, vo.getContractName())
                // 申请人
                .likeIfPresent(PaymentApplicationDO::getApplicantName, vo.getApplicantName())
                // 申请时间
                .betweenIfPresent(PaymentApplicationDO::getApplyTime, vo.getApplyTime0(), vo.getApplyTime1())
                // 审批状态
                .eqIfPresent(PaymentApplicationDO::getResult, vo.getResult());

        if (StringUtils.isNotEmpty(vo.getFrontCode())) {
            CommonFlowableReqVOResultStatusEnums enums = CommonFlowableReqVOResultStatusEnums.getFrontInstance(vo.getFrontCode());
            if (ObjectUtil.isNotNull(enums)) {
                switch (enums) {
                    case TO_SEND:
                        wrapperX.and(w -> w.eq(PaymentApplicationDO::getResult, CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode())
                                .or()
                                .eq(PaymentApplicationDO::getResult, CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode()));

                        break;
                    case APPROVING:
                        wrapperX.and(w -> w.eq(PaymentApplicationDO::getResult, CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode())
                                .or()
                                .eq(PaymentApplicationDO::getResult, CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode()));
                    default:
                        wrapperX.eq(PaymentApplicationDO::getResult, enums.getResultCode());
                        break;
                }
            }
        }
        return selectList(wrapperX);
    }

    /**
     * 申请金额统计(已付款)
     */
    default List<PaymentApplicationDO> selectPayedStatisticsAmountList(PaymentApplicationListBpmReqVO vo) {
        LambdaQueryWrapperX<PaymentApplicationDO> wrapperX = (LambdaQueryWrapperX<PaymentApplicationDO>) new LambdaQueryWrapperX<PaymentApplicationDO>()
                .orderByDesc(PaymentApplicationDO::getCreateTime)
                .eqIfPresent(PaymentApplicationDO::getIsDeferred, vo.getIsDeferred())
                // 付款编号
                .likeIfPresent(PaymentApplicationDO::getPaymentApplyCode, vo.getPaymentApplyCode())
                // 付款标题
                .likeIfPresent(PaymentApplicationDO::getTitle, vo.getTitle())
                // 合同名称
                .likeIfPresent(PaymentApplicationDO::getContractName, vo.getContractName())
                // 申请人
                .likeIfPresent(PaymentApplicationDO::getApplicantName, vo.getContractName())
                // 申请时间
                .betweenIfPresent(PaymentApplicationDO::getApplyTime, vo.getApplyTime0(), vo.getApplyTime1())
                // 审批状态
                .eqIfPresent(PaymentApplicationDO::getResult, vo.getResult())
                .isNotNull(true, PaymentApplicationDO::getPayDate);

        if (StringUtils.isNotEmpty(vo.getFrontCode())) {
            CommonFlowableReqVOResultStatusEnums enums = CommonFlowableReqVOResultStatusEnums.getFrontInstance(vo.getFrontCode());
            if (ObjectUtil.isNotNull(enums)) {
                switch (enums) {
                    case TO_SEND:
                        wrapperX.or()
                                .eq(PaymentApplicationDO::getResult, CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode())
                                .or()
                                .eq(PaymentApplicationDO::getResult, CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode());
                        break;
                    default:
                        wrapperX.eq(PaymentApplicationDO::getResult, enums.getResultCode());
                        break;
                }
            }
        }
        return selectList(wrapperX);


    }

    /**
     * 分页查询付款计划
     *
     * @param recordRepVO
     * @return
     */
    default PageResult<PaymentApplicationDO> selectPage(PaymentRecordRepVO recordRepVO) {
        MPJQueryWrapper<PaymentApplicationDO> mpjQueryWrapper = new MPJQueryWrapper<PaymentApplicationDO>().selectAll(PaymentApplicationDO.class)
                .orderByDesc("create_time")
                //追加（Pele）
                .leftJoin("ecms_payment_appl_sche_rel rel on t.id=rel.application_id")
                .leftJoin("ecms_payment_schedule p on rel.schedule_id=p.id")

                .leftJoin("ecms_contract c on t.contract_id=c.id").groupBy("t.id").orderByDesc("t.create_time").eq("p.status", 1)
                .isNotNull(true, "pay_date");


        //合同名称
        if (ObjectUtil.isNotEmpty(recordRepVO.getContractName())) {
            mpjQueryWrapper.like("c.name", recordRepVO.getContractName());
        }
        //合同编码
        if (ObjectUtil.isNotEmpty(recordRepVO.getContractCode())) {
            mpjQueryWrapper.like("c.code", recordRepVO.getContractCode());
        }
        //付款编码
        if (ObjectUtil.isNotEmpty(recordRepVO.getPaymentCode())) {
            mpjQueryWrapper.like("t.payment_apply_code", recordRepVO.getPaymentCode());
        }
        //付款标题
        if (ObjectUtil.isNotEmpty(recordRepVO.getTitle())) {
            mpjQueryWrapper.like("t.title", recordRepVO.getTitle());
        }
        //申请人
        if (ObjectUtil.isNotEmpty(recordRepVO.getApplicantName())) {
            mpjQueryWrapper.like("t.applicant_name", recordRepVO.getApplicantName());
        }
        //实际支付时间
        if (ObjectUtil.isNotEmpty(recordRepVO.getStartActualPayTime()) || ObjectUtil.isNotEmpty(recordRepVO.getEndActualPayTime())) {
            mpjQueryWrapper.between("p.actual_pay_time", recordRepVO.getStartActualPayTime(), recordRepVO.getEndActualPayTime());
        }
        return selectPage(recordRepVO, mpjQueryWrapper);
    }

    /**
     * 查询所有付款申请
     *
     * @param
     * @return
     */
    default List<PaymentApplicationDO> selectAll(PaymentAmountRepVO recordRepVO) {
        MPJQueryWrapper<PaymentApplicationDO> mpjQueryWrapper = new MPJQueryWrapper<PaymentApplicationDO>().selectAll(PaymentApplicationDO.class)
                .leftJoin("ecms_payment_appl_sche_rel a on t.id=a.application_id")
                .leftJoin("ecms_payment_schedule p on p.id=a.schedule_id")
                .leftJoin("ecms_contract c on t.contract_id=c.id").groupBy("t.id").eq("p.status", 1)
                .orderByDesc("create_time");
        /**
         * 收款合同/付款合同
         */
        if (ObjectUtil.isNotEmpty(recordRepVO.getAmountType())) {
            mpjQueryWrapper.like("c.amount_type", recordRepVO.getAmountType());
        }
        //合同名称
        if (ObjectUtil.isNotEmpty(recordRepVO.getContractName())) {
            mpjQueryWrapper.like("c.name", recordRepVO.getContractName());
        }
        //合同编码
        if (ObjectUtil.isNotEmpty(recordRepVO.getContractCode())) {
            mpjQueryWrapper.like("c.code", recordRepVO.getContractCode());
        }
        //付款编码
        if (ObjectUtil.isNotEmpty(recordRepVO.getPaymentCode())) {
            mpjQueryWrapper.like("t.payment_apply_code", recordRepVO.getPaymentCode());
        }
        //付款标题
        if (ObjectUtil.isNotEmpty(recordRepVO.getTitle())) {
            mpjQueryWrapper.like("t.title", recordRepVO.getTitle());
        }
        //申请人
        if (ObjectUtil.isNotEmpty(recordRepVO.getApplicantName())) {
            mpjQueryWrapper.like("t.applicant_name", recordRepVO.getApplicantName());
        }
        //实际支付时间
        if (ObjectUtil.isNotEmpty(recordRepVO.getStartActualPayTime()) || ObjectUtil.isNotEmpty(recordRepVO.getEndActualPayTime())) {
            mpjQueryWrapper.between("p.actual_pay_time", recordRepVO.getStartActualPayTime(), recordRepVO.getEndActualPayTime());
        }

        return selectList(mpjQueryWrapper);
    }

    /**
     * 审批-列表（审批人查看 ）
     */
    default PageResult<PaymentApplicationDO> selectApprovePage(PaymentApplicationListBpmReqVO pageVO) {
        if (CollectionUtils.isEmpty(pageVO.getInstanceIdList())) {
            return new PageResult<PaymentApplicationDO>().setTotal(0L).setList(Collections.emptyList());
        }
        LambdaQueryWrapperX<PaymentApplicationDO> wrapperX = new LambdaQueryWrapperX<PaymentApplicationDO>();
        //自己发起的申请不可出现在自己负责的审批列表，被退回的审批任务只能在申请列表展示并处理。
//                .neIfPresent(PaymentApplicationDO::getCreator, pageVO.getApplicantId())
        wrapperX.orderByDesc(PaymentApplicationDO::getApplyTime)
                .inIfPresent(PaymentApplicationDO::getProcessInstanceId, pageVO.getInstanceIdList())
                //是否延期
                .eqIfPresent(PaymentApplicationDO::getIsDeferred, pageVO.getIsDeferred())
                .likeIfPresent(PaymentApplicationDO::getApplicantName, pageVO.getApplicantName())
                .likeIfPresent(PaymentApplicationDO::getPaymentApplyCode, pageVO.getPaymentApplyCode())
                .likeIfPresent(PaymentApplicationDO::getTitle, pageVO.getTitle())
                .betweenIfPresent(PaymentApplicationDO::getApplyTime, pageVO.getApplyTime0(), pageVO.getApplyTime1());
        if (ObjectUtil.isNotEmpty(pageVO.getContractName())) {
            wrapperX.and(w -> w.like(PaymentApplicationDO::getContractName, pageVO.getContractName())
                    .or()
                    .like(PaymentApplicationDO::getContractCode, pageVO.getContractName()));
        }
        return selectPage(pageVO, wrapperX);
    }

    /**
     * 审批-待办-列表（审批人查看 ）
     */
    default List<PaymentApplicationDO> selectApprovePageForStatistic(PaymentApplicationListBpmReqVO pageVO) {

        return selectList(new LambdaQueryWrapperX<PaymentApplicationDO>()
                .orderByDesc(PaymentApplicationDO::getApplyTime)
                .inIfPresent(PaymentApplicationDO::getProcessInstanceId, pageVO.getInstanceIdList())
                //是否延期
                .eqIfPresent(PaymentApplicationDO::getIsDeferred, pageVO.getIsDeferred())
                .likeIfPresent(PaymentApplicationDO::getApplicantName, pageVO.getApplicantName())
                .likeIfPresent(PaymentApplicationDO::getPaymentApplyCode, pageVO.getPaymentApplyCode())
                .likeIfPresent(PaymentApplicationDO::getTitle, pageVO.getTitle())
                .likeIfPresent(PaymentApplicationDO::getContractName, pageVO.getContractName())
                .betweenIfPresent(PaymentApplicationDO::getApplyTime, pageVO.getApplyTime0(), pageVO.getApplyTime1())
        );
    }


    default List<PaymentApplicationDO> statisticsToDo(PaymentApplicationListBpmReqVO vo, List<String> instanceIdList) {
        return selectList(new LambdaQueryWrapperX<PaymentApplicationDO>().orderByDesc(PaymentApplicationDO::getCreateTime)
                .inIfPresent(PaymentApplicationDO::getProcessInstanceId, vo.getInstanceIdList())
                //是否延期
                .eqIfPresent(PaymentApplicationDO::getIsDeferred, vo.getIsDeferred())
                .likeIfPresent(PaymentApplicationDO::getApplicantName, vo.getApplicantName())
                .likeIfPresent(PaymentApplicationDO::getPaymentApplyCode, vo.getPaymentApplyCode())
                .likeIfPresent(PaymentApplicationDO::getTitle, vo.getTitle())
                .likeIfPresent(PaymentApplicationDO::getContractName, vo.getContractName())
                .betweenIfPresent(PaymentApplicationDO::getApplyTime, vo.getApplyTime0(), vo.getApplyTime1())
                .orderByDesc(PaymentApplicationDO::getApplyTime)
        );
    }


    default List<PaymentApplicationDO> statisticsAmount(PaymentApplicationListBpmReqVO vo) {
        return selectList(new LambdaQueryWrapperX<PaymentApplicationDO>()
                .inIfPresent(PaymentApplicationDO::getContractId, vo.getContractIds()));
    }

    default Long count4Bench(List<String> instanceIdList) {
        if (CollectionUtils.isEmpty(instanceIdList)) {
            return 0L;
        }
        return selectCount(new LambdaQueryWrapperX<PaymentApplicationDO>().in(PaymentApplicationDO::getProcessInstanceId, instanceIdList));
    }



    default List<PaymentApplicationDO> selectList4lContractId(String contractId){
        MPJLambdaWrapper<PaymentApplicationDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(PaymentApplicationDO.class);
        wrapper.leftJoin(PaymentApplScheRelDO.class,PaymentApplScheRelDO::getApplicationId,PaymentApplicationDO::getId);
        wrapper.leftJoin(PaymentScheduleDO.class,PaymentScheduleDO::getId,PaymentApplScheRelDO::getScheduleId)
                .distinct()
        ;
        wrapper.eq(PaymentScheduleDO::getContractId,contractId);
        return selectList(wrapper);

    }
}
