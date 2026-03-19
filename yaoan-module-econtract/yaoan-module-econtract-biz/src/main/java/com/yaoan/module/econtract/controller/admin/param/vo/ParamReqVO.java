package com.yaoan.module.econtract.controller.admin.param.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * @author doujiale
 */
@Data
@Schema(description = "参数")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ParamReqVO extends BaseParamVO {
    @Schema(description = "参数id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    @Schema(description = "minIO存放参数图标的路径", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long iconId;

//    @Schema(description = "图标文件", requiredMode = Schema.RequiredMode.REQUIRED)
//    private MultipartFile Img;
    /**
     * 行数 表格类型才有此字段
     */
    @Schema(description = "行数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer lineNum;
    /**
     * 列数 表格类型才有此字段
     */
    @Schema(description = "列数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer columnNum;
    /**
     * 表格方向 1：橫向，2：竖向 表格类型才有此字段
     */
    @Schema(description = "表格方向", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tableDirection;
    /**
     * 行高 表格类型才有此字段
     */
    @Schema(description = "行高", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer lineHigh;
    /**
     * 合计行 yes：有, no:无 表格类型才有此字段
     */
    @Schema(description = "合计行", requiredMode = Schema.RequiredMode.REQUIRED)
    private String totalLine;
    /**
     * 对齐方式 left:左对齐，right：右对齐，center：居中对齐 表格类型才有此字段
     */
    @Schema(description = "对齐方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String alignment;
    /**
     * 表格设置 表格类型才有此字段
     */
    @Schema(description = "表格设置", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tableSet;
    /**
     * 选项数 下拉框类型才有此字段
     */
    @Schema(description = "选项数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer optionNum;
    /**
     * 选项设置 下拉框类型才有此字段
     */
    @Schema(description = "选项设置", requiredMode = Schema.RequiredMode.REQUIRED)
    private String optionSet;

}
