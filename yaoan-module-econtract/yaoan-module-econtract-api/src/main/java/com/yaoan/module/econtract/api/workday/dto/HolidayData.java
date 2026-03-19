package com.yaoan.module.econtract.api.workday.dto;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/17 12:19
 */
@Data
public class HolidayData {
    /**
     * true表示是节假日，
     * false表示是调休
     */
    private Boolean holiday;
    /**
     * 节假日的中文名。如果是调休，则是调休的中文名，例如'国庆前调休'
     */
    private String name;
    /**
     * 薪资倍数，1表示是1倍工资
     */
    private Integer wage;
    /**
     * 日期
     */
    private String date;
    /**
     * 表示当前时间距离目标还有多少天。比如今天是 2018-09-28，距离 2018-10-01 还有3天
     */
    private Integer rest;

    /**
     * 只在调休下有该字段。
     * true表示放完假后调休，
     * false表示先调休再放假
     */
    private Boolean after;
    /**
     * 只在调休下有该字段。表示调休的节假日
     */
    private String target;
}
