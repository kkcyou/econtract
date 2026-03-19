package com.yaoan.module.econtract.controller.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 15:58
 */
@Data
public class RecentUseModelVO extends ModelBaseVO {

    /**
     * 模板id
     */
    @Schema(description = "模板id")
    private String id;

    /**
     * 模板编码
     */
    @Schema(description = "模板编码")
    private String code;
    /**
     * 模板名称
     */
    @Schema(description = "模板名称")
    private String name;
    /**
     * 合同类型
     */
    @Schema(description = "合同类型ID")
    private String contractType;
    /**
     * 合同类型名称
     */
    @Schema(description = "合同类型名称")
    private String contractTypeName;

    /**
     * 模板时效 (0=部分时间有效，1=长期有效)
     */
    @Schema(description = "模板时效标识")
    private Integer timeEffectModel;

    /**
     * 模板生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime effectStartTime;

    /**
     * 模板生效结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime effectEndTime;

    @Schema(description = "模板时效名称")
    private String timeEffectModelName;
    /**
     * 模板使用次数
     */
    @Schema(description = "模板使用次数")
    private String useNum;
    /**
     * 合同来源：模板，文件
     */
    @Schema(description = "合同来源")
    private String contractSource;

    /**
     * 版本
     */
    private Double version;

    /**
     * 是否失效 0失效 1有效
     */
    private Integer effective;

    /**
     * 生效时间内是否启用 0未启用 1启用
     */
    private Integer effectStatus;

    /**
     * 富文本转换文件ID（条款生成的模板的文件ID）
     */
    private Long rtfPdfFileId;
}
