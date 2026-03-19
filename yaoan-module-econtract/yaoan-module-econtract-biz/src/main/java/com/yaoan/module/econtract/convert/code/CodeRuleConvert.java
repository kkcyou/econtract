package com.yaoan.module.econtract.convert.code;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.code.vo.CodeRuleCreateReqVO;
import com.yaoan.module.econtract.controller.admin.code.vo.CodeRuleInfoReqVO;
import com.yaoan.module.econtract.controller.admin.code.vo.ListCodeRuleReqVO;
import com.yaoan.module.econtract.controller.admin.code.vo.ListCodeRuleRespVO;
import com.yaoan.module.econtract.controller.admin.codegeneration.vo.CodeGenerationRespVO;
import com.yaoan.module.econtract.dal.dataobject.code.CodeRuleDO;
import com.yaoan.module.econtract.dal.dataobject.code.CodeRuleInfoDO;
import com.yaoan.module.econtract.dal.dataobject.codegeneration.CodeGenerationDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CodeRuleConvert {
    CodeRuleConvert INSTANCE = Mappers.getMapper(CodeRuleConvert.class);

    CodeRuleDO convertDO(CodeRuleCreateReqVO bean);

    List<CodeRuleInfoDO> convertDOList(List<CodeRuleInfoReqVO> bean);

    List<CodeRuleInfoReqVO> convertList(List<CodeRuleInfoDO> bean);

    CodeRuleInfoDO convert(CodeRuleInfoReqVO bean);

    PageResult<ListCodeRuleRespVO> convertPage(PageResult<CodeRuleDO> bean);

    List<ListCodeRuleRespVO> convertListV2(List<CodeRuleDO> bean);

    ListCodeRuleRespVO convert(CodeRuleDO bean);
}
