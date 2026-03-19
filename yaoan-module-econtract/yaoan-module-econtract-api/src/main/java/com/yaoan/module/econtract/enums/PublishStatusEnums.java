package com.yaoan.module.econtract.enums;

import lombok.Getter;

@Getter
public enum PublishStatusEnums {

    /**
     * 0-未发布 1-已发布
     */
    UN_PUBLISH ("0", "未发布"),
    PUBLISH ("1", "已发布");


    private final String code;
    private final String info;

    PublishStatusEnums(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static PublishStatusEnums getInstance(String code) {
        for (PublishStatusEnums value : PublishStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
