package com.yaoan.module.econtract.dal.mysql.businessfile;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.businessfile.vo.BusinessFilePageReqVO;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 业务数据和附件关联关系 Mapper
 *
 * @author lls
 */
@Mapper
public interface BusinessFileMapper extends BaseMapperX<BusinessFileDO> {

    default PageResult<BusinessFileDO> selectPage(BusinessFilePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BusinessFileDO>()
                .eqIfPresent(BusinessFileDO::getBusinessId, reqVO.getBusinessId())
                .eqIfPresent(BusinessFileDO::getFileId, reqVO.getFileId())
                .betweenIfPresent(BusinessFileDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BusinessFileDO::getId));
    }
    
    default Integer deleteByBusinessId(String businessId) {
        LambdaQueryWrapperX<BusinessFileDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        lambdaQueryWrapperX.eq(BusinessFileDO::getBusinessId, businessId);
        return  delete(lambdaQueryWrapperX);
    }

    default List<BusinessFileDO> selectByBusinessId(String businessId) {
        LambdaQueryWrapperX<BusinessFileDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        lambdaQueryWrapperX.eq(BusinessFileDO::getBusinessId, businessId);
        return  selectList(lambdaQueryWrapperX);
    }
}