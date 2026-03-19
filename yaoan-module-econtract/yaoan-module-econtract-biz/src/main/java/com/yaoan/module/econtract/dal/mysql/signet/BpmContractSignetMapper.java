package com.yaoan.module.econtract.dal.mysql.signet;

import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.signet.vo.SealApplicationListBpmReqVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangeDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.signet.BpmContractSignetDO;
import com.yaoan.module.econtract.dal.dataobject.signet.ContractSignetDO;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BpmContractSignetMapper extends BaseMapperX<BpmContractSignetDO> {


    default PageResult<BpmContractSignetDO> selectBpmPage(SealApplicationListBpmReqVO pageVO){
        MPJLambdaWrapper<BpmContractSignetDO> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.selectAll(BpmContractSignetDO.class);
        queryWrapper.eq(BpmContractSignetDO::getCompanyId,pageVO.getCompanyId());
        queryWrapper.orderByDesc(BpmContractSignetDO::getUpdateTime);
        //是否政采
        if(IfNumEnums.YES.getCode().equals(pageVO.getIsGov())) {
            queryWrapper.leftJoin(ContractDO.class, ContractDO::getId,BpmContractSignetDO::getBusinessId)
                    .in(ContractDO::getUpload, ContractUploadTypeEnums.THIRD_PARTY.getCode(), ContractUploadTypeEnums.ORDER_DRAFT.getCode());
        }
        if(IfNumEnums.NO.getCode().equals(pageVO.getIsGov())) {
            queryWrapper.leftJoin(ContractDO.class, ContractDO::getId,BpmContractSignetDO::getBusinessId)
                    .in(ContractDO::getUpload,
                            ContractUploadTypeEnums.MODEL_DRAFT.getCode(),
                            ContractUploadTypeEnums.UPLOAD_FILE.getCode(),
                            ContractUploadTypeEnums.REGISTER.getCode(),
                            ContractUploadTypeEnums.COMPANY_LEVEL.getCode(),
                            ContractUploadTypeEnums.UPLOAD_CONTRACT_FILE.getCode());
        }
        if(ObjectUtil.isNotEmpty(pageVO.getContractName())){
            queryWrapper.leftJoin(ContractDO.class, ContractDO::getId,BpmContractSignetDO::getBusinessId)
                    .like(ContractDO::getName,pageVO.getContractName());
        }
        if(ObjectUtil.isNotEmpty(pageVO.getContractCode())){
            queryWrapper.leftJoin(ContractDO.class, ContractDO::getId,BpmContractSignetDO::getBusinessId)
                    .like(ContractDO::getCode,pageVO.getContractCode());
        }
        if(ObjectUtil.isNotEmpty(pageVO.getSealName())){
            queryWrapper.leftJoin(ContractSignetDO.class,ContractSignetDO::getId,BpmContractSignetDO::getSignetId)
            .like(ContractSignetDO::getSealName,pageVO.getSealName());
        }
        if(ObjectUtil.isNotEmpty(pageVO.getInstanceIdList())){
            queryWrapper.in(BpmContractSignetDO::getProcessInstanceId,pageVO.getInstanceIdList());
        }else{
            queryWrapper.apply("1=0");
        }
        if(ObjectUtil.isNotEmpty(pageVO.getResult())){
            queryWrapper.eq(BpmContractSignetDO::getResult,pageVO.getResult());
        }else {
            queryWrapper.ne(BpmContractChangeDO::getResult, 0);
        }

    return selectPage(pageVO,queryWrapper);
    }

    default Long selectBpmSignetNum(SealApplicationListBpmReqVO pageVO){
        MPJLambdaWrapper<BpmContractSignetDO> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.eq(BpmContractSignetDO::getCompanyId,pageVO.getCompanyId());
        //是否政采
        if(IfNumEnums.YES.getCode().equals(pageVO.getIsGov())) {
            queryWrapper.leftJoin(ContractDO.class, ContractDO::getId,BpmContractSignetDO::getBusinessId)
                    .in(ContractDO::getUpload, ContractUploadTypeEnums.THIRD_PARTY.getCode(), ContractUploadTypeEnums.ORDER_DRAFT.getCode());
        }
        if(IfNumEnums.NO.getCode().equals(pageVO.getIsGov())) {
            queryWrapper.leftJoin(ContractDO.class, ContractDO::getId,BpmContractSignetDO::getBusinessId)
                    .in(ContractDO::getUpload,
                            ContractUploadTypeEnums.MODEL_DRAFT.getCode(),
                            ContractUploadTypeEnums.UPLOAD_FILE.getCode(),
                            ContractUploadTypeEnums.REGISTER.getCode(),
                            ContractUploadTypeEnums.COMPANY_LEVEL.getCode(),
                            ContractUploadTypeEnums.UPLOAD_CONTRACT_FILE.getCode());
        }
        if(ObjectUtil.isNotEmpty(pageVO.getContractName())){
            queryWrapper.leftJoin(ContractDO.class, ContractDO::getId,BpmContractSignetDO::getBusinessId)
                    .like(ContractDO::getName,pageVO.getContractName());
        }
        if(ObjectUtil.isNotEmpty(pageVO.getSealName())){
            queryWrapper.leftJoin(ContractSignetDO.class,ContractSignetDO::getId,BpmContractSignetDO::getSignetId)
                    .like(ContractSignetDO::getSealName,pageVO.getSealName());
        }
        if(ObjectUtil.isNotEmpty(pageVO.getInstanceIdList())){
            queryWrapper.in(BpmContractSignetDO::getProcessInstanceId,pageVO.getInstanceIdList());
        }else{
            queryWrapper.apply("1=0");
        }
        if(ObjectUtil.isNotEmpty(pageVO.getResult())){
            queryWrapper.eq(BpmContractSignetDO::getResult,pageVO.getResult());
        }else {
            queryWrapper.ne(BpmContractChangeDO::getResult, 0);
        }

        return selectCount(queryWrapper);
    }











    default PageResult<BpmContractSignetDO> getSignetProcessPage(SealApplicationListBpmReqVO pageVO){
        MPJLambdaWrapper<BpmContractSignetDO> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.selectAll(BpmContractSignetDO.class);
        queryWrapper.eq(BpmContractSignetDO::getCompanyId,pageVO.getCompanyId());
        queryWrapper.orderByDesc(BpmContractSignetDO::getUpdateTime);
        if(ObjectUtil.isNotEmpty(pageVO.getContractName())){
            queryWrapper.leftJoin(ContractDO.class, ContractDO::getId,BpmContractSignetDO::getBusinessId)
                    .like(ContractDO::getName,pageVO.getContractName());
        }
        if(ObjectUtil.isNotEmpty(pageVO.getSealName())){
            queryWrapper.leftJoin(ContractSignetDO.class,ContractSignetDO::getId,BpmContractSignetDO::getSignetId)
                    .like(ContractSignetDO::getSealName,pageVO.getSealName());
        }
        if(ObjectUtil.isNotEmpty(pageVO.getResult())){
            queryWrapper.eq(BpmContractSignetDO::getResult,pageVO.getResult());
        }else{
            queryWrapper.ne(BpmContractChangeDO::getResult, 0);
        }
        return selectPage(pageVO,queryWrapper);
    }
}
