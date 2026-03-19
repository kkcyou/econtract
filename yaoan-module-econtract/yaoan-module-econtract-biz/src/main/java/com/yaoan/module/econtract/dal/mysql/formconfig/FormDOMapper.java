package com.yaoan.module.econtract.dal.mysql.formconfig;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormBusinessRelDO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 表单表 Mapper 接口
 * </p>
 *
 * @author Pele
 * @since 2024-03-18
 */
@Mapper
public interface FormDOMapper extends BaseMapperX<FormDO> {

    default List<FormDO> getFormBusinessByBusinessId(String businessId) {
        MPJLambdaWrapper<FormDO> mpjLambdaWrapper = new MPJLambdaWrapper<FormDO>()
                .selectAll(FormDO.class).orderByDesc(FormDO::getCreateTime)
                .leftJoin(FormBusinessRelDO.class, FormBusinessRelDO::getFormId, FormDO::getId)
                .eq(FormBusinessRelDO::getBusinessId, businessId)
                .orderByDesc(FormDO::getUpdateTime);
        return selectList(mpjLambdaWrapper);
    }

    default PageResult<FormDO> selectPage(FormSaveReqVO vo) {
        return selectPage(vo, null);
    }

    default List<FormDO> listFormByBizId(String bizId){
        MPJLambdaWrapper<FormDO> mpjLambdaWrapper = new MPJLambdaWrapper<FormDO>()
                .selectAll(FormDO.class).orderByDesc(FormDO::getCreateTime)
                .leftJoin(FormBusinessRelDO.class, FormBusinessRelDO::getFormId, FormDO::getId)
                .eq(FormBusinessRelDO::getBusinessId, bizId)
                .orderByAsc(FormBusinessRelDO::getSort)
                ;
        return selectList(mpjLambdaWrapper);
    }
}
