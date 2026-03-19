package com.yaoan.module.econtract.dal.mysql.change;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.change.vo.ContractChangeListReqVO;
import com.yaoan.module.econtract.controller.admin.change.vo.ContractChangePageReqVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangeDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SimpleContractDO;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/24 17:49
 */
@Mapper
public interface BpmContractChangeMapper extends BaseMapperX<BpmContractChangeDO> {

    default PageResult<BpmContractChangeDO> listSubmitContractChangeFast(ContractChangePageReqVO reqVO) {
        LambdaQueryWrapperX<BpmContractChangeDO> wrapperX = new LambdaQueryWrapperX<BpmContractChangeDO>()
                .orderByDesc(BpmContractChangeDO::getUpdateTime)
                .likeIfPresent(BpmContractChangeDO::getChangeName, reqVO.getName())
                .likeIfPresent(BpmContractChangeDO::getChangeCode, reqVO.getCode())
                .eqIfPresent(BpmContractChangeDO::getChangeType, reqVO.getChangeType())
                .eqIfPresent(BpmContractChangeDO::getResult, reqVO.getStatus())
                .betweenIfPresent(BpmContractChangeDO::getCreateTime, reqVO.getSubmitTime0(), reqVO.getSubmitTime1());

        if (StringUtils.isNotEmpty(reqVO.getFrontCode())) {
            CommonFlowableReqVOResultStatusEnums enums = CommonFlowableReqVOResultStatusEnums.getFrontInstance(reqVO.getFrontCode());
            if (ObjectUtil.isNotNull(enums)) {
                switch (enums) {
                    case TO_SEND:
//                        wrapperX.and(w -> w.eq(BpmContractChangeDO::getResult, CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode())
//                                .or()
//                                .eq(BpmContractChangeDO::getResult, CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode()));
                        List<Integer> resultList = new ArrayList<Integer>();
                        resultList.add(CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode());
                        resultList.add(CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode());
                        resultList.add(CommonFlowableReqVOResultStatusEnums.CANCEL.getResultCode());
                        wrapperX.in(BpmContractChangeDO::getResult, resultList);


                        break;
                    default:
                        wrapperX.eq(BpmContractChangeDO::getResult, enums.getResultCode());
                        break;
                }
            }
        }
        return selectPage(reqVO, wrapperX);

    }


    default PageResult<BpmContractChangeDO> selectContractChangeApprovePage(CommonBpmAutoPageReqVO pageVO) {
        if (CollectionUtils.isEmpty(pageVO.getInstanceIdList())) {
            return new PageResult<BpmContractChangeDO>().setList(Collections.emptyList()).setTotal(0L);
        }
        MPJLambdaWrapper<BpmContractChangeDO> mpjLambdaWrapper = new MPJLambdaWrapper<BpmContractChangeDO>()
                .selectAll(BpmContractChangeDO.class)
                //排除被撤回的申请
                .ne(BpmContractChangeDO::getResult, BpmProcessInstanceResultEnum.CANCEL.getResult())
                .orderByDesc(BpmContractChangeDO::getUpdateTime)
                .distinct();
        if(ObjectUtil.isNotEmpty(pageVO.getContractName())){
            mpjLambdaWrapper.leftJoin(ContractDO.class, ContractDO::getId, BpmContractChangeDO::getMainContractId);
            mpjLambdaWrapper.like(ContractDO::getName, pageVO.getContractName());
        }
        if(ObjectUtil.isNotEmpty(pageVO.getContractCode())){
            mpjLambdaWrapper.leftJoin(ContractDO.class, ContractDO::getId, BpmContractChangeDO::getMainContractId);
            mpjLambdaWrapper.like(ContractDO::getCode, pageVO.getContractCode());
        }
        if (ObjectUtil.isNotNull(pageVO.getResult())) {
            mpjLambdaWrapper.eq(BpmContractChangeDO::getResult, pageVO.getResult());
        }
        if (CollectionUtil.isNotEmpty(pageVO.getInstanceIdList())) {
            mpjLambdaWrapper.in(BpmContractChangeDO::getProcessInstanceId, pageVO.getInstanceIdList());
        }
        if (ObjectUtil.isNotNull(pageVO.getChangeTypes())) {
            mpjLambdaWrapper.in(BpmContractChangeDO::getChangeType, pageVO.getChangeTypes());
        }
        if (ObjectUtil.isNotNull(pageVO.getChangeType())) {
            mpjLambdaWrapper.in(BpmContractChangeDO::getChangeType, pageVO.getChangeType());
        }
        if (ObjectUtil.isNotNull(pageVO.getResult())) {
            mpjLambdaWrapper.eq(BpmContractChangeDO::getResult, pageVO.getResult());
        }
        if (StringUtils.isNotBlank(pageVO.getCode())) {
            mpjLambdaWrapper.like(BpmContractChangeDO::getChangeCode, pageVO.getCode());
        }
        if (StringUtils.isNotBlank(pageVO.getName())) {
            mpjLambdaWrapper.like(BpmContractChangeDO::getChangeName, pageVO.getName());
        }
        if (StringUtils.isNotBlank(pageVO.getApplicantName())) {
            if(CollectionUtil.isNotEmpty(pageVO.getUserList())){
                mpjLambdaWrapper.in(BpmContractChangeDO::getCreator, pageVO.getUserList());
            }else {
                return new PageResult<BpmContractChangeDO>().setList(Collections.emptyList()).setTotal(0L);
            }
        }
        if(ObjectUtil.isNotEmpty(pageVO.getIsStatusChange())){
            if(pageVO.getIsStatusChange().equals(0)){
                mpjLambdaWrapper.in(BpmContractChangeDO::getChangeType, ContractChangeTypeEnums.CHANGE.getCode(), ContractChangeTypeEnums.SUPPLEMENT.getCode(),ContractChangeTypeEnums.TERMINATE.getCode());
            }else{
                mpjLambdaWrapper.in(BpmContractChangeDO::getChangeType, ContractChangeTypeEnums.TERMINATED.getCode(), ContractChangeTypeEnums.CANCEL.getCode(), ContractChangeTypeEnums.CANCELLATION.getCode());
            }
        }
        //是否政采
        if(IfNumEnums.YES.getCode().equals(pageVO.getIsGov())) {
            mpjLambdaWrapper.leftJoin(ContractDO.class, ContractDO::getId, BpmContractChangeDO::getMainContractId)
                    .in(ContractDO::getUpload, ContractUploadTypeEnums.THIRD_PARTY.getCode(), ContractUploadTypeEnums.ORDER_DRAFT.getCode());
        }
        if(IfNumEnums.NO.getCode().equals(pageVO.getIsGov())) {
            mpjLambdaWrapper.leftJoin(ContractDO.class, ContractDO::getId,BpmContractChangeDO::getMainContractId)
                    .in(ContractDO::getUpload,
                            ContractUploadTypeEnums.MODEL_DRAFT.getCode(),
                            ContractUploadTypeEnums.UPLOAD_FILE.getCode(),
                            ContractUploadTypeEnums.REGISTER.getCode(),
                            ContractUploadTypeEnums.COMPANY_LEVEL.getCode(),
                            ContractUploadTypeEnums.UPLOAD_CONTRACT_FILE.getCode());
        }
        return selectPage(pageVO, mpjLambdaWrapper);
    }

    default Long selectContractChangeNum(CommonBpmAutoPageReqVO pageVO) {
        if (CollectionUtils.isEmpty(pageVO.getInstanceIdList())) {
            return 0L;
        }
        MPJLambdaWrapper<BpmContractChangeDO> mpjLambdaWrapper = new MPJLambdaWrapper<BpmContractChangeDO>()
                //排除被撤回的申请
                .ne(BpmContractChangeDO::getResult, BpmProcessInstanceResultEnum.CANCEL.getResult());
        if(ObjectUtil.isNotEmpty(pageVO.getContractName())){
            mpjLambdaWrapper.leftJoin(ContractDO.class, ContractDO::getId, BpmContractChangeDO::getMainContractId);
            mpjLambdaWrapper.like(ContractDO::getName, pageVO.getContractName());
        }
        if(ObjectUtil.isNotEmpty(pageVO.getContractCode())){
            mpjLambdaWrapper.leftJoin(ContractDO.class, ContractDO::getId, BpmContractChangeDO::getMainContractId);
            mpjLambdaWrapper.like(ContractDO::getCode, pageVO.getContractCode());
        }
        if (ObjectUtil.isNotNull(pageVO.getResult())) {
            mpjLambdaWrapper.eq(BpmContractChangeDO::getResult, pageVO.getResult());
        }
        if (CollectionUtil.isNotEmpty(pageVO.getInstanceIdList())) {
            mpjLambdaWrapper.in(BpmContractChangeDO::getProcessInstanceId, pageVO.getInstanceIdList());
        }
        if (ObjectUtil.isNotNull(pageVO.getChangeTypes())) {
            mpjLambdaWrapper.in(BpmContractChangeDO::getChangeType, pageVO.getChangeTypes());
        }
        if (ObjectUtil.isNotNull(pageVO.getChangeType())) {
            mpjLambdaWrapper.in(BpmContractChangeDO::getChangeType, pageVO.getChangeType());
        }
        if (ObjectUtil.isNotNull(pageVO.getResult())) {
            mpjLambdaWrapper.eq(BpmContractChangeDO::getResult, pageVO.getResult());
        }
        if (StringUtils.isNotBlank(pageVO.getCode())) {
            mpjLambdaWrapper.like(BpmContractChangeDO::getChangeCode, pageVO.getCode());
        }
        if (StringUtils.isNotBlank(pageVO.getName())) {
            mpjLambdaWrapper.like(BpmContractChangeDO::getChangeName, pageVO.getName());
        }
        if (StringUtils.isNotBlank(pageVO.getApplicantName())) {
            if(CollectionUtil.isNotEmpty(pageVO.getUserList())){
                mpjLambdaWrapper.in(BpmContractChangeDO::getCreator, pageVO.getUserList());
            }else {
                return 0L;
            }
        }
        if(ObjectUtil.isNotEmpty(pageVO.getIsStatusChange())){
            if(pageVO.getIsStatusChange().equals(0)){
                mpjLambdaWrapper.in(BpmContractChangeDO::getChangeType, ContractChangeTypeEnums.CHANGE.getCode(), ContractChangeTypeEnums.SUPPLEMENT.getCode(),ContractChangeTypeEnums.TERMINATE.getCode());
            }else{
                mpjLambdaWrapper.in(BpmContractChangeDO::getChangeType, ContractChangeTypeEnums.TERMINATED.getCode(), ContractChangeTypeEnums.CANCEL.getCode(), ContractChangeTypeEnums.CANCELLATION.getCode());
            }
        }
        //是否政采
        if(IfNumEnums.YES.getCode().equals(pageVO.getIsGov())) {
            mpjLambdaWrapper.leftJoin(ContractDO.class, ContractDO::getId, BpmContractChangeDO::getMainContractId)
                    .in(ContractDO::getUpload, ContractUploadTypeEnums.THIRD_PARTY.getCode(), ContractUploadTypeEnums.ORDER_DRAFT.getCode());
        }
        if(IfNumEnums.NO.getCode().equals(pageVO.getIsGov())) {
            mpjLambdaWrapper.leftJoin(ContractDO.class, ContractDO::getId,BpmContractChangeDO::getMainContractId)
                    .in(ContractDO::getUpload,
                            ContractUploadTypeEnums.MODEL_DRAFT.getCode(),
                            ContractUploadTypeEnums.UPLOAD_FILE.getCode(),
                            ContractUploadTypeEnums.REGISTER.getCode(),
                            ContractUploadTypeEnums.COMPANY_LEVEL.getCode(),
                            ContractUploadTypeEnums.UPLOAD_CONTRACT_FILE.getCode());
        }
        return  selectCount(mpjLambdaWrapper);
    }



    default PageResult<BpmContractChangeDO> selectPage(ContractChangeListReqVO reqVO){
        MPJLambdaWrapper<BpmContractChangeDO> wrapperX = new MPJLambdaWrapper<>();
        wrapperX.orderByDesc(BpmContractChangeDO::getUpdateTime);
        wrapperX.selectAll(BpmContractChangeDO.class);
        wrapperX.leftJoin(ContractDO.class, ContractDO::getId, BpmContractChangeDO::getMainContractId);
        wrapperX.selectAs(ContractDO::getCode, BpmContractChangeDO::getContractId);
        if(ObjectUtil.isNotEmpty(reqVO.getContractName())){
            wrapperX.like(BpmContractChangeDO::getContractName, reqVO.getContractName());
        }
        if(ObjectUtil.isNotEmpty(reqVO.getContractCode())){
            wrapperX.like(ContractDO::getCode, reqVO.getContractCode());
        }
        if(ObjectUtil.isNotEmpty(reqVO.getName())){
            wrapperX.like(BpmContractChangeDO::getChangeName, reqVO.getName());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getCode())){
            wrapperX.like(BpmContractChangeDO::getChangeCode, reqVO.getCode());
        }
        if (ObjectUtil.isNotNull(reqVO.getChangeType())){
            wrapperX.eq(BpmContractChangeDO::getChangeType, reqVO.getChangeType());
        }
        if(ObjectUtil.isNotNull(reqVO.getResult())){
            wrapperX.eq(BpmContractChangeDO::getResult, reqVO.getResult());
        }else{
            wrapperX.ne(BpmContractChangeDO::getResult, 0);
        }

        return selectPage(reqVO, wrapperX);
    }
    /**
     * 合同状态变更列表(关闭、取消、作废)
     */
    default PageResult<BpmContractChangeDO> listOverContractChange(ContractChangePageReqVO vo) {
        MPJLambdaWrapper<BpmContractChangeDO> mpjLambdaWrapper = new MPJLambdaWrapper<BpmContractChangeDO>()
                .selectAll(BpmContractChangeDO.class).orderByDesc(BpmContractChangeDO::getUpdateTime)
                .leftJoin(ContractDO.class, ContractDO::getId,BpmContractChangeDO::getMainContractId)
               ;
        // 名称
        if (StringUtils.isNotBlank(vo.getName())) {
            mpjLambdaWrapper.like(ContractDO::getName, vo.getName());
        }
        // 编码
        if (StringUtils.isNotBlank(vo.getCode())) {
            mpjLambdaWrapper.like(ContractDO::getCode, vo.getCode());
        }
        // 变动类型
        if (ObjectUtil.isNotNull(vo.getChangeType())) {
            mpjLambdaWrapper.eq(BpmContractChangeDO::getChangeType, vo.getChangeType());
        }
        if (ObjectUtil.isNotNull(vo.getChangeTypes())) {
            mpjLambdaWrapper.in(BpmContractChangeDO::getChangeType, vo.getChangeTypes());
        }
        // 协议状态
        if (ObjectUtil.isNotNull(vo.getStatus())) {
            mpjLambdaWrapper.eq(ContractDO::getStatus, vo.getStatus());
        }
        // 申请时间.
        if (ObjectUtil.isNotNull(vo.getSubmitTime0())) {
            mpjLambdaWrapper.in(BpmContractChangeDO::getCreateTime, vo.getSubmitTime0(), vo.getSubmitTime1());
        }
        if(ObjectUtil.isNotNull(vo.getFrontCode())){
            mpjLambdaWrapper.eq(BpmContractChangeDO::getResult, vo.getFrontCode());
        }
        if(ObjectUtil.isNotNull(vo.getFrontCodes())){
            mpjLambdaWrapper.in(BpmContractChangeDO::getResult, vo.getFrontCodes());
        }
        return selectPage(vo, mpjLambdaWrapper);
    }
}
