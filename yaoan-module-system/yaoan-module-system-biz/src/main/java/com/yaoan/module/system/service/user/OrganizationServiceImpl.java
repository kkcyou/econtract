package com.yaoan.module.system.service.user;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.bpm.api.bpm.activity.ActGeByteArrayApi;
import com.yaoan.module.bpm.api.bpm.activity.ActReModelApi;
import com.yaoan.module.bpm.api.bpm.activity.dto.ActGeByteArrayDTO;
import com.yaoan.module.bpm.api.bpm.activity.dto.ActReModelDTO;
import com.yaoan.module.econtract.api.contract.ContractProcessApi;
import com.yaoan.module.econtract.api.contract.ContractTypeApi;
import com.yaoan.module.econtract.api.contract.dto.ProductOrgDTO;
import com.yaoan.module.econtract.api.contract.dto.UserDTO;
import com.yaoan.module.econtract.api.contracttype.dto.ContractTypeDTO;
import com.yaoan.module.econtract.api.model.ModelApi;
import com.yaoan.module.econtract.api.model.dto.ModelDTO;
import com.yaoan.module.econtract.api.modelcategory.ModelCategoryApi;
import com.yaoan.module.econtract.api.modelcategory.dto.ModelCategoryDTO;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.supervise.PurCatalogTypeEnums;
import com.yaoan.module.econtract.enums.templatecategory.PlatformTemplateCategoryEnums;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.api.user.dto.RegionWithOrgDTO;
import com.yaoan.module.system.controller.admin.dept.vo.company.CompanyCreateReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import com.yaoan.module.system.controller.admin.org.vo.OrgReqVo;
import com.yaoan.module.system.controller.admin.org.vo.OrgRespVo;
import com.yaoan.module.system.controller.admin.permission.vo.role.RoleCreateReqVO;
import com.yaoan.module.system.controller.admin.region.vo.RegionReqVo;
import com.yaoan.module.system.controller.admin.tenant.vo.tenant.TenantCreateReqVO;
import com.yaoan.module.system.convert.tenant.TenantConvert;
import com.yaoan.module.system.convert.user.OrganizationConvert;
import com.yaoan.module.system.convert.user.UserConvert;
import com.yaoan.module.system.dal.dataobject.config.SystemConfigDO;
import com.yaoan.module.system.dal.dataobject.dept.CompanyDO;
import com.yaoan.module.system.dal.dataobject.dept.UserPostDO;
import com.yaoan.module.system.dal.dataobject.permission.RoleMenuDO;
import com.yaoan.module.system.dal.dataobject.region.Region;
import com.yaoan.module.system.dal.dataobject.systemuserrel.SystemuserRelDO;
import com.yaoan.module.system.dal.dataobject.tenant.TenantDO;
import com.yaoan.module.system.dal.dataobject.tenant.TenantPackageDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.dal.dataobject.user.OrganizationDO;
import com.yaoan.module.system.dal.mysql.config.SystemConfigMapper;
import com.yaoan.module.system.dal.mysql.dept.CompanyMapper;
import com.yaoan.module.system.dal.mysql.dept.DeptMapper;
import com.yaoan.module.system.dal.mysql.dept.UserPostMapper;
import com.yaoan.module.system.dal.mysql.permission.RoleMenuMapper;
import com.yaoan.module.system.dal.mysql.region.RegionMapper;
import com.yaoan.module.system.dal.mysql.systemuserrel.SystemuserRelMapper;
import com.yaoan.module.system.dal.mysql.tenant.TenantMapper;
import com.yaoan.module.system.dal.mysql.user.AdminUserMapper;
import com.yaoan.module.system.dal.mysql.user.OrganizationMapper;
import com.yaoan.module.system.enums.permission.RoleCodeEnum;
import com.yaoan.module.system.enums.permission.RoleTypeEnum;
import com.yaoan.module.system.enums.user.UserTypeEnums;
import com.yaoan.module.system.service.dept.CompanyService;
import com.yaoan.module.system.service.dept.DeptService;
import com.yaoan.module.system.service.permission.PermissionService;
import com.yaoan.module.system.service.permission.RoleService;
import com.yaoan.module.system.service.tenant.TenantPackageService;
import com.yaoan.module.system.service.tenant.TenantService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.common.util.collection.CollectionUtils.convertList;
import static com.yaoan.module.system.enums.ErrorCodeConstants.USER_COUNT_MAX;
import static java.util.Collections.singleton;

/**
 * 单位 用户 实现类
 *
 * @author zhc
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Value("${feign.client.contract.client_id}")
    private String clientId;
    @Value("${feign.client.contract.client_secret}")
    private String clientSecret;
    private static final AtomicInteger SEQUENCE_COUNTER = new AtomicInteger(0);

    // 日期格式
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Resource
    private OrganizationMapper organizationMapper;
    @Resource
    private RegionMapper regionMapper;

    @Resource
    private SystemuserRelMapper systemuserRelMapper;

    @Resource
    private TenantService tenantService;
    @Resource
    private TenantMapper tenantMapper;
    @Resource
    private TenantPackageService tenantPackageService;

    @Resource
    private ContractProcessApi contractProcessApi;

    @Resource
    private ModelApi modelApi;

    @Resource
    private CompanyService companyService;

    @Resource
    private DeptService deptService;

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private AdminUserMapper userMapper;

    @Resource
    private SystemConfigMapper systemConfigMapper;

    @Resource
    private ContractTypeApi contractTypeApi;
    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleService roleService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private DeptMapper deptMapper;

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private UserPostMapper userPostMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private ActReModelApi actReModelApi;

    @Resource
    private ActGeByteArrayApi actGeByteArrayApi;

    @Resource
    private ModelCategoryApi modelCategoryApi;

    @Override
    public List<OrganizationDTO> getOrganizationByName(String name) {
        List<OrganizationDO> organizationDOS = organizationMapper.selectList(new LambdaQueryWrapperX<OrganizationDO>().likeIfPresent(OrganizationDO::getName, name));
        return OrganizationConvert.INSTANCE.toDTOList(organizationDOS);
    }

    @Override
    public OrganizationDTO getOrganization(String id) {
        OrganizationDO organizationDO = organizationMapper.selectById(id);
        OrganizationDTO dto = OrganizationConvert.INSTANCE.toDTO(organizationDO);
        return dto;
    }

    @Override
    public List<OrganizationDTO> getOrganizationList(RegionReqVo reqVO) {
        List<OrganizationDO> organizationDOS = organizationMapper.selectByRegionCode(reqVO);
        return OrganizationConvert.INSTANCE.toDTOList(organizationDOS);
    }

    @Override
    public List<OrganizationDTO> getOrganizationByIds(List<String> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            List<OrganizationDO> organizationDOS = organizationMapper.selectBatchIds(ids);
            return OrganizationConvert.INSTANCE.toDTOList(organizationDOS);
        }
        return null;
    }

    @Override
    public List<OrganizationDTO> getOrganizationListByRegionGuid(String regionGuid) {
        List<OrganizationDO> organizationDOS = organizationMapper.selectList(new LambdaQueryWrapperX<OrganizationDO>().eq(OrganizationDO::getRegionGuid, regionGuid));
        return OrganizationConvert.INSTANCE.toDTOList(organizationDOS);
    }

    @Override
    public List<RegionWithOrgDTO> getOrgListByDistrict(RegionReqVo reqVO) {
        List<RegionWithOrgDTO> result = new ArrayList<>();
        //（相对方逻辑）免租户
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                // 1. 根据区划编码获取本级及所有下级区划 ID
                List<Region> regionIdList = getAllRegionIdsByCode(reqVO.getRegionCode());
                if (regionIdList.isEmpty()) {
                    return;
                }
                List<String> regionIds = regionIdList.stream().map(Region::getRegionGuid).collect(Collectors.toList());
                // 2. 根据区划 ID 查询单位列表
                List<OrganizationDO> orgList = organizationMapper.selectList(new LambdaQueryWrapperX<OrganizationDO>()
                        .in(OrganizationDO::getRegionGuid, regionIds).select(OrganizationDO::getId, OrganizationDO::getName, OrganizationDO::getRegionGuid));
                List<OrganizationDTO> orgDTOs = OrganizationConvert.INSTANCE.toDTOList(orgList);
                // 3. 按区划 ID 归类单位
                Map<String, List<OrganizationDTO>> regionUnitMap = orgDTOs.stream()
                        .collect(Collectors.groupingBy(OrganizationDTO::getRegionGuid));
                for (Region region : regionIdList) {
                    RegionWithOrgDTO dto = new RegionWithOrgDTO();
                    dto.setId(region.getRegionGuid());
                    dto.setCode(region.getRegionCode());
                    dto.setName(region.getRegionName());
                    dto.setParentId(region.getRegionParentGuid());
                    dto.setChildren(regionUnitMap.getOrDefault(region.getRegionGuid(), new ArrayList<>()));

                    result.add(dto);
                }

            });
        });
        return result;
    }

    @Override
    public PageResult<OrgRespVo> getOrgList(OrgReqVo reqVO) {

        if (ObjectUtil.isNotEmpty(reqVO.getIsOpen())) {
            List<SystemuserRelDO> systemuserRelDOS = systemuserRelMapper.selectList();
            List<String> orgIds = systemuserRelDOS.stream().map(SystemuserRelDO::getBuyerOrgId).collect(Collectors.toList());
            reqVO.setOrgIds(orgIds);
        }
        PageResult<OrganizationDO> pageResult = organizationMapper.selectPage(reqVO);

        return BeanUtils.toBean(pageResult, OrgRespVo.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String openOrg(String orgId) {
        Long aLong = systemuserRelMapper.selectCount(SystemuserRelDO::getBuyerOrgId, orgId);
        if (aLong > 0) {
            throw exception(ErrorCodeConstants.DIY_ERROR, "该单位已关联租户，不可再次开通");
        }
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("client_id", clientId);
        bodyParam.put("client_secret", clientSecret);
        String tokenObj = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
        JSONObject jsonObject = JSONObject.parseObject(tokenObj);
        if (jsonObject.get("error") != null) {
            throw exception(ErrorCodeConstants.DIY_ERROR, jsonObject.getString("error_description"));
        }
        String token = jsonObject.getString("access_token");


        OrganizationDO organizationDO = organizationMapper.selectById(orgId);
        TenantCreateReqVO tenantCreateReqVO = new TenantCreateReqVO();
        //设置租户名称为单位名称
        tenantCreateReqVO.setName(organizationDO.getName());
        //租户套餐先默认为测试套餐
        tenantCreateReqVO.setPackageId(126L);
        tenantCreateReqVO.setUsername(organizationDO.getCode());
        tenantCreateReqVO.setPassword("12345678");
        // TODO 有效期暂时设置的5年内有效
        tenantCreateReqVO.setExpireTime(LocalDateTime.now().plusYears(5));
        // TODO 用户数量暂时设置1000
        tenantCreateReqVO.setAccountCount(1000);
        tenantCreateReqVO.setOrgId(Arrays.asList(orgId));
        tenantCreateReqVO.setStatus(0);
        tenantCreateReqVO.setContactName(organizationDO.getName());
        tenantCreateReqVO.setContactMobile(ObjectUtil.isNotEmpty(organizationDO.getLinkPhone()) ? organizationDO.getLinkPhone() : organizationDO.getCode());
        Map map = tenantService.createTenantForOrg(tenantCreateReqVO);

        Long tenantId  = (Long) map.get("tenantId"); //租户id
        // 创建公司
        Long companyId = createCompany(organizationDO, tenantId);

        // 创建部门
        Long deptId = createDept(organizationDO, tenantId, companyId);

        // 更新租户管理员的单位和公司

        // 创建角色 分配角色菜单
        TenantPackageDO userpackage = tenantPackageService.validTenantPackage(tenantCreateReqVO.getPackageId());
        Long roleId = createRole(userpackage, tenantId, companyId);
        // 创建用户，并分配角色
        AdminUserDO user = createUser(token, roleId, orgId, tenantId, deptId, companyId);
        Long userId = user.getId();

        // 创建租户相关用户角色信息
        TenantUtils.execute(tenantId, () -> {
            TenantPackageDO tenantPackage  = (TenantPackageDO) map.get("tenantPackage"); // 租户管理员用户id
            tenantCreateReqVO.setUsername("tenant"+tenantId);
            tenantCreateReqVO.setName("租户管理员");
            // 创建角色 分配角色菜单
            Long tentRoleId = createRole(tenantPackage);
            // 创建用户，并分配角色
            Long tenantUserId = createUser(tentRoleId, tenantCreateReqVO, deptId, companyId);
            // 修改租户的管理员
            tenantMapper.updateById(new TenantDO().setId(tenantId).setContactUserId(tenantUserId));

            // 添加工作流相关
            addWorkFlow(tenantId, Arrays.asList(tenantUserId, userId));
        });


        String userName = StringUtils.isEmpty(user.getUsername())? tenantCreateReqVO.getUsername() : user.getUsername();
        String mobile = StringUtils.isEmpty(user.getUsername())? tenantCreateReqVO.getContactMobile() : user.getMobile();
        // 更新租户的联系人
        tenantMapper.updateById(new TenantDO().setId(tenantId).setContactUserId(userId).setContactName(userName).setContactMobile(mobile));

        //新增/修改ecms_systemuser_rel
        List<SystemuserRelDO> systemuserRelDOS = systemuserRelMapper.selectList(SystemuserRelDO::getBuyerOrgId, orgId, SystemuserRelDO::getCurrentUserId, userId);
        if (CollectionUtil.isEmpty(systemuserRelDOS)) {
            SystemuserRelDO systemuserRelDO = SystemuserRelDO.builder().buyerOrgId(orgId).currentUserId(userId).deptId(deptId).currentTenantId(tenantId).companyId(companyId).build();
            systemuserRelMapper.insert(systemuserRelDO);
        } else {
            systemuserRelDOS.get(0).setDeptId(deptId).setCurrentTenantId(tenantId).setCompanyId(companyId);
            systemuserRelMapper.updateById(systemuserRelDOS.get(0));
        }
        // 新增系统参数
        addSystemConfigForOrg(orgId, tenantId, deptId);
        // 添加合同类型
        List<ContractTypeDTO> contractTypes = contractTypeApi.createDefaultContractType(tenantId);
        Map<String, String> contractTypeMap = contractTypes.stream().collect(Collectors.toMap(ContractTypeDTO::getPlatId, ContractTypeDTO::getId));

        // 往黑龙江同步单位信息
        sendOrgToHLJ(organizationDO, tenantId, token);
        // 同步黑龙江政采模板
        List<ModelDTO> models = contractProcessApi.getModelListByOrgId(token, orgId);
        List<Integer> categoryIds = models.stream().map(ModelDTO::getCategoryId).distinct().collect(Collectors.toList());
        List<ModelCategoryDTO> modelCategoey = modelCategoryApi.getModelCategoey(categoryIds);
        Map<Integer, String> categoryIdAndCodeMap = modelCategoey.stream().collect(Collectors.toMap(ModelCategoryDTO::getId, ModelCategoryDTO::getCode));
        // 替换模板的contractType
        models.forEach(item-> {
            item.setPlatform("all");
            // 查询
            String code = categoryIdAndCodeMap.get(item.getCategoryId());
            PlatformTemplateCategoryEnums instanceByCode = PlatformTemplateCategoryEnums.getInstanceByCode(code);
            if (ObjectUtil.isNotEmpty(instanceByCode)){
                String key = instanceByCode.getKey();
                String paltId = PurCatalogTypeEnums.getKeyByCode(key);
                String contractTypeId = contractTypeMap.get(paltId);
                item.setContractType(contractTypeId);
            }

        });
        modelApi.insertModels(models, tenantId);
        return "开通成功";
    }

    private String getCodeByPrefix(String prefix) {
        LocalDateTime date = LocalDateTime.now();
        String formattedDate = DATE_FORMATTER.format(date);
        // 使用AtomicInteger生成递增序列号
        // 最多生成1千万个唯一序列号
        int sequenceNumber = SEQUENCE_COUNTER.incrementAndGet();
        // 序列号左填充0至7位
        String formattedSequence = String.format("%04d", sequenceNumber);
        return prefix + formattedDate + formattedSequence;
    }

    private void addSystemConfigForOrg(String orgId, Long tenantId, Long deptId) {

        // 查询默认租户配置项
        TenantUtils.executeIgnore(() -> {
            List<SystemConfigDO> systemConfigDOList = systemConfigMapper.selectList(SystemConfigDO::getTenantId, 1L);
            for (SystemConfigDO systemConfigDO : systemConfigDOList) {
                systemConfigDO.setId(null).setTenantId(tenantId).setCreator("1").setCreateTime(LocalDateTime.now()).setUpdater("1").setUpdateTime(LocalDateTime.now()).setDeleted(false);
                systemConfigDO.setDeptId(deptId);
            }
            systemConfigMapper.insertBatch(systemConfigDOList);
        });
    }

    private void addContractType(Long tenantId) {

    }

    private void addWorkFlow(Long tenantId, List<Long> userId){
        // 查询示例租户253的所有流程模板
        List<ActReModelDTO> actReModels = actReModelApi.getActReModelByTenantId("1");
        List<String> actReModelIds = actReModels.stream().map(ActReModelDTO::getEditorSourceValueId).collect(Collectors.toList());
        // 查询相关所有的流程模型
        List<ActGeByteArrayDTO> actGeByteArrays = actGeByteArrayApi.getActReModelByIds(actReModelIds);
        // 数据替换 替换actGeByteArrays 的modelId 对应替换 actReModels的editorSourceValueId， 再替换editorSourceValueId的id和tenantId
        Map<String, ActReModelDTO> modelMap = actReModels.stream().collect(
                Collectors.toMap(ActReModelDTO::getEditorSourceValueId, Function.identity()));

        List<ActGeByteArrayDTO> addByteArrayList = new ArrayList<>();
        List<ActReModelDTO> addModelList = new ArrayList<>();
        for (ActGeByteArrayDTO actGeByteArray : actGeByteArrays) {
            ActReModelDTO actReModelDTO = modelMap.get(actGeByteArray.getId());
            String uuid = IdUtil.fastUUID();
            actGeByteArray.setId(uuid);
            addByteArrayList.add(actGeByteArray);
            actReModelDTO.setId(IdUtil.fastUUID());
            actReModelDTO.setEditorSourceValueId(uuid);
            actReModelDTO.setTenantId(String.valueOf(tenantId));
            // 设置管理用户
            Map parse = (Map) JSONObject.parse(actReModelDTO.getMetaInfo());
            List managerUserIds = (List) parse.get("managerUserIds");
            managerUserIds.add(userId);
            parse.put("managerUserIds", userId); // 修改工作流管理用户为租户管理员和经办人
            actReModelDTO.setMetaInfo(JSONObject.toJSONString(parse));
            addModelList.add(actReModelDTO);
        }
        // 外键约束，先后顺序，先存储模型，再存储模型数据
        actGeByteArrayApi.insertActGeByteArrays(addByteArrayList);
        actReModelApi.insertActReModels(addModelList);

    }

    /**
     * 递归获取本级及所有下级区划 ID
     */
    private List<Region> getAllRegionIdsByCode(String regionCode) {
        List<Region> regionIds = new ArrayList<>();
        Region region = regionMapper.selectOne(new LambdaQueryWrapperX<Region>().eq(Region::getRegionCode, "230000"));
        if (region != null) {
            regionIds.add(region);
            getChildRegionIds(region.getRegionGuid(), regionIds);
        }
        return regionIds;
    }

    /**
     * 递归查询子区划 ID
     */
    private void getChildRegionIds(String parentId, List<Region> regionIds) {
        regionMapper.selectList(new LambdaQueryWrapperX<Region>().eq(Region::getRegionParentGuid, parentId)).forEach(childId -> {
            regionIds.add(childId);
            getChildRegionIds(childId.getRegionGuid(), regionIds);
        });
    }


    // region [开通单位相关方法]

    private String sendOrgToHLJ(OrganizationDO organizationDO, Long tenantId, String token) {
        ProductOrgDTO productOrgDTO = new ProductOrgDTO();
        productOrgDTO.setOrgId(organizationDO.getId());
        productOrgDTO.setOrgName(organizationDO.getName());
        productOrgDTO.setCompanyId(0);
        productOrgDTO.setTenantId(Math.toIntExact(tenantId));

        String result = contractProcessApi.openProductOrg(token, productOrgDTO);
        return result;
    }

    private Long createCompany(OrganizationDO organizationDO, Long tenantId) {
        String code = getCodeByPrefix("XDF");
        CompanyCreateReqVO companyCreateReqVO = new CompanyCreateReqVO();
        companyCreateReqVO.setUsername(code);
        companyCreateReqVO.setNickname(code);
        companyCreateReqVO.setPassword("12345678");
        companyCreateReqVO.setName(organizationDO.getName());
        companyCreateReqVO.setCreditCode(organizationDO.getIdCard());
        companyCreateReqVO.setSupplier(0);
        companyCreateReqVO.setSort(1);
        companyCreateReqVO.setPhone(ObjectUtil.isEmpty(organizationDO.getLinkPhone()) ? organizationDO.getCode() : organizationDO.getLinkPhone());
        companyCreateReqVO.setStatus(0);
        companyCreateReqVO.setMajor(2);
        Long relativeCompanyId = companyService.createCompanyWithTenantId(companyCreateReqVO, tenantId);
        return relativeCompanyId;
    }

    private Long createDept(OrganizationDO organizationDO, Long tenantId, Long companyId) {
        DeptCreateReqVO deptCreateReqVO = new DeptCreateReqVO();
        deptCreateReqVO.setName(organizationDO.getName());
        deptCreateReqVO.setStatus(0);
        deptCreateReqVO.setSort(1);
        deptCreateReqVO.setCompanyId(companyId);
        return deptService.createDept(deptCreateReqVO, tenantId);
    }

    private Long createRole(TenantPackageDO tenantPackage, Long tenantId, Long companyId) {
        // 创建角色
        RoleCreateReqVO reqVO = new RoleCreateReqVO();
        reqVO.setName(RoleCodeEnum.COMMON_OPERATOR.getName()).setCompanyId(companyId).setCode(RoleCodeEnum.COMMON_OPERATOR.getCode())
                .setSort(0).setRemark("系统自动生成");
        Long roleId = roleService.createRoleWithTenantId(reqVO, RoleTypeEnum.SYSTEM.getType(), tenantId);
        TenantUtils.executeIgnore(() -> {
            List<RoleMenuDO> roleMenuDOS = roleMenuMapper.selectList(RoleMenuDO::getRoleId, 3L, TenantBaseDO::getTenantId, 1);
            Set<Long> menuIds = roleMenuDOS.stream().map(RoleMenuDO::getMenuId).collect(Collectors.toSet());
            permissionService.assignRoleMenuWithTenantId(roleId, menuIds, tenantId);
        });
        // 分配权限
        return roleId;
    }
    private Long createRole(TenantPackageDO tenantPackage) {
        // 创建角色
        RoleCreateReqVO reqVO = new RoleCreateReqVO();
        reqVO.setName(RoleCodeEnum.TENANT_ADMIN.getName()).setCode(RoleCodeEnum.TENANT_ADMIN.getCode())
                .setSort(0).setRemark("系统自动生成");
        Long roleId = roleService.createRole(reqVO, RoleTypeEnum.SYSTEM.getType());
        // 分配权限
        permissionService.assignRoleMenu(roleId, tenantPackage.getMenuIds());
        return roleId;
    }

    private AdminUserDO createUser(String token, Long roleId, String orgId, Long tenantId, Long deptId, Long companyId) {

        // 从根据单位id,从黑龙江获取一个启用的用户
        UserDTO userByOrgId = contractProcessApi.getUserByOrgId(token, orgId);
        if (userByOrgId == null || ObjectUtil.isEmpty(userByOrgId.getId())){
            throw exception(ErrorCodeConstants.DIY_ERROR, "该单位政采端没有可用用户！");
        }
        AdminUserDO user = BeanUtils.toBean(userByOrgId, AdminUserDO.class);
        // 校验账户配合
        tenantService.handleTenantInfo(tenant -> {
            long count = userMapper.selectCount();
            if (count >= tenant.getAccountCount()) {
                throw exception(USER_COUNT_MAX, tenant.getAccountCount());
            }
        });
        // 校验正确性
        adminUserService.validateUserForCreateOrUpdateForOrg(null, user.getUsername(), user.getMobile(), user.getEmail(),
                user.getDeptId(), user.getPostIds());
        // 插入用户
        user.setStatus(CommonStatusEnum.ENABLE.getStatus()); // 默认开启
        user.setPassword(passwordEncoder.encode("12345678")); // 加密密码
        user.setTenantId(tenantId);
        user.setDeptId(deptId);
        user.setCompanyId(companyId);
        user.setNickname(user.getNickname()==null? "经办人" : user.getNickname());
//        user.setCompanyId(reqVO.getCompanyId() == null ? getLoginUser().getCompanyId() : reqVO.getCompanyId());

        //根据公司是否供应商，初始化用户的type
        CompanyDO companyDO = companyMapper.getByDeptId(user.getDeptId());
        if (ObjectUtil.isNotNull(companyDO)) {
            if (0 == companyDO.getSupplier()) {
                user.setType(UserTypeEnums.SUPPLIER.getCode());
            } else {
                user.setType(UserTypeEnums.PURCHASER_ORGANIZATION.getCode());
            }
        }

        userMapper.insert(user);
        // 插入关联岗位
        if (CollectionUtil.isNotEmpty(user.getPostIds())) {
            userPostMapper.insertBatch(convertList(user.getPostIds(),
                    postId -> new UserPostDO().setUserId(user.getId()).setPostId(postId)));
        }

        // 分配角色
        permissionService.assignUserRoleWithTenantId(user.getId(), singleton(roleId), tenantId);

        return user;
    }
    private Long createUser(Long roleId, TenantCreateReqVO createReqVO, Long deptId, Long companyId) {
        // 校验账户配合
        tenantService.handleTenantInfo(tenant -> {
            long count = userMapper.selectCount();
            if (count >= tenant.getAccountCount()) {
                throw exception(USER_COUNT_MAX, tenant.getAccountCount());
            }
        });
        // 插入用户
        AdminUserDO user = UserConvert.INSTANCE.convert(TenantConvert.INSTANCE.convert02(createReqVO));
        user.setStatus(CommonStatusEnum.ENABLE.getStatus()); // 默认开启
        user.setPassword(passwordEncoder.encode("12345678")); // 加密密码
        user.setDeptId(deptId);
        user.setCompanyId(companyId);

        //根据公司是否供应商，初始化用户的type
        CompanyDO companyDO = companyMapper.getByDeptId(deptId);
        if(ObjectUtil.isNotNull(companyDO)){
            if(0==companyDO.getSupplier()){
                user.setType(UserTypeEnums.SUPPLIER.getCode());
            }else{
                user.setType(UserTypeEnums.PURCHASER_ORGANIZATION.getCode());
            }
        }
        userMapper.insert(user);
        // 插入关联岗位
        if (CollectionUtil.isNotEmpty(user.getPostIds())) {
            userPostMapper.insertBatch(convertList(user.getPostIds(),
                    postId -> new UserPostDO().setUserId(user.getId()).setPostId(postId)));
        }
        // 分配角色
        permissionService.assignUserRole(user.getId(), singleton(roleId));
        return user.getId();
    }
    // endregion

}
