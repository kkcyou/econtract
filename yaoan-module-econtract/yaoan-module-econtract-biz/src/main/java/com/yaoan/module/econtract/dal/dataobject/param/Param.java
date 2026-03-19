package com.yaoan.module.econtract.dal.dataobject.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_param")
public class Param extends DeptBaseDO implements Serializable {
    private static final long serialVersionUID = -3315146623818234512L;
    /**
     * 参数id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数编码
     */
    private String code;


    /**
     * 参数分类id
     */
    private Integer categoryId;

    /**
     * 参数类型
     */
    private String type;

    /**
     * 输入提示
     */
    private String placeholder;

    /**
     * 字数长度要求
     */
    private String maxLength;

    /**
     * 参数描述
     */
    private String content;

    /**
     * minIO存放参数图标的路径
     */
    private Long iconId;

    /**
     * 合同类型
     */
    private String contractType;
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
