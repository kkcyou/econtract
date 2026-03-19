package com.yaoan.module.system.dal.dataobject.region;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 区划表
 * </p>
 *
 * @author doujiale
 * @since 2024-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_region")
public class Region extends BaseDO implements Serializable {


    private static final long serialVersionUID = 5434433995034850707L;

    /**
     * 区划id
     */
    @TableId("REGIONGUID")
    private String regionGuid;

    /**
     * 区划父ID
     */
    @TableField("REGIONPGUID")
    private String regionParentGuid;

    /**
     * 区划编码
     */
    @TableField("REGIONCODE")
    private String regionCode;

    /**
     * 监管区域编码
     */
    private String zoneCode;

    /**
     * 区划名称
     */
    @TableField("REGIONNAME")
    private String regionName;

    /**
     * 区划简称
     */
    @TableField("SHORTNAME")
    private String shortName;

    /**
     * 展示名称
     */
    @TableField("SHOWNAME")
    private String showName;

    /**
     * 接口编码（交互编号）
     */
    @TableField("INTERFACECODE")
    private String interfaceCode;

    /**
     * 日志路径
     */
//    private String LOGOPATH;
//    private String BANNER1;
//    private String BANNER2;
//    private String BANNER3;
//    private String BANNER4;
//    private String BANNER5;

    /**
     * 区划完整名称
     */
    @TableField("REGIONFULLNAME")
    private String regionFullName;

    /**
     * 根区划id
     */
//    private String ROOTREGIONGUID;

    /**
     * 区划类型
     */
//    private Integer REGIONTYPE;

    /**
     * 区划完整id
     */
//    private String REGIONFULLGUID;

    /**
     * 商品名称维护人员
     */
    private Integer commoditynamemaintenance;

    /**
     * 站点域名路径
     */
    private String sitePath;

    /**
     * 排序
     */
    private Integer sort;


}
