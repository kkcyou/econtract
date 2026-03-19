package com.yaoan.module.econtract.dal.mysql.contractaidraftrecord;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.ContractAiDraftRecordPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractaidraftrecord.ContractAiDraftRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 合同智能起草记录 Mapper
 *
 * @author doujiale
 */
@Mapper
public interface ContractAiDraftRecordMapper extends BaseMapperX<ContractAiDraftRecordDO> {

    default PageResult<ContractAiDraftRecordDO> selectPage(ContractAiDraftRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ContractAiDraftRecordDO>().orderByDesc(ContractAiDraftRecordDO::getCreateTime));
    }


}
