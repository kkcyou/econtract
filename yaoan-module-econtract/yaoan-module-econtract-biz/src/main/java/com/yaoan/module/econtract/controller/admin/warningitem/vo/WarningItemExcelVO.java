package com.yaoan.module.econtract.controller.admin.warningitem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 预警事项表（new预警） Excel VO
 *
 * @author admin
 */
@Data
public class WarningItemExcelVO {

    @ExcelProperty("主键")
    private String id;

    @ExcelProperty("检查点id")
    private String configId;

    @ExcelProperty("预警事项名称")
    private String itemName;

    @ExcelProperty("风险说明")
    private String itemRemark;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
