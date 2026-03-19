package com.yaoan.module.econtract.enums.supervise;

import lombok.Getter;

/**
 * @author ZHC
 * 供应商特殊性质
 */

@Getter
public enum SupplierFeaturesEnum {

    PRISON_ENTERPRISE(1, "监狱企业","gystsx001"),
    WELFARE_UNIT(2, "残疾人福利性单位","gystsx003"),
    OTHER(9, "其他","gystsx002");

    private final Integer code;
    private final String info;
    private final String code_str;

    SupplierFeaturesEnum(Integer code, String info,String code_str) {
        this.code = code;
        this.info = info;
        this.code_str = code_str;
    }

    public static SupplierFeaturesEnum getInstance(Integer code) {
        for (SupplierFeaturesEnum value : SupplierFeaturesEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    public static SupplierFeaturesEnum getInstanceByCode_str(String code_str) {
        for (SupplierFeaturesEnum value : SupplierFeaturesEnum.values()) {
            if (value.getCode_str().equals(code_str)) {
                return value;
            }
        }
        return null;
    }
}
