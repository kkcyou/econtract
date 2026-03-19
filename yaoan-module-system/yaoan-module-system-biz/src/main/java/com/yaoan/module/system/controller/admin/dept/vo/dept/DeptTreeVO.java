package com.yaoan.module.system.controller.admin.dept.vo.dept;

import cn.hutool.core.annotation.Alias;
import com.yaoan.module.system.controller.admin.user.vo.user.UserSimpleRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "管理后台 - 部门信息 Response VO")
@Data
public class DeptTreeVO {


    private Long id;

    @Schema(description = "部门名称")
    @Alias("name")
    private String nickname;

    @Schema(description = "父菜单 ID", example = "1024")
    private Long parentId;

    @Schema(description = "显示顺序不能为空")
    private Integer sort;

    @Schema(description = "子部门/当前部门下的用户")
    private List children = new ArrayList<>();

//    @Schema(description = "部门用户")
//    private List<UserSimpleRespVO> users = new ArrayList<>();


    public void addChild(Object obj) {
        children.add(obj);
    }

//    public void addUser(UserSimpleRespVO user) {
//        users.add(user);
//    }
}
