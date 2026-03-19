package com.yaoan.module.econtract.service.businessroleformfield;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.businessroleformfield.vo.BusinessRoleFormFieldBatchSaveReqVO;
import com.yaoan.module.econtract.controller.admin.businessroleformfield.vo.BusinessRoleFormFieldPageReqVO;
import com.yaoan.module.econtract.controller.admin.businessroleformfield.vo.BusinessRoleFormFieldReqVO;
import com.yaoan.module.econtract.controller.admin.businessroleformfield.vo.BusinessRoleFormFieldSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.businessroleformfield.BusinessRoleFormFieldDO;
import com.yaoan.module.econtract.dal.dataobject.businesstype.BusinessTypeDO;
import com.yaoan.module.econtract.dal.mysql.businessroleformfield.BusinessRoleFormFieldMapper;
import com.yaoan.module.econtract.dal.mysql.businesstype.BusinessTypeMapper;
import com.yaoan.module.system.api.permission.RoleApi;
import com.yaoan.module.system.dal.dataobject.permission.UserRoleDO;
import com.yaoan.module.system.dal.mysql.permission.UserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;


/**
 * 角色字段关系 Service 实现类
 *
 * @author lls
 */
@Service
@Validated
public class BusinessRoleFormFieldServiceImpl implements BusinessRoleFormFieldService {

    @Resource
    private BusinessRoleFormFieldMapper businessRoleFormFieldMapper;

    @Resource
    private BusinessTypeMapper businessTypeMapper;

    @Resource
    private RoleApi roleApi;
    @Resource
    private UserRoleMapper userRoleMapper;


    @Override
    public String createBusinessRoleFormField(BusinessRoleFormFieldSaveReqVO createReqVO) {
        // 插入
        BusinessRoleFormFieldDO businessRoleFormField = BeanUtils.toBean(createReqVO, BusinessRoleFormFieldDO.class);
        businessRoleFormFieldMapper.insert(businessRoleFormField);
        // 返回
        return businessRoleFormField.getId();
    }
    @Override
    public String batchCreateBusinessRoleFormField(BusinessRoleFormFieldBatchSaveReqVO createReqVO) {
        List<BusinessRoleFormFieldSaveReqVO> detailList = createReqVO.getFieldList();
        detailList.forEach(businessRoleFormFieldSaveReqVO -> {
            businessRoleFormFieldSaveReqVO.setBusinessId(createReqVO.getBusinessId());
            businessRoleFormFieldSaveReqVO.setFormId(createReqVO.getFormId());
            businessRoleFormFieldSaveReqVO.setRoleId(createReqVO.getRoleId());
        });
        LambdaQueryWrapperX<BusinessRoleFormFieldDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        lambdaQueryWrapperX.eq(BusinessRoleFormFieldDO::getBusinessId, createReqVO.getBusinessId());
        lambdaQueryWrapperX.eq(BusinessRoleFormFieldDO::getFormId, createReqVO.getFormId());
        lambdaQueryWrapperX.eq(BusinessRoleFormFieldDO::getRoleId, createReqVO.getRoleId());

        businessRoleFormFieldMapper.delete(lambdaQueryWrapperX);
        // 插入
        List<BusinessRoleFormFieldDO> businessRoleFormFieldDOList = BeanUtils.toBean(detailList, BusinessRoleFormFieldDO.class);
        businessRoleFormFieldMapper.insertBatch(businessRoleFormFieldDOList);
        // 返回
        return "true";
    }
    @Override
    public void updateBusinessRoleFormField(BusinessRoleFormFieldSaveReqVO updateReqVO) {
        // 校验存在
        validateBusinessRoleFormFieldExists(updateReqVO.getId());
        // 更新
        BusinessRoleFormFieldDO updateObj = BeanUtils.toBean(updateReqVO, BusinessRoleFormFieldDO.class);
        businessRoleFormFieldMapper.updateById(updateObj);
    }

    @Override
    public void deleteBusinessRoleFormField(String id) {
        // 校验存在
        validateBusinessRoleFormFieldExists(id);
        // 删除
        businessRoleFormFieldMapper.deleteById(id);
    }

    private void validateBusinessRoleFormFieldExists(String id) {
        if (businessRoleFormFieldMapper.selectById(id) == null) {
            throw exception(SYSTEM_ERROR,"数据不存在");
        }
    }

    @Override
    public BusinessRoleFormFieldDO getBusinessRoleFormField(String id) {
        return businessRoleFormFieldMapper.selectById(id);
    }

    @Override
    public PageResult<BusinessRoleFormFieldDO> getBusinessRoleFormFieldPage(BusinessRoleFormFieldPageReqVO pageReqVO) {
        return businessRoleFormFieldMapper.selectPage(pageReqVO);
    }
    @Override
    public List<BusinessRoleFormFieldDO> getFieldsByBusinessTypeAndRole(BusinessRoleFormFieldReqVO reqVO) {
        LambdaQueryWrapperX<BusinessTypeDO> lambdaQuery = new LambdaQueryWrapperX();
        List<BusinessTypeDO> businessTypeDOList =  businessTypeMapper.selectList(lambdaQuery);
        if(businessTypeDOList.isEmpty()){
            return null;
        }
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();

        //RoleRespDTO roles = roleApi.getRole(loginUser.getId());
        List<Long> userIdList = new ArrayList<>();
        userIdList.add(loginUser.getId());
        List<UserRoleDO> userRoleDOList =  userRoleMapper.selectByUserId(loginUser.getId());
        //List<RoleRespDTO> roles = roleApi.getRoleRespDTOByUserIds(userIdList);
        List<Long> roleIds = userRoleDOList.stream().map(UserRoleDO::getRoleId).collect(Collectors.toList());
        LambdaQueryWrapperX<BusinessRoleFormFieldDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        lambdaQueryWrapperX.eq(BusinessRoleFormFieldDO::getBusinessId,businessTypeDOList.get(0).getId());
        lambdaQueryWrapperX.in(BusinessRoleFormFieldDO::getRoleId,roleIds);
        // lambdaQueryWrapperX.eq(BusinessRoleFormFieldDO::getRoleId,reqVO.getRoleId());
        return businessRoleFormFieldMapper.selectList(lambdaQueryWrapperX);
    }
}