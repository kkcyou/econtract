package com.yaoan.module.econtract.dal.mysql.businessformfield;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.businessformfield.vo.BusinessFormFieldPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.businessformfield.BusinessFormFieldDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * 表单字段 Mapper
 *
 * @author lls
 */
@Mapper
public interface BusinessFormFieldMapper extends BaseMapperX<BusinessFormFieldDO> {

    default PageResult<BusinessFormFieldDO> selectPage(BusinessFormFieldPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BusinessFormFieldDO>()
                .eqIfPresent(BusinessFormFieldDO::getFieldCode, reqVO.getFieldCode())
                .likeIfPresent(BusinessFormFieldDO::getFieldName, reqVO.getFieldName())
                .eqIfPresent(BusinessFormFieldDO::getFormId, reqVO.getFormId())
                .eqIfPresent(BusinessFormFieldDO::getBusinessId, reqVO.getBusinessId())
                .eqIfPresent(BusinessFormFieldDO::getIsShow, reqVO.getIsShow())
                .eqIfPresent(BusinessFormFieldDO::getIsSearch, reqVO.getIsSearch())
                .orderByDesc(BusinessFormFieldDO::getId));
    }

}