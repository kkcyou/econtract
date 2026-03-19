package com.yaoan.module.econtract.dal.dataobject.contracttemplate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 合同范本-条款关系表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "ecms_template_term")
public class TemplateTermDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 5649800129054339297L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 范本id
     */
    private String templateId;

    /**
     * 条款内容
     */
    private String term;

    /**
     * 坐标
     */
    private String position;

    /**
     * 说明
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sort;
}
