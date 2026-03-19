package com.yaoan.module.econtract.dal.dataobject.contracttemplate;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @description: 范本图片关系
 * @author: Pele
 * @date: 2023/8/23 8:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_template_pic")
public class TemplatePic extends TenantBaseDO implements Serializable {

    private static final long serialVersionUID = 304949540032698801L;


    /**
     * 范本图片关系ID
     */
    private Long id;
    /**
     * 图片文件ID
     */
    private Long fileId;
    /**
     * 范本ID
     */
    private String templateId;

}
