package com.yaoan.module.system.api.user;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.econtract.api.contract.dto.UserDTO;
import com.yaoan.module.econtract.api.relative.RelativeApi;
import com.yaoan.module.econtract.api.relative.dto.RelativeContactDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyDeptRespDTO;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.convert.user.UserConvert;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.dal.mysql.user.AdminUserMapper;
import com.yaoan.module.system.service.user.AdminUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Admin 用户 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class AdminUserApiImpl implements AdminUserApi {

    @Resource
    private AdminUserService userService;
    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public List<AdminUserRespDTO> getUserListLikeNickname(String nickname) {
        List<AdminUserRespDTO> users = userService.getUserListLikeNickname(nickname);
        return users;
    }

    @Override
    @DataPermission(enable = false)
    public AdminUserRespDTO getUser(Long id) {
        AdminUserDO user = userService.getUser(id);
        return UserConvert.INSTANCE.convert4(user);
    }


    @Override
    public List<AdminUserRespDTO> getUserList(Collection<Long> ids) {
        List<AdminUserDO> users = userService.getUserList(ids);
        return UserConvert.INSTANCE.convertList4(users);
    }
    @Override
    public List<AdminUserRespDTO> getUserList2(List<Long> ids) {
        List<AdminUserDO> users = userService.getUserList2(ids);
        return UserConvert.INSTANCE.convertList4(users);
    }



    @Override
    public List<AdminUserRespDTO> getUserListByDeptIds(Collection<Long> deptIds) {
        List<AdminUserDO> users = userService.getUserListByDeptIds(deptIds);
        return UserConvert.INSTANCE.convertList4(users);
    }

    @Override
    public List<AdminUserRespDTO> getUserListByPostIds(Collection<Long> postIds) {
        List<AdminUserDO> users = userService.getUserListByPostIds(postIds);
        return UserConvert.INSTANCE.convertList4(users);
    }

    @Override
    public void validateUserList(Collection<Long> ids) {
        userService.validateUserList(ids);
    }

    @Override
    @DataPermission(enable = false)
    public AdminUserRespDTO selectAdminUser(String account, String name, String idCard) {
        return userService.selectAdminUser(account, name, idCard);
    }

    @Override
    @DataPermission(enable = false)
    public UserCompanyDeptRespDTO selectUserCompanyDept(Long userId) {
        //根据用户id获取用户信息
        AtomicReference<UserCompanyDeptRespDTO> atomic = new AtomicReference<>();

        //（相对方逻辑）免租户
        DataPermissionUtils.executeIgnore(()->{
            TenantUtils.executeIgnore(()->{
                AdminUserDO  user = userService.getUser(userId);
                UserCompanyDeptRespDTO userCompanyDeptRespDTO = UserConvert.INSTANCE.toUserCompanyDeptRespDTO(user);
                //根据部门id获取部门信息
                UserCompanyDeptRespDTO company = userService.getCompany(userCompanyDeptRespDTO, user == null ? null : user.getCompanyId());
                atomic.set(company);
            });
        });

        return atomic.get();
    }

    @Override
    @DataPermission(enable = false)
    public List<AdminUserRespDTO> getUserList() {
        List<AdminUserRespDTO> userList = userService.getUserList();
        return userList;
    }

    @Override
    public AdminUserRespDTO getUserByUsername(String username) {
        return UserConvert.INSTANCE.convert4(userService.getUserByUsername(username));
    }

    @Override
    public List<String> getUserIdsByNameLike(String searchText) {
        List<AdminUserRespDTO> nickUsers = new ArrayList<AdminUserRespDTO>();
        List<Long> nickUserIds = new ArrayList<Long>();
        List<String> userIdsAsString = new ArrayList<String>();
        if (StringUtils.isNotBlank(searchText)) {
            nickUsers = getUserListLikeNickname(searchText);
            if (CollectionUtil.isNotEmpty(nickUsers)) {
                //将昵称模糊查询选中的userId组成List
                nickUserIds = nickUsers.stream()
                        .map(AdminUserRespDTO::getId)
                        .collect(Collectors.toList());
            }
            //将Long转为String
            userIdsAsString = nickUserIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
            return userIdsAsString;
        }
        return Collections.emptyList();
    }

    @Override
    public List<AdminUserRespDTO> selectListByDeptIdsAndUserIds(List<Long> deptIds, Set<Long> userIds) {
        return userService.selectListByDeptIdsAndUserIds(deptIds, userIds);
    }

    @Override
    public Map<Long, AdminUserRespDTO> getUserRoleInfos(List<Long> userIds) {
        return userService.getUserRoleInfos(userIds);
    }

    @Override
    public List<AdminUserRespDTO> selectListByCompanyIdsAndRoleIds(List<Long> companyIds,List<Long> roleIds) {
        return userService.selectListByCompanyIdsAndRoleIds(companyIds,roleIds);
    }
    @Resource
    private RelativeApi relativeApi;
    @Override
    public List<AdminUserRespDTO> selectListByCompanyIdsSaas(List<Long> companyIds) {
        List<RelativeContactDTO> relativeContactDTOS = relativeApi.getRelativeContactByCompanyIds(companyIds);
        if(CollectionUtil.isEmpty(relativeContactDTOS)){
            return Collections.emptyList();
        }
        List<Long> userIds = relativeContactDTOS.stream().map(RelativeContactDTO::getUserId).collect(Collectors.toList());
        List<AdminUserDO> adminUserDOS = adminUserMapper.selectList(AdminUserDO::getId, userIds);
        return UserConvert.INSTANCE.convertList4(adminUserDOS);
    }

    @Override
    public List<AdminUserRespDTO> selectListByCompanyIdsAndUserIds(List<Long> companyIds, Set<Long> userIds) {
        return userService.selectListByCompanyIdsAndUserIds(companyIds,userIds);
    }



    @Override
    public List<AdminUserRespDTO> getUserNickList(List<Long> userIds) {
        List<AdminUserDO> users = userService.getUserNickListById(userIds);
        return UserConvert.INSTANCE.convertList4(users);
    }

    /**
     * 根据用户id获取用户昵称
     * @return
     */
    @Override
    public String getUserNickById(Long userId) {
        return userService.getUserNickById(userId);
    }

    @Override
    public List<AdminUserRespDTO> getUserList4Tel(List<String> telList){
        //手机号匹配用户
        List<AdminUserDO> userDOList = adminUserMapper.selectList(new LambdaQueryWrapperX<AdminUserDO>().inIfPresent(AdminUserDO::getMobile,telList));
        return UserConvert.INSTANCE.convertList4(userDOList);
    }

    @Override
    public List<Long> saveBatch(List<UserDTO> userDTOList) {
        List<AdminUserDO> userDOList = UserConvert.INSTANCE.dto2DOList(userDTOList);
        adminUserMapper.insertBatch(userDOList);
        return userDOList.stream().map(AdminUserDO::getId).collect(Collectors.toList());
    }

    @Override
    public void updateUsers(List<UserDTO> userDTOList) {
        List<AdminUserDO> userDOList = UserConvert.INSTANCE.dto2DOList(userDTOList);
        adminUserMapper.updateBatch(userDOList);
    }

}
