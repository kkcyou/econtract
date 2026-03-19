package com.yaoan.module.system.enums.user;

import cn.hutool.core.util.ArrayUtil;
import com.yaoan.module.system.enums.social.SocialTypeEnum;
import com.yaoan.module.system.enums.tenant.TenantPackageTypeEnum;
import lombok.Getter;

import java.util.Arrays;

/**
 * @description: 0:系统管理员,1:采购单位,2:供应商,3:代理机构,4:采购监管机构,5:财政业务部门,6:评审专家,7:金融机构用户
 * @author: Pele
 * @date: 2023/12/20 14:02
 */
@Getter
public enum UserTypeEnums {
    /**
     * 用户类型 枚举类
     */
    SYSTEM_ADMIN(0, "系统管理员"),
    PURCHASER_ORGANIZATION(1, "采购单位"),
    SUPPLIER(2, "供应商"),
    PROXY_ORGANIZATION(3, "代理机构"),
    PROCUREMENT_REGULATORY_AGENCY(4, "采购监管机构"),
    FINANCIAL_BUSINESS_DEPARTMENT(5, "财政业务部门"),
    EVALUATION_EXPERT(6, "评审专家"),
    FINANCIAL_INSTITUTION_USER(7, "金融机构用户"),
    ;

    private final Integer code;
    private final String info;

    UserTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static UserTypeEnums getInstance(Integer code) {
        for (UserTypeEnums value : UserTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
