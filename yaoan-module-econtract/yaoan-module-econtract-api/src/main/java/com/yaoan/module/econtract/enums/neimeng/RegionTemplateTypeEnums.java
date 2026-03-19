package com.yaoan.module.econtract.enums.neimeng;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tdb
 * @version 1.0
 * @date 2021-4-10 11:41
 */
@AllArgsConstructor
@Getter
public enum RegionTemplateTypeEnums {

    REGIONTEMPLATE_DIRECT(0, "直接订购合同模版"),
    REGIONTEMPLATE_DIRECT_JBG(12, "警备馆直接订购合同模版"),
    REGIONTEMPLATE_BIDDING(1, "竞价合同模版"),
    REGIONTEMPLATE_BIDDING_JBG(1217, "警备馆竞价合同模版"),
    REGIONTEMPLATE_BARGAIN(2, "议价合同模版"),
    REGIONTEMPLATE_PROVIDE(3, "定点服务合同模版"),
    REGIONTEMPLATE_ENQUIRY(4, "询价合同模版"),
    REGIONTEMPLATE_BATCH(5, "批量直购合同模版"),
    REGIONTEMPLATE_ACCEPTANCE(100, "验收单模版"),
    REGIONTEMPLATE_BIDDINGSTART(201, "网上竞价采购公告模版"),
    REGIONTEMPLATE_BIDDINGEND(202, "网上竞价结果公告模版"),
    REGIONTEMPLATE_BIDDINGSCRAP(203, "网上竞价废标公告"),
    REGIONTEMPLATE_PROVIDESTART(211, "定点服务采购公告模版(竞价)"),
    REGIONTEMPLATE_PROVIDEEND(212, "定点服务结果公告模版(竞价)"),
    REGIONTEMPLATE_PROVIDESCRAP(213, "定点服务废标公告模版(竞价)"),
    REGIONTEMPLATE_PROVIDEEND_DIRECT(214, "定点服务结果公告模版(直购)"),
    REGIONTEMPLATE_ENQUIRYSTART(221, "网上询价采购公告模版"),
    REGIONTEMPLATE_ENQUIRYEND(222, "网上询价结果公告模版"),
    REGIONTEMPLATE_ENQUIRYSCRAP(223, "网上询价废标公告"),
    REGIONTEMPLATE_DIRECT_NOTICE(301, "直接订购成交公示(协议采购)"),
    REGIONTEMPLATE_EPIDEMIC_NOTICE(900, "防疫物资采购公告模板"),
    REGIONTEMPLATE_END_BARGAIN(666, "议价成交公示"),
    REGIONTEMPLATE_END_BIDBARGAIN(662, "竞价成交公示"),
    REGIONTEMPLATE_SHOW_BIDBARGAIN(660, "议价公示公告"),
    //public static final Integer REGIONTEMPLATE_SCRAP_BIDBARGAIN = 664;
    REGIONTEMPLATE_NOTICE_FURNITURE(240, "家具用具采购公告"),
    REGIONTEMPLATE_NOTICE_END_FURNITURE(241, "家具用具结果公告"),
    REGIONTEMPLATE_CONTRACT_FURNITURE(8, "家具用具合同"),
    REGIONTEMPLATE_CONTRACT_CAR(9, "汽车馆合同"),
    REGIONTEMPLATE_NOTICE_AGAIN_FURNITURE(242, "家具用具废标公告"),
    REGIONTEMPLATE_PROVIDE_BARGAINING(230, "定点服务采购公告模版(议价)"),
    REGIONTEMPLATE_PROVIDE_DIRECTSTART(231, "定点服务采购公告模版(直购)"),
    REGIONTEMPLATE_PROVIDE_BARGAINEND(232, "定点服务结果公告模版(议价)"),
    REGIONTEMPLATE_PROVIDE_BARGAINFILD(233, "定点服务失败模版(议价)"),
    REGIONTEMPLATE_PROVIDE_BIDDINGCHANGE(234, "定点服务变更公告模版(竞价)"),
    REGIONTEMPLATE_PROVIDE_BARGAINCHANGE(235, "定点服务变更公告模版(议价)"),
    REGIONTEMPLATE_PROVIDE_BARGAIN_CONTRACT(236, "定点服务合同模版(议价)"),
    REGIONTEMPLATE_PROVIDE_DIRECT_CONTRACT(237, "定点服务合同模版(直购)"),
    REGIONTEMPLATE_NOTICE_PROJECTMULTIBRAND_TERMINATION(262, "多品牌竞价终止公告模版"),
    REGIONTEMPLATE_PROJECTMULTIBRAND_TRANSACTION_CONFIRMATION(264, "多品牌竞价成交确认书模版"),
    REGIONTEMPLATE_PROJECTMULTIBRAND_CONTRACT_PUBLICITY(272, "多品牌竞价合同公示模版"),
    REGIONTEMPLATE_NOTICE_BATCH_END(302, "批量直购结果公告"),
    REGIONTEMPLATE_NOTICE_BACKHANDSTART(620, "电子反拍采购公告"),
    REGIONTEMPLATE_CONTRACT_BACKHAND(621, "电子反拍合同模版"),
    REGIONTEMPLATE_CONTRACT_BACKHAND_JBG(1216, "警备馆电子反拍合同模版"),
    REGIONTEMPLATE_CONTRACT_BACKHAND_PUBLICITY(623, "电子反拍合同公示公告"),
    REGIONTEMPLATE_CONTRACT_BACKHAND_TERMINATION(624, "电子反拍终止公告"),
    REGIONTEMPLATE_CONTRACT_BACKHAND_END(626, "电子反拍结果公告"),
    REGIONTEMPLATE_CONTRACT_BACKHAND_END_JBG(1218, "警备馆直购结果公告"),
    REGIONTEMPLATE_CONTRACT_BACKHAND_CHANGE(628, "电子反拍更正公告"),
    REGIONTEMPLATE_ORDER_RETURN_UPDATE_NOTICE(629, "直购更正公告"),
    REGIONTEMPLATE_ORDER_RETURN_END_NOTICE(630, "直购终止公告"),
    REGIONTEMPLATE_DOWNLOAD_PLAN(1500, "陕西下载计划单模板"),
    REGIONTEMPLATE_VOLUME_PURCHASE(1410, "带量专区直购采购合同模板"),
    REGIONTEMPLATE_VOLUME_PURCHASE_NOTICE(1401, "带量专区直购采购成交公告模板"),

    REGIONTEMPLATE_PROVIDE_CONTRACT_ISS(236, "中介服务默认合同模版"),
    /**
     * 1018 工会直接订购结果公告模板
     */
    UNION_DIRECT_CJ(1018, "工会直接订购结果公告模板"),
    /**
     * 1019 工会竞价采购公告模板
     */
    UNION_BIDDING_CG(1019, "工会竞价采购公告模板"),
    /**
     * 1023 工会竞价废标公告模板
     */
    UNION_BIDDING_FB(1023, "工会竞价废标公告模板"),
    /**
     * 1022 工会竞价结果公告模板
     */
    NOTICE_END_UNION(1022, "工会竞价结果公告模板"),
    /**
     * 1010 工会直购合同模板
     */
    UNION_DIRECT_ZS(1010, "工会直购合同模板"),
    /**
     * 1017 工会竞价合同模板
     */
    UNION_BIDDING_ZS(1017, "工会竞价合同模板"),
    /**
     * 1032 工会定点竞价合同模板
     */
    UNION_DDFW_YJ(1032, "工会定点直购合同模板"),

    /**
     * 1033 工会定点议价竞价合同模板
     */
    UNION_DDFW_JJ(1033, "工会定点竞价合同模版"),


    /**
     * 1050 工会家具竞价合同模板
     */
    UNION_FURNITURE_BIDDING_ZS(1050, "工会家具竞价合同模板"),

    /**
     * 1051 工会家具直购合同模板
     */
    UNION_FURNITURE_DIRECT_ZS(1051, "工会家具直购合同模板"),

    /**
     * 1052 工会汽车竞价合同模板
     */
    UNION_CAR_BIDDING_ZS(1052, "工会汽车竞价合同模板"),

    /**
     * 1053 工会汽车直购合同模板
     */
    UNION_CAR_DIRECT_ZS(1053, "工会汽车直购合同模板"),

    /**
     * 1054 工会灯具竞价合同模板
     */
    UNION_LAMPS_BIDDING_ZS(1054, "工会灯具竞价合同模板"),

    /**
     * 1055 工会灯具直购合同模板
     */
    UNION_LAMPS_DIRECT_ZS(1055, "工会灯具直购合同模板"),

    /**
     * 1056 工会乡村振新竞价合同模板
     */
    UNION_POVERTY_ALLEVIATION_BIDDING_ZS(1056, "工会乡村振新竞价合同模板"),

    /**
     * 1057 工会乡村振新直购合同模板
     */
    UNION_POVERTY_ALLEVIATION_DIRECT_ZS(1057, "工会乡村振新直购合同模板"),

    /**
     * 1118 灯具馆直接订购结果公告模板
     */
    LAMPS_DIRECT_CJ(1118, "灯具馆直接订购结果公告模板"),
    /**
     * 1119 灯具馆竞价采购公告模板
     */
    LAMPS_BIDDING_CG(1119, "灯具馆竞价采购公告模板"),
    /**
     * 1123 灯具馆竞价废标公告模板
     */
    LAMPS_BIDDING_FB(1123, "灯具馆竞价废标公告模板"),
    /**
     * 1122 灯具馆竞价结果公告模板
     */
    NOTICE_END_LAMPS(1122, "灯具馆竞价结果公告模板"),
    /**
     * 1110 灯具馆直购合同模板
     */
    LAMPS_DIRECT_ZS(1110, "灯具馆直购合同模板"),
    /**
     * 1117 灯具馆竞价合同模板
     */
    LAMPS_BIDDING_ZS(1117, "灯具馆竞价合同模板"),

    /**
     * 218 乡村振新馆直接订购结果公告模板
     */
    POVERTY_ALLEVIATION_DIRECT_CJ(218, "乡村振新馆直接订购结果公告模板"),

    /**
     * 210 乡村振新馆直购合同模板
     */
    POVERTY_ALLEVIATION_DIRECT_ZS(210, "乡村振新馆直购合同模板"),

    /**
     * 2118 汕尾馆直接订购结果公告模板
     */
    SS_UNITED_DIRECT_CJ(2118, "汕尾馆直接订购结果公告模板"),

    /**
     * 2110 汕尾馆直购合同模板
     */
    SS_UNITED_DIRECT_ZS(2110, "汕尾馆直购合同模板"),

    /**
     * 2048 汕尾馆直购合同模板
     */
    DELIVER_TEMPLATE(2048, "送货单模板"),

    /**
     * 405 定点反拍合同模板
     */
    PROVIDE_REVERSE_CONTRACT_TEMPLATE(405, "定点反拍合同模板"),

    /**
     * 2050 终止合同-申请函模板
     */
    TERMINATE_SPONSOR(2050, "终止合同-申请函模板"),
    /**
     * 2052 终止合同-确认函模板
     */
    TERMINATE_RECEIVER(2052, "终止合同-确认函模板"),
    /**
     * 4018 制造业展馆直接订购结果公告模板
     */
    HIGH_QUALITY_DIRECT_CJ(4018, "制造业展馆直接订购结果公告模板"),
    /**
     * 4019 中山馆直接订购结果公告模板
     */
    HIGH_QUALITY_ZS_DIRECT_CJ(4019, "中山馆直接订购结果公告模板"),
    /**
     * 4020 东莞馆直接订购结果公告模板
     */
    HIGH_QUALITY_DW_DIRECT_CJ(4020, "东莞馆直接订购结果公告模板"),
    /**
     * 4021 珠海馆直接订购结果公告模板
     */
    HIGH_QUALITY_ZH_DIRECT_CJ(4021, "珠海馆直接订购结果公告模板"),
    /**
     * 4022 惠州馆直接订购结果公告模板
     */
    HIGH_QUALITY_HZ_DIRECT_CJ(4022, "惠州馆直接订购结果公告模板"),
    /**
     * 4023 江门馆直接订购结果公告模板
     */
    HIGH_QUALITY_JM_DIRECT_CJ(4023, "江门馆直接订购结果公告模板"),

    /**
     * 4025 制造业展馆直接订购合同模板
     */
    HIGH_QUALITY_DIRECT_ZS(4025, "制造业展馆直接订购合同模板"),
    /**
     * 4026 中山馆直接订购合同模板
     */
    HIGH_QUALITY_ZS_DIRECT_ZS(4026, "中山馆直接订购合同模板"),
    /**
     * 4027 东莞馆直接订购合同模板
     */
    HIGH_QUALITY_DW_DIRECT_ZS(4027, "东莞馆直接订购合同模板"),
    /**
     * 4028 珠海馆直接订购合同模板
     */
    HIGH_QUALITY_ZH_DIRECT_ZS(4028, "珠海馆直接订购合同模板"),
    /**
     * 4029 惠州馆直接订购合同模板
     */
    HIGH_QUALITY_HZ_DIRECT_ZS(4029, "惠州馆直接订购合同模板"),
    /**
     * 4030 江门馆直接订购合同模板
     */
    HIGH_QUALITY_JM_DIRECT_ZS(4030, "江门馆直接订购合同模板"),

    /**
     * 501 多商品竞价采购公告模板
     */
    MULTIPLE_GOODS_CG(501, "多商品竞价采购公告模板"),

    /**
     * 502 多商品竞价采结果公告模板
     */
    MULTIPLE_GOODS_JG(502, "多商品竞价采结果公告模板"),

    /**
     * 503 多商品竞价采废标公告模板
     */
    MULTIPLE_GOODS_FB(503, "多商品竞价采废标公告模板"),

    /**
     * 504 多商品竞价合同模板
     */
    MULTIPLE_GOODS_HT(504, "多商品竞价合同模板"),

    /**
     * 3111 绿色建材馆无计划采购结果公告模板
     */
    GREEN_BUILDING_JG(3111, "绿色建材馆无计划采购结果公告模板"),

    GREEN_BUILDING_HT(3112, "绿色建材馆直购合同模板"),

    XZLZG_JG(2011, "西藏林芝馆直接订购结果公告模板"),

    XZLZG_HT(2012, "西藏林芝馆直接订购合同模板");

    final Integer code;
    final String value;

    public static String getValue(Integer templateType) {
        RegionTemplateTypeEnums[] projectStatusEnums = values();
        for (RegionTemplateTypeEnums regionTemplateTypeEnum1 : projectStatusEnums) {
            if (templateType.equals(regionTemplateTypeEnum1.getCode())) {
                return regionTemplateTypeEnum1.getValue();
            }
        }
        return null;
    }

    public static RegionTemplateTypeEnums getInstanceByCode(Integer code) {
        for (RegionTemplateTypeEnums value : RegionTemplateTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
