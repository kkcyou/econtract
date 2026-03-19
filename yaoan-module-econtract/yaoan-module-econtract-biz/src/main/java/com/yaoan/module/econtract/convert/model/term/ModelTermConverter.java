package com.yaoan.module.econtract.convert.model.term;

import com.yaoan.module.econtract.controller.admin.model.vo.ModelTermsAddVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termparam.ParamVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termparam.TermParamVO;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.term.ModelTerm;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.dataobject.term.TermParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author：doujl
 * @Description: 转换类
 * @Date: 2023年07月05日14:30:38
 */
@Mapper
public interface ModelTermConverter {

    ModelTermConverter INSTANCE = Mappers.getMapper(ModelTermConverter.class);

    default List<ModelTerm> convert(List<ModelTermsAddVO> terms, Model model) {

        List<ModelTerm> result = new ArrayList<>();
        terms.forEach(item -> {
            ModelTerm termParam = new ModelTerm().setTermId(item.getTermId()).setModelId(model.getId()).setTermNum(item.getTermNum()).setTitle(item.getTitle()).setName(item.getName());
            result.add(termParam);
        });
        return result;
    }
}
