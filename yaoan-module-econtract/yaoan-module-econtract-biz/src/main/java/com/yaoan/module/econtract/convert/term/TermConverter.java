package com.yaoan.module.econtract.convert.term;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.TermsAddVO;
import com.yaoan.module.econtract.controller.admin.contracttemplate.vo.TermListRespVO;
import com.yaoan.module.econtract.controller.admin.contracttemplate.vo.TermOneRespVO;
import com.yaoan.module.econtract.controller.admin.contracttemplate.vo.TermReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.*;
import com.yaoan.module.econtract.controller.admin.term.vo.bpm.TermListApproveRespVO;
import com.yaoan.module.econtract.controller.admin.term.vo.tree.TermTreeDetailRespVO;
import com.yaoan.module.econtract.dal.dataobject.term.SimpleTerm;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * @Author：doujl
 * @Description: 转换类
 * @Date: 2023年07月05日14:30:38
 */
@Mapper
public interface TermConverter {

    TermConverter INSTANCE = Mappers.getMapper(TermConverter.class);

    @Mapping(target = "termContentTxt", source = "termContent", defaultValue = "")
    @Mapping(target = "termContent", expression = "java(termAddVo.getTermContent().getBytes())")
    Term toEntity(TermAddVO termAddVo);

    @Mapping(target = "termContentTxt", source = "termContent", defaultValue = "")
    @Mapping(target = "termContent", expression = "java(updateVo.getTermContent().getBytes())")
    Term toEntity(TermUpdateVO updateVo);

    @Mapping(target = "termContent", expression = "java(updateVo.getTermContent().getBytes())")
    Term toEntity(TermPublishVO updateVo);

    List<TermRespVO> convertList(List<Term> page);

    @Mapping(target = "termContent", expression = "java(content2String(term.getTermContent()))")
    TermsAddVO toAddVO(Term term);
    List<TermsAddVO> convertAddList(List<Term> list);

    PageResult<TermRespVO> convertPage(PageResult<Term> page);

    @Mapping(target = "termContent", expression = "java(content2String(term.getTermContent()))")
    TermRespVO toVO(Term term);
    default String content2String(byte[] termContent) {
        if(termContent==null){
            return "";
        }
        return StringUtils.toEncodedString(termContent, StandardCharsets.UTF_8);
    }


    List<TermTreeDetailRespVO> treeDO2RespVO(List<Term> termList);

    @Mapping(target = "termContent", expression = "java(content2String(entity.getTermContent()))")
    TermTreeDetailRespVO convertTree(Term entity);

    List<TermListApproveRespVO> convertBpmDO2Resp(List<Term> doList);

    @Mapping(target = "termContent", expression = "java(content2String(entity.getTermContent()))")
    TermListApproveRespVO convertDO2ApproveRespVO(Term entity);

    List<Term> convertReq2DO(List<TermReqVO> termList);

    List<TermListRespVO> do2Resp(List<Term> terms);

    List<TermOneRespVO> listEntity2Resp(List<Term> termList);

    @Mapping(target = "termContent", expression = "java(content2String(entity.getTermContent()))")
    TermOneRespVO c5(Term entity);

    List<TermSimpleRespVO> do2RespList(List<Term> searchProductList);

    PageResult<TermSimpleRespVO> pageDo2Resp4Es(PageResult<SimpleTerm> result);
}
