package com.yaoan.module.econtract.dal.dataobject.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_seal")
public class ContractSealDO extends TenantBaseDO {
    private static final long serialVersionUID = -3922708628987440387L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 签署方角色 0甲方 1乙方
     */
    private Integer role;

    /**
     * 合同章类型 0签名章 1骑缝章 2合同章
     */
    private Integer type;

    /**
     * 位置
     */
    private String position;

    /**
     * 比例
     */
    private String ratio;

    /**
     * 页码
     */
    private Long pageNumber;
}
