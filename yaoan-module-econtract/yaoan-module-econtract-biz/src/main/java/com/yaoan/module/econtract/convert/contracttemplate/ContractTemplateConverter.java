package com.yaoan.module.econtract.convert.contracttemplate;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.bpm.api.bpm.activity.dto.BpmProcessRespDTO;
import com.yaoan.module.econtract.api.contracttemplate.dto.ContractTemplateDTO;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.CommonOfModelAndTemplateBpmProcessRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.performance.suspend.vo.BpmProcessRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.template.vo.TemplateListApproveRespVO;
import com.yaoan.module.econtract.controller.admin.contracttemplate.vo.*;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.TemplateQuoteDO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/9 23:06
 */
@Mapper
public interface ContractTemplateConverter {
    ContractTemplateConverter INSTANCE = Mappers.getMapper(ContractTemplateConverter.class);

    default String content2String(byte[] termContent) {
        if (termContent == null) {
            return null;
        }
        return StringUtils.toEncodedString(termContent, StandardCharsets.UTF_8);
    }

    ContractTemplate toEntity(ContractTemplateDTO dto);

    ContractTemplateDTO toDTO(ContractTemplate contractTemplate);

    List<ContractTemplateDTO> toDTOs(List<ContractTemplate> demos);

    List<ContractTemplate> toEntities(List<ContractTemplateDTO> demos);


    PageResult<TemplatePageRespVo> convertPage(PageResult<ContractTemplate> page);

    PageResult<TemplatePageRespVo> toPageVo(PageResult<ContractTemplate> selectPage);

    //    @Mapping(target = "content", ignore = true)
    @Mapping(target = "content", expression = "java(vo.getContent() != null ? vo.getContent().getBytes() : null)")
    ContractTemplate createVotoEntity(TemplateCreateVo vo);

    @Mapping(target = "content", expression = "java(template.getContent().getBytes())")
    ContractTemplate singleToEntity(TemplateSingleVo template);

    @Mapping(target = "content", expression = "java(content2String(contractTemplate.getContent()))")
    TemplateSingleVo entityToSingle(ContractTemplate contractTemplate);

    List<TemplateSimpleVo> convert2SimpleVo(List<ContractTemplate> templates);

    @Mapping(target = "content", expression = "java(reqVO.getContent().getBytes())")
    ContractTemplate convert2DO(TemplateUpdateReqVO reqVO);


    TemplateCreateVo convert2Creat(TemplateUpdateReqVO reqVO);

    List<TemplateAllPermissionReqVo> convert2AllVo(List<ContractTemplate> list);

    @Mapping(target = "content", expression = "java(vo.getContent().getBytes())")
    ContractTemplate updateVo2Entity(TemplateUpdateReqVO vo);

    @Mapping(target = "content", expression = "java(content2String(reqVO.getContent()))")
    TemplateUpdateReqVO convert2Update(ContractTemplate reqVO);

    @Mapping(target = "content", expression = "java(vo.getContent().getBytes())")
    ContractTemplate updateVotoEntity(TemplateUpdateReqVO vo);

    List<CommonOfModelAndTemplateBpmProcessRespVO> convertDTO2RespVO(List<BpmProcessRespDTO> bpmProcessResDTOList);

    TemplateCreateVo convertCreateSubmitVO2Create(TemplateCreateSubmitReqVO vo);

    TemplateUpdateReqVO convertUpdateSubmitVO2Update(TemplateUpdateSubmitReqVO vo);

    List<TemplateHistoryRespVO> convertList(List<ContractTemplate> bean);

    PageResult<TemplateQuotePageRespVO> toQuotePageVo(PageResult<TemplateQuoteDO> selectPage);

    PageResult<TemplateAllPermissionReqVo> convertPagev1(PageResult<ContractTemplate> page);

    TemplateAllPermissionReqVo c1(ContractTemplate entity);

    List<TemplateListApproveRespVO> convertBpmDO2Resp(List<ContractTemplate> doList);


    TemplateOneRespVo entity2OneResp(ContractTemplate contractTemplate);
}
