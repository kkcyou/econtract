package com.yaoan.module.econtract.api.contract.dto;

import lombok.Data;

/**
 * 包状态修改 - 能否起草状态 hidden
 */
@Data
public class ProductOrgDTO {

    private String orgId;	//采购单位id
    private String orgName;	//采购单位id
    private Integer deptId;		//部门ID
    private Integer userId;		//用户id
    private String username;			//用户账号
    private Integer companyId;//公司编号
    private Integer tenantId;//租户编号

}
