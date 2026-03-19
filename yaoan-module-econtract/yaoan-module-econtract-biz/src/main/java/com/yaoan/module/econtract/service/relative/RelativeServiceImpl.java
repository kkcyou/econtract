package com.yaoan.module.econtract.service.relative;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.number.NumberUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.contract.vo.BpmTaskApproveReqVO;
import com.yaoan.module.econtract.controller.admin.param.vo.ReqIdsVO;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistApplyReqVO;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistHandleReqVO;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistPageReqVO;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistRespVO;
import com.yaoan.module.econtract.controller.admin.relative.vo.*;
import com.yaoan.module.econtract.convert.config.SystemConfigDTOConverter;
import com.yaoan.module.econtract.convert.relative.RelativeConverter;
import com.yaoan.module.econtract.convert.relativeBlacklist.relative.RelativeBlacklistConverter;
import com.yaoan.module.econtract.dal.dataobject.category.RelativeCategory;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.relativeBlacklist.RelativeBlacklist;
import com.yaoan.module.econtract.dal.dataobject.relativeContact.RelativeContact;
import com.yaoan.module.econtract.dal.mysql.category.RelativeCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.relativeBlacklist.RelativeBlacklistMapper;
import com.yaoan.module.econtract.dal.mysql.relativeContact.RelativeContactMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums;
import com.yaoan.module.econtract.enums.relative.RelativeStatusV2Enums;
import com.yaoan.module.econtract.service.contract.TaskService;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.flowable.FlowableUtil;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.api.dept.DeptApi;
import com.yaoan.module.system.api.dept.dto.CompanyRespDTO;
import com.yaoan.module.system.api.dept.dto.DeptRespDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyDeptRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.EcontractOrgApi;
import com.yaoan.module.system.api.user.OrganizationApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.api.user.dto.EcontractOrgDTO;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.dal.dataobject.systemuserrel.SystemuserRelDO;
import com.yaoan.module.system.dal.dataobject.user.SupplyDO;
import com.yaoan.module.system.dal.mysql.systemuserrel.SystemuserRelMapper;
import com.yaoan.module.system.dal.mysql.user.SupplyMapper;
import com.yaoan.module.system.service.dept.CompanyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.econtract.service.payment.paymentapplication.PaymentApplicationServiceImpl.AUTO_REASON;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
@Service
public class RelativeServiceImpl implements RelativeService {
    @Resource
    private RelativeMapper relativeMapper; //相对方mapper
    @Resource
    private RelativeContactMapper relativeContactMapper;
    @Resource
    private RelativeBlacklistMapper relativeBlacklistMapper;
    @Resource
    private RelativeCategoryMapper relativeCategoryMapper;  //相对方分类mapper
    @Resource
    private RelativeConverter relativeConverter;
    @Resource
    private TaskService taskService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private CompanyService companyService;
    @Resource
    private OrganizationApi organizationApi;
    @Resource
    private EcontractOrgApi econtractOrgApi;
    @Resource
    private SystemuserRelMapper systemuserRelMapper;
    @Resource
    private SupplyMapper supplyMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private BpmTaskApi bpmTaskApi;

    static final String DEFINITION_KEY = ActivityConfigurationEnum.RELATIVE_APPLICATION_APPROVE.getDefinitionKey();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveRelative(RelativeCreateReqVO relativeCreateReqVO) {
        //校验重复cardNo和Name
        checkRelative(relativeCreateReqVO);

        //1.转换成Relative
        Relative entity = relativeConverter.toEntity(relativeCreateReqVO);
        entity.setStatus(RelativeStatusV2Enums.DRAFTING.getCode());
        entity.setTenantType(0);
        entity.setTenantId(getLoginUser().getTenantId());
        String code = entity.getCode(); //ContractCodeUtil.getCodeByPrefix("XDF");
//        entity.setCode(code);
//        //3.设置账户状态的值
//        entity = setAccountStatus(entity);
//        entity.setCode(entity.getCardNo());
        List<RelativeContact> relativeContactList =  relativeCreateReqVO.getRelativeContactList();
        if(relativeContactList == null ||  relativeContactList.size() == 0 ){
            throw exception(500,"联系人不可为空");
        }
        RelativeContact relativeContact1 = relativeContactList.get(0);
        Long companyId = getLoginUser().getCompanyId();
        entity.setContactAccount(relativeContact1.getContactTel());
        entity.setContactAccount(relativeContact1.getContactTel());
        entity.setContactName(relativeContact1.getName());
        entity.setCompanyId(companyId);
        entity.setVirtualId(NumberUtils.generate());
        relativeMapper.insert(entity);
        String relativeId = entity.getId();
        for(RelativeContact relativeContact : relativeContactList) {
            relativeContact.setRelativeId(relativeId);
            relativeContact.setCardType(CardTypeEnums.IDCARD.getCode());
        }
        relativeContactMapper.insertBatch(relativeContactList);
        
        return entity.getId();
    }

    private void checkRelative(RelativeCreateReqVO reqVO) {
        Long count = relativeMapper.selectCount(Relative::getCardNo, reqVO.getCardNo());
        if(0<count){
            throw  exception(DIY_ERROR,"证件号码已存在");
        }
        Long count1 = relativeMapper.selectCount(Relative::getName, reqVO.getName());
        if(0<count1){
            throw  exception(DIY_ERROR,"相对方名称已存在");
        }
        Long count2 = relativeMapper.selectCount(Relative::getContactTel, reqVO.getContactTel());
        if(0<count2){
            throw  exception(DIY_ERROR,"相对方联系电话已存在");
        }

    }
    //新增和修改联系人合为一个接口

    @Override
    public void saveContact(ContactReqVO contactReqVO) {
        // 报500时
        //1.转换成Relative
        Relative entity = relativeConverter.toEntity(contactReqVO);
        //2.校验联系人信息
        entity = checkParam(entity);
        //3.修改相对方信息
        relativeMapper.updateById(entity);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(RelativeUpdateReqVO relativeUpdateReqVO) throws Exception {
        //1.转换成Relative
        Relative entity = relativeConverter.toEntity(relativeUpdateReqVO);
        String relativeId = entity.getId();
        LambdaQueryWrapperX<RelativeContact> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        lambdaQueryWrapperX.eq(RelativeContact::getRelativeId, entity.getId());
        List<RelativeContact> relativeContactList =  relativeUpdateReqVO.getRelativeContactList();
        //删除已被删除的联系人
        List<String> relativeContactIds = relativeContactList.stream().map(RelativeContact::getId).collect(Collectors.toList());
        lambdaQueryWrapperX.notIn(RelativeContact::getId, relativeContactIds);
        relativeContactMapper.delete(lambdaQueryWrapperX);
        List<RelativeContact> insertContactList = new ArrayList<>();
        List<RelativeContact> updateContactList = new ArrayList<>();
        
        for(RelativeContact relativeContact : relativeContactList) {
            if(relativeContact.getId() != null ){
                updateContactList.add(relativeContact);
            }else{
                relativeContact.setRelativeId(relativeId);
                relativeContact.setCardType(CardTypeEnums.IDCARD.getCode());
                insertContactList.add(relativeContact);
            }
        }
        //批量更新存在的联系人
        relativeContactMapper.updateBatch(updateContactList);
        //批量新增联系人
        relativeContactMapper.insertBatch(insertContactList);
        
        //修改
        relativeMapper.updateById(entity);
    }


    @Override
    public void removeByIds(ReqIdsVO reqIdsVO) {
        //1 根据相对方id列表删除相对方信息
        relativeMapper.deleteBatchIds(reqIdsVO.getIds());
    }

    @Override
    public void deleteContact(String id) {
        //1.将需要删除的字段设为空值
        Relative entity = new Relative();
//        entity.setContactId(null);
//        entity.setContactName(null);
//        entity.setContactAccount(null);
//        entity.setContactTel(null);
//        entity.setContactEmail(null);
//        entity.setContactRemark(null);
        entity.setId(id);
        //2.修改相对方信息中对应的联系人信息
        relativeMapper.update(entity, new LambdaQueryWrapperX<Relative>().eqIfPresent(Relative::getId, id)
        );
    }

    @Override
    @DataPermission(enable = false)
    public RelativeRespVO queryRelativeById(String id) {
        //1.根据相对方id查询相对方信息
        Relative relative = relativeMapper.selectById(id);
        LambdaQueryWrapperX<RelativeContact> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        lambdaQueryWrapperX.eq(RelativeContact::getRelativeId, id);
        List<RelativeContact> relativeContactList =  relativeContactMapper.selectList(lambdaQueryWrapperX);
        //2..转换成Relative
        RelativeRespVO respVO = relativeConverter.toRespVO(relative);
        if (ObjectUtil.isEmpty(respVO)){
            return null;
        }
        respVO.setRelativeContactList(relativeContactList);
        //3.设置创建人和更新人
        List<Long> userIds = new ArrayList<>();
        userIds.add(Long.valueOf(respVO.getCreator()));
        userIds.add(Long.valueOf(respVO.getUpdater()));
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(()->{
                List<AdminUserRespDTO> userList1 = adminUserApi.getUserList(userIds);
                if (ObjectUtil.isNotEmpty(userList1)) {
                    Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(userList1, AdminUserRespDTO::getId);
                    respVO.setCreatorName(userMap.get(Long.valueOf(respVO.getCreator())).getNickname());
                    respVO.setUpdaterName(userMap.get(Long.valueOf(respVO.getUpdater())).getNickname());
                }
            });
        });
        populateEnumInfo(respVO.getEntityType(), EntityTypeEnums::getInstance, EntityTypeEnums::getInfo, respVO::setEntityTypeName);
        populateEnumInfo(respVO.getRelativeType(), RelativeTypeEnums::getInstance, RelativeTypeEnums::getInfo, respVO::setRelativeTypeName);
        populateEnumInfo(respVO.getSourceType(), SourceTypeEnums::getInstance, SourceTypeEnums::getInfo, respVO::setSourceTypeName);
        populateEnumInfo(respVO.getStatus(), value -> RelativeStatusEnums.getInstance(Integer.valueOf(value)),
                RelativeStatusEnums::getInfo, respVO::setStatusName);
        populateEnumInfo(respVO.getCardType(), CardTypeEnums::getInstance, CardTypeEnums::getInfo, respVO::setCardTypeName);
        populateEnumInfo(respVO.getLegalCardType(), CardTypeEnums::getInstance, CardTypeEnums::getInfo, respVO::setLegalCardTypeName);
        populateEnumInfo(respVO.getHeadCardType(), CardTypeEnums::getInstance, CardTypeEnums::getInfo, respVO::setHeadCardTypeName);
        return respVO;
    }

    private <T, R> void populateEnumInfo(T value, Function<T, R> enumMapper, Function<R, String> infoGetter, Consumer<String> nameSetter) {
        if (value != null) {
            R instance = enumMapper.apply(value);
            if (instance != null) {
                nameSetter.accept(infoGetter.apply(instance));
            }
        }
    }

    @Override
    public PageResult<RelativeListRespVO> queryAllRelative(RelativePageReqVO relativePageReqVO) {
        //查询相对方管理列表信息
        PageResult<Relative> relativePageResult = relativeMapper.queryAllRelative(relativePageReqVO, "1", null);
        PageResult<RelativeListRespVO> relativeListRespVOPageResult = relativeConverter.convertPage(relativePageResult);
        relativeListRespVOPageResult = setCategoryName(relativeListRespVOPageResult, relativePageReqVO.getEntityType());
        relativeListRespVOPageResult = setCreatorName(relativeListRespVOPageResult);
        if (CollUtil.isNotEmpty(relativeListRespVOPageResult.getList())) {
            List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getALLToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.RELATIVE_APPLICATION_APPROVE.getDefinitionKey());
            Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList;
            List<String> instanceList = processInstanceRelationInfoRespDTOList.stream().map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskAllInfoRespVOList = bpmTaskApi.getAllTaskIdByProcessInstanceIds(instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }
            Map<String, BpmTaskAllInfoRespVO> finalTaskMap = taskMap;
            //设置其他枚举类的name值
            for (RelativeListRespVO relativeListRespVO : relativeListRespVOPageResult.getList()) {

                ContractProcessInstanceRelationInfoRespDTO processInstanceRelationInfoRespDTO =instanceRelationInfoRespDTOMap.get(relativeListRespVO.getProcessInstanceId());
                if(ObjectUtil.isNotNull(processInstanceRelationInfoRespDTO)){
                    relativeListRespVO.setTaskId(processInstanceRelationInfoRespDTO.getTaskId());
                }

                if(relativeListRespVO.getEntityType() != null ){
                    relativeListRespVO.setEntityTypeName(EntityTypeEnums.getInstance(relativeListRespVO.getEntityType()).getInfo());
                }
                if(relativeListRespVO.getRelativeType() != null ){
                    relativeListRespVO.setRelativeTypeName(RelativeTypeEnums.getInstance(relativeListRespVO.getRelativeType()).getInfo());
                }
                if(relativeListRespVO.getSourceType() != null){
                    relativeListRespVO.setSourceTypeName(SourceTypeEnums.getInstance(relativeListRespVO.getSourceType()).getInfo());
                }
                if(relativeListRespVO.getStatus() != null){
                    relativeListRespVO.setStatusName(RelativeStatusEnums.getInstance(Integer.valueOf(relativeListRespVO.getStatus())).getInfo());
                }
                RelativeStatusV2Enums statusV2Enums = RelativeStatusV2Enums.getInstance(relativeListRespVO.getStatus());
                if(ObjectUtil.isNotNull(statusV2Enums)){
                    relativeListRespVO.setFlowableStatusName(statusV2Enums.getInfo());
                }
                //任务节点
                //审批中
                if (ObjectUtil.isNotNull(relativeListRespVO.getProcessInstanceId()) && ObjectUtil.isNotNull(finalTaskMap.get(relativeListRespVO.getProcessInstanceId()))) {
                    relativeListRespVO.setNodeName(finalTaskMap.get(relativeListRespVO.getProcessInstanceId()).getName());
                }
                //未提交
                else if ("4".equals(relativeListRespVO.getStatus())) {
                    relativeListRespVO.setNodeName("草稿箱");
                }

            }
        }
        return relativeListRespVOPageResult;
    }

    @DataPermission(enable = false)
    @Override
    public PageResult<RelativeListRespVO> queryAllByContact(RelativePageReqVO relativePageReqVO) {
        //查询已添加联系人且已激活的信息
        // 条件：1.主体类型为企业或政府  2.激活状态为已激活  3.已添加联系人==》有联系人的一定是已激活的，顾只需判断是否有联系人即可
        Long companyId = null;
        UserCompanyDeptRespDTO userCompanyDeptRespDTO = adminUserApi.selectUserCompanyDept(WebFrameworkUtils.getLoginUserId());
        if (BeanUtil.isNotEmpty(userCompanyDeptRespDTO)) {
            CompanyRespDTO companyInfo = userCompanyDeptRespDTO.getCompanyInfo();
            if (BeanUtil.isNotEmpty(companyInfo)) {
                companyId = companyInfo.getId();
            }
        }
        PageResult<Relative> relativePageResult = relativeMapper.queryAllRelative(relativePageReqVO, "2", companyId);
        PageResult<RelativeListRespVO> relativeListRespVOPageResult = relativeConverter.convertPage(relativePageResult);
        relativeListRespVOPageResult = setCategoryName(relativeListRespVOPageResult, relativePageReqVO.getEntityType());
        relativeListRespVOPageResult = setCreatorName(relativeListRespVOPageResult);
        if (CollUtil.isNotEmpty(relativeListRespVOPageResult.getList())) {
            //设置其他枚举类的name值
            for (RelativeListRespVO relativeListRespVO : relativeListRespVOPageResult.getList()) {
                if(relativeListRespVO.getEntityType() != null ){
                    relativeListRespVO.setEntityTypeName(EntityTypeEnums.getInstance(relativeListRespVO.getEntityType()).getInfo());
                }
                if(relativeListRespVO.getRelativeType() != null ){
                    relativeListRespVO.setRelativeTypeName(RelativeTypeEnums.getInstance(relativeListRespVO.getRelativeType()).getInfo());
                }
                if(relativeListRespVO.getCardType() != null ){
                    relativeListRespVO.setCardTypeName(CardTypeEnums.getInstance(relativeListRespVO.getCardType()).getInfo());
                }
                if(relativeListRespVO.getSourceType() != null){
                    relativeListRespVO.setSourceTypeName(SourceTypeEnums.getInstance(relativeListRespVO.getSourceType()).getInfo());
                }
                if(relativeListRespVO.getStatus() != null){
                    relativeListRespVO.setStatusName(RelativeStatusEnums.getInstance(Integer.valueOf(relativeListRespVO.getStatus())).getInfo());
                }
            }
        }
//        for (RelativeListRespVO relativeListRespVO : relativeListRespVOPageResult.getList()) {
//            if(!EntityTypeEnums.INDIVIDUAL.getCode().equals(relativeListRespVO.getEntityType())){
//                relativeListRespVO.setName(relativeListRespVO.getContactName());
//                relativeListRespVO.setAccount(relativeListRespVO.getContactAccount());
//            }
//        }
//        relativeListRespVOPageResult = setCategoryName(relativeListRespVOPageResult, relativePageReqVO.getEntityType());
//        relativeListRespVOPageResult = setCreatorName(relativeListRespVOPageResult);
        return relativeListRespVOPageResult;
    }

    @Override
    public RelativeByUserRespVO queryRelativeByLoginUser() {
        RelativeByUserRespVO relativeByUserRespVO = new RelativeByUserRespVO();
        UserCompanyDeptRespDTO userCompanyDeptRespDTO = adminUserApi.selectUserCompanyDept(WebFrameworkUtils.getLoginUserId());
        relativeByUserRespVO.setAccount(userCompanyDeptRespDTO.getUsername());
        relativeByUserRespVO.setName(userCompanyDeptRespDTO.getNickname());
        relativeByUserRespVO.setUserId(userCompanyDeptRespDTO.getId());
        //部门id为空 表示此账号为个人账户
        if (ObjectUtil.isEmpty(userCompanyDeptRespDTO.getDeptId())) {
            relativeByUserRespVO.setEntityType(EntityTypeEnums.INDIVIDUAL.getCode());
        } else {
            //部门id不为空 表示此账号为企业或者单位账户
            if (ObjectUtil.isNotEmpty(userCompanyDeptRespDTO.getCompanyInfo())) {
                //此部门为顶级部门
                relativeByUserRespVO.setCompanyName(userCompanyDeptRespDTO.getCompanyInfo().getName());
                relativeByUserRespVO.setCreditCode(userCompanyDeptRespDTO.getCompanyInfo().getCreditCode());
                relativeByUserRespVO.setEntityType(userCompanyDeptRespDTO.getCompanyInfo().getMajor().toString());
            }
        }
        if (ObjectUtil.isNotEmpty(userCompanyDeptRespDTO.getOrgId())) {
            OrganizationDTO organization = organizationApi.getOrganization(userCompanyDeptRespDTO.getOrgId());
            if (ObjectUtil.isNotEmpty(organization)) {
                relativeByUserRespVO.setContactAccount(organization.getLinkMan() != null ? organization.getLinkMan() : null);
                relativeByUserRespVO.setContactName(organization.getLinkMan() != null ? organization.getLinkMan() : null);
                relativeByUserRespVO.setContactTel(organization.getLinkPhone() != null ? organization.getLinkPhone() : null);
            }
            EcontractOrgDTO econtractOrg = econtractOrgApi.getEcontractOrgById(userCompanyDeptRespDTO.getOrgId());
            if (ObjectUtil.isNotEmpty(econtractOrg)){
                relativeByUserRespVO.setBankAccountName(econtractOrg.getBankAccountName());
                relativeByUserRespVO.setBankName(econtractOrg.getBankName());
                relativeByUserRespVO.setBankAccount(econtractOrg.getBankAccount());
            }

        } else {
            //获取当前用户
            LoginUser loginUser = getLoginUser();
            List<SystemuserRelDO> systemUserRelDOS = systemuserRelMapper.selectList(new LambdaQueryWrapperX<SystemuserRelDO>().eq(SystemuserRelDO::getCurrentTenantId, loginUser.getTenantId()));
            if (CollUtil.isNotEmpty(systemUserRelDOS)) {
                OrganizationDTO organization = organizationApi.getOrganization(systemUserRelDOS.get(0).getBuyerOrgId());
                if (ObjectUtil.isNotEmpty(organization)) {
                    relativeByUserRespVO.setContactAccount(organization.getLinkMan() != null ? organization.getLinkMan() : null);
                    relativeByUserRespVO.setContactTel(organization.getLinkPhone() != null ? organization.getLinkPhone() : null);
                }
            }
        }
        //设置主体类型
        relativeByUserRespVO.setEntityTypeName(setEntityType(relativeByUserRespVO.getEntityType()));
        return relativeByUserRespVO;
    }

    private Long getTopLevelDepartmentId(Long departmentId, Long pId) {
        List<DeptRespDTO> deptList = deptApi.getDeptList();
        //3.将部门信息转成map
        Map<Long, DeptRespDTO> deptDOMap = CollectionUtils.convertMap(deptList, DeptRespDTO::getId);
        Long parentId = deptDOMap.get(departmentId).getParentId();  // 查询部门的父ID
        if (parentId == pId) {
            return departmentId;  // 找到顶级部门ID
        } else {
            return getTopLevelDepartmentId(parentId, pId);  // 继续递归调用
        }
    }


    private Relative checkParam(Relative entity) {
        //1.获取电子签约平台账号到用户表中查询用户信息,电子签约平台账号==用户表system_users中username字段
        // 根据账号查询是否有此可用用户信息，单位和企业联系人不校验姓名，
        /**
        if (StringUtils.isNotBlank(entity.getContactAccount())) {
            AdminUserRespDTO adminUserRespDTO = adminUserApi.selectAdminUser(entity.getContactAccount(), null, null);
            Relative relative = relativeMapper.selectById(entity.getId());
            if (ObjectUtil.isNotEmpty(adminUserRespDTO) && ObjectUtil.isNotEmpty(relative)) {
//                CompanyRespDTO company;
//                DeptRespDTO dept = getDeptRespDTO(adminUserRespDTO.getDeptId());//
//                //2.求顶级部门id
//
//                if (dept.getParentId()==0L) {
//                    //为公司
//                    //3.根据部门id查询可用公司信息
//                    company = companyApi.getCompany(adminUserRespDTO.getDeptId(), 0);
//                } else{
//                    Long departmentId = getTopLevelDepartmentId(dept.getParentId(), 0L);
//                    company = companyApi.getCompany(departmentId, 0);
//                }

                //4.校验相对方公司id 判断联系人是否属于此相对方公司
                if (!adminUserRespDTO.getCompanyId().equals(relative.getRelativeCompanyId())) {
                    throw exception(ErrorCodeConstants.COMPNAY_ERROR);
                }
                //5. 设置联系人id
              //  entity.setContactId(adminUserRespDTO.getId());
            } else {
                throw exception(ErrorCodeConstants.USER_ERROR);
            }
        }
         */
        return entity;
    }

    @DataPermission(enable = false)
    private DeptRespDTO getDeptRespDTO(Long id) {
        DeptRespDTO dept = deptApi.getDept(id);
        return dept;
    }


    private Relative setAccountStatus(Relative entity) {
        //1.如果是个人就到用户表system_users中校验联系人信息
        // 校验姓名+身份证号+账号+状态
       /* if (EntityTypeEnums.INDIVIDUAL.getCode().equals(entity.getEntityType())) {
            //校验个人身份证是否重复
            idCardExist(entity.getId(), entity.getIdCard());
            //校验用户信息
            AdminUserRespDTO adminUserRespDTO = adminUserApi.selectAdminUser(entity.getAccount(), entity.getName(), entity.getIdCard());
            if (ObjectUtil.isNotEmpty(adminUserRespDTO)) {
                //2.匹配成功，将账号状态设置为1：已激活
                entity.setAccountStatus(AccountStatus.ACTIVATED.getCode());
                entity.setContactId(adminUserRespDTO.getId());
            } else {
                //默认给未激活，满足改已激活
                //2.匹配成失败，将账号状态设置为0：未激活
                entity.setAccountStatus(AccountStatus.UNACTIVATED.getCode());
            }
        } else {
            //2.校验公司名和信用代码是否重复
            companyExist(entity.getId(), entity.getCompanyName(), entity.getCreditCode());
            CompanyRespDTO company = companyApi.getCompany(entity.getCompanyName(), entity.getCreditCode(), 0);
            if (ObjectUtil.isNotEmpty(company)) {
                //2.匹配成功，将账号状态设置为1：已激活
                entity.setAccountStatus(AccountStatus.ACTIVATED.getCode());
                //3.设置相对方公司id
                entity.setRelativeCompanyId(company.getId());
            } else {
                //2.匹配成失败，将账号状态设置为0：未激活
                entity.setAccountStatus(AccountStatus.UNACTIVATED.getCode());
            }
        }*/
        return entity;
    }

    private PageResult<RelativeListRespVO> setCategoryName(PageResult<RelativeListRespVO> relativeListRespVOPageResult, String entityType) {
        //获取所有分类信息
        List<RelativeCategory> relativeCategories = relativeCategoryMapper.selectList(new LambdaQueryWrapperX<RelativeCategory>().eq(RelativeCategory::getEntityType, entityType));
        Map<Integer, RelativeCategory> categoryMap = CollectionUtils.convertMap(relativeCategories, RelativeCategory::getId);
        if (CollUtil.isNotEmpty(relativeListRespVOPageResult.getList())) {
            //设置参数分类名称  优化  转成map key 为id
            for (RelativeListRespVO relativeListRespVO : relativeListRespVOPageResult.getList()) {
                RelativeCategory category = categoryMap.get(relativeListRespVO.getCategoryId());
                relativeListRespVO.setCategoryName(category == null ? null : category.getName());
            }
        }
        return relativeListRespVOPageResult;
    }

    private PageResult<RelativeListRespVO> setCreatorName(PageResult<RelativeListRespVO> relativeListRespVOPageResult) {
        //获取所有creator用户信息
        List<String> creatorIds = relativeListRespVOPageResult.getList().stream().map(RelativeListRespVO::getCreator).collect(Collectors.toList());
        List<Long> ids = creatorIds.stream().map(s-> s==null ? 0 : Long.parseLong(s)).collect(Collectors.toList());
        
        AtomicReference<List<AdminUserRespDTO>> userList = new AtomicReference<>(new ArrayList<>());
        DataPermissionUtils.executeIgnore(()->{
            userList.set(adminUserApi.getUserList(ids));
        });
                
        Map<Long, AdminUserRespDTO> creatorMap = CollectionUtils.convertMap(userList.get(), AdminUserRespDTO::getId);

        if (CollUtil.isNotEmpty(relativeListRespVOPageResult.getList())) {
            //设置参数分类名称  优化  转成map key 为id
            for (RelativeListRespVO relativeListRespVO : relativeListRespVOPageResult.getList()) {
                AdminUserRespDTO adminUserRespDTO = creatorMap.get(Long.valueOf(relativeListRespVO.getCreator()));
                relativeListRespVO.setCreatorName(adminUserRespDTO == null ? null : adminUserRespDTO.getNickname());
                //设置主体类型
                relativeListRespVO.setEntityTypeName(setEntityType(relativeListRespVO.getEntityType()));
                //设置状态
                //已激活
//                if (AccountStatus.ACTIVATED.getCode().equals(relativeListRespVO.getAccountStatus())) {
//                    relativeListRespVO.setAccountStatusName(AccountStatus.ACTIVATED.getInfo());
//                }
//                //未激活
//                if (AccountStatus.UNACTIVATED.getCode().equals(relativeListRespVO.getAccountStatus())) {
//                    relativeListRespVO.setAccountStatusName(AccountStatus.UNACTIVATED.getInfo());
//                }
            }
        }
        return relativeListRespVOPageResult;
    }

    public void companyExist(String id, String companyName, String creditCode) {
        if (relativeMapper.companyNameExist(id, companyName) || relativeMapper.creditCodeExist(id, creditCode)) {
            //公司名或信用代码重复
            throw exception(ErrorCodeConstants.COMPNAY_EXISTS);
        }
    }

    public void idCardExist(String id, String idCard) {
        if (relativeMapper.idCardExist(id, idCard)) {
            //身份证号重复
            throw exception(ErrorCodeConstants.IDCARD_EXISTS);
        }
    }

    private String setEntityType(String entityType) {
        String entityTypeName = null;
        //个人
        if (EntityTypeEnums.INDIVIDUAL.getCode().equals(entityType)) {
            entityTypeName = EntityTypeEnums.INDIVIDUAL.getInfo();
        }
        //单位
        if (EntityTypeEnums.ORGANIZATION.getCode().equals(entityType)) {
            entityTypeName = EntityTypeEnums.ORGANIZATION.getInfo();
        }
        //企业
        if (EntityTypeEnums.COMPANY.getCode().equals(entityType)) {
            entityTypeName = EntityTypeEnums.COMPANY.getInfo();
        }
        return entityTypeName;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveRelativeBlacklist(BlacklistApplyReqVO blacklistApplyReqVO) {
        
        RelativeBlacklist relativeBlacklist = new RelativeBlacklist();
        relativeBlacklist.setRelativeId(blacklistApplyReqVO.getRelativeId());
        relativeBlacklist.setApplyType(blacklistApplyReqVO.getApplyType());
        relativeBlacklist.setApplyMsg(blacklistApplyReqVO.getApplyMsg());
        relativeBlacklist.setFileId(blacklistApplyReqVO.getFileId());
        relativeBlacklist.setFileName(blacklistApplyReqVO.getFileName());
        relativeBlacklist.setApplyStatus(0);
        relativeBlacklistMapper.insert(relativeBlacklist);
        Relative relative = new Relative();
        relative.setId(blacklistApplyReqVO.getRelativeId());
        if(0 == blacklistApplyReqVO.getApplyType()){
            relative.setStatus(String.valueOf(RelativeStatusEnums.ToBlacklist.getCode()));
        }else{
            relative.setStatus(String.valueOf(RelativeStatusEnums.RemoveBlacklist.getCode()));
        }
        relativeMapper.updateById(relative);
        return relativeBlacklist.getId();
    }

    @Override
    public PageResult<BlacklistRespVO> getRelativeBlacklist(BlacklistPageReqVO blacklistPageReqVO) {
        PageResult<RelativeBlacklist> relativeBlacklistPageResult = new PageResult<>();
        AtomicReference<PageResult<RelativeBlacklist>> ignoreList = new AtomicReference<>(new PageResult<>());

        DataPermissionUtils.executeIgnore(()->{
            TenantUtils.executeIgnore(()->{
                ignoreList.set(relativeBlacklistMapper.selectRegisterPage(blacklistPageReqVO));
            });
        });
        relativeBlacklistPageResult = ignoreList.get();
        if(relativeBlacklistPageResult.getTotal() == 0 ){
            return PageResult.empty();
        }
        List<String > relativeIdList = relativeBlacklistPageResult.getList().stream().map(RelativeBlacklist::getRelativeId).collect(Collectors.toList());
        LambdaQueryWrapperX<Relative> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        lambdaQueryWrapperX.in(Relative::getId, relativeIdList);
        List<Relative> relativeList = relativeMapper.selectList(lambdaQueryWrapperX);
        PageResult<BlacklistRespVO> blacklistRespVOPageResult = RelativeBlacklistConverter.INSTANCE.convertPage(relativeBlacklistPageResult);
        List<String> creatorIds = relativeBlacklistPageResult.getList().stream().map(RelativeBlacklist::getCreator).collect(Collectors.toList());
        List<Long> ids = creatorIds.stream().map(Long::valueOf).collect(Collectors.toList());
        List<AdminUserRespDTO> userList = adminUserApi.getUserList(ids);
        Map<Long, AdminUserRespDTO> creatorMap = CollectionUtils.convertMap(userList, AdminUserRespDTO::getId);
        List<Long> deptIds =  userList.stream().map(AdminUserRespDTO::getDeptId).collect(Collectors.toList());
        Map<Long,DeptRespDTO> deptRespDTOMap = new HashMap<>();
        if (CollUtil.isNotEmpty(deptIds)){
            List<DeptRespDTO> deptRespDTOS =  deptApi.getDeptList(deptIds);
            deptRespDTOMap = CollectionUtils.convertMap(deptRespDTOS, DeptRespDTO::getId);
        }
        if (CollUtil.isNotEmpty(blacklistRespVOPageResult.getList())) {
            for (BlacklistRespVO blacklistRespVO : blacklistRespVOPageResult.getList()) {
                AdminUserRespDTO adminUserRespDTO = creatorMap.get(Long.valueOf(blacklistRespVO.getCreator()));
                blacklistRespVO.setCreatorName(adminUserRespDTO == null ? null : adminUserRespDTO.getNickname());
                if(adminUserRespDTO.getDeptId() != null ){
                    DeptRespDTO deptRespDTO = deptRespDTOMap.get(adminUserRespDTO.getDeptId());
                    if(deptRespDTO != null){
                        blacklistRespVO.setDeptName(deptRespDTO.getName());
                    }
                }
                for (Relative relative: relativeList) {
                    
                    if (relative.getId().equals(blacklistRespVO.getRelativeId())) {
                        blacklistRespVO.setCode(relative.getCode());
                        blacklistRespVO.setName(relative.getName());
                        blacklistRespVO.setSourceType(relative.getSourceType());
                        blacklistRespVO.setEntityType(relative.getEntityType());
                        blacklistRespVO.setRelativeType(relative.getRelativeType());
                        blacklistRespVO.setLevelNo(relative.getLevelNo());
                        blacklistRespVO.setStatus(relative.getStatus());
                        if(blacklistRespVO.getEntityType() != null ){
                            blacklistRespVO.setEntityTypeName(EntityTypeEnums.getInstance(blacklistRespVO.getEntityType()).getInfo());
                        }
                        if(blacklistRespVO.getRelativeType() != null ){
                            blacklistRespVO.setRelativeTypeName(RelativeTypeEnums.getInstance(blacklistRespVO.getRelativeType()).getInfo());
                        }
                        if(blacklistRespVO.getSourceType() != null){
                            blacklistRespVO.setSourceTypeName(SourceTypeEnums.getInstance(blacklistRespVO.getSourceType()).getInfo());
                        }

                    }
                }
            }
        }
        
        
        return blacklistRespVOPageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String auditRelativeBlacklistApply(BlacklistHandleReqVO blacklistApplyReqVO) {
        RelativeBlacklist relativeBlacklist = relativeBlacklistMapper.selectById(blacklistApplyReqVO.getId()); //RelativeBlacklistConverter.INSTANCE.toEntity(blacklistApplyReqVO);
        relativeBlacklist.setAuditType(0);
        relativeBlacklist.setApplyStatus(1);
        relativeBlacklist.setAuditMsg(blacklistApplyReqVO.getAuditMsg());
        relativeBlacklistMapper.updateById(relativeBlacklist);
        Relative relative = new Relative();
        relative.setId(relativeBlacklist.getRelativeId());
        
        if(relativeBlacklist.getApplyType() == 0 ){
            //移入黑名单，审批通过则原相对方加入黑名单 状态2
            relative.setStatus(String.valueOf(RelativeStatusEnums.Blacklist.getCode()));
            relativeMapper.updateById(relative);
        }else if(relativeBlacklist.getApplyType() == 1){
            //移出黑名单  审批通过则原相对方状态设置为正常
            relative.setStatus(String.valueOf(RelativeStatusEnums.Normal.getCode()));
            relativeMapper.updateById(relative);
        }else{
            throw  new RuntimeException("无法回去申请状态");
        }
        
        return relativeBlacklist.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String rejectRelativeBlacklistApply(BlacklistHandleReqVO blacklistApplyReqVO) {
        RelativeBlacklist relativeBlacklist = relativeBlacklistMapper.selectById(blacklistApplyReqVO.getId()); //RelativeBlacklistConverter.INSTANCE.toEntity(blacklistApplyReqVO);
        relativeBlacklist.setApplyStatus(1);
        relativeBlacklist.setAuditType(1);
        relativeBlacklist.setAuditMsg(blacklistApplyReqVO.getAuditMsg());
        relativeBlacklistMapper.updateById(relativeBlacklist);
        
        Relative relative = new Relative();
        relative.setId(relativeBlacklist.getRelativeId());
        if(relativeBlacklist.getApplyType() == 0 ){
            //移入黑名单，驳回则原相对方加入黑名单 状态2
            relative.setStatus(String.valueOf(RelativeStatusEnums.Normal.getCode()));
            relativeMapper.updateById(relative);
        }else if(relativeBlacklist.getApplyType() == 1){
            //移出黑名单  驳回则原相对方状态设置为正常
            relative.setStatus(String.valueOf(RelativeStatusEnums.Blacklist.getCode()));
            relativeMapper.updateById(relative); 
        }else{
            throw  new RuntimeException("无法回去申请状态");
        }
        
        relativeMapper.updateById(relative);
        return relativeBlacklist.getId();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BlacklistRespVO queryRelativeBlacklistById(String id) {
        RelativeBlacklist relativeBlacklist = relativeBlacklistMapper.selectById(id);
        BlacklistRespVO blacklistRespVO = RelativeBlacklistConverter.INSTANCE.toVO(relativeBlacklist);
        AdminUserRespDTO user = adminUserApi.getUser(Long.valueOf(blacklistRespVO.getCreator()));
        if(user != null){
            blacklistRespVO.setCreatorName(user.getNickname());
        }
        if(user.getDeptId() != null ){
            DeptRespDTO deptRespDTO = deptApi.getDept(user.getDeptId());
            if(deptRespDTO != null){
                blacklistRespVO.setDeptName(deptRespDTO.getName());
            }
        }
        Relative relative = relativeMapper.selectById(relativeBlacklist.getRelativeId());
        if (relative == null ){
            return blacklistRespVO;
        }
        blacklistRespVO.setCode(relative.getCode());
        blacklistRespVO.setName(relative.getName());
        blacklistRespVO.setSourceType(relative.getSourceType());
        blacklistRespVO.setEntityType(relative.getEntityType());
        blacklistRespVO.setRelativeType(relative.getRelativeType());
        blacklistRespVO.setLevelNo(relative.getLevelNo());
        blacklistRespVO.setStatus(relative.getStatus());
        blacklistRespVO.setCardType(relative.getCardType());
        blacklistRespVO.setCardNo(relative.getCardNo());

        if(blacklistRespVO.getEntityType() != null ){
            blacklistRespVO.setEntityTypeName(EntityTypeEnums.getInstance(blacklistRespVO.getEntityType()).getInfo());
        }
        if(blacklistRespVO.getRelativeType() != null ){
            blacklistRespVO.setRelativeTypeName(RelativeTypeEnums.getInstance(blacklistRespVO.getRelativeType()).getInfo());
        }
        if(blacklistRespVO.getSourceType() != null){
            blacklistRespVO.setSourceTypeName(SourceTypeEnums.getInstance(blacklistRespVO.getSourceType()).getInfo());
        }
        if(blacklistRespVO.getCardType() != null){
            blacklistRespVO.setCardTypeName(CardTypeEnums.getInstance(blacklistRespVO.getCardType()).getInfo());
        }
        return blacklistRespVO ;
    }

    @Override
    public Relative selectById(String id) {
        Relative relative = relativeMapper.selectById(id);
        if(relative == null){
            SupplyDO supplyDO = supplyMapper.selectById(id);
            if(supplyDO != null){
                relative = RelativeConverter.INSTANCE.supplyToEntity(supplyDO); 
            }
        }
        return relative;
    }
    @Resource
    private BpmProcessInstanceApi processInstanceApi;


    @Override
    public String submit(RelativeSubReqVO vo) {
        Long loginUserId = getLoginUserId();
        String relativeId = vo.getRelativeId();
        String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(DEFINITION_KEY).setBusinessKey(relativeId));
        //更新相对方申请信息
        relativeMapper.updateById(new Relative()
                .setResult(CommonFlowableReqVOResultStatusEnums.APPROVING.getResultCode())
                .setStatus(RelativeStatusV2Enums.APPROVING.getCode())
                .setSubmitTime(LocalDateTime.now())
                .setId(relativeId)
                .setProcessInstanceId(processInstanceId)
        );

        //是否直接通过
        if(ObjectUtil.isNotEmpty(vo.getIsSubmit()) && 1 == vo.getIsSubmit()){
            //添加taskId
            if(ObjectUtil.isNotEmpty(processInstanceId)){
                List<String> processInstanceIds = Arrays.asList(processInstanceId);
                List<ContractProcessInstanceRelationInfoRespDTO>  processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(DEFINITION_KEY);

                if(ObjectUtil.isNotEmpty(processInstanceRelationInfoRespDTOList)){
                    String taskId = processInstanceRelationInfoRespDTOList.get(0).getTaskId();
                        BpmTaskApproveReqVO taskApproveReqVO = new BpmTaskApproveReqVO().setTaskId(taskId).setReason(AUTO_REASON);
                        try {
                            taskService.approveTask(getLoginUserId(), taskApproveReqVO);
                        } catch (Exception e) {
                            throw exception(DIY_ERROR,"approveTask异常");
                        }

                }
            }
        }
        return processInstanceId;
    }

    /**
     * 相对方申请列表
     * */
    @Override
    public PageResult<RelativeBpmListBpmRespVO> listApplication(RelativeBpmPageReqVO vo) {
        PageResult<Relative> relativePageResult = relativeMapper.selectPage(vo);
        if(CollectionUtil.isEmpty(relativePageResult.getList())){
            return new PageResult<RelativeBpmListBpmRespVO>();
        }
        return enhancePage(relativePageResult);
    }

    private PageResult<RelativeBpmListBpmRespVO> enhancePage(PageResult<Relative> relativePageResult) {
        PageResult<RelativeBpmListBpmRespVO> respVOPageResult = RelativeConverter.INSTANCE.convertBpmPage(relativePageResult);
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.RELATIVE_APPLICATION_APPROVE.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        for (RelativeBpmListBpmRespVO respVO : respVOPageResult.getList()) {
            CommonFlowableReqVOResultStatusEnums resultEnum = CommonFlowableReqVOResultStatusEnums.getInstance(respVO.getResult());
            if(ObjectUtil.isNotNull(resultEnum)){
                respVO.setResultStr(resultEnum.getInfo());
            }
            ContractProcessInstanceRelationInfoRespDTO processInstanceRelationInfoRespDTO =instanceRelationInfoRespDTOMap.get(respVO.getProcessInstanceId());
            if(ObjectUtil.isNotNull(processInstanceRelationInfoRespDTO)){
                respVO.setTaskId(processInstanceRelationInfoRespDTO.getTaskId());
            }

            if(respVO.getEntityType() != null ){
                respVO.setEntityType(EntityTypeEnums.getInstance(respVO.getEntityType()).getInfo());
            }
            if(respVO.getRelativeType() != null ){
                respVO.setRelativeTypeName(RelativeTypeEnums.getInstance(respVO.getRelativeType()).getInfo());
            }
            if(respVO.getSourceType() != null){
                respVO.setSourceTypeName(SourceTypeEnums.getInstance(respVO.getSourceType()).getInfo());
            }
            if(respVO.getStatus() != null){
                respVO.setStatusName(RelativeStatusEnums.getInstance(Integer.valueOf(respVO.getStatus())).getInfo());
            }
        }
        return respVOPageResult;
    }

    @Override
    public BigRelativeBpmRespVO getBpmAllTaskPage(Long loginUserId, RelativeBpmPageReqVO pageVO) {
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        // 查询待办任务
        processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(DEFINITION_KEY);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<Relative> doPageResult = relativeMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public BigRelativeBpmRespVO getBpmDoneTaskPage(Long loginUserId, RelativeBpmPageReqVO pageVO) {
        Integer taskResult = StatusConstants.TEMP_INTEGER;
        // 查询待办任务
        if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
            taskResult = pageVO.getTaskResult();
        }
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        //获得已处理任务数据(已过滤掉已取消的任务),可筛选审批状态
        processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(DEFINITION_KEY, taskResult);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<Relative> doPageResult = relativeMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public BigRelativeBpmRespVO getBpmToDoTaskPage(Long loginUserId, RelativeBpmPageReqVO pageVO) {
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        // 查询待办任务
        processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.RELATIVE_APPLICATION_APPROVE.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<Relative> doPageResult = relativeMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    private BigRelativeBpmRespVO enhanceBpmPage(PageResult<Relative> doPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap) {

        List<Relative> doList = doPageResult.getList();
        List<RelativeBpmListBpmRespVO> respVOList = RelativeConverter.INSTANCE.cList(doList);
        if (CollectionUtil.isNotEmpty(doList)) {
            //流程信息（业务和流程同表）
            List<String> instanceList = doList.stream().map(Relative::getProcessInstanceId).collect(Collectors.toList());
            Map<String, BpmTaskAllInfoRespVO> taskEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();

            Long loginUserId = getLoginUserId();
            List<BpmTaskAllInfoRespVO> originalTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            //待处理的任务
            Map<String, BpmTaskAllInfoRespVO> toDoTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            if (CollectionUtil.isNotEmpty(instanceList)) {
                originalTaskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(loginUserId), instanceList);

                toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTimeByUserId(originalTaskAllInfoRespVOList, getLoginUserId());
                toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }
            //获取所有creator用户信息
            List<String> creatorIds = respVOList.stream().map(RelativeBpmListBpmRespVO::getCreator).collect(Collectors.toList());
            List<Long> ids = creatorIds.stream().map(s-> s==null ? 0 : Long.parseLong(s)).collect(Collectors.toList());
            AtomicReference<List<AdminUserRespDTO>> userList = new AtomicReference<>(new ArrayList<>());
            DataPermissionUtils.executeIgnore(()->{
                userList.set(adminUserApi.getUserList(ids));
            });
            
            Map<Long, AdminUserRespDTO> creatorMap = CollectionUtils.convertMap(userList.get(), AdminUserRespDTO::getId);

            for (RelativeBpmListBpmRespVO respVO : respVOList) {
                AdminUserRespDTO user = creatorMap.get(Long.valueOf(respVO.getCreator()));
                if(ObjectUtil.isNotNull(user)) {
                    respVO.setCreatorName(user.getNickname());
                }
                //业务状态
                if(respVO.getEntityType() != null ){
                    respVO.setEntityTypeName(EntityTypeEnums.getInstance(respVO.getEntityType()).getInfo());
                }
                if(respVO.getRelativeType() != null ){
                    respVO.setRelativeTypeName(RelativeTypeEnums.getInstance(respVO.getRelativeType()).getInfo());
                }
                if(respVO.getSourceType() != null){
                    respVO.setSourceTypeName(SourceTypeEnums.getInstance(respVO.getSourceType()).getInfo());
                }
                if(respVO.getStatus() != null){
                    respVO.setStatusName(RelativeStatusEnums.getInstance(Integer.valueOf(respVO.getStatus())).getInfo());
                }

                //审批状态
                BpmTaskAllInfoRespVO toDoTaskAllInfoRespVO = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(toDoTaskAllInfoRespVO)) {
                    respVO.setFlowableStatus(FlowableUtil.getFlowableStatus(loginUserId, toDoTaskAllInfoRespVO.getAssigneeUserId()));
                } else {
                    respVO.setFlowableStatus(FlowableStatusEnums.DONE.getCode());
                }
                BpmProcessInstanceResultEnum bpmProcessInstanceResultEnum = BpmProcessInstanceResultEnum.getInstance(respVO.getResult());
                if (ObjectUtil.isNotNull(bpmProcessInstanceResultEnum)) {
                    respVO.setResultStr(bpmProcessInstanceResultEnum.getDesc());
                }
                //流程任务
                ContractProcessInstanceRelationInfoRespDTO processInstanceRelationInfoRespDTO = instanceRelationInfoRespDTOMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(processInstanceRelationInfoRespDTO)) {
                    respVO.setTaskId(processInstanceRelationInfoRespDTO.getTaskId());

                }
                //历史任务信息（所有审批人）
                BpmTaskAllInfoRespVO historyTask = taskEndTimeMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(historyTask)) {
                    respVO.setHisTaskResult(historyTask.getResult());
                }
                //全部审批列表的审批状态(找不到当前登录人的待办任务，既是已办任务)
                BpmTaskAllInfoRespVO toDoTask = toDoTaskMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(toDoTask)) {
                    respVO.setAssigneeId(toDoTask.getAssigneeUserId());
                    respVO.setFlowableStatus(FlowableUtil.getFlowableStatus(loginUserId, toDoTask.getAssigneeUserId()));

                }
                //审批状态(全部里)
                else {
                    respVO.setFlowableStatus(FlowableStatusEnums.DONE.getCode());
                }
                //已审批任务的状态赋值（当前人对该审批的最近的一次操作结果）
                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = instanceRelationInfoRespDTOMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(infoRespDTO)) {
                    respVO.setDoneTaskResult(infoRespDTO.getProcessResult());
                }
            }
            PageResult<RelativeBpmListBpmRespVO> pageResult = new PageResult<RelativeBpmListBpmRespVO>();
            pageResult.setList(respVOList).setTotal(doPageResult.getTotal());
            BigRelativeBpmRespVO result = new BigRelativeBpmRespVO().setPageResult(pageResult);
            //获取配置
            result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.RELATIVE_APPLICATION_APPROVE)));
            return result;
        }
        BigRelativeBpmRespVO result = new BigRelativeBpmRespVO()
                .setPageResult(new PageResult<RelativeBpmListBpmRespVO>()
                        .setList(Collections.emptyList())
                        .setTotal(doPageResult.getTotal())
                );
        result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.RELATIVE_APPLICATION_APPROVE)));
        return result;
    }

    @Override
    public Long getOneDefaultContactId(String relativeId) {
        Relative relative = relativeMapper.selectOne(Relative::getId, relativeId);
        if (ObjectUtil.isNotNull(relative) && ObjectUtil.isNotEmpty(relative.getContactId())) {
            return relative.getVirtualId();
        } else {
            MPJLambdaWrapper<RelativeContact> mpjLambdaWrapper = new MPJLambdaWrapper<>();
            mpjLambdaWrapper.select(RelativeContact::getRelativeId,RelativeContact::getUserId)
                    .leftJoin(Relative.class, Relative::getId, RelativeContact::getRelativeId)
                    .eq(RelativeContact::getRelativeId, relativeId);
            mpjLambdaWrapper.orderByAsc(RelativeContact::getCreateTime);
            List<RelativeContact> relativeContact = relativeContactMapper.selectList(mpjLambdaWrapper);
            if (CollectionUtil.isNotEmpty(relativeContact)){
                return relativeContact.get(0).getUserId();
            }
            return 0L;
        }
    }
    @Override
    public List<Long> getAllContactId(String relativeId) {
        List<Long> ret  = new ArrayList<>();
        Relative relative = relativeMapper.selectOne(Relative::getId, relativeId);
        if (ObjectUtil.isNotNull(relative) && ObjectUtil.isNotEmpty(relative.getContactId())) {
            ret.add(relative.getContactId());
        }
        MPJLambdaWrapper<RelativeContact> mpjLambdaWrapper = new MPJLambdaWrapper<>();
        mpjLambdaWrapper.select(RelativeContact::getRelativeId,RelativeContact::getUserId)
                .leftJoin(Relative.class, Relative::getId, RelativeContact::getRelativeId)
                .eq(RelativeContact::getRelativeId, relativeId);
        mpjLambdaWrapper.orderByAsc(RelativeContact::getCreateTime);
        List<RelativeContact> relativeContact = relativeContactMapper.selectList(mpjLambdaWrapper);
        if (CollectionUtil.isNotEmpty(relativeContact)){
            ret.addAll(relativeContact.stream().map(RelativeContact::getUserId).collect(Collectors.toList()));
        }
        return ret;

    }

}
