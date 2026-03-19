package com.yaoan.module.system.framework.datapermission.config;

import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import com.yaoan.module.system.dal.dataobject.dept.PostDO;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.framework.datapermission.core.rule.dept.DeptDataPermissionRuleCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * system 模块的数据权限 Configuration
 *
 * @author 芋道源码
 */
@Configuration(proxyBeanMethods = false)
public class DeptDataPermissionConfiguration {

    @Bean
    public DeptDataPermissionRuleCustomizer sysDeptDataPermissionRuleCustomizer() {
        return rule -> {
            // dept
            rule.addDeptColumn(AdminUserDO.class);
            rule.addDeptColumn(DeptDO.class, "id");
            // user
//            rule.addUserColumn(AdminUserDO.class, "id");
            // company
            rule.addCompanyColumn(AdminUserDO.class);
            rule.addCompanyColumn(DeptDO.class);
            rule.addCompanyColumn(PostDO.class);
            rule.addCompanyColumn(RoleDO.class);

//            rule.addTenantColumn(AdminUserDO.class);
        };
    }

}
