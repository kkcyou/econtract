package com.yaoan.module.system.controller.admin.invitecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 邀请码管理 Excel VO
 *
 * @author admin
 */
@Data
public class InviteCodeExcelVO {

    @ExcelProperty("主键")
    private Integer id;

    @ExcelProperty("邀请码")
    private String code;

    @ExcelProperty("有效天数")
    private Integer validDays;

    @ExcelProperty("有效截至日期")
    private LocalDateTime validEndDate;

    @ExcelProperty("可用次数（-1不限次数）")
    private Integer validTimes;

    @ExcelProperty("类型")
    private Integer type;

    @ExcelProperty("是否启用")
    private Integer status;

    @ExcelProperty("使用的用户id")
    private Long userId;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
