package com.yaoan.module.econtract.enums.order;

import lombok.Getter;

@Getter
public enum SortEnums {

    /**
     * 排序 枚举类
     */
    ASCENDING_SORT("asc", "升序"),

    DESCENDING_SORT("des", "降序");

    private final String code;
    private final String info;

    SortEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static SortEnums getInstance(String code) {
        for (SortEnums value : SortEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
