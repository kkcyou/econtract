package com.yaoan.module.econtract.api.bpm.company;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.contract.dto.UserDTO;
import com.yaoan.module.econtract.dal.dataobject.bpm.saas.company.CompanyBpmDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.relativeContact.RelativeContact;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.relativeContact.RelativeContactMapper;
import com.yaoan.module.econtract.dal.mysql.saas.company.CompanyBpmMapper;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.api.dept.dto.CompanyRespDTO;
import com.yaoan.module.system.api.permission.PermissionApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.controller.admin.dept.vo.company.CompanyRespVO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.dal.mysql.dept.DeptMapper;
import com.yaoan.module.system.dal.mysql.permission.RoleMapper;
import com.yaoan.module.system.enums.permission.RoleCodeEnum;
import com.yaoan.module.system.service.dept.CompanyService;
import com.yaoan.module.system.service.dept.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DATA_ERROR;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-29 11:04
 */
@Service
@Slf4j
public class BpmCompanyApiImpl implements BpmCompanyApi {
    @Resource
    private CompanyBpmMapper companyBpmMapper;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private RelativeContactMapper relativeContactMapper;
    @Resource
    private RelativeMapper relativeMapper;
    @Autowired
    private AdminUserApi adminUserApi;
    @Resource
    private PermissionApi permissionApi;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Long> calculateUsers4SaasCompanyExpression(String bpmId) {
        CompanyBpmDO companyBpmDO = companyBpmMapper.selectById(bpmId);
        if (ObjectUtil.isNull(companyBpmDO)) {
            return Collections.emptyList();
        }
        CompanyRespDTO companyDTO = companyApi.getOneById(companyBpmDO.getCompanyId());
        if (ObjectUtil.isEmpty(companyDTO) || ObjectUtil.isNull(companyDTO.getRelativeId())) {
            return Collections.emptyList();
        }
        Relative relative = relativeMapper.selectById(companyDTO.getRelativeId());
        if (ObjectUtil.isNull(relative)) {
            return Collections.emptyList();
        }
        return Arrays.asList(relative.getVirtualId());
    }

    @Resource
    private DeptService deptService;
    @Resource
    private CompanyService companyService;
    @Resource
    private DeptMapper deptMapper;
    @Override
    public void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum instance) {
        CompanyBpmDO companyBpmDO = companyBpmMapper.selectById(businessKey);
        if (ObjectUtil.isNull(companyBpmDO)) {
            throw exception(DATA_ERROR);
        }
        Long companyId = companyBpmDO.getCompanyId();
        CompanyRespDTO companyDTO = companyApi.getOneById(companyId);
        if (ObjectUtil.isEmpty(companyDTO) || ObjectUtil.isNull(companyDTO.getLeaderUserId())) {
            throw exception(DATA_ERROR);
        }
        switch (instance) {
            case APPROVE:
               // select the deptId
                Long deptId = deptMapper.selectList(DeptDO::getCompanyId, Collections.singletonList(companyId)).get(0).getId();
                //部门赋值给用户
                enhanceUsersSaas(companyId, deptId);
                //申请人关联上公司
                RelativeContact relativeContact = new RelativeContact();
                relativeContact.setCompanyId(companyBpmDO.getCompanyId());
                relativeContact.setUserId(Long.valueOf(companyBpmDO.getCreator()));
                relativeContact.setRelativeId(companyDTO.getRelativeId());
                relativeContact.setDeptId(deptId);
                relativeContactMapper.insert(relativeContact);

                //分配角色
                List<RoleDO> roleDOS = roleMapper.selectList(RoleDO::getCode, RoleCodeEnum.SAAS_HANDLER.getCode());
                log.error("APP经办人角色缺失！");
                if (CollectionUtil.isEmpty(roleDOS)) {
                    throw exception(DATA_ERROR);
                }
                permissionApi.assignUserRole4Saas(Long.valueOf(companyBpmDO.getCreator()), Collections.singleton(roleDOS.get(0).getId()), relativeContact.getRelativeId());
                // 发通过短信通知

                break;
            case REJECT:
                // 发拒绝短信通知
                break;
            default:
                break;
        }

        //申请信息更新
        companyBpmDO.setResult(instance.getResult());
        companyBpmDO.setApproveDate(LocalDateTime.now());
        companyBpmMapper.updateById(companyBpmDO);
    }



    private void enhanceUsers(Long companyId, Long deptId) {
        List<UserDTO> userDTOList = new ArrayList<>();
        List<AdminUserRespDTO> userRespDTOList = adminUserApi.selectListByCompanyIdsAndRoleIds(new ArrayList<>(Collections.singletonList(companyId)), null);
        for (AdminUserRespDTO userRespDTO : userRespDTOList) {
            UserDTO userDTO = new UserDTO().setId(userRespDTO.getId()).setDeptId(deptId);
            userDTOList.add(userDTO);
        }
        adminUserApi.updateUsers(userDTOList);
    }

    /**
     * assign specific deptId to users with the companyId
     */
    private void enhanceUsersSaas(Long companyId, Long deptId) {
        List<UserDTO> userDTOList = new ArrayList<>();
        List<AdminUserRespDTO> userRespDTOList = adminUserApi.selectListByCompanyIdsSaas(new ArrayList<>(Collections.singletonList(companyId)));
        List<RelativeContact> relativeContactList = new ArrayList<>();
        for (AdminUserRespDTO userRespDTO : userRespDTOList) {
            RelativeContact relativeContact = new RelativeContact();
            relativeContact.setCompanyId(companyId);
            relativeContact.setUserId(Long.valueOf(userRespDTO.getId()));
            relativeContact.setDeptId(deptId);
            relativeContactList.add(relativeContact);
        }
        relativeContactMapper.updateBatch(relativeContactList);
    }
}
