package com.yaoan.module.econtract.dal.dataobject.term;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author doujiale
 * @since 2023-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_model_term")
public class ModelTerm extends TenantBaseDO implements Serializable {

    private static final long serialVersionUID = -1154319834293573994L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同模板id
     */
    private String modelId;

    /**
     * 合同模板条款id
     */
    private String termId;

    /**
     * 条款序号
     */
    private Integer termNum;

    /**
     * 标题
     */
    private String title;

    /**
     * 能否编辑 0能编辑  1不能编辑
     */
    private Integer editable;

    /**
     * 名称
     */
    private String name;
}
