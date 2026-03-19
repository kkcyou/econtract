package com.yaoan.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.query.QueryWrapperX;
import com.yaoan.module.econtract.api.relative.dto.RelativeContactDTO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptUpdateReqVO;
import com.yaoan.module.system.convert.dept.DeptConvert;
import com.yaoan.module.system.dal.dataobject.dept.CompanyDO;
import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import com.yaoan.module.system.dal.mysql.dept.CompanyMapper;
import com.yaoan.module.system.dal.mysql.dept.DeptMapper;
import com.yaoan.module.system.dal.redis.RedisKeyConstants;
import com.yaoan.module.system.enums.dept.DeptIdEnum;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.common.util.collection.CollectionUtils.convertSet;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;
import static com.yaoan.module.infra.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.system.enums.ErrorCodeConstants.*;

/**
 * 部门 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class DeptServiceImpl implements DeptService {

    @Resource
    private DeptMapper deptMapper;

    @Resource
    private CompanyMapper companyMapper;

    //    @Resource
//    private
    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST,
            allEntries = true) // allEntries 清空所有缓存，因为操作一个部门，涉及到多个缓存
    public Long createDept(DeptCreateReqVO reqVO) {
        // 校验正确性
        if (reqVO.getParentId() == null) {
            reqVO.setParentId(DeptIdEnum.ROOT.getId());
        }
        if(reqVO.getCompanyId() == null || reqVO.getCompanyId() == 0){
            Long parentId = reqVO.getParentId();
            DeptDO parentDO = deptMapper.selectById(parentId);
            //如果parentDO为空，说明部门表找不到该父节点，此时父节点在公司表中，parentId=companyId
            if(parentDO == null){
                reqVO.setCompanyId(parentId);
            }else{
                reqVO.setCompanyId(parentDO.getCompanyId());
            }
        }
        Long companyId = reqVO.getCompanyId() != null && reqVO.getCompanyId() > 0 ? reqVO.getCompanyId() : getLoginUser().getCompanyId();
        validateForCreateOrUpdate(null, reqVO.getParentId(),
                companyId,
                reqVO.getName());
        //添加所属公司
        reqVO.setCompanyId(companyId);
        // 插入部门
        DeptDO dept = DeptConvert.INSTANCE.convert(reqVO);
        deptMapper.insert(dept);
        return dept.getId();
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST,
            allEntries = true) // allEntries 清空所有缓存，因为操作一个部门，涉及到多个缓存
    public Long createDept(DeptCreateReqVO reqVO, Long tenantId) {
        // 校验正确性
        if (reqVO.getParentId() == null) {
            reqVO.setParentId(DeptIdEnum.ROOT.getId());
        }
        if(reqVO.getCompanyId() == null || reqVO.getCompanyId() == 0){
            log.error("公司id不能为空，请确认是否已选择公司信息。");
            throw exception(DIY_ERROR,"请确认是否已选择公司信息");
        }
        validateForCreateOrUpdate(null, reqVO.getParentId(),
                reqVO.getCompanyId(),
                reqVO.getName());
        // 插入部门
        DeptDO dept = DeptConvert.INSTANCE.convert(reqVO);
        dept.setTenantId(tenantId);
        deptMapper.insert(dept);
        return dept.getId();
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST,
            allEntries = true) // allEntries 清空所有缓存，因为操作一个部门，涉及到多个缓存
    public void updateDept(DeptUpdateReqVO reqVO) {
        // 校验正确性 parentId 无作用不需要检验
//        if (reqVO.getParentId() == null) {
//            reqVO.setParentId(DeptIdEnum.ROOT.getId());
//        }
        DeptDO deptDO = deptMapper.selectById(reqVO.getId());
        if (deptDO == null) {
            throw exception(DEPT_NOT_FOUND);
        }
//        Long companyId = getLoginUser().getCompanyId();//获取当前登录人
//        if (deptDO.getCompanyId() != companyId) {
//            throw exception(DEPT_NOT_HANDLE);
//        }
        validateForCreateOrUpdate(reqVO.getId(), reqVO.getParentId(), deptDO.getCompanyId(), reqVO.getName());
        // 更新部门
        DeptDO updateObj = DeptConvert.INSTANCE.convert(reqVO);
        deptMapper.updateById(updateObj);
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST,
            allEntries = true) // allEntries 清空所有缓存，因为操作一个部门，涉及到多个缓存
    public void deleteDept(Long id) {
        // 校验是否存在
        validateDeptExists(id);
        // 校验是否有子部门
        if (deptMapper.selectCountByParentId(id) > 0) {
            throw exception(DEPT_EXITS_CHILDREN);
        }
        // 删除部门
        deptMapper.deleteById(id);
    }

    private void validateForCreateOrUpdate(Long id, Long parentId, Long companyId, String name) {
        // 校验自己存在
        validateDeptExists(id);
        // 校验父部门的有效性 所有部门统一被公司所管理
        validateParentDept(id, parentId);
        // 校验同一公司部门名的唯一性
        validateDeptNameUnique(id, companyId, name);
    }

    @VisibleForTesting
    void validateDeptExists(Long id) {
        if (id == null) {
            return;
        }
        DeptDO dept = deptMapper.selectById(id);
        if (dept == null) {
            throw exception(DEPT_NOT_FOUND);
        }
    }

    @VisibleForTesting
    void validateParentDept(Long id, Long parentId) {
        if (parentId == null || DeptIdEnum.ROOT.getId().equals(parentId)) {
            return;
        }
        // 不能设置自己为父部门
        if (parentId.equals(id)) {
            throw exception(DEPT_PARENT_ERROR);
        }
        // 父岗位不存在
        DeptDO dept = deptMapper.selectById(parentId);
        //父岗位可能是公司
        CompanyDO companyDO = companyMapper.selectById(parentId);
        if (dept == null && companyDO == null) {
            throw exception(DEPT_PARENT_NOT_EXITS);
        }
        // 父部门不能是原来的子部门
        List<DeptDO> children = getChildDeptList(id);
        if (children.stream().anyMatch(dept1 -> dept1.getId().equals(parentId))) {
            throw exception(DEPT_PARENT_IS_CHILD);
        }
    }

    @VisibleForTesting
    void validateDeptNameUnique(Long id, Long companyId, String name) {
        DeptDO dept = deptMapper.selectByCompanyIdAndName(companyId, name);
        if (dept == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw exception(DEPT_NAME_DUPLICATE);
        }
        if (ObjectUtil.notEqual(dept.getId(), id)) {
            throw exception(DEPT_NAME_DUPLICATE);
        }
    }

    @Override
    public DeptDO getDept(Long id) {
        return deptMapper.selectById(id);
    }

    @Override
    public DeptDO getDeptByParentId(Long parentId) {
        return deptMapper.selectOne(DeptDO::getParentId, 0);
    }

    @Override
    public List<DeptDO> getDeptList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return deptMapper.selectBatchIds(ids);
    }

    @Override
    public List<DeptDO> getDeptList() {

        return deptMapper.selectList();
    }

    @Override
    public List<DeptDO> getDeptList(DeptListReqVO reqVO) {
        //暂时添加一层公司1-超管 2-租户管理员  3-其他用户
        //userType=3的话，只能看本公司的
        //部门只存在公司下
        QueryWrapperX<CompanyDO> wrapperX = new QueryWrapperX<>();
        wrapperX.eq(reqVO.getUserType() != null && reqVO.getUserType() == 3, "id", getLoginUser().getCompanyId());
        List<CompanyDO> companyList = companyMapper.selectList(wrapperX);
        //公司类型转换为部门 根据公司查询部门
        ArrayList<DeptDO> list = new ArrayList<>();
        companyList.forEach(companyDO -> {
            DeptDO deptDO = DeptConvert.INSTANCE.company2Dept(companyDO);
            deptDO.setParentId(0L);
            list.add(deptDO);
            List<DeptDO> deptDOS = deptMapper.selectList(DeptDO::getCompanyId, deptDO.getCompanyId());
            //顶级部门指向公司
            for (DeptDO dept : deptDOS) {
                if (dept.getParentId() == 0) {
                    dept.setParentId(deptDO.getId());
                }
            }
            list.addAll(deptDOS);
        });
//        return deptMapper.selectList(reqVO);
        return list;
    }
    @Override
    public List<DeptDO> getListAllSimpleBy( List<RelativeContactDTO> relativeContactDTOS ) {
        //根据相对方ID获取到公司ID
        Set<Long> companyIds = relativeContactDTOS.stream().filter(relativeContactDTO -> relativeContactDTO.getCompanyId() != null)
                .map(relativeContactDTO -> relativeContactDTO.getCompanyId()).collect(Collectors.toSet());
            List<DeptDO> deptDOS = deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().in(DeptDO::getCompanyId, companyIds)
                    .eq(DeptDO::getStatus, CommonStatusEnum.ENABLE.getStatus()));
        return deptDOS;
    }

    @Override
    public List<DeptDO> getParentDeptList() {
        List<DeptDO> deptDOS = deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().eqIfPresent(DeptDO::getParentId, 0L));
        return deptDOS;
    }

    @Override
    public List<DeptDO> getChildDeptList(Long id) {
        List<DeptDO> children = new LinkedList<>();
        // 遍历每一层
        Collection<Long> parentIds = Collections.singleton(id);
        for (int i = 0; i < Short.MAX_VALUE; i++) { // 使用 Short.MAX_VALUE 避免 bug 场景下，存在死循环
            // 查询当前层，所有的子部门
            List<DeptDO> depts = deptMapper.selectListByParentId(parentIds);
            // 1. 如果没有子部门，则结束遍历
            if (CollUtil.isEmpty(depts)) {
                break;
            }
            // 2. 如果有子部门，继续遍历
            children.addAll(depts);
            parentIds = convertSet(depts, DeptDO::getId);
        }
        return children;
    }

    @Override
    @DataPermission(enable = false) // 禁用数据权限，避免简历不正确的缓存
    @Cacheable(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST, key = "#id")
    public Set<Long> getChildDeptIdListFromCache(Long id) {
        List<DeptDO> children = getChildDeptList(id);
        return convertSet(children, DeptDO::getId);
    }

    @Override
    public void validateDeptList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得科室信息
        Map<Long, DeptDO> deptMap = getDeptMap(ids);
        // 校验
        ids.forEach(id -> {
            DeptDO dept = deptMap.get(id);
            if (dept == null) {
                CompanyDO companyDO = companyMapper.selectById(id);
                if (companyDO == null) {
                    throw exception(DEPT_NOT_FOUND);
                }
                if (!CommonStatusEnum.ENABLE.getStatus().equals(companyDO.getStatus())) {
                    throw exception(DEPT_NOT_ENABLE, dept.getName());
                }
                return;
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(dept.getStatus())) {
                throw exception(DEPT_NOT_ENABLE, dept.getName());
            }
        });
    }

    @Override
    public List<DeptDO> getDeptListByCompanyId(Long companyId) {
        List<DeptDO> deptDOS = deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().eqIfPresent(DeptDO::getCompanyId, companyId));
        return deptDOS;
    }

}
