package com.yaoan.module.system.controller.admin.dept.vo.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 单位 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 * @author doujiale
 */
@Data
public class CompanyBaseVO {

    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
//    @NotBlank(message = "单位名称不能为空")
    @Size(max = 30, message = "单位名称长度不能超过30个字符")
    private String name;

    @Schema(description = "信用代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
//    @NotBlank(message = "信用代码不能为空")
    @Size(max = 30, message = "信用代码长度不能超过30个字符")
    private String creditCode;

//    @Schema(description = "部门 ID", example = "1024")
//    private Long deptId;

    @Schema(description = "是否是供应商,0是，1不是", example = "true")
    private Integer supplier;

    @Schema(description = "显示顺序不能为空", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;

    @Schema(description = "负责人的用户编号", example = "2048")
    private Long leaderUserId;

    @Schema(description = "联系电话", example = "15601691000")
    @Size(max = 11, message = "联系电话长度不能超过11个字符")
    private String phone;

    @Schema(description = "邮箱", example = "yudao@iocoder.cn")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    @Schema(description = "状态,见 CommonStatusEnum 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "公司类型,见 MajorEnum 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "公司类型不能为空")
    private Integer major;

    private Long tenantId;

}
