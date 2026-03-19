package com.yaoan.module.econtract.api.contract;

import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.api.contracttype.dto.ContractTypeDTO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ContractTypeApiImpl implements ContractTypeApi {

    @Resource
    private ContractTypeMapper contractTypeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ContractTypeDTO> createDefaultContractType(Long tenantId) {

        ContractType zcContractType = new ContractType();
        zcContractType.setName("政府采购类").setParentId("0").setCode("ZC").setCodeRuleId("de087c196fdf441ba1eaec3f26d8bec6").setTypeStatus(1).setTenantId(tenantId);
        contractTypeMapper.insert(zcContractType);
        String parentId = zcContractType.getId();
        List<ContractType> addList = new ArrayList<>();
        ContractType hwContractType = new ContractType();
        hwContractType.setName("货物类").setCode("AA00001").setTypePrefix("HW").setParentId(parentId).setTypeStatus(1).setCodeRuleId("de087c196fdf441ba1eaec3f26d8bec6").setRemark("").setDraftApprovalProcess("contract_approve").setRegisterProcess("contract_register_application_approve").setChangeProcess("contract_change_application_approve").setCollectionProcess("contract_invoice_approve").setPaymentProcess("payment_application_approve").setBorrowProcess("contract_borrow_submit_approve").setPlatId("1").setNeedSignet(true).setCreateTime(LocalDateTime.now()).setCreator("1").setUpdater("1").setUpdateTime(LocalDateTime.now()).setDeleted(false);
        hwContractType.setTenantId(tenantId);
        addList.add(hwContractType);
        ContractType fwContractType = new ContractType();
        fwContractType.setName("服务类").setCode("BB00002").setTypePrefix("FW").setParentId(parentId).setTypeStatus(1).setCodeRuleId("de087c196fdf441ba1eaec3f26d8bec6").setRemark("").setDraftApprovalProcess("contract_approve").setRegisterProcess("contract_register_application_approve").setChangeProcess("contract_change_application_approve").setCollectionProcess("contract_invoice_approve").setPaymentProcess("payment_application_approve").setBorrowProcess("contract_borrow_submit_approve").setPlatId("3").setNeedSignet(true).setCreateTime(LocalDateTime.now()).setCreator("1").setUpdater("1").setUpdateTime(LocalDateTime.now()).setDeleted(false);
        fwContractType.setTenantId(tenantId);
        addList.add(fwContractType);
        ContractType gcContractType = new ContractType();
        gcContractType.setName("工程类").setCode("AA00003").setTypePrefix("GC").setParentId(parentId).setTypeStatus(1).setCodeRuleId("de087c196fdf441ba1eaec3f26d8bec6").setRemark("").setDraftApprovalProcess("contract_approve").setRegisterProcess("contract_register_application_approve").setChangeProcess("contract_change_application_approve").setCollectionProcess("contract_invoice_approve").setPaymentProcess("payment_application_approve").setBorrowProcess("contract_borrow_submit_approve").setPlatId("2").setNeedSignet(true).setCreateTime(LocalDateTime.now()).setCreator("1").setUpdater("1").setUpdateTime(LocalDateTime.now()).setDeleted(false);
        gcContractType.setTenantId(tenantId);
        addList.add(gcContractType);
        contractTypeMapper.insertBatch(addList);
        return BeanUtils.toBean(addList, ContractTypeDTO.class);
    }
}
