package com.yaoan.module.econtract.controller.admin.warningparam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 预警消息模板参数(new预警) Excel VO
 *
 * @author admin
 */
@Data
public class WarningParamExcelVO {

    @ExcelProperty("主键")
    private String id;

    @ExcelProperty("参数名称")
    private String name;

    @ExcelProperty("参数配置")
    private String paramCfg;

    @ExcelProperty("模块来源")
    private String modelId;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
