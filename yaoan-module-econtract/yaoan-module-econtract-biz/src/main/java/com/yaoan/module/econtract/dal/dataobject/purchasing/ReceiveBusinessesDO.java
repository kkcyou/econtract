package com.yaoan.module.econtract.dal.dataobject.purchasing;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 项目采购，电子卖场，框架协议id标识存放表实体类
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_receive_businesses")
public class ReceiveBusinessesDO extends DeptBaseDO implements Serializable {
    private static final long serialVersionUID = 1487281616394918000L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 类型 ：项目采购id为1，框架协议为2，电子卖场为3
     */
    private Integer type;
    /**
     * 项目关联id
     */
    private String projectPurchasingId;


}
