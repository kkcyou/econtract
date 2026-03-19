package com.yaoan.module.econtract.controller.admin.watermark.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yaoan.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 水印管理 Excel 导出 Request VO，参数和 WatermarkPageReqVO 是一致的")
@Data
public class WatermarkExportReqVO {

    @Schema(description = "编码")
    private String code;

    @Schema(description = "水印名称", example = "赵六")
    private String name;

    @Schema(description = "水印类型 文字0 图片1", example = "2")
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

    @Schema(description = "图片url", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "图片宽度")
    private String picWidth;

    @Schema(description = "图片高度")
    private String picHeight;

    @Schema(description = "部门标识", example = "1233")
    private Long deptId;

    @Schema(description = "创建人名称", example = "李四")
    private String creatorName;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
