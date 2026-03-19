package com.yaoan.module.econtract.api.bpm.relative;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.contract.dto.UserDTO;
import com.yaoan.module.econtract.convert.relative.RelativeConverter;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.relativeContact.RelativeContact;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.relativeContact.RelativeContactMapper;
import com.yaoan.module.econtract.enums.relative.RelativeStatusV2Enums;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.controller.admin.dept.vo.company.CompanyCreateReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.company.CompanyRespVO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.service.dept.CompanyService;
import com.yaoan.module.system.service.dept.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;

/**
 * @description:
 * @author: Pele
 * @date: 2025/3/12 14:20
 */
@Slf4j
@Service
public class RelativeBpmApiImpl implements RelativeBpmApi {

    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private RelativeContactMapper relativeContactMapper;
    @Resource
    private CompanyService companyService;
    @Resource
    private DeptService deptService;
    @Autowired
    private AdminUserApi adminUserApi;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) {

        Relative relative = relativeMapper.selectById(businessKey);
        if (ObjectUtil.isNull(relative)) {
            log.error("{}相对方不存在", businessKey);
            throw exception(DIY_ERROR,"请检查是否已填写完整业务信息" );
        }

        switch (statusEnums) {
            case APPROVE:
                relative.setResult(BpmProcessInstanceResultEnum.APPROVE.getResult());
                relative.setStatus(RelativeStatusV2Enums.NORMAL.getCode());
                relative.setApproveTime(LocalDateTime.now());
                // 相对方终审后，创建公司
                List<RelativeContact> relativeContactList = relativeContactMapper.selectList(new LambdaQueryWrapperX<RelativeContact>()
                        .eq(RelativeContact::getRelativeId, relative.getId()).orderByDesc(RelativeContact::getContactTel));
                if (CollectionUtil.isEmpty(relativeContactList)) {
                    log.error("相对方联系人不存在，请维护相应的联系人信息后再试。");
                    throw exception(DIY_ERROR, "请添加联系人后再试。");
                }
                RelativeContact relativeContact1 = relativeContactList.get(0);
                CompanyCreateReqVO companyCreateReqVO = new CompanyCreateReqVO();
                companyCreateReqVO.setUsername(relativeContact1.getContactTel());
                companyCreateReqVO.setNickname(relativeContact1.getName());
                companyCreateReqVO.setPassword("12345678");
                companyCreateReqVO.setName(relative.getName());
                companyCreateReqVO.setCreditCode(relative.getCardNo());
                companyCreateReqVO.setSupplier(relative.getRelativeType());
                companyCreateReqVO.setSort(1);
                companyCreateReqVO.setPhone(relative.getContactTel());

                companyCreateReqVO.setEmail(relative.getEmail());
                companyCreateReqVO.setStatus(0);
                companyCreateReqVO.setMajor(Integer.valueOf(relative.getEntityType()));
                Long relativeCompanyId = companyService.createSupplyCompany(companyCreateReqVO.setRelativeId(businessKey));
                CompanyRespVO company = companyService.getCompany(relativeCompanyId);
                relative.setContactId(company.getLeaderUserId());
                log.info("【相对方激活】相对方{}创建了公司{}", relative.getId(), company.getId());
                //创建部门
                Long deptId = createDept4Company(company);
                //部门赋值给用户
                enhanceUsers(relativeCompanyId,deptId);
                break;
            case DRAFT:
                //退回的到草稿
                if (RelativeStatusV2Enums.APPROVING.getCode().equals(relative.getStatus())) {
                    relative.setStatus(RelativeStatusV2Enums.DRAFTING.getCode());
                    relative.setResult(BpmProcessInstanceResultEnum.BACK.getResult());
                }
                break;
            case PROCESS:
                relative.setStatus(RelativeStatusV2Enums.APPROVING.getCode());
                relative.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());

            default:
                break;
        }

        relativeMapper.updateById(relative);
    }
    private void enhanceUsers(Long companyId, Long deptId) {
        List<UserDTO> userDTOList = new ArrayList<>();
        List<AdminUserRespDTO> userRespDTOList = adminUserApi.selectListByCompanyIdsAndRoleIds(new ArrayList<>(Collections.singletonList(companyId)),null);
        for (AdminUserRespDTO userRespDTO : userRespDTOList) {
            UserDTO userDTO = new UserDTO().setId(userRespDTO.getId()).setDeptId(deptId);
            userDTOList.add(userDTO);
        }
        adminUserApi.updateUsers(userDTOList);
    }

    private Long createDept4Company(CompanyRespVO company) {
        DeptCreateReqVO deptCreateReqVO = new DeptCreateReqVO();
        deptCreateReqVO
                .setName(company.getName())
                .setPhone(company.getPhone())
                .setSort(1)
                .setEmail(company.getEmail())
                .setStatus(1)
                .setCompanyId(company.getId())
        ;
        return deptService.createDept(deptCreateReqVO, company.getTenantId());
    }

    private List<Long> createUsers(List<RelativeContact> relativeContactList, CompanyRespVO company) {
        List<UserDTO> userDTOList = new ArrayList<>();
        for (RelativeContact relativeContact : relativeContactList) {
            UserDTO userDTO = new UserDTO();
            //默认供应商公司
            userDTO.setType(4);
            userDTO.setMobile(relativeContact.getContactTel());
            userDTO.setUsername(relativeContact.getName());
            userDTO.setNickname(relativeContact.getName());
            userDTO.setPassword(passwordEncoder.encode("12345678"));
            userDTO.setEmail(relativeContact.getEmail());
            userDTO.setCompanyId(company.getId());
            userDTO.setTenantId(relativeContact.getTenantId());
            userDTOList.add(userDTO);
        }
        return adminUserApi.saveBatch(userDTOList);
    }

}
