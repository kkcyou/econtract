package com.yaoan.module.system.controller.admin.auth.vo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.mybatis.core.type.JsonLongSetTypeHandler;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.module.system.enums.common.SexEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/30 18:01
 */

@TableName(value = "system_users", autoResultMap = true) // 由于 SQL Server 的 system_user 是关键字，所以使用 system_users
@KeySequence("system_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRespVO extends TenantBaseDO {

    /**
     * 用户ID
     */
    @TableId
    private Long id;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 公司编号
     */
    private Long companyId;
    /**
     * 加密后的密码
     *
     * 因为目前使用 {@link BCryptPasswordEncoder} 加密器，所以无需自己处理 salt 盐
     */
    private String password;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 备注
     */
    private String remark;
    /**
     * 部门 ID
     */
    private Long deptId;
    /**
     * 岗位编号数组
     */
    @TableField(typeHandler = JsonLongSetTypeHandler.class)
    private Set<Long> postIds;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 用户性别
     *
     * 枚举类 {@link SexEnum}
     */
    private Integer sex;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 帐号状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;

    @Schema(description = "首次登陆", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer firstLogin;
}
