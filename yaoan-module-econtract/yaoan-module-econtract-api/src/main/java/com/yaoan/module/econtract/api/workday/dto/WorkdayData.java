package com.yaoan.module.econtract.api.workday.dto;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/14 15:34
 */
@Data
public class WorkdayData {

    /**
     * 日期
     * 格式为”2023-05-01“
     */
    private String date;
    /**
     * 农历日期
     * 格式为”2023-05-01“
     */
    private String lunarDate;
    /**
     * 周内天数（1-7）
     * 周一为1，周日为7
     */
    private Integer weekDay;
    /**
     * 状态：
     * 1上班（含正常工作日及补班）
     * 2放假（含周末及节假日）	-
     */
    private Integer status;
    /**
     * 节日	国家法定节假日：
     * 元旦节, 除夕, 春节, 清明节, 劳动节, 端午节, 中秋节, 国庆节
     */
    private String festival;
    /**
     * 是否补班：
     * 1补班。仅当需要补班时有该字段。
     * 需要补班，真是难受的一天！
     */
    private Integer badDay;
    /**
     * 参数含义
     * 目前仅需要补班时有该字段，
     * 表示什么时候补班，例如劳动节前补班、国庆节后补班等
     */
    private String description;

}
