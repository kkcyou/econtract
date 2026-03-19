package com.yaoan.module.econtract.dal.dataobject.modelcategory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author Pele
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_model_category")
public class ModelCategory extends TenantBaseDO implements Serializable {

    private static final long serialVersionUID = -2426987718162417291L;
    /**
     * 分类id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父分类id
     */
    private Integer parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类编码
     */
    private String code;
    /**
     * 公司id
     */
    private Long companyId;
}
