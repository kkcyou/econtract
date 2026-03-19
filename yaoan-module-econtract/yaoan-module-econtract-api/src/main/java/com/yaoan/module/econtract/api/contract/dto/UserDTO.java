package com.yaoan.module.econtract.api.contract.dto;

import com.yaoan.framework.common.enums.CommonStatusEnum;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 包状态修改 - 能否起草状态 hidden
 */
@Data
public class UserDTO {

    private Long id;
    private Long tenantId;

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
     * 平台用户ID
     */
    private String platformUserId;
    /**
     * 用户当前的业务类型 业务类型 1:监管用户 2:采购人 3:代理机构 4:供应商
     */
    private Integer type;
    /**
     * 供应商ID
     */
    private String supplyId;
    /**
     * 是否管理员1:管理员,0:普通用户
     */
    private Boolean isAdmin;
    /**
     * 区划编码
     */
    private String regionCode;
    /**
     * CA序列号
     */
    private String caUniCode;
    /**
     * 单位ID
     */
    private String orgId;
    /**
     * 代理机构ID
     */
    private String agentId;
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

    private Long companyId;
}
