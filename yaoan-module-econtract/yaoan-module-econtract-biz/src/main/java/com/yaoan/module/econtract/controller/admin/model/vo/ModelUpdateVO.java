package com.yaoan.module.econtract.controller.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 16:37
 */
@Schema(description = "模板更新 Request VO")
@Data
public class ModelUpdateVO {


    @Schema(description = "模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotBlank(message = "id不能为空")
    private String id;

    @Schema(description = "模板类型 1.调款新增 2.上传文件 3.范本新增", example = "1")
    @NotBlank(message = "类型不能为空")
    private String type;

    @Schema(description = "模板编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotBlank(message = "模板编码不能为空")
    @Size(max = 30, message = "模板编码长度不能超过100个字符")
    private String code;

    @Schema(description = "模板名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotBlank(message = "模板名称不能为空")
    @Size(max = 30, message = "模板名称长度不能超过100个字符")
    private String name;

    @Schema(description = "模板分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
//    @NotNull(message = "模板分类不能为空")
    private Integer categoryId;

    @Schema(description = "条款列表")
    private String terms;

    @Schema(description = "模版内容")
    private String modelContent = "";

    @Schema(description = "模板类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotBlank(message = "模板类型不能为空")
    private String contractType;

    /**
     * 时效模式 (0=部分时间有效，1=长期有效)
     */
    @Schema(description = "时效模式", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @NotNull(message = "时效模式不能为空")
    private Integer timeEffectModel;

    /**
     * 模板生效时间
     */
    @Schema(description = "模板生效时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime effectStartTime;

    /**
     * 模板生效结束时间
     */
    @Schema(description = "模板生效结束时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-07-21 16:02:57")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime effectEndTime;

    /**
     * 模板生效时间
     */
    @Schema(description = "模板生效时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2021-07-11 16:02:57")
    private String effectStartTimeReciever;

    /**
     * 模板生效结束时间
     */
    @Schema(description = "模板生效结束时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-07-21 16:02:57")
    private String effectEndTimeReciever;

    /**
     * 模板描述
     */
    @Schema(description = "模板描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "模板描述内容sample-模板描述内容sample")
    private String description;

    /**
     * 上传的模板文件
     */
    @Schema(description = "上传的模板文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private MultipartFile file;

    /**
     * 模板所绑定的参数
     */
    @Schema(description = "模板所绑定的参数", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private List<ParamModelVo> paramModelVoList;

    /**
     * 参数接收器
     */
    @Schema(description = "参数接收器", requiredMode = Schema.RequiredMode.REQUIRED, example = "[{\"paramId\": " +
            "\"53506c3a044f558887f4da0cda79e134\",\"location\": \"x1y1\"}," +
            "{\"paramId\": \"e087e3200fc634204f236e9cec6c5da5\",\"location\": \"x2y2\"}]")
    private String paramListReciever;

    /**
     * 远端文件对应ID（上传后的文件）
     */
    @Schema(description = "远端文件对应ID（上传后的文件）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long remoteFileId;

    /**
     * 编辑ID（被编辑的模板ID）
     */
    @Schema(description = "编辑ID（被编辑的模板ID）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String updateId;

    /**
     * 范本id
     */
    @Schema(description = "范本id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String templateId;

    /**
     * 是否失效 0失效 1有效
     */
    @Schema(description = "是否失效 0失效 1有效", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer effective;
    /**
     * 版本
     */
    @Schema(description = "版本", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double version;

    /**
     * 文件名称
     */
    @Schema(description = "文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;

    /**
     * 区划编号
     */
    private String regionCode;

    /**
     * 区划名称
     */
    private String regionName;

    /**
     * 是否有编辑权限
     * 0=无，1=有
     */
    private Integer editPermission;
}
