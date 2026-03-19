package com.yaoan.module.system.dal.dataobject.invitecode;

import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 邀请码管理 DO
 *
 * @author admin
 */
@TableName("system_invite_code")
@KeySequence("system_invite_code_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteCodeDO extends TenantBaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 邀请码
     */
    private String code;
    /**
     * 有效天数。-1为永久有效
     */
    private Integer validDays;
    /**
     * 有效截至日期
     */
    private LocalDateTime validEndDate;
    /**
     * 可用次数（-1不限次数）
     */
    private Integer validTimes;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 是否启用
     */
    private Integer status;
    /**
     * 使用的用户id, -1所有人可用
     */
    private Long userId;
    /**
     * 备注
     */
    private String remark;

}
