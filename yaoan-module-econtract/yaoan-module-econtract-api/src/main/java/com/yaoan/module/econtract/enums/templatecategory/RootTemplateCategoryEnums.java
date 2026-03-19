package com.yaoan.module.econtract.enums.templatecategory;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/14 20:28
 */
@Getter
public enum RootTemplateCategoryEnums {
    /**
     * 模板相关 2001000 ~ 2001099//所属分类A:货物  B：工程 C：服务
     */
    SERVICE (1,"10000", "通用服务类","服务类","C"),
    GOODS (2,"20000", "通用货物类","货物类","A"),
    ENGINEER (3,"30000", "通用工程类","工程类","B"),


    ;

    private final Integer id;
    private final String code;
    private final String info;
    private final String key;
    private final String type;


    RootTemplateCategoryEnums(Integer id,String code, String info,String key,String type)
    {
        this.id = id;
        this.code = code;
        this.info = info;
        this.key = key;
        this.type = type;
    }

    public static RootTemplateCategoryEnums getInstanceByKey(String key) {
        for (RootTemplateCategoryEnums value : RootTemplateCategoryEnums.values()) {
            if (value.getKey().equals(key)) {
                return value;
            }
        }
        return null;
    }
    public static RootTemplateCategoryEnums getInstanceByCode(String code) {
        for (RootTemplateCategoryEnums value : RootTemplateCategoryEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    public static String getNameByType(String type) {
        for (RootTemplateCategoryEnums value : RootTemplateCategoryEnums.values()) {
            if (value.getType().equals(type)) {
                return value.getKey();
            }
        }
        return null;
    }
}
