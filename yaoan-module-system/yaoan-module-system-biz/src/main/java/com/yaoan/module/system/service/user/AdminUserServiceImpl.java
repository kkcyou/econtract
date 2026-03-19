package com.yaoan.module.system.service.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.annotations.VisibleForTesting;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.common.exception.ErrorCode;
import com.yaoan.framework.common.exception.ServiceException;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.econtract.api.relative.RelativeApi;
import com.yaoan.module.econtract.api.relative.dto.RelativeContactDTO;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.system.api.dept.CompanyApiImpl;
import com.yaoan.module.system.api.dept.dto.CompanyRespDTO;
import com.yaoan.module.system.api.dept.dto.DeptRespDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyDeptRespDTO;
import com.yaoan.module.system.api.sms.dto.code.SmsCodeValidateReqDTO;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.controller.admin.auth.vo.AuthActiveUserReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptTreeVO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptUserRespVO;
import com.yaoan.module.system.controller.admin.user.vo.profile.UserProfileUpdatePasswordReqVO;
import com.yaoan.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import com.yaoan.module.system.controller.admin.user.vo.role.RoleSimple4UserRespVO;
import com.yaoan.module.system.controller.admin.user.vo.user.*;
import com.yaoan.module.system.convert.dept.CompanyConvert;
import com.yaoan.module.system.convert.dept.DeptConvert;
import com.yaoan.module.system.convert.user.UserConvert;
import com.yaoan.module.system.dal.dataobject.dept.CompanyDO;
import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import com.yaoan.module.system.dal.dataobject.dept.UserPostDO;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.dal.dataobject.permission.UserRoleDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.dal.mysql.dept.CompanyMapper;
import com.yaoan.module.system.dal.mysql.dept.DeptMapper;
import com.yaoan.module.system.dal.mysql.dept.UserPostMapper;
import com.yaoan.module.system.dal.mysql.permission.RoleMapper;
import com.yaoan.module.system.dal.mysql.permission.UserRoleMapper;
import com.yaoan.module.system.dal.mysql.user.AdminUserMapper;
import com.yaoan.module.system.enums.ErrorCodeConstants;
import com.yaoan.module.system.enums.sms.SmsSceneEnum;
import com.yaoan.module.system.enums.user.UserTypeEnums;
import com.yaoan.module.system.service.dept.DeptService;
import com.yaoan.module.system.service.dept.PostService;
import com.yaoan.module.system.service.permission.PermissionService;
import com.yaoan.module.system.service.sms.SmsCodeService;
import com.yaoan.module.system.service.tenant.TenantService;
import com.yaoan.module.system.service.user.saas.SaasUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.common.util.collection.CollectionUtils.convertList;
import static com.yaoan.framework.common.util.collection.CollectionUtils.convertSet;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.system.enums.ErrorCodeConstants.*;

/**
 * 后台用户 Service 实现类
 *
 * @author 芋道源码
 */
@Service("adminUserService")
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {
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
    private CompanyMapper companyMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private CompanyApiImpl companyApi;
    /**
     * 用户初始密码
     */
    @Value("${user-config.init_user_password:123456789}")
    private String initUserPassword;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RelativeApi relativeApi;
    @Resource
    private SaasUserService saasUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserCreateReqVO reqVO) {

        // 校验账户配合
        tenantService.handleTenantInfo(tenant -> {
            long count = userMapper.selectCount();
            if (count >= tenant.getAccountCount()) {
                throw exception(USER_COUNT_MAX, tenant.getAccountCount());
            }
        });
        // 校验正确性
        validateUserForCreateOrUpdate(null, reqVO.getUsername(), reqVO.getMobile(), reqVO.getEmail(),
                reqVO.getDeptId(), reqVO.getPostIds());
        // 插入用户
        AdminUserDO user = UserConvert.INSTANCE.convert(reqVO);
        user.setStatus(CommonStatusEnum.ENABLE.getStatus()); // 默认开启
        user.setPassword(encodePassword(reqVO.getPassword())); // 加密密码
        if (ObjectUtil.isNotEmpty(reqVO.getDeptId())) {
            DeptDO deptDO = deptMapper.selectOne(DeptDO::getId, reqVO.getDeptId());
            user.setCompanyId(deptDO.getCompanyId());
        }
//        user.setCompanyId(reqVO.getCompanyId() == null ? getLoginUser().getCompanyId() : reqVO.getCompanyId());

        //根据公司是否供应商，初始化用户的type
        CompanyDO companyDO = companyMapper.getByDeptId(reqVO.getDeptId());
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
        return user.getId();
    }

    /**
     * 激活账户，更改密码
     */
    @Override
    @DataPermission(enable = false)
    @Transactional(rollbackFor = Exception.class)
    public void activeUser(AuthActiveUserReqVO authVo) {

//        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        //校验验证码
        String userVeriCode = authVo.getCaptchaVerification();
//        String realVerCode = (String) redisTemplate.opsForValue().get(VERIFICATION_CODE);

        //为配合前端调试，临时写死验证码
        String realVerCode = TEMP_VERI_CODE;

        if (!StringUtils.equals(userVeriCode, realVerCode)) {
            throw exception(ErrorCodeConstants.AUTH_LOGIN_VERI_CODE_ERROR);
        }
        String encodeInitUserPassword = encodePassword(initUserPassword);
        String newPassword = encodePassword(authVo.getPassword());
//        AdminUserDO oldUser = userMapper.selectById(loginUserId);
        AdminUserDO oldUser = userMapper.selectOne(new LambdaQueryWrapperX<AdminUserDO>().eq(AdminUserDO::getMobile, authVo.getMobile()));
        String oldMobile = oldUser.getMobile();
        //校验登录手机号
        if (!StringUtils.equals(oldMobile, authVo.getMobile())) {
            throw exception(ErrorCodeConstants.AUTH_LOGIN_SAME_MOBILE);
        }
        //校验新密码与初始密码
        if (isPasswordMatch(initUserPassword, newPassword)) {
            throw exception(ErrorCodeConstants.AUTH_SAME_PASSWORD);
        }

        // 更新用户
        oldUser.setPassword(newPassword);
        userMapper.updateById(oldUser);
        //激活单位状态
        DeptDO deptDO = deptMapper.selectById(oldUser.getDeptId());
        System.out.println(deptDO);

        deptMapper.updateById(deptDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateReqVO reqVO) {
        // 1.校验用户存在,用户不存在或者为自己不可删除
        saasUserService.validateUserExists(reqVO.getId());
        //2.判断改用户下是否哦有合同，有合同需要转交合同后才能删除用户--查询合同签订表
        if(ObjectUtil.isNotEmpty(reqVO.getStatus())&&reqVO.getStatus().equals(1)){
            //停用
            saasUserService.validateUserExistsContract(reqVO.getId(),"stop");
        }
        // 校验正确性
        validateUserForCreateOrUpdate(reqVO.getId(), reqVO.getUsername(), reqVO.getMobile(), reqVO.getEmail(),
                reqVO.getDeptId(), reqVO.getPostIds());
        // 更新用户
        AdminUserDO updateObj = UserConvert.INSTANCE.convert(reqVO);
        userMapper.updateById(updateObj);
        // 更新岗位
        updateUserPost(reqVO, updateObj);
    }

    private void updateUserPost(UserUpdateReqVO reqVO, AdminUserDO updateObj) {
        Long userId = reqVO.getId();
        Set<Long> dbPostIds = convertSet(userPostMapper.selectListByUserId(userId), UserPostDO::getPostId);
        // 计算新增和删除的岗位编号
        Set<Long> postIds = updateObj.getPostIds();
        if(CollectionUtil.isNotEmpty(postIds)&&CollectionUtil.isNotEmpty(dbPostIds)){
            Collection<Long> createPostIds = CollUtil.subtract(postIds, dbPostIds);
            Collection<Long> deletePostIds = CollUtil.subtract(dbPostIds, postIds);
            // 执行新增和删除。对于已经授权的菜单，不用做任何处理
            if (!CollectionUtil.isEmpty(createPostIds)) {
                userPostMapper.insertBatch(convertList(createPostIds,
                        postId -> new UserPostDO().setUserId(userId).setPostId(postId)));
            }
            if (!CollectionUtil.isEmpty(deletePostIds)) {
                userPostMapper.deleteByUserIdAndPostId(userId, deletePostIds);
            }
        }
    }

    @Override
    public void updateUserLogin(Long id, String loginIp) {
        userMapper.updateById(new AdminUserDO().setId(id).setLoginIp(loginIp).setLoginDate(LocalDateTime.now()));
    }

    @Override
    public void updateUserProfile(Long id, UserProfileUpdateReqVO reqVO) {
        // 校验正确性
        validateUserExists(id);
        validateEmailUnique(id, reqVO.getEmail());
        validateMobileUnique(id, reqVO.getMobile());
        // 执行更新
        userMapper.updateById(UserConvert.INSTANCE.convert(reqVO).setId(id));
    }

    @Override
    public void updateUserPassword(Long id, UserProfileUpdatePasswordReqVO reqVO) {
        // 校验旧密码密码
        validateOldPassword(id, reqVO.getOldPassword());
        // 执行更新
        AdminUserDO updateObj = new AdminUserDO().setId(id);
        updateObj.setPassword(encodePassword(reqVO.getNewPassword())); // 加密密码
        userMapper.updateById(updateObj);
    }

    @Override
    public String updateUserAvatar(Long id, InputStream avatarFile) throws Exception {
        validateUserExists(id);
        // 存储文件
        String avatar = fileApi.createFile(IoUtil.readBytes(avatarFile));
        // 更新路径
        AdminUserDO sysUserDO = new AdminUserDO();
        sysUserDO.setId(id);
        sysUserDO.setAvatar(avatar);
        userMapper.updateById(sysUserDO);
        return avatar;
    }

    @Override
    public void updateUserPassword(Long id, String password) {
        // 校验用户存在
        validateUserExists(id);
        // 更新密码
        AdminUserDO updateObj = new AdminUserDO();
        updateObj.setId(id);
        updateObj.setRemark("已修改密码");
        updateObj.setPassword(encodePassword(password)); // 加密密码
        userMapper.updateById(updateObj);
    }

    @Override
    public void changeUserPassword(UserForgetPasswordReqVO changePasswordReqVO) {
        String password = changePasswordReqVO.getPassword();
        String mobile = changePasswordReqVO.getMobile();
        String smsCode = changePasswordReqVO.getSmsCode();
        String accountName = changePasswordReqVO.getAccountName();
        AdminUserDO adminUserDO = userMapper.selectByMobile(mobile);
        AdminUserDO adminUserDO1 = userMapper.selectByUsername(accountName);
        if (ObjectUtil.isNull(adminUserDO) || ObjectUtil.isNull(adminUserDO1) || !adminUserDO.getId().equals(adminUserDO1.getId())) {
            throw exception(DIY_ERROR, "账户名或手机号错误");
        }

        Long id = adminUserDO.getId();
        // 校验短信验证码
        // smsCodeService.validateSmsCode(new SmsCodeValidateReqDTO().setMobile(mobile).setCode(smsCode).setScene(SmsSceneEnum.ADMIN_FORGET_PASSWORD.getScene()));
        // 更新密码
        AdminUserDO updateObj = new AdminUserDO();
        updateObj.setId(id);
        updateObj.setRemark("已修改密码");
        updateObj.setPassword(encodePassword(password)); // 加密密码
        userMapper.updateById(updateObj);
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        // 校验用户存在
        validateUserExists(id);
        // 更新状态
        AdminUserDO updateObj = new AdminUserDO();
        updateObj.setId(id);
        updateObj.setStatus(status);
        userMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        // 校验用户存在
        validateUserExists(id);
        // 删除用户
        userMapper.deleteById(id);
        // 删除用户关联数据
        permissionService.processUserDeleted(id);
        // 删除用户岗位
        userPostMapper.deleteByUserId(id);
    }

    @Override
    public AdminUserDO getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public AdminUserDO getUserByUsernameOrMobile(String username) {
        AdminUserDO adminUserDO = userMapper.selectByUsername(username);
        if (adminUserDO == null) {
            return userMapper.selectByMobile(username);
        }
        return adminUserDO;
    }

    @Override
    public AdminUserDO getUserByAppOpenId(String openId) {
        return userMapper.selectByAppOpenId(openId);
    }

    @Override
    public AdminUserDO getUserByMobile(String mobile) {
        return userMapper.selectByMobile(mobile);
    }

    @Override
    public PageResult<AdminUserDO> getUserPage(UserPageReqVO reqVO) {
        return userMapper.selectPage(reqVO, getDeptCondition(reqVO.getDeptId()));
    }

    @Override
    public AdminUserDO getUser(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<AdminUserDO> getUserListByDeptIds(Collection<Long> deptIds) {
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        return userMapper.selectListByDeptIds(deptIds);
    }

    @Override
    public List<AdminUserDO> getUserListByPostIds(Collection<Long> postIds) {
        if (CollUtil.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        Set<Long> userIds = convertSet(userPostMapper.selectListByPostIds(postIds), UserPostDO::getUserId);
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return userMapper.selectBatchIds(userIds);
    }

    @Override
    public List<AdminUserDO> getUserList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return userMapper.selectBatchIds(ids);
    }

    @Override
    public List<AdminUserDO> getUserList2(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return userMapper.selectListXml(ids);
    }

    @Override
    public void validateUserList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得岗位信息
        List<AdminUserDO> users = userMapper.selectBatchIds(ids);
        Map<Long, AdminUserDO> userMap = CollectionUtils.convertMap(users, AdminUserDO::getId);
        // 校验
        ids.forEach(id -> {
            AdminUserDO user = userMap.get(id);
            if (user == null) {
                throw exception(USER_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(user.getStatus())) {
                throw exception(USER_IS_DISABLE, user.getNickname());
            }
        });
    }

    @Override
    public List<AdminUserDO> getUserList(UserExportReqVO reqVO) {
        return userMapper.selectList(reqVO, getDeptCondition(reqVO.getDeptId()));
    }

    @Override
    public List<AdminUserDO> getUserListByNickname(String nickname) {
        return userMapper.selectListByNickname(nickname);
    }

    /**
     * 获得部门条件：查询指定部门的子部门编号们，包括自身
     *
     * @param deptId 部门编号
     * @return 部门编号集合
     */
    private Set<Long> getDeptCondition(Long deptId) {
        if (deptId == null) {
            return Collections.emptySet();
        }
        Set<Long> deptIds = convertSet(deptService.getChildDeptList(deptId), DeptDO::getId);
        deptIds.add(deptId); // 包括自身
        return deptIds;
    }

    private void validateUserForCreateOrUpdate(Long id, String username, String mobile, String email,
                                               Long deptId, Set<Long> postIds) {
        // 关闭数据权限，避免因为没有数据权限，查询不到数据，进而导致唯一校验不正确
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                // 校验用户存在
                validateUserExists(id);
                // 校验用户名唯一
                validateUsernameUnique(id, username);
                // 校验手机号唯一
                validateMobileUnique(id, mobile);
                // 校验邮箱唯一
                validateEmailUnique(id, email);
                // 校验部门处于开启状态
                deptService.validateDeptList(CollectionUtils.singleton(deptId));
                // 校验岗位处于开启状态
                postService.validatePostList(postIds);
            });
        });
    }

    @Override
    public void validateUserForCreateOrUpdateForOrg(Long id, String username, String mobile, String email,
                                                    Long deptId, Set<Long> postIds) {
        // 关闭数据权限，避免因为没有数据权限，查询不到数据，进而导致唯一校验不正确
        DataPermissionUtils.executeIgnore(() -> {
            // 校验用户存在
            validateUserExists(id);
            // 校验用户名唯一
            validateUsernameUnique(id, username);
            // 校验手机号唯一
            validateMobileUnique(id, mobile);
            // 校验邮箱唯一
            validateEmailUnique(id, email);
            // 校验部门处于开启状态
            deptService.validateDeptList(CollectionUtils.singleton(deptId));
            // 校验岗位处于开启状态
            postService.validatePostList(postIds);
        });
    }

    @VisibleForTesting
    void validateUserExists(Long id) {
        if (id == null) {
            return;
        }
        AdminUserDO user = userMapper.selectById(id);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
    }

    @VisibleForTesting
    void validateUsernameUnique(Long id, String username) {
        if (StrUtil.isBlank(username)) {
            return;
        }
        AdminUserDO user = userMapper.selectByUsername(username);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw exception(USER_USERNAME_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw exception(USER_USERNAME_EXISTS);
        }
    }

    @VisibleForTesting
    void validateEmailUnique(Long id, String email) {
        if (StrUtil.isBlank(email)) {
            return;
        }
        AdminUserDO user = userMapper.selectByEmail(email);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw exception(USER_EMAIL_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw exception(USER_EMAIL_EXISTS);
        }
    }

    @VisibleForTesting
    void validateMobileUnique(Long id, String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return;
        }
        AdminUserDO user = userMapper.selectByMobile(mobile);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw exception(USER_MOBILE_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw exception(USER_MOBILE_EXISTS);
        }
    }

    /**
     * 校验旧密码
     *
     * @param id          用户 id
     * @param oldPassword 旧密码
     */
    @VisibleForTesting
    void validateOldPassword(Long id, String oldPassword) {
        AdminUserDO user = userMapper.selectById(id);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
        if (!isPasswordMatch(oldPassword, user.getPassword())) {
            throw exception(USER_PASSWORD_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 添加事务，异常则回滚所有导入
    public UserImportRespVO importUserList(List<UserImportExcelVO> importUsers, boolean isUpdateSupport) {
        if (CollUtil.isEmpty(importUsers)) {
            throw exception(USER_IMPORT_LIST_IS_EMPTY);
        }
        UserImportRespVO respVO = UserImportRespVO.builder().createUsernames(new ArrayList<>())
                .updateUsernames(new ArrayList<>()).failureUsernames(new LinkedHashMap<>()).build();
        importUsers.forEach(importUser -> {
            // 校验，判断是否有不符合的原因
            try {
                validateUserForCreateOrUpdate(null, null, importUser.getMobile(), importUser.getEmail(),
                        importUser.getDeptId(), null);
            } catch (ServiceException ex) {
                respVO.getFailureUsernames().put(importUser.getUsername(), ex.getMessage());
                return;
            }
            // 判断如果不存在，在进行插入
            AdminUserDO existUser = userMapper.selectByUsername(importUser.getUsername());
            if (existUser == null) {
                userMapper.insert(UserConvert.INSTANCE.convert(importUser)
                        .setPassword(encodePassword(userInitPassword)).setPostIds(new HashSet<>())); // 设置默认密码及空岗位编号数组
                respVO.getCreateUsernames().add(importUser.getUsername());
                return;
            }
            // 如果存在，判断是否允许更新
            if (!isUpdateSupport) {
                respVO.getFailureUsernames().put(importUser.getUsername(), USER_USERNAME_EXISTS.getMsg());
                return;
            }
            AdminUserDO updateUser = UserConvert.INSTANCE.convert(importUser);
            updateUser.setId(existUser.getId());
            userMapper.updateById(updateUser);
            respVO.getUpdateUsernames().add(importUser.getUsername());
        });
        return respVO;
    }

    @Override
    public List<AdminUserDO> getUserListByStatus(Integer status) {
        return userMapper.selectListByStatus(status);
    }

    @Override
    public List<AdminUserDO> getUserListByRoleId(Long roleId) {
        List<UserRoleDO> userRoleDOS = userRoleMapper.selectListByRoleIds(Arrays.asList(roleId));
        List<Long> userIds = userRoleDOS.stream().map(UserRoleDO::getUserId).collect(Collectors.toList());
        return userMapper.selectList(new LambdaQueryWrapperX<AdminUserDO>().eqIfPresent(AdminUserDO::getStatus,CommonStatusEnum.ENABLE.getStatus()).inIfPresent(AdminUserDO::getId, userIds));
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public AdminUserRespDTO selectAdminUser(String account, String name, String idCard) {
        AdminUserDO adminUserDO = userMapper.selectOne(new LambdaQueryWrapperX<AdminUserDO>()
                //平台账号
                .eqIfPresent(AdminUserDO::getUsername, account)
                //姓名
                .eqIfPresent(AdminUserDO::getNickname, name)
                //身份账号
                .eqIfPresent(AdminUserDO::getIdCard, idCard)
                //状态为0正常的
                .eqIfPresent(AdminUserDO::getStatus, 0)
        );
        if (adminUserDO == null) {
            return null;
        }
        return UserConvert.INSTANCE.convert4(adminUserDO);
    }

    @Override
    public UserCompanyDeptRespDTO getCompany(UserCompanyDeptRespDTO userCompanyDeptRespDTO, Long deptId) {
        //1.设置部门信息
        DeptDO deptDO = deptMapper.selectById(userCompanyDeptRespDTO.getDeptId());
        DeptRespDTO deptRespDTO = DeptConvert.INSTANCE.convert03(deptDO);
        userCompanyDeptRespDTO.setDeptInfo(deptRespDTO);
//        if (BeanUtil.isNotEmpty(deptDO) && deptDO.getCompanyId() != null) {
//            CompanyDO companyDO;
//            if (deptDO.getParentId() == 0L) {
        //为公司
//                companyDO = companyMapper.selectOne(CompanyDO::getDeptId, deptDO.getId());
        CompanyDO companyDO = companyMapper.selectById(deptId);
//            } else {
//                Long id = getTopLevelDepartmentId(deptId, 0L);
//                companyDO = companyMapper.selectOne(CompanyDO::getDeptId, id);

//            }
        //3.设置公司信息
        CompanyRespDTO companyRespDTO = CompanyConvert.INSTANCE.convert03(companyDO);
        userCompanyDeptRespDTO.setCompanyInfo(companyRespDTO);
//        }
        return userCompanyDeptRespDTO;
    }

    @Override
    public List<AdminUserRespDTO> getUserList() {
        List<AdminUserDO> adminUserDOS = userMapper.selectList();
        List<AdminUserRespDTO> adminUserRespDTOS = UserConvert.INSTANCE.convertList4(adminUserDOS);
        return adminUserRespDTOS;
    }

    @Override
    public AdminUserRespDTO getUserByPlatformUserId(String platformUserId) {
        List<AdminUserDO> adminUserDOS = userMapper.selectList(new LambdaQueryWrapper<AdminUserDO>().eq(AdminUserDO::getPlatformUserId, platformUserId));
        if (adminUserDOS.size() > 0) {
            List<AdminUserRespDTO> adminUserRespDTOS = UserConvert.INSTANCE.convertList4(adminUserDOS);
            return adminUserRespDTOS.get(0);
        }
        return null;
    }

    @Override
    public List<AdminUserRespDTO> getUserListLikeNickname(String nickname) {
        List<AdminUserDO> dos = userMapper.selectList(new LambdaQueryWrapperX<AdminUserDO>().likeIfPresent(AdminUserDO::getNickname, nickname));
        return UserConvert.INSTANCE.convert2DO(dos);
    }

    @Override
    @DataPermission(enable = false)
    public List<DeptUserRespVO> getDeptAndUserList() {
        List<DeptUserRespVO> result = new ArrayList<>();
        //1.获取用户信息
        AdminUserDO adminUserDO = userMapper.selectById(WebFrameworkUtils.getLoginUserId());
        if (BeanUtil.isNotEmpty(adminUserDO)) {
            //3.求顶级部门id
            List<DeptDO> deptList = deptService.getDeptList(new DeptListReqVO().setStatus(CommonStatusEnum.ENABLE.getStatus()));
            Map<Long, DeptDO> deptIdTreeMap = CollectionUtils.convertMap(deptList, DeptDO::getId);

            Long companyDeptId = companyApi.judgeCompanyDeptId(deptIdTreeMap, adminUserDO.getDeptId());

            //部门集合
            Set<Long> deptIds = deptService.getChildDeptIdListFromCache(companyDeptId);
            deptIds.add(companyDeptId);

            List<DeptDO> companyDeptList = deptList.stream().filter(item -> deptIds.contains(item.getId())).collect(Collectors.toList());

            Map<Long, DeptDO> deptMap = CollectionUtils.convertMap(companyDeptList, DeptDO::getId);

            //根据部门id集合获取用户信息
            List<AdminUserDO> adminUserDOS = userMapper.selectListByDeptIds(companyDeptList.stream().map(DeptDO::getId).collect(Collectors.toList()));

            Map<Long, List<AdminUserDO>> deptUsersMap = adminUserDOS.stream().collect(Collectors.groupingBy(AdminUserDO::getDeptId));
            deptUsersMap.forEach((deptId, users) -> {

                DeptDO deptDO = deptMap.get(deptId);
                if (deptDO != null) {
                    DeptUserRespVO itemResp = new DeptUserRespVO().setId(deptDO.getId()).setName(deptDO.getName()).setParentId(deptDO.getParentId());
                    List<Map<String, Object>> children = new ArrayList<>();
                    for (AdminUserDO user : users) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", user.getId());
                        map.put("name", user.getNickname());
                        children.add(map);
                    }
                    itemResp.setChildren(children);
                    result.add(itemResp);
                }
            });
        }
        return result;
    }

    @Override
    public List<AdminUserRespDTO> selectListByDeptIdsAndUserIds(List<Long> deptIds, Set<Long> userIds) {
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        return UserConvert.INSTANCE.selectListByDeptIdsAndUserIds(userMapper.selectListByDeptIdsAndUserIds(deptIds, userIds));
    }

    /**
     * 对密码进行加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private Long getTopLevelDepartmentId(Long departmentId, Long pId) {
        List<DeptDO> deptDOS = deptMapper.selectList();
        //3.将部门信息转成map
        Map<Long, DeptDO> deptDOMap = CollectionUtils.convertMap(deptDOS, DeptDO::getId);
        log.error("==============deptDOMap:{}", deptDOMap);
        log.error("==============departmentId:" + departmentId);
        Long parentId = deptDOMap.get(departmentId).getParentId();  // 查询部门的父ID
        if (parentId == pId) {
            return departmentId;  // 找到顶级部门ID
        } else {
            return getTopLevelDepartmentId(parentId, pId);  // 继续递归调用
        }
    }

    @Override
    @DataPermission(enable = false)
    public Map<Long, AdminUserRespDTO> getUserRoleInfos(List<Long> userIds) {
        List<AdminUserDO> userDOList = getUserList(userIds);
        List<UserRoleDO> userRoleDOList = new ArrayList<UserRoleDO>();
        List<RoleDO> roleDOList = new ArrayList<RoleDO>();
        Map<Long, UserRoleDO> userRoleDOMap = new HashMap<Long, UserRoleDO>();
        Map<Long, RoleDO> roleDOMap = new HashMap<Long, RoleDO>();
        userRoleDOList = userRoleMapper.selectList(new LambdaQueryWrapperX<UserRoleDO>().inIfPresent(UserRoleDO::getUserId, userIds));
        userDOList = userMapper.selectList(AdminUserDO::getId, userIds);
        if (CollectionUtil.isNotEmpty(userRoleDOList)) {
            userRoleDOMap = CollectionUtils.convertMap(userRoleDOList, UserRoleDO::getUserId);
            List<Long> roleIds = userRoleDOList.stream().map(UserRoleDO::getRoleId).distinct().collect(Collectors.toList());
            roleDOList = roleMapper.selectList(new LambdaQueryWrapperX<RoleDO>().inIfPresent(RoleDO::getId, roleIds));
            if (CollectionUtil.isNotEmpty(roleDOList)) {
                roleDOMap = CollectionUtils.convertMap(roleDOList, RoleDO::getId);
            }
        }
        List<AdminUserRespDTO> userRespDTOList = new ArrayList<AdminUserRespDTO>();
        Map<Long, AdminUserRespDTO> result = new HashMap<Long, AdminUserRespDTO>();
        if (CollectionUtil.isNotEmpty(userDOList)) {
            userRespDTOList = UserConvert.INSTANCE.convert2DO(userDOList);
            for (AdminUserRespDTO userRespDTO : userRespDTOList) {
                UserRoleDO userRoleDO = userRoleDOMap.get(userRespDTO.getId());
                if (ObjectUtil.isNotNull(userRoleDO)) {
                    RoleDO roleDO = roleDOMap.get(userRoleDO.getRoleId());
                    if (ObjectUtil.isNotNull(roleDO)) {
                        userRespDTO.setRoleId(roleDO.getId());
                        userRespDTO.setRoleName(roleDO.getName());
                    }
                }
                result.put(userRespDTO.getId(), userRespDTO);
            }
        }

        return result;
    }

    @Override
    @DataPermission(enable = false)
    public List<AdminUserRespDTO> selectListByCompanyIdsAndRoleIds(List<Long> companyIds, List<Long> roleIds) {
        List<AdminUserRespDTO> result = new ArrayList<AdminUserRespDTO>();
        List<AdminUserDO> userDOList = userMapper.selectListByCompanyIdsAndRoleIds(companyIds, roleIds);

        if (CollectionUtil.isNotEmpty(userDOList)) {
            result = UserConvert.INSTANCE.convertList4(userDOList);
        }
        return result;
    }



    @Override
    public List<AdminUserRespDTO> selectListByCompanyIds(List<Long> companyIds) {
        List<AdminUserRespDTO> result = new ArrayList<AdminUserRespDTO>();
        List<AdminUserDO> userDOList = userMapper.selectListByCompanyIds(companyIds);
        if (CollectionUtil.isNotEmpty(userDOList)) {
            result = UserConvert.INSTANCE.convertList4(userDOList);
        }
        return result;
    }

    @Override
    public List<AdminUserRespDTO> selectListByCompanyIdsAndUserIds(List<Long> companyIds, Set<Long> userIds) {
        List<AdminUserRespDTO> result = new ArrayList<AdminUserRespDTO>();
        List<AdminUserDO> userDOList = userMapper.selectListByCompanyIdsAndUserIds(companyIds, userIds);
        if (CollectionUtil.isNotEmpty(userDOList)) {
            result = UserConvert.INSTANCE.convertList4(userDOList);
        }
        return result;
    }

    @Override
    public List<AdminUserDO> getUserNickListById(List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return userMapper.selectList(new LambdaQueryWrapperX<AdminUserDO>().inIfPresent(AdminUserDO::getId, userIds)
                .select(AdminUserDO::getId, AdminUserDO::getNickname));
    }

    @Override
    @DataPermission(enable = false)
    public List<DeptTreeVO> getCompanyUserTreeList(Integer status) {
        //获取当前用户公司id
        Long companyId = SecurityFrameworkUtils.getLoginUser().getCompanyId();
        List<AdminUserDO> adminUserDOS = userMapper.selectCompanyUserListByStatus(status, companyId);
        List<UserSimpleRespVO> userSimpleRespVOS = BeanUtils.toBean(adminUserDOS, UserSimpleRespVO.class);

        List<Long> userDeptIds = adminUserDOS.stream().map(AdminUserDO::getDeptId).distinct().collect(Collectors.toList());

        List<DeptDO> deptList = deptMapper.selectList(DeptDO::getId, userDeptIds);
        List<DeptTreeVO> deptTreeVOS = BeanUtils.toBean(deptList, DeptTreeVO.class);

        Map<Long, DeptTreeVO> deptMap = new HashMap<>();
        deptTreeVOS.forEach(dept -> deptMap.put(dept.getId(), dept));
        List<DeptTreeVO> rootList = new ArrayList<>();
        for (DeptTreeVO dept : deptTreeVOS) {
            long parentId = dept.getParentId();
            if (deptMap.get(parentId) == null) {
                rootList.add(dept);
            } else {
                DeptTreeVO parent = deptMap.get(parentId);
                if (parent != null) {
                    parent.addChild(dept);
                }
            }
        }
        Deque<DeptTreeVO> queue = new LinkedList<>(rootList);
        while (!queue.isEmpty()) {
            DeptTreeVO dept = queue.poll();
            deptMap.put(dept.getId(), dept);
            queue.addAll(dept.getChildren());
        }

        for (UserSimpleRespVO user : userSimpleRespVOS) {
            Long deptId = user.getDeptId();
            DeptTreeVO dept = deptMap.get(deptId);
            if (dept != null) {
                dept.addChild(user);
            }
        }
        return rootList;
    }

    @Override
    @DataPermission(enable = false)
    public List<AdminUserDO> getCompanyUserList(Integer status) {
        //获取当前用户公司id
        Long companyId = SecurityFrameworkUtils.getLoginUser().getCompanyId();
        return userMapper.selectCompanyUserListByStatus(status, companyId);
    }

    @Override
    public List<AdminUserDO> getUsersByPlatformUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            return userMapper.selectList(new LambdaQueryWrapperX<AdminUserDO>().eqIfPresent(AdminUserDO::getPlatformUserId, userId));
        }
        return Collections.emptyList();
    }

    @Override
    public String getUserNickById(Long userId) {
        AdminUserDO userDO = userMapper.selectOne(new LambdaQueryWrapperX<AdminUserDO>().inIfPresent(AdminUserDO::getId, userId)
                .select(AdminUserDO::getNickname).last(" limit 1"));
        return userDO.getNickname() == null ? "" : userDO.getNickname();
    }

    @Override
    public List<RoleSimple4UserRespVO> getRoleUserList() {
        List<RoleSimple4UserRespVO> result = new ArrayList<>();


        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        Long tenantId = loginUser.getTenantId();
        //角色
        AtomicReference<List<RoleDO>> roleDOList = new AtomicReference<>(new ArrayList<>());
        DataPermissionUtils.executeIgnore(() -> {
            roleDOList.set(roleMapper.selectList(RoleDO::getTenantId, tenantId));

        });
        if (CollUtil.isEmpty(roleDOList.get())) {
            return Collections.emptyList();
        }
        AtomicReference<List<AdminUserDO>> userDOList = new AtomicReference<>(new ArrayList<>());
        DataPermissionUtils.executeIgnore(() -> {
            userDOList.set(userMapper.selectList(AdminUserDO::getTenantId, tenantId));

        });
        if (CollUtil.isEmpty(userDOList.get())) {
            return Collections.emptyList();
        }
        List<Long> userIds = userDOList.get().stream().map(AdminUserDO::getId).collect(Collectors.toList());
        Map<Long, AdminUserDO> userMap = CollectionUtils.convertMap(userDOList.get(), AdminUserDO::getId);
        List<UserRoleDO> userRoleDOList = userRoleMapper.selectList(UserRoleDO::getUserId, userIds);
        if (CollUtil.isEmpty(userRoleDOList)) {
            return Collections.emptyList();
        }
        Map<Long, List<UserRoleDO>> rel4roleIdMap = CollectionUtils.convertMultiMap(userRoleDOList, UserRoleDO::getRoleId);
        for (RoleDO role : roleDOList.get()) {
            RoleSimple4UserRespVO respVO = new RoleSimple4UserRespVO();
            respVO.setId(role.getId()).setName(role.getName());
            List<UserRoleDO> userRoleDOs = rel4roleIdMap.get(role.getId());
            respVO.setUserList(enhanceUsers(userRoleDOs, userMap));
            result.add(respVO);
        }

        return result;

    }

    @Override
    public void updateUserOpenId(Long id, String openId) {
        // 如果当前操作用户不是要绑定的用户
        if (!SecurityFrameworkUtils.getLoginUserId().equals(id)){
            throw exception(DIY_ERROR, "只能绑定您自己的账户");
        }
        AdminUserDO adminUser = userMapper.selectByAppOpenId(openId);
        if(ObjectUtil.isNotNull(adminUser)) {
            throw exception(DIY_ERROR, "该用户身份标识已与其他用户绑定，请确认标识是否正确无误");
        }
        AdminUserDO adminUserDO = userMapper.selectById(id);
        if(ObjectUtil.isNotNull(adminUserDO)) {
            adminUserDO.setAppOpenId(openId);
            userMapper.updateById(adminUserDO);
        } else {
            throw new ServiceException(ErrorCodeConstants.USER_NOT_EXISTS);
        }
    }

    private List<UserSimpleRespVO> enhanceUsers(List<UserRoleDO> userRoleDOs, Map<Long, AdminUserDO> userMap) {
        if(CollUtil.isEmpty(userRoleDOs)) {
            return Collections.emptyList();
        }
        List<UserSimpleRespVO> result = new ArrayList<>();
        for (UserRoleDO userRoleDO : userRoleDOs) {
            UserSimpleRespVO userSimpleRespVO = new UserSimpleRespVO();
            AdminUserDO userDO = userMap.get(userRoleDO.getUserId());
            if(ObjectUtil.isNotNull(userDO)) {
                userSimpleRespVO.setId(userRoleDO.getUserId());
                userSimpleRespVO.setNickname(userDO.getNickname());
            }
            result.add(userSimpleRespVO);
        }
        return result;
    }
    @Override
    public UserRespVO getUserInfo(Long id) {
        AdminUserDO user = userMapper.selectById(id);
        //1.从redis中获取企业的ID
        String acheKey = SecurityFrameworkUtils.getLoginUserKey4Space();
        //获取到相对方ID
        String relativeId = redisUtils.get(acheKey);
        List<RelativeContactDTO> relativeContactDTOS = relativeApi.getRelativeUserId(relativeId,null);
        List<DeptDO> list = deptService.getListAllSimpleBy(relativeContactDTOS);
        DeptDO deptDO = list.size()>0||list!=null?  list.get(0): null;

        UserPageItemRespVO userPageItemRespVO = UserConvert.INSTANCE.convert(user).setDept(UserConvert.INSTANCE.convert(deptDO));
        userPageItemRespVO.setDeptId(deptDO.getId());
        return userPageItemRespVO;
    }

}
