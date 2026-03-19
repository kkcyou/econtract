package com.yaoan.framework.mybatis.core.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 通用参数填充实现类
 * <p>
 * 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 *
 * @author hexiaowu
 */
public class DefaultDBFieldHandler implements MetaObjectHandler {

    private static final String CREATE_USER_FIELD = "creator";
    private static final String CREATE_DATE_BY_FIELD = "createTime";
    private static final String UPDATE_BY_BY_FIELD = "updater";
    private static final String UPDATE_DATE_BY_FIELD = "updateTime";
    private static final String DEPT_ID_FIELD = "deptId";
    private static final String COMPANY_ID = "company_id";

    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject)) {
            Long userId = WebFrameworkUtils.getLoginUserId();
            LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
            Long companyId = null;
            if (loginUser != null) {
                companyId = loginUser.getCompanyId();
            }
            if (Objects.nonNull(userId) && metaObject.hasSetter(CREATE_USER_FIELD)) {
                this.strictInsertFill(metaObject, CREATE_USER_FIELD, String.class, userId.toString());
            }
            if (metaObject.hasSetter(CREATE_DATE_BY_FIELD)) {
                this.strictInsertFill(metaObject, CREATE_DATE_BY_FIELD, LocalDateTime.class, LocalDateTime.now());
            }
            if (Objects.nonNull(userId) && metaObject.hasSetter(UPDATE_BY_BY_FIELD)) {
                this.strictUpdateFill(metaObject, UPDATE_BY_BY_FIELD, String.class, userId.toString());
            }
            if (metaObject.hasSetter(UPDATE_DATE_BY_FIELD)) {
                this.strictUpdateFill(metaObject, UPDATE_DATE_BY_FIELD, LocalDateTime.class, LocalDateTime.now());
            }
            if (Objects.nonNull(userId) && metaObject.hasSetter(DEPT_ID_FIELD)) {
                AdminUserApi adminUserApi = SpringUtil.getBean(AdminUserApi.class);
                AdminUserRespDTO user = adminUserApi.getUser(userId);
                if (user != null && user.getDeptId() != null) {
                    this.strictInsertFill(metaObject, DEPT_ID_FIELD, Long.class, user.getDeptId());
                }
            }
            if (metaObject.hasSetter(COMPANY_ID)) {
                this.strictInsertFill(metaObject, COMPANY_ID, Long.class, companyId);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新以当前时间为更新时间
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);

        // 当前登录用户不为空，则当前登录用户为更新人
        Long userId = WebFrameworkUtils.getLoginUserId();
        if (Objects.nonNull(userId)) {
            setFieldValByName("updater", userId.toString(), metaObject);
        }
    }
}
