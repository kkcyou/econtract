package com.yaoan.module.econtract.enums.changxie;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/25 16:05
 */
@Getter
public enum FileTypeEnums {

    /**
     * 文件格式类型 的枚举
     */
    DOCX(".docx", "docx类型"),
    DOC(".doc", "docx类型"),
    PDF(".pdf", "pdf类型"),



    ;

    private final String code;
    private final String info;

    FileTypeEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static FileTypeEnums getInstance(String code) {
        for (FileTypeEnums value : FileTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
