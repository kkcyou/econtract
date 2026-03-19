package com.yaoan.module.system.api.dept.dto;

import com.yaoan.framework.common.enums.CommonStatusEnum;
import lombok.Data;

/**
 * 岗位表
 *
 * @author ruoyi
 */

@Data
public class PostDTO {

    /**
     * 岗位序号
     */
    private Long id;
    /**
     * 岗位名称
     */
    private String name;
    /**
     * 岗位编码
     */
    private String code;
    /**
     * 岗位排序
     */
    private Integer sort;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}
