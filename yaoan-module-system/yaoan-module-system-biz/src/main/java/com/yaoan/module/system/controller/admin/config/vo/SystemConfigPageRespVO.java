package com.yaoan.module.system.controller.admin.config.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/23 10:13
 */
@Data
public class SystemConfigPageRespVO {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标识
     */
    @TableField("key")
    private String key;

    /**
     * 内容
     */
    @TableField("value")
    private String value;

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

    private LocalDateTime updateTime;
}
