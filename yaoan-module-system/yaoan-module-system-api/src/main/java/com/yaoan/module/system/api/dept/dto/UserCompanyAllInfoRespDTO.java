package com.yaoan.module.system.api.dept.dto;

import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.enums.dept.MajorEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/1 16:06
 */
@Data
public class UserCompanyAllInfoRespDTO {


    /**
     * 单位ID
     */
    private Long id;

    /**
     * 单位名称
     */
    private String name;
    /**
     * 单位ID
     */
    private Long deptId;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 负责人
     * <p>
     * 关联 {@link AdminUserRespDTO#getId()}  }
     */
    private Long leaderUserId;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 单位状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 单位类型
     * <p>
     * 枚举 {@link MajorEnum}
     */
    private Integer major;

    /**
     * 信用代码
     */
    private String creditCode;

    /**
     * 是否是供应商
     */
    private Integer supplier;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
