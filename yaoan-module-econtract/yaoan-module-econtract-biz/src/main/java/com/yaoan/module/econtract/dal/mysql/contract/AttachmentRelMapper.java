package com.yaoan.module.econtract.dal.mysql.contract;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.contract.AttachmentRelDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AttachmentRelMapper extends BaseMapperX<AttachmentRelDO> {
    default List<AttachmentRelDO> selectByContractId(String id){
        return selectList();
    };
}
