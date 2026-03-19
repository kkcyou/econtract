package com.yaoan.module.econtract.dal.dataobject.contracttemplate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_template_quote")
public class TemplateQuoteDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = -1544280231977273811L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 模板id
     */
    private String modelId;

    /**
     * 模板编码
     */
    private String modelCode;
    /**
     * 范本id
     */
    private String templateCode;
    /**
     * 单位名称
     */
    private String companyName;
    /**
     * 模板名称
     */
    private String modelName;
    /**
     * 版本号
     */
    private Double modelVersion;
    /**
     * 启用状态（0=未启用，1=启用中）
     */
    private Integer status;

    /**
     * 范本版本
     */
    private Double templateVersion;



}
