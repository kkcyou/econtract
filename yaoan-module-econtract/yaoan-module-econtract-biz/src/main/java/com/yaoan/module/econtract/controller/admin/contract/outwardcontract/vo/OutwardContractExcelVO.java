package com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 对外合同 Excel VO
 *
 * @author Pele
 */
@Data
public class OutwardContractExcelVO {

    @ExcelProperty("主键")
    private String id;

    @ExcelProperty("合同编码")
    private String code;

    @ExcelProperty("合同名称")
    private String name;

    @ExcelProperty("解除之前的状态")
    private Integer oldStatus;

    @ExcelProperty("合同状态")
    private Integer status;

    @ExcelProperty("合同内容")
    private byte[] contractContent;

    @ExcelProperty("合同分类")
    private Integer contractCategory;

    @ExcelProperty("合同类型")
    private String contractType;

    @ExcelProperty("合同描述")
    private String contractDescription;

    @ExcelProperty("签署文件名称")
    private String fileName;

    @ExcelProperty("文件地址id")
    private Long fileAddId;

    @ExcelProperty("文件pdf地址id")
    private Long pdfFileId;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("部门标识")
    private Long deptId;

    @ExcelProperty("公司id")
    private Long companyId;

}
