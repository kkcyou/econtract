package com.yaoan.module.econtract.dal.dataobject.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @description: 模板图片关系
 * @author: Pele
 * @date: 2023/8/22 21:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_model_pic")
public class ModelPic extends TenantBaseDO implements Serializable {

    private static final long serialVersionUID = 684206987740921382L;

    /**
     * 模板图片关系ID
     */
    private Long id;
    /**
     * 图片文件ID
     */
    private Long fileId;
    /**
     * 模板ID
     */
    private String modelId;


}
