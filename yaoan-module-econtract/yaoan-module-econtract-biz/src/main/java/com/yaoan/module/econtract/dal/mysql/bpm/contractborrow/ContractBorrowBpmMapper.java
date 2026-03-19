package com.yaoan.module.econtract.dal.mysql.bpm.contractborrow;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.ContractBorrowBpmPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.ContractBorrowRecordPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow.ContractBorrowBpmDO;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.joda.time.LocalDateTime;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/10/8 21:22
 */
@Mapper
public interface ContractBorrowBpmMapper extends BaseMapperX<ContractBorrowBpmDO> {
    default PageResult<ContractBorrowBpmDO> selectPage(ContractBorrowBpmPageReqVO reqVO) {
        LambdaQueryWrapperX<ContractBorrowBpmDO> wrapperX = new LambdaQueryWrapperX<ContractBorrowBpmDO>()
                .orderByDesc(ContractBorrowBpmDO::getCreateTime)
                //提交时间（就是创建时间）
                .betweenIfPresent(ContractBorrowBpmDO::getCreateTime, reqVO.getStartCreateTime(), reqVO.getEndCreateTime())
                //审批状态
                .eqIfPresent(ContractBorrowBpmDO::getResult, reqVO.getResult())
                //将 流程实例存在的流程DO找出
                .inIfPresent(ContractBorrowBpmDO::getProcessInstanceId, reqVO.getProcessInstanceIds());

        //todo: 提交人查询
        return selectPage(reqVO, wrapperX);
    }

    default PageResult<ContractBorrowBpmDO> listApprovedTerm(ContractBorrowBpmPageReqVO reqVO) {
        LambdaQueryWrapperX<ContractBorrowBpmDO> wrapperX = new LambdaQueryWrapperX<ContractBorrowBpmDO>()
                .orderByDesc(ContractBorrowBpmDO::getUpdateTime);
        //查询申请时间范围
        wrapperX.betweenIfPresent(ContractBorrowBpmDO::getCreateTime, reqVO.getStartCreateTime(), reqVO.getEndCreateTime());
        //查询借阅时间范围
        wrapperX.betweenIfPresent(ContractBorrowBpmDO::getSubmitTime, reqVO.getStartBorrowTime(), reqVO.getEndBorrowTime());

        if (StringUtils.isNotEmpty(reqVO.getFrontCode())) {
            CommonFlowableReqVOResultStatusEnums enums = CommonFlowableReqVOResultStatusEnums.getFrontInstance(reqVO.getFrontCode());
            if (ObjectUtil.isNotNull(enums)) {
                switch (enums) {
                    case TO_SEND:
                        wrapperX.and(w -> w
                                .or()
                                .eq(ContractBorrowBpmDO::getResult, CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode())
                                .or()
                                .eq(ContractBorrowBpmDO::getResult, CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode()));

                        break;
                    default:
                        wrapperX.eq(ContractBorrowBpmDO::getResult, enums.getResultCode());
                        break;
                }
            }
        }
        return selectPage(reqVO, wrapperX);
    }

    default PageResult<ContractBorrowBpmDO> selectContractChangeApprovePage(ContractBorrowBpmPageReqVO reqVO) {
        if (CollectionUtils.isEmpty(reqVO.getInstanceIdList())) {
            return new PageResult<ContractBorrowBpmDO>().setTotal(0L).setList(Collections.emptyList());
        }
        MPJLambdaWrapper<ContractBorrowBpmDO> wrapperX = new MPJLambdaWrapper<ContractBorrowBpmDO>()
                .orderByDesc(ContractBorrowBpmDO::getUpdateTime)
                .selectAll(ContractBorrowBpmDO.class);
        if (ObjectUtil.isNotEmpty(reqVO.getResult())) {
            wrapperX.eq(ContractBorrowBpmDO::getResult, reqVO.getResult());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getInstanceIdList())) {
            wrapperX.in(ContractBorrowBpmDO::getProcessInstanceId, reqVO.getInstanceIdList());
        }
        //申请时间范围
        if(ObjectUtil.isNotEmpty(reqVO.getStartCreateTime()) && ObjectUtil.isNotEmpty(reqVO.getEndCreateTime())){
            wrapperX.between(ContractBorrowBpmDO::getCreateTime, reqVO.getStartCreateTime(), reqVO.getEndCreateTime());
        }

        //借阅时间范围
        if(ObjectUtil.isNotEmpty(reqVO.getStartBorrowTime()) && ObjectUtil.isNotEmpty(reqVO.getEndBorrowTime())){
            wrapperX.between(ContractBorrowBpmDO::getSubmitTime, reqVO.getStartBorrowTime(), reqVO.getEndBorrowTime());
        }
        //预计归还时间
        if(ObjectUtil.isNotEmpty(reqVO.getStartReturnTime()) && ObjectUtil.isNotEmpty(reqVO.getEndReturnTime())){
            wrapperX.between(ContractBorrowBpmDO::getReturnTime, reqVO.getStartReturnTime(), reqVO.getEndReturnTime());
        }
        wrapperX.leftJoin(ContractArchivesDO.class, ContractArchivesDO::getId, ContractBorrowBpmDO::getArchiveId);
        //档案名称
        if (ObjectUtil.isNotEmpty(reqVO.getArchiveName())) {
            wrapperX.like(ContractArchivesDO::getName, reqVO.getArchiveName());
        }
        //档案档号
//        if (ObjectUtil.isNotEmpty(reqVO.getArchiveCode())) {
//            wrapperX.like(ContractArchivesDO::getCode, reqVO.getArchiveCode());
//        }
        if (ObjectUtil.isNotEmpty(reqVO.getProCode())) {
            wrapperX.like(ContractArchivesDO::getProCode, reqVO.getProCode());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getProName())) {
            wrapperX.like(ContractArchivesDO::getProName, reqVO.getProName());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getUserList())) {
            wrapperX.in(ContractBorrowBpmDO::getCreator, reqVO.getUserList());
        }
        return selectPage(reqVO, wrapperX);
    }

    default PageResult<ContractBorrowBpmDO> selectBorrowRecordPage(ContractBorrowRecordPageReqVO reqVO, List<Long> deptIdList){
        MPJLambdaWrapper<ContractBorrowBpmDO> wrapperX = new MPJLambdaWrapper<ContractBorrowBpmDO>()
                .selectAll(ContractBorrowBpmDO.class);
        wrapperX.leftJoin(ContractArchivesDO.class, ContractArchivesDO::getId, ContractBorrowBpmDO::getArchiveId);
        //档案名称
        if (ObjectUtil.isNotEmpty(reqVO.getName())) {
            wrapperX.like(ContractArchivesDO::getName, reqVO.getName());
        }
//        //档号
//        if (ObjectUtil.isNotEmpty(reqVO.getCode())) {
//            wrapperX.like(ContractArchivesDO::getCode, reqVO.getCode());
//        }
//        //全宗号
//        if (ObjectUtil.isNotEmpty(reqVO.getFondsNo())) {
//            wrapperX.like(ContractArchivesDO::getFondsNo, reqVO.getFondsNo());
//        }
//        //一级类别号
//        if (ObjectUtil.isNotEmpty(reqVO.getFirstLevelNo())) {
//            wrapperX.like(ContractArchivesDO::getFirstLevelNo, reqVO.getFirstLevelNo());
//        }
//        //二级类别号
//        if (ObjectUtil.isNotEmpty(reqVO.getSecondLevelNo())) {
//            wrapperX.like(ContractArchivesDO::getSecondLevelNo, reqVO.getSecondLevelNo());
//        }
//        //案卷号
//        if (ObjectUtil.isNotEmpty(reqVO.getVolumeNo())) {
//            wrapperX.like(ContractArchivesDO::getVolumeNo, reqVO.getVolumeNo());
//        }
        // 项目名称
        if (ObjectUtil.isNotEmpty(reqVO.getProCode())) {
            wrapperX.like(ContractArchivesDO::getProCode, reqVO.getProCode());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getProName())) {
            wrapperX.like(ContractArchivesDO::getProName, reqVO.getProName());
        }
        Date now = new Date();
        //借阅状态
        if(ObjectUtil.isNotEmpty(reqVO.getBorrowStatus())){
            if( reqVO.getBorrowStatus().equals(5)){
                wrapperX.eq(ContractBorrowBpmDO::getResult,CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode());
            }else if( reqVO.getBorrowStatus().equals(1)){
                wrapperX.notIn(ContractBorrowBpmDO::getResult, CollectionUtil.newArrayList(CommonFlowableReqVOResultStatusEnums.SUCCESS.getResultCode(),CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode()));
            }else if(reqVO.getBorrowStatus().equals(2)){
                //待归档 借阅时间大于现在时间
                wrapperX.eq(ContractBorrowBpmDO::getResult, 2).gt(ContractBorrowBpmDO::getSubmitTime, now);
            }else if(reqVO.getBorrowStatus().equals(3)){
                //借阅中 电子：预计归还时间>现在时间 纸质：is_return=0
                wrapperX.eq(ContractBorrowBpmDO::getResult, 2)
                        .and(i -> i.or(j -> j
                                        .lt(ContractBorrowBpmDO::getSubmitTime, now)
                                        .eq(ContractBorrowBpmDO::getIsReturn, 0))
                                .or(j -> j
                                        .gt(ContractBorrowBpmDO::getReturnTime, now)
                                        .eq(ContractBorrowBpmDO::getBorrowType, "0")
                                        .lt(ContractBorrowBpmDO::getSubmitTime, now)
                                )
                        );
            }else if(reqVO.getBorrowStatus().equals(4)){
                //已归还 纸质：is_return = 1 电子：预计归还时间<NOW()
                wrapperX.eq(ContractBorrowBpmDO::getResult, 2)
                        .and(w -> w.eq(ContractBorrowBpmDO::getIsReturn, 1)
                                .or(j -> j.lt(ContractBorrowBpmDO::getReturnTime, now)
                                        .eq(ContractBorrowBpmDO::getBorrowType, "0")));

            }

        }
        //归档人
        if (ObjectUtil.isNotEmpty(reqVO.getDocumentName())) {
            wrapperX.like(ContractArchivesDO::getCreatorName, reqVO.getDocumentName());
        }
        //借阅人
        if (ObjectUtil.isNotEmpty(reqVO.getApplicantName())) {
            wrapperX.in(ContractBorrowBpmDO::getCreator, reqVO.getApplicantIds());
        }
        //归档时间
        if (ObjectUtil.isNotEmpty(reqVO.getBeginArchiveTime()) && ObjectUtil.isNotEmpty(reqVO.getEndArchiveTime())) {
            wrapperX.between(ContractArchivesDO::getArchiveTime, reqVO.getBeginArchiveTime(), reqVO.getEndArchiveTime());
        }
        //借阅时间
        if (ObjectUtil.isNotEmpty(reqVO.getStartBorrowTime()) && ObjectUtil.isNotEmpty(reqVO.getEndBorrowTime())) {
            wrapperX.between(ContractBorrowBpmDO::getSubmitTime, reqVO.getStartBorrowTime(), reqVO.getEndBorrowTime());
        }
        //预计归还时间
        if (ObjectUtil.isNotEmpty(reqVO.getStartReturnTime()) && ObjectUtil.isNotEmpty(reqVO.getEndReturnTime())) {
            wrapperX.between(ContractBorrowBpmDO::getReturnTime, reqVO.getStartReturnTime(), reqVO.getEndReturnTime());
        }
        //实际归还时间
        if (ObjectUtil.isNotEmpty(reqVO.getStartActualReturnTime()) && ObjectUtil.isNotEmpty(reqVO.getEndActualReturnTime())) {
            wrapperX.between(ContractBorrowBpmDO::getActualReturnTime, reqVO.getStartActualReturnTime(), reqVO.getEndActualReturnTime());
        }
        //部门
        if(ObjectUtil.isNotEmpty(deptIdList)){
            wrapperX.in(ContractBorrowBpmDO::getDeptId, deptIdList);
        }
        //按照借阅时间由近及远排序
        wrapperX.orderByDesc(ContractBorrowBpmDO::getSubmitTime);
        return selectPage(reqVO, wrapperX);
    }

    default Long count4Bench(List<String> instanceIdList){
        if (CollectionUtils.isEmpty(instanceIdList)) {
            return 0L;
        }
        return selectCount(new LambdaQueryWrapperX<ContractBorrowBpmDO>().in(ContractBorrowBpmDO::getProcessInstanceId,instanceIdList));
    }

    default List<ContractBorrowBpmDO> select4Ledger(String contractId){
        MPJLambdaWrapper<ContractBorrowBpmDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.leftJoin(ContractArchivesDO.class,ContractArchivesDO::getId,ContractBorrowBpmDO::getArchiveId)
                .eq(ContractArchivesDO::getContractId, contractId)
                .orderByDesc(ContractBorrowBpmDO::getSubmitTime)
                .selectAll(ContractBorrowBpmDO.class)
        ;
        return selectList(wrapper);

    }
}
