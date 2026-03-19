package com.yaoan.module.econtract.controller.admin.watermark.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 水印管理 Excel VO
 *
 * @author lls
 */
@Data
public class WatermarkExcelVO {

    @ExcelProperty("id")
    private String id;

    @ExcelProperty("编码")
    private String code;

    @ExcelProperty("水印名称")
    private String name;

    @ExcelProperty("水印类型 文字0 图片1")
    private Integer type;

    @ExcelProperty("字号")
    private Integer watermarkSize;

    @ExcelProperty("倾斜角度")
    private Integer watermarkAngle;

    @ExcelProperty("水印透明度")
    private Integer watermarkAlpha;

    @ExcelProperty("水印位置 充满full ")
    private String position;

    @ExcelProperty("图片id ")
    private Integer fileId;

    @ExcelProperty("图片url")
    private String fileUrl;

    @ExcelProperty("图片宽度")
    private String picWidth;

    @ExcelProperty("图片高度")
    private String picHeight;

    @ExcelProperty("部门标识")
    private Long deptId;

    @ExcelProperty("创建人名称")
    private String creatorName;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
