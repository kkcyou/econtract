package com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 角色工作台关联 Excel VO
 *
 * @author admin
 */
@Data
public class RoleWorkbenchRelExcelVO {

    @ExcelProperty("id")
    private String id;

    @ExcelProperty("角色id")
    private Long roleId;

    @ExcelProperty("角色名称")
    private String roleName;

    @ExcelProperty("工作台id")
    private String workbenchId;

    @ExcelProperty("工作台名称")
    private String workbenchName;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
