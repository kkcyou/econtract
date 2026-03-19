package com.yaoan.module.econtract.service.codegeneration;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.codegeneration.vo.*;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/5 10:36
 */
public interface CodeGenerationService {

    /**
     * 编号生成规则列表
     */
    PageResult<CodeGenerationRespVO> list(CodeGenerationReqVO vo);


    /**
     * 编号生成规则新增
     */
    String save(CodeGenerationSaveReqVO vo);

    /**
     * 根据规则id，生成编号
     */
    String generateCodeByVO(CodeQueryReqVO vo);

    String update(CodeGenerationUpdateVO vo);

    String deleteBatch(List<String> idList);

    /**
     * 详情查看
     */
    CodeGenDetailRespVO queryCodeRuleById(String id);
}
