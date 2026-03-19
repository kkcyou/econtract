package com.yaoan.module.econtract.controller.admin.warningcfg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 预警检查配置表(new预警) Excel VO
 *
 * @author admin
 */
@Data
public class WarningCfgExcelVO {

    @ExcelProperty("主键")
    private String id;

    @ExcelProperty("检查点名称")
    private String name;

    @ExcelProperty("模块来源")
    private String modelCode;

    @ExcelProperty("模块来源名称")
    private String modelName;

    @ExcelProperty("启用状态")
    private String status;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
