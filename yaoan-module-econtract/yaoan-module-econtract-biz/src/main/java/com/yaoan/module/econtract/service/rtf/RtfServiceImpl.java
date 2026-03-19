package com.yaoan.module.econtract.service.rtf;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.module.econtract.controller.admin.model.vo.ModelSingleRespVo;
import com.yaoan.module.econtract.controller.admin.model.vo.TermsDetailsVo;
import com.yaoan.module.econtract.controller.admin.rtf.vo.ModelRespVO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.term.ModelTerm;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.ContractTemplateMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.term.ModelTermMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
import com.yaoan.module.econtract.enums.ModelTypeEnum;
import com.yaoan.module.econtract.service.model.ModelService;
import com.yaoan.module.econtract.util.AsposeUtil;
import com.yaoan.module.econtract.util.PdfConvertHtmlUtil;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.EMPTY_DATA_ERROR;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.EMPTY_FIELD_ERROR;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author doujiale
 * @since 2023-09-18
 */
@Service
public class RtfServiceImpl implements RtfService {

    @Resource
    private FileApi fileApi;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private ContractTemplateMapper contractTemplateMapper;
    @Resource
    private ModelService modelService;
    @Resource
    private ModelTermMapper modelTermMapper;
    @Resource
    private TermMapper termMapper;

    @Override
    public String getRtfByFileId(Long id) throws Exception {

        FileDTO fileDTO = fileApi.selectById(id);
        String path = "/Users/Desktop/";
        FileUtil.mkdir(path);

        if (fileDTO.getName().endsWith(".doc") || fileDTO.getName().endsWith(".docx")) {
            ByteArrayInputStream inputStream = IoUtil.toStream(fileApi.getFileContentById(id));
            String tmpPath = path + UUID.randomUUID() + ".html";
            AsposeUtil.docxStream2Html(inputStream, tmpPath);
            String result = FileUtil.readString(tmpPath, Charset.defaultCharset());
            FileUtil.del(tmpPath);
            return result;
        }
        if (fileDTO.getName().endsWith(".pdf")) {
            ByteArrayInputStream inputStream = IoUtil.toStream(fileApi.getFileContentById(id));
            String tmpPath = path + UUID.randomUUID() + ".html";
            PdfConvertHtmlUtil.pdfStream2html(inputStream, tmpPath);
            String result = FileUtil.readString(tmpPath, Charset.defaultCharset());
            FileUtil.del(tmpPath);
            return result;
        }
        return "succeed";
    }

    //    @Override
//    public String getRtfByModelId(String id) throws Exception {
//
//        Model model = modelMapper.selectById(id);
//        if (model == null) {
//            throw exception(EMPTY_DATA_ERROR);
//        }
//        return StringUtils.toEncodedString(model.getModelContent(), StandardCharsets.UTF_8);
//    }
    @Override
    public String getRtfByModelId(String id) throws Exception {

        Model model = modelMapper.selectById(id);
        if (model == null) {
            throw exception(EMPTY_DATA_ERROR);
        }
        if (ModelTypeEnum.FILE_UPLOAD == ModelTypeEnum.getInstance(model.getType()) || ModelTypeEnum.TEMPLATE == ModelTypeEnum.getInstance(model.getType())) {
            return StringUtils.toEncodedString(model.getModelContent(), StandardCharsets.UTF_8);
        }

        ModelSingleRespVo singleRespVo = modelService.getModel(id);
        return singleRespVo.getTerms().stream().map(TermsDetailsVo::getTermContent).collect(Collectors.joining(","));
    }

    @Override
    public String getRtfByTemplateId(String id) throws Exception {

        ContractTemplate template = contractTemplateMapper.selectById(id);
        if (template == null) {
            throw exception(EMPTY_DATA_ERROR);
        }

        if (template.getContent() != null && template.getContent().length > 0) {
            return StringUtils.toEncodedString(template.getContent(), StandardCharsets.UTF_8);
        }

        Long sourceFileId = template.getSourceFileId();
        if (sourceFileId != null) {
            return getRtfByFileId(sourceFileId);
        } else if (template.getContent() != null) {
            return StringUtils.toEncodedString(template.getContent(), StandardCharsets.UTF_8);
        } else {
            throw exception(EMPTY_FIELD_ERROR);
        }

    }
    @Override
    public ModelRespVO getRtfByModelId1(String id) throws Exception {
        ModelRespVO modelRespVO = new ModelRespVO();
        Model model = modelMapper.selectById(id);
        if (model == null) {
            throw exception(EMPTY_DATA_ERROR);
        }
        if (ModelTypeEnum.FILE_UPLOAD == ModelTypeEnum.getInstance(model.getType())) {
            return modelRespVO.setContent(StringUtils.toEncodedString(model.getModelContent(), StandardCharsets.UTF_8));
        }
        //范本方式新增的模板 添加条款信息
        if (ModelTypeEnum.TEMPLATE == ModelTypeEnum.getInstance(model.getType())) {
            List<ModelTerm> modelTerms = modelTermMapper.selectList(ModelTerm::getModelId, id);
            if (CollectionUtil.isNotEmpty(modelTerms)) {
                List<TermsDetailsVo> result = new ArrayList<>();
                List<String> termIds = modelTerms.stream().map(ModelTerm::getTermId).collect(Collectors.toList());
                List<Term> terms = termMapper.selectBatchIds(termIds);
                Map<String, Term> termMap = CollectionUtils.convertMap(terms, Term::getId);
                modelTerms.forEach(item -> {
                    Term termInfo = termMap.get(item.getTermId());
                    if (termInfo != null) {
                        result.add(new TermsDetailsVo()
                                .setId(item.getTermId())
                                .setTermType(termInfo.getTermType())
                                .setTermCode(termInfo.getCode())
                                .setTermNum(item.getTermNum())
                                .setTermName(termInfo.getName())
                                .setTermContent(StringUtils.toEncodedString(termInfo.getTermContent(), StandardCharsets.UTF_8))
                                .setIsRequired(termInfo.getIsRequired())
                                .setShowSort(termInfo.getShowSort())
                                .setTitle(item.getTitle())
                                .setTermKind(termInfo.getTermKind())
                                .setTermKindName(termInfo.getTermKindName())
                                .setTermComment(termInfo.getTermComment())
                                .setEditable(item.getEditable())
                                .setEnableEdit(termInfo.getEnableEdit())
                                .setShowName(termInfo.getShowName()));
                    }
                });
                modelRespVO.setTerms(result);
            }
            return modelRespVO.setContent(StringUtils.toEncodedString(model.getModelContent(), StandardCharsets.UTF_8));
        }

        ModelSingleRespVo singleRespVo = modelService.getModel(id);
        return modelRespVO.setContent(singleRespVo.getTerms().stream().map(TermsDetailsVo::getTermContent).collect(Collectors.joining(",")));
    }
}
