package com.yaoan.module.econtract.controller.admin.warningnoticecfg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 预警通知配置表（new预警） Excel VO
 *
 * @author admin
 */
@Data
public class WarningNoticeCfgExcelVO {

    @ExcelProperty("主键")
    private String id;

    @ExcelProperty("预警规则id")
    private String ruleId;

    @ExcelProperty("通知对象选取来源（1.选择用户，2.根据工作流）")
    private Integer userType;

    @ExcelProperty("用户角色id")
    private Integer userRole;

    @ExcelProperty("用户ids(用逗号分割)")
    private String userIds;

    @ExcelProperty("通知方式（1.站内信，2邮件，3短信）")
    private Integer noticeWay;

    @ExcelProperty("通知模板")
    private String contentTemplate;

    @ExcelProperty("通知次数")
    private Integer noticeTimes;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
