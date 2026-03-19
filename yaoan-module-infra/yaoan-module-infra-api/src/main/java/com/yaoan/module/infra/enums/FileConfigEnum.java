package com.yaoan.module.infra.enums;

import lombok.Getter;

/**
 * @author doujiale
 */

@Getter
public enum FileConfigEnum {
    /**
     * 文件上传路径枚举
     */
    CONTRACT_DRAFT("本地磁盘", 5L),
    CONTRACT_SIGNING("MinIO", 18L)
    ;


    private final String name;
    private final Long id;

    FileConfigEnum(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public static FileConfigEnum getInstance(Long id) {
        for (FileConfigEnum value : FileConfigEnum.values()) {
            if (value.getId().equals(id)) {
                return value;
            }
        }
        return null;
    }
}
