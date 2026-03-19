package com.yaoan.module.econtract.dal.dataobject.agreement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_pref_agreement_rel")
public class PrefAgreementRelDO extends TenantBaseDO implements Serializable {
    private static final long serialVersionUID = 6751593521319175053L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 履约id
     */
    private String prefId;

    /**
     * 文件地址id
     */
    private Long infraFileId;

    /**
     * 文件名称
     */
    private String fileName;
}
