package com.yaoan.module.econtract.dal.mysql.agreement;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.dal.dataobject.agreement.PrefAgreementRelDO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface PrefAgreementRelMapper extends BaseMapperX<PrefAgreementRelDO> {
    /**
     * 校验名称是否存在
     * @param id
     * @param name
     * @return
     */
    default Boolean nameExist(String id, String name){
        return selectCount(new LambdaQueryWrapperX<PrefAgreementRelDO>()
                .eq(PrefAgreementRelDO::getFileName, name)
                .ne(PrefAgreementRelDO::getId,id))>0;
    }
}
