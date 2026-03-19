package com.yaoan.module.econtract.dal.dataobject.modelcategory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author doujl
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_client_model_category")
public class ClientModelCategory extends TenantBaseDO implements Serializable {

    private static final long serialVersionUID = -4457017746118509074L;
    /**
     * 分类id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * clientId
     */
    private String clientId;
    /**
     * 客户端主键id
     */
    private Long oauth2ClientId;

}
