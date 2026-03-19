package com.yaoan.module.econtract.enums.watermark;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2025-9-8 11:24
 */
@Getter
public enum WatermarkTypeEnums {


    /**
     * 模板相关 2001000 ~ 2001099
     */
    DIY(0, "自定义文字水印"),
    BUSINESS(0, "业务字段");


    private final Integer code;
    private final String info;

    WatermarkTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static WatermarkTypeEnums getInstance(Integer code) {
        for (WatermarkTypeEnums value : WatermarkTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
