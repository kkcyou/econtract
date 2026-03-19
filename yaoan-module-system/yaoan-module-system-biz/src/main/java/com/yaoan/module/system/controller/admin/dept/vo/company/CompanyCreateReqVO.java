package com.yaoan.module.system.controller.admin.dept.vo.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 单位创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CompanyCreateReqVO extends CompanyBaseVO {

    @Schema(description = "用户名", example = "1")
    private String username;

    @Schema(description = "密码", example = "12345678")
    private String password;

    /**
     * 个人独有属性
     */
    @Schema(description = "身份证号", example = "15601691000")
    private String idCard;

    @Schema(description = "昵称")
    private String nickname;

    /**
     * 相对方id
     */
    private String relativeId;
    private Long deptId;

}
