package com.yaoan.module.econtract.dal.dataobject.bpm.change;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 变更要素表
 * </p>
 *
 * @author Pele
 * @since 2024-01-24
 */
@Data
@TableName("ecms_change_element")
public class ChangeElementDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = 8390491384078612143L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 变动id
     */
    @TableField("change_id")
    private String changeId;

    /**
     * 要素id
     */
    @TableField("element_id")
    private String elementId;
    /**
     * 变更前名称
     */
    @TableField("before_name")
    private String beforeName;

    /**
     * 变更后名称（简版）
     */
    @TableField("after_name")
    private String afterName;

}
