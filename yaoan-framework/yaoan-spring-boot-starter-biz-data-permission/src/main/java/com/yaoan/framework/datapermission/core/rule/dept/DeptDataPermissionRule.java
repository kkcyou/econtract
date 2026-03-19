package com.yaoan.framework.datapermission.core.rule.dept;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.yaoan.framework.common.enums.UserTypeEnum;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.json.JsonUtils;
import com.yaoan.framework.datapermission.core.rule.DataPermissionRule;
import com.yaoan.framework.expression.OrExpressionX;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.mybatis.core.util.MyBatisUtils;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.system.api.permission.PermissionApi;
import com.yaoan.module.system.api.permission.dto.DeptDataPermissionRespDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 基于部门的 {@link DataPermissionRule} 数据权限规则实现
 * <p>
 * 注意，使用 DeptDataPermissionRule 时，需要保证表中有 dept_id 部门编号的字段，可自定义。
 * <p>
 * 实际业务场景下，会存在一个经典的问题？当用户修改部门时，冗余的 dept_id 是否需要修改？
 * 1. 一般情况下，dept_id 不进行修改，则会导致用户看不到之前的数据。【yaoan-server 采用该方案】
 * 2. 部分情况下，希望该用户还是能看到之前的数据，则有两种方式解决：【需要你改造该 DeptDataPermissionRule 的实现代码】
 * 1）编写洗数据的脚本，将 dept_id 修改成新部门的编号；【建议】
 * 最终过滤条件是 WHERE dept_id = ?
 * 2）洗数据的话，可能涉及的数据量较大，也可以采用 user_id 进行过滤的方式，此时需要获取到 dept_id 对应的所有 user_id 用户编号；
 * 最终过滤条件是 WHERE user_id IN (?, ?, ? ...)
 * 3）想要保证原 dept_id 和 user_id 都可以看的到，此时使用 dept_id 和 user_id 一起过滤；
 * 最终过滤条件是 WHERE dept_id = ? OR user_id IN (?, ?, ? ...)
 *
 * @author 芋道源码
 */
@AllArgsConstructor
@Slf4j
@Lazy
public class DeptDataPermissionRule implements DataPermissionRule {

    /**
     * LoginUser 的 Context 缓存 Key
     */
    protected static final String CONTEXT_KEY = DeptDataPermissionRule.class.getSimpleName();
    static final Expression EXPRESSION_NULL = new NullValue();
    private static final String DEPT_COLUMN_NAME = "dept_id";
    private static final String COMPANY_COLUMN_NAME = "company_id";
    private static final String TENANT_COLUMN_NAME = "tenant_id";
    private static final String USER_COLUMN_NAME = "user_id";
    private static final String SUPPLY_COLUMN_NAME = "supply_id";
    private static final String ORG_COLUMN_NAME = "org_id";
    private static final String REGION_COLUMN_NAME = "region_id";
    private static final String AGENT_COLUMN_NAME = "agent_guid";
    private final PermissionApi permissionApi;

    /**
     * 基于部门的表字段配置
     * 一般情况下，每个表的部门编号字段是 dept_id，通过该配置自定义。
     * <p>
     * key：表名
     * value：字段名
     */
    private final Map<String, String> deptColumns = new HashMap<>();
    /**
     * 基于公司的表字段配置
     * 一般情况下，每个表的部门编号字段是 company_id，通过该配置自定义。
     * <p>
     * key：表名
     * value：字段名
     */
    private final Map<String, String> companyColumns = new HashMap<>();

    /**
     * 基于供应商ID的表字段配置
     * 一般情况下，每个表的供应商字段是 supply_id，通过该配置自定义。
     * <p>
     * key：表名
     * value：字段名
     */
    private final Map<String, String> supplyColumns = new HashMap<>();
    /**
     * 基于单位id的表字段配置
     * 一般情况下，每个表的单位字段是 org_id，通过该配置自定义。
     * <p>
     * key：表名
     * value：字段名
     */
    private final Map<String, String> orgColumns = new HashMap<>();
    /**
     * 基于代理机构id的表字段配置
     * 一般情况下，每个表的代理机构字段是 agent_guid，通过该配置自定义。
     * <p>
     * key：表名
     * value：字段名
     */
    private final Map<String, String> agentColumns = new HashMap<>();
    /**
     * 基于区划的表字段配置
     * 一般情况下，每个表的区划字段是 region_code，通过该配置自定义。
     * <p>
     * key：表名
     * value：字段名
     */
    private final Map<String, String> regionColumns = new HashMap<>();
    /**
     * 基于用户的表字段配置
     * 一般情况下，每个表的部门编号字段是 dept_id，通过该配置自定义。
     * <p>
     * key：表名
     * value：字段名
     */
    private final Map<String, String> userColumns = new HashMap<>();
    /**
     * 所有表名，是 {@link #deptColumns} 和 {@link #userColumns} 的合集
     */
    private final Set<String> TABLE_NAMES = new HashSet<>();

    @Override
    public Set<String> getTableNames() {
        return TABLE_NAMES;
    }

    @Override
    public Expression getExpression(String tableName, Alias tableAlias) {
        // 只有有登陆用户的情况下，才进行数据权限的处理
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if (loginUser == null) {
            return null;
        }

        // 只有管理员类型的用户，才进行数据权限的处理
        if (ObjectUtil.notEqual(loginUser.getUserType(), UserTypeEnum.ADMIN.getValue())) {
            return null;
        }
//        // 情况一，如果是超级管理员 可查看全部，则无需拼接条件
//        if (0 == loginUser.getType() && StringUtils.isBlank(loginUser.getRegionCode())) {
//            return null;
//        }
//        // 情况二，如果是区划管理员，查看区划数据
//        if (0 == loginUser.getType() && StringUtils.isNotBlank(loginUser.getRegionCode())) {
//            return buildRegionExpression(tableName, tableAlias, loginUser.getRegionCode());
//        }

//        //判断用户身份，填充条件
//        if (loginUser.getType() == null) {
//            return EXPRESSION_NULL;
//        }
        //添加采购人单位逻辑
//        if (1 == loginUser.getType()) {
//            return buildOrgExpressionV2(tableName, tableAlias, loginUser.getOrgId());
//        }
//        //添加供应商逻辑
//        if (2 == loginUser.getType()) {
//            return buildSupplyExpression(tableName, tableAlias, loginUser.getSupplyId());
//        }
//        //添加代理机构逻辑
//        if (3 == loginUser.getType()) {
//            return buildAgentExpression(tableName, tableAlias, loginUser.getAgentId());
//        }
        //添加监管部门逻辑
//        if (4 == loginUser.getType()) {
//            if (StringUtils.isNotBlank(loginUser.getRegionCode()) && "230001".equals(loginUser.getRegionCode())) {
//                return null;
//            }
//            return buildRegionExpression(tableName, tableAlias, loginUser. ());
//        }


        // 获得数据权限
        DeptDataPermissionRespDTO deptDataPermission = loginUser.getContext(CONTEXT_KEY, DeptDataPermissionRespDTO.class);
        // 从上下文中拿不到，则调用逻辑进行获取
        if (deptDataPermission == null) {
            deptDataPermission = permissionApi.getDeptDataPermission(loginUser.getId());
            if (deptDataPermission == null) {
                log.error("[getExpression][LoginUser({}) 获取数据权限为 null]", JsonUtils.toJsonString(loginUser));
                throw new NullPointerException(String.format("LoginUser(%d) Table(%s/%s) 未返回数据权限",
                        loginUser.getId(), tableName, tableAlias.getName()));
            }
            // 添加到上下文中，避免重复计算
            loginUser.setContext(CONTEXT_KEY, deptDataPermission);
        }

        // 情况一，如果是 ALL 可查看全部，则无需拼接条件
        if (deptDataPermission.getAll()) {
            return null;
        }

        // 有公司权限情况，不需要考虑部门和自己权限
        if (deptDataPermission.getCompany()) {
            return buildCompanyExpression(tableName, tableAlias, deptDataPermission.getCompany(), loginUser.getCompanyId());
        }

        // 情况二，即不能查看部门，又不能查看自己，则说明 100% 无权限
        if (CollUtil.isEmpty(deptDataPermission.getDeptIds())
                && Boolean.FALSE.equals(deptDataPermission.getSelf())) {
            return new EqualsTo(null, null); // WHERE null = null，可以保证返回的数据为空
        }

        // 情况三，拼接 Dept 和 User 的条件，最后组合
        Expression deptExpression = buildDeptExpression(tableName, tableAlias, deptDataPermission.getDeptIds());
        Expression userExpression = buildUserExpression(tableName, tableAlias, deptDataPermission.getSelf(), loginUser.getId());
        if (deptExpression == null && userExpression == null) {
            // TODO 芋艿：获得不到条件的时候，暂时不抛出异常，而是不返回数据
            log.warn("[getExpression][LoginUser({}) Table({}/{}) DeptDataPermission({}) 构建的条件为空]",
                    JsonUtils.toJsonString(loginUser), tableName, tableAlias, JsonUtils.toJsonString(deptDataPermission));
//            throw new NullPointerException(String.format("LoginUser(%d) Table(%s/%s) 构建的条件为空",
//                    loginUser.getId(), tableName, tableAlias.getName()));
//            return EXPRESSION_NULL;
            return null;
        }
        if (deptExpression == null) {
            return userExpression;
        }
        if (userExpression == null) {
            return deptExpression;
        }
        // 目前，如果有指定部门 + 可查看自己，采用 OR 条件。即，WHERE (dept_id IN ? OR user_id = ?)
        return new OrExpressionX(deptExpression, userExpression);
    }

    private Expression buildDeptExpression(String tableName, Alias tableAlias, Set<Long> deptIds) {
        // 如果不存在配置，则无需作为条件
        String columnName = deptColumns.get(tableName);
        if (StrUtil.isEmpty(columnName)) {
            return null;
        }
        // 如果为空，则无条件
        if (CollUtil.isEmpty(deptIds)) {
            return null;
        }
        // 拼接条件
        return new InExpression(MyBatisUtils.buildColumn(tableName, tableAlias, columnName),
                new ExpressionList(CollectionUtils.convertList(deptIds, LongValue::new)));
    }

    private Expression buildUserExpression(String tableName, Alias tableAlias, Boolean self, Long userId) {
        // 如果不查看自己，则无需作为条件
        if (Boolean.FALSE.equals(self)) {
            return null;
        }
        String columnName = userColumns.get(tableName);
        if (StrUtil.isEmpty(columnName)) {
            return null;
        }
        // 拼接条件
        return new EqualsTo(MyBatisUtils.buildColumn(tableName, tableAlias, columnName), new LongValue(userId));
    }

    private Expression buildCompanyExpression(String tableName, Alias tableAlias, Boolean company, Long companyId) {
        // 如果不查看公司，则无需作为条件
        if (Boolean.FALSE.equals(company)) {
            return null;
        }
        String columnName = companyColumns.get(tableName);
        if (StrUtil.isEmpty(columnName)) {
            return null;
        }
        // 拼接条件
        return new EqualsTo(MyBatisUtils.buildColumn(tableName, tableAlias, columnName), new LongValue(companyId));
    }

    private Expression buildOrgExpressionV2(String tableName, Alias tableAlias, String orgId) {
        // 如果不存在配置，则无需作为条件
        String columnName = orgColumns.get(tableName);
        if (StrUtil.isEmpty(columnName)) {
            return null;
        }
        // 如果为空，则无条件
        if (StringUtils.isEmpty(orgId)) {
            return null;
        }
        LikeExpression likeExpression = new LikeExpression();
        likeExpression.setLeftExpression(MyBatisUtils.buildColumn(tableName, tableAlias, columnName));
        likeExpression.setRightExpression(new StringValue("%" + orgId + "%"));
        return likeExpression;
    }

    private Expression buildRegionExpression(String tableName, Alias tableAlias, String regionCode) {
        // 如果不存在配置，则无需作为条件
        String columnName = regionColumns.get(tableName);
        if (StrUtil.isEmpty(columnName)) {
            return null;
        }
        // 如果为空，则无条件
        if (StringUtils.isEmpty(regionCode)) {
            return null;
        }
        // 拼接条件
        return new EqualsTo(MyBatisUtils.buildColumn(tableName, tableAlias, columnName), new StringValue(regionCode));
    }

    private Expression buildSupplyExpression(String tableName, Alias tableAlias, String supplyId) {
        // 如果不存在配置，则无需作为条件
        String columnName = supplyColumns.get(tableName);
        if (StrUtil.isEmpty(columnName)) {
            return null;
        }
        // 如果为空，则无条件
        if (StringUtils.isEmpty(supplyId)) {
            return null;
        }
        // 拼接条件
        LikeExpression likeExpression = new LikeExpression();
        likeExpression.setLeftExpression(MyBatisUtils.buildColumn(tableName, tableAlias, columnName));
        likeExpression.setRightExpression(new StringValue("%" + supplyId + "%"));
        return likeExpression;
    }

    private Expression buildRegionExpressionV2(String tableName, Alias tableAlias, Set<String> regionCodes) {
        // 如果不存在配置，则无需作为条件
        String columnName = regionColumns.get(tableName);
        if (StrUtil.isEmpty(columnName)) {
            return null;
        }
        // 如果为空，则无条件
        if (CollUtil.isEmpty(regionCodes)) {
            return null;
        }
        // 拼接条件
        return new InExpression(MyBatisUtils.buildColumn(tableName, tableAlias, columnName),
                new ExpressionList(CollectionUtils.convertList(regionCodes, StringValue::new)));
    }

    //代理机构
    private Expression buildAgentExpression(String tableName, Alias tableAlias, String ragentGuId) {
        // 如果不存在配置，则无需作为条件
        String columnName = agentColumns.get(tableName);
        if (StrUtil.isEmpty(columnName)) {
            return null;
        }
        // 如果为空，则无条件
        if (StringUtils.isEmpty(ragentGuId)) {
            return null;
        }
        // 拼接条件
        return new EqualsTo(MyBatisUtils.buildColumn(tableName, tableAlias, columnName), new StringValue(ragentGuId));
    }


    // ==================== 添加配置 ====================

    public void addDeptColumn(Class<? extends BaseDO> entityClass) {
        addDeptColumn(entityClass, DEPT_COLUMN_NAME);
    }

    public void addDeptColumn(Class<? extends BaseDO> entityClass, String columnName) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        addDeptColumn(tableName, columnName);
    }

    public void addDeptColumn(String tableName, String columnName) {
        deptColumns.put(tableName, columnName);
        TABLE_NAMES.add(tableName);
    }

    public void addUserColumn(Class<? extends BaseDO> entityClass) {
        addUserColumn(entityClass, USER_COLUMN_NAME);
    }

    public void addUserColumn(Class<? extends BaseDO> entityClass, String columnName) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        addUserColumn(tableName, columnName);
    }

    public void addUserColumn(String tableName, String columnName) {
        userColumns.put(tableName, columnName);
        TABLE_NAMES.add(tableName);
    }

    public void addCompanyColumn(Class<? extends BaseDO> entityClass) {
        addCompanyColumn(entityClass, COMPANY_COLUMN_NAME);
    }

    public void addTenantColumn(Class<? extends BaseDO> entityClass) {
        addTenantColumn(entityClass, TENANT_COLUMN_NAME);
    }

    public void addCompanyColumn(Class<? extends BaseDO> entityClass, String columnName) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        addCompanyColumn(tableName, columnName);
    }

    public void addCompanyColumn(String tableName, String columnName) {
        companyColumns.put(tableName, columnName);
        TABLE_NAMES.add(tableName);
    }

    public void addSupplyColumn(Class<? extends BaseDO> entityClass) {
        addSupplyColumn(entityClass, SUPPLY_COLUMN_NAME);
    }

    public void addSupplyColumn(Class<? extends BaseDO> entityClass, String columnName) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        addSupplyColumn(tableName, columnName);
    }

    public void addSupplyColumn(String tableName, String columnName) {
        supplyColumns.put(tableName, columnName);
        TABLE_NAMES.add(tableName);
    }

    public void addOrgColumn(Class<? extends BaseDO> entityClass) {
        addOrgColumn(entityClass, ORG_COLUMN_NAME);
    }

    public void addOrgColumn(Class<? extends BaseDO> entityClass, String columnName) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        addOrgColumn(tableName, columnName);
    }

    public void addOrgColumn(String tableName, String columnName) {
        orgColumns.put(tableName, columnName);
        TABLE_NAMES.add(tableName);
    }

    public void addRegionColumn(Class<? extends BaseDO> entityClass) {
        addRegionColumn(entityClass, REGION_COLUMN_NAME);
    }

    public void addRegionColumn(Class<? extends BaseDO> entityClass, String columnName) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        addRegionColumn(tableName, columnName);
    }

    public void addRegionColumn(String tableName, String columnName) {
        regionColumns.put(tableName, columnName);
        TABLE_NAMES.add(tableName);
    }

    //代理机构
    public void addAgentColumn(Class<? extends BaseDO> entityClass) {
        addAgentColumn(entityClass, AGENT_COLUMN_NAME);
    }

    public void addAgentColumn(Class<? extends BaseDO> entityClass, String columnName) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        addAgentColumn(tableName, columnName);
    }


    public void addAgentColumn(String tableName, String columnName) {
        agentColumns.put(tableName, columnName);
        TABLE_NAMES.add(tableName);
    }

    public void addTenantColumn(Class<? extends BaseDO> entityClass, String columnName) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        addTenantColumn(tableName, columnName);
    }

    public void addTenantColumn(String tableName, String columnName) {
        agentColumns.put(tableName, columnName);
        TABLE_NAMES.add(tableName);
    }
}
