package com.yaoan.framework.security.core;

import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yaoan.framework.common.enums.UserTypeEnum;
import com.yaoan.module.system.enums.user.UserTypeEnums;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录用户信息
 *
 * @author 芋道源码
 */
@Data
public class LoginUser {

    /**
     * 用户编号
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;
    
    /**
     * 用户类型
     *
     * 关联 {@link UserTypeEnum}
     */
    private Integer userType;
    /**
     * 用户当前的业务类型 业务类型 0:系统管理员,1:采购单位,2:供应商,3:代理机构,4:采购监管机构,5:财政业务部门,6:评审专家,7:金融机构用户
     * {@link UserTypeEnums}
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
     * 是否管理员1:管理员,0:普通用户
     */
    private Boolean isAdmin;
    /**
     * 区划编码
     */
    private String regionCode;
    /**
     * 租户编号
     */
    private Long tenantId;
    /**
     * 租户套餐类型
     */
    private Integer tenantPackageType;
    /**
     * 授权范围
     */
    private List<String> scopes;

    /**
     * 公司编号
     */
    private Long companyId;

    /**
     * 手机号码
     */
    private String mobile;
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

    // ========== 上下文 ==========
    /**
     * 上下文字段，不进行持久化
     *
     * 1. 用于基于 LoginUser 维度的临时缓存
     */
    @JsonIgnore
    private Map<String, Object> context;

    public void setContext(String key, Object value) {
        if (context == null) {
            context = new HashMap<>();
        }
        context.put(key, value);
    }

    public <T> T getContext(String key, Class<T> type) {
        return MapUtil.get(context, key, type);
    }

}
