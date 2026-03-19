package com.yaoan.module.system.api.dept.dto;

import com.yaoan.framework.common.enums.CommonStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 单位 Response DTO
 *
 * @author doujiale
 */
@Data
public class CompanyRespDTO {

    /**
     * 单位编号
     */
    private Long id;
    /**
     * 单位名称
     */
    private String name;
    /**
     * 信用代码
     */
    private String creditCode;
    /**
     * 单位编号
     */
//    private Long deptId;
    /**
     * 负责人的用户编号
     */
    private Long leaderUserId;
    /**
     * 单位状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 单位类型
     * <p>
     * 枚举 {@link com.yaoan.module.system.enums.dept.MajorEnum}
     */
    private Integer major;

    /**
     * 是否是供应商
     */
    private Integer supplier;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 相对方id
     */
    private String relativeId;
}
