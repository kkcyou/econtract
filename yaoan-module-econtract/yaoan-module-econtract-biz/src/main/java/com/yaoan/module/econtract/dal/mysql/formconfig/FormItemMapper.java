package com.yaoan.module.econtract.dal.mysql.formconfig;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormBusinessRelDO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormDO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 表单项目表 Mapper 接口
 * </p>
 *
 * @author Pele
 * @since 2024-03-18
 */
@Mapper
public interface FormItemMapper extends BaseMapperX<FormItemDO> {

    default List<FormItemDO> selectOldItems(String businessId) {
        MPJLambdaWrapper<FormItemDO> mpjLambdaWrapper = new MPJLambdaWrapper<FormItemDO>()
                .selectAll(FormItemDO.class)
                .leftJoin(FormDO.class, FormDO::getId, FormItemDO::getFormId)
                .leftJoin(FormBusinessRelDO.class, FormBusinessRelDO::getFormId, FormItemDO::getFormId)
                .eq(FormBusinessRelDO::getBusinessId, businessId)
                .orderByDesc(FormItemDO::getCreateTime);
        return selectList(mpjLambdaWrapper);
    }

    default PageResult<FormItemDO> list(FormSaveReqVO vo) {
        return selectPage(vo, new LambdaQueryWrapperX<FormItemDO>().orderByDesc(FormItemDO::getUpdateTime));
    }

    default List<FormItemDO> getItemsByFormId(String formId) {
        return selectList(new LambdaQueryWrapper<FormItemDO>()
                .eq(FormItemDO::getFormId, formId).orderByAsc(FormItemDO::getSort)
        );
    }

    /**
     * 将formId相关的表项关联的formId都清空
     */
    default void cleanByFormId(FormItemDO formItemDO, String formId) {
        update(formItemDO, new LambdaQueryWrapperX<FormItemDO>().eq(FormItemDO::getFormId, formId));
    }
}
