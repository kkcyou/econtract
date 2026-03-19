package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典类型 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class ContractTypeExcelVO {

    @ExcelProperty("ID")
    private String id;

    @ExcelProperty("名字")
    private String name;

    @ExcelProperty("编号")
    private String code;

    @ExcelProperty("父类编号")
    private String parentId;

    @ExcelProperty("编号生成规则名称")
    private String codeRuleName;

    @ExcelProperty("流程配置数量")
    private Integer processNum;

    @ExcelProperty("合同类型状态")
    private String typeStatusName;

    @ExcelProperty("创建人")
    private String creatorName;

    @ExcelProperty("创建时间")
    private String createTime;


}
