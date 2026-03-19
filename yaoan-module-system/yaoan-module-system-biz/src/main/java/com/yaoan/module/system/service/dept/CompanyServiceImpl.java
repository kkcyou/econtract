package com.yaoan.module.system.service.dept;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.number.NumberUtils;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.api.relative.RelativeApi;
import com.yaoan.module.econtract.api.relative.dto.RelativeCompanyDTO;
import com.yaoan.module.econtract.api.relative.dto.RelativeContactDTO;
import com.yaoan.module.econtract.api.relative.dto.RelativeDTO;
import com.yaoan.module.econtract.enums.EntityTypeEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.saas.CompanyAuthEnums;
import com.yaoan.module.econtract.enums.saas.RealNameEnums;
import com.yaoan.module.system.api.auth.AuthApi;
import com.yaoan.module.system.controller.admin.dept.vo.company.*;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.saas.CompanySimpleSaveReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.saas.SaasCompanyRespVO;
import com.yaoan.module.system.convert.dept.CompanyConvert;
import com.yaoan.module.system.dal.dataobject.dept.CompanyDO;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.dal.mysql.dept.CompanyMapper;
import com.yaoan.module.system.dal.mysql.dept.DeptMapper;
import com.yaoan.module.system.dal.mysql.permission.RoleMapper;
import com.yaoan.module.system.dal.mysql.user.AdminUserMapper;
import com.yaoan.module.system.enums.ErrorCodeConstants;
import com.yaoan.module.system.enums.dept.MajorEnum;
import com.yaoan.module.system.enums.permission.RoleCodeEnum;
import com.yaoan.module.system.framework.core.event.CompanyInitializeEvent;
import com.yaoan.module.system.framework.core.event.CompanyInitializeEventPublisher;
import com.yaoan.module.system.framework.core.event.SupplyCompanyInitializeEvent;
import com.yaoan.module.system.framework.core.event.SupplyCompanyInitializeEventPublisher;
import com.yaoan.module.system.service.auth.AdminAuthService;
import com.yaoan.module.system.service.permission.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DATA_ERROR;
import static com.yaoan.module.infra.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.system.enums.CommonConstants.*;
import static com.yaoan.module.system.enums.ErrorCodeConstants.*;

/**
 * 单位 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private CompanyInitializeEventPublisher companyInitializeEventPublisher;
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private SupplyCompanyInitializeEventPublisher supplyCompanyInitializeEventPublisher;
    @Resource
    private AuthApi authApi;
    @Autowired
    private PermissionService permissionService;
    @Resource
    private DeptService deptService;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private RoleMapper roleMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createCompany(CompanyCreateReqVO reqVO) {
        // 校验正确性
        validateCompanyForCreateOrUpdate(null, reqVO.getName(), reqVO.getCreditCode());
        CompanyDO company = CompanyConvert.INSTANCE.convert(reqVO);
        //添加租户id
        company.setTenantId(getLoginUser().getTenantId());
        companyMapper.insert(company);
        //发送创建事件
        CompanyInitializeEvent convert = CompanyConvert.INSTANCE.convert(this, company, reqVO.getUsername(), reqVO.getPassword(), reqVO.getIdCard(), reqVO.getNickname(), reqVO.getEmail());
        companyInitializeEventPublisher.sendCompanyInitializeEvent(convert);
        //新增顶级部门
//        DeptDO deptDO = CompanyConvert.INSTANCE.toDeptDO(company);
//        deptMapper.insert(deptDO.setParentId(0L).setCompanyId(company.getId()));
        return company.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createSupplyCompany(CompanyCreateReqVO reqVO) {
        // 校验正确性
        validateCompanyForCreateOrUpdate(null, reqVO.getName(), reqVO.getCreditCode());
        CompanyDO company = CompanyConvert.INSTANCE.convert(reqVO);
        //添加租户id
        company.setTenantId(getLoginUser().getTenantId());
        companyMapper.insert(company);
        //发送创建事件
        SupplyCompanyInitializeEvent convert = CompanyConvert.INSTANCE.convertSupply(this, company, reqVO.getUsername(), reqVO.getPassword(), reqVO.getIdCard(), reqVO.getNickname(), reqVO.getEmail());
        supplyCompanyInitializeEventPublisher.sendCompanyInitializeEvent(convert.setRelativeId(reqVO.getRelativeId()).setCompanyId(company.getId()));
        //新增顶级部门
//        DeptDO deptDO = CompanyConvert.INSTANCE.toDeptDO(company);
//        deptMapper.insert(deptDO.setParentId(0L).setCompanyId(company.getId()));
        return company.getId();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createCompanyWithTenantId(CompanyCreateReqVO reqVO, Long tenantId) {
        // 校验正确性
        validateCompanyForCreateOrUpdate(null, reqVO.getName(), reqVO.getCreditCode());
        CompanyDO company = CompanyConvert.INSTANCE.convert(reqVO);
        //添加租户id
        company.setTenantId(tenantId);
        companyMapper.insert(company);
        //发送创建事件
        CompanyInitializeEvent convert = CompanyConvert.INSTANCE.convert(this, company, reqVO.getUsername(), reqVO.getPassword(), reqVO.getIdCard(), reqVO.getNickname(), reqVO.getEmail());
        // 暂时不创建公司管理员了
//        companyInitializeEventPublisher.sendCompanyInitializeEvent(convert);
        //新增顶级部门
//        DeptDO deptDO = CompanyConvert.INSTANCE.toDeptDO(company);
//        deptMapper.insert(deptDO.setParentId(0L).setCompanyId(company.getId()));
        return company.getId();
    }

    private void validateCompanyForCreateOrUpdate(Long id, String name, String code) {
        // 校验自己存在
        validateCompanyExists(id);
        // 校验单位名的唯一性
        validateCompanyNameUnique(id, name);
        // 校验单位信用编码的唯一性
        validateCompanyCodeUnique(id, code);
    }

    private void validateCompanyCodeUnique(Long id, String code) {
        CompanyDO company = companyMapper.selectByCode(code);
        if (company == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw exception(COMPANY_CODE_DUPLICATE);
        }
        if (!company.getId().equals(id)) {
            throw exception(COMPANY_CODE_DUPLICATE);
        }
    }

    private void validateCompanyNameUnique(Long id, String name) {
        CompanyDO company = companyMapper.selectByName(name);
        if (company == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw exception(COMPANY_NAME_DUPLICATE);
        }
        if (!company.getId().equals(id)) {
            throw exception(COMPANY_NAME_DUPLICATE);
        }
    }

    private void validateCompanyExists(Long id) {
        if (id == null) {
            return;
        }
        if (companyMapper.selectById(id) == null) {
            throw exception(COMPANY_NOT_FOUND);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCompany(CompanyUpdateReqVO reqVO) {
        CompanyDO companyDO = companyMapper.selectById(reqVO.getId());
        if (ObjectUtil.isEmpty(companyDO)) {
            throw exception(ErrorCodeConstants.COMPANY_NOT_FOUND);
        }
        validateCompanyForCreateOrUpdate(reqVO.getId(), reqVO.getName(), reqVO.getCreditCode());
        // 更新单位
        CompanyDO updateObj = CompanyConvert.INSTANCE.convert(reqVO);
        companyMapper.updateById(updateObj);
        if (EntityTypeEnums.INDIVIDUAL.getCode().equals(String.valueOf(updateObj.getMajor()))) {
            adminUserMapper.updateById(new AdminUserDO()
                    .setId(companyDO.getLeaderUserId())
                    .setNickname(reqVO.getNickname())
                    .setIdCard(reqVO.getIdCard()));
        }
        //更新顶级部门
//        DeptDO deptDO = CompanyConvert.INSTANCE.toDeptDO(updateObj);
//        Long deptId = companyMapper.selectById(reqVO.getId()).getDeptId();
//        deptMapper.updateById(deptDO.setId(deptId));
    }

    @Override
    public void deleteCompany(Long id) {
        // 校验是否存在
        validateCompanyExists(id);
        // 删除单位
        companyMapper.deleteById(id);
    }


    @Override
    public CompanyRespVO getCompany(Long id) {
        CompanyDO companyDO = companyMapper.selectById(id);
        if (BeanUtil.isEmpty(companyDO)) {
//            companyDO = companyMapper.selectOne(new LambdaQueryWrapperX<CompanyDO>().eqIfPresent(CompanyDO::getDeptId,id));
        }
        CompanyRespVO convert = CompanyConvert.INSTANCE.convert(companyDO);
        //个人添加身份证号
        if (EntityTypeEnums.INDIVIDUAL.getCode().equals(String.valueOf(companyDO.getMajor()))) {
            DataPermissionUtils.executeIgnore(() -> {
                AdminUserDO adminUserDO = adminUserMapper.selectById(companyDO.getLeaderUserId());
                if (adminUserDO != null) {
                    convert.setIdCard(adminUserDO.getIdCard());
                    convert.setNickname(adminUserDO.getNickname());
                }
            });

        }
        return convert;
    }

    @Override
    public List<CompanyDO> getCompanyList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return companyMapper.selectBatchIds(ids);
    }

    @Override
    public List<CompanyDO> getCompanyList(CompanyListReqVO reqVO) {
        return companyMapper.selectList(reqVO);
    }

    @Override
    public Long getCompanyIdByName(String name) {
        CompanyDO companyDO = companyMapper.selectByName(name);
        if (BeanUtil.isEmpty(companyDO)) {
            throw exception(COMPANY_NOT_FOUND);
        }
        return companyDO.getId();
    }

    @Override
    public void checkCreditCode(String creditCode) {
        CompanyDO companyDO = companyMapper.selectOne(CompanyDO::getCreditCode, creditCode);
        //未激活

        if (ObjectUtil.isNotNull(companyDO)) {
            AdminUserDO adminUserDO = adminUserMapper.selectById(companyDO.getLeaderUserId());
            String managerName = "";
            if (ObjectUtil.isNotEmpty(adminUserDO)) {
                managerName = adminUserDO.getNickname();
            }
            throw exception(DIY_ERROR, "企业已存在，可联系管理员（" + managerName + "）进行操作");

        }
    }

    @Resource
    private RelativeApi relativeApi;
    @Resource
    private AdminAuthService adminAuthService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(CompanySimpleSaveReqVO reqVO) {
        AdminUserDO loginUser = new AdminUserDO();
        loginUser = adminUserMapper.selectById(reqVO.getUserId());
        CompanyDO companyDO = CompanyConvert.INSTANCE.simpleReq2Do(reqVO);
        companyDO.setTenantId(SAAS_TENANT_ID);
        companyDO.setSupplier(0);
        companyDO.setSort(1);
        companyDO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        companyDO.setLeaderUserId(loginUser.getId());
        companyDO.setMajor(MajorEnum.COMPANY.getMajor());
        companyMapper.insert(companyDO);

        Long deptId = createDept4Company(companyDO);

        String relativeId = "";
        //app逻辑：信用代码查，相对方是否已存在，如果已存在，则该相对方初始化数据
        RelativeDTO relativeDTO = relativeApi.getOneByCreditCode(reqVO.getCreditCode());
        boolean isExist = false;
        if (ObjectUtil.isEmpty(relativeDTO)) {
            // initialize the info of counterpart
            relativeDTO = new RelativeDTO();
            relativeDTO.setVirtualId(NumberUtils.generate());
            relativeDTO.setRelativeType(1);
            relativeDTO.setEntityType(EntityTypeEnums.COMPANY.getCode());
            relativeDTO.setCompanyId(companyDO.getId());
            relativeDTO.setCode(IdUtil.fastSimpleUUID().substring(0, 6));
            relativeDTO.setName(reqVO.getName());
            relativeDTO.setContactName(loginUser.getNickname());
            relativeDTO.setContactTel(loginUser.getMobile());
            relativeDTO.setRelativeCompanyId(companyDO.getId());
            relativeDTO.setActive(IfNumEnums.YES.getCode());
            relativeDTO.setContactId(loginUser.getId());
            relativeDTO.setCompanyId(companyDO.getId());
            relativeDTO.setCardNo(reqVO.getCreditCode());
            relativeDTO.setTenantId(SAAS_TENANT_ID);
            relativeId = relativeApi.saveRelative(relativeDTO);
        } else {
            relativeDTO.setRelativeType(1);
            relativeDTO.setEntityType(EntityTypeEnums.COMPANY.getCode());
            relativeDTO.setCompanyId(companyDO.getId());
            relativeDTO.setCode(IdUtil.fastSimpleUUID().substring(0, 6));
            relativeDTO.setName(reqVO.getName());
            relativeDTO.setContactName(loginUser.getNickname());
            relativeDTO.setContactTel(loginUser.getMobile());
            relativeDTO.setRelativeCompanyId(companyDO.getId());
            relativeDTO.setActive(IfNumEnums.YES.getCode());
            relativeDTO.setContactId(loginUser.getId());
            relativeDTO.setCompanyId(companyDO.getId());
            relativeDTO.setCardNo(reqVO.getCreditCode());
            relativeDTO.setTenantId(SAAS_TENANT_ID);
            relativeId = relativeDTO.getId();
            relativeApi.updateRelative(relativeDTO);
        }

        // synchronize relativeId to company
        CompanyDO companyDO1 = new CompanyDO();
        companyDO1.setId(companyDO.getId());
        companyDO1.setRelativeId(relativeId);
        companyMapper.updateById(companyDO1);

        // synchronize user info
        RelativeContactDTO contactDTO = new RelativeContactDTO();
        contactDTO.setCompanyId(companyDO.getId());
        contactDTO.setUserId(loginUser.getId());
        contactDTO.setRelativeId(relativeId);
        contactDTO.setDeptId(deptId);
        List<RelativeContactDTO> contactDTOList = new ArrayList<>();
        contactDTOList.add(contactDTO);
        relativeApi.saveRelativeContacts4Saas(contactDTOList);

        // assign role to user
        Set<Long> roleIds = new HashSet<>();
        List<RoleDO> roleDOS = roleMapper.selectList(RoleDO::getCode, RoleCodeEnum.SAAS_ADMIN.getCode());
        if(CollectionUtil.isEmpty(roleDOS)) {
            log.error("角色缺失！");
            throw exception(DATA_ERROR);
        }
        roleIds.add(roleDOS.get(0).getId());
        permissionService.assignUserRole4Saas(loginUser.getId(),roleIds,relativeId);

        return companyDO.getId();
    }
    private Long createDept4Company(CompanyDO company) {
        DeptCreateReqVO deptCreateReqVO = new DeptCreateReqVO();
        deptCreateReqVO
                .setName(company.getName())
                .setPhone(company.getPhone())
                .setSort(1)
                .setEmail(company.getEmail())
                .setStatus(CommonStatusEnum.ENABLE.getStatus())
                .setCompanyId(company.getId())
        ;
        return deptService.createDept(deptCreateReqVO, company.getTenantId());
    }


    @Override
    public String acheRelativeId(String relativeId) {
        String acheKey = SecurityFrameworkUtils.getLoginUserKey4Space();
        boolean rs = redisUtils.set(acheKey, relativeId);
        log.info(acheKey + "的Redis值为：" + redisUtils.get(acheKey));
        if (rs) {
            return acheKey;
        } else {
            throw exception(DIY_ERROR, "相对方信息缓存同步异常,请联系管理员。");
        }
    }

    @Override
    public List<SaasCompanyRespVO> list4login() {
        List<RelativeCompanyDTO> relativeCompanyDTOS = relativeApi.getCompanyIds4login();
        // if only one, is the individual space
        if (1 == relativeCompanyDTOS.size()) {
            LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
            List<SaasCompanyRespVO> respVOList = new ArrayList<>();
            SaasCompanyRespVO relativeRespVO = new SaasCompanyRespVO();
            relativeRespVO.setRelativeId(relativeCompanyDTOS.get(0).getRelativeId());
            relativeRespVO.setCompanyName(loginUser.getNickName()+" 的 "+INDIVIDUAL_SPACE);
            relativeRespVO.setRealNameStr(RealNameEnums.TODO.getInfo());
            relativeRespVO.setCurrentFlag(IfNumEnums.YES.getCode());
            respVOList.add(relativeRespVO);
            return respVOList;
        }
        List<Long> companyIds = relativeCompanyDTOS.stream().map(RelativeCompanyDTO::getCompanyId).collect(Collectors.toList());
        if (CollUtil.isEmpty(relativeCompanyDTOS)) {
            return Collections.emptyList();
        }
        List<CompanyDO> companyDOList = companyMapper.selectList(CompanyDO::getId, companyIds);
        if (CollectionUtil.isEmpty(companyDOList)) {
            return Collections.emptyList();
        }
        Map<Long, RelativeCompanyDTO> Rel4ComMap = CollectionUtils.convertMap(relativeCompanyDTOS, RelativeCompanyDTO::getCompanyId);

        return enhanceRelCom(companyDOList, Rel4ComMap);
    }

    private List<SaasCompanyRespVO> enhanceRelCom(List<CompanyDO> companyRespVOList, Map<Long, RelativeCompanyDTO> relativeCompanyDTOS) {
        // individual space
        RelativeDTO relativeDTO = relativeApi.getIndividualRelativeUserId(SecurityFrameworkUtils.getLoginUserId());
        if (Objects.isNull(relativeDTO)) {
            throw exception(DATA_ERROR);
        }

        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        List<SaasCompanyRespVO> respVOList = new ArrayList<>();
        SaasCompanyRespVO relativeRespVO = new SaasCompanyRespVO();
        relativeRespVO.setRelativeId(relativeDTO.getId());
        relativeRespVO.setCompanyName(loginUser.getNickName()+" 的 "+INDIVIDUAL_SPACE);
        relativeRespVO.setRealNameStr(RealNameEnums.TODO.getInfo());

        String relativeId = redisUtils.get(SecurityFrameworkUtils.getLoginUserKey4Space());
        // 如果没有缓存空间id,是初次登录 则默认当前是个人空间
        if(Objects.isNull(relativeId)){
            relativeRespVO.setCurrentFlag(IfNumEnums.YES.getCode());
            relativeId = relativeDTO.getId();
            redisUtils.set(SecurityFrameworkUtils.getLoginUserKey4Space(), relativeId);
        }else {
            // exist and is equal to the individual space
            if(relativeId.equals(relativeDTO.getId())){
                relativeRespVO.setCurrentFlag(IfNumEnums.YES.getCode());
            }else {
                // exist and not equal to the individual space
                relativeRespVO.setCurrentFlag(IfNumEnums.NO.getCode());
            }
        }
        respVOList.add(relativeRespVO);

        for (CompanyDO companyDO : companyRespVOList) {
            SaasCompanyRespVO relRespVO = new SaasCompanyRespVO();
            relRespVO.setCompanyId(companyDO.getId());
            relRespVO.setCompanyName(companyDO.getName());
            if (ObjectUtil.isNotNull(companyDO)) {
                RelativeCompanyDTO relativeCompanyDTO = relativeCompanyDTOS.get(companyDO.getId());
                if (ObjectUtil.isNotNull(relativeCompanyDTO)) {
                    relRespVO.setRelativeId(relativeCompanyDTO.getRelativeId());
                    CompanyAuthEnums enums = CompanyAuthEnums.getInstance(relativeCompanyDTO.getRealName());
                    relRespVO.setRealNameStr(enums.getInfo());
                }
            }
            // determine whether it's the current counterparty
            if (relativeId.equals(relRespVO.getRelativeId())) {
                relRespVO.setCurrentFlag(IfNumEnums.YES.getCode());
            } else {
                relRespVO.setCurrentFlag(IfNumEnums.NO.getCode());
            }
            respVOList.add(relRespVO);
        }
        return respVOList;
    }
}
