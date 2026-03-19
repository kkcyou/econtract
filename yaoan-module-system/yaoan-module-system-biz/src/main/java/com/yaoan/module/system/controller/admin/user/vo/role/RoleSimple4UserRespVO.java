package com.yaoan.module.system.controller.admin.user.vo.role;

import com.yaoan.module.system.controller.admin.user.vo.user.UserSimpleRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-9 13:55
 */
@Data
public class RoleSimple4UserRespVO {

    @Schema(description = "角色编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
    private String name;

    private List<UserSimpleRespVO> userList;
}
