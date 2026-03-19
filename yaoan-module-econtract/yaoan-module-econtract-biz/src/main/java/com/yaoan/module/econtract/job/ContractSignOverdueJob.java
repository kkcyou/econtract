package com.yaoan.module.econtract.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.yaoan.framework.quartz.core.handler.JobHandler;

import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.enums.ContractStatusEnums;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author doujiale
 */
@Slf4j
@Component
public class ContractSignOverdueJob implements JobHandler {

    @Resource
    private ContractMapper contractMapper;

    @Override
    @TenantIgnore
    public String execute(String param) throws Exception {

        log.debug("执行了合同过期判断任务～");

        List<ContractDO> contracts = contractMapper.selectList(ContractDO::getStatus, ContractStatusEnums.TO_BE_SIGNED.getCode());
        List<ContractDO> targetContracts = contracts.stream().filter(contractDO -> !DateUtil.isIn(DateUtil.date(), new DateTime(contractDO.getCreateTime()), contractDO.getExpirationDate())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(targetContracts)) {
            List<ContractDO> doList = targetContracts.stream().map(target -> target.setStatus(ContractStatusEnums.SIGN_OVERDUE.getCode())).collect(Collectors.toList());
            contractMapper.updateBatch(doList);
            return "执行合同过期任务完成：更新合同份数:" + targetContracts.size() + ",更新合同编号:" + targetContracts.stream().map(ContractDO::getCode).collect(Collectors.joining(", "));
        }
        return "执行合同过期任务完成：未检测到过期合同";
    }

}
