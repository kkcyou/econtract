package com.yaoan.module.econtract.convert.contracttemplate;

import com.yaoan.module.econtract.controller.admin.contracttemplate.vo.TermReqVO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplateTermRelDO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.TemplateTermDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface TemplateTermConverter {
    TemplateTermConverter INSTANCE = Mappers.getMapper(TemplateTermConverter.class);

    List<TemplateTermDO> convertList(List<TermReqVO> bean);

    List<TermReqVO> convert2List(List<TemplateTermDO> templateTermDOS);

    default List<ContractTemplateTermRelDO> convertReq2DO(List<TermReqVO> termList, String templateId) {
        List<ContractTemplateTermRelDO> result = new ArrayList<>();
        termList.forEach(item -> {
            ContractTemplateTermRelDO entity = new ContractTemplateTermRelDO()
                    .setTermId(item.getId())
                    .setTemplateId(templateId)
                    .setSort(item.getSort())
                    .setTitle(item.getTitle());
            result.add(entity);
        });
        return result;
    }
}
