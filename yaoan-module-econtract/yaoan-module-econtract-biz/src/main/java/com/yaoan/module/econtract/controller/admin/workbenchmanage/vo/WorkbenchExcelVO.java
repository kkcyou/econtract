package com.yaoan.module.econtract.controller.admin.workbenchmanage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 工作台管理 Excel VO
 *
 * @author lls
 */
@Data
public class WorkbenchExcelVO {

    @ExcelProperty("id")
    private String id;

    @ExcelProperty("工作台编码")
    private String code;

    @ExcelProperty("工作台名称")
    private String name;

    @ExcelProperty("组件路径")
    private String component;

    @ExcelProperty("组件名称")
    private String componentName;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
