package com.yaoan.module.system.dal.dataobject.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 系统动态配置
 * @author: Pele
 * @date: 2024/2/22 17:37
 */
@Data
@TableName("system_config")
@EqualsAndHashCode(callSuper = true)
public class SystemConfigDO extends DeptBaseDO {

    private static final long serialVersionUID = -9092861112504259239L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标识
     * {@link SystemConfigKeyEnums}
     */
    @TableField("c_key")
    private String CKey;

    /**
     * 内容
     */
    @TableField("c_value")
    private String CValue;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 启用状态（0=停用，1=启用）
     */
    @TableField("status")
    private Boolean status;

    /**
     * 流程定义key
     */
    @TableField("pro_def_key")
    private String proDefKey;
}
