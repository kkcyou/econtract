package com.yaoan.module.econtract.controller.admin.codegeneration.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/5 10:46
 */
@Data
public class CodeGenerationSaveReqVO {
    private static final Integer DEFAULT_LENGTH = 4;
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;
    /**
     * 编码类型/业务类型
     * (1=合同编码   2=范本编码  3=模板编码)
     */
    private String businessType;
    /**
     * 业务名称
     */
    private String businessName;
    /**
     * 业务名称
     */
    private String childrenType;
    /**
     * 子类类型标签
     */
    private String childrenTypeTag;
    /**
     * 状态（0=关闭，1=开启）
     */
    private Integer status;
    /**
     * 编码格式
     */
    @NotBlank(message = "编码格式不能为空")
    private String generateRule;

    /**
     * 序号长度
     * 默认4
     */
    private Integer length = DEFAULT_LENGTH;

    /**
     * 业务前缀
     */
    private String prefix;

    /**
     * 备注
     */
    private String remark;
}
