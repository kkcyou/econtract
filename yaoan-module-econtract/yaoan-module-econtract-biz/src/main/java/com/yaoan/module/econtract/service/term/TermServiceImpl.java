package com.yaoan.module.econtract.service.term;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.codegeneration.vo.CodeQueryReqVO;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.BatchSubmitReqVO;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableConfigRespVO;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.SubmitReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.BpmTaskApproveReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.*;
import com.yaoan.module.econtract.controller.admin.term.vo.bpm.TermListApproveReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.bpm.TermListApproveRespVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termlabel.LabelVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termlabel.TermLabelVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termparam.ParamVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termparam.TermParamRespVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termparam.TermParamVO;
import com.yaoan.module.econtract.controller.admin.term.vo.tree.TermTreeDetailRespVO;
import com.yaoan.module.econtract.controller.admin.term.vo.tree.TermTreeReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.tree.TermTreeRespVO;
import com.yaoan.module.econtract.convert.config.SystemConfigDTOConverter;
import com.yaoan.module.econtract.convert.param.ParamConverter;
import com.yaoan.module.econtract.convert.term.TermConverter;
import com.yaoan.module.econtract.convert.term.label.TermLabelConverter;
import com.yaoan.module.econtract.convert.term.param.TermParamConverter;
import com.yaoan.module.econtract.dal.dataobject.category.TermsCategory;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.param.Param;
import com.yaoan.module.econtract.dal.dataobject.term.SimpleTerm;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.dataobject.term.TermLabel;
import com.yaoan.module.econtract.dal.dataobject.term.TermParam;
import com.yaoan.module.econtract.dal.mysql.category.TermsCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.param.ParamMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermLabelMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermParamMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermRepository;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.StatusConstants;
import com.yaoan.module.econtract.enums.WhetherEnum;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums;
import com.yaoan.module.econtract.enums.term.TermApproveResultStatusEnums;
import com.yaoan.module.econtract.enums.term.TermLibraryEnums;
import com.yaoan.module.econtract.enums.term.TermStatusEnums;
import com.yaoan.module.econtract.enums.term.TermTypeEnums;
import com.yaoan.module.econtract.service.codegeneration.CodeGenerationService;
import com.yaoan.module.econtract.service.contract.TaskService;
import com.yaoan.module.econtract.util.DBExistUtil;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.flowable.FlowableUtil;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;

/**
 * <p>
 * 合同 服务实现类
 * </p>
 *
 * @author doujiale
 * @since 2023-09-18
 */
@Service
public class TermServiceImpl extends ServiceImpl<TermMapper, Term> implements TermService {

    private static final String HEAD = "head";
    private static final String COM = "com";
    private static final String END = "end";
    @Resource
    TermsCategoryMapper termsCategoryMapper;
    @Resource
    private TermMapper termMapper;
    @Resource
    private TermParamMapper termParamMapper;
    @Resource
    private TermLabelMapper termLabelMapper;
    @Resource
    private ParamMapper paramMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private TaskService taskService;
    @Autowired
    private TermRepository termRepository;

    @Resource
    private CodeGenerationService codeGenerationService;

    @Override
    public PageResult<TermRespVO> queryByPage(TermPageReqVO reqVO) {
        PageResult<Term> termPageResult = termMapper.selectPage(reqVO);
        PageResult<TermRespVO> pageResult = TermConverter.INSTANCE.convertPage(termPageResult);
        return enhanceTermPage(pageResult);
    }

    /**
     * 增强分页结果
     */
    private PageResult<TermRespVO> enhanceTermPage(PageResult<TermRespVO> pageResult) {
        if (CollectionUtil.isNotEmpty(pageResult.getList())) {
            Long loginId = getLoginUserId();
            List<TermRespVO> list = pageResult.getList();
            List<String> instanceList = list.stream().map(TermRespVO::getProcessInstanceId).filter(Objects::nonNull).collect(Collectors.toList());
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            // 找出有所相关的流程任务
            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(loginId, instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }
            for (TermRespVO respVO : pageResult.getList()) {
                BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(respVO.getResult());
                if (ObjectUtil.isNotNull(resultEnum)) {
                    if (BpmProcessInstanceResultEnum.PROCESS == resultEnum) {
                        //审批中的任务分为 被退回 和审批中
                        BpmTaskAllInfoRespVO task = taskMap.get(respVO.getProcessInstanceId());
                    }

                    //如果是被退回的申请，回显任务id
                    if (BpmProcessInstanceResultEnum.BACK == resultEnum) {
                        BpmTaskAllInfoRespVO rejectedTask = taskMap.get(respVO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(rejectedTask)) {
                            respVO.setTaskId(rejectedTask.getTaskId());
                        }
                    }
                }
            }
        }
        return pageResult;
    }

    /**
     * 新增条款
     * 目前逻辑：保存即通过审批
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addTerm(TermAddVO addVo) {
        if (CollectionUtil.isNotEmpty(addVo.getContractTypes())) {
            addVo.setContractType(String.join(",", addVo.getContractTypes()));
        }
        Term term = TermConverter.INSTANCE.toEntity(addVo);
        //自动生成编号
//        String code = EcontractUtil.getRandomCodeAutoByTimestamp();

        if (StringUtils.isNotBlank(addVo.getId())) {
            termMapper.updateById(term);
        } else {
            CodeQueryReqVO codeQueryReqVO = new CodeQueryReqVO();
            codeQueryReqVO.setType("term");
            String code = codeGenerationService.generateCodeByVO(codeQueryReqVO).replace("-", "");
            term.setCode(code);
            term.setIfParam(CollectionUtil.isEmpty(addVo.getParams()) ? WhetherEnum.NO.getStringValue() : WhetherEnum.YES.getStringValue());

            // 如果是单位条款库新增，就添加租户信息
            if (TermLibraryEnums.AGENCY.getCode().equals(addVo.getTermLibrary())) {
                term.setTenantId(getLoginUser().getTenantId());
            }
            termMapper.insert(term);
        }
        buildTermParam(addVo.getParams(), term);
//        buildTermLabel(addVo.getLabels(), term);
        return term.getId();
    }
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String addTerm(TermAddVO addVo) {
//        DBExistUtil.isExist(null, addVo.getName(), addVo.getCode(), null, termMapper);
//        Term term = TermConverter.INSTANCE.toEntity(addVo);
//        term.setIfParam(CollectionUtil.isEmpty(addVo.getParams()) ? WhetherEnum.NO.getStringValue() : WhetherEnum.YES.getStringValue());
//        term.setStatus(TermStatusEnums.YES.getCode());
//        termMapper.insert(term);
//        buildTermParam(addVo.getParams(), term);
//        buildTermLabel(addVo.getLabels(), term);
//        return term.getId();
//    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTerm(TermUpdateVO updateVo) {
        Term termSource = termMapper.selectById(updateVo.getId());
        if (termSource == null) {
            throw exception(NO_DATA_FIND_ERROR);
        }
        if (CollectionUtil.isNotEmpty(updateVo.getContractTypes())) {
            updateVo.setContractType(String.join(",", updateVo.getContractTypes()));
        }
        Term term = TermConverter.INSTANCE.toEntity(updateVo);
        term.setIfParam(CollectionUtil.isEmpty(updateVo.getParams()) ? WhetherEnum.NO.getStringValue() : WhetherEnum.YES.getStringValue());

        termLabelMapper.delete(new LambdaQueryWrapperX<TermLabel>().eq(TermLabel::getTermId, term.getId()));
        termParamMapper.delete(new LambdaQueryWrapperX<TermParam>().eq(TermParam::getTermId, term.getId()));

        buildTermParam(updateVo.getParams(), term);
//        buildTermLabel(updateVo.getLabels(), term);

        termMapper.updateById(term);
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateTerm(TermUpdateVO updateVo) {
//        DBExistUtil.isExist(updateVo.getId(), updateVo.getName(), updateVo.getCode(), null, termMapper);
//        Term termSource = termMapper.selectById(updateVo.getId());
//        if (termSource == null) {
//            throw exception(NO_DATA_FIND_ERROR);
//        }
//        Term term = TermConverter.INSTANCE.toEntity(updateVo);
//        term.setIfParam(CollectionUtil.isEmpty(updateVo.getParams()) ? WhetherEnum.NO.getStringValue() : WhetherEnum.YES.getStringValue());
//
//        termLabelMapper.delete(new LambdaQueryWrapperX<TermLabel>().eq(TermLabel::getTermId, term.getId()));
//        termParamMapper.delete(new LambdaQueryWrapperX<TermParam>().eq(TermParam::getTermId, term.getId()));
//
//        buildTermParam(updateVo.getParams(), term);
//        buildTermLabel(updateVo.getLabels(), term);
//
//        termMapper.updateById(term);
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishTerm(TermPublishVO updateVo) {

        Term term = TermConverter.INSTANCE.toEntity(updateVo);
        term.setStatus(WhetherEnum.YES.getStringValue());
        term.setIfParam(CollectionUtil.isEmpty(updateVo.getParams()) ? WhetherEnum.NO.getStringValue() : WhetherEnum.YES.getStringValue());
        if (StringUtils.isNotBlank(updateVo.getId())) {
            termMapper.updateById(term);
            termLabelMapper.delete(new LambdaQueryWrapperX<TermLabel>().eq(TermLabel::getTermId, term.getId()));
            termParamMapper.delete(new LambdaQueryWrapperX<TermParam>().eq(TermParam::getTermId, term.getId()));
        } else {
            termMapper.insert(term);
        }
        buildTermParam(updateVo.getParams(), term);
        buildTermLabel(updateVo.getLabels(), term);
    }

    @Override
    public void removeTermById(String id) {

        Term termSource = termMapper.selectById(id);
        if (termSource == null) {
            throw exception(NO_DATA_FIND_ERROR);
        }
        if (WhetherEnum.YES == WhetherEnum.getInstance(termSource.getStatus())) {
            throw exception(UNCHANGEABLE_ERROR);
        }

        termMapper.deleteById(id);
        termLabelMapper.delete(new LambdaQueryWrapperX<TermLabel>().eq(TermLabel::getTermId, id));
        termParamMapper.delete(new LambdaQueryWrapperX<TermParam>().eq(TermParam::getTermId, id));
        termRepository.deleteById(id);
    }

    @Override
    public List<TermRespVO> queryByConditions(TermQueryVO queryVo) {
        LambdaQueryWrapperX<Term> queryWrapperX = new LambdaQueryWrapperX<Term>()
                .eqIfPresent(Term::getCode, queryVo.getCode())
                .likeIfPresent(Term::getName, queryVo.getName())
                .eqIfPresent(Term::getTermType, queryVo.getTermType())
                .eqIfPresent(Term::getStatus, WhetherEnum.YES.getStringValue())
                .eqIfPresent(Term::getCategoryId, queryVo.getCategoryId())
                .eqIfPresent(Term::getContractType, queryVo.getContractType());
        if (CollectionUtil.isNotEmpty(queryVo.getIgnoreTerms())) {
            queryWrapperX.notIn(Term::getId, queryVo.getIgnoreTerms());
        }
        List<Term> terms = termMapper.selectList(queryWrapperX);
        return TermConverter.INSTANCE.convertList(terms);
    }

    @Override
    public TermRespVO getTermById(String id) {

        Term term = termMapper.selectById(id);
        if (term == null) {
            throw exception(NO_DATA_FIND_ERROR);
        }
        TermRespVO termRespVO = TermConverter.INSTANCE.toVO(term);
        List<TermLabel> termLabels = termLabelMapper.selectList(TermLabel::getTermId, id);
        List<TermLabelVO> labels = TermLabelConverter.INSTANCE.toList(termLabels);
        termRespVO.setLabels(labels);
        List<TermParam> termParams = termParamMapper.selectList(TermParam::getTermId, id);
        List<TermParamVO> params = TermParamConverter.INSTANCE.toList(termParams);
        termRespVO.setParams(params);
        //设置分类名称
        termRespVO.setCategoryName(termsCategoryMapper.selectById(termRespVO.getCategoryId()) == null ? null : termsCategoryMapper.selectById(termRespVO.getCategoryId()).getName());
        // 设置创建人名称
        AdminUserRespDTO user = adminUserApi.getUser(Long.valueOf(termRespVO.getCreator()));
        if (ObjectUtil.isNotNull(user)) {
            termRespVO.setCreatorName(user.getNickname());
        }
        if (StringUtils.isNotBlank(termRespVO.getContractType())) {
            termRespVO.setContractTypes(Arrays.asList(termRespVO.getContractType().split(",")));
            //设置合同类型名称
            List<ContractType> contractTypes = contractTypeMapper.selectList(ContractType::getId, termRespVO.getContractTypes());
            if (CollectionUtil.isNotEmpty(contractTypes)) {
                List<String> names = contractTypes.stream().map(ContractType::getName).collect(Collectors.toList());
                termRespVO.setContractTypeName(String.join(",", names));
                termRespVO.setContractTypeNames(names);
            }
        } else {
            termRespVO.setContractTypes(new ArrayList<>());
        }
        return termRespVO;
    }

    @Override
    public void publish(String id) {
        Term termSource = termMapper.selectById(id);
        if (termSource == null) {
            throw exception(NO_DATA_FIND_ERROR);
        }
        termSource.setStatus(WhetherEnum.YES.getStringValue());
        termMapper.updateById(termSource);
        termRepository.saveSimple(termSource);
    }

    @Override
    public void cancelTerm(String id) {
        Term termSource = termMapper.selectById(id);
        if (termSource == null) {
            throw exception(NO_DATA_FIND_ERROR);
        }
        termSource.setStatus(WhetherEnum.NO.getStringValue());
        termMapper.updateById(termSource);
        termRepository.deleteById(id);
    }

    @Override
    public Map<String, List<TermListRespVO>> queryAllTerms(TermQueryVO queryVo) {
        //获取条款数据
        Map<String, List<TermListRespVO>> respMap = new HashMap<>();
        List<TermRespVO> termRespVOS = this.queryByConditions(queryVo);
        List<TermListRespVO> termListRespVOList = TermParamConverter.INSTANCE.toList2(termRespVOS);
        if (CollectionUtil.isNotEmpty(termListRespVOList)) {
            List<String> termIds = termListRespVOList.stream().map(TermListRespVO::getId).collect(Collectors.toList());
            //参数条款关联表中获取数据
            List<TermParam> termParams = termParamMapper.selectList(new LambdaQueryWrapperX<TermParam>().inIfPresent(TermParam::getTermId, termIds).select(TermParam::getTermId, TermParam::getParamId, TermParam::getParamNum));
            List<String> paramIds = termParams.stream().map(TermParam::getParamId).collect(Collectors.toList());
            //参数表中获取参数信息
            List<Param> params = paramMapper.selectBatchIds(paramIds);
            List<TermParamRespVO> paramRespVOS = ParamConverter.INSTANCE.toList(params);
            //设置合同参数序号
            Map<String, TermParamRespVO> paramMap = CollectionUtils.convertMap(paramRespVOS, TermParamRespVO::getId);

            List<TermListRespVO> headlist = new ArrayList<>();
            List<TermListRespVO> comlist = new ArrayList<>();
            List<TermListRespVO> endlist = new ArrayList<>();
            for (TermListRespVO termListRespVO : termListRespVOList) {
                List<TermParamRespVO> list = new ArrayList<>();
                for (TermParam termParam : termParams) {
                    if (termParam.getTermId().equals(termListRespVO.getId())) {
                        TermParamRespVO termParamRespVO = paramMap.get(termParam.getParamId());
                        if (BeanUtil.isNotEmpty(termParamRespVO)) {
                            termParamRespVO.setParamNum(termParam.getParamNum());
                            list.add(termParamRespVO);
                        }
                    }
                }
                termListRespVO.setParams(list);
                if (HEAD.equals(termListRespVO.getTermType())) {
                    headlist.add(termListRespVO);
                }
                if (COM.equals(termListRespVO.getTermType())) {
                    comlist.add(termListRespVO);
                }
                if (END.equals(termListRespVO.getTermType())) {
                    endlist.add(termListRespVO);
                }
            }
            respMap.put(HEAD, headlist);
            respMap.put(COM, comlist);
            respMap.put(END, endlist);
        }
        return respMap;
    }

    private void buildTermLabel(List<LabelVO> labels, Term term) {
        if (CollectionUtil.isNotEmpty(labels)) {
            List<TermLabel> convert = TermLabelConverter.INSTANCE.convert(labels, term);
            termLabelMapper.insertBatch(convert);
        }
    }

    private void buildTermParam(List<ParamVO> params, Term term) {
        if (CollectionUtil.isNotEmpty(params)) {
            List<TermParam> convert = TermParamConverter.INSTANCE.convert(params, term);
            termParamMapper.insertBatch(convert);
        }
    }

    /**
     * 条款树形结构
     */
    @Override
    public List<TermTreeRespVO> listTermAndCategory(TermTreeReqVO vo) {
        //找出所有子级分类待处理完成
        if (StringUtils.isNotBlank(vo.getCategoryId())) {
            List<TermsCategory> termsCategoryList = termsCategoryMapper.selectList();
            List<TermsCategory> allChildrenCategoryList = findChildCategories(termsCategoryList, Integer.valueOf(vo.getCategoryId()));
            if (CollectionUtil.isNotEmpty(allChildrenCategoryList)) {
                List<String> categoryIds = termsCategoryList.stream().map(TermsCategory::getId).map(String::valueOf).collect(Collectors.toList());
                vo.setCategoryIdList(categoryIds);
            }
        }
        List<Term> termList = termMapper.listTermAndCategory(vo);
        List<TermTreeDetailRespVO> detailRespVOList = TermConverter.INSTANCE.treeDO2RespVO(termList);
        return enhanceTermDetail(detailRespVOList);
    }

    private List<TermsCategory> findChildCategories(List<TermsCategory> categories, Integer index) {
        List<TermsCategory> result = new ArrayList<>();

        for (TermsCategory category : categories) {
            if (category.getParentId().equals(index)) {
                result.add(category);

                // 递归查找当前分类的子分类
                List<TermsCategory> children = findChildCategories(categories, category.getId());
                result.addAll(children);
            }
        }

        return result;
    }

    /**
     * 增强树形结构
     */
    private List<TermTreeRespVO> enhanceTermDetail(List<TermTreeDetailRespVO> detailRespVOList) {
        List<TermTreeRespVO> finalTermTreeRespVOS = new ArrayList<TermTreeRespVO>();
        //将搜索出来的条款按照分类重组
        Map<String, List<TermTreeDetailRespVO>> detailRespVOMap = detailRespVOList.stream()
                .collect(Collectors.groupingBy(TermTreeDetailRespVO::getCategoryId));

        //将分类的信息都找出来
        List<TermsCategory> termsCategoryList = termsCategoryMapper.selectList();
        Map<Integer, TermsCategory> termsCategoryMap = CollectionUtils.convertMap(termsCategoryList, TermsCategory::getId);
        for (Map.Entry<String, List<TermTreeDetailRespVO>> entry : detailRespVOMap.entrySet()) {
            List<TermTreeDetailRespVO> respVOList = entry.getValue();
            TermTreeRespVO termTreeRespVO = new TermTreeRespVO();
            //给分类信息赋值
            TermsCategory termsCategory = termsCategoryMap.get(Integer.valueOf(entry.getKey()));
            if (ObjectUtil.isNotNull(termsCategory)) {
                termTreeRespVO.setId(termsCategory.getId());
                termTreeRespVO.setName(termsCategory.getName());
                termTreeRespVO.setParentId(termsCategory.getParentId());
                termTreeRespVO.setDetailRespVOList(respVOList);
            }
            finalTermTreeRespVOS.add(termTreeRespVO);
        }
        //将空分类排除掉
        finalTermTreeRespVOS = finalTermTreeRespVOS.stream()
                .filter(term -> term.getId() != null)
                .collect(Collectors.toList());
        return finalTermTreeRespVOS;
    }

    /**
     * 条款发起申请
     */
    @Override
    public String submitApproveTerm(TermAddVO reqVO) {
        Long loginUserId = getLoginUserId();
        String termId;
        Term term = new Term();
        //先保存
        termId = addTerm(reqVO);


        term = termMapper.selectById(termId);

        if (ObjectUtil.isNull(term)) {
            throw exception(EMPTY_DATA_ERROR);
        }
        // 判断是否为单位条款库条款，是则需要走审批流，否则直接修改为审批完成
        if (!TermLibraryEnums.AGENCY.getCode().equals(term.getTermLibrary())) {
            termMapper.updateById(new Term().setId(termId).setResult(TermApproveResultStatusEnums.SUCCESS.getCode()));
            termRepository.saveSimple(term);
            return termId;
        }
        //1.插入请求单
        //校验是否发起过审批
        TermApproveResultStatusEnums resultStatusEnums = TermApproveResultStatusEnums.getInstance(term.getResult());
        if (TermApproveResultStatusEnums.TO_SEND != resultStatusEnums && TermApproveResultStatusEnums.REJECTED != resultStatusEnums) {
            throw exception(ErrorCodeConstants.TERM_APPLIED_ERROR);
        }

        // 2 发起申请 BPM
        // 2.1 流程变量
        Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
        processInstanceVariables.put("id", termId);
        // 2.2 流程实例id
        String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(ActivityConfigurationEnum.TERM_APPLICATION_APPROVE.getDefinitionKey()).setVariables(processInstanceVariables).setBusinessKey(termId));
        //更新条款信息
        termMapper.updateById(new Term()
                .setResult(CommonFlowableReqVOResultStatusEnums.APPROVING.getResultCode())
                .setApplyTime(LocalDateTime.now())
                .setId(termId)
                .setProcessInstanceId(processInstanceId)
        );
        return termId;
    }

    @Override
    public TermBigListApproveRespVO getBpmAllTaskPage(Long loginUserId, TermListApproveReqVO pageVO) {
        // 查询所有任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.TERM_APPLICATION_APPROVE.getDefinitionKey());
        //去除已取消的任务。
        processInstanceRelationInfoRespDTOList = EcontractUtil.clearRepealTask(processInstanceRelationInfoRespDTOList);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 递归获取条款分类
        if (ObjectUtil.isNotNull(pageVO.getCategoryId())) {
            List<Integer> categoryIds = new ArrayList();
            selectAllChildTypes(Arrays.asList(pageVO.getCategoryId()), categoryIds);
            pageVO.setCategoryIds(categoryIds);
        }

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream().map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId).distinct().collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<Term> doPageResult = termMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public TermBigListApproveRespVO getBpmDoneTaskPage(Long loginUserId, TermListApproveReqVO pageVO) {
        Integer taskResult = StatusConstants.TEMP_INTEGER;
        if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
            taskResult = pageVO.getTaskResult();
        }
        // 递归获取条款分类
        if (ObjectUtil.isNotNull(pageVO.getCategoryId())) {
            List<Integer> categoryIds = new ArrayList();
            selectAllChildTypes(Arrays.asList(pageVO.getCategoryId()), categoryIds);
            pageVO.setCategoryIds(categoryIds);
        }
        //获得已处理任务数据
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.TERM_APPLICATION_APPROVE.getDefinitionKey(), taskResult);
        //去除已取消的任务。
        processInstanceRelationInfoRespDTOList = EcontractUtil.clearRepealTask(processInstanceRelationInfoRespDTOList);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream().map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId).distinct().collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<Term> doPageResult = termMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public TermBigListApproveRespVO getBpmToDoTaskPage(Long loginUserId, TermListApproveReqVO pageVO) {
        // 查询待办任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.TERM_APPLICATION_APPROVE.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 递归获取条款分类
        if (ObjectUtil.isNotNull(pageVO.getCategoryId())) {
            List<Integer> categoryIds = new ArrayList();
            selectAllChildTypes(Arrays.asList(pageVO.getCategoryId()), categoryIds);
            pageVO.setCategoryIds(categoryIds);
        }
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream().map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId).distinct().collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<Term> doPageResult = termMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    private TermBigListApproveRespVO enhanceBpmPage(PageResult<Term> doPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap) {
        // 查询出代办任务用于判断当前操作人是否可以操作当前数据
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.TERM_APPLICATION_APPROVE.getDefinitionKey());
        List<String> todoList = processInstanceRelationInfoRespDTOList.stream().map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId).collect(Collectors.toList());


        TermBigListApproveRespVO result = new TermBigListApproveRespVO();
        List<Term> doList = doPageResult.getList();
        List<TermListApproveRespVO> respVOList = TermConverter.INSTANCE.convertBpmDO2Resp(doList);
        if (CollectionUtil.isNotEmpty(doList)) {
            //流程信息
            List<String> instanceList = doList.stream().map(Term::getProcessInstanceId).filter(Objects::nonNull).collect(Collectors.toList());

            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> taskEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> taskDoneEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();

            Long loginUserId = getLoginUserId();
            List<BpmTaskAllInfoRespVO> originalTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskEndTimeAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            List<BpmTaskAllInfoRespVO> taskDoneEndTimeAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            //待处理的任务
            Map<String, BpmTaskAllInfoRespVO> toDoTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskAllInfoRespVOList = bpmTaskApi.getAllTaskIdByProcessInstanceIds(instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTimeByUserId(originalTaskAllInfoRespVOList, getLoginUserId());
                toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                taskDoneEndTimeAllInfoRespVOList = EcontractUtil.distinctDoneTaskLatestEndTime(originalTaskAllInfoRespVOList);
                taskDoneEndTimeMap = CollectionUtils.convertMap(taskDoneEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }

            //有结束时间的流程任务
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskEndTimeAllInfoRespVOList = EcontractUtil.distinctTaskHaveEndTime(originalTaskAllInfoRespVOList);
            }

            List<TermsCategory> termsCategoryList = termsCategoryMapper.selectList();
            Map<Integer, TermsCategory> termsCategoryMap = CollectionUtils.convertMap(termsCategoryList, TermsCategory::getId);
            //合同类型
            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);

            List<Long> userIds = new ArrayList<>();
            respVOList.stream().map(TermListApproveRespVO::getCreator).filter(Objects::nonNull).collect(Collectors.toList()).forEach(userId -> {
                userIds.add(Long.valueOf(userId));
            });
            List<AdminUserRespDTO> userList = adminUserApi.getUserList(userIds);
            Map<Long, AdminUserRespDTO> userMap = userList.stream().collect(Collectors.toMap(AdminUserRespDTO::getId, item -> item));
            for (TermListApproveRespVO respVO : respVOList) {
                AdminUserRespDTO adminUserRespDTO = userMap.get(Long.valueOf(respVO.getCreator()));
                if (ObjectUtil.isNotEmpty(adminUserRespDTO)) {
                    respVO.setCreatorName(adminUserRespDTO.getNickname());
                }
                if (ObjectUtil.isNotNull(respVO.getCategoryId())) {
                    TermsCategory termsCategory = termsCategoryMap.get(respVO.getCategoryId());
                    if (ObjectUtil.isNotNull(termsCategory)) {
                        respVO.setCategoryName(termsCategory.getName());
                    }
                }
                if (StringUtils.isNotBlank(respVO.getContractType())) {
                    respVO.setContractTypes(Arrays.asList(respVO.getContractType().split(",")));
                    //设置合同类型名称
                    List<ContractType> contractTypes = contractTypeMapper.selectList(ContractType::getId, respVO.getContractTypes());
                    if (CollectionUtil.isNotEmpty(contractTypes)) {
                        List<String> names = contractTypes.stream().map(ContractType::getName).collect(Collectors.toList());
                        respVO.setContractTypeName(String.join(",", names));
                        respVO.setContractTypeNames(names);
                    }
                }
                respVO.setIsOperate(todoList.contains(respVO.getProcessInstanceId()) ? IfNumEnums.YES.getCode() : IfNumEnums.NO.getCode());

                ContractProcessInstanceRelationInfoRespDTO processInstanceRelationInfoRespDTO = instanceRelationInfoRespDTOMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(processInstanceRelationInfoRespDTO)) {
                    //历史任务信息
                    BpmTaskAllInfoRespVO historyTask = taskMap.get(respVO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(historyTask)) {
                        respVO.setHisTaskResult(historyTask.getResult());
                    }
                    //已审批任务的状态赋值
                    respVO.setDoneTaskResult(processInstanceRelationInfoRespDTO.getProcessResult());
                    //全部审批列表的审批状态(找不到当前登录人的待办任务，既是已办任务)
                    BpmTaskAllInfoRespVO toDoTask = toDoTaskMap.get(processInstanceRelationInfoRespDTO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(toDoTask)) {
                        respVO.setFlowableStatus(FlowableUtil.getFlowableStatus(loginUserId, toDoTask.getAssigneeUserId()));
                    }
                    //审批状态
                    else {
                        respVO.setFlowableStatus(FlowableStatusEnums.DONE.getCode());
                    }

                    respVO.setTaskId(processInstanceRelationInfoRespDTO.getTaskId());
                    PageResult<TermListApproveRespVO> page = new PageResult<TermListApproveRespVO>().setList(respVOList).setTotal(doPageResult.getTotal());
                    //获得动态配置
                    FlowableConfigRespVO flowableConfigRespVO = SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.TERM_APPLICATION_APPROVE));
                    result.setPageResult(page).setFlowableConfigRespVO(flowableConfigRespVO);
                    //历史任务信息（所有审批人）
                    if (todoList.contains(respVO.getProcessInstanceId())) {
                        respVO.setResultName("待审批");
                    } else {
                        if (ObjectUtil.isNotEmpty(historyTask) && BpmProcessInstanceResultEnum.BACK.getResult().equals(historyTask.getResult())) {
                            respVO.setResultName("被退回");
                        } else {
                            switch (respVO.getResult()) {
                                case 0:
                                    respVO.setResultName(BpmProcessInstanceResultEnum.DRAFT.getDesc());
                                    break;
                                case 1:
                                    if (ObjectUtil.isNotNull(historyTask)) {
                                        respVO.setResultName(historyTask.getName());
                                    }
                                    break;
                                case 2:
                                    respVO.setResultName("审批通过");
                                    break;
                                case 5:
                                    respVO.setResultName("被退回");
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
            PageResult<TermListApproveRespVO> pageResult = new PageResult<TermListApproveRespVO>();
            pageResult.setList(respVOList).setTotal(doPageResult.getTotal());
            TermBigListApproveRespVO respVO = new TermBigListApproveRespVO().setPageResult(pageResult);
            //获取配置
            respVO.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.TERM_APPLICATION_APPROVE)));
            return respVO;
        }
        result = new TermBigListApproveRespVO()
                .setPageResult(new PageResult<TermListApproveRespVO>()
                        .setList(Collections.emptyList())
                        .setTotal(doPageResult.getTotal())
                );
        result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.TERM_APPLICATION_APPROVE)));
        return result;
    }

    /**
     * 条款库列表/条款库
     * flag =0 ：条款制作（非通过）：
     * frontCode：{@link CommonFlowableReqVOResultStatusEnums}
     * ALL, TO_SEND, SUCCESS
     * flag =1：条款库（审批通过）
     */
    @Override
    public TermBigRespVO listAutoTerm(TermListApproveReqVO reqVO) {
        List<Long> userIdList = new ArrayList<Long>();
        if (ObjectUtil.isNotNull(reqVO.getCreatorName())) {
            List<AdminUserRespDTO> adminUserRespDTOList = adminUserApi.getUserListLikeNickname(reqVO.getCreatorName());
            userIdList = adminUserRespDTOList.stream().map(AdminUserRespDTO::getId).collect(Collectors.toList());
        }
        // 递归获取条款分类
        if (ObjectUtil.isNotNull(reqVO.getCategoryId())) {
            List<Integer> categoryIds = new ArrayList();
            selectAllChildTypes(Arrays.asList(reqVO.getCategoryId()), categoryIds);
            reqVO.setCategoryIds(categoryIds);
        }
        PageResult<Term> doPageResult = termMapper.listApprovedTerm(reqVO, userIdList);
        TermBigRespVO result = new TermBigRespVO();
        PageResult<TermRespVO> respVOPageResult = TermConverter.INSTANCE.convertPage(doPageResult);
        result.setPageResult(enhancePage(respVOPageResult));
        FlowableConfigRespVO flowableConfigRespVO = SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.TERM_APPLICATION_APPROVE));
        result.setFlowableConfigRespVO(flowableConfigRespVO);
        return result;
    }

    private PageResult<TermRespVO> enhancePage(PageResult<TermRespVO> respVOPageResult) {
        Long loginId = getLoginUserId();

        //条款分类
        List<TermsCategory> termsCategoryList = termsCategoryMapper.selectList();
        Map<Integer, TermsCategory> termsCategoryMap = CollectionUtils.convertMap(termsCategoryList, TermsCategory::getId);
        //合同类型
        List<ContractType> contractTypeList = contractTypeMapper.selectList();
        Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
        //创建人
        List<AdminUserRespDTO> adminUserRespDTOList = adminUserApi.getUserList();
        Map<Long, AdminUserRespDTO> userRespDTOMap = CollectionUtils.convertMap(adminUserRespDTOList, AdminUserRespDTO::getId);
        if (CollectionUtil.isNotEmpty(respVOPageResult.getList())) {

            List<String> instanceList = respVOPageResult.getList().stream().map(TermRespVO::getProcessInstanceId).filter(Objects::nonNull).collect(Collectors.toList());
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskAllInfoRespVOList = bpmTaskApi.getAllTaskIdByProcessInstanceIds(instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }

            for (TermRespVO respVO : respVOPageResult.getList()) {
                if (ObjectUtil.isNotNull(respVO.getCategoryId())) {
                    TermsCategory termsCategory = termsCategoryMap.get(respVO.getCategoryId());
                    if (ObjectUtil.isNotNull(termsCategory)) {
                        respVO.setCategoryName(termsCategory.getName());
                    }
                }
                if (StringUtils.isNotBlank(respVO.getContractType())) {
                    respVO.setContractTypes(Arrays.asList(respVO.getContractType().split(",")));
                    //设置合同类型名称
                    List<ContractType> contractTypes = contractTypeMapper.selectList(ContractType::getId, respVO.getContractTypes());
                    if (CollectionUtil.isNotEmpty(contractTypes)) {
                        List<String> names = contractTypes.stream().map(ContractType::getName).collect(Collectors.toList());
                        respVO.setContractTypeName(String.join(",", names));
                        respVO.setContractTypeNames(names);
                    }
                }
                if (ObjectUtil.isNotNull(respVO.getTermType())) {
                    TermTypeEnums termTypeEnums = TermTypeEnums.getInstance(respVO.getTermType());
                    if (ObjectUtil.isNotNull(termTypeEnums)) {
                        respVO.setTermType(termTypeEnums.getInfo());
                    }
                }
                if (StringUtils.isNotBlank(respVO.getCreator())) {
                    AdminUserRespDTO userRespDTO = userRespDTOMap.get(Long.valueOf(respVO.getCreator()));
                    if (ObjectUtil.isNotNull(userRespDTO)) {
                        respVO.setCreatorName(userRespDTO.getNickname());
                    }
                }
                //如果是被退回状态，需要返回流程任务id
                BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(respVO.getResult());
                if (ObjectUtil.isNotNull(resultEnum)) {
                    if (BpmProcessInstanceResultEnum.BACK == resultEnum) {
                        BpmTaskAllInfoRespVO task = taskMap.get(respVO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(task)) {
                            respVO.setTaskId(String.valueOf(task.getTaskId()));
                        }
                    }
                }
                // 审批状态
                //历史任务信息（所有审批人）
                BpmTaskAllInfoRespVO historyTask = taskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotEmpty(historyTask) && BpmProcessInstanceResultEnum.BACK.getResult().equals(historyTask.getResult())) {
                    respVO.setResultName("被退回");
                } else {
                    switch (resultEnum) {
                        case DRAFT:
                            respVO.setResultName("草稿");
                            break;
                        case PROCESS:
                            if (ObjectUtil.isNotNull(historyTask)) {
                                respVO.setResultName(historyTask.getName());
                            }
                            break;
                        case APPROVE:
                            respVO.setResultName("审批通过");
                            break;
                        case BACK:
                            respVO.setResultName("被退回");
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return respVOPageResult;
    }

    private String content2String(byte[] termContent) {
        return StringUtils.toEncodedString(termContent, StandardCharsets.UTF_8);
    }

    /**
     * 批量提交条款
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitApproveTermBatch(BatchSubmitReqVO vo) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();

        //判断走审批流
        if (systemConfigApi.ifApprove(ActivityConfigurationEnum.TERM_APPLICATION_APPROVE)) {
            // 走审批流
            try {
                submitBatch(loginUserId, vo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 不走审批流，直接通过
            fastPass(vo);
        }
        return "success";
    }

    private void submitBatch(Long loginUserId, BatchSubmitReqVO vo) {
        //分组：首次提交（草稿 或者 已取消的）
        List<SubmitReqVO> submitVOList = FlowableUtil.getSubmitReqVO(vo);
        //分组：被退回的
        List<SubmitReqVO> backVOList = FlowableUtil.getBackReqVO(vo);
        //批量提交
        List<TermAddVO> submitTermAddVOList = FlowableUtil.enhanceBusinessSubmitReq(TermAddVO.class, submitVOList);
        List<String> backTaskIdList = enhanceBackTaskIdList(backVOList);
        if (CollectionUtil.isNotEmpty(submitTermAddVOList)) {
            for (TermAddVO item : submitTermAddVOList) {
                submitApproveTerm(item);
            }
        }

        //批量通过退回任务
        if (CollectionUtil.isNotEmpty(backTaskIdList)) {
            Long userId = SecurityFrameworkUtils.getLoginUserId();
            for (String taskId : backTaskIdList) {
                BpmTaskApproveReqVO taskApproveReqVO = new BpmTaskApproveReqVO().setTaskId(taskId);
                try {
                    taskService.approveTask(userId, taskApproveReqVO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void fastPass(BatchSubmitReqVO vo) {
        List<SubmitReqVO> list = vo.getSubmitReqList();
        if (CollectionUtil.isEmpty(list)) {
            throw exception(FLOWABLE_SUBMIT_REQUEST_ERROR);
        }
        List<String> doIdList = list.stream().map(SubmitReqVO::getBusinessId).collect(Collectors.toList());
        List<Term> entityList = new ArrayList<Term>();
        for (String doId : doIdList) {
            Term entity = new Term();
            entity.setId(doId)
                    .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult())
                    .setApproveTime(LocalDateTime.now());
            entityList.add(entity);
        }
        termMapper.updateBatch(entityList);
    }


    private List<String> enhanceBackTaskIdList(List<SubmitReqVO> backVOList) {
        return backVOList.stream().map(SubmitReqVO::getTaskId).collect(Collectors.toList());
    }

    private List<TermAddVO> enhanceTermSubmitReq(List<SubmitReqVO> submitVOList) {
        List<TermAddVO> result = new ArrayList<TermAddVO>();
        for (SubmitReqVO submitReqVO : submitVOList) {
            TermAddVO r = new TermAddVO();
            r.setId(submitReqVO.getBusinessId());
            result.add(r);
        }
        return result;
    }

    private void selectAllChildTypes(List<Integer> parentIds, List<Integer> typeIds) {
        typeIds.addAll(parentIds);
        List<TermsCategory> list = termsCategoryMapper.selectList(TermsCategory::getParentId, parentIds);
        if (CollectionUtil.isNotEmpty(list)) {
            List<Integer> ids = list.stream().map(TermsCategory::getId).collect(Collectors.toList());
            selectAllChildTypes(ids, typeIds);
        }
    }
}