package com.yaoan.module.bpm.api.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;

/**
 * 处理任务DTO
 *
 * @author doujl
 */
@Data
public class BpmHandleTaskReqDTO {

    @NotNull(message = "流程处理人不能为空")
    private Long userId;

    @NotEmpty(message = "流程处理任务ID不能为空")
    private String taskId;

    /**
     * 变量实例
     */
    private Map<String, Object> variables;

    @NotNull(message = "处理结果不能空")
    private Integer result;

    private String reason;

    @Schema(description = "抄送的用户编号数组", requiredMode = Schema.RequiredMode.REQUIRED, example = "1,2")
    private Collection<Long> copyUserIds;

}
