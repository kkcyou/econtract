package com.yaoan.module.econtract.controller.admin.performtasktype.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/1 13:41
 */
@Data
public class PerformTaskTypeUpdateReqVO {
    /**
     * 履约任务ID
     */
    @NotEmpty(message = "履约任务ID不可为空")
    @Schema(description = "履约任务ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 履约任务名称
     */
    @NotEmpty(message = "履约任务名称不可为空")
    @Schema(description = "履约任务名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;




}