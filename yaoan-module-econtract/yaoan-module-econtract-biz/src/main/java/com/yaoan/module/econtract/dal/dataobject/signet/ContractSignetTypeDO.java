package com.yaoan.module.econtract.dal.dataobject.signet;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 印章表
 */

@Data
@TableName("ecms_contract_signet_type")
@EqualsAndHashCode(callSuper = true)
public class ContractSignetTypeDO extends DeptBaseDO {
    private static final long serialVersionUID = -2605570257794631052L;

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private String id;

    /**
     * 印章类型名称
     */
    private String name;

    /**
     * 印章类型编号
     */
    private String code;
}
