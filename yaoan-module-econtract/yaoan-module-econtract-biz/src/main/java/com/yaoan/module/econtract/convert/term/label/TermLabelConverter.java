package com.yaoan.module.econtract.convert.term.label;

import com.yaoan.module.econtract.controller.admin.term.vo.termlabel.LabelVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termlabel.TermLabelVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termparam.ParamVO;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.dataobject.term.TermLabel;
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
public interface TermLabelConverter {

    TermLabelConverter INSTANCE = Mappers.getMapper(TermLabelConverter.class);

    default List<TermLabel> convert(List<LabelVO> labelVOs, Term term) {

        List<TermLabel> result = new ArrayList<>();
        labelVOs.forEach(item -> {
            TermLabel termLabel = new TermLabel().setTermId(term.getId()).setLabelId(item.getLabelId()).setLabelName(item.getLabelName());
            result.add(termLabel);
        });
        return result;
    }

    List<TermLabelVO> toList(List<TermLabel> termLabels);
}
