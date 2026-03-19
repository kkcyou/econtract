package com.yaoan.module.econtract.convert.codegeneration;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.codegeneration.vo.CodeGenDetailRespVO;
import com.yaoan.module.econtract.controller.admin.codegeneration.vo.CodeGenerationRespVO;
import com.yaoan.module.econtract.controller.admin.codegeneration.vo.CodeGenerationSaveReqVO;
import com.yaoan.module.econtract.controller.admin.codegeneration.vo.CodeGenerationUpdateVO;
import com.yaoan.module.econtract.dal.dataobject.codegeneration.CodeGenerationDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/5 10:29
 */
@Mapper
public interface CodeGenerationConvert {
    CodeGenerationConvert INSTANCE = Mappers.getMapper(CodeGenerationConvert.class);


    PageResult<CodeGenerationRespVO> pageD2R(PageResult<CodeGenerationDO> pageResult);

    List<CodeGenerationRespVO> listD2R(List<CodeGenerationDO> i);

    CodeGenerationRespVO d2R(CodeGenerationDO i);

    CodeGenerationDO r2D(CodeGenerationSaveReqVO vo);

    CodeGenerationDO upR2D(CodeGenerationUpdateVO vo);

    CodeGenDetailRespVO detailD2R(CodeGenerationDO generationDO);
}
