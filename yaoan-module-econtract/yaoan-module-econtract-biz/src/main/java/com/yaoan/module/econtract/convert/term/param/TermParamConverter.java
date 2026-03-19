package com.yaoan.module.econtract.convert.term.param;

import com.yaoan.module.econtract.controller.admin.term.vo.TermListRespVO;
import com.yaoan.module.econtract.controller.admin.term.vo.TermRespVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termparam.ParamVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termparam.TermParamVO;
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
public interface TermParamConverter {

    TermParamConverter INSTANCE = Mappers.getMapper(TermParamConverter.class);

    default List<TermParam> convert(List<ParamVO> paramVOs, Term term) {

        List<TermParam> result = new ArrayList<>();
        paramVOs.forEach(item -> {
            TermParam termParam = new TermParam().setTermId(term.getId()).setParamId(item.getParamId()).setParamNum(item.getParamNum());
            result.add(termParam);
        });
        return result;
    }

    List<TermParamVO> toList(List<TermParam> termParams);
    List<TermListRespVO> toList2(List<TermRespVO> termLabels);
}
