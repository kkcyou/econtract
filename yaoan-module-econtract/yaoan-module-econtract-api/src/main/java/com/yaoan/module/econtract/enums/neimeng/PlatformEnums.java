package com.yaoan.module.econtract.enums.neimeng;

import lombok.Getter;

/**
 * @description:
 * @author: doujl
 * @date: 2023/11/8 18:22
 */
@Getter
public enum PlatformEnums {
    ALL("all", "all", "通用"),
    GPMS_GPX("gpms-gpx", "gpms-gpx-5.3", "电子交易"),
    GPMALL("gpmall", "gpmall-5.3", "协议定点"),
    HUICAIMALL("HuiCaiMall", "HuiCaiMall", "徽采商城"),
    WANCAIMALL("WanCaiMall", "WanCaiMall", "皖采商城"),
    JDMALL("JdMall", "JdMall", "电子卖场"),
    EPOINT("Epoint", "Epoint", "国泰新点"),
    ZCD("zcd", "zcd-3.0", "政府采购金融服务"),
    GPMALL_SAAS("gpmall-saas", "gpmall-saas", "公采云saas卖场"),
    GPC_GPCMS("gpc-gpcms", "gpc-gpcms", "诚信"),
    ZHUBAJIE("zhubajie", "zhubajie", "服务工程超市"),
    GP_GPFA("gp-gpfa", "gp-gpfa", "框架协议采购"),
    SPEED_PSMS("speed-psms", "speed-psms", "思必得内控"),
    PROCESSLESS("ecms", "ecms", "无过程采购"),
    JIANGUAN("jianguan", "jianguan", "监管"),

    ;

    private final String guid;
    private final String code;
    private final String info;

    PlatformEnums(String guid, String code, String info) {
        this.guid = guid;
        this.code = code;
        this.info = info;
    }

    public static PlatformEnums getInstance(String code) {
        for (PlatformEnums value : PlatformEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
