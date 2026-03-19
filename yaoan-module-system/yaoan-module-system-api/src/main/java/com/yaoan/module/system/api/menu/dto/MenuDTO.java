package com.yaoan.module.system.api.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class MenuDTO {
    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
    private String name;

    @Schema(description = "权限标识,仅菜单类型为按钮时，才需要传递", example = "sys:menu:add")
    private String permission;

    @Schema(description = "类型，参见 MenuTypeEnum 枚举类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "父菜单 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long parentId;

    @Schema(description = "路由地址,仅菜单类型为菜单或者目录时，才需要传", example = "post")
    private String path;

    @Schema(description = "菜单图标,仅菜单类型为菜单或者目录时，才需要传", example = "/menu/list")
    private String icon;

    @Schema(description = "组件路径,仅菜单类型为菜单时，才需要传", example = "system/post/index")
    private String component;

    @Schema(description = "组件名", example = "SystemUser")
    private String componentName;

    @Schema(description = "状态,见 CommonStatusEnum 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "是否可见", example = "false")
    private Boolean visible;

    @Schema(description = "是否缓存", example = "false")
    private Boolean keepAlive;

    @Schema(description = "是否总是显示", example = "false")
    private Boolean alwaysShow;

    @Schema(description = "快捷入口图标")
    private String fastEntryIcon;

    @Schema(description = "菜单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 子路由
     */
    private List<MenuDTO> children;
}
