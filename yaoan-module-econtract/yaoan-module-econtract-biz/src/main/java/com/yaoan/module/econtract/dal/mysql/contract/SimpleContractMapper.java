package com.yaoan.module.econtract.dal.mysql.contract;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.bpm.register.vo.ContractRegisterListApproveReqVO;
import com.yaoan.module.econtract.controller.admin.change.vo.ContractChangePageReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.PaymentSchedulePageReqVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.LedgerListReqVo;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangeDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.register.BpmContractRegisterDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SimpleContractDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @description:
 * @author: Pele
 * @date: 2023/11/9 9:46
 */
@Mapper
public interface SimpleContractMapper extends BaseMapperX<SimpleContractDO> {


    default PageResult<SimpleContractDO> listPaymentManagement(PaymentSchedulePageReqVO paymentPlanRepVO, List<String> userIds1, List<String> userIds2) {
        List<String> finalUserIdList = getAllList(userIds1, userIds2);
        MPJLambdaWrapper<SimpleContractDO> mpjQueryWrapper = new MPJLambdaWrapper<SimpleContractDO>()
                .selectAll(SimpleContractDO.class)
                .orderByDesc(SimpleContractDO::getUpdateTime)
                .leftJoin(SignatoryRelDO.class, SignatoryRelDO::getContractId, SimpleContractDO::getId)
                .leftJoin(Relative.class, Relative::getId, SignatoryRelDO::getSignatoryId)
                //合同状态为签署完成
                .eq(SimpleContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode());
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getAmountType())) {
            mpjQueryWrapper.eq(SimpleContractDO::getAmountType, paymentPlanRepVO.getAmountType());
        }
        //当前时间小于合同生效结束时间
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getCurrentDate())) {
            mpjQueryWrapper.gt(SimpleContractDO::getValidity1, paymentPlanRepVO.getCurrentDate());
        }
        //合同名称编码
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getCode())) {
            mpjQueryWrapper.like(SimpleContractDO::getCode, paymentPlanRepVO.getCode());
        }
        //合同名称
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getName())) {
            mpjQueryWrapper.like(SimpleContractDO::getName, paymentPlanRepVO.getName());
        }
        //合同类型
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getContractType())) {
            mpjQueryWrapper.eq(SimpleContractDO::getContractType, paymentPlanRepVO.getContractType());
        }
        //我方签约主体
        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getPayerName())) {
            mpjQueryWrapper.in(SimpleContractDO::getCreator, userIds1);
        }
        //相对方
        if (ObjectUtil.isNotNull(paymentPlanRepVO.getPayeeName())) {
            mpjQueryWrapper.in(Relative::getId, userIds2);
        }
        //相对方签约主体
        if (ObjectUtil.isNotNull(paymentPlanRepVO.getPartBName())) {
            mpjQueryWrapper.like(SimpleContractDO::getPartBName, paymentPlanRepVO.getPartBName());
        }

        if (ObjectUtil.isNotNull(paymentPlanRepVO.getPayeeName()) && ObjectUtil.isNotEmpty(paymentPlanRepVO.getPayerName())) {
            mpjQueryWrapper.and(
                    w -> w.in(SimpleContractDO::getCreator, paymentPlanRepVO.getPayerName())
                            .or()
                            .like(Relative::getCompanyName, paymentPlanRepVO.getPayeeName())
            );
        }
        //相对方签约主体
        if (ObjectUtil.isNotNull(paymentPlanRepVO.getPartBName())) {
            mpjQueryWrapper.like(SimpleContractDO::getPartBName, paymentPlanRepVO.getPartBName());
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

    /**
     * 合同变更申请列表
     */
    default PageResult<SimpleContractDO> listSubmitContractChange(ContractChangePageReqVO vo) {
        MPJLambdaWrapper<SimpleContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<SimpleContractDO>()
                .selectAll(SimpleContractDO.class).orderByDesc(SimpleContractDO::getUpdateTime)
                .leftJoin(BpmContractChangeDO.class, BpmContractChangeDO::getContractId, SimpleContractDO::getId)
                .isNotNull(SimpleContractDO::getMainContractId);
        // 名称
        if (StringUtils.isNotBlank(vo.getName())) {
            mpjLambdaWrapper.like(SimpleContractDO::getName, vo.getName());
        }
        // 编码
        if (StringUtils.isNotBlank(vo.getCode())) {
            mpjLambdaWrapper.like(SimpleContractDO::getCode, vo.getCode());
        }
        // 变动类型
        if (ObjectUtil.isNotNull(vo.getChangeType())) {
            mpjLambdaWrapper.eq(SimpleContractDO::getChangeType, vo.getChangeType());
        }
        // 协议状态
        if (ObjectUtil.isNotNull(vo.getStatus())) {
            mpjLambdaWrapper.eq(SimpleContractDO::getStatus, vo.getStatus());
        }
        // 申请时间.
        if (ObjectUtil.isNotNull(vo.getSubmitTime0())) {
            mpjLambdaWrapper.in(BpmContractChangeDO::getCreateTime, vo.getSubmitTime0(), vo.getSubmitTime1());
        }

        if (StringUtils.isNotEmpty(vo.getFrontCode())) {
            CommonFlowableReqVOResultStatusEnums enums = CommonFlowableReqVOResultStatusEnums.getFrontInstance(vo.getFrontCode());
            if (ObjectUtil.isNotNull(enums)) {
                switch (enums) {
                    case TO_SEND:

//                        mpjLambdaWrapper.and(w -> w.eq(BpmContractChangeDO::getResult, CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode())
//                                .or()
//                                .eq(BpmContractChangeDO::getResult, CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode()));
                        //待发送，被退回，撤销
                        List<Integer> resultList = new ArrayList<Integer>();
                        resultList.add(CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode());
                        resultList.add(CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode());
                        resultList.add(CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode());
                        mpjLambdaWrapper.in(BpmContractChangeDO::getResult, resultList);

                        break;
                    default:
                        mpjLambdaWrapper.eq(BpmContractChangeDO::getResult, enums.getResultCode());
                        break;
                }
            }
        }
        return selectPage(vo, mpjLambdaWrapper);
    }

    /**
     * 合同登记审批列表
     */
    default PageResult<SimpleContractDO> selectContractRegisterApprovePage(ContractRegisterListApproveReqVO pageVO) {
        if (CollectionUtils.isEmpty(pageVO.getInstanceIdList())) {
            return new PageResult<SimpleContractDO>().setTotal(0L).setList(Collections.emptyList());
        }
        MPJLambdaWrapper<SimpleContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<SimpleContractDO>()
                .leftJoin(BpmContractRegisterDO.class, BpmContractRegisterDO::getContractId, SimpleContractDO::getId)
                .selectAll(SimpleContractDO.class)
                //补录合同
                .eq(SimpleContractDO::getUpload, ContractUploadTypeEnums.REGISTER.getCode())
                .orderByDesc(SimpleContractDO::getUpdateTime)
                .distinct();

        if (ObjectUtil.isNotNull(pageVO.getResult())) {
            mpjLambdaWrapper.eq(BpmContractRegisterDO::getResult, pageVO.getResult());
        }
        if (CollectionUtil.isNotEmpty(pageVO.getInstanceIdList())) {
            mpjLambdaWrapper.in(BpmContractRegisterDO::getProcessInstanceId, pageVO.getInstanceIdList());
        }
        if (ObjectUtil.isNotNull(pageVO.getSubmitTime0())) {
            mpjLambdaWrapper.between(SimpleContractDO::getSubmitTime, pageVO.getSubmitTime0(), pageVO.getSubmitTime1());
        }
        if (ObjectUtil.isNotNull(pageVO.getSignDate0())) {
            mpjLambdaWrapper.between(SimpleContractDO::getSignDate, pageVO.getSignDate0(), pageVO.getSignDate1());
        }
        if (ObjectUtil.isNotNull(pageVO.getAmount0())) {
            mpjLambdaWrapper.between(SimpleContractDO::getAmount, pageVO.getAmount0(), pageVO.getAmount1());
        }
        if (ObjectUtil.isNotNull(pageVO.getContractType())) {
            mpjLambdaWrapper.eq(SimpleContractDO::getContractType, pageVO.getContractType());
        }
        if (ObjectUtil.isNotNull(pageVO.getSignType())) {
            mpjLambdaWrapper.eq(SimpleContractDO::getSignType, pageVO.getSignType());
        }

        if (ObjectUtil.isNotNull(pageVO.getResult())) {
            mpjLambdaWrapper.eq(BpmContractRegisterDO::getResult, pageVO.getResult());
        }
        if (ObjectUtil.isNotNull(pageVO.getCode())) {
            mpjLambdaWrapper.like(SimpleContractDO::getCode, pageVO.getCode());
        }
        if (ObjectUtil.isNotNull(pageVO.getName())) {
            mpjLambdaWrapper.like(SimpleContractDO::getName, pageVO.getName());
        }
        if (ObjectUtil.isNotNull(pageVO.getApplicantName())) {
            mpjLambdaWrapper.in(BpmContractRegisterDO::getCreator, pageVO.getUserList());
        }

        return selectPage(pageVO, mpjLambdaWrapper);
    }

    /**
     * 台账模块分页列表(从合同表查)
     */
    default PageResult<SimpleContractDO> listLedgerFromContract(LedgerListReqVo vo) {
        return selectPage(vo, new LambdaQueryWrapperX<SimpleContractDO>()
                //签署完成的合同会进入到台账
                .eq(SimpleContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED)
                //合同名称
                .likeIfPresent(SimpleContractDO::getName, vo.getName())
                //合同类型
                .eqIfPresent(SimpleContractDO::getContractType, vo.getContractType())
                //签署时间
                .betweenIfPresent(SimpleContractDO::getSignDate, vo.getSignTimeStart(), vo.getSignTimeEnd())
        );
    }
}
