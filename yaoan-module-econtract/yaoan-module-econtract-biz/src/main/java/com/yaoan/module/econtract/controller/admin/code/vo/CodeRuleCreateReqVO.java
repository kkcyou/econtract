package com.yaoan.module.econtract.controller.admin.code.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import java.util.List;

@Schema(description = "CodeRuleCreateReqVO")
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class CodeRuleCreateReqVO {
    /**
     * 主键
     */
    private String id;

    /**
     * 编号名称
     */
    private String name;

    /**
     * 编号规则内容
     */
    private String rule;

    /**
     * 编号规则详情list
     */
    @Valid
    private List<CodeRuleInfoReqVO> codeRuleInfoReqVOList;

    /**
     * 状态 0：禁用，1：启用
     */
    private Integer status;
}
