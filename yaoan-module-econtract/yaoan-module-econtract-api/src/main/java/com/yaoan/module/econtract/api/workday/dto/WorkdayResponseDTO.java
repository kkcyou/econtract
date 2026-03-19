package com.yaoan.module.econtract.api.workday.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/14 15:23
 */
@Data
public class WorkdayResponseDTO {

    private Integer code;

    private List<Map<String, HolidayData>> holiday;

}
