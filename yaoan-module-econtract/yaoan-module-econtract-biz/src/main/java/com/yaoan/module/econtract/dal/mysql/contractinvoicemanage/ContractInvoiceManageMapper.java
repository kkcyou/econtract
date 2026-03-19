package com.yaoan.module.econtract.dal.mysql.contractinvoicemanage;

import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage.vo.ContractInvoiceManagePageReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contractInvoiceManage.ContractInvoiceManageDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 发票 Mapper
 *
 * @author lls
 */
@Mapper
public interface ContractInvoiceManageMapper extends BaseMapperX<ContractInvoiceManageDO> {

    default PageResult<ContractInvoiceManageDO> selectPage(ContractInvoiceManagePageReqVO reqVO) {
        MPJLambdaWrapper<ContractInvoiceManageDO> wrapperX = new MPJLambdaWrapper<ContractInvoiceManageDO>();
        wrapperX.selectAll(ContractInvoiceManageDO.class);
        if (ObjectUtil.isNotEmpty(reqVO.getCode())) {
            wrapperX.eq(ContractInvoiceManageDO::getCode, reqVO.getCode());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getTitle())) {
            wrapperX.like(ContractInvoiceManageDO::getTitle, reqVO.getTitle());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getStatus())) {
            wrapperX.eq(ContractInvoiceManageDO::getStatus, reqVO.getStatus());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getAmountType())) {
            wrapperX.eq(ContractInvoiceManageDO::getAmountType, reqVO.getAmountType());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getRemark())) {
            wrapperX.eq(ContractInvoiceManageDO::getRemark, reqVO.getRemark());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getInvoiceCompany())) {
            wrapperX.eq(ContractInvoiceManageDO::getInvoiceCompany, reqVO.getInvoiceCompany());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getInvoiceType())) {
            wrapperX.eq(ContractInvoiceManageDO::getInvoiceType, reqVO.getInvoiceType());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getInvoiceAmont())) {
            wrapperX.eq(ContractInvoiceManageDO::getInvoiceAmont, reqVO.getInvoiceAmont());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getCurrencyType())) {
            wrapperX.eq(ContractInvoiceManageDO::getCurrencyType, reqVO.getCurrencyType());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getInvoiceDate())) {
            wrapperX.between(ContractInvoiceManageDO::getInvoiceDate, reqVO.getInvoiceDate()[0], reqVO.getInvoiceDate()[1]);
        }
        if (ObjectUtil.isNotEmpty(reqVO.getBuyerNumber())) {
            wrapperX.eq(ContractInvoiceManageDO::getBuyerNumber, reqVO.getBuyerNumber());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getBuyerTel())) {
            wrapperX.eq(ContractInvoiceManageDO::getBuyerTel, reqVO.getBuyerTel());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getBuyerAddress())) {
            wrapperX.eq(ContractInvoiceManageDO::getBuyerAddress, reqVO.getBuyerAddress());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getBankName())) {
            wrapperX.like(ContractInvoiceManageDO::getBankName, reqVO.getBankName());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getBankAccount())) {
            wrapperX.eq(ContractInvoiceManageDO::getBankAccount, reqVO.getBankAccount());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getSendType())) {
            wrapperX.eq(ContractInvoiceManageDO::getSendType, reqVO.getSendType());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getSendPerson())) {
            wrapperX.eq(ContractInvoiceManageDO::getSendPerson, reqVO.getSendPerson());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getSendTel())) {
            wrapperX.eq(ContractInvoiceManageDO::getSendTel, reqVO.getSendTel());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getSendAddress())) {
            wrapperX.eq(ContractInvoiceManageDO::getSendAddress, reqVO.getSendAddress());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getProcessInstanceId())) {
            wrapperX.eq(ContractInvoiceManageDO::getProcessInstanceId, reqVO.getProcessInstanceId());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getInstanceIdList())) {
            wrapperX.in(ContractInvoiceManageDO::getProcessInstanceId, reqVO.getInstanceIdList());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getCreateTime())) {
            wrapperX.between(ContractInvoiceManageDO::getCreateTime, reqVO.getCreateTime()[0], reqVO.getCreateTime()[1]);
        }
        wrapperX.orderByDesc(ContractInvoiceManageDO::getUpdateTime);
        if (StringUtils.isNotBlank(reqVO.getContractName())) {
            wrapperX.leftJoin(ContractDO.class, ContractDO::getId, ContractInvoiceManageDO::getContractId);
            wrapperX.and(
                    w -> w.like(ContractDO::getName, reqVO.getContractName())
                            .or()
                            .like(ContractDO::getCode, reqVO.getContractName())
            );
        }
        return selectPage(reqVO, wrapperX);
    }

    default Long count4Bench(List<String> instanceIdList) {
        if (CollectionUtils.isEmpty(instanceIdList)) {
            return 0L;
        }
        return selectCount(new LambdaQueryWrapperX<ContractInvoiceManageDO>().in(ContractInvoiceManageDO::getProcessInstanceId, instanceIdList));
    }
}