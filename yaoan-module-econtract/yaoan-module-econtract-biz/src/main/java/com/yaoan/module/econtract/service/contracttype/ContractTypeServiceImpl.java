package com.yaoan.module.econtract.service.contracttype;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistRespVO;
import com.yaoan.module.econtract.controller.admin.contracttype.vo.*;
import com.yaoan.module.econtract.convert.contracttype.ContractTypeConverter;
import com.yaoan.module.econtract.dal.dataobject.code.CodeRuleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.RelContractTypeChecklistDO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewChecklistDO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.param.Param;
import com.yaoan.module.econtract.dal.mysql.code.CodeRuleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contractreviewitems.RelContractTypeChecklistMapper;
import com.yaoan.module.econtract.dal.mysql.contractreviewitems.ReviewChecklistMapper;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.ContractTemplateMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.param.ParamMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.order.ProjectCategoryEnums;
import com.yaoan.module.econtract.service.model.ModelService;
import com.yaoan.module.econtract.util.DBExistUtil;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.flowable.engine.RepositoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
public class ContractTypeServiceImpl extends ServiceImpl<ContractTypeMapper, ContractType> implements ContractTypeService {

    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ModelService modelService;
    @Resource
    private ParamMapper paramMapper;
    @Resource
    private ContractTemplateMapper contractTemplateMapper;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private CodeRuleMapper codeRuleMapper;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RelContractTypeChecklistMapper relContractTypeChecklistMapper;
    @Resource
    private ReviewChecklistMapper reviewChecklistMapper;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(ContractTypeCreateV2Vo vo) {
        // 判断当前层级是否到5级
        judgeLevel(vo);
        //判断编号规则和审批流程是否配置
        List<String> fields = Arrays.asList(
                vo.getCodeRuleId(),
                vo.getDraftApprovalProcess(),
                vo.getRegisterProcess(),
                vo.getChangeProcess(),
                vo.getCollectionProcess(),
                vo.getPaymentProcess(),
                vo.getBorrowProcess()
        );
        if (fields.stream().anyMatch(ObjectUtil::isEmpty)) {
            vo.setTypeStatus(0);
        } else {
            vo.setTypeStatus(1);
        }

        ContractType contractType = ContractTypeConverter.INSTANCE.createVoToEntityV2(vo);
        //自动生成合同类型编号
        contractType.setCode(generateContractTypeNo());
        contractTypeMapper.insert(contractType);
        // 关联审查清单
        if (CollectionUtil.isNotEmpty(vo.getCheckListIds())) {
            List<RelContractTypeChecklistDO> relContractTypeChecklistDOList = vo.getCheckListIds().stream()
                    .map(checkListId -> {
                        RelContractTypeChecklistDO relContractTypeChecklistDO = new RelContractTypeChecklistDO();
                        relContractTypeChecklistDO.setContractType(contractType.getId());
                        relContractTypeChecklistDO.setReviewListId(checkListId);
                        return relContractTypeChecklistDO;
                    }).collect(Collectors.toList());
            relContractTypeChecklistMapper.insertBatch(relContractTypeChecklistDOList);
        }
    }

    private void judgeLevel(ContractTypeCreateV2Vo vo) {
        // 判断当前层级是否到5级
        int level = 1;  // 初始层级是 1
        String currentParentId = vo.getParentId();  // 获取当前父类id
        // 判断父类是否为空，避免NullPointerException
        while (ObjectUtil.isNotEmpty(currentParentId) && !"0".equals(currentParentId) && level < 5) {
            ContractType contractType = contractTypeMapper.selectOne(new LambdaQueryWrapperX<ContractType>()
                    .eq(ContractType::getId, currentParentId).select(ContractType::getParentId));
            // 如果没有找到父类，说明已经到顶层
            if (contractType == null) {
                break;
            }
            // 更新当前父类的ID为父类的parentId，递归判断
            currentParentId = contractType.getParentId();
            level++;  // 增加层级
        }
        // 如果当前层级已经达到5级，不允许继续添加
        if (level >= 5) {
            throw exception(ErrorCodeConstants.CONTRACT_FIVE_ERROR);
        }
    }

    public String generateContractTypeNo() {
        // 查询当前最大的合同类型编号
        ContractType contractType = contractTypeMapper.selectOne(
                new LambdaQueryWrapperX<ContractType>()
                        .select(ContractType::getCode)
                        .ne(ContractType::getCode,"ZC")
                        .orderByDesc(ContractType::getCode)
                        .last("LIMIT 1")
        );
        // 提取数字部分并递增
        int nextNumber = 1;
        if (contractType != null && contractType.getCode() != null) {
            String maxContractTypeNo = contractType.getCode();
            String numberPart = maxContractTypeNo.substring(4);
            nextNumber = Integer.parseInt(numberPart) + 1;
        }
        // 格式化为四位数字并加上前缀 'HTLX'
        return String.format("HTLX%04d", nextNumber);
    }

    public static boolean addSubLevel(List<?> list) {
        int currentDepth = getDepth(list, 1);
        if (currentDepth >= 5) {
            return false; // 深度已经达到或超过5，不允许添加
        }
        return true; // 深度小于5，可以添加下级
    }

    // 递归计算当前列表的最大深度
    private static int getDepth(List<?> list, int currentDepth) {
        if (list == null || list.isEmpty()) {
            return currentDepth;
        }

        if (currentDepth >= 5) {
            return currentDepth; // 达到最大深度 5 时停止
        }

        int maxDepth = currentDepth;
        for (Object item : list) {
            if (item instanceof List) {
                maxDepth = Math.max(maxDepth, getDepth((List<?>) item, currentDepth + 1));
            }
        }
        return maxDepth;
    }

    @Override
    public PageResult<ContractTypePageV2RespVo> getContractTypePage(ContractTypeListV2ReqVO vo) {
        PageResult<ContractTypePageV2RespVo> result = new PageResult<>();
        if (ObjectUtil.isNotEmpty(vo.getIds())) {
            List<ContractType> contractTypes = contractTypeMapper.selectList(ContractType::getId, vo.getIds());
            List<ContractTypePageV2RespVo> respList = ContractTypeConverter.INSTANCE.toSelectRespList(contractTypes);
            fillData(contractTypes, respList);
            return result.setList(respList);
        }
        LambdaQueryWrapperX<ContractType> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.orderByAsc(ContractType::getCode);
        wrapperX.likeIfPresent(ContractType::getName, vo.getName());
        wrapperX.eqIfPresent(ContractType::getTypeStatus, vo.getTypeStatus());
        List<ContractType> contractTypes = contractTypeMapper.selectList(wrapperX);
        List<ContractTypePageV2RespVo> respList = ContractTypeConverter.INSTANCE.toSelectRespList(contractTypes);
        List<ContractTypePageV2RespVo> contractTypePageV2RespVos = listToTree(respList);
        fillData(contractTypes, respList);
        // 手动分页
        int total = contractTypePageV2RespVos.size();
        int fromIndex = Math.min((vo.getPageNo() - 1) * vo.getPageSize(), total);
        int toIndex = Math.min(vo.getPageNo() * vo.getPageSize(), total);
        List<ContractTypePageV2RespVo> pageRecords = contractTypePageV2RespVos.subList(fromIndex, toIndex);
        result.setTotal((long) total);
        result.setList(pageRecords);
        return result;
    }

    public List<ContractTypePageV2RespVo> listToTree(List<ContractTypePageV2RespVo> nodes) {
        // 将所有节点放入Map中，以便通过ID快速访问
        Map<String, ContractTypePageV2RespVo> nodeMap = nodes.stream()
                .collect(Collectors.toMap(ContractTypePageV2RespVo::getId, node -> node));

        // 定义根节点集合
        List<ContractTypePageV2RespVo> rootNodes = new ArrayList<>();
        // 遍历所有节点，将子节点挂载到父节点的children中
        for (ContractTypePageV2RespVo node : nodes) {
            if (node.getParentId().equals("0") || !nodeMap.containsKey(node.getParentId())) {
                // 如果没有父节点或父节点不存在，视为根节点
                rootNodes.add(node);
            } else {
                // 有父节点，将自己添加到父节点的children列表中
                ContractTypePageV2RespVo parent = nodeMap.get(node.getParentId());
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>()); // 初始化 childrenList
                }
                parent.getChildren().add(node);
            }
        }
        return rootNodes;
    }

    private void fillData(List<ContractType> pageResultList, List<ContractTypePageV2RespVo> resultList) {
        //找出用户DO用于赋值
        List<Long> userIds = pageResultList.stream().map(ContractType::getCreator).collect(Collectors.toList()).stream().map(Long::parseLong).collect(Collectors.toList());
        AtomicReference<Map<Long, AdminUserRespDTO>> userMap = new AtomicReference<>(new HashMap<>());
        DataPermissionUtils.executeIgnore(() -> {
            List<AdminUserRespDTO> userRespDTOS = userApi.getUserList(userIds);
            userMap.set(CollectionUtils.convertMap(userRespDTOS, AdminUserRespDTO::getId));
        });

        //查询合同编号规则名称
        List<String> codeIds = pageResultList.stream().map(ContractType::getCodeRuleId).collect(Collectors.toList());
        List<CodeRuleDO> codeRuleDOS = codeRuleMapper.selectList(CodeRuleDO::getId, codeIds);
        Map<String, CodeRuleDO> codeRuleMap = CollectionUtils.convertMap(codeRuleDOS, CodeRuleDO::getId);
        //计算流程配置数量
        List<ContractTypePageV2RespVo> list = countNonEmptyFields(pageResultList);
        Map<String, Integer> countMap = list.stream().collect(Collectors.toMap(ContractTypePageV2RespVo::getId, ContractTypePageV2RespVo::getProcessNum));
        resultList.forEach(resVo -> {
            AdminUserRespDTO userRespDTO = userMap.get().get(Long.valueOf(resVo.getCreator()));
            if (ObjectUtil.isNotEmpty(userRespDTO)) {
                resVo.setCreatorName(userRespDTO.getNickname());
            }
            if (ObjectUtil.isNotNull(countMap.get(resVo.getId()))) {
                resVo.setProcessNum(countMap.get(resVo.getId()));
            }
            if (ObjectUtil.isNotEmpty(resVo.getCodeRuleId())) {
                resVo.setCodeRuleName(codeRuleMap.get(resVo.getCodeRuleId())!=null?codeRuleMap.get(resVo.getCodeRuleId()).getRule():null);
            }
            if (resVo.getTypeStatus().equals(1)) {
                resVo.setTypeStatusName("启用");
            } else {
                resVo.setTypeStatusName("停用");
            }
        });
    }

    public List<ContractTypePageV2RespVo> countNonEmptyFields(List<ContractType> myList) {
        List<ContractTypePageV2RespVo> counts = new ArrayList<>();
        for (ContractType obj : myList) {
            int count = 0;
            if (obj.getBorrowProcess() != null && !obj.getBorrowProcess().isEmpty()) count++;
            if (obj.getChangeProcess() != null && !obj.getChangeProcess().isEmpty()) count++;
            if (obj.getCollectionProcess() != null && !obj.getCollectionProcess().isEmpty()) count++;
            if (obj.getDraftApprovalProcess() != null && !obj.getDraftApprovalProcess().isEmpty()) count++;
            if (obj.getPaymentProcess() != null && !obj.getPaymentProcess().isEmpty()) count++;
            if (obj.getRegisterProcess() != null && !obj.getRegisterProcess().isEmpty()) count++;
            counts.add(new ContractTypePageV2RespVo(obj.getId(), count));
        }
        return counts;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ContractTypeUpdateV2Vo vo) {
        ContractType contractType = ContractTypeConverter.INSTANCE.updateVotoEntityNew(vo);
        if (ObjectUtil.isEmpty(vo)) {
            throw exception(ErrorCodeConstants.CONTRACT_TYPE_EMPTY_PARAM_ERROR);
        }
        if (isNameExists(contractType.getName(), contractType.getId())) {
            throw exception(ErrorCodeConstants.CONTRACT_TYPE_NAME_EXISTS_ERROR);
        }
        ContractType contractTypeNewDO = contractTypeMapper.selectById(vo.getId());
        if (ObjectUtil.isEmpty(contractTypeNewDO)) {
            throw exception(ErrorCodeConstants.DIY_ERROR, "修改失败，合同类型不存在");
        }
        //TODO 实体类更新忽略注解未生效。暂未排查出原因先做如下处理
        contractType.setPlatId(contractTypeNewDO.getPlatId());
        contractTypeMapper.updateById(contractType);
        // 关联审查清单
        if (CollectionUtil.isNotEmpty(vo.getCheckListIds())) {
            relContractTypeChecklistMapper.delete(new LambdaUpdateWrapper<RelContractTypeChecklistDO>().eq(RelContractTypeChecklistDO::getContractType, contractType.getId()));
            List<RelContractTypeChecklistDO> relContractTypeChecklistDOList = vo.getCheckListIds().stream()
                    .map(checkListId -> {
                        RelContractTypeChecklistDO relContractTypeChecklistDO = new RelContractTypeChecklistDO();
                        relContractTypeChecklistDO.setContractType(contractType.getId());
                        relContractTypeChecklistDO.setReviewListId(checkListId);
                        return relContractTypeChecklistDO;
                    }).collect(Collectors.toList());
            relContractTypeChecklistMapper.insertBatch(relContractTypeChecklistDOList);
        }
    }

    @Override
    public List<ContractType> showSelectContractType() {
        return contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().select(ContractType::getId, ContractType::getParentId, ContractType::getName));
    }

    @Override
    public ContractTypeSelectRespVO getContractTypeDetail(String id) {
        ContractType contractTypeNewDO = contractTypeMapper.selectById(id);
        if (ObjectUtil.isEmpty(contractTypeNewDO)) {
            return null;
        }
        ContractTypeSelectRespVO result = ContractTypeConverter.INSTANCE.toSelectRespVO(contractTypeNewDO);
        AdminUserRespDTO userRespDTOS = userApi.getUser(result.getCreator());
        if(ObjectUtil.isNotEmpty(userRespDTOS)){
            result.setCreatorName(userRespDTOS.getNickname());
        }
        CodeRuleDO codeRuleDO = codeRuleMapper.selectById(result.getCodeRuleId());
        if (ObjectUtil.isNotEmpty(codeRuleDO)) {
            result.setIsReserve(codeRuleDO.getIsReserve());
            result.setCodeRuleName(codeRuleDO.getRule());
        }
        ContractType contractType = contractTypeMapper.selectById(contractTypeNewDO.getParentId());
        if (ObjectUtil.isNotEmpty(contractType)) {
            result.setParentName(contractType.getName());
        }
        List<org.flowable.engine.repository.Model> models = repositoryService.createModelQuery().modelTenantId(TenantContextHolder.getTenantId().toString())
                .orderByCreateTime()
                .desc()
                .list();
        Map<String, org.flowable.engine.repository.Model> modelMap = CollectionUtils.convertMap(models, org.flowable.engine.repository.Model::getKey);

        result.setDraftApprovalProcessName(result.getDraftApprovalProcess() == null ? null : modelMap.get(result.getDraftApprovalProcess()).getName());
        result.setRegisterProcessName(result.getRegisterProcess() == null ? null : modelMap.get(result.getRegisterProcess()).getName());
        result.setCollectionProcessName(result.getCollectionProcess() == null ? null : modelMap.get(result.getCollectionProcess()).getName());
        result.setPaymentProcessName(result.getPaymentProcess() == null ? null : modelMap.get(result.getPaymentProcess()).getName());
        result.setBorrowProcessName(result.getBorrowProcess() == null ? null : modelMap.get(result.getBorrowProcess()).getName());
        result.setChangeProcessName(result.getChangeProcess() == null ? null : modelMap.get(result.getChangeProcess()).getName());
        // 获取关联审查清单
        List<RelContractTypeChecklistDO> relContractTypeChecklistDOList = relContractTypeChecklistMapper.selectList(new LambdaQueryWrapperX<RelContractTypeChecklistDO>().eq(RelContractTypeChecklistDO::getContractType, id));
        // 审查清单ids
        List<String> checkListIds = relContractTypeChecklistDOList.stream().map(RelContractTypeChecklistDO::getReviewListId).collect(Collectors.toList());
        List<ReviewChecklistDO> reviewChecklistDOS = reviewChecklistMapper.selectList(ReviewChecklistDO::getId, checkListIds);
        Map<String, ReviewChecklistDO> reviewChecklistDOMap =  CollectionUtils.convertMap(reviewChecklistDOS, ReviewChecklistDO::getId);
        result.setReviewCheckList(relContractTypeChecklistDOList.stream().map(item -> {
            ReviewCheckListVO vo = new ReviewCheckListVO();
            vo.setId(item.getReviewListId());
            vo.setName(reviewChecklistDOMap.get(item.getReviewListId()).getReviewListName());
            return vo;
        }).collect(Collectors.toList()));

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateContractTypeStatus(ContractTypeUpdateStatusReqVO vo) {
        if (ObjectUtil.isEmpty(vo)) {
            throw exception(ErrorCodeConstants.CONTRACT_TYPE_EMPTY_PARAM_ERROR);
        }
        String id = vo.getId();
        Integer status = vo.getContractTypeStatus();
        ContractType contractTypeNewDO = contractTypeMapper.selectById(vo.getId());
        if (vo.getContractTypeStatus().equals(1)) {
            //判断是否有下级
            Long l = contractTypeMapper.selectCount(new LambdaQueryWrapperX<ContractType>().eq(ContractType::getParentId, vo.getId()));
            //有下级则能启用
            if (l > 0) {
                updateStatus(id, status);
                return true;
            } else {
                //若当前类型无下级类型，启用需判断【编号规则】和【审批流程】是否均配置完成
//                List<String> fields = getStrings(contractTypeNewDO);
//                if (fields.stream().anyMatch(ObjectUtil::isEmpty)) {
//                    throw exception(ErrorCodeConstants.CONTRACT_CONFIGURE_ERROR);
//                } else {
//                    updateStatus(id, status);
//                }
                if (contractTypeNewDO.getCodeRuleId() != null) {
                    updateStatus(id, status);
                } else {
                    throw exception(ErrorCodeConstants.SYSTEM_ERROR, "编号规则未配置");
                }

            }
            //有上级要把上级全部启用
            findAllParents(id, status);

            return true;
        }
        if (vo.getContractTypeStatus().equals(0)) {
            //如果该合同类型被使用过则不能停用
            if (isContractTypeUsed(contractTypeNewDO.getId())) {
                throw exception(ErrorCodeConstants.CONTRACT_STOP_ERROR);
            }
            updateStatus(id, status);
            //若该类型下还存在子类型，该类型停用，子类型默认全部【停用】
            List<ContractType> contractTypeNewDOS = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().eq(ContractType::getParentId, vo.getId()));
            if (!contractTypeNewDOS.isEmpty()) {
                List<String> idList = contractTypeNewDOS.stream().map(ContractType::getId).collect(Collectors.toList());
                contractTypeMapper.update(null, new LambdaUpdateWrapper<ContractType>().set(ContractType::getTypeStatus, status).in(ContractType::getId, idList));
                return true;
            }
            //若该父级未配置好【编号规则】和【审批流程】，该父级类型也自动变为【停用】状态
            findAllParents(id, status);
        }


        return true;
    }

    @Override
    public List<ContractTypePageV2RespVo> getList(ContractTypeExportReqVO exportReqVO) {
        List<ContractType> contractTypeNewDOS = contractTypeMapper.selectList(exportReqVO);
        List<ContractTypePageV2RespVo> contractTypeCreateV2Vos = ContractTypeConverter.INSTANCE.toSelectRespList(contractTypeNewDOS);
        fillData(contractTypeNewDOS, contractTypeCreateV2Vos);
        return contractTypeCreateV2Vos;
    }
private final static String GOV_PURCHASE = "政府采购类";
    @Override
    public Object selectList(ContractTypeSelectReqVO vo) {
        if (ObjectUtil.isNotEmpty(vo.getFlag())) {
            if (vo.getFlag() == 0) {
                return DBExistUtil.getCategoryListRespVOListV2(contractTypeMapper.selectList());
            }
            if (vo.getFlag() == 1) {
                LambdaQueryWrapperX<ContractType> wrapperX = new LambdaQueryWrapperX<ContractType>();
                wrapperX.select(ContractType::getId, ContractType::getParentId, ContractType::getName, ContractType::getTypePrefix
                        , ContractType::getTypeStatus, ContractType::getCode, ContractType::getCodeRuleId, ContractType::getPlatId);
                //默认查政采的合同类型
                if(ObjectUtil.isNotNull(vo.getIsGov())){
                    switch (vo.getIsGov()) {
                        case 0:
                            wrapperX.isNull(ContractType::getPlatId);
                            break;
                        case 1:
                            wrapperX.isNotNull(ContractType::getPlatId);
                    }
                }

                List<ContractType> contractTypeNewDOS = contractTypeMapper.selectList(wrapperX);
                Map<String, ContractType> recordMap = contractTypeNewDOS.stream()
                        .collect(Collectors.toMap(ContractType::getId, record -> record));
                List<ContractType> childRecords = contractTypeNewDOS.stream()
                        .filter(record -> record.getTypeStatus() == 1)
                        .collect(Collectors.toList());
                Set<ContractType> resultSet = new HashSet<>();
                for (ContractType child : childRecords) {
                    findAllParents(child, recordMap, resultSet);
                }
                // 查询全部时，确保 "政府采购类" 排在最前面
                List<ContractType> contractTypeList = new ArrayList<>(resultSet);
                if (ObjectUtil.isNull(vo.getIsGov())) {
                    contractTypeList =
                            contractTypeList.stream()
                                    .sorted(
                                            (ct1, ct2) -> {
                                                if (GOV_PURCHASE.equals(ct1.getName())
                                                        && !GOV_PURCHASE.equals(ct2.getName())) {
                                                    return -1;
                                                }
                                                if (GOV_PURCHASE.equals(ct2.getName())
                                                        && !GOV_PURCHASE.equals(ct1.getName())) {
                                                    return 1;
                                                }
                                                return ct1.getName().compareTo(ct2.getName()); // 按 name 字段进行排序
                                            })
                                    .collect(Collectors.toList());
                }
                //转换成树状
                return DBExistUtil.getCategoryListRespVOListV2(contractTypeList);
            }
        }
        return DBExistUtil.getCategoryListRespVOListV2(contractTypeMapper.selectList());
    }

    @Override
    public Object isContractActive(String id) {
        if (isContractTypeUsed(id)) {
            return true;
        }
        return false;
    }

    @Override
    public Object isNeedSignet(ContractTypeSignetReqVO signetReqVO) {
        if (ObjectUtil.isNotEmpty(signetReqVO.getId())){
            ContractType contractType = contractTypeMapper.selectById(signetReqVO.getId());
            if (ObjectUtil.isNotEmpty(contractType)){
                return contractType.getNeedSignet();
            }
        }else if (ObjectUtil.isNotEmpty(signetReqVO.getPlatId())){
            List<Object> platIdList = new ArrayList<>();
            platIdList.add(signetReqVO.getPlatId());
            //兼容传入的为ABC类的类型   
            ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(signetReqVO.getPlatId());
            if(projectCategoryEnums != null ){
                platIdList.add(projectCategoryEnums.getValue());
            }
            List<ContractType> contractTypes = contractTypeMapper.selectList(
                    new LambdaQueryWrapper<ContractType>().in(ContractType::getPlatId, platIdList)
                    );
            if (CollectionUtil.isNotEmpty(contractTypes)){
                return contractTypes.get(0).getNeedSignet();
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<String> ids) {
        //有子级的时候不能删除父级
        Long l = contractTypeMapper.selectCount(new LambdaQueryWrapperX<ContractType>().in(ContractType::getParentId, ids));
        if(l>0){
            throw exception(ErrorCodeConstants.SYSTEM_ERROR,"合同类型存在下级，不可删除");
        }
        LambdaQueryWrapperX<ContractType> wrapper = new LambdaQueryWrapperX<>();
        wrapper.in(ContractType::getId, ids);
        contractTypeMapper.delete(wrapper);
        // 删除关联审查清单
        relContractTypeChecklistMapper.delete(RelContractTypeChecklistDO::getContractType, ids);

    }


    /**
     * 展示所有启用的一级合同类型
     * @return {@link List }<{@link ContractTypeRespVO }>
     */
    @Override
    public List<ContractTypeRespVO> listActiveContractType() {
        List<ContractType> contractTypeList = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
                .select(ContractType::getId,ContractType::getName)
                .eq(ContractType::getTypeStatus,1)
                .eq(ContractType::getParentId,0)
        );
        return ContractTypeConverter.INSTANCE.listDO2Resp(contractTypeList);
    }



    // 递归函数：查找上级
    private void findAllParents(ContractType currentRecord, Map<String, ContractType> recordMap, Set<ContractType> resultSet) {
        // 将当前记录添加到结果集
        resultSet.add(currentRecord);

        // 获取上级记录
        String parentId = currentRecord.getParentId();
        if (parentId != null && recordMap.containsKey(parentId)) {
            ContractType parentRecord = recordMap.get(parentId);
            // 递归查找上级的上级
            findAllParents(parentRecord, recordMap, resultSet);
        }
    }

    private List<String> getStrings(ContractType contractTypeNewDO) {
        return Arrays.asList(
                contractTypeNewDO.getCodeRuleId(),
                contractTypeNewDO.getDraftApprovalProcess(),
                contractTypeNewDO.getRegisterProcess(),
                contractTypeNewDO.getChangeProcess(),
                contractTypeNewDO.getCollectionProcess(),
                contractTypeNewDO.getPaymentProcess(),
                contractTypeNewDO.getBorrowProcess()
        );
    }

    private void updateStatus(String id, Integer status) {
        contractTypeMapper.update(null, new LambdaUpdateWrapper<ContractType>().set(ContractType::getTypeStatus, status).eq(ContractType::getId, id));
    }

    // 递归查询所有上级
    public void findAllParents(String id, Integer status) {
        List<ContractType> result = new ArrayList<>();
        findParentsRecursive(id, result, status);
    }

    // 递归查找上级
    private void findParentsRecursive(String id, List<ContractType> result, Integer status) {
        ContractType contractTypeNewDO = contractTypeMapper.selectById(id);
        if (contractTypeNewDO != null) {
            if (contractTypeNewDO.getTypeStatus() == 1) {
                if (ObjectUtil.isNotEmpty(contractTypeNewDO.getParentId())) {
//                    List<String> fields = getStrings(contractTypeNewDO);
//                    if (fields.stream().anyMatch(ObjectUtil::isEmpty)) {
//                        updateStatus(id, status);
//                    }
                    if (contractTypeNewDO.getCodeRuleId() != null) {
                        updateStatus(id, status);
                    }
                }
            } else {
                updateStatus(id, status);
            }
            result.add(contractTypeNewDO);
            if (status.equals(1)) {
                // 继续查找上级
                if (contractTypeNewDO.getParentId() != null) {
                    findParentsRecursive(contractTypeNewDO.getParentId(), result, status);
                }
            } else {
                if (contractTypeNewDO.getParentId() != null) {
                    ContractType parentDO = contractTypeMapper.selectById(contractTypeNewDO.getParentId());
                    if (parentDO != null) {
//                        List<String> fields = getStrings(parentDO);
//                        if (fields.stream().anyMatch(ObjectUtil::isEmpty)) {
//                            updateStatus(parentDO.getId(), status);
//                        }
                        if(parentDO.getCodeRuleId()!=null){
                            updateStatus(parentDO.getId(), status);
                        }
                        result.add(parentDO);
                    }
                }
            }
        }
    }

    /**
     * 合同类型是否含调用过
     */
    public boolean isContractTypeUsed(String id) {
        if (CollUtil.isNotEmpty(contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getContractType, id).select(ContractDO::getId)))) {
            return true;
        }
        if (CollUtil.isNotEmpty(modelService.list(new LambdaQueryWrapperX<Model>().eq(Model::getContractType, id).select(Model::getId)))) {
            return true;
        }
        if (CollUtil.isNotEmpty(paramMapper.selectList(new LambdaQueryWrapperX<Param>().eq(Param::getContractType, id).select(Param::getId)))) {
            return true;
        }
        return CollUtil.isNotEmpty(contractTemplateMapper.selectList(new LambdaQueryWrapperX<ContractTemplate>().eq(ContractTemplate::getContractType, id).select(ContractTemplate::getId)));
    }

    /**
     * 是否名称已存在
     */
    public boolean isNameExists(String name, String id) {
        Long count = contractTypeMapper.selectCount(new LambdaQueryWrapperX<ContractType>().eq(ContractType::getName, name).ne(ContractType::getId, id));
        return count > 0;
    }


//    final static Integer ZERO = 0;
//    @Resource
//    private ContractTypeMapper contractTypeMapper;
//    @Resource
//    private ContractTypeNewMapper contractTypeMapper;
//    @Resource
//    private AdminUserApi userApi;
//    @Resource
//    private ContractTypeCategoryMapper contractTypeCategoryMapper;
//
//    @Override
//    @DataPermission(enable = false)
//    public PageResult<ContractTypePageRespVo> getContractTypePage(ContractTypeListReqVo vo) {
////        String searchText = vo.getSearchText();
////        List<AdminUserRespDTO> nickUsers = new ArrayList<AdminUserRespDTO>();
////        List<Long> nickUserIds = new ArrayList<Long>();
////
////        if (StringUtils.isNotBlank(searchText)) {
////            nickUsers = userApi.getUserListLikeNickname(searchText);
////            //将昵称模糊查询选中的userId组成List
////            nickUserIds = nickUsers.stream().map(AdminUserRespDTO::getId).collect(Collectors.toList());
////        }
//        //将Long转为String
////        List<String> userIdsAsString = nickUserIds.stream().map(Object::toString).collect(Collectors.toList());
//        LambdaQueryWrapperX<ContractType> wrapperX = new LambdaQueryWrapperX<ContractType>()
//                .orderByDesc(ContractType::getCreateTime)
//                //类型名称模糊匹配
//                .likeIfPresent(ContractType::getName, vo.getSearchText())
//                //类型分类
//                .eqIfPresent(ContractType::getTypeCategory, vo.getTypeCategory());
//        //创建时间
//        if (vo.getStartCreateTime() != null) {
//            wrapperX.between(ContractType::getCreateTime, DateUtils.of(vo.getStartCreateTime()), DateUtils.of(vo.getEndCreateTime()));
//        }
//        if (StringUtils.isNotBlank(vo.getSearchText())) {
//            wrapperX.or(w -> w.like(ContractType::getName, vo.getSearchText()));
//        }
////        if (CollectionUtil.isNotEmpty(userIdsAsString)) {
////            wrapperX.or(w -> w.in(ContractType::getCreator, userIdsAsString));
////        }
//
//        PageResult<ContractType> result = contractTypeMapper.selectPage(vo, wrapperX);
//        PageResult<ContractTypePageRespVo> pageResult = ContractTypeConverter.INSTANCE.convertPage(result);
//        //找出用户DO用于赋值
//        List<Long> userIds = result.getList().stream().map(ContractType::getCreator).collect(Collectors.toList()).stream().map(Long::parseLong).collect(Collectors.toList());
//        List<AdminUserRespDTO> userRespDTOS = userApi.getUserList(userIds);
//
//        Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(userRespDTOS, AdminUserRespDTO::getId);
//        // 找出合同类型分类DO用于赋值
//        if (ObjectUtil.isNotNull(vo.getTypeCategory())) {
//            List<Integer> contractCategoryIds = result.getList().stream().map(ContractType::getTypeCategory).collect(Collectors.toList());
//            if (contractCategoryIds.size() != ZERO) {
//                List<ContractTypeCategory> categoryList = contractTypeCategoryMapper.selectBatchIds(contractCategoryIds);
//                Map<Integer, ContractTypeCategory> categoryMap = CollectionUtils.convertMap(categoryList, ContractTypeCategory::getId);
//                pageResult.getList().forEach(resVo -> {
//                    AdminUserRespDTO userRespDTO = userMap.get(Long.valueOf(resVo.getCreator()));
//                    resVo.setCreatorName(userRespDTO.getNickname());
//                    ContractTypeCategory contractTypeCategory = categoryMap.get(resVo.getTypeCategory());
//                    String categoryName = contractTypeCategory == null ? "" : String.valueOf(contractTypeCategory.getName());
//                    resVo.setCategoryName(categoryName);
//                    resVo.setTypeCategory(contractTypeCategory.getId());
//                });
//            } else {
//                pageResult.getList().forEach(resVo -> {
//                    AdminUserRespDTO userRespDTO = userMap.get(Long.valueOf(resVo.getCreator()));
//                    resVo.setCreatorName(userRespDTO.getNickname());
//                });
//            }
//        } else {
//            List<ContractTypeCategory> categoryList = contractTypeCategoryMapper.selectList();
//            Map<Integer, ContractTypeCategory> categoryMap = CollectionUtils.convertMap(categoryList, ContractTypeCategory::getId);
//            pageResult.getList().forEach(resVo -> {
//                AdminUserRespDTO userRespDTO = userMap.get(Long.valueOf(resVo.getCreator()));
//                if (ObjectUtil.isNotNull(userRespDTO)) {
//                    resVo.setCreatorName(userRespDTO.getNickname());
//                }
//                ContractTypeCategory contractTypeCategory = categoryMap.get(resVo.getTypeCategory());
//                if (contractTypeCategory != null) {
//                    resVo.setCategoryName(contractTypeCategory.getName());
//                    resVo.setTypeCategory(contractTypeCategory.getId());
//                }
//            });
//        }
//
//        return pageResult;
//    }
//
//    @Override
//    public void updateType(ContractTypeUpdateVo vo) {
//        ContractType contractType = ContractTypeConverter.INSTANCE.updateVotoEntity(vo);
//        if (ObjectUtil.isEmpty(vo)) {
//            throw exception(ErrorCodeConstants.CONTRACT_TYPE_EMPTY_PARAM_ERROR);
//        }
//        if (isNameExists(contractType.getName())) {
//            throw exception(ErrorCodeConstants.CONTRACT_TYPE_NAME_EXISTS_ERROR);
//        }
//        if (isCodeExists(contractType.getCode())) {
//            throw exception(ErrorCodeConstants.CONTRACT_TYPE_CODE_EXISTS_ERROR);
//        }
//        contractTypeMapper.updateById(contractType);
//    }
//
//    @Override
//    public List<ContractTypeSimpleRespVo> getContractTypeSimpleList() {
//        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
//        List<ContractTypeSimpleRespVo> respVos = ContractTypeConverter.INSTANCE.convert2SimpleVO(contractTypes);
//        return respVos;
//    }
//
//    @Override
//    public List<ContractTypeCategoryVO> showSelectContractType(ContractTypeSelectReqVO reqVO) {
//
//        List<ContractTypeCategory> allCategories = contractTypeCategoryMapper.selectList();
//        List<ContractTypeCategory> rootList = allCategories.stream().filter(category -> category.getParentId() == 0).collect(Collectors.toList());
//        List<ContractTypeCategoryVO> voList = ContractTypeConverter.INSTANCE.convert2VO(rootList);
//
//        //将合同类型分类按照父子级重组。
//        return voList;
//    }
//
//    @Override
//    public void insert(ContractTypeCreateVo vo) {
//        String typeCode = vo.getCode();
//        if (Boolean.TRUE.equals(existCode(typeCode))) {
//            throw exception(CODE_EXIST_ERROR);
//        }
//        ContractType contractType = ContractTypeConverter.INSTANCE.createVoToEntity(vo);
//        contractTypeMapper.insert(contractType);
//    }
//
//    @Override
//    public void insertV2(ContractTypeCreateV2Vo vo) {
//        //判断编号规则和审批流程是否配置
//        List<String> fields = Arrays.asList(
//                vo.getCodeRuleId(),
//                vo.getDraftApprovalProcess(),
//                vo.getRegisterProcess(),
//                vo.getChangeProcess(),
//                vo.getCollectionProcess(),
//                vo.getPaymentProcess(),
//                vo.getBorrowProcess()
//        );
//        if (fields.stream().anyMatch(ObjectUtil::isEmpty)) {
//            vo.setTypeStatus(0);
//        }else{
//            vo.setTypeStatus(1);
//        }
//        ContractType contractType = ContractTypeConverter.INSTANCE.createVoToEntityV2(vo);
//        contractTypeMapper.insert(contractType);
//    }
//
//    @Override
//    public PageResult<ContractTypePageV2RespVo> getContractTypePageV2(ContractTypeListV2ReqVO vo) {
//        LambdaQueryWrapperX<ContractType> wrapper = new LambdaQueryWrapperX<>();
//        if(ObjectUtil.isNotEmpty(vo.getName())){
//            wrapper.likeIfPresent(ContractType::getName,vo.getName());
//        }
//        if(ObjectUtil.isNotEmpty(vo.getTypeStatus())){
//            wrapper.eq(ContractType::getTypeStatus,vo.getTypeStatus());
//        }
//        PageResult<ContractType> pageResult = contractTypeMapper.selectPage(vo,wrapper);
//        PageResult<ContractTypePageV2RespVo> result = ContractTypeConverter.INSTANCE.convertPageV2(pageResult);
//        return result;
//    }
//
//    /**
//     * 是否名称已存在
//     */
//    public boolean isNameExists(String name) {
//        Long count = contractTypeMapper.selectCount(new LambdaQueryWrapperX<ContractType>().eq(ContractType::getName, name));
//        return count > 0;
//    }
//
//    /**
//     * 是否编号已存在
//     */
//    public boolean isCodeExists(String code) {
//        Long count = contractTypeMapper.selectCount(new LambdaQueryWrapperX<ContractType>().eq(ContractType::getCode, code));
//        return count > 0;
//    }
//
//    /**
//     * 将合同类型分类按照父子重组
//     */
//    public List<ContractTypeCategoryVO> reorganizeCategories(List<ContractTypeCategoryVO> categoryList) {
//        List<ContractType> allContractTypes = contractTypeMapper.selectList();
//        Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(allContractTypes, ContractType::getId);
//
//        // 遍历列表，根据 parentId 将元素放入对应的 childrenList 字段中
//        for (ContractTypeCategoryVO category : categoryList) {
//            Integer parentId = category.getParentId();
//            //如果父级是0，则继续遍历
//
//            //如果父级是存在的，则归入父级category
//
////            if (parentId != 0) {
////                ContractTypeCategoryVO parentCategory = categoryMap.get(parentId);
////                if (parentCategory != null) {
////                    parentCategory.getChildrenList().add(category);
////                }else {
////                    //叶子节点
////                    List<ContractType> contractTypes=allContractTypes.stream()
////                            .filter(contractType -> contractType.getTypeCategory().equals(category.getId()))
////                            .collect(Collectors.toList());
////
////                    category.setDataList(contractTypes);
////                }
////            }
//        }
//        return categoryList;
//    }
//
//    /**
//     * 合同类型的编码是否已经存在
//     */
//    Boolean existCode(String code) {
//        List<ContractType> list = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().eq(ContractType::getCode, code));
//        return list.size() > 0;
//
//    }

}




