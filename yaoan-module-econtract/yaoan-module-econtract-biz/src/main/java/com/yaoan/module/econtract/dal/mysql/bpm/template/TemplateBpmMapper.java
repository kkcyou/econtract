package com.yaoan.module.econtract.dal.mysql.bpm.template;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.bpm.template.vo.TemplateBpmPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.performance.suspend.BpmPerformance;
import com.yaoan.module.econtract.dal.dataobject.bpm.template.TemplateBpmDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/14 15:22
 */
@Mapper
public interface TemplateBpmMapper extends BaseMapperX<TemplateBpmDO> {

}