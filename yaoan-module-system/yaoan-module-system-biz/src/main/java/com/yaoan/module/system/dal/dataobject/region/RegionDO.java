package com.yaoan.module.system.dal.dataobject.region;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 内蒙区划DO
 * @author: Pele
 * @date: 2023/12/12 16:56
 */

@Data
@TableName("ecms_gcy_region")
@EqualsAndHashCode(callSuper = true)
public class RegionDO extends BaseDO {

    private static final long serialVersionUID = 9199555009021104566L;
    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 区划编号
     */
    @TableField("region_code")
    private Long regionCode;

    /**
     * 区划名称
     */
    @TableField("region_name")
    private String regionName;

    /**
     * 父级区划编号
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 是否是叶子节点
     */
    @TableField("leaf")
    private Boolean leaf;

    /**
     * 区划全称
     */
    @TableField("path")
    private String path;

    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 显示顺序
     *
     */
    private Integer sort;
}
