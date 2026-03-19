package com.yaoan.module.system.api.permission.dto;

import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.module.system.enums.permission.RoleTypeEnum;
import lombok.Data;

import java.util.Set;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/25 16:22
 */
@Data
public class RoleRespDTO {

    /**
     * 角色ID
     */
    private Long id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色标识
     *
     * 枚举
     */
    private String code;
    /**
     * 角色排序
     */
    private Integer sort;
    /**
     * 角色状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 角色类型
     *
     * 枚举 {@link RoleTypeEnum}
     */
    private Integer type;
    /**
     * 备注
     */
    private String remark;

    private Set<Long> dataScopeDeptIds;
}
