package com.yaoan.module.econtract.controller.admin.reviewresult.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 智能审查结果 Excel VO
 *
 * @author admin
 */
@Data
public class ReviewResultExcelVO {

    @ExcelProperty("主键")
    private String id;

    @ExcelProperty("合同id")
    private String contractId;

    @ExcelProperty("审查结果（0=通过，1=低风险，2=中风险，3=高风险）")
    private Integer result;

    @ExcelProperty("标题")
    private String title;

    @ExcelProperty("风险等级（1=低风险，2=中风险，3=高风险）")
    private Integer riskLevel;

    @ExcelProperty("风险提示")
    private String riskWarning;

    @ExcelProperty("版本")
    private Long version;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
