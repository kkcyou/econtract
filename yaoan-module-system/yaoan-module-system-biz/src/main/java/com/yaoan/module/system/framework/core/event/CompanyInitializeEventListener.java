package com.yaoan.module.system.framework.core.event;

import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.module.system.controller.admin.permission.vo.role.RoleCreateReqVO;
import com.yaoan.module.system.controller.admin.user.vo.user.UserCreateReqVO;
import com.yaoan.module.system.convert.dept.CompanyConvert;
import com.yaoan.module.system.dal.dataobject.dept.CompanyDO;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.dal.mysql.dept.CompanyMapper;
import com.yaoan.module.system.dal.mysql.permission.RoleMapper;
import com.yaoan.module.system.dal.mysql.user.AdminUserMapper;
import com.yaoan.module.system.enums.permission.DataScopeEnum;
import com.yaoan.module.system.enums.permission.RoleCodeEnum;
import com.yaoan.module.system.enums.permission.RoleTypeEnum;
import com.yaoan.module.system.service.permission.PermissionService;
import com.yaoan.module.system.service.user.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;

/**
 * {@link CompanyInitializeEvent} 的监听器
 *
 * @author doujl
 */
@Component
@Slf4j
public class CompanyInitializeEventListener implements ApplicationListener<CompanyInitializeEvent> {


    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private AdminUserService userService;

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionService permissionService;

    @Override
    public final void onApplicationEvent(CompanyInitializeEvent event) {
        log.info("监听到公司创建消息～～");
        //查询是否已经有该租户的公司管理员角色
        Long initRoleId = 0L;
        int i = 0;
        RoleDO roleDO = roleMapper.selectOne(RoleDO::getCode, RoleCodeEnum.COMPANY_ADMIN.getCode());
        if (roleDO != null) {
            initRoleId = roleDO.getId();
        } else {
            //创建新的公司管理员角色
            RoleDO role = new RoleDO();
            role.setName(RoleCodeEnum.COMPANY_ADMIN.getName()).setCode(RoleCodeEnum.COMPANY_ADMIN.getCode()).setStatus(CommonStatusEnum.ENABLE.getStatus())
                    .setType(RoleTypeEnum.SYSTEM.getType()).setSort(0).setRemark("系统自动生成").setDataScope(DataScopeEnum.COMPANY.getScope()).setCompanyId(event.getId());
            i = roleMapper.insert(role);
            //角色分配权限
            if (i > 0) {
                initRoleId = role.getId();
                Integer[] menuIds = {1, 5, 100, 101, 102, 103, 104, 105, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1025, 1026, 1027, 1028, 1029, 1030, 1063, 1064, 1065, 1110, 1111, 1112, 1113, 1114, 1115, 1138, 1139, 1140, 1141, 1142, 1143, 1185, 1186, 1187, 1188, 1189, 1190, 1191, 1192, 1193, 1194, 1195, 1196, 1197, 1198, 1199, 1200, 1201, 1202, 1207, 1208, 1209, 1210, 1211, 1212, 1213, 1215, 1216, 1217, 1218, 1219, 1220, 1221, 1222, 1224, 1225, 1226, 1227, 1228, 1229, 1247, 1248, 1249, 1250, 1251, 1252, 2085, 2086, 2087, 2088, 2089, 2090, 2091, 2119, 2125, 2126, 2127, 2307, 2308, 2313, 2314, 2316, 2318, 2319, 2321, 2328, 2329, 2330, 2331, 2333, 2334, 2337, 2338, 2339, 2340, 2341, 2342, 2343, 2348, 2350, 2351, 2352, 2353, 2354, 2355, 2356, 2357, 2358, 2359, 2360, 2361, 2362, 2367, 2373, 2374, 2375, 2376, 2377, 2378, 2379, 2380, 2381, 2382, 2383, 2384, 2385, 2386, 2387, 2388, 2389, 2390, 2391, 2392, 2393, 2394, 2395, 2396, 2397, 2398, 2399, 2400, 2401, 2402, 2403, 2404, 2405, 2406, 2407, 2408, 2409, 2410, 2411, 2412, 2413, 2414, 2415, 2416, 2417, 2418, 2426, 2427, 2428, 2439, 2440, 2441, 2442, 2443, 2444, 2445, 2446, 2447, 2448, 2449, 2450, 2451, 2452, 2453, 2454, 2455, 2456, 2457, 2458, 2459, 2460};
                HashSet<Long> menuSet = Arrays.stream(menuIds)
                        .map(Integer::longValue) // 将Integer转换为Long
                        .collect(Collectors.toCollection(HashSet::new));
                permissionService.assignRoleMenu(initRoleId, menuSet);
            } else {
                log.error("创建公司管理员角色失败");
                throw new RuntimeException("创建公司管理员角色失败,请重新创建");
            }
        }
        //添加公司管理员
        UserCreateReqVO userVo = CompanyConvert.INSTANCE.company2User(event);
        userVo.setPassword(StringUtils.isEmpty(event.getPassword()) ? "12345678" : event.getPassword());//前端加上之后删除
        userVo.setUsername(StringUtils.isEmpty(event.getUsername()) ? userVo.getMobile() : event.getUsername());
        Long userId = userService.createUser(userVo);
        //分配角色权限
        permissionService.assignUserRole(userId, singleton(initRoleId));
        //修改公司联系人
        companyMapper.updateById(new CompanyDO()
                .setId(event.getId())
                .setLeaderUserId(userId));
    }


}
