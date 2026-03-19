package com.yaoan.module.econtract.controller.admin.code.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ListCodeRuleRespVO {
    /**
     * 主键
     */
    @ExcelIgnore
    private String id;

    /**
     * 编号名称
     */
    @ExcelProperty("编号名称")
    @ColumnWidth(40)
    private String name;

    /**
     * 编号规则内容
     */
    @ExcelProperty("编号规则")
    @ColumnWidth(40)
    private String rule;

    /**
     * 编号规则详情list
     */
    @ExcelIgnore
    private List<CodeRuleInfoReqVO> codeRuleInfoReqVOList;

    /**
     * 状态 0：禁用，1：启用
     */
    @ExcelIgnore
    private Integer status;

    /**
     * 状态名称
     */
    @ExcelProperty("当前状态")
    @ColumnWidth(40)
    private String statusName;

    /**
     * 合同引用类型
     */
//    @ExcelProperty("引用合同类型")
//    @ColumnWidth(40)
    @ExcelIgnore
    private List<String> contractTypeListName;

    /**
     * 合同引用类型
     */
    @ExcelProperty("引用合同类型")
    @ColumnWidth(40)
    private String contractTypeName;

    /**
     * 创建人
     */
    @ExcelIgnore
    private Long creator;

    /**
     * 创建人名字
     */
    @ExcelProperty("创建人")
    @ColumnWidth(40)
    private String creatorName;

    /**
     * 创建时间
     */
    @ExcelIgnore
    private LocalDateTime createTime;

    /**
     * 创建时间前端导出专用
     */
    @ExcelProperty("创建时间")
    @ColumnWidth(40)
    private String createTimeStr;

    /**
     * 引用模块
     */
    @ExcelProperty("引用模块")
    @ColumnWidth(40)
    private String referenceModule;
    /**
     * 引用路径
     */
    @ExcelProperty("引用路径")
    @ColumnWidth(40)
    private String referencePath;

    /**
     * 是否预留 0 否 1 是
     */
    @ExcelIgnore
    public Integer isReserve;
}
