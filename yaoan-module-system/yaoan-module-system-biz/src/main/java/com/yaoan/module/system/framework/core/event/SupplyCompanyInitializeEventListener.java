package com.yaoan.module.system.framework.core.event;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.module.econtract.api.relative.RelativeApi;
import com.yaoan.module.econtract.api.relative.dto.RelativeContactDTO;
import com.yaoan.module.econtract.api.relative.dto.RelativeDTO;
import com.yaoan.module.system.controller.admin.user.vo.user.UserCreateReqVO;
import com.yaoan.module.system.convert.dept.CompanyConvert;
import com.yaoan.module.system.dal.dataobject.dept.CompanyDO;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.dal.mysql.dept.CompanyMapper;
import com.yaoan.module.system.dal.mysql.permission.RoleMapper;
import com.yaoan.module.system.enums.permission.DataScopeEnum;
import com.yaoan.module.system.enums.permission.RoleCodeEnum;
import com.yaoan.module.system.enums.permission.RoleTypeEnum;
import com.yaoan.module.system.service.permission.PermissionService;
import com.yaoan.module.system.service.user.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.infra.enums.ErrorCodeConstants.DIY_ERROR;
import static java.util.Collections.singleton;

/**
 * {@link SupplyCompanyInitializeEvent} 的监听器
 *
 * @author lls
 */
@Component
@Slf4j
public class SupplyCompanyInitializeEventListener implements ApplicationListener<SupplyCompanyInitializeEvent> {

    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private AdminUserService userService;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionService permissionService;
    @Resource
    private RelativeApi relativeApi;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public final void onApplicationEvent(SupplyCompanyInitializeEvent event) {
        log.info("监听到供应商公司创建消息～～");
        //查询是否已经有该租户的公司角色
        Long initRoleId = 0L;
        int i = 0;
        List<RoleDO> roleDOList = roleMapper.selectList(new LambdaQueryWrapper<RoleDO>().eq(RoleDO::getName,"供应商"));
        if (roleDOList.size() > 0) {
            initRoleId = roleDOList.get(0).getId();
        } else {
            //创建新的公司角色
            RoleDO role = new RoleDO();
            role.setName(RoleCodeEnum.SUPPLY_OPERATOR.getName()).setCode(RoleCodeEnum.SUPPLY_OPERATOR.getCode()).setStatus(CommonStatusEnum.ENABLE.getStatus())
                    .setType(RoleTypeEnum.CUSTOM.getType()).setSort(0).setRemark("系统自动生成").setDataScope(DataScopeEnum.SELF.getScope()).setCompanyId(event.getId());
            i = roleMapper.insert(role);
            //角色分配权限
            if (i > 0) {
                initRoleId = role.getId();
                Integer[] menuIds = {2328, 2331, 2348, 2438};
                HashSet<Long> menuSet = Arrays.stream(menuIds)
                        .map(Integer::longValue) // 将Integer转换为Long
                        .collect(Collectors.toCollection(HashSet::new));
                permissionService.assignRoleMenu(initRoleId, menuSet);

            } else {
                throw new RuntimeException("创建供应商角色失败,请重新创建");
            }
        }
        //添加用户
        List<RelativeContactDTO>  contactDTOList = relativeApi.getContacts4RelativeId(event.getRelativeId());
        if(CollectionUtil.isEmpty(contactDTOList)){
            throw exception(DIY_ERROR,"相对方"+ event.getRelativeId() +"的联系人不存在");
        }
        buildUsers(initRoleId,event,contactDTOList);

        //关联相对方
        relativeApi.saveRelativeContacts(contactDTOList);
//        //修改公司联系人
        companyMapper.updateById(new CompanyDO()
                .setId(event.getId())
                .setLeaderUserId(contactDTOList.get(0).getUserId()));
    }

    private void buildUsers(Long initRoleId, SupplyCompanyInitializeEvent event, List<RelativeContactDTO> contactDTOList) {
        for (RelativeContactDTO contactDTO : contactDTOList) {
            UserCreateReqVO userVo = new UserCreateReqVO();
            userVo.setPassword(StringUtils.isEmpty(event.getPassword()) ? "12345678"  : event.getPassword());//前端加上之后删除
            userVo.setUsername(contactDTO.getContactTel());
            userVo.setMobile(contactDTO.getContactTel());
            userVo.setCompanyId(event.getCompanyId());
            userVo.setNickname(contactDTO.getName());
            userVo.setEmail(contactDTO.getEmail());
            userVo.setIdCard(contactDTO.getCardNo());
            Long userId = userService.createUser(userVo);
            //分配角色权限
            permissionService.assignUserRole(userId, singleton(initRoleId));
            contactDTO.setUserId(userId);
        }

    }


}
