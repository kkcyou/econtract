package com.yaoan.module.econtract.dal.mysql.businessroleformfield;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.businessroleformfield.vo.BusinessRoleFormFieldPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.businessroleformfield.BusinessRoleFormFieldDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色字段关系 Mapper
 *
 * @author lls
 */
@Mapper
public interface BusinessRoleFormFieldMapper extends BaseMapperX<BusinessRoleFormFieldDO> {

    default PageResult<BusinessRoleFormFieldDO> selectPage(BusinessRoleFormFieldPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BusinessRoleFormFieldDO>()
                .eqIfPresent(BusinessRoleFormFieldDO::getRoleId, reqVO.getRoleId())
                .eqIfPresent(BusinessRoleFormFieldDO::getFieldCode, reqVO.getFieldCode())
                .likeIfPresent(BusinessRoleFormFieldDO::getFieldName, reqVO.getFieldName())
                .eqIfPresent(BusinessRoleFormFieldDO::getFieldId, reqVO.getFieldId())
                .eqIfPresent(BusinessRoleFormFieldDO::getFormId, reqVO.getFormId())
                .eqIfPresent(BusinessRoleFormFieldDO::getBusinessId, reqVO.getBusinessId())
                .eqIfPresent(BusinessRoleFormFieldDO::getIsShow, reqVO.getIsShow())
                .orderByDesc(BusinessRoleFormFieldDO::getId));
    }

}