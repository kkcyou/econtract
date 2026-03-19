package com.yaoan.module.econtract.service.warningcfg.query;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yaoan.framework.mybatis.core.query.QueryWrapperX;
import com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow.ContractBorrowBpmDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface DOQueryStrategy<T> {
    // 策略接口
    QueryWrapperX<T> createQueryWrapper();
    /** 获取对应的Mapper */
    BaseMapper<T> getMapper();
    Class<T> getDOClass();

}
