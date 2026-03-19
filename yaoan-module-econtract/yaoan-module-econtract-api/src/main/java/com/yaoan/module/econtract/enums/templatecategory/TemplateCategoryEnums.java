package com.yaoan.module.econtract.enums.templatecategory;

import lombok.Getter;

/**
 * @description:
 * @author: doujl
 * @date: 2024/5/14 20:28
 */
@Getter
public enum TemplateCategoryEnums {

    TFW(1, "10000", "通用服务类","通用服务类", "服务类"),
    THW(2, "20000", "通用货物类","通用货物类", "货物类"),
    TGC(3, "30000", "通用工程类","通用工程类", "工程类"),
    KJH(2, "40001", "框架协议采购-货物类","通用货物类", "货物类"),
    KJF(1, "40002", "框架协议采购-服务类","通用服务类", "服务类"),
    XYH(2, "50001", "协议定点-货物类", "通用货物类","货物类"),
    XYF(1, "50002", "协议定点-服务类", "通用服务类","服务类"),
    XYG(3, "50003", "协议定点-工程类", "通用工程类","工程类"),
    DZH(2, "60001", "电子卖场-货物类","通用货物类", "货物类"),
    DZF(1, "60002", "电子卖场-服务类", "通用服务类","服务类"),
    FWF(1, "70001", "服务工程超市-服务类", "通用服务类","服务类"),
    DZJYH(2, "80001", "电子交易-货物类","通用货物类", "货物类"),
    DZJYF(1, "80002", "电子交易-服务类", "通用服务类","服务类"),
    ;

    private final Integer id;
    private final String code;
    private final String info;
    private final String commonInfo;
    private final String key;


    TemplateCategoryEnums(Integer id, String code, String info, String commonInfo, String key) {
        this.id = id;
        this.code = code;
        this.info = info;
        this.commonInfo = commonInfo;
        this.key = key;
    }

    public static TemplateCategoryEnums getInstanceByCode(String code) {
        for (TemplateCategoryEnums value : TemplateCategoryEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
