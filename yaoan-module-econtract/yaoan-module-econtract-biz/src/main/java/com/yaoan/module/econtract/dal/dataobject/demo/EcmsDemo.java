package com.yaoan.module.econtract.dal.dataobject.demo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * demo
 * </p>
 *
 * @author doujl
 * @since 2023-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_demo")
public class EcmsDemo extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = 5896801346332943048L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 姓名
     */
    private String name;

}
