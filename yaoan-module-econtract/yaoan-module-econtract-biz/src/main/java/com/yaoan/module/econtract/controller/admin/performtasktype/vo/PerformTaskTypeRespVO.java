package com.yaoan.module.econtract.controller.admin.performtasktype.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/1 13:35
 */
@Data
public class PerformTaskTypeRespVO {
    /**
     * 履约任务类型id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @Schema(description = "履约任务类型id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 履约任务类型编码
     */
    @Schema(description = "履约任务类型编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    /**
     * 履约任务类型名称
     */
    @Schema(description = "履约任务类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 创建者昵称
     */
    @Schema(description = "创建者昵称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creatorName;

    /**
     * 履约任务类型创建时间
     */
    private String createTime;


    /**
     * 是否可更改
     */
    @Schema(description = "是否可更改", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean modifiable;
}
