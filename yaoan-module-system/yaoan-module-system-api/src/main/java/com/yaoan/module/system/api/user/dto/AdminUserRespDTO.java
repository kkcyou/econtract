package com.yaoan.module.system.api.user.dto;

import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.module.system.enums.user.UserTypeEnums;
import lombok.Data;

import java.util.Set;

/**
 * Admin 用户 Response DTO
 *
 * @author 芋道源码
 */
@Data
public class AdminUserRespDTO {

    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 帐号状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
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
     * 部门ID
     */
    private Long deptId;
    /**
     * 岗位编号数组
     */
    private Set<Long> postIds;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色id
     */
    private String roleName;
    /**
     *公司id
     */
    private Long companyId;

    private String platformUserId;

    private Long tenantId;

    /**
     * 是否管理员1:管理员,0:普通用户
     */
    private Boolean isAdmin;
    /**
     * 区划编码
     */
    private String regionCode;

    private String regionGuid;
    /**
     * 身份证
     */
    private String idCard;

    /**
     * 邀请方式
     */
    private Integer inviteMethod;
    /**
     * 实名情况
     * {@link com.yaoan.module.econtract.enums.saas.RealNameEnums}
     */
    private Integer realName;
}
