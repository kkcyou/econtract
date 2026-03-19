package com.yaoan.module.econtract.controller.admin.contractdraft.vo;

import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;

import java.io.Serializable;

@Data
public class ContractTypeShort  {

    /**
     * 合同类型ID
     */
    private String id;

    /**
     * 合同类型名称
     */
    private String name;

}
