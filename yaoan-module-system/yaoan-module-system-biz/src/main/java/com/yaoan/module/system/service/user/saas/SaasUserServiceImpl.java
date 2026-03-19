package com.yaoan.module.system.service.user.saas;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.annotations.VisibleForTesting;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.number.NumberUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.econtract.api.contract.ContractSignatoryApi;
import com.yaoan.module.econtract.api.contract.dto.contract.SignatoryRelDTO;
import com.yaoan.module.econtract.api.relative.RelativeApi;
import com.yaoan.module.econtract.api.relative.dto.RelativeContactDTO;
import com.yaoan.module.econtract.api.relative.dto.RelativeDTO;
import com.yaoan.module.econtract.enums.EntityTypeEnums;
import com.yaoan.module.econtract.enums.WarningRulesNotifyTemplateEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.saas.InviteMethodEnums;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.system.api.dept.CompanyApiImpl;
import com.yaoan.module.system.api.dept.dto.UserCompanyInfoRespDTO;
import com.yaoan.module.system.api.permission.PermissionApi;
import com.yaoan.module.system.controller.admin.user.saas.vo.SaasUserPageRespVO;
import com.yaoan.module.system.controller.admin.user.saas.vo.SaasUserSaveReqVO;
import com.yaoan.module.system.controller.admin.user.saas.vo.SaasUserTransferAdminUserReqVO;
import com.yaoan.module.system.controller.admin.user.vo.user.UserPageReqVO;
import com.yaoan.module.system.convert.user.SaasUserConvert;
import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.dal.dataobject.permission.UserRoleDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.dal.mysql.dept.DeptMapper;
import com.yaoan.module.system.dal.mysql.dept.UserPostMapper;
import com.yaoan.module.system.dal.mysql.permission.RoleMapper;
import com.yaoan.module.system.dal.mysql.permission.UserRoleMapper;
import com.yaoan.module.system.dal.mysql.user.AdminUserMapper;
import com.yaoan.module.system.enums.permission.RoleCodeEnum;
import com.yaoan.module.system.service.dept.DeptService;
import com.yaoan.module.system.service.dept.PostService;
import com.yaoan.module.system.service.notify.NotifySendService;
import com.yaoan.module.system.service.permission.PermissionService;
import com.yaoan.module.system.service.tenant.TenantService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DATA_ERROR;
import static com.yaoan.module.infra.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.system.enums.CommonConstants.SAAS_TENANT_ID;
import static com.yaoan.module.system.enums.ErrorCodeConstants.USER_NOT_EXISTS;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-23 20:08
 */
@Slf4j
@Service
public class SaasUserServiceImpl implements SaasUserService {
  static final String TEMP_VERI_CODE = "123456";

  @Value("${sys.user.init-password:yudaoyuanma}")
  private String userInitPassword;

    @Resource
    private AdminUserMapper userMapper;
    @Resource
    private DeptService deptService;
    @Resource
    private PostService postService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    @Lazy // 延迟，避免循环依赖报错
    private TenantService tenantService;
    @Resource
    private UserPostMapper userPostMapper;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private CompanyApiImpl companyApi;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private PermissionApi permissionApi;
    @Resource
    private ContractSignatoryApi signatoryRelApi;
    @Resource
    private RelativeApi relativeApi;
    @Resource
    private NotifySendService notifySendService;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(SaasUserSaveReqVO reqVO) {
        // 验证用户名是否存在
        Long countUsername = userMapper.selectCount(AdminUserDO::getUsername, reqVO.getUsername());
        if (0L != countUsername) {
            throw exception(DIY_ERROR, "用户账号已经存在");
        }

        // 验证手机号是否已注册
        Long count = userMapper.selectCount(AdminUserDO::getMobile, reqVO.getMobile());
        if (0L != count) {
            throw exception(DIY_ERROR, "该手机号已被注册，请重新输入或直接登录");
        }
        String relativeId = "";
        AdminUserDO user = SaasUserConvert.INSTANCE.convert(reqVO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setNickname(reqVO.getUsername());
        user.setRealName(IfNumEnums.YES.getCode());
        user.setInviteMethod(InviteMethodEnums.STAFF_SELF.getCode());
        user.setTenantId(SAAS_TENANT_ID);
        userMapper.insert(user);
        Long userId = user.getId();
        //是否存在未激活的个人空间
        RelativeDTO relativeDTO2 = relativeApi.getOneByMoble4Individual(reqVO.getMobile());
        if (ObjectUtil.isNotNull(relativeDTO2)) {
            relativeId = relativeDTO2.getId();
            //激活相对方
            relativeDTO2.setName(reqVO.getUsername());
            relativeDTO2.setActive(IfNumEnums.YES.getCode());
            relativeDTO2.setContactId(userId);
            relativeDTO2.setTenantId(SAAS_TENANT_ID);
            relativeApi.updateRelative(relativeDTO2);
            //关联相对方
            RelativeContactDTO contactDTO = new RelativeContactDTO();
            contactDTO.setUserId(user.getId());
            contactDTO.setRelativeId(relativeDTO2.getId());
            contactDTO.setContactTel(reqVO.getMobile());
            contactDTO.setName(reqVO.getUsername());
            contactDTO.setTenantId(SAAS_TENANT_ID);
            relativeApi.saveRelativeContacts(Collections.singletonList(contactDTO));
        } else {
            // 初始化个人空间（个人相对方）
            RelativeDTO relativeDTO = new RelativeDTO();
            relativeDTO.setName(reqVO.getUsername());
            relativeDTO.setEntityType(EntityTypeEnums.INDIVIDUAL.getCode());
            relativeDTO.setActive(IfNumEnums.YES.getCode());
            relativeDTO.setVirtualId(NumberUtils.generate());
            relativeDTO.setContactId(userId);
            relativeDTO.setContactName(reqVO.getUsername());
            relativeDTO.setTenantId(SAAS_TENANT_ID);
            relativeId = relativeApi.saveRelative(relativeDTO);
            RelativeContactDTO contactDTO = new RelativeContactDTO();
            contactDTO.setUserId(user.getId());
            contactDTO.setRelativeId(relativeId);
            contactDTO.setContactTel(reqVO.getMobile());
            contactDTO.setName(reqVO.getUsername());
            contactDTO.setTenantId(SAAS_TENANT_ID);
            relativeApi.saveRelativeContacts(Collections.singletonList(contactDTO));
        }
        //初始化权限(个人空间，只是经办人权限)
        Set<Long> roleIds = new HashSet<>();
        List<RoleDO> roleDOS = roleMapper.selectList(RoleDO::getCode, RoleCodeEnum.SAAS_HANDLER.getCode());
        if(CollectionUtil.isEmpty(roleDOS)) {
            log.error("角色缺失！");
            throw exception(DATA_ERROR);
        }
        roleIds.add(roleDOS.get(0).getId());
        permissionApi.assignUserRole4Saas(user.getId(), roleIds, relativeId);

        return user.getId();
    }
  @Override
  public PageResult <SaasUserPageRespVO> querySaasUserList(UserPageReqVO reqVO) {
   //获取公司ID
    List<RelativeContactDTO> userDTOS = getUserInfo(reqVO.getDeptId());
      if (CollectionUtil.isEmpty(userDTOS)) {
          new PageResult<>(0L);
      }
      List<Long> userIds = userDTOS.stream().map(RelativeContactDTO::getUserId).distinct().collect(Collectors.toList());
      Map<Long, RelativeContactDTO> userMap = CollectionUtils.convertMap(userDTOS, RelativeContactDTO::getUserId);
      //2.根据企业ID，部门ID，用户名，手机号，账号状态查询用户列表
    LambdaQueryWrapperX<AdminUserDO> queryWrapperX = new LambdaQueryWrapperX<AdminUserDO>()
            .likeIfPresent(AdminUserDO::getUsername, reqVO.getUsername())
            .likeIfPresent(AdminUserDO::getMobile, reqVO.getMobile())
            .eqIfPresent(AdminUserDO::getStatus, reqVO.getStatus())
            .in(AdminUserDO::getId, userIds);
    if (StringUtils.isNotBlank(reqVO.getSearchText())) {
        queryWrapperX.and(wrapper -> wrapper
                .like(AdminUserDO::getNickname, reqVO.getSearchText())
                .or().like(AdminUserDO::getUsername, reqVO.getSearchText())
                .or().like(AdminUserDO::getMobile, reqVO.getSearchText()));
    }
    PageResult<AdminUserDO> pageResult = userMapper.selectPage(reqVO,queryWrapperX);
    if (CollectionUtil.isEmpty(pageResult.getList())) {
      return new PageResult<>(pageResult.getTotal());
    }
    //3.转换成SaasUserListRespVO
    PageResult<SaasUserPageRespVO> saasUserListRespVOS = SaasUserConvert.INSTANCE.toUserPage(pageResult);
      //4.获取部门ids
      List<Long> deptIds = userDTOS.stream().map(RelativeContactDTO::getDeptId).distinct().collect(Collectors.toList());
    //5.设置部门名称
    List<DeptDO> deptDOS = CollectionUtil.isEmpty(deptIds) ? null : deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().in(DeptDO::getId, deptIds).select(DeptDO::getId, DeptDO::getName));
    Map<Long, DeptDO> deptDOMap = CollectionUtil.isEmpty(deptDOS) ? null : CollectionUtils.convertMap(deptDOS, DeptDO::getId);
    for (int i = 0; i < saasUserListRespVOS.getList().size(); i++) {
      SaasUserPageRespVO saasUserPageRespVO = saasUserListRespVOS.getList().get(i);
      //设置序号
      saasUserPageRespVO.setSort(i+1);
      Long deptId = ObjectUtil.isEmpty(userMap)? null :userMap.get(saasUserPageRespVO.getId()).getDeptId();
      //设置部门名称
      saasUserPageRespVO.setDeptName(Objects.isNull(deptDOMap) || Objects.isNull(deptId) || Objects.isNull(deptDOMap.get(deptId)) ? "-" : deptDOMap.get(deptId).getName());
    }
    return saasUserListRespVOS;
  }
  private  List<RelativeContactDTO>  getUserInfo(Long deptId){
    //1.从redis中获取企业的ID
      String acheKey = SecurityFrameworkUtils.getLoginUserKey4Space();
    //获取到相对方ID
    String relativeId = redisUtils.get(acheKey);
    //根据相对方ID获取到公司ID
    List<RelativeContactDTO> relativeUser = relativeApi.getRelativeUserId(relativeId,deptId);
    return relativeUser;
  }
  @Transactional(rollbackFor = Exception.class)
  @Override
  public void deleteUser(Long id) {
    // 1.校验用户存在,用户不存在或者为自己不可删除
    validateUserExists(id);
    //2.判断改用户下是否有合同，有合同需要转交合同后才能删除用户--查询合同签订表
    validateUserExistsContract(id,"del");
    // 3.删除用户
    userMapper.deleteById(id);
    // 4.删除用户关联数据
    permissionService.processUserDeleted(id);
    // 5.删除用户岗位
    userPostMapper.deleteByUserId(id);
  }



  @Transactional(rollbackFor = Exception.class)
  @Override
  public void updateUserStatus(Long id, Integer status) {
    // 1.校验用户存在,用户不存在或者为自己不可删除
    validateUserExists(id);
    //2.判断改用户下是否哦有合同，有合同需要转交合同后才能删除用户--查询合同签订表
    if(status.equals(1)){
      //停用
      validateUserExistsContract(id,"stop");
    }
    // 更新状态
    AdminUserDO updateObj = new AdminUserDO();
    updateObj.setId(id);
    updateObj.setStatus(status);
    userMapper.updateById(updateObj);
  }
    @Transactional(rollbackFor = Exception.class)
  @Override
    public void transferAdminUser(Long id) {
        if (id == null) {
            return;
        }
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        if (loginUserId.equals(id)) {
            throw exception(DIY_ERROR,"管理员不能转让管理员角色给自己");
        }
        //1.从redis中获取企业的ID
        String acheKey = SecurityFrameworkUtils.getLoginUserKey4Space();
        //获取到相对方ID
        String relativeId = redisUtils.get(acheKey);
        if(ObjectUtil.isNotEmpty(id)){
            //根据用户ID查询用户合同关联信息
            try {
                //修改公司负责人
                companyApi.updateCompanyLeaderUserId(relativeId,id);
                //查询当前用户和被转交人的用户角色信息
                List<UserRoleDO> userRoleDOS = userRoleMapper.selectList(new LambdaQueryWrapperX<UserRoleDO>().in(UserRoleDO::getUserId, id, SecurityFrameworkUtils.getLoginUserId())
                        .eq(UserRoleDO::getRelativeId, relativeId));
                List<UserRoleDO> adminUser = userRoleDOS.stream().filter(userRoleDO -> userRoleDO.getUserId().equals(SecurityFrameworkUtils.getLoginUserId())).collect(Collectors.toList());
                List<UserRoleDO> transferUser = userRoleDOS.stream().filter(userRoleDO -> userRoleDO.getUserId().equals(id)).collect(Collectors.toList());
                Long adminUserRoleId = adminUser.get(0).getRoleId();
                Long transferUserroleId = transferUser.get(0).getRoleId();
                userRoleDOS.forEach(userRoleDO -> {
                    if (userRoleDO.getUserId().equals( id)) {
                        userRoleDO.setRoleId(adminUserRoleId);
                    }else {
                        userRoleDO.setRoleId(transferUserroleId);
                    }
                });
                //修改用户角色
                userRoleMapper.updateBatch(userRoleDOS);
            } catch (Exception e) {
                throw exception(DIY_ERROR, "转让管理员失败"+e.getMessage());
            }
        }

    }
  @Override
  public void transferContract(SaasUserTransferAdminUserReqVO reqVO) {
    if (ObjectUtil.isEmpty(reqVO.getUserId())||reqVO.getUserId()==0||CollectionUtil.isEmpty(reqVO.getContractIds())) {
      throw exception(DIY_ERROR, "转交合同失败,需转交的合同id或用户ID为空，请检查参数是否正确");
    }
      //获取当前用户ID
      try {
          Long loginUserId = WebFrameworkUtils.getLoginUserId();
          List<SignatoryRelDTO> contractSignatoryRelList = signatoryRelApi.getContractSignatoryRelList(loginUserId.toString(),reqVO.getContractIds());
          if (CollectionUtil.isNotEmpty(contractSignatoryRelList)) {
            contractSignatoryRelList.forEach(signatoryRel -> {
              signatoryRel.setContactId(reqVO.getUserId());
            });
            signatoryRelApi.updateContractSignatory(contractSignatoryRelList);
            //根据用户ID获取用户名称，公司名称
            List<Long> userId = new ArrayList<>();
            userId.add(reqVO.getUserId());
            List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfo(userId);
            //发送转发合同消息通知被转交合同的用户
            Map<String, Object> templateParams = new HashMap<>();
            templateParams.put("companyName",CollectionUtil.isNotEmpty(userCompanyInfoList)?userCompanyInfoList.get(0).getName():null);
            templateParams.put("userName", CollectionUtil.isNotEmpty(userCompanyInfoList)?userCompanyInfoList.get(0).getUserName():null);
            templateParams.put("contractNum",reqVO.getContractIds().size() );
            Long resultMessageId = notifySendService.sendSingleNotifyToAdminV2(reqVO.getUserId(), WarningRulesNotifyTemplateEnums.SAAS_ADMIN_TRANSFER_CONTRACT_REMINDER.getCode(),"合同管理", templateParams);
          }
      } catch (Exception e) {
        throw exception(DIY_ERROR, "转交合同失败"+e.getMessage());
      }


  }
  @VisibleForTesting
  @Override
  public void validateUserExists(Long id) {
    if (id == null) {
      return;
    }
    Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
    if (loginUserId.equals(id)) {
      throw exception(DIY_ERROR,"管理员不能删除/停用自己");
    }
    AdminUserDO user = userMapper.selectById(id);
    if (user == null) {
      throw exception(USER_NOT_EXISTS);
    }
  }

    @Override
  public void validateUserExistsContract(Long id,String  type) {
    //判断改用户下是否哦有合同，有合同需要转交合同后才能删除用户--查询合同签订表
    List<SignatoryRelDTO> contractSignatoryRelList = signatoryRelApi.getContractSignatoryRelList(id.toString(),null);
    if (CollectionUtil.isNotEmpty(contractSignatoryRelList)) {
      Set<String> contractIds = contractSignatoryRelList.stream().map(SignatoryRelDTO::getContractId).collect(Collectors.toSet());
      if("del".equals(type)){
        throw exception(DIY_ERROR, "该成员名下有"+contractIds.size()+"份合同需要转交，请转交后删除");
      }
      if("stop".equals(type)){
        throw exception(DIY_ERROR, "该成员名下有"+contractIds.size()+"份合同需要转交，请转交后停用");
      }
    }
  }
}
