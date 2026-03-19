package com.yaoan.module.econtract.controller.admin.performtasktype.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/3 17:58
 */
@Data
public class PerformTaskTypeAllVO extends TenantBaseDO {

    /**
     * 履约任务类型id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @Schema(description = "履约任务类型id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;


    /**
     * 履约任务类型编码
     */
    @NotNull(message = "编码不能为空")
    @Schema(description = "履约任务类型编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "履约任务类型编码")
    private String code;

    /**
     * 履约任务类型名称
     */
    @NotNull(message = "名称不能为空")
    @Schema(description = "履约任务类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "履约任务类型名称")
    private String name;

    @Schema(description = "履约类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String performTypeId;

    @Schema(description = "创建者昵称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creatorNikeName;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    private Boolean modifiable;
}
