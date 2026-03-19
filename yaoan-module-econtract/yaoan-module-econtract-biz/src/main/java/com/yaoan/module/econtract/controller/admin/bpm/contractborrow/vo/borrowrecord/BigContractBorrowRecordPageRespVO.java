package com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.common.FlowableConfigParamBaseRespVO;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableParam;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/11 17:23
 */
@Data
public class BigContractBorrowRecordPageRespVO extends FlowableConfigParamBaseRespVO {
    private PageResult<ContractBorrowRecordPageRespVO> pageResult;
}
