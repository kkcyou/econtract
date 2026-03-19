package com.yaoan.module.system.controller.admin.dept.vo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 部门列表 Request VO")
@Data
public class DeptUserRespVO {

    @Schema(description = "部门名称", example = "芋道")
    private String name;

    @Schema(description = "部门编号id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "部门编号id不能为空")
    private Long id;
    @Schema(description = "父菜单 ID", example = "1024")
    private Long parentId;

    private List<Map<String,Object>> children;   //userid  username

}
