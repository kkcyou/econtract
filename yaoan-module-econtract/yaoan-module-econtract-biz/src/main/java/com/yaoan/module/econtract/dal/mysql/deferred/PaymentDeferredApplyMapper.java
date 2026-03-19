package com.yaoan.module.econtract.dal.mysql.deferred;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.payment.deferred.vo.PaymentDeferredListReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmReqVO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.deferred.PaymentDeferredApplyDO;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/30 16:09
 */
@Mapper
public interface PaymentDeferredApplyMapper extends BaseMapperX<PaymentDeferredApplyDO> {

    /**
     * 审批-列表（审批人查看 ）
     * param draftFlag 是否要查询起草的申请(流程实例id为null)
     */
    default PageResult<PaymentDeferredApplyDO> selectApprovePage(PaymentApplicationListBpmReqVO vo, Boolean draftFlag) {
        if (CollectionUtils.isEmpty(vo.getInstanceIdList())) {
            return new PageResult<PaymentDeferredApplyDO>().setTotal(0L).setList(Collections.emptyList());
        }
        LambdaQueryWrapperX<PaymentDeferredApplyDO> wrapperX = new LambdaQueryWrapperX<PaymentDeferredApplyDO>()
                .orderByDesc(PaymentDeferredApplyDO::getUpdateTime)
                .orderByDesc(PaymentDeferredApplyDO::getApplyTime);
        if (draftFlag){
            wrapperX.and(w -> w.in(PaymentDeferredApplyDO::getProcessInstanceId, vo.getInstanceIdList()).or().isNull(PaymentDeferredApplyDO::getProcessInstanceId));
        } else {
            wrapperX.in(PaymentDeferredApplyDO::getProcessInstanceId, vo.getInstanceIdList());

        }

        if (StringUtils.isNotEmpty(vo.getFrontCode())) {
            CommonFlowableReqVOResultStatusEnums enums = CommonFlowableReqVOResultStatusEnums.getFrontInstance(vo.getFrontCode());
            if (ObjectUtil.isNotNull(enums)) {
                switch (enums) {
                    case TO_SEND:
                        wrapperX.and(
                                w -> w.eq(PaymentDeferredApplyDO::getResult, CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode())
                                        .or()
                                        .eq(PaymentDeferredApplyDO::getResult, CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode())
                        );

                        break;
                    default:
                        wrapperX.eq(PaymentDeferredApplyDO::getResult, enums.getResultCode());
                        break;
                }
            }
        }
        return selectPage(vo, wrapperX);
    }

    default PageResult<PaymentDeferredApplyDO> selectPage(PaymentDeferredListReqVO vo) {
        LambdaQueryWrapperX<PaymentDeferredApplyDO> wrapperX = new LambdaQueryWrapperX<PaymentDeferredApplyDO>()
                .orderByDesc(PaymentDeferredApplyDO::getUpdateTime)
                .orderByDesc(PaymentDeferredApplyDO::getApplyTime);

        if (StringUtils.isNotBlank(vo.getInputStr())) {
            wrapperX.and(
                    w -> w.like(PaymentDeferredApplyDO::getContractName, vo.getInputStr())
                            .or()
                            .like(PaymentDeferredApplyDO::getContractCode, vo.getInputStr())
            );
        }

        if (StringUtils.isNotEmpty(vo.getFrontCode())) {
            CommonFlowableReqVOResultStatusEnums enums = CommonFlowableReqVOResultStatusEnums.getFrontInstance(vo.getFrontCode());
            if (ObjectUtil.isNotNull(enums)) {
                switch (enums) {
                    case TO_SEND:
                        wrapperX.and(
                                w -> w.eq(PaymentDeferredApplyDO::getResult, CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode())
                                        .or()
                                        .eq(PaymentDeferredApplyDO::getResult, CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode())
                        );

                        break;
                    default:
                        wrapperX.eq(PaymentDeferredApplyDO::getResult, enums.getResultCode());
                        break;
                }
            }
        }

        return selectPage(vo, wrapperX);
    }

    default Long count4Bench(List<String> instanceIdList) {
        if (CollectionUtils.isEmpty(instanceIdList)) {
            return 0L;
        }
        return selectCount(new LambdaQueryWrapperX<PaymentDeferredApplyDO>().in(PaymentDeferredApplyDO::getProcessInstanceId, instanceIdList));
    }
}
