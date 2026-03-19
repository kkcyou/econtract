package com.yaoan.module.econtract.enums.supervise;

import lombok.Getter;

/**
 * @author ZHC
 * 供应商规模
 * 制造商规模
 */

@Getter
public enum SupplierSizeEnum {

    MAJOR_INDUSTRY(1, "大型企业","gysgm004"),
    MEDIUM_ENTERPRISE(2, "中型企业","gysgm003"),
    SMALL_ENTERPRISE(3, "小型企业","gysgm002"),
    MICRO_ENTERPRISE(4, "微型企业","gysgm002"),
    OTHER(9, "其他","gysgm099");

    private final Integer code;
    private final String info;
    private final String code_str;

    SupplierSizeEnum(Integer code, String info,String code_str) {
        this.code = code;
        this.info = info;
        this.code_str = code_str;
    }

    public static SupplierSizeEnum getInstance(Integer code) {
        for (SupplierSizeEnum value : SupplierSizeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    public static SupplierSizeEnum getInstanceByCode_str(String code_str) {
        for (SupplierSizeEnum value : SupplierSizeEnum.values()) {
            if (value.getCode_str().equals(code_str)) {
                return value;
            }
        }
        return null;
    }
    public static SupplierSizeEnum getInstanceByName(String info) {
        for (SupplierSizeEnum value : SupplierSizeEnum.values()) {
            if (value.getInfo().equals(info)) {
                return value;
            }
        }
        return null;
    }



}
