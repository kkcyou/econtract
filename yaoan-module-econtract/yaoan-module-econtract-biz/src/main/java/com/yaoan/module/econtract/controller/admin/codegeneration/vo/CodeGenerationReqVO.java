package com.yaoan.module.econtract.controller.admin.codegeneration.vo;

import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.enums.codeGeneration.CodeGenBusinessTypeEnums;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/5 10:39
 */
@Data
public class CodeGenerationReqVO extends PageParam {

    private static final long serialVersionUID = -5591497972335843336L;

    /**
     * 规则分类
     */
    private String ruleCategory;
    /**
     * 业务名称
     */
    private String businessName;
    /**
     * 编码类型/业务类型
     * (1=合同编码   2=范本编码  3=模板编码)
     * {@link CodeGenBusinessTypeEnums}
     */
    private String businessType;
    /**
     * 业务名称
     */
    private String childrenType;
    /**
     * 子类类型标签
     */
    private String childrenTypeTag;
    /**
     * 状态
     */
    private Integer status;

}
