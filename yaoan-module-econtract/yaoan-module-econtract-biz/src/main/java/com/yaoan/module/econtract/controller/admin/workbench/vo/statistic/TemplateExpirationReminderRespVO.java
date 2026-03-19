package com.yaoan.module.econtract.controller.admin.workbench.vo.statistic;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @description: 模板到期提醒
 * @author: Pele
 * @date: 2023/11/8 21:22
 */
@Data
public class TemplateExpirationReminderRespVO {

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板编号
     */
    private String code;
    /**
     * 模板分类名称
     */
    private String categoryName;
    /**
     * 到期时间
     */
    private LocalDateTime expirationTime;


}
