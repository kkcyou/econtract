package com.yaoan.module.econtract.convert.model;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.bpm.api.bpm.activity.dto.BpmProcessRespDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ModelCreateReqDTO;
import com.yaoan.module.econtract.api.model.dto.ModelDTO;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.ModelBpmAllVO;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.ModelBpmPageRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.CommonOfModelAndTemplateBpmProcessRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.ModelListApproveRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.*;
import com.yaoan.module.econtract.controller.admin.model.vo.client.ModelClientRespVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.model.ModelBpmDO;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.model.ModelClientDO;
import com.yaoan.module.econtract.dal.dataobject.model.MyCollectModel;
import com.yaoan.module.econtract.dal.dataobject.model.SimpleModel;
import com.yaoan.module.econtract.dal.dataobject.wps.SaveModel;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 16:09
 */
@Mapper
public interface ModelConverter {
    ModelConverter INSTANCE = Mappers.getMapper(ModelConverter.class);

    @Mapping(target = "modelContent", expression = "java(modelCreateReqVO.getModelContent().getBytes())")
    @Mapping(target = "fileName", expression = "java(modelCreateReqVO.getName() + \".pdf\")")
    Model toEntity(ModelCreateReqVO modelCreateReqVO);
    ModelCreateReqVO toModelCreateReqVO(Model model);
    ModelCreateReqDTO toDTO(ModelCreateReqVO model);
    Model toEntity(ModelSubmitReqVo bean);

    @Mapping(target = "modelContent", expression = "java(modelCreateReqVO.getModelContent().getBytes())")
    Model vosToModel(ModelCreateReqVO modelCreateReqVO);

    @Mapping(target = "modelContent", expression = "java(bean.getModelContent().getBytes())")
    Model convert2Model(ModelUpdateVO bean);


    ModelDTO toDTO(Model model);

    List<ModelDTO> toOutputList(List<Model> demos);

    List<Model> toOutputModels(List<ModelDTO> demos);

    PageResult<ModelPageRespVO> convertPage(PageResult<Model> page);

    ModelDTO createVoToDto(ModelCreateReqVO vo);

    @Mapping(target = "modelContent", expression = "java(content2String(model.getModelContent()))")
    ModelSingleRespVo voToModel(Model model);

    default String content2String(byte[] termContent) {
        if (termContent == null) {
            return null;
        }
        return StringUtils.toEncodedString(termContent, StandardCharsets.UTF_8);
    }

    List<ModelPageRespVO> convert2Vo(List<Model> models);

    List<ModelAllVO> convert2AllVo(List<Model> models);

    PageResult<ModelAllVO> convert2Allpages(PageResult<Model> page);

    PageResult<ModelAllVO> convertPage2(PageResult<Model> page);

    PageResult<ModelAllVO> convertPage21(PageResult<SimpleModel> page);

    List<ModelAllVO> convertList(List<SimpleModel> list);
    List<ModelIdAndNameVO> toIdAndNameList(List<SimpleModel> list);

    PageResult<ModelPageRespVO> convertPage3(PageResult<ModelAllVO> allPageResult);

    List<ModelPageRespVO> convertLsitV1(List<ModelAllVO> list);

    PageResult<ModelStoreRespVO> convertPage5(PageResult<ModelAllVO> allPageResult);

    PageResult<ModelBpmPageRespVO> convert2BpmPage(PageResult<ModelBpmDO> modelBpmDOPageResult);

    PageResult<ModelBpmAllVO> convert2AllVO(PageResult<ModelBpmDO> modelBpmDOPageResult);

    PageResult<ModelBpmPageRespVO> convert2BpmResVO(PageResult<ModelBpmAllVO> allVOPageResult);


    @Mapping(target = "modelContent", expression = "java(vo.getModelContent().getBytes())")
    @Mapping(target = "fileName", expression = "java(vo.getName() + \".pdf\")")
    Model createVoToEntity(ModelUpdateVO vo);

    SimpleModel convertV1(ModelUpdateVO vo);

    List<RecentUseModelVO> ModelToVo(List<Model> models);

    @Mapping(source = "modelId", target = "id")
    RecentUseModelVO ModelToVo2(MyCollectModel models);

    List<RecentUseModelVO> ModelToVo2(List<MyCollectModel> models);

    List<RecentUseModelVO> convertToRecentVO(List<SimpleModel> list);

    List<CommonOfModelAndTemplateBpmProcessRespVO> convertDTO2RespVO(List<BpmProcessRespDTO> bpmProcessRespDTOList);

    ModelCreateReqVO convertWps2Create(SaveModel saveModel);

    List<ModelListApproveRespVO> convertBpmDO2Resp(List<Model> doList);

    SimpleModel convertSimple(Model model);

    List<ModelClientRespVO> listClientDo2Resp(List<ModelClientDO> modelClientDOList);
}
