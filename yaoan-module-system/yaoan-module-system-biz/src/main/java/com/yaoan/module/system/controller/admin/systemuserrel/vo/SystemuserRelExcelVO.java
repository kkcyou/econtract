package com.yaoan.module.system.controller.admin.systemuserrel.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统对接用户关系 Excel VO
 *
 * @author lls
 */
@Data
public class SystemuserRelExcelVO {

    @ExcelProperty("id")
    private String id;

    @ExcelProperty("采购单位id")
    private String buyerOrgId;

    @ExcelProperty("采购人id")
    private String buyerUserId;

    @ExcelProperty("对应本系统用户id")
    private Long currentUserId;

    @ExcelProperty("对应本系统租户id")
    private Long currentTenantId;

    @ExcelProperty("对应本系统部门id")
    private Long deptId;

    @ExcelProperty("创建人名称")
    private String creatorName;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
