package com.yaoan.module.econtract.controller.admin.change.vo;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.term.vo.FlowableConfigParamBaseRespVO;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/11 10:31
 */
@Data
public class BigContractChangeListApproveRespVO extends FlowableConfigParamBaseRespVO {

    private static final long serialVersionUID = 2963098682097104746L;

    private  PageResult<ContractChangeListApproveRespVO> pageResult;
}
