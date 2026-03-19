package com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PerfTaskReqVO {
    /**
     * 履约任务id
     */
    @Schema(description = "履约任务id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "履约任务id不能为空")
    private String id;
    /**
     * 履约任务状态编码
     */
    @Schema(description = "履约任务状态编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "履约任务状态编码不能为空")
    private Integer taskStatus;

    /**
     * 履约文件
     */
    @Schema(description = "履约文件", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private MultipartFile file;
    /**
     * 履约说明
     */
    @Schema(description = "履约说明", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String remark;
    /**
     * 履约文件
     */
    @Schema(description = "履约文件id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long fileId;
}
