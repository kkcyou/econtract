package com.yaoan.module.econtract.dal.dataobject.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @description:
 * @author: Pele
 * @date: 2025/4/1 17:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_model_client")
public class ModelClientDO extends TenantBaseDO implements Serializable {


    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 平台标识
     */
    private String platformFlag;

    /**
     * 平台名称
     */
    private String platformName;

}
