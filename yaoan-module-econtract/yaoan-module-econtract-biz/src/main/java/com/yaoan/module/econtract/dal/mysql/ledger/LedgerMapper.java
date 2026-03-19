package com.yaoan.module.econtract.dal.mysql.ledger;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.ledger.vo.LedgerListReqVo;
import com.yaoan.module.econtract.dal.dataobject.ledger.Ledger;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Pele
 * @description 针对表【ecms_ledger(台账表)】的数据库操作Mapper
 * @Entity Ledger
 */
@Mapper
public interface LedgerMapper extends BaseMapperX<Ledger> {
    default PageResult<Ledger> selectPage(LedgerListReqVo vo) {
        return selectPage(vo, new LambdaQueryWrapperX<Ledger>()
                .likeIfPresent(Ledger::getName, vo.getName())
                .eqIfPresent(Ledger::getContractType, vo.getContractType())
                .eqIfPresent(Ledger::getContractStatus, vo.getContractStatus())
                .eqIfPresent(Ledger::getSignType, vo.getSignType())
                .likeIfPresent(Ledger::getCounterparty, vo.getCounterparty())
                .betweenIfPresent(Ledger::getSignTime,vo.getSignTimeStart(),vo.getSignTimeEnd())
                .betweenIfPresent(Ledger::getFilingTime,vo.getFilingTimeStart(),vo.getFilingTimeEnd())

        );
    }

}




