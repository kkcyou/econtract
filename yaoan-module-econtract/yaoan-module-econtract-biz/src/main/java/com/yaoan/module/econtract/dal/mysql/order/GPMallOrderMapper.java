package com.yaoan.module.econtract.dal.mysql.order;


import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.DraftOrderInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/4 11:40
 */
@Mapper
public interface GPMallOrderMapper extends BaseMapperX<DraftOrderInfoDO> {

}
