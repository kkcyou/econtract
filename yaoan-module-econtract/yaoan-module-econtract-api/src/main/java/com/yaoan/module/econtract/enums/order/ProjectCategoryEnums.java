package com.yaoan.module.econtract.enums.order;

import lombok.Getter;

@Getter
public enum ProjectCategoryEnums {
    /**
     * 项目所属类型 枚举类
     */
    GOODS("A", "货物",1),

    ENGINEER("B", "工程",2),

    SERVICE("C", "服务",3);

    private final String code;
    private final String info;
    private final Integer value;

    ProjectCategoryEnums(String code, String info, Integer value) {
        this.code = code;
        this.info = info;
        this.value = value;
    }

    public static ProjectCategoryEnums getInstance(String code) {
        for (ProjectCategoryEnums value : ProjectCategoryEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
