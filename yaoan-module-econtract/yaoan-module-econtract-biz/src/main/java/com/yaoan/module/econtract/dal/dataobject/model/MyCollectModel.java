package com.yaoan.module.econtract.dal.dataobject.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

/**
 * @author Pele
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_my_collect_model")
public class MyCollectModel extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = 6706211311364032608L;
    /**
     * 模板id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 模板编码
     */
    private String code;
    /**
     * 模板名称
     */
    private String name;

    /**
     * 合同类型ID
     */
    private String contractType;

    /**
     * 时效模式 (0=部分时间有效，1=长期有效)
     */
    private Integer timeEffectModel;

    /**
     * 模板id
     */
    private String modelId;

}
