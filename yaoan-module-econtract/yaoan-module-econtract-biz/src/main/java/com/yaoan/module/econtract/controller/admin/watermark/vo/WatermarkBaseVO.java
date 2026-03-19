package com.yaoan.module.econtract.controller.admin.watermark.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 水印管理 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class WatermarkBaseVO {

    @Schema(description = "编码")
    private String code;

    @Schema(description = "水印名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "鸿运科技")
    @NotNull(message = "水印名称不能为空")
    private String name;

    @Schema(description = "水印内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "鸿运科技")
    private String content;

    @Schema(description = "水印类型 自定义文字水印 = 0 业务字段 = 1", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer type;

    @Schema(description = "字号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer watermarkSize;

    @Schema(description = "倾斜角度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "倾斜角度不能为空")
    private Integer watermarkAngle;

    @Schema(description = "水印透明度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "水印透明度不能为空")
    private Integer watermarkAlpha;

    @Schema(description = "水印位置 充满full ")
    private String position;

    @Schema(description = "图片id ", example = "8600")
    private Integer fileId;

    @Schema(description = "图片url", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "图片宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    private String picWidth;

    @Schema(description = "图片高度", requiredMode = Schema.RequiredMode.REQUIRED)
    private String picHeight;

    @Schema(description = "部门标识", example = "1233")
    private Long deptId;

    @Schema(description = "创建人名称", example = "李四")
    private String creatorName;

}
