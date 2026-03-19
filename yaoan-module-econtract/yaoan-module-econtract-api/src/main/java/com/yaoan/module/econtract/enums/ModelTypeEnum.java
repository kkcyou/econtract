package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @author doujiale
 */

@Getter
public enum ModelTypeEnum {

    TERM_ADD ("1", "条款新增"),
    FILE_UPLOAD ("2", "文件上传"),
    TEMPLATE ("3", "范本新增"),
    WPS_UPLOAD ("4", "WPS新增"),
    TEMPLATE_UPLOAD ("5", "范本(上传)新增");
    private final String code;
    private final String info;

    ModelTypeEnum(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static ModelTypeEnum getInstance(String code) {
        for (ModelTypeEnum value : ModelTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
