package com.yaoan.module.econtract.dal.dataobject.bpm.change;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 要素表
 * </p>
 *
 * @author Pele
 * @since 2024-01-24
 */
@Data
@TableName("ecms_element")
public class ElementDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = -5447455323788094180L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 要素
     */
    @TableField("element")
    private String element;
    /**
     * 要素名称
     */
    @TableField("element_name")
    private String elementName;

    /**
     * 要素名称
     */
    @TableField("element_type")
    private String elementType;



}
