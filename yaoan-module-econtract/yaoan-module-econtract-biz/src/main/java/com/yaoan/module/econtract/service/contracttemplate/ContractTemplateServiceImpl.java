package com.yaoan.module.econtract.service.contracttemplate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.query.QueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.bpm.api.bpm.activity.BpmActivityApi;
import com.yaoan.module.bpm.api.bpm.activity.dto.BpmProcessRespDTO;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.CommonOfModelAndTemplateBpmProcessRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.template.vo.TemplateBpmSubmitCreateReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractToPdfVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.ContractPerformanceAcceptanceRespVO;
import com.yaoan.module.econtract.controller.admin.contracttemplate.vo.*;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.performtasktype.vo.DeleteVO;
import com.yaoan.module.econtract.convert.contracttemplate.ContractTemplateConverter;
import com.yaoan.module.econtract.convert.contracttemplate.TemplateTermConverter;
import com.yaoan.module.econtract.convert.term.TermConverter;
import com.yaoan.module.econtract.dal.dataobject.bpm.template.TemplateBpmDO;
import com.yaoan.module.econtract.dal.dataobject.category.TemplateCategory;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.*;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.mysql.bpm.template.TemplateBpmMapper;
import com.yaoan.module.econtract.dal.mysql.category.TemplateCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.*;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.StatusEnums;
import com.yaoan.module.econtract.enums.TemplateEnums;
import com.yaoan.module.econtract.service.bpm.template.TemplateBpmService;
import com.yaoan.module.econtract.service.contract.ContractService;
import com.yaoan.module.econtract.service.cx.ChangXieService;
import com.yaoan.module.econtract.util.AsposeUtil;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.PDF2JPGConverter;
import com.yaoan.module.econtract.util.YhAgentUtil;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.infra.api.file.dto.FilesDTO;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;
import static com.yaoan.module.econtract.enums.ModuleConstants.MODULE_CONTRACT_TEMPLATE;
import static com.yaoan.module.econtract.enums.StatusConstants.*;
import static com.yaoan.module.econtract.enums.TemplateEnums.*;
import static dm.jdbc.desc.Configuration.map;

/**
 * @author Pele
 * @description 针对表【ecms_contract_template】的数据库操作Service实现
 * @createDate 2023-08-09 16:13:20
 */
@Slf4j
@Service
public class ContractTemplateServiceImpl extends ServiceImpl<ContractTemplateMapper, ContractTemplate> implements ContractTemplateService {

    static final int TRUECONSTANT = 1;
    static final Integer TEMPLATE_TYPE = 1;
    @Resource
    private ContractTemplateMapper contractTemplateMapper;
    @Resource
    private TemplatePicMapper templatePicMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private TemplateCategoryMapper templateCategoryMapper;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private TemplateBpmMapper templateBpmMapper;
    @Resource
    private BpmActivityApi bpmActivityApi;
    @Resource
    private TemplateBpmService templateBpmService;
    @Resource
    private TemplateQuoteMapper templateQuoteMapper;
    @Resource
    private BpmTaskApi bpmTaskApi;
//    @Resource
//    private RegionApi regionApi;

    @Resource
    private TemplateTermMapper templateTermMapper;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private ContractTemplateTermRelMapper contractTemplateTermRelMapper;
    @Resource
    private TermMapper termMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private ContractService contractService;

    /**
     * 范本制作列表
     * （由于文件id逻辑变动，且前端还没使用缩略图，暂缓展示缩略图字段）
     */
    @Override
    public PageResult<TemplateSimpleVo> getMyAllTemplatePage(TemplateListReqVo vo) throws Exception {
        List<TemplateCategory> allCategories = templateCategoryMapper.selectList();// 所有的ModelCategory数据
        vo.setAllCategories(allCategories);
        //得到子分类
        List<TemplateCategory> finalTemplateCategoryList = templateCategoryMapper.selectList(TemplateCategory::getParentId, vo.getTemplateCategoryId());
        if (CollectionUtil.isNotEmpty(finalTemplateCategoryList)) {
            List<Integer> categoryIds = finalTemplateCategoryList.stream().map(TemplateCategory::getId).collect(Collectors.toList());
            categoryIds.add(vo.getTemplateCategoryId());
            vo.setCategoryIdList(categoryIds);
        }

        //当前用户的id
        PageResult<ContractTemplate> templPr = contractTemplateMapper.getMyAllTemplatePage(vo);
        List<ContractTemplate> list = templPr.getList();
        if (CollectionUtil.isEmpty(list)) {
            return new PageResult<TemplateSimpleVo>()
                    .setList(Collections.emptyList())
                    .setTotal(templPr.getTotal())
                    ;
        }
        List<TemplateSimpleVo> finalList = ContractTemplateConverter.INSTANCE.convert2SimpleVo(list);
        List<Long> userIds = list.stream().map(ContractTemplate::getCreator).collect(Collectors.toList()).stream().map(Long::parseLong).collect(Collectors.toList());
//        List<FileDTO> fileDTOList = fileApi.getContractFileList(userIds);
//        Map<Long, FileDTO> fileDTOMap = CollectionUtils.convertMap(fileDTOList, FileDTO::getId);
        List<AdminUserRespDTO> users = adminUserApi.getUserNickList(userIds);
        Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(users, AdminUserRespDTO::getId);

//        List<TemplateCategory> templateCategoryList = templateCategoryMapper.selectList();
        Map<Integer, TemplateCategory> templateCategoryMap = CollectionUtils.convertMap(allCategories, TemplateCategory::getId);

        //------------ 获取任务id --------------
        Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
//        Map<String, BpmTaskAllInfoRespVO> taskEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();

        List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList;
//        List<BpmTaskAllInfoRespVO> taskEndTimeAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

        List<String> idList = list.stream().map(ContractTemplate::getId).collect(Collectors.toList());
//        Map<String, ContractTemplate> modelMap = CollectionUtils.convertMap(list, ContractTemplate::getId);
        List<TemplateBpmDO> modelBpmDOList = templateBpmMapper.selectList(new LambdaQueryWrapperX<TemplateBpmDO>().inIfPresent(TemplateBpmDO::getTemplateId, idList)
                .select(TemplateBpmDO::getTemplateId, TemplateBpmDO::getProcessInstanceId, TemplateBpmDO::getResult));

        Map<String, TemplateBpmDO> modelBpmDOMap = CollectionUtils.convertMap(modelBpmDOList, TemplateBpmDO::getTemplateId);
        List<String> instanceList = modelBpmDOList.stream().map(TemplateBpmDO::getProcessInstanceId).collect(Collectors.toList());

        //待处理的任务
        List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList;
        Map<String, BpmTaskAllInfoRespVO> toDoTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();

        if (CollectionUtil.isNotEmpty(instanceList)) {
            taskAllInfoRespVOList = bpmTaskApi.getAllTaskIdAndAssigneeNameByProcessInstanceIds(getLoginUserId(), instanceList);
            taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
            taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

            //待处理的任务
            toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTime(taskAllInfoRespVOList);
            toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
        }
        //赋值
        List<ContractType> contractTypeList = new ArrayList<>();
        Map<String, ContractType> contractTypeMap = new HashMap<>();
        contractTypeList = contractTypeMapper.selectList();
        if (CollectionUtil.isNotEmpty(contractTypeList)) {
            contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
        }

        for (TemplateSimpleVo simpleVo : finalList) {
            simpleVo.setTemplateCategoryName(getTemplateCategoryName(templateCategoryMap, simpleVo.getTemplateCategoryId()));
            ContractType contractType = contractTypeMap.get(simpleVo.getContractType());
            if (ObjectUtil.isNotNull(contractType)) {
                simpleVo.setContractTypeName(contractType.getName());
            }

            TemplateBpmDO bpmDO = modelBpmDOMap.get(simpleVo.getId());
            if (ObjectUtil.isNotNull(bpmDO)) {
                simpleVo.setProcessInstanceId(bpmDO.getProcessInstanceId());
                BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(bpmDO.getResult());
                if (ObjectUtil.isNotNull(resultEnum)) {
                    //被退回的任务id
                    if (BpmProcessInstanceResultEnum.BACK == resultEnum) {
                        BpmTaskAllInfoRespVO taskAllInfoRespVO = taskMap.get(bpmDO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(taskAllInfoRespVO)) {
                            simpleVo.setTaskId(taskAllInfoRespVO.getTaskId());
                        }
                    }

                    //待办任务的被分配人
                    if (BpmProcessInstanceResultEnum.APPROVE != resultEnum) {
                        BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = toDoTaskMap.get(simpleVo.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
                            simpleVo.setAssigneeId(bpmTaskAllInfoRespVO.getAssigneeUserId());
                        }
                    }
                }
            }

            //每个文件的后一位就是相关图片的第一张。
//            Long firstPicId = simpleVo.getRemoteFileId() + 1;
//            String path = fileApi.getURL(firstPicId);
//            String path=
//            simpleVo.setPicPath(path);
//            List<TemplatePic> pics = templatePicMapper.selectList(
//                    new LambdaQueryWrapperX<TemplatePic>()
//                            .eq(TemplatePic::getTemplateId, simpleVo.getId()));
//            simpleVo.setPageCount(String.valueOf(pics.size()));
            if (userMap.get(Long.valueOf(simpleVo.getCreator())) != null) {
                AdminUserRespDTO creator = userMap.get(Long.valueOf(simpleVo.getCreator()));
                AdminUserRespDTO updater = userMap.get(Long.valueOf(simpleVo.getUpdater()));
                if (ObjectUtil.isNotNull(creator)) {
                    simpleVo.setCreatorName(creator.getNickname());
                }
                if (ObjectUtil.isNotNull(updater)) {
                    simpleVo.setCreatorName(updater.getNickname());

                }
            }

            //转换范本来源str
            TemplateEnums templateEnums = TemplateEnums.getInstance(simpleVo.getTemplateSource());
            if (templateEnums != null) {
                simpleVo.setTemplateSourceStr(templateEnums.getInfo());
            }

            //富文本文件id
            StatusEnums statusEnum = StatusEnums.getInstance(simpleVo.getApproveStatus());
            simpleVo.setApproveStatusStr(statusEnum == null ? "" : statusEnum.getInfo());
        }

        PageResult<TemplateSimpleVo> pageResult = new PageResult<>();

        pageResult.setList(finalList);
        pageResult.setTotal(templPr.getTotal());
        return pageResult;
    }

    /**
     * 范本库列表
     */
    @Override
    public PageResult<TemplateAllPermissionReqVo> getAllTemplates(TemplateListReqVo vo) throws Exception {
        PageResult<ContractTemplate> templPr = contractTemplateMapper.queryTemplatePage(vo);
        if (CollectionUtil.isEmpty(templPr.getList())) {
            return new PageResult<>();
        }
        List<ContractTemplate> list = templPr.getList();
        List<Long> userIds = list.stream().map(ContractTemplate::getCreator).collect(Collectors.toList()).stream().map(Long::parseLong).collect(Collectors.toList());

        List<TemplateAllPermissionReqVo> finalList = ContractTemplateConverter.INSTANCE.convert2AllVo(list);

        List<TemplateCategory> templateCategoryList = templateCategoryMapper.selectList();
        Map<Integer, TemplateCategory> templateCategoryMap = CollectionUtils.convertMap(templateCategoryList, TemplateCategory::getId);
        List<FileDTO> fileDTOList = fileApi.getContractFileByUserList(userIds);
        Map<Long, FileDTO> fileDTOMap = CollectionUtils.convertMap(fileDTOList, FileDTO::getId);

        List<ContractType> contractTypeList = new ArrayList<>();
        Map<String, ContractType> contractTypeMap = new HashMap<>();
        contractTypeList = contractTypeMapper.selectList();
        if (CollectionUtil.isNotEmpty(contractTypeList)) {
            contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
        }

        for (TemplateAllPermissionReqVo simpleVo : finalList) {

            ContractType contractType = contractTypeMap.get(simpleVo.getContractType());
            if (ObjectUtil.isNotNull(contractType)) {
                simpleVo.setContractTypeName(contractType.getName());
            }
            FileDTO file = fileDTOMap.get(simpleVo.getRemoteFileId());
            simpleVo.setPageCount(ObjectUtil.isNotNull(file) ? file.getPageCount() : null);

            TemplateEnums templateEnums = TemplateEnums.getInstance(simpleVo.getTemplateSource());

            simpleVo.setTemplateSourceStr(ObjectUtil.isNotNull(templateEnums) ? templateEnums.getInfo() : "");
            TemplateCategory templateCategory = templateCategoryMap.get(simpleVo.getTemplateCategoryId());
            simpleVo.setTemplateCategoryName(getTemplateCategoryName(templateCategoryMap, simpleVo.getTemplateCategoryId()));

        }
        DataPermissionUtils.executeIgnore(()->{
            TenantUtils.executeIgnore(()->{
                List adminUserRespDTOList = userApi.getUserList(userIds);
                Map<Long, AdminUserRespDTO> userRespDTOMap = CollectionUtils.convertMap(adminUserRespDTOList, AdminUserRespDTO::getId);
                for (TemplateAllPermissionReqVo simpleVo : finalList) {
                    if(simpleVo.getCreator() != null && userRespDTOMap.containsKey(Long.parseLong(simpleVo.getCreator()))){
                        if(userRespDTOMap.get(Long.parseLong(simpleVo.getCreator())) != null){
                            simpleVo.setCreatorName(userRespDTOMap.get(Long.parseLong(simpleVo.getCreator())).getNickname());
                        }
                    }
                }
            });

        });
        PageResult<TemplateAllPermissionReqVo> pageResult = new PageResult<>();
        pageResult.setList(finalList);
        pageResult.setTotal(templPr.getTotal());
        return pageResult;
    }

    @Override
    public PageResult<TemplateAllPermissionReqVo> getAllPage(TemplateListReqVo vo) {
        PageResult<ContractTemplate> contractTemplatePageResult = new PageResult<>();
        if (ObjectUtil.isNotEmpty(vo.getIsDraft()) && vo.getIsDraft() == 1) {
            //草拟模板选择范本列表
            LambdaQueryWrapper<ContractTemplate> contractTemplateLambdaQueryWrapper = new LambdaQueryWrapper<ContractTemplate>()
                    .eq(ContractTemplate::getApproveStatus, StatusEnums.APPROVED.getCode())
                    .orderByDesc(ContractTemplate::getUpdateTime)
                    .orderByAsc(ContractTemplate::getCode);
            if (ObjectUtil.isNotEmpty(vo.getName())) {
                contractTemplateLambdaQueryWrapper.like(ContractTemplate::getName, vo.getName());
            }
            contractTemplatePageResult = contractTemplateMapper.selectPage(vo, contractTemplateLambdaQueryWrapper);
        } else {

            String subQuery = "SELECT code, approve_status, MAX(version) AS max_version FROM ecms_contract_template where publish_status = 1 GROUP BY code, approve_status";
            // 构建主查询，连接子查询并筛选出每个code分组中，再按照approve_status分组的最大version的记录
            LambdaQueryWrapper<ContractTemplate> wrapperX = new LambdaQueryWrapper<ContractTemplate>()
                    .inSql(ContractTemplate::getId, "SELECT id FROM ecms_contract_template WHERE (code, approve_status, version) IN (" + subQuery + ") AND approve_status = 2 AND publish_status = 1")
                    .orderByDesc(ContractTemplate::getUpdateTime);
            //发布时间
            if (ObjectUtil.isNotEmpty(vo.getStartPublishTime()) && ObjectUtil.isNotEmpty(vo.getEndPublishTime())) {
                wrapperX.between(ContractTemplate::getPublishTime, vo.getStartPublishTime(), vo.getEndPublishTime());
            }
            //发布来源
            if (StringUtils.isNotBlank(vo.getTemplateSource())) {
                wrapperX.eq(ContractTemplate::getTemplateSource, vo.getTemplateSource());
            }
            String searchText = vo.getSearchText();
            //模糊查询 范本名称/发布机构
            if (StringUtils.isNotBlank(searchText)) {
                wrapperX.and(w -> w.or().like(ContractTemplate::getPublishOrgan, searchText).or().like(ContractTemplate::getName, searchText));
            }
            if (vo.getTemplateCategoryList() != null && vo.getTemplateCategoryList().size() > 0) {
                //按照范本分类查询
                wrapperX.in(ContractTemplate::getTemplateCategoryId, vo.getTemplateCategoryList());
            }
            contractTemplatePageResult = contractTemplateMapper.selectPage(vo, wrapperX);
        }


        PageResult<TemplateAllPermissionReqVo> templateAllPermissionReqVoPageResult = ContractTemplateConverter.INSTANCE.convertPagev1(contractTemplatePageResult);

        List<ContractTemplate> list = contractTemplatePageResult.getList();
        List<Long> userIds = list.stream().map(ContractTemplate::getCreator).collect(Collectors.toList()).stream().map(Long::parseLong).collect(Collectors.toList());

        List<TemplateAllPermissionReqVo> finalList = ContractTemplateConverter.INSTANCE.convert2AllVo(list);

        // 查询用户列表
        List<AdminUserRespDTO> userList = userApi.getUserList();
        Map<Long, AdminUserRespDTO> userMap = userList.stream().collect(Collectors.toMap(AdminUserRespDTO::getId, Function.identity()));

        List<TemplateCategory> templateCategoryList = templateCategoryMapper.selectList();
        Map<Integer, TemplateCategory> templateCategoryMap = CollectionUtils.convertMap(templateCategoryList, TemplateCategory::getId);
        List<FileDTO> fileDTOList = new ArrayList<>();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(userIds)) {
            fileDTOList = fileApi.getContractFileByUserList(userIds);
        }

        Map<Long, FileDTO> fileDTOMap = CollectionUtils.convertMap(fileDTOList, FileDTO::getId);

        for (TemplateAllPermissionReqVo simpleVo : finalList) {
            List<ContractType> contractTypeList = new ArrayList<>();
            Map<String, ContractType> contractTypeMap = new HashMap<>();
            contractTypeList = contractTypeMapper.selectList();
            if (CollectionUtil.isNotEmpty(contractTypeList)) {
                contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            }
            // 设置创建者昵称
            AdminUserRespDTO creator = userMap.get(Long.valueOf(simpleVo.getCreator()));
            simpleVo.setCreatorName(creator != null ? creator.getNickname() : "");
            //每个文件的后一位就是相关图片的第一张。
            FileDTO file = fileDTOMap.get(simpleVo.getRemoteFileId());
            simpleVo.setPageCount(ObjectUtil.isNotNull(file) ? file.getPageCount() : null);

            TemplateEnums templateEnums = TemplateEnums.getInstance(simpleVo.getTemplateSource());

            simpleVo.setTemplateSourceStr(ObjectUtil.isNotNull(templateEnums) ? templateEnums.getInfo() : "");

            simpleVo.setTemplateCategoryName(getTemplateCategoryName(templateCategoryMap, simpleVo.getTemplateCategoryId()));

            //范本引用次数
            ContractTemplate contractTemplate = contractTemplateMapper.selectById(simpleVo.getId());
            String subQuery = "SELECT model_code, MAX(model_version) AS max_version FROM ecms_contract_template_quote GROUP BY model_code";
            LambdaQueryWrapper<TemplateQuoteDO> wrapperX = new LambdaQueryWrapper<TemplateQuoteDO>().inSql(TemplateQuoteDO::getId, "SELECT id FROM ecms_contract_template_quote WHERE (model_code, model_version) IN (" + subQuery + ") ").eq(TemplateQuoteDO::getTemplateCode, contractTemplate.getCode());
            //只查询数量
            Integer count = Math.toIntExact(templateQuoteMapper.selectCount(wrapperX));
            simpleVo.setQuoteCount(count);

            //合同类型
            ContractType contractType = contractTypeMap.get(simpleVo.getContractType());
            if (ObjectUtil.isNotNull(contractType)) {
                simpleVo.setContractTypeName(contractType.getName());
            }
        }
        templateAllPermissionReqVoPageResult.getList().clear();
        templateAllPermissionReqVoPageResult.setList(finalList);
        return templateAllPermissionReqVoPageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertTemplateAndSubmitApprove(TemplateCreateSubmitReqVO vo) throws Exception {
        TemplateCreateVo createVo = ContractTemplateConverter.INSTANCE.convertCreateSubmitVO2Create(vo);
        String templateId = saveTemplateByUpload(createVo);
        return templateBpmService.submitTemplateApproveFlowable(SecurityFrameworkUtils.getLoginUserId(), new TemplateBpmSubmitCreateReqVO().setId(templateId).setApproveIntroduction(vo.getApproveIntroduction()));
    }

    @Override
    public String updateTemplateAndSubmitApprove(TemplateUpdateSubmitReqVO vo) throws Exception {
        checkCodeAndName4Update(vo);
        TemplateUpdateReqVO updateReqVO = ContractTemplateConverter.INSTANCE.convertUpdateSubmitVO2Update(vo);
        updateTemplate(updateReqVO);
        return templateBpmService.submitTemplateApproveFlowable(SecurityFrameworkUtils.getLoginUserId(), new TemplateBpmSubmitCreateReqVO().setId(vo.getId()).setApproveIntroduction(vo.getApproveIntroduction()));
    }

    private void checkCodeAndName4Update(TemplateUpdateSubmitReqVO vo) {
        Long count = 0L;
        count = contractTemplateMapper.selectCount(new LambdaQueryWrapperX<ContractTemplate>().eq(ContractTemplate::getCode, vo.getCode()));
        if (1 < count) {
            throw exception(CODE_EXIST_ERROR);
        }
        count = contractTemplateMapper.selectCount(new LambdaQueryWrapperX<ContractTemplate>().eq(ContractTemplate::getName, vo.getName()));
        if (1 < count) {
            throw exception(NAME_EXISTS);
        }
    }

    @Override
    public String updateTemplate(TemplateUpdateReqVO reqVO) throws Exception {
        LocalDateTime startDate =  LocalDateTime.now();
        ContractTemplate sqlTemplate = contractTemplateMapper.selectById(reqVO.getId());
        //只有上传文件才有富文本
//        if ("1".equals(reqVO.getUploadType()) && !StringUtils.isNotBlank(reqVO.getContent())) {
//            throw exception(TEMPLATE_FILE_CONTENT_ERROR);
//        }
        //校验激活状态
        if (StatusEnums.APPROVED.getCode().equals(sqlTemplate.getApproveStatus())) {
            throw exception(TEMPLATE_UPLOAD_APPROVED_ERROR);
        }

        // 编辑范本（条款生成的）
        if (Integer.valueOf("2").equals(sqlTemplate.getUploadType())) {
            return updateTemplateByTerm(reqVO);
        }

        Long pdfId = fileApi.insertFileInfo(new FilesDTO());
        log.info("新pdfId=" + pdfId);
        if (Integer.valueOf("1").equals(sqlTemplate.getUploadType())) {
            // 因为上传文件（富文本）起草范本，changxie可编辑docx文件,因此重新生成pdf文件
            ContractTemplate template = contractTemplateMapper.selectById(reqVO.getId());

      // 异步同步文件内容
      ContractTemplate finalSqlTemplate = sqlTemplate;
      CompletableFuture.runAsync(
          () -> {
            asyncFileChecker(0, 0L, startDate, template.getSourceFileId());
            try {
              toPdfV2(
                  new ContractToPdfVO()
                      .setName(template.getFileName())
                      .setFileAddId(template.getSourceFileId())
                      .setPdfFileId(pdfId));
              if (pdfId != null) {
                finalSqlTemplate.setRemoteFileId(pdfId);
              }
            } catch (Exception e) {
              log.error("范本编号为" + reqVO.getCode() + "范本文件同步异常");
              log.error(e.getMessage());
            }
          });
    }

        sqlTemplate = enhanceUpdate2Do(sqlTemplate, reqVO);
        // 详情查看使用的pdf文件
        sqlTemplate.setRemoteFileId(pdfId);
        //将流式文件转换成版式文件
        MultipartFile file = reqVO.getFile();

        //如果上传文件不为空
        if (ObjectUtil.isNotNull(file)) {
            String fileName = file.getOriginalFilename();
            //将pdf文件和生成的缩略图统一都放入远端的uuid目录中。

            String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();

            String pdfFileName = "";
            //转pdf保存到pdf根目录
            String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
            String tempWordFile = localFolderPath + "/" + fileName;
            String pdfPath = localFolderPath;
            String pdfPicPath = localFolderPath + "/pics";
            int pageCount = 0;
            FileUtil.mkdir(pdfPicPath);

            MultipartFile tempWord = reqVO.getFile();
            InputStream ins = tempWord.getInputStream();
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
//            Word2PDFConverter.ConvertToPDF(tempWordFile, pdfPath);
                AsposeUtil.docx2Pdf(tempWordFile, pdfPath);
                pageCount = EcontractUtil.getFilePageFromPDF(pdfPath);
            }
            sqlTemplate.setFileName(pdfFileName);
            File pdfFile = new File(pdfPath);
            FileInputStream fileInputStream = new FileInputStream(pdfFile);
            MultipartFile multipartPdfFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(), null, fileInputStream);

            //将PDF文件解析出多个图片
            PDF2JPGConverter pcv = new PDF2JPGConverter();
            pcv.convertAll(pdfPath, pdfPicPath, PDF_PICS_DPI);

            //将pdf根目录上传
            String uploadPath = EcontractUtil.getRemoteFolderPath(MODULE_CONTRACT_TEMPLATE, folderId);
            String picsUploadPath = uploadPath + "/" + "pics";

            Long uploadId = fileApi.uploadFile(pdfFileName, uploadPath + "/" + pdfFileName, IoUtil.readBytes(multipartPdfFile.getInputStream()), pageCount);

//            //存文件id
            sqlTemplate.setRemoteFileId(uploadId);

            //将旧文件和图片删除
            deletePDFAndPics(reqVO.getId());

            //上传源文件保存处理
            this.sourceFileProcessor(file, sqlTemplate);
        }
//        if ("0".equals(reqVO.getUploadType())) {
        //删除旧条款关系
        templateTermMapper.delete(new LambdaQueryWrapper<TemplateTermDO>().eq(TemplateTermDO::getTemplateId, reqVO.getId()));
        //保存条款关系
        List<TermReqVO> termList = reqVO.getTermList();
        if (CollUtil.isNotEmpty(termList)) {
            List<TemplateTermDO> templateTermList = TemplateTermConverter.INSTANCE.convertList(termList);
            templateTermList.forEach(templateTermDO -> {
                templateTermDO.setTemplateId(reqVO.getId());
            });
            templateTermMapper.insertBatch(templateTermList);
//            }

        }


        //如果为空，则直接存入原来的文件id
        contractTemplateMapper.updateById(sqlTemplate);
        return "success";
    }

  private void asyncFileChecker(
      int index, long waitingTime, LocalDateTime startDate, Long sourceFileId) {
    if (4 == index) {
      return;
    }

    // 等待五秒
    ThreadUtil.sleep(5000);
    waitingTime += 5000;
    log.info("已等待" + waitingTime + "毫秒");
    FileDTO fileDTO = fileApi.selectById(sourceFileId);
    if (ObjectUtil.isNull(fileDTO)) {
      throw exception(DATA_ERROR);
    }
    LocalDateTime fileDate = fileDTO.getUpdateTime();
    if (fileDate.isBefore(startDate)) {
      asyncFileChecker(index + 1,waitingTime, fileDate, sourceFileId);
    } else {
      log.info("文件：" + sourceFileId + "更新成功，时间：" + fileDate.toString());
      return;
    }
    }

    /**
     * 编辑范本（条款生成的）
     */
    private String updateTemplateByTerm(TemplateUpdateReqVO reqVO) {
        //先清空范本相关的条款
        contractTemplateTermRelMapper.delete(new LambdaQueryWrapperX<ContractTemplateTermRelDO>().eqIfPresent(ContractTemplateTermRelDO::getTemplateId, reqVO.getId()));
        buildTerms(reqVO.getTermList(), reqVO.getId());

        ContractTemplate contractTemplate = ContractTemplateConverter.INSTANCE.updateVotoEntity(reqVO);
        contractTemplateMapper.updateById(contractTemplate);
        return "success";
    }

    /**
     * 更新VO转换
     */
    private ContractTemplate enhanceUpdate2Do(ContractTemplate result, TemplateUpdateReqVO r) {
        if (r.getName() != null) {
            result.setName(r.getName());
        }
        if (r.getTemplateCategoryId() != null) {
            result.setTemplateCategoryId(r.getTemplateCategoryId());
        }
        if (r.getName() != null) {
            result.setTemplateSource(r.getTemplateSource());
        }
        if (r.getTemplateIntro() != null) {
            result.setTemplateIntro(r.getTemplateIntro());
        }
        if (ObjectUtil.isNotEmpty(r.getVersion())) {
            result.setVersion(r.getVersion());
        }
        if (StringUtils.isNotBlank(r.getContent())) {
            result.setContent(r.getContent().getBytes());
        }

        //判断来源
        //校验官方范本是否填写发布机构和发布时间
        TemplateEnums templateEnums = TemplateEnums.getInstance(r.getTemplateSource());
        if (templateEnums == GOVERNMENT_TEMPLATE) {
            r.setTemplateSource(r.getTemplateSource());
            // 发布机构存入
            if (StringUtils.isNotEmpty(r.getPublishOrgan())) {
                result.setPublishOrgan(r.getPublishOrgan());
            }
            //将发布时间
            if (ObjectUtils.isNotEmpty(r.getPublishTime())) {
                result.setPublishTime(r.getPublishTime());
            }

        } else if (templateEnums == STANDARD_TEMPLATE || templateEnums == THIRD_PARTY_TEMPLATE) {
            //如果发布来源是标准范本
            result.setTemplateSource(r.getTemplateSource());
            result.setPublishOrgan(null);
            result.setPublishTime(null);
            UpdateWrapper<ContractTemplate> updateWrapper = new UpdateWrapper<ContractTemplate>().eq("id", result.getId()).set("publish_time", null).set("publish_organ", null);
            contractTemplateMapper.update(result, updateWrapper);

        } else {
            throw exception(ErrorCodeConstants.TEMPLATE_SAVE_UPLOAD_UNKNOWN_TEMPLATE_SOURCE_ERROR);
        }

        return result;
    }

    @Override
    public PageResult<ContractTemplate> getPage(TemplateListReqVo vo) {
        return contractTemplateMapper.selectPage(vo);
    }

    /**
     * 查看范本引用情况
     */
    @Override
    public PageResult<TemplateQuotePageRespVO> getTemplateQuotePage(TemplateQuotePageReqVO vo) {
        ContractTemplate contractTemplate = contractTemplateMapper.selectById(vo.getTemplateId());
        String subQuery = "SELECT model_code, MAX(model_version) AS max_version FROM ecms_contract_template_quote GROUP BY model_code";
        LambdaQueryWrapper<TemplateQuoteDO> wrapperX = new LambdaQueryWrapper<TemplateQuoteDO>().inSql(TemplateQuoteDO::getId, "SELECT id FROM ecms_contract_template_quote WHERE (model_code, model_version) IN (" + subQuery + ") ").eq(TemplateQuoteDO::getTemplateCode, contractTemplate.getCode()).orderByDesc(TemplateQuoteDO::getUpdateTime);
        PageResult<TemplateQuoteDO> templateQuoteDOPageResult = templateQuoteMapper.selectPage(vo, wrapperX);
        return ContractTemplateConverter.INSTANCE.toQuotePageVo(templateQuoteDOPageResult);
    }

    /**
     * 拼接父类子类分类名称
     */
    private String getTemplateCategoryName(Map<Integer, TemplateCategory> categoryMap, Integer categoryId) {
        String result = "";
        TemplateCategory category = categoryMap.get(categoryId);
        if (ObjectUtil.isNotNull(category)) {
            TemplateCategory fatherModelCategory = categoryMap.get(category.getParentId());
            result = category.getName();
            if (ObjectUtil.isNotNull(fatherModelCategory)) {
                result = fatherModelCategory.getName() + "-" + category.getName();
            }
        }
        return result;
    }

    /**
     * 查看单个范本
     */
    @Override
    public TemplateSingleVo getTemplateById(String id) throws Exception {
        ContractTemplate template = contractTemplateMapper.selectById(id);
        if (ObjectUtil.isNull(template)) {
            return new TemplateSingleVo();
        }
        TemplateSingleVo vo = ContractTemplateConverter.INSTANCE.entityToSingle(template);

//        List<Long> picids = new ArrayList<Long>();
//        try {
//            picids = templatePicMapper.selectList(new LambdaQueryWrapperX<TemplatePic>()
//                            .eq(TemplatePic::getTemplateId, id)).stream()
//                    .map(TemplatePic::getFileId)
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            throw exception(EMPTY_PIC_DATA_ERROR);
//        }
//
//        List<String> paths = new ArrayList<String>();

        List<ContractType> contractTypeList = new ArrayList<>();
        Map<String, ContractType> contractTypeMap = new HashMap<>();
        contractTypeList = contractTypeMapper.selectList();
        if (CollectionUtil.isNotEmpty(contractTypeList)) {
            contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
        }
        ContractType contractType = contractTypeMap.get(vo.getContractType());
        if (ObjectUtil.isNotNull(contractType)) {
            vo.setContractTypeName(contractType.getName());
        }
        List<TemplateCategory> templateCategoryList = templateCategoryMapper.selectList();

        Map<Integer, TemplateCategory> templateCategoryMap = CollectionUtils.convertMap(templateCategoryList, TemplateCategory::getId);
        vo.setTemplateCategoryStr(getTemplateCategoryName(templateCategoryMap, Integer.valueOf(vo.getTemplateCategoryId())));

//        //获取审批信息
//        List<CommonOfModelAndTemplateBpmProcessRespVO> bpmProcessRespVOList = getProcessInfo(template);
//        vo.setBpmProcessRespVOList(bpmProcessRespVOList);
//
//        //自动生成文件字数
//        if (ObjectUtil.isNotEmpty(vo.getRemoteFileId())) {
//            Integer wordCount = getWordCountForPdfFile(vo.getRemoteFileId());
//            vo.setWordCount(wordCount);
//        }
        //获取条款说明
        List<TemplateTermDO> templateTermDOS = templateTermMapper.selectList(new LambdaQueryWrapperX<TemplateTermDO>()
                .eq(TemplateTermDO::getTemplateId, vo.getId())
                .orderByAsc(TemplateTermDO::getSort));
        vo.setTermList(TemplateTermConverter.INSTANCE.convert2List(templateTermDOS));
        return vo;
    }

    /**
     * 获得pdf文件字数
     */
    private Integer getWordCountForPdfFile(Long remoteFileId) throws Exception {
        //审批通过，将范本文件转成ofd格式
        FileDTO fileDTO = fileApi.selectById(remoteFileId);
        if (fileDTO != null) {
            //将pdf文件下载
            String fileName = fileDTO.getName();
            String uuid = IdUtil.fastSimpleUUID();
            Long fileId = fileDTO.getId();
            String ready2UploadFolderPath = READY_TO_UPLOAD_PATH + "/" + uuid;
            FileUtil.mkdir(ready2UploadFolderPath);
            String pdfFilePath = ready2UploadFolderPath + "/" + fileDTO.getName();
            ByteArrayInputStream inputStream = IoUtil.toStream(fileApi.getFileContentById(fileId));
            File pdfFile = new File(pdfFilePath);
            // 将流的内容写入文件
            FileUtil.writeFromStream(inputStream, pdfFile);
            EcontractUtil.getPdfWordCount(pdfFilePath);
            Integer result = EcontractUtil.getPdfWordCount(pdfFilePath);
            //删除临时目录
            FileUtil.del(ready2UploadFolderPath);
            return result;
        }
        return null;
    }

    private List<CommonOfModelAndTemplateBpmProcessRespVO> getProcessInfo(ContractTemplate template) {
        //找出最近得一条流程记录
        List<TemplateBpmDO> templateBpmDOList = templateBpmMapper.selectList(new LambdaQueryWrapperX<TemplateBpmDO>().eq(TemplateBpmDO::getTemplateId, template.getId()).orderByDesc(TemplateBpmDO::getCreateTime).last("LIMIT 1"));

        //未发起审批，审批流程为空。
        if (CollUtil.isEmpty(templateBpmDOList)) {
//            List<BpmProcessResDTO> bpmProcessResDTOList = bpmActivityApi.getActivityAssignInfoByApproveCode(TEMPLATE_TYPE);
//            return ModelConverter.INSTANCE.convertDTO2RespVO(bpmProcessResDTOList);
            return null;
        }
        TemplateBpmDO templateBpmDO = templateBpmDOList.get(0);
        List<BpmProcessRespDTO> bpmProcessResDTOList = bpmActivityApi.getActivityInfoListByProcessInstanceId(templateBpmDO.getProcessInstanceId());
        if (CollUtil.isEmpty(bpmProcessResDTOList)) {
            return Collections.emptyList();
        }
        return ContractTemplateConverter.INSTANCE.convertDTO2RespVO(bpmProcessResDTOList);
    }

    /**
     * 范本上传方式 0.wps 1.富文本 2.条款
     */
    @Override
    public String saveTemplateByUpload(TemplateCreateVo vo) throws Exception {
        LocalDateTime startDate =  LocalDateTime.now();
        if (ObjectUtil.isNotEmpty(vo.getUploadType()) && 2 == vo.getUploadType()) {
            return saveTemplateByTerm(vo);
        }

        boolean result = false;
        if (ObjectUtil.isEmpty(vo)) {
            throw exception(ErrorCodeConstants.TEMPLATE_SAVE_UPLOAD_ERROR);
        }

        //校验官方范本是否填写发布机构和发布时间
        TemplateEnums instance = TemplateEnums.getInstance(vo.getTemplateSource());
        if (GOVERNMENT_TEMPLATE == instance) {

            if (StringUtils.isEmpty(vo.getPublishOrgan())) {
                throw exception(ErrorCodeConstants.TEMPLATE_SAVE_UPLOAD_EMPTY_PUBLISH_ORGANIZATION_ERROR);
            }
            if (ObjectUtil.isEmpty(vo.getPublishTime())) {
                throw exception(ErrorCodeConstants.TEMPLATE_SAVE_UPLOAD_EMPTY_PUBLISH_TIME_ERROR);
            }
        }
        String localFolderPath = "";
        String fileName = null;
        String pdfFileName = null;
        //将流式文件转换成版式文件
        if (vo.getFile() != null) {
            MultipartFile file = vo.getFile();
             fileName = file.getOriginalFilename();
            //将pdf文件和生成的缩略图统一都放入远端的uuid目录中。

            String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();


            //转pdf保存到pdf根目录
            localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
            String tempWordFile = localFolderPath + "/" + fileName;
            String pdfPath = localFolderPath;
//        String pdfPicPath = localFolderPath + "/pics";
            int pageCount = 0;
//        FileUtil.mkdir(pdfPicPath);

            MultipartFile tempWord = vo.getFile();
            InputStream ins = tempWord.getInputStream();
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
//            AsposeUtil.docx2Pdf(tempWordFile, pdfPath); //红色水印Evaluation Only.
//                YhAgentUtil.officeToPDF(tempWordFile, pdfPath);
//                pageCount = EcontractUtil.getFilePageFromPDF(pdfPath);
            }

//            File pdfFile = new File(pdfPath);
//            FileInputStream fileInputStream = new FileInputStream(pdfFile);
//            MultipartFile multipartPdfFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(), null, fileInputStream);

            //将pdf根目录上传
//            String uploadPath = EcontractUtil.getRemoteFolderPath(MODULE_CONTRACT_TEMPLATE, folderId);
//            String picsUploadPath = uploadPath + "/" + "pics";
//            Long uploadId = fileApi.uploadFile(pdfFileName, FileUploadPathEnum.TEMPLATE.getPath(vo.getCode(), vo.getName())+".pdf", IoUtil.readBytes(multipartPdfFile.getInputStream()), pageCount);

            //存文件id
//            vo.setRemoteFileId(uploadId);
        } else {
            //校验是否可以无文件
            List<ContractTemplate> contractTemplates = contractTemplateMapper.selectList(new LambdaQueryWrapperX<ContractTemplate>()
                    .eq(ContractTemplate::getCode, vo.getCode())
                    .isNotNull(ContractTemplate::getSourceFileId)
                    .orderByDesc(ContractTemplate::getVersion));
            //没有数据则代表第一次添加文件文件不能为空
            if (contractTemplates == null || contractTemplates.size() < 1) {
                throw exception(ErrorCodeConstants.TEMPLATE_FILE_UPLOAD_ERROR);
            }
            vo.setRemoteFileId(contractTemplates.get(0).getRemoteFileId());
            vo.setSourceFileId(contractTemplates.get(0).getSourceFileId());
        }
        ContractTemplate template = ContractTemplateConverter.INSTANCE.createVotoEntity(vo);
        template.setFileName(fileName);
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
//        template.setRegionCode(loginUser != null ? loginUser.getRegionCode() : "");
//        if (StringUtils.isNotBlank(template.getRegionCode())) {
//            RegionDTO region = regionApi.getRegionByCode(template.getRegionCode());
//            if (region != null) {
//                template.setRegionName(region.getRegionName());
//            }
//        }
        if(vo.getSourceFileId() == null || vo.getSourceFileId() == 0){
            this.sourceFileProcessor(vo.getFile(), template);
        }
//        ThreadUtil.sleep(3000);
//        Long pdfId = toPdf(new ContractToPdfVO().setName(pdfFileName).setFileAddId(template.getSourceFileId()));
//        //Long pdfId = word2Pdf(vo.getRemoteFileId(), FileUploadPathEnum.TEMPLATE.getCode());
//        if(pdfId != null){
//            template.setRemoteFileId(pdfId);
//        }
        Long pdfId = fileApi.insertFileInfo(new FilesDTO());
        log.info("新pdfId=" + pdfId);
        template.setRemoteFileId(pdfId);
        // 异步同步文件内容
        ContractTemplate finalSqlTemplate = template;
        CompletableFuture.runAsync(
                () -> {
                    asyncFileChecker(0, 0L, startDate, template.getSourceFileId());
                    try {
                        toPdfV2(
                                new ContractToPdfVO()
                                        .setName(template.getFileName())
                                        .setFileAddId(template.getSourceFileId())
                                        .setPdfFileId(pdfId));
                        if (pdfId != null) {
                            finalSqlTemplate.setRemoteFileId(pdfId);
                        }
                    } catch (Exception e) {
                        log.error("范本编号为" + finalSqlTemplate.getCode() + "范本文件同步异常");
                        log.error(e.getMessage());
                    }
                });


        //获取用户的租户id
        template.setTenantId(loginUser != null ? loginUser.getTenantId() : 0L);
        result = contractTemplateMapper.insert(finalSqlTemplate) == INSERT_SUCCESS;
        String templateId = finalSqlTemplate.getId();

        //保存条款关系
        List<TermReqVO> termList = vo.getTermList();
        if (CollUtil.isNotEmpty(termList)) {
            List<TemplateTermDO> templateTermList = TemplateTermConverter.INSTANCE.convertList(termList);
            templateTermList.forEach(templateTermDO -> {
                templateTermDO.setTemplateId(templateId);
            });
            templateTermMapper.insertBatch(templateTermList);
        }

        //将本次缓存目录删除
        try {
            FileUtil.del(localFolderPath);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw exception(TEMPLATE_FILE_DELETE_ERROR);
        }

        return result ? templateId : "范本操作发送异常";
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

        AsposeUtil.docx2Pdf(wordFilePath,pdfFilePath);
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
     * 通过条款制作范本
     */
    private String saveTemplateByTerm(TemplateCreateVo vo) {
        if (CollectionUtil.isEmpty(vo.getTermList())) {
            throw exception(EMPTY_TERM_ERROR);
        }

        ContractTemplate template = ContractTemplateConverter.INSTANCE.createVotoEntity(vo);
        //获取用户的租户id
        template.setTenantId(SecurityFrameworkUtils.getLoginUser().getTenantId());
        contractTemplateMapper.insert(template);
        String templateId = template.getId();
        buildTerms(vo.getTermList(), templateId);
        return templateId;
    }

    private void buildTerms(List<TermReqVO> termList, String id) {
        if (CollectionUtil.isNotEmpty(termList)) {
            List<ContractTemplateTermRelDO> convert = TemplateTermConverter.INSTANCE.convertReq2DO(termList, id);
            contractTemplateTermRelMapper.insertBatch(convert);
        }

    }

    /**
     * 存入范本的源文件id
     */
    private void sourceFileProcessor(MultipartFile file, ContractTemplate template) throws IOException {
        if (file != null) {
            Long fileId = fileApi.uploadFile(file.getOriginalFilename(), FileUploadPathEnum.TEMPLATE.getPath() + template.getCode() + "/" + file.getOriginalFilename(), IoUtil.readBytes(file.getInputStream()));
            template.setSourceFileId(fileId);
        }
    }

    /**
     * 批量上传图片
     */
    public void uploadPics(List<MultipartFile> files, String uploadPath, String templateId) {
        List<TemplatePic> list = new ArrayList<TemplatePic>();
        try {
            //将目录中的图片全部上传
            for (MultipartFile file : files) {
                TemplatePic templatePic = new TemplatePic();
                templatePic.setTemplateId(templateId);
                long picId = fileApi.uploadFile(file.getName(), uploadPath + "/" + file.getName(), IoUtil.readBytes(file.getInputStream()));
                templatePic.setFileId(picId);
                list.add(templatePic);
            }
            //将模板图片的关系批量入库
            templatePicMapper.insertBatch(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw exception(ErrorCodeConstants.MODEL_INSERT_UPLOAD_ERROR);
        }

    }

    /**
     * 删除相关图片
     */
    public void deletePDFAndPics(String templateId) throws Exception {
        //删除相关pdf文件
        Long pdfId = contractTemplateMapper.selectById(templateId).getRemoteFileId();
        fileApi.deleteFile(pdfId);
        //得到相关图片的文件id
        List<Long> fileIds = templatePicMapper.selectList(new LambdaQueryWrapperX<TemplatePic>().eq(TemplatePic::getTemplateId, templateId)).stream().map(TemplatePic::getFileId).collect(Collectors.toList());
        //将这个图片文件都删除
        for (Long fileId : fileIds) {
            fileApi.deleteFile(fileId);
        }

    }

    @Override
    public List<TemplateSimpleVo> getFiveTemplates(int categoryId) throws Exception {
        LambdaQueryWrapperX<ContractTemplate> wrapper = new LambdaQueryWrapperX<ContractTemplate>();
        wrapper.likeIfPresent(ContractTemplate::getTemplateCategoryId, String.valueOf(categoryId)).last("LIMIT 5");
        List<ContractTemplate> list = contractTemplateMapper.selectList(wrapper);

        List<TemplateSimpleVo> finalList = new ArrayList<TemplateSimpleVo>();
        for (ContractTemplate contractTemplate : list) {
            TemplateSimpleVo simpleVo = new TemplateSimpleVo();
            TemplateSingleVo singleVo = ContractTemplateConverter.INSTANCE.entityToSingle(contractTemplate);
            int pageCount = fileApi.getPageCount(contractTemplate.getRemoteFileId());
            //每个文件的后一位就是相关图片的第一张。
            Long firstPicId = singleVo.getRemoteFileId() + 1;
            String path = fileApi.getURL(firstPicId);
            simpleVo.setPicPath(path);
            simpleVo.setId(contractTemplate.getId());
            simpleVo.setName(contractTemplate.getName());
            List<TemplatePic> pics = templatePicMapper.selectList(new LambdaQueryWrapperX<TemplatePic>().eq(TemplatePic::getTemplateId, contractTemplate.getId()));
            simpleVo.setPageCount(String.valueOf(pics.size()));
            simpleVo.setTemplateCategoryName(templateCategoryMapper.selectById(contractTemplate.getTemplateCategoryId()).getName());
            finalList.add(simpleVo);
        }
        return finalList;
    }

    @Override
    public void deleteTemplate(DeleteVO reqVO) {
        List<String> ids = reqVO.getIds();
        List<ContractTemplate> templates = contractTemplateMapper.selectBatchIds(ids);
        //校验是否存在审批中的
        if (templates.stream().anyMatch(t -> BpmProcessInstanceResultEnum.PROCESS.getResult().equals(t.getApproveStatus()) || BpmProcessInstanceResultEnum.APPROVE.getResult().equals(t.getApproveStatus()))) {
            throw exception(TEMPLATE_DELETE_APPROVED_ERROR);
        }
        contractTemplateMapper.deleteBatchIds(ids);
        //删除对应条款
        templateTermMapper.delete(new LambdaQueryWrapperX<TemplateTermDO>().in(TemplateTermDO::getTemplateId, ids));
    }

    public Boolean isCodeExist(String code) {
        return contractTemplateMapper.selectList().stream().anyMatch(template -> code.equals(template.getCode()));
    }

    @Override
    public TemplateVersionVO getTemplateVersion(String code, Integer i) {
        TemplateVersionVO templateVersionVO = new TemplateVersionVO();
        List<BigDecimal> bigDecimals = new ArrayList<>();
        if (StringUtils.isNotBlank(code)) {
            LambdaQueryWrapperX<ContractTemplate> contractTemplateLambdaQueryWrapperX = new LambdaQueryWrapperX<>();
            contractTemplateLambdaQueryWrapperX.eq(ContractTemplate::getCode, code).orderByDesc(ContractTemplate::getVersion);
            List<ContractTemplate> contractTemplateList = contractTemplateMapper.selectList(contractTemplateLambdaQueryWrapperX);
            String templateId = contractTemplateList.get(0).getId();
            if (i == 0) {
                // 外部列表
                TemplateBpmDO templateBpmDO = templateBpmMapper.selectOne(TemplateBpmDO::getTemplateId, templateId);
                if (ObjectUtil.isEmpty(templateBpmDO) || templateBpmDO.getResult() == 0 || templateBpmDO.getResult() == 5) {
                    return templateVersionVO.setTemplateId(templateId);
                }
                if (contractTemplateList != null && contractTemplateList.size() > 0) {
                    //判断相关版本是否在审核中
                    for (ContractTemplate contractTemplate : contractTemplateList) {
                        TemplateBpmDO templateBpmDO1 = templateBpmMapper.selectOne(TemplateBpmDO::getTemplateId, contractTemplate.getId());
                        //没有值-草稿抓状态  |  有值 ！= 2   -  在审核中
                        if (templateBpmDO1 != null && templateBpmDO1.getResult() == 1) {
                            throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY, "范本" + contractTemplate.getVersion() + "修订中，新版本取消或发布后可重新修订！");
                        }
                    }
                }
                QueryWrapperX<ContractTemplate> wrapper = new QueryWrapperX<>();
                wrapper.eq("code", code).orderByDesc("version");
                wrapper.inSql("id", "select template_id from ecms_contract_template_bpm where result = 2");
                List<ContractTemplate> contractTemplates = contractTemplateMapper.selectList(wrapper);
//                List<ContractTemplate> contractTemplates = contractTemplateMapper.selectList(new LambdaQueryWrapperX<ContractTemplate>().eq(ContractTemplate::getId, templateId));
                Double version = contractTemplates.get(0).getVersion();
                BigDecimal decimalVersion = BigDecimal.valueOf(version);
                bigDecimals.add(decimalVersion.add(BigDecimal.valueOf(0.1)));
                //获取version的整数部分，并加1
                int integerPart = (int) Math.floor(version);
                bigDecimals.add(BigDecimal.valueOf(integerPart + 1));
            } else {
                // 内部编辑
                QueryWrapperX<ContractTemplate> wrapper = new QueryWrapperX<>();
                wrapper.eq("code", code).orderByDesc("version");
                wrapper.inSql("id", "select template_id from ecms_contract_template_bpm where result = 2");
                List<ContractTemplate> contractTemplates = contractTemplateMapper.selectList(wrapper);
//                List<ContractTemplate> contractTemplates = contractTemplateMapper.selectList(new LambdaQueryWrapperX<ContractTemplate>().eq(ContractTemplate::getId, templateId));
                Double version = contractTemplates.get(0).getVersion();
                BigDecimal decimalVersion = BigDecimal.valueOf(version);
                bigDecimals.add(decimalVersion.add(BigDecimal.valueOf(0.1)));
                //获取version的整数部分，并加1
                int integerPart = (int) Math.floor(version);
                bigDecimals.add(BigDecimal.valueOf(integerPart + 1));
            }
        }
        return templateVersionVO.setVersionList(bigDecimals);
    }

    @Override
    public List<TemplateHistoryRespVO> getModelHistory(String code) {
        if (StringUtils.isNotBlank(code)) {
            LambdaQueryWrapperX<ContractTemplate> wrapper = new LambdaQueryWrapperX<>();
            wrapper.eq(ContractTemplate::getApproveStatus, StatusEnums.APPROVED.getCode())
                    .eq(ContractTemplate::getPublishStatus, "1")//已发布
                    .eq(ContractTemplate::getCode, code)
                    .orderByDesc(ContractTemplate::getVersion);

//            wrapper.eq("code", code).orderByDesc("version");
//            wrapper.inSql("id", "select template_id from ecms_contract_template_bpm where result = 2");
            List<ContractTemplate> contractTemplateList = contractTemplateMapper.selectList(wrapper);
//            if (CollectionUtil.isEmpty(contractTemplateList)) {
            if (CollectionUtil.isEmpty(contractTemplateList)) {
                return new ArrayList<>();
            }
            contractTemplateList.remove(0);
            List<TemplateHistoryRespVO> templateHistoryRespVOList = ContractTemplateConverter.INSTANCE.convertList(contractTemplateList);
            //获得user信息
            List<AdminUserRespDTO> userList1 = adminUserApi.getUserList();
            Map<Long, AdminUserRespDTO> creatorMap = CollectionUtils.convertMap(userList1, AdminUserRespDTO::getId);
            //找到模板对应的流程实例
            Map<String, TemplateBpmDO> templateBpmDOMap = new HashMap<>();
            List<String> collect = templateHistoryRespVOList.stream().map(TemplateHistoryRespVO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collect)) {
                List<TemplateBpmDO> modelBpmDOList = templateBpmMapper.selectList(new LambdaQueryWrapperX<TemplateBpmDO>().in(TemplateBpmDO::getTemplateId, templateHistoryRespVOList.stream().map(TemplateHistoryRespVO::getId).collect(Collectors.toList())));
                templateBpmDOMap = CollectionUtils.convertMap(modelBpmDOList, TemplateBpmDO::getTemplateId);
            }
            for (TemplateHistoryRespVO templateHistoryRespVO : templateHistoryRespVOList) {
                //得到将每个昵称赋值
                if (creatorMap.get(Long.valueOf(templateHistoryRespVO.getCreator())) != null) {
                    AdminUserRespDTO user = creatorMap.get(Long.valueOf(templateHistoryRespVO.getCreator()));
                    if (user != null) {
                        templateHistoryRespVO.setCreatorName(user.getNickname());
                    }
                }
                //将流程实例id添加进返回结果中
                if (CollectionUtil.isNotEmpty(templateBpmDOMap)) {
                    TemplateBpmDO templateBpmDO = templateBpmDOMap.get(templateHistoryRespVO.getId());
                    if (ObjectUtil.isNotNull(templateBpmDO) && ObjectUtil.isNotEmpty(templateBpmDO.getProcessInstanceId())) {
                        templateHistoryRespVO.setProcessInstanceId(templateBpmDO.getProcessInstanceId());
                    }
                }
            }

            return templateHistoryRespVOList;
        }
//        }
        return null;
    }

    @Override
    public void batchPublish(BatchPublishReqVO reqVO) {
        if (reqVO.getPublish().equals(0)) {
            //发布
            contractTemplateMapper.update(null, new LambdaUpdateWrapper<ContractTemplate>().set(ContractTemplate::getPublishStatus, 1).set(ContractTemplate::getPublishTimeReciever, LocalDateTime.now()).in(ContractTemplate::getId, reqVO.getIdList()));
        } else {
            //取消发布
            contractTemplateMapper.update(null, new LambdaUpdateWrapper<ContractTemplate>().set(ContractTemplate::getPublishStatus, 0).set(ContractTemplate::getPublishTimeReciever, null).in(ContractTemplate::getId, reqVO.getIdList()));
        }
    }

    @Override
    public List<TermListRespVO> selectTermsByTemplateId(IdReqVO reqVO) {
        String templateId = reqVO.getId();
//        List<Term> terms = termMapper.selectTermsByTemplateId(templateId);
        List<ContractTemplateTermRelDO> relDOList = contractTemplateTermRelMapper.selectList(ContractTemplateTermRelDO::getTemplateId, templateId);
        if(CollectionUtil.isEmpty(relDOList)) {
            return new ArrayList<>();
        }
        List<String> termIds = relDOList.stream().map(ContractTemplateTermRelDO::getTermId).collect(Collectors.toList());
        List<Term> terms2 = termMapper.selectList(new LambdaQueryWrapperX<Term>().in(Term::getId,termIds).orderByAsc(Term::getCreateTime));
        return enhanceTerm(terms2, templateId);
    }

    private List<TermListRespVO> enhanceTerm(List<Term> terms, String templateId) {
        List<TermListRespVO> result = TermConverter.INSTANCE.do2Resp(terms);

        List<ContractTemplateTermRelDO> relDOS = new ArrayList<ContractTemplateTermRelDO>();
        Map<String, ContractTemplateTermRelDO> relDOMap = new HashMap<>();
        relDOS = contractTemplateTermRelMapper.selectList(new LambdaQueryWrapperX<ContractTemplateTermRelDO>().eq(ContractTemplateTermRelDO::getTemplateId, templateId));
        if (CollectionUtil.isNotEmpty(relDOS)) {
            relDOMap = CollectionUtils.convertMap(relDOS, ContractTemplateTermRelDO::getTermId);
        }
        for (TermListRespVO respVO : result) {
            ContractTemplateTermRelDO relDO = relDOMap.get(respVO.getId());
            if (ObjectUtil.isNotNull(relDO)) {
                respVO.setSort(relDO.getSort());
                respVO.setTitle(relDO.getTitle());
            }
        }
        //按照sort排序
        Collections.sort(result, Comparator.comparingInt((TermListRespVO o) -> o == null || o.getSort() == null? Integer.MAX_VALUE : o.getSort()));

        return result;
    }

    List<TemplateCategory> getAllModelCategoryFromRoot(List<Integer> rootCategoryIds) {
        List<TemplateCategory> leafCategories = new ArrayList<>(); // 存放叶子节点的ModelCategory列表
        List<TemplateCategory> allModelCategories = templateCategoryMapper.selectList();// 所有的ModelCategory数据

        for (Integer rootCategoryId : rootCategoryIds) {
            findLeafCategories(rootCategoryId, allModelCategories, leafCategories);
        }
        return leafCategories;
    }

    private void findLeafCategories(Integer parentId, List<TemplateCategory> allModelCategories, List<TemplateCategory> leafCategories) {
        for (TemplateCategory category : allModelCategories) {
            if (category.getParentId().equals(parentId)) {
                if (hasChildren(category.getId(), allModelCategories)) {
                    findLeafCategories(category.getId(), allModelCategories, leafCategories);
                } else {
                    leafCategories.add(category);
                }
            }
        }
    }

    private boolean hasChildren(Integer parentId, List<TemplateCategory> allModelCategories) {
        for (TemplateCategory category : allModelCategories) {
            if (category.getParentId().equals(parentId)) {
                return true;
            }
        }
        return false;
    }

    private LambdaQueryWrapperX<ContractTemplate> enhanceWrapperByCategoryIds(LambdaQueryWrapperX<ContractTemplate> wrapperX, TemplateListReqVo vo) {

        //模板分类的树状图搜索条件
        List<Integer> rootCategoryIds = new ArrayList<Integer>();
        rootCategoryIds.add(vo.getTemplateCategoryId());
        List<TemplateCategory> categoryList = getAllModelCategoryFromRoot(rootCategoryIds);
        List<Integer> leafCategoryIds = new ArrayList<Integer>();
        if (CollectionUtil.isNotEmpty(categoryList)) {
            leafCategoryIds = categoryList.stream().map(TemplateCategory::getId).collect(Collectors.toList());
            vo.setCategoryIdList(leafCategoryIds);
        } else {
            leafCategoryIds.add(vo.getTemplateCategoryId());
            vo.setCategoryIdList(leafCategoryIds);
        }
        wrapperX.in(ContractTemplate::getTemplateCategoryId, leafCategoryIds);

        return wrapperX;
    }

    @Override
    public TemplateOneRespVo getTemplate(String id) {
        TemplateOneRespVo result = new TemplateOneRespVo();
        //基本信息
        ContractTemplate contractTemplate = contractTemplateMapper.selectById(id);
        if (ObjectUtil.isNull(contractTemplate)) {
            throw exception(EMPTY_DATA_ERROR);
        }
        result = enhanceTemplate(result, contractTemplate);
        //相关条款
        List<Term> termList = termMapper.getTermsByTemplateId(id);
        if (CollectionUtil.isEmpty(termList)) {
            throw exception(EMPTY_DATA_ERROR);
        }
        result = enhanceTerms(result, termList);

        return result;
    }

    private TemplateOneRespVo enhanceTemplate(TemplateOneRespVo result, ContractTemplate contractTemplate) {
        List<ContractType> contractTypeList = new ArrayList<>();
        Map<String, ContractType> contractTypeMap = new HashMap<>();
        contractTypeList = contractTypeMapper.selectList();
        if (CollectionUtil.isNotEmpty(contractTypeList)) {
            contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
        }
        List<TemplateCategory> categoryList = new ArrayList<>();
        Map<Integer, TemplateCategory> categoryMap = new HashMap<>();
        categoryList = templateCategoryMapper.selectList();
        if (CollectionUtil.isNotEmpty(categoryList)) {
            categoryMap = CollectionUtils.convertMap(categoryList, TemplateCategory::getId);
        }

        result = ContractTemplateConverter.INSTANCE.entity2OneResp(contractTemplate);
        ContractType contractType = contractTypeMap.get(contractTemplate.getContractType());
        if (ObjectUtil.isNotNull(contractType)) {
            result.setContractTypeName(contractType.getName());
        }
        TemplateCategory category = categoryMap.get(contractTemplate.getTemplateCategoryId());
        if (ObjectUtil.isNotNull(category)) {
            result.setTemplateCategoryName(category.getName());
        }
        return result;
    }

    private TemplateOneRespVo enhanceTerms(TemplateOneRespVo result, List<Term> termList) {
        List<TermOneRespVO> termOneRespVOS = new ArrayList<TermOneRespVO>();
        termOneRespVOS = TermConverter.INSTANCE.listEntity2Resp(termList);
        List<String> termIds = termOneRespVOS.stream().map(TermOneRespVO::getId).collect(Collectors.toList());

        List<ContractTemplateTermRelDO> relDOList = new ArrayList<ContractTemplateTermRelDO>();
        Map<String, ContractTemplateTermRelDO> relDOMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(termIds)) {
            relDOList = contractTemplateTermRelMapper.selectList(new LambdaQueryWrapperX<ContractTemplateTermRelDO>()
                    .eq(ContractTemplateTermRelDO::getTemplateId, result.getId())
                    .in(ContractTemplateTermRelDO::getTermId, termIds));
        }
        if (CollectionUtil.isNotEmpty(relDOList)) {
            relDOMap = CollectionUtils.convertMap(relDOList, ContractTemplateTermRelDO::getTermId);
        }
        termOneRespVOS = TermConverter.INSTANCE.listEntity2Resp(termList);
        for (TermOneRespVO termOneRespVO : termOneRespVOS) {
            ContractTemplateTermRelDO relDO = relDOMap.get(termOneRespVO.getId());
            if (ObjectUtil.isNotNull(relDO)) {
                termOneRespVO.setSort(relDO.getSort());
            }
        }
        result.setTermOneRespVOS(termOneRespVOS);
        return result;
    }

    @Override
    public String insertRtf4Template(TemplateCreateVo reqVO) {
        ContractTemplate contractTemplate = ContractTemplateConverter.INSTANCE.createVotoEntity(reqVO);
        contractTemplateMapper.updateById(contractTemplate);
        return contractTemplate.getId();
    }

    @Override
    public void downloadTemplate(HttpServletResponse response, String templateId) throws Exception {
        // 获取范本的富文本
        ContractTemplate contractTemplate = contractTemplateMapper.selectById(templateId);
        if (ObjectUtil.isNull(contractTemplate)) {
            throw exception(SYSTEM_ERROR, "范本不存在");
        }

        // 转成pdf
        String content = StringUtils.toEncodedString(contractTemplate.getContent(), StandardCharsets.UTF_8);

        String uuid = String.valueOf(UUID.randomUUID());
        String path = MODEL_RTF_PDF_TEMP_PATH + uuid + ".pdf";
        //富文本生成pdf，存查看的文件id地址
        String folderId = "TEMP-" + IdUtil.fastSimpleUUID();
        String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
        FileUtil.touch(path);
        contractService.convertRtf2Pdf(content, path);
        Long rtf_fileId = fileApi.uploadFile(contractTemplate.getName() + ".pdf", FileUploadPathEnum.TEMPLATE.getPath(uuid, contractTemplate.getName() + ".pdf"), IoUtil.readBytes(FileUtil.getInputStream(path)));
        FileUtil.del(path);
        FileUtil.del(localFolderPath);

        // 下载
        fileApi.getFileContentByFileId(response, rtf_fileId);
    }


    @Resource
    private ChangXieService changXieService;
    /**
     * word转pdf
     * */
    public Long toPdf(ContractToPdfVO contractToPdfVO) throws Exception {
        //富文本生成pdf，存查看的文件id地址
        String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
        String s = contractToPdfVO.getName();
        String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
        FileUtil.mkdir(localFolderPath);
        Long pdfFileId = new Long(0);
        //富文本生成pdf
        if (ObjectUtil.isNotEmpty(contractToPdfVO.getContent())) {
            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(contractToPdfVO.getContent().getBytes());
            FileUtil.writeFromStream(byteArrayInputStream, localFolderPath + "/" + "TEMPLATE" + "/" + "temp.rtf");
            YhAgentUtil.officeToPDF(localFolderPath + "/" + "TEMPLATE" + "/" + "temp.rtf", localFolderPath + "/" + s + ".pdf");
            Path path = Paths.get(localFolderPath + "/" + s + ".pdf");
            pdfFileId = fileApi.uploadFile(s + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
        }
        //docx生成pdf
        if (ObjectUtil.isNotEmpty(contractToPdfVO.getFileAddId())) {
//            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(fileApi.getFileContentById(contractToPdfVO.getFileAddId()));
//            FileUtil.writeFromStream(byteArrayInputStream, localFolderPath + "/" + "TEMPLATE" + "/" + fileApi.getName(contractToPdfVO.getFileAddId()));
//            YhAgentUtil.officeToPDF(localFolderPath + "/" + "TEMPLATE" + "/" + fileApi.getName(contractToPdfVO.getFileAddId()), localFolderPath + "/" + FileNameUtil.mainName(fileApi.getName(contractToPdfVO.getFileAddId())) + ".pdf");
//            Path path = Paths.get(localFolderPath + "/" + FileNameUtil.mainName(fileApi.getName(contractToPdfVO.getFileAddId())) + ".pdf");
//            if (StringUtils.isNotBlank(contractToPdfVO.getName())) {
//                pdfFileId = fileApi.uploadFile(contractToPdfVO.getName() + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
//
//            } else {
//                pdfFileId = fileApi.uploadFile(FileNameUtil.mainName(fileApi.getName(contractToPdfVO.getFileAddId())) + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
//
//            }
            //将word清稿,然后转pdf
            Long cleanDraftFileId = changXieService.cleandraftV2(contractToPdfVO.getFileAddId());
            //畅写转pdf
            pdfFileId = changXieService.converterDocx2PdfV2(cleanDraftFileId, FileUploadPathEnum.CONTRACT_SIGNING);
            return pdfFileId;
        }
        FileUtil.del(localFolderPath);
        return pdfFileId;
    }

    /**
     * word转pdf(异步版)
     * */
    public Long toPdfV2(ContractToPdfVO contractToPdfVO) throws Exception {
        //富文本生成pdf，存查看的文件id地址
        String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
        String s = contractToPdfVO.getName();
        String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
        FileUtil.mkdir(localFolderPath);
        Long pdfFileId = new Long(0);
        //富文本生成pdf
        if (ObjectUtil.isNotEmpty(contractToPdfVO.getContent())) {
            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(contractToPdfVO.getContent().getBytes());
            FileUtil.writeFromStream(byteArrayInputStream, localFolderPath + "/" + "TEMPLATE" + "/" + "temp.rtf");
            YhAgentUtil.officeToPDF(localFolderPath + "/" + "TEMPLATE" + "/" + "temp.rtf", localFolderPath + "/" + s + ".pdf");
            Path path = Paths.get(localFolderPath + "/" + s + ".pdf");
            pdfFileId = fileApi.uploadFile(s + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
        }
        //docx生成pdf
        if (ObjectUtil.isNotEmpty(contractToPdfVO.getFileAddId())) {
            //将word清稿,然后转pdf
            Map<String,String> map = changXieService.cleandraftV3(contractToPdfVO.getFileAddId());

            //畅写转pdf
            pdfFileId = changXieService.converterDocx2PdfV3(map, FileUploadPathEnum.CONTRACT_SIGNING,contractToPdfVO.getPdfFileId());

            return pdfFileId;
        }
        FileUtil.del(localFolderPath);
        return pdfFileId;
    }
}





