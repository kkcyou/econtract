package com.yaoan.module.econtract.controller.admin.warningmodel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 预警模块来源（new预警） Excel VO
 *
 * @author admin
 */
@Data
public class WarningModelExcelVO {

    @ExcelProperty("主键")
    private String id;

    @ExcelProperty("编码")
    private String code;

    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("父级节点id")
    private String parentId;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
