package com.yaoan.module.econtract.enums.supervise;

import lombok.Getter;

/**
 * @description:采购分类枚举
 * @author: zhc
 * @date: 2024-03-18
 */
@Getter
public enum PurCatalogTypeEnums {
    //A:货物  B：工程 C：服务
    GOODS("1", "货物","A"),

    ENGINEER("3", "工程","B"),

    SERVICE("2", "服务","C");

    private final String code;
    private final String info;
    private final String key;

    PurCatalogTypeEnums(String code, String info,String key) {
        this.code = code;
        this.info = info;
        this.key = key;
    }

    public static PurCatalogTypeEnums getInstance(String code) {
        for (PurCatalogTypeEnums value : PurCatalogTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    public static String getKeyByCode(String key) {
        for (PurCatalogTypeEnums value : PurCatalogTypeEnums.values()) {
            if (value.getKey().equals(key)) {
                return value.getCode();
            }
        }
        return null;
    }
}
