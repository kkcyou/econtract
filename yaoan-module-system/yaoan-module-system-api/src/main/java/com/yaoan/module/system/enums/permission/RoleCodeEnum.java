package com.yaoan.module.system.enums.permission;

import com.yaoan.framework.common.util.object.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色标识枚举
 */
@Getter
@AllArgsConstructor
public enum RoleCodeEnum {

    SUPER_ADMIN("super_admin", "超级管理员"),
    TENANT_ADMIN("tenant_admin", "租户管理员"),
    COMPANY_ADMIN("company_admin", "公司管理员"),
    CONTRACT_SIGNER("contract_signer", "合同签章人"),
    COMMON_OPERATOR("common_operator", "普通经办人"),
    SUPPLY_OPERATOR("common_supply", "供应商"),

    APP_HANDLER("app_handler", "APP经办人"),
    APP_ADMIN("app_admin", "APP管理员"),
    SAAS_ADMIN("saas_admin","saas管理员"),
    SAAS_HANDLER("saas_handler","saas经办人"),

    ;

    /**
     * 角色编码
     */
    private final String code;
    /**
     * 名字
     */
    private final String name;

    public static boolean isSuperAdmin(String code) {
        return ObjectUtils.equalsAny(code, SUPER_ADMIN.getCode());
    }

}
