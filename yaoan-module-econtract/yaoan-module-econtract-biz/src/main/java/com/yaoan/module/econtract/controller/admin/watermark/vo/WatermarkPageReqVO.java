package com.yaoan.module.econtract.controller.admin.watermark.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 水印管理分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WatermarkPageReqVO extends PageParam {

    @Schema(description = "编码")
    private String code;

    @Schema(description = "水印名称", example = "测试水印")
    private String name;

    @Schema(description = "水印类型 文字0 图片1", example = "0")
    private Integer type;

    @Schema(description = "字号")
    private Integer watermarkSize;

    @Schema(description = "倾斜角度")
    private Integer watermarkAngle;

    @Schema(description = "水印透明度")
    private Integer watermarkAlpha;

    @Schema(description = "水印位置 充满full ")
    private String position;

    @Schema(description = "图片id ", example = "8600")
    private Integer fileId;

    @Schema(description = "图片url", example = "https://www.baidu.cn")
    private String fileUrl;

    @Schema(description = "图片宽度")
    private String picWidth;

    @Schema(description = "图片高度")
    private String picHeight;

    @Schema(description = "部门标识", example = "1233")
    private Long deptId;

    @Schema(description = "创建人名称", example = "李四")
    private String creatorName;

  

}
