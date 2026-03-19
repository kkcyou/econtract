package com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 合同模板推荐 Excel VO
 *
 * @author doujiale
 */
@Data
public class ContractAiDraftShowExcelVO {

    @ExcelProperty("模板ID")
    private Long templateiId;

    @ExcelProperty("模板名称")
    private String templateName;

    @ExcelProperty("使用的大模型")
    private String model;

    @ExcelProperty("模板内容")
    private String templateContent;

    @ExcelProperty("合同名称")
    private String contractName;

    @ExcelProperty("使用次数")
    private Integer useNum;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
