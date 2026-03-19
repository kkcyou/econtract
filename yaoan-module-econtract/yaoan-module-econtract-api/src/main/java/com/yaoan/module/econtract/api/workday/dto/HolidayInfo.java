package com.yaoan.module.econtract.api.workday.dto;

import lombok.Data;

import java.util.Map;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/18 10:34
 */
@Data
public class HolidayInfo {

    private Map<String, HolidayData> data;
}
