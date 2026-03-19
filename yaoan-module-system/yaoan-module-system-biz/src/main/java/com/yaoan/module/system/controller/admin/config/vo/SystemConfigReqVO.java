package com.yaoan.module.system.controller.admin.config.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yaoan.framework.common.pojo.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/23 10:12
 */
@Data
public class SystemConfigReqVO extends PageParam {

    private static final long serialVersionUID = 5027367905635792372L;

    /**
     * id
     */
    private Long id;

    /**
     * 标识
     */
    private String configKey;

    /**
     * 内容
     */
    private String configValue;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 启用状态（0=停用，1=启用）
     */
    private Boolean status;

}
