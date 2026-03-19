package com.yaoan.module.econtract.service.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.query.QueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.bpm.activity.BpmActivityApi;
import com.yaoan.module.bpm.api.bpm.activity.dto.BpmProcessRespDTO;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.contract.ContractProcessApi;
import com.yaoan.module.econtract.api.model.dto.ModelDTO;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.CommonOfModelAndTemplateBpmProcessRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.ModelBpmSubmitCreateReqVO;
import com.yaoan.module.econtract.controller.admin.catalog.vo.PurCatalogVO;
import com.yaoan.module.econtract.controller.admin.codegeneration.vo.CodeQueryReqVO;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableConfigRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractToPdfVO;
import com.yaoan.module.econtract.controller.admin.model.vo.*;
import com.yaoan.module.econtract.controller.admin.model.vo.client.ModelClientRespVO;
import com.yaoan.module.econtract.controller.admin.outward.template.vo.ModelApiListReqVO;
import com.yaoan.module.econtract.controller.admin.outward.template.vo.ModelApiListRespVO;
import com.yaoan.module.econtract.convert.category.CategoryConverter;
import com.yaoan.module.econtract.convert.config.SystemConfigDTOConverter;
import com.yaoan.module.econtract.convert.model.ModelConverter;
import com.yaoan.module.econtract.convert.model.term.ModelTermConverter;
import com.yaoan.module.econtract.convert.parammodel.ParamModelConverter;
import com.yaoan.module.econtract.dal.dataobject.bpm.model.ModelBpmDO;
import com.yaoan.module.econtract.dal.dataobject.catalog.ModelCatalogDo;
import com.yaoan.module.econtract.dal.dataobject.catalog.PurCatalogDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.TemplateQuoteDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.model.ModelClientDO;
import com.yaoan.module.econtract.dal.dataobject.model.ModelPic;
import com.yaoan.module.econtract.dal.dataobject.model.SimpleModel;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ClientModelCategory;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ModelCategory;
import com.yaoan.module.econtract.dal.dataobject.paramModel.ParamModel;
import com.yaoan.module.econtract.dal.dataobject.term.ModelTerm;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.mysql.bpm.model.ModelBpmMapper;
import com.yaoan.module.econtract.dal.mysql.catalog.ModelCatalogMapper;
import com.yaoan.module.econtract.dal.mysql.catalog.PurCatalogMapper;
import com.yaoan.module.econtract.dal.mysql.category.TemplateCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.ContractTemplateMapper;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.TemplateQuoteMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelClientMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelPicMapper;
import com.yaoan.module.econtract.dal.mysql.model.SimpleModelMapper;
import com.yaoan.module.econtract.dal.mysql.modelcategory.ClientModelCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.modelcategory.ModelCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.term.ModelTermMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.common.IfEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.model.ModelEffectTpeEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.enums.templatecategory.RootTemplateCategoryEnums;
import com.yaoan.module.econtract.enums.templatecategory.TemplateCategoryEnums;
import com.yaoan.module.econtract.service.bpm.model.ModelBpmService;
import com.yaoan.module.econtract.service.code.CodeRuleService;
import com.yaoan.module.econtract.service.contract.ContractService;
import com.yaoan.module.econtract.service.modelcategory.ModelCategoryService;
import com.yaoan.module.econtract.service.paramModel.ParamModelMapper;
import com.yaoan.module.econtract.service.paramModel.ParamModelService;
import com.yaoan.module.econtract.util.*;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.config.dto.FlowableConfigRespDTO;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.api.dept.dto.CompanyRespDTO;
import com.yaoan.module.system.api.region.RegionApi;
import com.yaoan.module.system.api.region.dto.RegionDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;
import static com.yaoan.module.econtract.enums.ModuleConstants.MODULE_MODEL;
import static com.yaoan.module.econtract.enums.StatusConstants.*;
import static com.yaoan.module.econtract.enums.model.ModelEffectTpeEnums.TIME_LIMITED;
import static com.yaoan.module.system.enums.config.SystemConfigKeyEnums.IF_NEED_MODEL_CATEGORY;

/**
 * @description: * 条款方法：只传富文本 ：持久化 remote_file_id 和 rtf_pdf_fileId
 * * 上传文件方法：传富文本和Multipart文件，持久化 remote_file_id 和 rtf_pdf_fileId
 * * 范本方法：只传富文本，无Multipart文件 RtfPdfFileId :持久化 rtf_file_id
 * * WPS方法：只传一个wps编辑保存后的文件，持久化 remote_file_id，将wps文件转成pdf存到 rtf_pdf_fileId,给前端展示用。
 * @author: Pele
 * @date: 2023/8/3 15:39
 */
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {

    static final String PARAM_ID = "paramId";
    static final String LOCATION = "location";
    static final String PARAM_NUM = "paramNum";
    static final String TAG_ID = "tagId";
    static final Integer MODEL_TYPE = 2;
    static final Long WPS_USER = 125L;

    @Value("${feign.client.contract.client_id}")
    private String clientId;
    @Value("${feign.client.contract.client_secret}")
    private String clientSecret;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private SimpleModelMapper simpleModelMapper;
    @Resource
    private ModelTermMapper modelTermMapper;
    @Resource
    private TermMapper termMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private ParamModelService paramModelService;
    @Resource
    private ModelPicMapper modelPicMapper;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private ModelCategoryMapper modelCategoryMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private ContractTemplateMapper templateMapper;
    @Resource
    private ModelBpmMapper modelBpmMapper;
    @Resource
    private BpmActivityApi bpmActivityApi;
    @Resource
    private ModelBpmService modelBpmService;

    @Resource
    private SystemConfigApi systemConfigApi;

    @Resource
    private ClientModelCategoryMapper clientModelCategoryMapper;
    @Resource
    private ContractService contractService;
    @Resource
    private CategoryConverter categoryConverter;
    @Resource
    private TemplateCategoryMapper templateCategoryMapper;
    @Resource
    private ModelCategoryService modelCategoryService;
    @Resource
    private ParamModelMapper paramModelMapper;
    @Resource
    private TemplateQuoteMapper templateQuoteMapper;
    //    @Resource
//    private OrganizationApi organizationApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private RegionApi regionApi;
    @Resource
    private ModelCatalogMapper modelCatalogMapper;
    @Resource
    private PurCatalogMapper purCatalogMapper;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private ContractMapper contractMapper;

    @Resource
    private ContractProcessApi contractProcessApi;
    @Resource
    private CodeRuleService codeRuleService;

    /**
     * 条款方法：前端只传富文本 ：后端转成pdf文件， type=1
     * 上传文件方法：传富文本和文件名，type=2
     * 范本方法：只传富文本，templateId，无Multipart文件 和文件名， type=3
     * （目前停用）WPS方法：只传一个wps编辑保存后的文件id --remoteFileId和Multipart文件，没有富文本，后端将wps文件转成pdf存到 rtf_pdf_fileId,给前端展示用。 type=4
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ModelIdCodeRespVO insertModelByUpload(@Validated ModelCreateReqVO vo) throws Exception {
        //默认不需要模板分类
        String value = systemConfigApi.getConfigByKey(IF_NEED_MODEL_CATEGORY.getKey());
        Boolean needCategory = IfEnums.YES.getCode().equals(value);

        ModelIdCodeRespVO ret = new ModelIdCodeRespVO();
        //检验通用类模板分类是否都存在模板不存在不允许新增
        Integer categoryId = vo.getCategoryId();
        if (!(RootTemplateCategoryEnums.SERVICE.getId().equals(categoryId) || RootTemplateCategoryEnums.GOODS.getId().equals(categoryId) || RootTemplateCategoryEnums.ENGINEER.getId().equals(categoryId))) {
            //   modelCategoryService.checkModelApprove();
        }
        LoginUser loginUser = getLoginUser();
        if (StringUtils.isEmpty(vo.getId())) {
            //新增
            //根据合同类型，自动生成模板编号
            String modelCode = codeRuleService.generate4ContractType(new CodeQueryReqVO().setContractType(vo.getContractType()).setType("model"));
            checkCode(vo, 0);
            vo.setCode(modelCode);
            ret.setCode(modelCode);

//            DBExistUtil.isExist(null, vo.getName(), vo.getCode(), null, simpleModelMapper);
            if (needCategory) {
                //校验模板分类下，是否已存在模板。
                //政府采购模板分类下，模板数量限制
                getModelCount(vo);
            }
//            String value = systemConfigApi.getConfigByKey(MODEL_MAX_COUNT_PER_CATEGORY.getKey());
//            if (StringUtils.isBlank(value)) {
//                throw exception(EMPTY_DATA_ERROR);
//            }
//            Long maxCount = Long.valueOf(value);
//            Long count = simpleModelMapper.selectCount(new LambdaQueryWrapperX<SimpleModel>()
//                    .eq(SimpleModel::getCategoryId, vo.getCategoryId())
//                    .eq(SimpleModel::getApproveStatus, BpmProcessInstanceResultEnum.APPROVE.getResult())
//            );
//            if (maxCount < count) {
//                throw exception(MODEL_CATEGORY_EXISTS_MODELS_ERROR, value);
//            }
        } else {
            // 编辑时不修改编号
//            checkCodeAndName4Update(vo);
//            if (needCategory) {
//                getModelCount(vo);
//            }
        }

        //如果是编辑模版
        if (StringUtils.isNotBlank(vo.getId())) {
            Model model = modelMapper.selectOne(new LambdaQueryWrapperX<Model>().eqIfPresent(Model::getId, vo.getId()).select(Model.class, info -> !"model_content".equals(info.getColumn())));
            if (ObjectUtil.isEmpty(model)) {
                throw exception(ErrorCodeConstants.GOMall_Query_Error, "此模板不存在，请检查模板id");
            }
            //校验模版是否可以编辑
            if (!isModifiable2(model.getApproveStatus())) {
                throw exception(ErrorCodeConstants.MODEL_STATUS_NOT_MODIFIABLE);
            }
            //删除参数与模板的关联关系
            paramModelMapper.delete(new LambdaQueryWrapper<ParamModel>().eq(ParamModel::getModelId, vo.getId()));

        }
        //如果是新增模版
        //将日期字符串转化成Date
        if (StringUtils.isNotBlank(vo.getEffectStartTimeReciever())) {
            vo.setEffectStartTime(EcontractUtil.getDateFromSimpleStr(vo.getEffectStartTimeReciever()));
        }
        if (StringUtils.isNotBlank(vo.getEffectEndTimeReciever())) {
            vo.setEffectEndTime(EcontractUtil.getDateFromSimpleStr(vo.getEffectEndTimeReciever()));
        }
        //判断合同是否在有效期
//        if (vo.getTimeEffectModel() == 0 && vo.getEffectStartTime() != null && vo.getEffectEndTime() != null) {
//            if (vo.getEffectStartTime().isAfter(vo.getEffectEndTime())) {
//                throw new Exception("开始时间不能大于结束时间");
//            }
//            //当前时间在开始时间和结束时间之间
//            LocalDateTime currentTime = LocalDateTime.now();
//            if (currentTime.isAfter(vo.getEffectStartTime()) && currentTime.isBefore(vo.getEffectEndTime())) {
//                vo.setEffective(1);
//            } else {
//                vo.setEffective(0);
//            }
//        } else {
//            vo.setEffective(1);
//        }
        // 如果是条款新增的方式(编辑也走这)
        if (ModelTypeEnum.TERM_ADD == ModelTypeEnum.getInstance(vo.getType())) {
            ret.setId(termAddProcessor(vo));
            return ret;
        }
        if (ModelTypeEnum.TEMPLATE_UPLOAD == ModelTypeEnum.getInstance(vo.getType())) {
            ret.setId(insertModelByUploadTemplate(vo));
            return ret;
        }
        // 如果是WPS新增的方式
        if (ModelTypeEnum.WPS_UPLOAD == ModelTypeEnum.getInstance(vo.getType())) {
            ret.setId(insertModelByWps(vo));
            return ret;
        }

        //范本方式-新增模板
        //范本新增分为wps和文件 如果wps没有富文本
        Model model = ModelConverter.INSTANCE.toEntity(vo);
        if (ModelTypeEnum.TEMPLATE == ModelTypeEnum.getInstance(vo.getType()) && vo.getUploadType() != null) {
            if (vo.getUploadType() == 0) {
                //WPS
                Long pdfId = contractService.toPdf(new ContractToPdfVO().setName(vo.getName()).setFileAddId(vo.getRemoteFileId()), FileUploadPathEnum.MODEL);
                model.setRtfPdfFileId(pdfId);
                model.setRemoteFileId(vo.getRemoteFileId());
            } else { //富文本

                Long pdfId = contractService.toPdf(new ContractToPdfVO().setName(vo.getName()).setContent(vo.getModelContent()), FileUploadPathEnum.MODEL);
                model.setRtfPdfFileId(pdfId);

            }
        }
        //政府采购模板特殊处理，赋值orgId
        if (model.getIsGov() == 1) {
            // LoginUser loginUser = getLoginUser();
            model.setOrgId(loginUser.getOrgId());
        }
        //赋值 时效模式
        enhanceModelEffect(vo);
        // 上传文件方式-新增模板
        // 之前要传file，现在只用富文本转成pdf
        if (ModelTypeEnum.FILE_UPLOAD == ModelTypeEnum.getInstance(vo.getType())) {
            //同步文件信息（上传文件方式）
            if (ObjectUtil.isNotNull(vo.getModelContent())) {
                Long rtf_fileId = rtfContentProcessor(vo, vo.getModelContent());
                model.setRtfPdfFileId(rtf_fileId);
                model.setModelContent(vo.getModelContent().getBytes());
            }
        }
        MultipartFile file = vo.getFile();
        if (ObjectUtil.isNotEmpty(file)) {
            model = uploadModelFileAndPics(file, model);
        }

        //范本方法-新增模板 （范本不传Multipart文件）
        boolean result = false;
        enhanceModelDO(model);
        TemplateQuoteDO templateQuoteDO = new TemplateQuoteDO();
        if (StringUtils.isNotBlank(vo.getId())) {
            //修改模板
            result = modelMapper.updateById(model) == INSERT_SUCCESS;
            //修改范本引用情况表
            if (ModelTypeEnum.TEMPLATE == ModelTypeEnum.getInstance(vo.getType())) {
                ContractTemplate contractTemplate = templateMapper.selectById(vo.getTemplateId());
                templateQuoteDO = getTemplateQuoteDO(model, contractTemplate == null ? null : contractTemplate.getCode(), contractTemplate == null ? null : contractTemplate.getVersion());
                templateQuoteMapper.update(templateQuoteDO, new LambdaQueryWrapperX<TemplateQuoteDO>().eqIfPresent(TemplateQuoteDO::getModelId, vo.getId()));
            }
        } else {
            result = modelMapper.insert(model) == INSERT_SUCCESS;
            //添加到范本引用情况表
            if (ModelTypeEnum.TEMPLATE == ModelTypeEnum.getInstance(vo.getType())) {
                ContractTemplate contractTemplate = templateMapper.selectById(vo.getTemplateId());
                templateQuoteDO = getTemplateQuoteDO(model, contractTemplate == null ? null : contractTemplate.getCode(), contractTemplate == null ? null : contractTemplate.getVersion());
                //添加到引用情况表
                templateQuoteMapper.insert(templateQuoteDO);
            }
        }
        if (ObjectUtil.isNotEmpty(vo.getParamModelVoList())) {
            //绑定参数模板
            List<ParamModel> params = ParamModelConverter.INSTANCE.toEntityList(vo.getParamModelVoList());
            Model finalModel = model;
            params.stream().forEach(paramModel -> paramModel.setModelId(finalModel.getId()));
            paramModelMapper.insertBatch(params);
        }

        //新增模板和条款的关联关系
        if (CollectionUtil.isNotEmpty(vo.getTerms())) {
            modelTermMapper.delete(new LambdaQueryWrapper<ModelTerm>().eq(ModelTerm::getModelId, model.getId()));
            List<ModelTerm> convert = ModelTermConverter.INSTANCE.convert(vo.getTerms(), model);
            modelTermMapper.insertBatch(convert);
        }
//        //添加品目联系
        if (result) {
            addModelPurCatalog(model, vo.getPurCatalogGuid());
        }
        ret.setId(model.getId());
        return result ? ret : new ModelIdCodeRespVO().setId("保存模板异常");
    }

    private void getModelCount(ModelCreateReqVO vo) {
        String value = systemConfigApi.getConfigByKey(SystemConfigKeyEnums.MODEL_COUNT_CONTROL.getKey());
        if (StringUtils.isEmpty(value)) {
            throw exception(ErrorCodeConstants.SYSTEM_ERROR, "未配置模板数量");
        }
        //按照货物、服务、工程、非政采的顺序拆分开
        String[] split = value.split(",");
        long goodsCount = Long.parseLong(split[0]);
        long serviceCount = Long.parseLong(split[1]);
        long projectCount = Long.parseLong(split[2]);
        long otherCount = Long.parseLong(split[3]);
        if (ObjectUtil.isNotEmpty(vo.getIsGov()) && vo.getIsGov() == 1) {
            ModelCategory modelCategory = modelCategoryMapper.selectById(vo.getCategoryId());
            if (ObjectUtil.isNotEmpty(modelCategory)) {
                //货物
                if (modelCategory.getCode().equals(TemplateCategoryEnums.THW.getCode())) {
                    compareCount(vo, goodsCount, TemplateCategoryEnums.THW.getKey());
                }
                //服务
                if (modelCategory.getCode().equals(TemplateCategoryEnums.TFW.getCode())) {
                    //获取服务类模板的数量
                    compareCount(vo, serviceCount, TemplateCategoryEnums.TFW.getKey());
                }
                //工程
                if (modelCategory.getCode().equals(TemplateCategoryEnums.TGC.getCode())) {
                    //获取工程类模板的数量
                    compareCount(vo, projectCount, TemplateCategoryEnums.TGC.getKey());
                }
            }
        } else {
            //非政采
            Long count = simpleModelMapper.selectCount(new LambdaQueryWrapperX<SimpleModel>()
                    .eq(SimpleModel::getIsGov, 0));
            if (otherCount <= count) {
                throw exception(ErrorCodeConstants.SYSTEM_ERROR, "非政府采购最多可创建模板" + otherCount + "个");
            }
        }
    }

    private void compareCount(ModelCreateReqVO vo, long maxCount, String name) {
        //获取政府采购模板的数量
        Long count = simpleModelMapper.selectCount(new LambdaQueryWrapperX<SimpleModel>()
                .eq(SimpleModel::getCategoryId, vo.getCategoryId()).eq(SimpleModel::getIsGov, 1));
        if (maxCount <= count) {
            throw exception(ErrorCodeConstants.SYSTEM_ERROR, name + "最多可创建模板" + maxCount + "个");
        }
    }

    /**
     * 模板关联品目信息
     */
    private void addModelPurCatalog(Model model, List<String> purCatalogGuid) {
        LoginUser loginUser = getLoginUser();
        String orgId;
        if (loginUser.getType() != null && loginUser.getType() == 1) {
            orgId = loginUser.getOrgId();
        } else if (loginUser.getType() != null && loginUser.getType() == 2) {
            orgId = loginUser.getSupplyId();
        } else {
            orgId = "";
        }
        String regionCode = loginUser.getRegionCode();
        //如果是修改模板
        List<ModelCatalogDo> catalogDoList = modelCatalogMapper.selectList(new LambdaQueryWrapperX<ModelCatalogDo>()
                .eq(ModelCatalogDo::getModelId, model.getId())
                .eq(ModelCatalogDo::getOrgId, orgId));
        if (!catalogDoList.isEmpty()) {
            List<String> guidList = catalogDoList.stream().map(ModelCatalogDo::getCatalogGuid).collect(Collectors.toList());
            List<String> ids = catalogDoList.stream().map(ModelCatalogDo::getId).collect(Collectors.toList());
            if (ObjectUtils.isEmpty(purCatalogGuid)) {
                modelCatalogMapper.deleteBatchIds(ids);
                return;
            }
            if (guidList.containsAll(purCatalogGuid) && purCatalogGuid.containsAll(guidList)) {
                return;
            }
            //不相同则删除原来的
            if (!ids.isEmpty()) {
                modelCatalogMapper.deleteBatchIds(ids);
            }
        }
        if (ObjectUtils.isEmpty(purCatalogGuid)) {
            return;
        }
        //是否存在改品目  品目信息
        List<PurCatalogDO> purCatalogDOList = purCatalogMapper.selectList(new LambdaQueryWrapperX<PurCatalogDO>().in(PurCatalogDO::getPurCatalogGuid, purCatalogGuid));
        if (CollectionUtil.isEmpty(purCatalogDOList)) {
            throw exception(EMPTY_DATA_ERROR);
        }
        //根据配置区划文件判断该组织的品目是否达到上限

        //从当前登录人所属最小区划编码开始向上查询
        //如果查出来的code为空说明为最顶级
        String maxCatalogNum = systemConfigApi.getConfigByKey(regionCode + "_max_catalog_num");
        int i = 0;
        while (StringUtils.isEmpty(maxCatalogNum)) {
            i++;
            regionCode = regionApi.getParentRegionByCode(regionCode);
            maxCatalogNum = systemConfigApi.getConfigByKey(regionCode + "_max_catalog_num");
            if (i == 5) {
                maxCatalogNum = systemConfigApi.getConfigByKey("model_max_count_per_category");
                break;
            }
        }
        //查询该组织是否达到上限
        List<ModelCatalogDo> modelCatalogDos = modelCatalogMapper.selectList(new MPJLambdaWrapper<ModelCatalogDo>()
                .select(ModelCatalogDo::getCatalogGuid)
                .select("count(*) as kind")
                .eq(ModelCatalogDo::getOrgId, orgId)
                .in(ModelCatalogDo::getCatalogGuid, purCatalogGuid)
                .groupBy(ModelCatalogDo::getCatalogGuid)
        );
        Optional<Long> count = modelCatalogDos.stream().map(ModelCatalogDo::getKind).max(Comparator.naturalOrder()).map(Long::parseLong);
        if (count.isPresent() && count.get() >= Long.parseLong(maxCatalogNum)) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "品目模板数量已达到上限，可对现有品目模板进行删除或编辑，如新增更多品目模板，或联系系统管理员。");
        }
        Map<String, PurCatalogDO> catalogDOMap = CollectionUtils.convertMap(purCatalogDOList, PurCatalogDO::getPurCatalogGuid);
        //确定平台  查找顶级模板分类id
//        ModelCategory modelCategory = modelCategoryMapper.selectById(categoryId);
        ClientModelCategory clientModelCategory = clientModelCategoryMapper.selectOne(new MPJLambdaWrapper<ClientModelCategory>()
                .select(ClientModelCategory::getClientId)
                .leftJoin(ModelCategory.class, ModelCategory::getParentId, ClientModelCategory::getCategoryId)
                .eq(ModelCategory::getId, model.getCategoryId())
                .last("limit 1")
        );
//        if (ObjectUtil.isEmpty(clientModelCategory)) {
//            throw exception(ErrorCodeConstants.MODEL_CATEGORY_EMPTY_ERROR);
//        }
        ArrayList<ModelCatalogDo> list = new ArrayList<>();
        String platform = ObjectUtil.isEmpty(clientModelCategory) ? "ALL" : clientModelCategory.getClientId();
        for (String d : purCatalogGuid) {
            ModelCatalogDo modelCatalogDo = new ModelCatalogDo();
            PurCatalogDO purCatalogDO = catalogDOMap.get(d);
            modelCatalogDo.setCatalogGuid(d);
            modelCatalogDo.setModelId(model.getId());
            modelCatalogDo.setModelName(model.getName());
            modelCatalogDo.setOrgId(orgId);
            modelCatalogDo.setCatalogName(purCatalogDO.getPurCatalogName());
            modelCatalogDo.setCatalogCode(purCatalogDO.getPurCatalogCode());
            modelCatalogDo.setPurCatalogType(purCatalogDO.getPurCatalogType());
            modelCatalogDo.setKind(purCatalogDO.getKind());
            modelCatalogDo.setPlatform(platform);
            list.add(modelCatalogDo);
        }
        modelCatalogMapper.insertBatch(list);
    }

    /**
     * 是否可以改动
     * (如果含有处于审批通过或者处理中的记录，则返回false)
     */
    private Boolean isModifiable2(Integer approveStatus) {
        boolean result = true;
        StatusEnums instanceResultEnum = StatusEnums.getInstance(approveStatus);
        if (ObjectUtil.isNotNull(instanceResultEnum)) {
            switch (instanceResultEnum) {
                case APPROVING:
                case APPROVED:
                    result = false;
                    break;
                default:
                    return result;
            }
        }

        return result;
    }

    @NotNull
    private TemplateQuoteDO getTemplateQuoteDO(Model model, String templateCode, Double version) {
        TemplateQuoteDO templateQuoteDO = new TemplateQuoteDO();
        templateQuoteDO.setModelId(model == null ? null : model.getId());
        templateQuoteDO.setModelCode(model == null ? null : model.getCode());
        templateQuoteDO.setTemplateCode(templateCode);
        templateQuoteDO.setModelName(model == null ? null : model.getName());
        templateQuoteDO.setStatus(model == null ? null : model.getStatus());
        templateQuoteDO.setModelVersion(model == null ? null : model.getVersion());
        templateQuoteDO.setTemplateVersion(version);
        //获取当前单位名称
//        if (model.getOrgId() != null) {
//            OrganizationDTO organization = organizationApi.getOrganization(model.getOrgId());
//            templateQuoteDO.setCompanyName(ObjectUtil.isNotNull(organization) ? organization.getName() : "");
//        }
        return templateQuoteDO;
    }

    private void enhanceModelDO(Model model) {

        LoginUser loginUser = getLoginUser();
        AdminUserRespDTO user = adminUserApi.getUser(getLoginUserId());
        model.setCreatorName(user != null ? user.getNickname() : "");
        model.setCompanyId(user != null ? user.getCompanyId() : null);
        if (ObjectUtil.isNotEmpty(user) && user.getCompanyId() != null) {
            List<Long> companyIds = new ArrayList<>();
            companyIds.add(user.getCompanyId());
            List<CompanyRespDTO> companyRespDTOList = companyApi.getCompanyByIds(companyIds);
            if (companyRespDTOList.size() > 0) {
                model.setCompanyName(companyRespDTOList.get(0).getName());
            }
        }
        //先不使用用户区划覆盖界面选择的区划，否则页面选择区划没有意义
        //model.setRegionCode(loginUser != null ? loginUser.getRegionCode() : "");
        if (StringUtils.isNotBlank(model.getRegionCode())) {
            RegionDTO region = regionApi.getRegionByCode(model.getRegionCode());
            if (region != null) {
                model.setRegionName(region.getRegionName());
            }
        }
//        if (UserTypeEnums.PURCHASER_ORGANIZATION.getCode().equals(loginUser.getType())) {
//            model.setOrgId(loginUser.getOrgId());
//        }
    }

    /**
     * 上传文件并解析出缩略图和页数
     */
    private Model uploadModelFileAndPics(MultipartFile file, Model model) throws IOException {
        String fileName = file.getOriginalFilename();
        //将pdf文件和生成的图片统一都放入远端的uuid目录中。
        String folderId = "MODEL-" + IdUtil.fastSimpleUUID();
        String pdfFileName = "";
        //转pdf保存到pdf根目录
        String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
        String tempWordFile = localFolderPath + "/" + fileName;
        String pdfPath = localFolderPath;
        String pdfPicPath = localFolderPath + "/pics";
        int pageCount = 0;
        FileUtil.mkdir(pdfPicPath);
        //（1)pdf暂存本地目录（本地temp目录。且子目录pics下存储图片）
        InputStream ins = file.getInputStream();
        File newFile = new File(tempWordFile);
        //将输入流传给临时文件，不存在则会创建。存在则会覆盖。
        FileUtils.copyInputStreamToFile(ins, newFile);
        //如果文件格式是pdf,就不用转换
        if (StringUtils.equals(SUFFIX_PDF, FileNameUtil.extName(fileName))) {
            pdfFileName = file.getOriginalFilename();
            pdfPath = pdfPath + "/" + fileName;
        } else if (StringUtils.equals(SUFFIX_DOC, FileNameUtil.extName(fileName)) || StringUtils.equals(SUFFIX_DOCX, FileNameUtil.extName(fileName))) {
            assert fileName != null;
            pdfFileName = EcontractUtil.exchangeName2pdf(fileName);
            pdfPath = pdfPath + "/" + pdfFileName;
            //将流式文件转换成版式文件,并得到页数
//                YhAgentUtil.officeToPDF(tempWordFile, pdfPath);
//                AsposeUtil.docx2Pdf(tempWordFile, pdfPath); //红色水印Evaluation Only.
//                pageCount = EcontractUtil.getFilePageFromPDF(pdfPath);
        }
        File pdfFile = new File(pdfPath);
        FileInputStream fileInputStream = new FileInputStream(pdfFile);
        MultipartFile multipartPdfFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(), null, fileInputStream);
        //将pdf根目录上传
        String uploadPath = EcontractUtil.getRemoteFolderPath(MODULE_MODEL, folderId);
        Long uploadId = fileApi.uploadFile(pdfFileName, uploadPath + "/" + pdfFileName, IoUtil.readBytes(multipartPdfFile.getInputStream()), pageCount);
        //存文件id
        model.setRemoteFileId(uploadId);
        return model;
    }

    /**
     * 可重试三次
     */
    private void checkCode(ModelCreateReqVO vo, int depth) {
        // 最大递归深度3次
        if (3 == depth) {
            return;
        }
        String code = vo.getCode();
        //第三次，可跨一次
        Double version = vo.getVersion();
        Long count = simpleModelMapper.selectCount(new LambdaQueryWrapperX<SimpleModel>()
                .eq(SimpleModel::getCode, code)
                .eq(SimpleModel::getVersion, version));

        if (0 < count) {
            // 编号重复，重新生成
            String modelCode = codeRuleService.generate4ContractType(new CodeQueryReqVO()
                    .setContractType(vo.getContractType())
                    .setType("model"));
            vo.setCode(modelCode);

            // 如果编号重复，则重新生成，递归调用时增加深度
            checkCode(vo, depth + 1);
        }
    }

    /**
     * 一次增加十个顺位
     */
    private String biggerModelCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        // 提取最后4位数字
        String lastFourDigits = code.substring(code.length() - 4);

        // 将字符串转换为整数并增加10
        int number = Integer.parseInt(lastFourDigits);
        number += 10;

        // 将结果转换回字符串
        String newCode = code.substring(0, code.length() - 4) + String.format("%04d", number);
        return newCode;

    }

    /**
     * 只重新生成一次
     */
    private void checkCode(ModelCreateReqVO vo, boolean isRegenerated) {
        String code = vo.getCode();
        Double version = vo.getVersion();
        Long count = simpleModelMapper.selectCount(new LambdaQueryWrapperX<SimpleModel>()
                .eq(SimpleModel::getCode, code)
                .eq(SimpleModel::getVersion, version));

        if (0 < count) {
            // 编号重复，重新生成
            String modelCode = codeRuleService.generate4ContractType(new CodeQueryReqVO()
                    .setContractType(vo.getContractType())
                    .setType("model"));
            vo.setCode(modelCode);

            // 如果已经生成过新编号，则不再递归
            if (!isRegenerated) {
                // 如果没有重新生成过编号，则递归检查
                checkCode(vo, true);  // 设置标志位为true，表示已经生成过新的编码
            }
        }
    }


    private void checkCodeAndName4Update(ModelCreateReqVO vo) {
        //校验编号
        checkCode(vo, 0);
        Long count = 0L;
        count = simpleModelMapper.selectCount(new LambdaQueryWrapperX<SimpleModel>().eq(SimpleModel::getName, vo.getName())
                .ne(SimpleModel::getId, vo.getId()));
        if (1 < count) {
            throw exception(NAME_EXISTS);
        }
    }

    /**
     * 模板库-列表
     */
    @Override
    public PageResult<ModelStoreRespVO> getAllModelPage(ModelListReqVO vo) throws Exception {
        String searchText = vo.getSearchText();
        List<AdminUserRespDTO> nickUsers = new ArrayList<AdminUserRespDTO>();
        List<Long> nickUserIds = new ArrayList<Long>();
        List<String> userIdsAsString = new ArrayList<String>();
        if (StringUtils.isNotBlank(searchText)) {
            nickUsers = userApi.getUserListLikeNickname(searchText);
            if (CollectionUtil.isNotEmpty(nickUsers)) {
                //将昵称模糊查询选中的userId组成List
                nickUserIds = nickUsers.stream().map(AdminUserRespDTO::getId).collect(Collectors.toList());
            }
            //将Long转为String
            userIdsAsString = nickUserIds.stream().map(Object::toString).collect(Collectors.toList());

        }

        PageResult<SimpleModel> page;
        if (ObjectUtil.isNotEmpty(vo.getIsDraft()) && vo.getIsDraft() == 1) {
            //草拟合同模板选择列表
            LambdaQueryWrapper<SimpleModel> wrapperX = new LambdaQueryWrapper<SimpleModel>().eq(SimpleModel::getEffectStatus, 1).eq(SimpleModel::getEffective, 1).eq(SimpleModel::getApproveStatus, 2).orderByDesc(SimpleModel::getUpdateTime).orderByAsc(SimpleModel::getCode);
            if (ObjectUtil.isNotEmpty(vo.getName())) {
                wrapperX.like(SimpleModel::getName, vo.getName());
            }
            wrapperX.eq(SimpleModel::getEffectStatus,IfNumEnums.YES.getCode());
            //模板分类的树状图搜索条件
            wrapperX = enhanceWrapperByCategoryIds(wrapperX, vo);

            page = simpleModelMapper.selectPage(vo, wrapperX);
        } else {
            String subQuery = "SELECT code, approve_status, MAX(version) AS max_version FROM ecms_model GROUP BY code, approve_status";

            // 构建主查询，连接子查询并筛选出每个code分组中，再按照approve_status分组的最大version的记录
            LambdaQueryWrapper<SimpleModel> wrapperX = new LambdaQueryWrapper<SimpleModel>().inSql(SimpleModel::getId, "SELECT id FROM ecms_model WHERE (code, approve_status, version) IN (" + subQuery + ") AND approve_status = 2").orderByDesc(SimpleModel::getCreateTime);
            //创建时间
            if (vo.getStartCreateTime() != null && vo.getEndCreateTime() != null) {
                wrapperX.between(SimpleModel::getCreateTime, vo.getStartCreateTime(), vo.getEndCreateTime());
            }
//            //模板分类
//            if (vo.getCategoryId() != null) {
//                wrapperX.eq(SimpleModel::getCategoryId, vo.getCategoryId());
//            }
            //时效模式
            if (vo.getTimeEffectModel() != null) {
                wrapperX.eq(SimpleModel::getTimeEffectModel, vo.getTimeEffectModel());
            }
            //合同类型
            if (vo.getContractType() != null) {
                wrapperX.eq(SimpleModel::getContractType, vo.getContractType());
            }
            //模板类型
            if (vo.getType() != null) {
                wrapperX.eq(SimpleModel::getType, vo.getType());
            }
            //审批时间
            if (vo.getStartApproveTime() != null && vo.getEndApproveTime() != null) {
                wrapperX.between(SimpleModel::getApproveTime, vo.getStartApproveTime(), vo.getEndApproveTime());
            }
            //合同起草，选择模板参数 模板有效
            if (vo.getEffective() != null) {
                wrapperX.eq(SimpleModel::getEffective, vo.getEffective());
            }
            //启用
            if (vo.getEffectStatus() != null) {
                wrapperX.eq(SimpleModel::getEffectStatus, vo.getEffectStatus());
            }

            //模糊查询的并集
            if (CollectionUtil.isNotEmpty(userIdsAsString)) {
                List<String> finalUserIdsAsString = userIdsAsString;
                wrapperX.and(w -> w.or().like(SimpleModel::getName, vo.getSearchText()).or().in(SimpleModel::getCreator, finalUserIdsAsString));
            } else {
                //如果userIdsAsString为空，说明没有匹配的用户，单查模板名称就可以了
                if (StringUtils.isNotEmpty(searchText)) {
                    wrapperX.like(SimpleModel::getName, searchText);
                }
            }
            //编码
            if (vo.getCode() != null) {
                wrapperX.like(SimpleModel::getCode, vo.getCode());
            }
            //是否失效
            if (vo.getEffective() != null) {
                wrapperX.eq(SimpleModel::getEffective, vo.getEffective());
            }
            //发布时间
            if (vo.getStartPublishTime() != null && vo.getEndPublishTime() != null) {
                wrapperX.between(SimpleModel::getApproveResultTime, vo.getStartPublishTime(), vo.getEndPublishTime());
            }
            //区划编号
            if (StringUtils.isNotBlank(vo.getRegionCode())) {
                wrapperX.eq(SimpleModel::getRegionCode, vo.getRegionCode());
            }


            // 查询政采类合同及其子类合同id
            if (IfNumEnums.NO.getCode().equals(vo.getIsGov())) {
                List<ContractType> zc = contractTypeMapper.selectList(ContractType::getCode, "ZC");
                if (CollectionUtil.isNotEmpty(zc)){
                    List<String> types = new ArrayList();
                    List<String> zcIds = zc.stream().map(ContractType::getId).collect(Collectors.toList());
                    selectAllChildTypes(zcIds, types);
                    wrapperX.notIn(SimpleModel::getContractType, types);
                }
            }

            //模板分类的树状图搜索条件
            wrapperX = enhanceWrapperByCategoryIds(wrapperX, vo);
            wrapperX.eq(SimpleModel::getEffectStatus, IfNumEnums.YES.getCode());
            page = simpleModelMapper.selectPage(vo, wrapperX);

        }
        if (CollectionUtil.isEmpty(page.getList())) {
            return new PageResult<ModelStoreRespVO>().setList(Collections.emptyList()).setTotal(page.getTotal());
        }

        PageResult<ModelAllVO> allPageResult = ModelConverter.INSTANCE.convertPage21(page);
        for (ModelAllVO allVO : allPageResult.getList()) {
            allVO.setEffectivePeriod(allVO.getEffectStartTime() + " —— " + allVO.getEffectEndTime());
        }
        PageResult<ModelStoreRespVO> pageResult = ModelConverter.INSTANCE.convertPage5(allPageResult);
        List<ModelStoreRespVO> vos = pageResult.getList();
        // 查询用户列表
        List<AdminUserRespDTO> userList = userApi.getUserList();
        Map<Long, AdminUserRespDTO> userMap = userList.stream().collect(Collectors.toMap(AdminUserRespDTO::getId, Function.identity()));

        // 查询模板分类列表
        List<ModelCategory> modelCategoryList = modelCategoryMapper.selectList();
        Map<Integer, ModelCategory> modelCategoryMap = modelCategoryList.stream().collect(Collectors.toMap(ModelCategory::getId, Function.identity()));

        // 查询合同类型列表
        List<ContractType> contractTypeList = contractTypeMapper.selectList();
        Map<String, ContractType> contractTypeMap = contractTypeList.stream().collect(Collectors.toMap(ContractType::getId, Function.identity()));

        // 查询模板列表
        List<SimpleModel> modelList = simpleModelMapper.selectList();
        Map<String, SimpleModel> modelMap = modelList.stream().collect(Collectors.toMap(SimpleModel::getId, Function.identity()));

        //查询合同使用的模板
        List<String> modelIds = pageResult.getList().stream().map(ModelStoreRespVO::getId).collect(Collectors.toList());
        List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().in(ContractDO::getTemplateId, modelIds).eq(ContractDO::getUpload, ContractUploadTypeEnums.MODEL_DRAFT.getCode()));
        //统计该模板下的合同数量
        Map<String, Long> contractCountMap = contractDOS.stream().collect(Collectors.groupingBy(ContractDO::getTemplateId, Collectors.counting()));
        for (ModelStoreRespVO respVo : vos) {
            respVo.setCount(contractCountMap.getOrDefault(respVo.getId(), 0L));
            // 判断模板有效期，并添加相应提示
            if (respVo.getTimeEffectModel() != null && respVo.getEffective() != null && respVo.getTimeEffectModel() == 0 && respVo.getEffective() == 0) {
                if (respVo.getEffectStartTime() != null && respVo.getEffectEndTime() != null) {
                    if (respVo.getEffectStartTime().isAfter(LocalDateTime.now())) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String formattedDate = respVo.getEffectStartTime().format(formatter);
                        respVo.setEffectivePeriodTips("模板时效于" + formattedDate + "开始，该模板未生效");
                    }
                    if (respVo.getEffectEndTime().isBefore(LocalDateTime.now())) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String formattedDate = respVo.getEffectEndTime().format(formatter);
                        respVo.setEffectivePeriodTips("模板时效至" + formattedDate + "，该模板已失效");
                    }
                }
            }
            SimpleModel model = modelMap.get(respVo.getId());
            if (model != null) {
                // 设置创建者昵称
                AdminUserRespDTO creator = userMap.get(Long.valueOf(model.getCreator()));
                respVo.setCreatorName(creator != null ? creator.getNickname() : "");

                // 设置模板分类名称
                ModelCategory modelCategory = modelCategoryMap.get(model.getCategoryId());
                respVo.setCategoryName(modelCategory != null ? modelCategory.getName() : "");

                // 设置合同类型名称
                ContractType contractType = contractTypeMap.get(model.getContractType());
                respVo.setContractTypeName(contractType != null ? contractType.getName() : "");

                // 设置模板类型字符串
                ModelTypeEnum modelTypeEnum = ModelTypeEnum.getInstance(model.getType());
                respVo.setTypeStr(modelTypeEnum != null ? modelTypeEnum.getInfo() : "");

                // 设置文件名
                respVo.setFileName(fileApi.getName(model.getRemoteFileId()));

                // 设置文件ID
                respVo.setFileId(ModelTypeEnum.WPS_UPLOAD == ModelTypeEnum.getInstance(model.getType()) ? model.getRemoteFileId() : model.getRtfPdfFileId());
            }
        }

        pageResult.setList(vos);
        pageResult.setTotal(page.getTotal());
        return pageResult;
    }

    /**
     * 模板制作-列表
     */
    @Override
    public ModelBigPageRespVO getMyModelPage(ModelListReqVO vo) throws Exception {

        String loginUserId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        //动态配置
        ModelBigPageRespVO bigPageRespVO = new ModelBigPageRespVO();
        FlowableConfigRespDTO flowableConfigRespDTO = systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.MODEL_APPROVE);
        FlowableConfigRespVO flowableConfigRespVO = SystemConfigDTOConverter.INSTANCE.dto2resp(flowableConfigRespDTO);
        bigPageRespVO.setFlowableConfigRespVO(flowableConfigRespVO);

        String searchText = vo.getSearchText();

        Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

        Map<String, BpmTaskAllInfoRespVO> taskEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        List<BpmTaskAllInfoRespVO> taskEndTimeAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

        LambdaQueryWrapper<SimpleModel> wrapperX = new LambdaQueryWrapper<>();

        // 查出全部类型
        if (StringUtils.isNotEmpty(vo.getContractType())) {
            List<String> types = new ArrayList();
            selectAllChildTypes(Arrays.asList(vo.getContractType()), types);
            wrapperX.in(SimpleModel::getContractType, types);
        }

        //模板全部列表  构建子查询
        if (StringUtils.isEmpty(vo.getFrontCode())) {
            // 构建子查询，获取每个code分组中，再按照approve_status分组的最大version
            String subQuery = "SELECT code, approve_status, MAX(version) AS max_version FROM ecms_model GROUP BY code, approve_status";

            // 构建主查询，连接子查询并筛选出每个code分组中，再按照approve_status分组的最大version的记录
            wrapperX.inSql(SimpleModel::getId, "SELECT id FROM ecms_model WHERE (code, approve_status, version) IN (" + subQuery + ")").orderByDesc(SimpleModel::getUpdateTime);
        } else if ("SUCCESS".equals(vo.getFrontCode())) {
            String subQuery = "SELECT code, approve_status, MAX(version) AS max_version FROM ecms_model GROUP BY code, approve_status";

            // 构建主查询，连接子查询并筛选出每个code分组中，再按照approve_status分组的最大version的记录
            wrapperX = new LambdaQueryWrapper<SimpleModel>().inSql(SimpleModel::getId, "SELECT id FROM ecms_model WHERE (code, approve_status, version) IN (" + subQuery + ") AND approve_status = 2").orderByDesc(SimpleModel::getUpdateTime);
        } else {
            //模板制作其他对应状态列表
            // 构建子查询，获取每组中version的最大值
            String subQuery = "SELECT code, MAX(version) AS max_version FROM ecms_model GROUP BY code";
            // 构建主查询，连接子查询并筛选出每组中version等于最大值的记录
            wrapperX.inSql(SimpleModel::getId, "SELECT id FROM ecms_model WHERE (code, version) IN (" + subQuery + ")").orderByDesc(SimpleModel::getUpdateTime);

        }

        // 模板名字
        if (ObjectUtil.isNotEmpty(searchText)) {
            wrapperX.like(SimpleModel::getName, searchText);
        }

        // 创建时间
        if (ObjectUtil.isNotEmpty(vo.getStartCreateTime()) && ObjectUtil.isNotEmpty(vo.getEndCreateTime())) {
            wrapperX.between(SimpleModel::getCreateTime, vo.getStartCreateTime(), vo.getEndCreateTime());
        }

        // 审批时间
        if (ObjectUtil.isNotEmpty(vo.getStartApproveTime()) && ObjectUtil.isNotEmpty(vo.getEndApproveTime())) {
            wrapperX.between(SimpleModel::getApproveTime, vo.getStartApproveTime(), vo.getEndApproveTime());
        }

        // 模板分类
//        if (ObjectUtil.isNotEmpty(vo.getCategoryId())) {
//            wrapperX.eq(SimpleModel::getCategoryId, vo.getCategoryId());
//        }

        // 时效模式
        if (ObjectUtil.isNotEmpty(vo.getTimeEffectModel())) {
            wrapperX.eq(SimpleModel::getTimeEffectModel, vo.getTimeEffectModel());
        }

        // 审批状态
        if (ObjectUtil.isNotEmpty(vo.getApproveStatus())) {
            wrapperX.eq(SimpleModel::getApproveStatus, vo.getApproveStatus());
        }
        //编码
        if (vo.getCode() != null) {
            wrapperX.like(SimpleModel::getCode, vo.getCode());
        }
        //是否失效
        if (vo.getEffective() != null) {
            wrapperX.eq(SimpleModel::getEffective, vo.getEffective());
        }
        //是否启用
        if (vo.getEffectStatus() != null) {
            wrapperX.eq(SimpleModel::getEffectStatus, vo.getEffectStatus());
        }
        //区划编号
        if (StringUtils.isNotBlank(vo.getRegionCode())) {
            wrapperX.eq(SimpleModel::getRegionCode, vo.getRegionCode());
        }
        wrapperX.orderByDesc(SimpleModel::getUpdateTime);

        if (StringUtils.isNotEmpty(vo.getFrontCode())) {
            CommonFlowableReqVOResultStatusEnums enums = CommonFlowableReqVOResultStatusEnums.getFrontInstance(vo.getFrontCode());
            if (ObjectUtil.isNotNull(enums)) {
                switch (enums) {
                    case TO_SEND:
                        List<Integer> statusList = new ArrayList<Integer>();
                        statusList.add(CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode());
                        statusList.add(CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode());
                        statusList.add(CommonFlowableReqVOResultStatusEnums.CANCEL.getResultCode());

                        wrapperX.in(SimpleModel::getApproveStatus, statusList);
                        break;
                    default:
                        wrapperX.eq(SimpleModel::getApproveStatus, enums.getResultCode());
                        break;
                }
            }
        } else {
            //模板制作 全部列表
            //构建待审核list
            ArrayList<Integer> toSendList = new ArrayList<>();
            toSendList.add(CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode());
            toSendList.add(CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode());
            toSendList.add(CommonFlowableReqVOResultStatusEnums.APPROVING.getResultCode());
            toSendList.add(CommonFlowableReqVOResultStatusEnums.SUCCESS.getResultCode());
            toSendList.add(CommonFlowableReqVOResultStatusEnums.CANCEL.getResultCode());
            wrapperX.in(SimpleModel::getApproveStatus, toSendList);
//            wrapperX.and(
//                    w -> w.in(SimpleModel::getApproveStatus, toSendList)
//                            .or()
//                            .eq(SimpleModel::getApproveStatus, CommonFlowableReqVOResultStatusEnums.APPROVING.getResultCode())
//                            .or()
//                            .eq(SimpleModel::getApproveStatus, CommonFlowableReqVOResultStatusEnums.SUCCESS.getResultCode())
//            );
        }
        //模板分类的树状图搜索条件
        wrapperX = enhanceWrapperByCategoryIds(wrapperX, vo);

        // 执行查询
        Page<SimpleModel> page1 = new Page<>(vo.getPageNo(), vo.getPageSize());
        IPage<SimpleModel> resultPage = simpleModelMapper.selectPage(page1, wrapperX);
        List<SimpleModel> records = resultPage.getRecords();
        long total = resultPage.getTotal();

        PageResult<SimpleModel> page = new PageResult<>();
        page.setList(records);
        page.setTotal(total);


        if (CollectionUtil.isEmpty(page.getList())) {
            return bigPageRespVO.setPageResult(new PageResult<ModelPageRespVO>().setList(Collections.emptyList()).setTotal(page.getTotal()));
        }
        PageResult<ModelAllVO> allPageResult = ModelConverter.INSTANCE.convertPage21(page);
        for (ModelAllVO allVO : allPageResult.getList()) {
            allVO.setEffectivePeriod(allVO.getEffectStartTime() + "to" + allVO.getEffectEndTime());
        }
        PageResult<ModelPageRespVO> pageResult = ModelConverter.INSTANCE.convertPage3(allPageResult);
        List<ModelPageRespVO> vos = pageResult.getList();

        //获得user信息
        List<AdminUserRespDTO> userList1 = userApi.getUserList();
        Map<Long, AdminUserRespDTO> creatorMap = CollectionUtils.convertMap(userList1, AdminUserRespDTO::getId);

        Long userId = SecurityFrameworkUtils.getLoginUserId();

        List<SimpleModel> modelList = simpleModelMapper.selectList();
        Map<String, SimpleModel> modelMap = CollectionUtils.convertMap(modelList, SimpleModel::getId);

        List<ModelCategory> modelCategoryList = modelCategoryMapper.selectList();
        Map<Integer, ModelCategory> modelCategoryMap = CollectionUtils.convertMap(modelCategoryList, ModelCategory::getId);

        List<ContractType> contractTypeList = contractTypeMapper.selectList();
        Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
        //找到模板对应的流程实例
        List<ModelBpmDO> modelBpmDOList = modelBpmMapper.selectList(new LambdaQueryWrapperX<ModelBpmDO>());
        List<String> instanceList = modelBpmDOList.stream().map(ModelBpmDO::getProcessInstanceId).collect(Collectors.toList());
        Map<String, ModelBpmDO> modelBpmDOMap = CollectionUtils.convertMap(modelBpmDOList, ModelBpmDO::getModelId);
        if (CollectionUtil.isNotEmpty(instanceList)) {
            taskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(loginUserId), instanceList);
            taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
            taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
        }
        //有结束时间的流程任务
        Map<String, ModelBpmDO> modelEndTimeBpmDOMap = CollectionUtils.convertMap(modelBpmDOList, ModelBpmDO::getModelId);

        //待处理的任务
        List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
        Map<String, BpmTaskAllInfoRespVO> toDoTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();

        if (CollectionUtil.isNotEmpty(instanceList)) {
            taskEndTimeAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(loginUserId), instanceList);
            taskEndTimeAllInfoRespVOList = EcontractUtil.distinctTaskHaveEndTime(taskEndTimeAllInfoRespVOList);
            taskEndTimeMap = CollectionUtils.convertMap(taskEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

            toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTime(taskAllInfoRespVOList);
            toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

        }
        for (ModelPageRespVO respVo : vos) {
            //将流程实例id添加进返回结果中
            if (CollectionUtil.isNotEmpty(modelBpmDOMap)) {
                ModelBpmDO modelBpmDO = modelBpmDOMap.get(respVo.getId());
                if (ObjectUtil.isNotNull(modelBpmDO) && ObjectUtil.isNotEmpty(modelBpmDO.getProcessInstanceId())) {
                    respVo.setProcessInstanceId(modelBpmDO.getProcessInstanceId());
                }
            }
            // 判断模板有效期，并添加相应提示
            if (respVo.getTimeEffectModel() != null && respVo.getEffective() != null && respVo.getTimeEffectModel() == 0 && respVo.getEffective() == 0) {
                if (respVo.getEffectStartTime() != null && respVo.getEffectEndTime() != null) {
                    if (respVo.getEffectStartTime().isAfter(LocalDateTime.now())) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String formattedDate = respVo.getEffectStartTime().format(formatter);
                        respVo.setEffectivePeriodTips("模板时效于" + formattedDate + "开始，该模板未生效");
                    }
                    if (respVo.getEffectEndTime().isBefore(LocalDateTime.now())) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String formattedDate = respVo.getEffectEndTime().format(formatter);
                        respVo.setEffectivePeriodTips("模板时效至" + formattedDate + "，该模板已失效");
                    }
                }
            }
            SimpleModel model = modelMap.get(respVo.getId());
            if (model != null) {
                //得到将每个昵称赋值
                if (creatorMap.get(Long.valueOf(model.getCreator())) != null) {
                    AdminUserRespDTO user = creatorMap.get(Long.valueOf(model.getCreator()));
                    if (user != null) {
                        respVo.setCreatorName(user.getNickname());
                    }
                }
                ModelCategory modelCategory = modelCategoryMap.get(model.getCategoryId());
                if (modelCategory != null) {
                    respVo.setCategoryName(modelCategory.getName());
                }
                ContractType contractType = contractTypeMap.get(model.getContractType());
                if (contractType != null) {
                    respVo.setContractTypeName(contractType.getName());
                }
                if (fileApi.getName(model.getRemoteFileId()) != null) {
                    respVo.setFileName(fileApi.getName(model.getRemoteFileId()));
                }
                ModelTypeEnum modelTypeEnum = ModelTypeEnum.getInstance(model.getType());
                respVo.setTypeStr(modelTypeEnum == null ? "" : modelTypeEnum.getInfo());
                respVo.setModelDescription(model.getDescription());
//                //富文本文件id
//                respVo.setFileId(model.getRtfPdfFileId());
                StatusEnums modelStatusEnum = StatusEnums.getInstance(model.getApproveStatus());
                respVo.setApproveStatusStr(modelStatusEnum == null ? "" : modelStatusEnum.getInfo());
                //给前端暴露统一的文件id ()
                respVo.setFileId(ModelTypeEnum.WPS_UPLOAD == ModelTypeEnum.getInstance(model.getType()) ? model.getRemoteFileId() : model.getRtfPdfFileId());

                ModelBpmDO modelBpmDO = modelBpmDOMap.get(respVo.getId());
                if (ObjectUtil.isNotNull(modelBpmDO)) {
                    BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(modelBpmDO.getResult());
                    //如果是被退回
                    if (ObjectUtil.isNotNull(resultEnum)) {
                        //获取任务endTime，便知道审批时间
                        BpmTaskAllInfoRespVO endTimeTask = taskEndTimeMap.get(modelBpmDO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(endTimeTask)) {
                            respVo.setApproveTime(endTimeTask.getEndTime());
                        }
                        //如果是被退回的申请，回显任务id
                        if (BpmProcessInstanceResultEnum.BACK == resultEnum) {
                            BpmTaskAllInfoRespVO rejectedTask = taskMap.get(modelBpmDO.getProcessInstanceId());
                            if (ObjectUtil.isNotNull(rejectedTask)) {
                                respVo.setTaskId(rejectedTask.getTaskId());
                            }
                        }

                        //待办任务的被分配人
                        if (BpmProcessInstanceResultEnum.APPROVE != resultEnum) {
                            BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = toDoTaskMap.get(respVo.getProcessInstanceId());
                            if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
                                Long assigneeId = bpmTaskAllInfoRespVO.getAssigneeUserId();
                                respVo.setAssigneeId(assigneeId);

                                //如果处理人不是发起人，则不可以撤回
                                if (!userId.equals(assigneeId)) {
                                    respVo.setIfRepeal(IfEnums.NO.getCode());
                                } else {
                                    respVo.setIfRepeal(IfEnums.YES.getCode());
                                }
                            }
                        }
                    }
                }
            }
        }

        pageResult.setList(vos);
        pageResult.setTotal(page.getTotal());
        bigPageRespVO.setPageResult(pageResult);

        return bigPageRespVO;
    } //模板制作-列表end

    /**
     * 递归获取指定合同类型的所有下级节点
     *
     * @param parentId 父类ID
     * @return 所有下级节点列表
     */
    public List<ContractType> getAllSubContractTypes(String parentId) {
        // 获取所有子节点
        List<ContractType> childContractTypes = contractTypeMapper.findByParentId(parentId);

        // 对每个子节点进行递归查找它们的子节点
        for (ContractType child : childContractTypes) {
            List<ContractType> subChildContractTypes = getAllSubContractTypes(child.getId());
            child.getSubContractTypes().addAll(subChildContractTypes); // 将下级节点加到当前节点中
        }

        return childContractTypes;
    }


    /**
     * 查看模板历史版本 自用方法
     *
     * @param code
     * @return
     */
    @Override
    public PageResult<ModelPageRespVO> getModelHistory(String code) throws Exception {
        if (StringUtils.isNotBlank(code)) {
            Page<SimpleModel> page1 = new Page<>(1, 1000);
            QueryWrapperX<SimpleModel> wrapper = new QueryWrapperX<>();
            wrapper.eq("code", code).orderByDesc("version");
            IPage<SimpleModel> resultPage = simpleModelMapper.selectPage(page1, wrapper);
            List<SimpleModel> records = resultPage.getRecords();
            long total = resultPage.getTotal();

            PageResult<SimpleModel> page = new PageResult<>();
            page.setList(records);
            page.setTotal(total);

            if (CollectionUtil.isEmpty(page.getList())) {
                return new PageResult<>();
            }
            PageResult<ModelAllVO> allPageResult = ModelConverter.INSTANCE.convertPage21(page);
            for (ModelAllVO allVO : allPageResult.getList()) {
                allVO.setEffectivePeriod(allVO.getEffectStartTime() + "to" + allVO.getEffectEndTime());
            }
            PageResult<ModelPageRespVO> pageResult = ModelConverter.INSTANCE.convertPage3(allPageResult);
            List<ModelPageRespVO> vos = pageResult.getList();
            //获得user信息
            List<AdminUserRespDTO> userList1 = userApi.getUserList();
            Map<Long, AdminUserRespDTO> creatorMap = CollectionUtils.convertMap(userList1, AdminUserRespDTO::getId);

            List<SimpleModel> modelList = simpleModelMapper.selectList();
            Map<String, SimpleModel> modelMap = CollectionUtils.convertMap(modelList, SimpleModel::getId);

            List<ModelCategory> modelCategoryList = modelCategoryMapper.selectList();
            Map<Integer, ModelCategory> modelCategoryMap = CollectionUtils.convertMap(modelCategoryList, ModelCategory::getId);

            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            for (ModelPageRespVO respVo : vos) {
                SimpleModel model = modelMap.get(respVo.getId());
                if (model != null) {
                    //得到将每个昵称赋值
                    if (creatorMap.get(Long.valueOf(model.getCreator())) != null) {
                        AdminUserRespDTO user = creatorMap.get(Long.valueOf(model.getCreator()));
                        if (user != null) {
                            respVo.setCreatorName(user.getNickname());
                        }
                    }
                    ModelCategory modelCategory = modelCategoryMap.get(model.getCategoryId());
                    if (modelCategory != null) {
                        respVo.setCategoryName(modelCategory.getName());
                    }
                    ContractType contractType = contractTypeMap.get(model.getContractType());
                    if (contractType != null) {
                        respVo.setContractTypeName(contractType.getName());
                    }
                    if (fileApi.getName(model.getRemoteFileId()) != null) {
                        respVo.setFileName(fileApi.getName(model.getRemoteFileId()));
                    }
                    ModelTypeEnum modelTypeEnum = ModelTypeEnum.getInstance(model.getType());
                    respVo.setTypeStr(modelTypeEnum == null ? "" : modelTypeEnum.getInfo());
                    respVo.setModelDescription(model.getDescription());
//                //富文本文件id
//                respVo.setFileId(model.getRtfPdfFileId());
                    StatusEnums modelStatusEnum = StatusEnums.getInstance(model.getApproveStatus());
                    respVo.setApproveStatusStr(modelStatusEnum == null ? "" : modelStatusEnum.getInfo());
                    //给前端暴露统一的文件id ()
                    respVo.setFileId(ModelTypeEnum.WPS_UPLOAD == ModelTypeEnum.getInstance(model.getType()) ? model.getRemoteFileId() : model.getRtfPdfFileId());
                }
            }

            pageResult.setList(vos);
            pageResult.setTotal(page.getTotal());
            return pageResult;

        }
        return null;
    }

    /**
     * 查看模板历史版本 前端调用方法
     *
     * @param code
     * @return
     */
    @Override
    public List<ModelPageRespVO> getModelHistoryV1(String code) throws Exception {
        if (StringUtils.isNotBlank(code)) {
            QueryWrapperX<SimpleModel> wrapper = new QueryWrapperX<>();
            wrapper.eq("code", code).orderByDesc("version");
            wrapper.inSql("id", "select model_id from ecms_model_bpm where result = 2");
            List<SimpleModel> simpleModelList = simpleModelMapper.selectList(wrapper);
            if (CollectionUtil.isEmpty(simpleModelList)) {
                return new ArrayList<>();
            }
            simpleModelList.remove(0);
            List<ModelAllVO> modelAllVOList = ModelConverter.INSTANCE.convertList(simpleModelList);
            for (ModelAllVO allVO : modelAllVOList) {
                allVO.setEffectivePeriod(allVO.getEffectStartTime() + "to" + allVO.getEffectEndTime());
            }
            List<ModelPageRespVO> modelPageRespVOList = ModelConverter.INSTANCE.convertLsitV1(modelAllVOList);
            //获得user信息
            List<AdminUserRespDTO> userList1 = userApi.getUserList();
            Map<Long, AdminUserRespDTO> creatorMap = CollectionUtils.convertMap(userList1, AdminUserRespDTO::getId);

            List<SimpleModel> modelList = simpleModelMapper.selectList();
            Map<String, SimpleModel> modelMap = CollectionUtils.convertMap(modelList, SimpleModel::getId);

            List<ModelCategory> modelCategoryList = modelCategoryMapper.selectList();
            Map<Integer, ModelCategory> modelCategoryMap = CollectionUtils.convertMap(modelCategoryList, ModelCategory::getId);

            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            //找到模板对应的流程实例
            Map<String, ModelBpmDO> modelBpmDOMap = new HashMap<>();
            List<String> collect = modelAllVOList.stream().map(ModelAllVO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collect)) {
                List<ModelBpmDO> modelBpmDOList = modelBpmMapper.selectList(new LambdaQueryWrapperX<ModelBpmDO>().in(ModelBpmDO::getModelId, modelAllVOList.stream().map(ModelAllVO::getId).collect(Collectors.toList())));
                modelBpmDOMap = CollectionUtils.convertMap(modelBpmDOList, ModelBpmDO::getModelId);
            }

            for (ModelPageRespVO respVo : modelPageRespVOList) {
                SimpleModel model = modelMap.get(respVo.getId());
                if (model != null) {
                    //将流程实例id添加进返回结果中
                    if (CollectionUtil.isNotEmpty(modelBpmDOMap)) {
                        ModelBpmDO modelBpmDO = modelBpmDOMap.get(respVo.getId());
                        if (ObjectUtil.isNotNull(modelBpmDO) && ObjectUtil.isNotEmpty(modelBpmDO.getProcessInstanceId())) {
                            respVo.setProcessInstanceId(modelBpmDO.getProcessInstanceId());
                        }
                    }
                    //得到将每个昵称赋值
                    if (creatorMap.get(Long.valueOf(model.getCreator())) != null) {
                        AdminUserRespDTO user = creatorMap.get(Long.valueOf(model.getCreator()));
                        if (user != null) {
                            respVo.setCreatorName(user.getNickname());
                        }
                    }
                    ModelCategory modelCategory = modelCategoryMap.get(model.getCategoryId());
                    if (modelCategory != null) {
                        respVo.setCategoryName(modelCategory.getName());
                    }
                    ContractType contractType = contractTypeMap.get(model.getContractType());
                    if (contractType != null) {
                        respVo.setContractTypeName(contractType.getName());
                    }
                    if (fileApi.getName(model.getRemoteFileId()) != null) {
                        respVo.setFileName(fileApi.getName(model.getRemoteFileId()));
                    }
                    ModelTypeEnum modelTypeEnum = ModelTypeEnum.getInstance(model.getType());
                    respVo.setTypeStr(modelTypeEnum == null ? "" : modelTypeEnum.getInfo());
                    respVo.setModelDescription(model.getDescription());
//                //富文本文件id
//                respVo.setFileId(model.getRtfPdfFileId());
                    StatusEnums modelStatusEnum = StatusEnums.getInstance(model.getApproveStatus());
                    respVo.setApproveStatusStr(modelStatusEnum == null ? "" : modelStatusEnum.getInfo());
                    //给前端暴露统一的文件id ()
                    respVo.setFileId(ModelTypeEnum.WPS_UPLOAD == ModelTypeEnum.getInstance(model.getType()) ? model.getRemoteFileId() : model.getRtfPdfFileId());
                }
            }
            return modelPageRespVOList;
        }
        return null;
    }

    /**
     * 获取最新版本
     *
     * @param code
     * @return
     * @throws Exception
     */
    @Override
    public ModelVersionVO getModelVersion(String code, Integer i) throws Exception {
        ModelVersionVO modelVersionVO = new ModelVersionVO();
        List<BigDecimal> bigDecimals = new ArrayList<>();
        if (StringUtils.isNotBlank(code)) {
            if (i == 0) {
                // 外部列表
                PageResult<ModelPageRespVO> modelHistory = this.getModelHistory(code);
                String modelId = modelHistory.getList().get(0).getId();
                ModelBpmDO modelBpmDOV1 = modelBpmMapper.selectOne(ModelBpmDO::getModelId, modelId);
                if (ObjectUtil.isEmpty(modelBpmDOV1) || modelBpmDOV1.getResult() == 0 || modelBpmDOV1.getResult() == 5) {
                    return modelVersionVO.setModelId(modelId);
                }
                if (modelHistory != null && modelHistory.getList() != null && modelHistory.getList().size() > 0) {
                    //判断相关版本是否在审核中
                    for (ModelPageRespVO modelPageRespVO : modelHistory.getList()) {
                        ModelBpmDO modelBpmDO = modelBpmMapper.selectOne(ModelBpmDO::getModelId, modelPageRespVO.getId());
                        //没有值-草稿抓状态  |  有值 ！= 2   -  在审核中
                        if (modelBpmDO != null && modelBpmDO.getResult() == 1) {
                            throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY, "模板" + modelPageRespVO.getVersion() + "修订中，新版本取消或发布后可重新修订！");
                        }
                    }
                }
                QueryWrapperX<SimpleModel> wrapper = new QueryWrapperX<>();
                wrapper.eq("code", code).orderByDesc("version");
                wrapper.inSql("id", "select model_id from ecms_model_bpm where result = 2");
                List<SimpleModel> simpleModelList = simpleModelMapper.selectList(wrapper);
                Double version = simpleModelList.get(0).getVersion();
                BigDecimal decimalVersion = BigDecimal.valueOf(version);
                bigDecimals.add(decimalVersion.add(BigDecimal.valueOf(0.1)));
                //获取version的整数部分，并加1
                int integerPart = (int) Math.floor(version);
                bigDecimals.add(BigDecimal.valueOf(integerPart + 1));
            } else {
                // 内部编辑
                QueryWrapperX<SimpleModel> wrapper = new QueryWrapperX<>();
                wrapper.eq("code", code).orderByDesc("version");
                wrapper.inSql("id", "select model_id from ecms_model_bpm where result = 2");
                List<SimpleModel> simpleModelList = simpleModelMapper.selectList(wrapper);
                Double version = simpleModelList.get(0).getVersion();
                BigDecimal decimalVersion = BigDecimal.valueOf(version);
                bigDecimals.add(decimalVersion.add(BigDecimal.valueOf(0.1)));
                //获取version的整数部分，并加1
                int integerPart = (int) Math.floor(version);
                bigDecimals.add(BigDecimal.valueOf(integerPart + 1));
            }
        }
        return modelVersionVO.setVersionList(bigDecimals);
    }


    private void existCode(String code) {
        List<String> codeList = simpleModelMapper.selectList().stream().map(SimpleModel::getCode).collect(Collectors.toList());
        Boolean exist = codeList.contains(code);
        if (exist) {
            throw exception(CODE_EXIST_ERROR);
        }
    }

    /**
     * 新增模板 富文本转pdf
     */
    private Long rtfContentProcessor(ModelCreateReqVO vo, String modelContent) throws Exception {
        String modelName = vo.getName();
        Long rtf_fileId = 0L;
        if (StringUtils.isNotBlank(modelContent)) {
            String uuid = String.valueOf(UUID.randomUUID());
            String path = MODEL_RTF_PDF_TEMP_PATH + uuid + ".pdf";
            //富文本生成pdf，存查看的文件id地址
            String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
            String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
            FileUtil.touch(path);
            contractService.convertRtf2Pdf(modelContent, path);
            //将存于path的pdf文件上到minio的路径FileUploadPathEnum.MODEL.getPath(vo.getCode(),vo.getName())
            rtf_fileId = fileApi.uploadFile(modelName + ".pdf", FileUploadPathEnum.MODEL.getPath(uuid, vo.getName() + ".pdf"), IoUtil.readBytes(FileUtil.getInputStream(path)));
            FileUtil.del(path);
            FileUtil.del(localFolderPath);
        }
        return rtf_fileId;
    }

    //    private Long rtfContentProcessor(ModelCreateReqVO vo, String modelContent) throws Exception {
//        String modelName = vo.getName();
//        Long rtf_fileId = 0L;
//        if (StringUtils.isNotBlank(modelContent)) {
//            String uuid = String.valueOf(UUID.randomUUID());
//            String path = MODEL_RTF_PDF_TEMP_PATH + uuid + ".pdf";
//            PdfConvertHtmlUtil.rtf2Pdf(modelContent, path);
//            //将存于path的pdf文件上到minio的路径FileUploadPathEnum.MODEL.getPath(vo.getCode(),vo.getName())
//            rtf_fileId = fileApi.uploadFile(modelName + ".pdf", FileUploadPathEnum.MODEL.getPath(uuid, vo.getName() + ".pdf"), IoUtil.readBytes(FileUtil.getInputStream(path)));
//            FileUtil.del(path);
//        }
//        return rtf_fileId;
//    }
    @Resource
    private WkHtmlToPdfManager wkHtmlToPdfManager;

    /**
     * 编辑模板 富文本转pdf
     */
    private Long rtfContentProcessor(ModelUpdateVO vo, String modelContent) throws Exception {
        String modelName = vo.getName();
        Long rtf_fileId = 0L;
        if (StringUtils.isNotBlank(modelContent)) {
            String uuid = String.valueOf(UUID.randomUUID());
            String path = MODEL_RTF_PDF_TEMP_PATH + uuid + ".pdf";
            wkHtmlToPdfManager.fileAuthHtmlToPdfFromstring(modelContent, path);
            //将存于path的pdf文件上到minio的路径FileUploadPathEnum.MODEL.getPath(vo.getCode(),vo.getName())
            rtf_fileId = fileApi.uploadFile(modelName + ".pdf", FileUploadPathEnum.MODEL.getPath(new Date().toString(), vo.getName() + ".pdf"), IoUtil.readBytes(FileUtil.getInputStream(path)));
            FileUtil.del(path);
        }
        return rtf_fileId;
    }

    /**
     * 通过范本新增模板
     */
    private String insertModelByTemplate(ModelCreateReqVO vo) {
        //范本模版文件所需
        String templateFileName = "";
        Long templateFileId = null;
        ContractTemplate template = templateMapper.selectById(vo.getTemplateId());
        if (template != null) {
            FileDTO templateFile = fileApi.selectById(template.getRemoteFileId());
            if (templateFile != null) {
                templateFileId = templateFile.getId();
            }
        }
        Model model = ModelConverter.INSTANCE.toEntity(vo);
        model.setTemplateId(vo.getTemplateId());
        model.setRemoteFileId(templateFileId);
        modelMapper.insert(model);
        return model.getId();
    }

    /**
     * 根据上传文件的范本新增。由于此时的范本是word文档，故没有条款，没有富文本
     *
     * @param vo
     * @return
     * @throws Exception
     */
    private String insertModelByUploadTemplate(ModelCreateReqVO vo) throws Exception {
        Model model = ModelConverter.INSTANCE.toEntity(vo);
        model.setRemoteFileId(vo.getRemoteFileId());
        //将wps的word文件转为pdf文件，用于前端展示
        Long pdfFileId = word2Pdf(vo.getRemoteFileId(), FileUploadPathEnum.MODEL.getCode());
        model.setRtfPdfFileId(pdfFileId);
        if (vo.getId() != null) {
            modelMapper.updateById(model);
        } else {
            modelMapper.insert(model);
        }

        this.modelTermProcessor(vo, model);
        return model.getId();
    }

    /**
     * 条款新增模板
     * 条款方法：只传富文本 ：转成pdf文件，持久化字段 remote_file_id 和 rtf_pdf_fileId
     */
    private String termAddProcessor(ModelCreateReqVO vo) throws Exception {
        Model model = ModelConverter.INSTANCE.toEntity(vo);
        //富文本转成pdf
        Long rtf_fileId = rtfContentProcessor(vo, vo.getModelContent());
        model.setRtfPdfFileId(rtf_fileId);
        model.setRemoteFileId(rtf_fileId);
        modelMapper.insert(model);
        this.modelTermProcessor(vo, model);
        return model.getId();
    }

    private void modelTermProcessor(ModelCreateReqVO vo, Model model) {
        if (ModelTypeEnum.TERM_ADD == ModelTypeEnum.getInstance(vo.getType())) {
            if (CollectionUtil.isNotEmpty(vo.getTerms())) {
                List<ModelTerm> convert = ModelTermConverter.INSTANCE.convert(vo.getTerms(), model);
                modelTermMapper.insertBatch(convert);
            }
        }
    }


    /**
     * 编辑模板
     * 条款方法：只传富文本 ：转成pdf文件，持久化字段 remote_file_id 和 rtf_pdf_fileId
     * 上传文件方法：传富文本和Multipart文件，持久化 remote_file_id 和 rtf_pdf_fileId
     * 范本方法：只传富文本，无Multipart文件 RtfPdfFileId ，持久化 rtf_file_id
     * WPS方法：只传一个wps编辑保存后的文件id remoteFileId，持久化 remote_file_id，将wps文件转成pdf存到 rtf_pdf_fileId,给前端展示用。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateModel(@Validated ModelUpdateVO vo) throws Exception {
        checkValue(vo);
        SimpleModel sqlModel = ModelConverter.INSTANCE.convertV1(vo);
        List<SimpleModel> models = new ArrayList<SimpleModel>();
        models.add(sqlModel);
        //校验模版是否可以编辑
        if (!isModifiable(models)) {
            throw exception(ErrorCodeConstants.MODEL_STATUS_NOT_MODIFIABLE);
        }

        //将日期字符串转化成Date
        if (StringUtils.isNotBlank(vo.getEffectStartTimeReciever())) {
            vo.setEffectStartTime(EcontractUtil.getDateFromSimpleStr(vo.getEffectStartTimeReciever()));
            vo.setEffectEndTime(EcontractUtil.getDateFromSimpleStr(vo.getEffectEndTimeReciever()));
        }
        //判断合同是否在有效期
        if (vo.getTimeEffectModel() == 0 && vo.getEffectStartTime() != null && vo.getEffectEndTime() != null) {
            if (vo.getEffectStartTime().isAfter(vo.getEffectEndTime())) {
                throw new Exception("开始时间不能大于结束时间");
            }
            //当前时间在开始时间和结束时间之间
            LocalDateTime currentTime = LocalDateTime.now();
            if (currentTime.isAfter(vo.getEffectStartTime()) && currentTime.isBefore(vo.getEffectEndTime())) {
                vo.setEffective(1);
            } else {
                vo.setEffective(0);
            }
        } else {
            vo.setEffective(1);
        }

        simpleModelMapper.updateById(sqlModel);
        //wps方式的编辑
        if (ModelTypeEnum.WPS_UPLOAD == ModelTypeEnum.getInstance(sqlModel.getType())) {
            return updateModelByWps(vo);
        }

        Boolean paramsExist = StringUtils.isNotBlank(vo.getParamListReciever());
        if (paramsExist) {
            //将接收的JSONObject类型的绑定参数信息深克隆成ParamModelVo
            List<JSONObject> jsonList = JSONObject.parseObject(vo.getParamListReciever(), List.class);
            List<ParamModelVo> finalParamModelDTOList = new ArrayList<ParamModelVo>();

            for (JSONObject jsonObject : jsonList) {
                String paramId = jsonObject.getString(PARAM_ID);
                String location = jsonObject.getString(LOCATION);
                Integer paramNum = jsonObject.getInteger(PARAM_NUM);
                String tagId = jsonObject.getString(TAG_ID);
                ParamModelVo newParamModelDTO = new ParamModelVo();
                newParamModelDTO.setParamId(paramId);
                newParamModelDTO.setLocation(location);
                //追加字段
                newParamModelDTO.setParamNum(paramNum);
                newParamModelDTO.setTagId(tagId);
                finalParamModelDTOList.add(newParamModelDTO);
            }
            vo.setParamModelVoList(finalParamModelDTOList);
        }

        ModelTypeEnum modelTypeEnum = ModelTypeEnum.getInstance(vo.getType());
        if (ModelTypeEnum.TEMPLATE == modelTypeEnum) {
            return updateModelByTemplate(vo);
        }

        if (ModelTypeEnum.TERM_ADD == ModelTypeEnum.getInstance(sqlModel.getType())) {
            Model model = ModelConverter.INSTANCE.convert2Model(vo);
//            model.setApproveStatus(StatusEnums.NEVER_APPROVED.getCode());
            modelMapper.updateById(model);
            modelTermProcessor(vo, model);
            return "success";
        }
        //同步文件信息（上传文件方式）
        Model model = enhanceUpdate2Do(vo, sqlModel);
        if (ObjectUtil.isNotNull(vo.getModelContent())) {
            Long rtf_fileId = rtfContentProcessor(vo, vo.getModelContent());
            model.setRtfPdfFileId(rtf_fileId);
            model.setModelContent(vo.getModelContent().getBytes());
            modelMapper.updateById(model);
        }
        return "success";
    }

    private String updateModelByWps(ModelUpdateVO vo) throws Exception {
        Model model = ModelConverter.INSTANCE.convert2Model(vo);
        //将wps的word文件转为pdf文件，用于前端展示
        Long pdfFileId = word2Pdf(vo.getRemoteFileId(), FileUploadPathEnum.MODEL.getCode());
        model.setRtfPdfFileId(pdfFileId);
        int i = modelMapper.updateById(model);
        return i == 1 ? "success" : "fail";
    }

    private void checkValue(ModelUpdateVO vo) {
        if (StringUtils.isEmpty(vo.getId())) {
            if (ObjectUtil.isEmpty(vo)) {
                throw exception(ErrorCodeConstants.MODEL_EMPTY_PARAM_ERROR);
            }
            if (StringUtils.isEmpty(vo.getId())) {
                throw exception(ErrorCodeConstants.MODEL_CHECK_ID);
            }
            if (StringUtils.isEmpty(vo.getName())) {
                throw exception(ErrorCodeConstants.MODEL_CHECK_NAME);
            }
            if (ObjectUtil.isEmpty(vo.getCategoryId())) {
                throw exception(ErrorCodeConstants.MODEL_CHECK_CATEGORY_ID);
            }
            if (StringUtils.isEmpty(vo.getContractType())) {
                throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE);
            }
            if (ObjectUtil.isEmpty(vo.getTimeEffectModel())) {
                throw exception(ErrorCodeConstants.MODEL_CHECK_TIME_EFFECT_MODEL);
            }

        }
    }

    private String updateModelByTemplate(ModelUpdateVO vo) throws Exception {
        Model updateModel = ModelConverter.INSTANCE.createVoToEntity(vo);
        String templateId = vo.getTemplateId();
        if (templateId != null) {
            if (StringUtils.isNotBlank(templateId)) {
                ContractTemplate template = templateMapper.selectById(templateId);
                if (template != null && template.getRemoteFileId() != null && template.getRemoteFileId() != 0) {
                    FileDTO fileDTO = fileApi.selectById(template.getRemoteFileId());
                    if (fileDTO != null) {
                        updateModel.setRemoteFileId(fileDTO.getId());
                    }
                }
            }
        }
        if (ObjectUtil.isNotNull(vo.getModelContent())) {
            Long rtf_fileId = rtfContentProcessor(vo, vo.getModelContent());
            updateModel.setRtfPdfFileId(rtf_fileId);
        }
//        updateModel.setApproveStatus(StatusEnums.NEVER_APPROVED.getCode());
        int i = modelMapper.updateById(updateModel);
        return i == 1 ? "success" : "fail";
    }

    private void modelTermProcessor(ModelUpdateVO vo, Model model) throws Exception {
        if (ModelTypeEnum.TERM_ADD == ModelTypeEnum.getInstance(vo.getType())) {
            //同步文件信息
            SimpleModel simpleModel = ModelConverter.INSTANCE.convertSimple(model);
            Model newModel = enhanceUpdate2Do(vo, simpleModel);
            if (ObjectUtil.isNotNull(vo.getModelContent())) {
                Long rtf_fileId = rtfContentProcessor(vo, vo.getModelContent());
                newModel.setRtfPdfFileId(rtf_fileId);
                newModel.setModelContent(vo.getModelContent().getBytes());
                modelMapper.updateById(newModel);
            }
            if (StringUtils.isNotBlank(vo.getTerms())) {
                List<ModelTermsAddVO> terms = JSONObject.parseArray(vo.getTerms(), ModelTermsAddVO.class);
                List<ModelTerm> convert = ModelTermConverter.INSTANCE.convert(terms, model);
                modelTermMapper.delete(new LambdaQueryWrapper<ModelTerm>().eq(ModelTerm::getModelId, model.getId()));
                modelTermMapper.insertBatch(convert);
            }
        }
    }

    private Model enhanceUpdate2Do(ModelUpdateVO vo, SimpleModel sqlModel) throws Exception {
        //必填项
        Model result = new Model();
        String sqlModelId = sqlModel.getId();
        result.setId(sqlModelId);
        result.setStatus(sqlModel.getStatus());
        result.setCode(vo.getCode());
        result.setName(vo.getName());
        result.setCategoryId(vo.getCategoryId());
        result.setContractType(vo.getContractType());

        ModelEffectEnum modelEffectEnum = ModelEffectEnum.getInstance(vo.getTimeEffectModel());
        if (modelEffectEnum == ModelEffectEnum.LIMITED_TIME_EFFECT) {
            result.setEffectStartTime(vo.getEffectStartTime());
            result.setEffectEndTime(vo.getEffectEndTime());
        }
        result.setTimeEffectModel(vo.getTimeEffectModel());

        //选填项
        //时效模式
        if (vo.getDescription() != null) {
            result.setDescription(vo.getDescription());
        }
        //绑定参数
        if (CollectionUtil.isNotEmpty(vo.getParamModelVoList())) {
            List<ParamModelVo> paramModelVoList = new ArrayList<ParamModelVo>();
            vo.getParamModelVoList().forEach(o -> {
                ParamModelVo paramModelVo = new ParamModelVo();
                paramModelVo.setParamId(o.getParamId());
                paramModelVo.setLocation(o.getParamId());
                paramModelVo.setParamNum(o.getParamNum());
                paramModelVo.setTagId(o.getTagId());
                paramModelVo.setId(o.getId());
                paramModelVoList.add(paramModelVo);
            });
            //插入参数模板
//            List<ParamModelVo> finalParamModelList = vo.getParamModelVoList();
            List<ParamModel> params = ParamModelConverter.INSTANCE.toEntityList(vo.getParamModelVoList());
            params.stream().forEach(paramModel -> paramModel.setModelId(sqlModelId));
            //删除旧参数数据
            paramModelService.remove(new LambdaQueryWrapperX<ParamModel>().eq(ParamModel::getModelId, sqlModelId));
            //插入新数据
            paramModelService.saveBatch(params);
        }

        //是否更新文件
        if (vo.getFile() != null) {
            MultipartFile file = vo.getFile();

            String fileName = file.getOriginalFilename();

            //将pdf文件和生成的图片统一都放入远端的uuid目录中。
            String folderId = "MODEL-" + IdUtil.fastSimpleUUID();

            String pdfFileName = "";
            //转pdf保存到pdf根目录
            String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
            String tempWordFile = localFolderPath + "/" + fileName;
            String pdfPath = localFolderPath;
            String pdfPicPath = localFolderPath + "/pics";
            int pageCount = 0;
            FileUtil.mkdir(pdfPicPath);

            //（1)pdf暂存本地目录（本地temp目录。且子目录pics下存储图片）
            InputStream ins = file.getInputStream();

            File newFile = new File(tempWordFile);

            //将输入流传给临时文件，不存在则会创建。存在则会覆盖。
            FileUtils.copyInputStreamToFile(ins, newFile);

            //如果文件格式是pdf,就不用转换
            if (StringUtils.equals(SUFFIX_PDF, FileNameUtil.extName(fileName))) {
                pdfFileName = file.getOriginalFilename();
                pdfPath = pdfPath + "/" + fileName;

            } else if (StringUtils.equals(SUFFIX_DOC, FileNameUtil.extName(fileName)) || StringUtils.equals(SUFFIX_DOCX, FileNameUtil.extName(fileName))) {
                assert fileName != null;
                pdfFileName = EcontractUtil.exchangeName2pdf(fileName);

                pdfPath = pdfPath + "/" + pdfFileName;
                //将流式文件转换成版式文件,并得到页数
                AsposeUtil.docx2Pdf(tempWordFile, pdfPath); //红色水印Evaluation Only.
                pageCount = EcontractUtil.getFilePageFromPDF(pdfPath);
            }

            File pdfFile = new File(pdfPath);
            FileInputStream fileInputStream = new FileInputStream(pdfFile);
            MultipartFile multipartPdfFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(), null, fileInputStream);

            //将PDF文件解析出多个图片
            PDF2JPGConverter pcv = new PDF2JPGConverter();
            pcv.convertAll(pdfPath, pdfPicPath, PDF_PICS_DPI);

            //将pdf根目录上传
            String uploadPath = EcontractUtil.getRemoteFolderPath(MODULE_MODEL, folderId);
            String picsUploadPath = uploadPath + "/" + "pics";

//            Long uploadId = fileApi.uploadFile(pdfFileName, uploadPath + "/" + pdfFileName, IoUtil.readBytes(multipartPdfFile.getInputStream()), pageCount);
            Long uploadId = fileApi.uploadFile(pdfFileName, FileUploadPathEnum.TEMPLATE.getPath(vo.getCode(), pdfFileName), IoUtil.readBytes(multipartPdfFile.getInputStream()), pageCount);
            //将旧的文件和图片删除
//            fileApi.deleteFile(sqlModel.getRemoteFileId());
            deletePics(vo.getId());

            //存文件id
            result.setRemoteFileId(uploadId);
        }
        result.setRemoteFileId(sqlModel.getRemoteFileId());
        return result;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object deleteModels(List<String> ids) throws Exception {
        if (CollUtil.isEmpty(ids)) {
            throw exception(ErrorCodeConstants.MODEL_EMPTY_PARAM_ERROR);
        }
        List<SimpleModel> models = simpleModelMapper.selectBatchIds(ids);
        // 只有处于待送审和审核未通过的才允许删除
        if (!isModifiable(models)) {
            throw exception(ErrorCodeConstants.MODEL_STATUS_NOT_MODIFIABLE);
        }
        modelMapper.deleteBatchIds(ids);
        //删除模板条款关联关系
        modelTermMapper.delete(new LambdaQueryWrapper<ModelTerm>().in(ModelTerm::getModelId, ids));
        //删除参数与模板的关联关系
        paramModelMapper.delete(new LambdaQueryWrapper<ParamModel>().in(ParamModel::getModelId, ids));
        //删除模版与范本的关联关系
        templateQuoteMapper.delete(new LambdaQueryWrapper<TemplateQuoteDO>().in(TemplateQuoteDO::getModelId, ids));
        //删除模版关联品目关系表数据
        modelCatalogMapper.delete(new LambdaQueryWrapper<ModelCatalogDo>().in(ModelCatalogDo::getModelId, ids));
        //删除相关图片
        for (String id : ids) {
            deletePics(id);
        }
        return true;
    }

    /**
     * 是否可以改动
     * (如果含有处于审批通过或者处理中的记录，则返回false)
     */
    private Boolean isModifiable(List<SimpleModel> models) {
        boolean result = true;
        for (SimpleModel model : models) {
            StatusEnums instanceResultEnum = StatusEnums.getInstance(model.getApproveStatus());
            if (ObjectUtil.isNotNull(instanceResultEnum)) {
                switch (instanceResultEnum) {
                    case APPROVING:
                    case APPROVED:
                        result = false;
                        break;
                    default:
                        return result;
                }
            }
        }
        return result;
    }

    /**
     * 暂时不用实现
     */
    @Override
    public Object submitApproval(@Valid ModelSubmitReqVo vo) {
//        String id = vo.getId();
//        Model model = modelMapper.selectById(id);
//        model.setApproveStatus(StatusEnums.APPROVED.getCode());
//        //暂定直接审批通过，存入当前时间
//        LocalDateTime date = DateUtil.parseLocalDateTime(DateUtil.now());
//        model.setApproveTime(date);
//        return modelMapper.updateById(model);
        return null;
    }

    @Override
    public ModelSingleRespVo getModel(String id) throws Exception {
        Model model = modelMapper.selectById(id);
        if (model == null) {
            return new ModelSingleRespVo();
        }
        ModelSingleRespVo vo = ModelConverter.INSTANCE.voToModel(model);
        ModelTimeEffectEnums enums = ModelTimeEffectEnums.getInstance(model.getTimeEffectModel());
        if (enums != null) {
            vo.setTimeEffectModelStr(enums.getInfo());
        }
        //
        ModelTypeEnum modelTypeEnum = ModelTypeEnum.getInstance(model.getType());

        //如果是范本生成的模板
        if (ModelTypeEnum.TEMPLATE == modelTypeEnum || ModelTypeEnum.TEMPLATE_UPLOAD == modelTypeEnum) {
            ContractTemplate template = templateMapper.selectById(model.getTemplateId());
            if (template != null) {
                if (template.getSourceFileId() != null && template.getSourceFileId() != 0) {
                    FileDTO fileDTO = fileApi.selectById(template.getSourceFileId());
                    vo.setTemplateFileId(fileDTO.getId());
                    vo.setTemplateFileName(fileDTO.getName());
                } else if (template.getRemoteFileId() != null && template.getRemoteFileId() != 0) {
                    FileDTO fileDTO = fileApi.selectById(template.getRemoteFileId());
                    vo.setTemplateFileId(fileDTO.getId());
                    vo.setTemplateFileName(fileDTO.getName());
                }
                vo.setTemplateName(template.getName());

            }
        }
        //只暴露给前端统一的文件id

        if (ModelTypeEnum.TERM_ADD == modelTypeEnum || ModelTypeEnum.TEMPLATE == modelTypeEnum) {
            //条款方式
            modelTermPreprocessor(vo);
        } else {
            //如果是上传文件
            vo.setFileId(model.getRtfPdfFileId());

            //WPS方法(wps新增文件时，已经转成pdf并存入rtfPdfFileId，所以用rtfPdfFileId交给前端展示pdf文件)
            vo.setFileId(ModelTypeEnum.WPS_UPLOAD == ModelTypeEnum.getInstance(model.getType()) ? model.getRtfPdfFileId() : model.getRemoteFileId());


        }
        //最后都是展示rtf_pdf文件
        vo.setFileId(model.getRtfPdfFileId());

        List<ModelPageRespVO> modelHistoryV1 = this.getModelHistoryV1(vo.getCode());
        if (CollectionUtil.isNotEmpty(modelHistoryV1)) {
            vo.setHistoryList(modelHistoryV1);
        }

        //获取审批信息
        List<ModelBpmDO> modelBpmDOList = modelBpmMapper.selectList(new LambdaQueryWrapperX<ModelBpmDO>().eq(ModelBpmDO::getModelId, vo.getId()).orderByDesc(ModelBpmDO::getCreateTime));
        if (CollectionUtil.isNotEmpty(modelBpmDOList)) {
            ModelBpmDO modelBpmDO = modelBpmDOList.get(0);
            List<BpmProcessRespDTO> activityRecordByProcessInstanceId = bpmActivityApi.getActivityRecord(modelBpmDO.getProcessInstanceId());
            if (CollectionUtil.isNotEmpty(activityRecordByProcessInstanceId)) {
                vo.setBpmProcessRespVOList(activityRecordByProcessInstanceId);
            }
        }

        ModelCategory modelCategory = modelCategoryMapper.selectById(model.getCategoryId());
        if (modelCategory != null) {
            vo.setCategoryStr(modelCategory.getName());
        }
        ContractType contractType = contractTypeMapper.selectById(model.getContractType());
        if (ObjectUtil.isNotNull(contractType)) {
            vo.setContractTypeStr(contractType.getName());
        }
        PlatformEnums platformEnums = PlatformEnums.getInstance(model.getPlatform());
        if (ObjectUtil.isNotNull(platformEnums)) {
            vo.setPlatformName(platformEnums.getInfo());
        }
        //品目
        enhancePurCatalog(vo);

        // 如果返回的模板文件id为空，就生成一个文件
        if (ObjectUtil.isEmpty(vo.getRtfPdfFileId()) && ObjectUtil.isNotNull(vo.getModelContent())) {
            ModelUpdateVO modelUpdateVO = BeanUtils.toBean(vo, ModelUpdateVO.class);
            Long rtf_fileId = rtfContentProcessor(modelUpdateVO, vo.getModelContent());
            model.setRtfPdfFileId(rtf_fileId);
            modelMapper.updateById(model);
            vo.setRtfPdfFileId(rtf_fileId);
        }
        return vo;
    }

    private void enhancePurCatalog(ModelSingleRespVo vo) {
        List<ModelCatalogDo> modelCatalogDos = modelCatalogMapper.selectList(ModelCatalogDo::getModelId, vo.getId());
        if (CollectionUtil.isNotEmpty(modelCatalogDos)) {
            List<PurCatalogRespVO> purCatalogVOList = new ArrayList<>();
            for (ModelCatalogDo modelCatalogDo : modelCatalogDos) {
                PurCatalogRespVO purCatalogVO = new PurCatalogRespVO()
                        .setPurCatalogGuid(modelCatalogDo.getCatalogGuid())
                        .setPurCatalogName(modelCatalogDo.getCatalogName())
                        .setPurCatalogCode(modelCatalogDo.getCatalogCode());
                purCatalogVOList.add(purCatalogVO);
            }
            vo.setPurCatalogVOList(purCatalogVOList);
        }
    }

    /**
     * 获得流程信息
     */
    private List<CommonOfModelAndTemplateBpmProcessRespVO> getProcessInfo(Model model) {
        //找出最近得一条流程记录
        List<ModelBpmDO> modelBpmDOList = modelBpmMapper.selectList(new LambdaQueryWrapperX<ModelBpmDO>().eq(ModelBpmDO::getModelId, model.getId()).orderByDesc(ModelBpmDO::getCreateTime).last("LIMIT 1"));
        //未发起审批，审批流程为空。
        if (CollectionUtil.isEmpty(modelBpmDOList)) {
//            List<BpmProcessResDTO> bpmProcessResDTOList = bpmActivityApi.getActivityAssignInfoByApproveCode(MODEL_TYPE);
//            return ModelConverter.INSTANCE.convertDTO2RespVO(bpmProcessResDTOList);
            return Collections.emptyList();
        }
        ModelBpmDO modelBpmDO = modelBpmDOList.get(0);
        List<BpmProcessRespDTO> bpmProcessResDTOList = bpmActivityApi.getActivityRecord(modelBpmDO.getProcessInstanceId());
        if (CollectionUtil.isEmpty(bpmProcessResDTOList)) {
            return Collections.emptyList();
        }
        return ModelConverter.INSTANCE.convertDTO2RespVO(bpmProcessResDTOList);
    }

    /**
     * 条款模板的单个查看方法
     * 将模板关联的条款找出并加入相应参数的list字段
     */
    private void modelTermPreprocessor(ModelSingleRespVo vo) {

        List<ModelTerm> modelTerms = modelTermMapper.selectList(ModelTerm::getModelId, vo.getId());
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
                            .setShowName(termInfo.getShowName())
                            .setTermKind(termInfo.getTermKind())
                            .setTermKindName(termInfo.getTermKindName())
                            .setEnableEdit(termInfo.getEnableEdit())
                            .setTitle(item.getTitle())
                            .setEditable(item.getEditable())
                            .setName(item.getName())
                            .setTermComment(termInfo.getTermComment()));
                }
            });
            vo.setTerms(result);
        }
    }

    /**
     * 模板中是否含有启动状态的
     */
    public boolean isStatusActive(List<SimpleModel> models) {
        return models.stream().anyMatch(model -> STATUS_IS_ACTIVE == model.getStatus());
    }

    /**
     * 批量上传图片
     */
    public void uploadPics(List<MultipartFile> files, String uploadPath, String modelId) {
        List<ModelPic> list = new ArrayList<ModelPic>();
        try {
            //将目录中的图片全部上传
            for (MultipartFile file : files) {
                ModelPic modelPic = new ModelPic();
                modelPic.setModelId(modelId);
                long picId = fileApi.uploadFile(file.getName(), uploadPath + "/" + file.getName(), IoUtil.readBytes(file.getInputStream()));
                modelPic.setFileId(picId);
                list.add(modelPic);
            }
            //将模板图片的关系批量入库
            modelPicMapper.insertBatch(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw exception(ErrorCodeConstants.MODEL_INSERT_UPLOAD_ERROR);
        }
    }

    /**
     * 删除相关图片
     */
    public void deletePics(String modelId) throws Exception {
        //得到相关图片的文件id
        List<Long> fileIds = modelPicMapper.selectList(new LambdaQueryWrapperX<ModelPic>().eq(ModelPic::getModelId, modelId)).stream().map(ModelPic::getFileId).collect(Collectors.toList());
        //将这个图片文件都删除
        for (Long fileId : fileIds) {
            fileApi.deleteFile(fileId);
        }
    }

    /**
     * 直接保存并发送审批
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertModelAndSubmitApprove(ModelCreateSubmitReqVO vo) throws Exception {
        ModelIdCodeRespVO modelIdCodeRespVO = insertModelByUpload(vo);
        String modelId = modelIdCodeRespVO.getId();
        ModelBpmSubmitCreateReqVO modelBpmSubmitCreateReqVO = new ModelBpmSubmitCreateReqVO().setModelId(modelId).setApproveIntroduction(vo.getApproveIntroduction());
        return modelBpmService.submitModelApproveFlowable(getLoginUserId(), modelBpmSubmitCreateReqVO);
    }

    /**
     * 直接编辑并发送审批
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateModelAndSubmitApprove(ModelUpdateSubmitReqVO vo) throws Exception {
        updateModel(vo);
        ModelBpmSubmitCreateReqVO modelBpmSubmitCreateReqVO = new ModelBpmSubmitCreateReqVO().setModelId(vo.getId()).setApproveIntroduction(vo.getApproveIntroduction());
        return modelBpmService.submitModelApproveFlowable(getLoginUserId(), modelBpmSubmitCreateReqVO);
    }

    /**
     * wps文件保存的模板
     */
    public String insertModelByWps(ModelCreateReqVO vo) throws Exception {
        //参数转化
        //将日期字符串转化成Date
        if (StringUtils.isNotBlank(vo.getEffectStartTimeReciever())) {
            vo.setEffectStartTime(EcontractUtil.getDateFromSimpleStr(vo.getEffectStartTimeReciever()));
        }
        if (StringUtils.isNotBlank(vo.getEffectEndTimeReciever())) {
            vo.setEffectEndTime(EcontractUtil.getDateFromSimpleStr(vo.getEffectEndTimeReciever()));
        }
        Model model = ModelConverter.INSTANCE.toEntity(vo);
        model.setRemoteFileId(vo.getRemoteFileId());
        //将wps的word文件转为pdf文件，用于前端展示
        Long pdfFileId = word2Pdf(vo.getRemoteFileId(), FileUploadPathEnum.MODEL.getCode());
        model.setRtfPdfFileId(pdfFileId);
        modelMapper.insert(model);
        return model.getId();
    }

    public void enhanceModelEffect(ModelCreateReqVO vo) {
        ModelEffectEnum modelEffectEnum = ModelEffectEnum.getInstance(vo.getTimeEffectModel());
        if (ModelEffectEnum.LIMITED_TIME_EFFECT == modelEffectEnum) {
            //将日期字符串转化成Date
            vo.setEffectStartTime(EcontractUtil.getDateFromSimpleStr(vo.getEffectStartTimeReciever()));
            vo.setEffectEndTime(EcontractUtil.getDateFromSimpleStr(vo.getEffectEndTimeReciever()));
        }
    }

    /**
     * 根据合同类型，获得模板
     * 返回模板id和名称
     */
    @Override
    public ModelGetContractTypeRespVO getModelByContractType(IdReqVO vo) {
        List<SimpleModel> modelList = simpleModelMapper.selectList(SimpleModel::getContractType, vo.getId());
        if (CollectionUtil.isNotEmpty(modelList)) {
            SimpleModel model = modelList.get(0);
            return new ModelGetContractTypeRespVO().setModelId(model.getId()).setModelName(model.getName());
        }
        return new ModelGetContractTypeRespVO();

//        //判断当前用户是否有wps权限
//        AdminUserRespDTO userRespDTO = userApi.getUser(SecurityFrameworkUtils.getLoginUserId());
//        if (ObjectUtil.isNotNull(userRespDTO)) {
//            if (WPS_USER.equals(userRespDTO.getDeptId())) {
//
//                //有权限，则找到wps文件id即可
//                List<SimpleModel> modelList = simpleModelMapper.selectList(SimpleModel::getContractType, vo.getId());
//                if (CollectionUtil.isNotEmpty(modelList)) {
//                    SimpleModel wpsModel = modelList.get(0);
//                    return new ModelGetContractTypeRespVO().setFileId(wpsModel.getRemoteFileId());
//                }
//            }
//
//            //该用户没wps权限，则返回富文本
//            List<Model> models = modelMapper.selectList(Model::getContractType, vo.getId());
//            if (CollectionUtil.isNotEmpty(models)) {
//                Model rtfModel = models.get(0);
//                return new ModelGetContractTypeRespVO().setModelContent(rtfModel.getModelContent());
//            }

//        }
//        return new ModelGetContractTypeRespVO();
    }

    /**
     * 根据文件id，找到文件，生成pdf
     * 返回pdf文件id
     * fileUploadPathEnumCode： {@link FileUploadPathEnum} 6=范本，7=模板
     */
    //docx生成pdf
    private Long word2Pdf(Long wordFileId, Integer fileUploadPathEnumCode) throws Exception {
        FileUploadPathEnum fileUploadPathEnum = FileUploadPathEnum.getInstance(fileUploadPathEnumCode);
        ThreadUtil.sleep(3000);
        //下载word文件
        String wordFileName = fileApi.getName(wordFileId);
        String mainFileName = FileNameUtil.mainName(fileApi.getName(wordFileId));
        String pdfFileName = mainFileName + FILE_TYPE_PDF;
        String folderId = IdUtil.fastSimpleUUID();
        String localFolderPath = READY_TO_UPLOAD_PATH + "/" + fileUploadPathEnum.getPath() + folderId;
        FileUtil.mkdir(localFolderPath);
        Long pdfFileId = new Long(0);
        ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(fileApi.getFileContentById(wordFileId));
        String wordFilePath = localFolderPath + "/" + fileApi.getName(wordFileId);
        String pdfFilePath = localFolderPath + "/" + pdfFileName;
        FileUtil.writeFromStream(byteArrayInputStream, wordFilePath);


        //将word文件转成pdf文件
        // YhAgentUtil.officeToPDF(wordFilePath, pdfFilePath);

        AsposeUtil.docx2Pdf(wordFilePath, pdfFilePath);
//
        //上传pdf文件
        File pdfFile = new File(pdfFilePath);
        FileInputStream fileInputStream = new FileInputStream(pdfFile);
        MultipartFile multipartPdfFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(), null, fileInputStream);
        pdfFileId = fileApi.uploadFile(pdfFileName, pdfFilePath, IoUtil.readBytes(multipartPdfFile.getInputStream()));

        //返回删除临时目录
        FileUtil.del(localFolderPath);
        return pdfFileId;
    }

    /**
     * 修改启用状态 -生效时间 启用字段
     */
    @Override
    public Boolean updateModelEnable(String id) {
        //根据id查询模板信息
        SimpleModel simpleModel = simpleModelMapper.selectById(id);
        if (ObjectUtil.isNotNull(simpleModel)) {
            if (simpleModel.getEffectStatus() == 0) {
                simpleModel.setEffectStatus(1);
            } else {
                simpleModel.setEffectStatus(0);
            }
        }
        int i = simpleModelMapper.updateById(simpleModel);
        if (i > 0) {
            //修改成功，修改模板状态
            return true;
        }
        return false;
    }

    /**
     * 修改收藏状态
     */
    @Override
    public Boolean updateModelCollect(String id) {
        if (ObjectUtil.isNotNull(id)) {
            SimpleModel simpleModel = simpleModelMapper.selectById(id);
            if (ObjectUtil.isNotNull(simpleModel)) {
                if (simpleModel.getCollect() == 0) {
                    simpleModel.setCollect(1);
                } else {
                    simpleModel.setCollect(0);
                }
            }
            int i = simpleModelMapper.updateById(simpleModel);
            if (i > 0) {
                //修改成功，修改模板状态
                return true;
            }
        }
        return false;
    }

    /**
     * 定时任务修改有效期生效状态
     */
    @PermitAll()
    @Override
    public void updateModelTimeEffect() {
        List<SimpleModel> simpleModelList = simpleModelMapper.selectList();
        if (CollectionUtil.isNotEmpty(simpleModelList)) {
            for (SimpleModel simpleModel : simpleModelList) {
                if (simpleModel.getTimeEffectModel() == 0) {
                    LocalDateTime effectStartTime = simpleModel.getEffectStartTime();
                    LocalDateTime effectEndTime = simpleModel.getEffectEndTime();
                    //获取当前时间
                    if (ObjectUtil.isNotNull(effectStartTime) && ObjectUtil.isNotNull(effectEndTime)) {
                        //获取当前时间
                        LocalDateTime now = LocalDateTime.now();
                        //获取当前时间是否在有效时间
                        if (now.isAfter(effectStartTime) && now.isBefore(effectEndTime)) {
                            simpleModel.setEffective(1);
                        } else {
                            simpleModel.setEffective(0);
                        }
                    }
                }
            }
        }
        simpleModelMapper.updateBatch(simpleModelList);
    }

    /**
     * API-模板列表
     */
    @Override
    public PageResult<ModelApiListRespVO> list(ModelApiListReqVO vo) {

        List<ClientModelCategory> categories = clientModelCategoryMapper.selectList(ClientModelCategory::getClientId, SecurityFrameworkUtils.getClientId());
        List<Integer> rootCategoryIds = categories.stream().map(ClientModelCategory::getCategoryId).collect(Collectors.toList());
        List<ModelCategory> modelCategoryList = getAllModelCategoryFromRoot(rootCategoryIds);
        List<Integer> modelCategoryIds = modelCategoryList.stream().map(ModelCategory::getId).collect(Collectors.toList());
        vo.setCategoryIdList(modelCategoryIds);
        PageResult<SimpleModel> doPage = simpleModelMapper.selectPage(vo);
        return enhanceApiPage(doPage);
    }

    private PageResult<ModelApiListRespVO> enhanceApiPage(PageResult<SimpleModel> doPage) {
        if (CollectionUtil.isEmpty(doPage.getList())) {
            return new PageResult<ModelApiListRespVO>().setList(Collections.emptyList()).setTotal(doPage.getTotal());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<ModelCategory> modelCategoryList = modelCategoryMapper.selectList();
        Map<Integer, ModelCategory> modelCategoryMap = new HashMap<Integer, ModelCategory>();
        if (CollectionUtil.isNotEmpty(modelCategoryList)) {
            modelCategoryMap = CollectionUtils.convertMap(modelCategoryList, ModelCategory::getId);
        }
        List<String> regionCodeList = doPage.getList().stream().map(SimpleModel::getRegionCode).collect(Collectors.toList());
        List<RegionDTO> regionList = regionApi.getRegionByCodes(regionCodeList);

        Map<String, RegionDTO> regionMap = new HashMap<String, RegionDTO>();
        if (CollectionUtil.isNotEmpty(regionList)) {
            regionMap = CollectionUtils.convertMap(regionList, RegionDTO::getRegionCode);
        }

        PageResult<ModelApiListRespVO> result = new PageResult<ModelApiListRespVO>();
        List<ModelApiListRespVO> listRespVOList = new ArrayList<ModelApiListRespVO>();
        for (SimpleModel entity : doPage.getList()) {
            ModelApiListRespVO respVO = new ModelApiListRespVO();
            respVO.setId(entity.getId());
            respVO.setTemplateCode(entity.getCode());
            respVO.setTemplateName(entity.getName());
            respVO.setCategoryId(String.valueOf(entity.getCategoryId()));
            ModelCategory modelCategory = modelCategoryMap.get(entity.getCategoryId());
            if (ObjectUtil.isNotNull(modelCategory)) {
                respVO.setCategoryName(modelCategory.getName());
            }
            respVO.setCreateTime(entity.getCreateTime().toInstant(ZoneOffset.UTC).toEpochMilli());

            ModelEffectTpeEnums modelEffectTpeEnums = ModelEffectTpeEnums.getInstance(entity.getTimeEffectModel());
            if (ObjectUtil.isNotNull(modelEffectTpeEnums)) {
                respVO.setEffectType(modelEffectTpeEnums.getInfo());
                if (TIME_LIMITED == modelEffectTpeEnums) {
                    String formattedStartDateTime = entity.getEffectStartTime().format(formatter);
                    respVO.setEffectStartTime(formattedStartDateTime);
                    String formattedEndDateTime = entity.getEffectEndTime().format(formatter);
                    respVO.setEffectEndTime(formattedEndDateTime);
                }
            }
            respVO.setVersion(String.valueOf(entity.getVersion()));
            respVO.setRegionCode(entity.getRegionCode());
            RegionDTO region = regionMap.get(entity.getRegionCode());
            if (ObjectUtil.isNotNull(region)) {
                respVO.setRegionName(region.getRegionName());
            }
            listRespVOList.add(respVO);
        }
        return result.setList(listRespVOList).setTotal(doPage.getTotal());
    }

    /**
     * 模板查询
     */
    @Override
    public PageResult<ModelApiListRespVO> searchList(ModelApiListReqVO vo) {
        List<ClientModelCategory> categories = clientModelCategoryMapper.selectList(ClientModelCategory::getClientId, SecurityFrameworkUtils.getClientId());
        List<Integer> rootCategoryIds = categories.stream().map(ClientModelCategory::getCategoryId).collect(Collectors.toList());
        List<ModelCategory> modelCategoryList = getAllModelCategoryFromRoot(rootCategoryIds);
        List<Integer> modelCategoryIds = modelCategoryList.stream().map(ModelCategory::getId).collect(Collectors.toList());
        vo.setCategoryIdList(modelCategoryIds);
        PageResult<SimpleModel> doPage = simpleModelMapper.selectPage(vo);
        return enhanceApiPage(doPage);
    }

    @Override
    public String insertRtf4Model(ModelCreateReqVO reqVO) {
        Model model = ModelConverter.INSTANCE.vosToModel(reqVO);
        modelMapper.updateById(model);
        return model.getId();
    }

    @Override
    public List<ModelIdAndNameVO> getMyModelList() {
        String subQuery = "SELECT code, approve_status, MAX(version) AS max_version FROM ecms_model GROUP BY code, approve_status";
        LambdaQueryWrapper<SimpleModel> wrapperX = new LambdaQueryWrapper<SimpleModel>().inSql(SimpleModel::getId, "SELECT id FROM ecms_model WHERE (code, approve_status, version) IN (" + subQuery + ") AND approve_status = 2").orderByDesc(SimpleModel::getCreateTime);
        wrapperX.select(SimpleModel::getId, SimpleModel::getName);
        List<SimpleModel> list = simpleModelMapper.selectList(wrapperX);
        List<ModelIdAndNameVO> result = ModelConverter.INSTANCE.toIdAndNameList(list);
        return result;
    }

    @Override
    public void getModelByOrgId(String orgId) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("client_id", clientId);
        bodyParam.put("client_secret", clientSecret);
        String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);

        // 同步黑龙江政采模板
        List<ModelDTO> modelListByOrgId = contractProcessApi.getModelListByOrgId(token, orgId);
        List<Model> models = BeanUtils.toBean(modelListByOrgId, Model.class);
        models.forEach(item -> item.setTenantId(getLoginUser().getTenantId()));
        modelMapper.insertBatch(models);
    }


    List<ModelCategory> getAllModelCategoryFromRoot(List<Integer> rootCategoryIds) {
        List<ModelCategory> leafCategories = new ArrayList<>(); // 存放叶子节点的ModelCategory列表
        List<ModelCategory> allModelCategories = modelCategoryMapper.selectList();// 所有的ModelCategory数据

        for (Integer rootCategoryId : rootCategoryIds) {
            findLeafCategories(rootCategoryId, allModelCategories, leafCategories);
        }
        return leafCategories;
    }

    private void findLeafCategories(Integer parentId, List<ModelCategory> allModelCategories, List<ModelCategory> leafCategories) {
        for (ModelCategory category : allModelCategories) {
            if (category.getParentId().equals(parentId)) {
                if (hasChildren(category.getId(), allModelCategories)) {
                    findLeafCategories(category.getId(), allModelCategories, leafCategories);
                } else {
                    leafCategories.add(category);
                }
            }
        }
    }

    private boolean hasChildren(Integer parentId, List<ModelCategory> allModelCategories) {
        for (ModelCategory category : allModelCategories) {
            if (category.getParentId().equals(parentId)) {
                return true;
            }
        }
        return false;
    }

    private LambdaQueryWrapper<SimpleModel> enhanceWrapperByCategoryIds(LambdaQueryWrapper<SimpleModel> wrapperX, ModelListReqVO vo) {
        if (ObjectUtil.isNull(vo.getCategoryId())) {
            return wrapperX;
        }
        //模板分类的树状图搜索条件
        List<Integer> rootCategoryIds = new ArrayList<Integer>();
        rootCategoryIds.add(vo.getCategoryId());
        List<ModelCategory> categoryList = getAllModelCategoryFromRoot(rootCategoryIds);
        List<Integer> leafCategoryIds = new ArrayList<Integer>();
        if (CollectionUtil.isNotEmpty(categoryList)) {
            leafCategoryIds = categoryList.stream().map(ModelCategory::getId).collect(Collectors.toList());
            vo.setCategoryIdList(leafCategoryIds);
        } else {
            leafCategoryIds.add(vo.getCategoryId());
            vo.setCategoryIdList(leafCategoryIds);
        }
        wrapperX.in(SimpleModel::getCategoryId, leafCategoryIds);

        return wrapperX;
    }


    @Resource
    private ModelClientMapper modelClientMapper;

    @Override
    public List<ModelClientRespVO> listModelClient() {
        List<ModelClientDO> modelClientDOList = modelClientMapper.selectList();
        List<ModelClientRespVO> result = ModelConverter.INSTANCE.listClientDo2Resp(modelClientDOList);
        return result;
    }

    private void selectAllChildTypes(List parentIds, List typeIds) {
        typeIds.addAll(parentIds);
        List<ContractType> list = contractTypeMapper.selectList(ContractType::getParentId, parentIds);
        if (CollectionUtil.isNotEmpty(list)) {
            List<String> ids = list.stream().map(getId -> getId.getId()).collect(Collectors.toList());
            selectAllChildTypes(ids, typeIds);
        }
    }
}
