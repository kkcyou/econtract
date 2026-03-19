package com.yaoan.module.system.dal.dataobject.user;

import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.mybatis.core.type.JsonLongSetTypeHandler;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.module.econtract.enums.saas.InviteMethodEnums;
import com.yaoan.module.system.enums.common.SexEnum;
import com.yaoan.module.system.enums.user.UserTypeEnums;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 管理后台的用户 DO
 *
 * @author 芋道源码
 */
@TableName(value = "system_users", autoResultMap = true) // 由于 SQL Server 的 system_user 是关键字，所以使用 system_users
@KeySequence("system_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDO extends TenantBaseDO {

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户账号
     */
    private String username;
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

    /**
     * 公司id
     */
    private Long  companyId;

    /**
     * 用户当前的业务类型 业务类型 0:系统管理员,1:采购单位,2:供应商,3:代理机构,4:采购监管机构,5:财政业务部门,6:评审专家,7:金融机构用户
     *{@link UserTypeEnums}
     */
    private Integer type;
    /**
     * 供应商ID
     */
    private String supplyId;
    /**
     * 单位ID
     */
    private String orgId;
    /**
     * 代理机构ID
     */
    private String agentId;
    /**
     * 区划编码
     */
    private String regionCode;

    /**
     * CA序列号
     */
    private String caUniCode;
    /**
     * 是否管理员1:管理员,0:普通用户
     */
    private Boolean isAdmin;
    /**
     * 平台用户id
     */
    private String platformUserId;

    private String appOpenId;

    /**
     * 实名情况
     * {@link com.yaoan.module.econtract.enums.saas.RealNameEnums}
     */
    private Integer realName;

    /**
     * 邀请方式
     * {@link InviteMethodEnums}
     * */
    private Integer inviteMethod;
}
