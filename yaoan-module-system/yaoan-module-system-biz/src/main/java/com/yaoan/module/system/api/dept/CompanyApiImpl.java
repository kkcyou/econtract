package com.yaoan.module.system.api.dept;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.econtract.api.relative.RelativeApi;
import com.yaoan.module.econtract.api.relative.dto.RelativeContactDTO;
import com.yaoan.module.system.api.dept.dto.CompanyRespDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyAllInfoRespDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyInfoRespDTO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import com.yaoan.module.system.convert.dept.CompanyConvert;
import com.yaoan.module.system.convert.user.UserConvert;
import com.yaoan.module.system.dal.dataobject.dept.CompanyDO;
import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.dal.mysql.dept.CompanyMapper;
import com.yaoan.module.system.service.dept.DeptService;
import com.yaoan.module.system.service.user.AdminUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.infra.enums.ErrorCodeConstants.DIY_ERROR;

/**
 * 单位 API 实现类
 *
 * @author doujl
 */
@Service
public class CompanyApiImpl implements CompanyApi {

    @Resource
    private DeptService deptService;
    @Resource
    private AdminUserService userService;
    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private RelativeApi relativeApi;

    @Override
    @DataPermission(enable = false)
    public CompanyRespDTO getCompany(Long deptId, Integer status) {
        DeptDO deptDO = deptService.getDept(deptId);
        CompanyDO companyDO = companyMapper.selectById(deptDO.getCompanyId());
//        CompanyDO companyDO = companyMapper.selectOne(new LambdaQueryWrapperX<CompanyDO>()
//                .eqIfPresent(CompanyDO::getDeptId, deptId)
//                .eqIfPresent(CompanyDO::getStatus, status)
//        );
        return CompanyConvert.INSTANCE.convert03(companyDO);
    }

    @Override
    public CompanyRespDTO getCompany(String name, String creditCode, Integer status) {
        CompanyDO companyDO = companyMapper.selectOne(new LambdaQueryWrapperX<CompanyDO>()
                .eqIfPresent(CompanyDO::getName, name)
                .eqIfPresent(CompanyDO::getCreditCode, creditCode)
                .eqIfPresent(CompanyDO::getStatus, status)
        );
        return CompanyConvert.INSTANCE.convert03(companyDO);
    }

    @Override
    public List<CompanyRespDTO> getCompanyByIds(List<Long> ids) {
        return CompanyConvert.INSTANCE.convertList03(companyMapper.selectBatchIds(ids));
    }

    @Override
    public CompanyRespDTO getOneById(Long companyId) {
        CompanyDO companyDO = companyMapper.selectById(companyId);
        return CompanyConvert.INSTANCE.convert03(companyDO);
    }

    @Override
    public CompanyRespDTO getCompany4CreditCode(String creditCode) {
        List<CompanyDO> companyDOs = companyMapper.selectList(CompanyDO::getCreditCode,creditCode);
        if(CollectionUtil.isEmpty(companyDOs)){
            return null;
        }
        return CompanyConvert.INSTANCE.convert03(companyDOs.get(0));
    }


    @Override
    public void updateCompanyLeaderUserId(String relativeId, Long leaderUserId) {
        Long loginUserId = WebFrameworkUtils.getLoginUserId();
        CompanyDO companyDO = companyMapper.selectOne(new LambdaQueryWrapperX<CompanyDO>()
                .eq(CompanyDO::getRelativeId, relativeId)
                .eq(CompanyDO::getLeaderUserId, loginUserId)
                .orderByDesc(CompanyDO::getUpdateTime)
                .last(" limit 1"));
        if (ObjectUtil.isEmpty(companyDO)) {
            throw exception(DIY_ERROR, "未查询到该公司下此用户作为管理员的相关信息");
        }
           companyDO.setLeaderUserId(leaderUserId);
            companyMapper.updateById(companyDO);
    }

    @Override
    @DataPermission(enable = false)
    public List<UserCompanyInfoRespDTO> getUserCompanyInfoList(Collection<Long> userIds) {

        List<UserCompanyInfoRespDTO> result = new ArrayList<>();
//        List<DeptDO> deptList = deptService.getDeptList(new DeptListReqVO().setStatus(CommonStatusEnum.ENABLE.getStatus()));
//        Map<Long, DeptDO> deptIdTreeMap = CollectionUtils.convertMap(deptList, DeptDO::getId);//部门集合转map
//        List<CompanyDO> companyDOList = companyMapper.selectList();
//        Map<Long, CompanyDO> companyMap = CollectionUtils.convertMap(companyDOList, CompanyDO::getId);//公司集合转map

        List<AdminUserDO> userList = userService.getUserList(userIds);//获取用户信息集合
        for (AdminUserDO userDO : userList) {

            CompanyDO companyDO = companyMapper.selectById(userDO.getCompanyId());
            if (companyDO != null) {
                UserCompanyInfoRespDTO itemDTO = new UserCompanyInfoRespDTO().setUserId(userDO.getId())
                        .setCreditCode(companyDO.getCreditCode()).setName(companyDO.getName())
                        .setSupplier(companyDO.getSupplier()).setCreateTime(companyDO.getCreateTime());
                result.add(itemDTO);
            }
//            if (userDO.getDeptId() == null) {//如果没有部门添加结果跳过
//                result.add(new UserCompanyInfoRespDTO().setUserId(userDO.getId()));
//                continue;
//            }

//            Long deptId = judgeCompanyDeptId(deptIdTreeMap, userDO.getDeptId());//通过部门id获取部门信息
//            CompanyDO companyDO = companyMap.get(deptId);//通过部门id获取公司信息
//            if (companyDO != null) {
//                UserCompanyInfoRespDTO itemDTO = new UserCompanyInfoRespDTO().setUserId(userDO.getId())
//                        .setCreditCode(companyDO.getCreditCode()).setName(companyDO.getName())
//                        .setSupplier(companyDO.getSupplier()).setCreateTime(companyDO.getCreateTime());
//                result.add(itemDTO);
//            }
        }
        return result;
    }
    @Override
    @DataPermission(enable = false)
    public List<UserCompanyInfoRespDTO> getUserCompanyInfo(List<Long> userIds) {

        List<UserCompanyInfoRespDTO> result = new ArrayList<>();
        List<AdminUserDO> userList = userService.getUserList(userIds);//获取用户信息集合
       //根据用户获取相对方信息
        List<RelativeContactDTO> relativeContactByUserIds = relativeApi.getRelativeContactByUserIds(userIds);
        Map<Long, RelativeContactDTO> relativeContactDTOMap = CollectionUtils.convertMap(relativeContactByUserIds, RelativeContactDTO::getUserId);
        Set<Long> companyIds = userList.stream().filter(userDO ->  userDO.getCompanyId()!=null&&userDO.getCompanyId()!=0).map(AdminUserDO::getCompanyId).collect(Collectors.toSet());
        Set<Long> companyIds2 = relativeContactByUserIds.stream().filter(relativeContact -> relativeContact.getCompanyId() != null).map(RelativeContactDTO::getCompanyId).collect(Collectors.toSet());
        companyIds.addAll(companyIds2);
        List<CompanyDO> companyDOS =CollectionUtil.isEmpty(companyIds)?null: companyMapper.selectList(new LambdaQueryWrapperX<CompanyDO>().in(CompanyDO::getId, companyIds));
        Map<Long, CompanyDO> companyDOMap = CollectionUtils.convertMap(companyDOS, CompanyDO::getId);
        for (AdminUserDO userDO : userList) {
            Long companyId = (ObjectUtil.isEmpty(userDO.getCompanyId())||userDO.getCompanyId()==0)&&ObjectUtil.isNotEmpty(relativeContactDTOMap)&&ObjectUtil.isNotEmpty(relativeContactDTOMap.get(userDO.getId()))?relativeContactDTOMap.get(userDO.getId()).getCompanyId():userDO.getCompanyId();
            CompanyDO companyDO = ObjectUtil.isEmpty(companyDOMap)? null: companyDOMap.get(companyId);
            if (companyDO != null) {
                UserCompanyInfoRespDTO itemDTO = new UserCompanyInfoRespDTO()
                        .setUserId(userDO.getId()).setUserName(userDO.getNickname())
                        .setCreditCode(companyDO.getCreditCode()).setName(companyDO.getName())
                        .setSupplier(companyDO.getSupplier()).setCreateTime(companyDO.getCreateTime());
                result.add(itemDTO);
            }
        }
        return result;
    }

    public Long judgeCompanyDeptId(Map<Long, DeptDO> deptIdTreeMap, Long deptId) {
        DeptDO deptDO = deptIdTreeMap.get(deptId);
        if (deptDO == null) {
            return 0L;
        }
        if (deptDO.getParentId() == 0) {
            return deptDO.getId();
        }
        return judgeCompanyDeptId(deptIdTreeMap, deptDO.getParentId());
    }


    @Override
    @DataPermission(enable = false)
    public List<UserCompanyAllInfoRespDTO> getUserCompanyAllInfoList(Collection<Long> userIds) {

        List<UserCompanyAllInfoRespDTO> result = new ArrayList<>();
//        List<DeptDO> deptList = deptService.getDeptList(new DeptListReqVO().setStatus(CommonStatusEnum.ENABLE.getStatus()));
//        Map<Long, DeptDO> deptIdTreeMap = CollectionUtils.convertMap(deptList, DeptDO::getId);
//        List<CompanyDO> companyDOList = companyMapper.selectList();
//        Map<Long, CompanyDO> companyMap = CollectionUtils.convertMap(companyDOList, CompanyDO::getDeptId);

        List<AdminUserDO> userList = userService.getUserList(userIds);
        for (AdminUserDO userDO : userList) {

//            if (userDO.getDeptId() == null) {
//                result.add(new UserCompanyAllInfoRespDTO().setUserId(userDO.getId()));
//                continue;
//            }

//            Long deptId = judgeCompanyDeptId(deptIdTreeMap, userDO.getDeptId());
//            CompanyDO companyDO = companyMap.get(deptId);
            CompanyDO companyDO = companyMapper.selectById(userDO.getCompanyId());
            if (companyDO != null) {
                UserCompanyAllInfoRespDTO itemDTO = UserConvert.INSTANCE.convert2CompanyDTO(companyDO);
                itemDTO.setUserId(userDO.getId())
                        .setCreditCode(companyDO.getCreditCode()).setName(companyDO.getName())
                        .setSupplier(companyDO.getSupplier()).setCreateTime(companyDO.getCreateTime());
                result.add(itemDTO);
            }
        }
        return result;
    }


}
