package com.yaoan.module.econtract.convert.term;

import com.yaoan.module.econtract.controller.admin.model.vo.TermsDetailsVo;
import com.yaoan.module.econtract.dal.dataobject.term.ContractTermDO;
import org.mapstruct.Mapper;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractTermDTO;
import com.yaoan.module.econtract.controller.admin.model.vo.TermsDetailsVo;
import com.yaoan.module.econtract.dal.dataobject.term.ContractTermDO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Mapper
public interface ContractTermConverter {
    ContractTermConverter INSTANCE = Mappers.getMapper(ContractTermConverter.class);
    List<ContractTermDO> convertList(List<TermsDetailsVo> beans);
    @Mapping(target = "editable", expression = "java(toIntegerEnable(beans.getEnableEdit()))")
    @Mapping(target = "termName", source = "name")
    ContractTermDO convert2DO(TermsDetailsVo beans);

    List<TermsDetailsVo> convertListVO(List<ContractTermDO> beans);
    @Mapping(target = "enableEdit", expression = "java(toBooleanEnable(beans.getEditable()))")
    @Mapping(target = "name", source = "termName")
    TermsDetailsVo convertVO (ContractTermDO beans);

    @Mapping(target = "enableEdit", expression = "java(toBooleanEnable(beans.getEditable()))")
    @Mapping(target = "name", source = "termName")
    ContractTermDTO convertDTO (ContractTermDO beans);
    List<ContractTermDTO> convertDTOList(List<ContractTermDO> beans);

    default Boolean toBooleanEnable(Integer editable){
        if(ObjectUtil.isNotEmpty(editable) && editable == 0){
            return false;
        }
        return true;
    }

    default Integer toIntegerEnable(Boolean editable){
        if(ObjectUtil.isNotEmpty(editable) && !editable){
            return 0;
        }
        return 1;
    }

    default String content2String(byte[] termContent) {
        return StringUtils.toEncodedString(termContent, StandardCharsets.UTF_8);
    }
}
