package com.yaoan.module.econtract.enums.templatecategory;

import lombok.Getter;


@Getter
public enum PlatformTemplateCategoryEnums {
    //所属分类A:货物  B：工程 C：服务
    TFW( "10000", "通用服务类","ALL", "C","ALL"),
    THW("20000", "通用货物类","ALL", "A","ALL"),
    TGC("30000", "通用工程类","ALL", "B","ALL"),
    KJH("40001", "框架协议采购-货物类","gp-gpfa", "A","HLJ-gp-gpfa"),
    KJF("40002", "框架协议采购-服务类","gp-gpfa", "C","HLJ-gp-gpfa"),
    XYH( "50001", "协议定点-货物类", "gpmall-5.3","A","HLJ-gpmall-5.3"),
    XYF( "50002", "协议定点-服务类", "gpmall-5.3","C","HLJ-gpmall-5.3"),
    XYG( "50003", "协议定点-工程类", "gpmall-5.3","B","HLJ-gpmall-5.3"),
    DZH("60001", "电子卖场-货物类","JdMall", "A","HLJ-JdMall"),
    DZF("60002", "电子卖场-服务类", "JdMall","C","HLJ-JdMall"),
    FWF( "70001", "服务工程超市-服务类", "zhubajie","C","HLJ-zhubajie"),
    FWG( "70003", "服务工程超市-工程类", "zhubajie","B","HLJ-zhubajie"),
    FWH("70002", "服务工程超市-货物类", "zhubajie","A","HLJ-zhubajie"),
    DZJYH("80001", "电子交易-货物类","gpms-gpx-5.3", "A","HLJ-gpms-gpx-5.3"),
    DZJYF("80002", "电子交易-服务类", "gpms-gpx-5.3","C","HLJ-gpms-gpx-5.3"),
    DZJYG("80003", "电子交易-工程类", "gpms-gpx-5.3","B","HLJ-gpms-gpx-5.3"),

    /**
     * 模板名称匹配
     *
     * */
    TFW_0( "1", "服务","TEMP", "C","TEMP"),
    THW_0("2", "货物","TEMP", "A","TEMP"),
    TGC_0("3", "工程","TEMP", "B","TEMP"),
    ;


    private final String code;
    private final String info;
    private final String platform;
    private final String key;
    private final String clientId;


    PlatformTemplateCategoryEnums(String code, String info, String platform, String key, String clientId) {

        this.code = code;
        this.info = info;
        this.platform = platform;
        this.key = key;
        this.clientId = clientId;
    }

    public static PlatformTemplateCategoryEnums getInstanceByCode(String code) {
        for (PlatformTemplateCategoryEnums value : PlatformTemplateCategoryEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static String getInstanceByPlatform(String clientId,String key) {
        for (PlatformTemplateCategoryEnums value : PlatformTemplateCategoryEnums.values()) {
            if (value.getClientId().equals(clientId) && value.getKey().equals(key)) {
                return value.getCode();
            }
        }
        return null;
    }

    public static String getClientIdByPlatform(String platform) {
        for (PlatformTemplateCategoryEnums value : PlatformTemplateCategoryEnums.values()) {
            if (value.getPlatform().equals(platform)) {
                return value.getClientId();
            }
        }
        return null;
    }

    public static String getInfoByPlatform(String clientId,String key) {
        for (PlatformTemplateCategoryEnums value : PlatformTemplateCategoryEnums.values()) {
            if (value.getClientId().equals(clientId) && value.getKey().equals(key)) {
                return value.getInfo();
            }
        }
        return null;
    }
}
