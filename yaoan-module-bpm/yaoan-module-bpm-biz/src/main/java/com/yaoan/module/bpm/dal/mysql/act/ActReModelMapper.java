package com.yaoan.module.bpm.dal.mysql.act;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.controller.admin.definition.vo.category.BpmCategoryPageReqVO;
import com.yaoan.module.bpm.dal.dataobject.act.ActReModel;
import com.yaoan.module.bpm.dal.dataobject.definition.BpmCategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * BPM 流程分类 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ActReModelMapper extends BaseMapperX<ActReModel> {
}