package com.yaoan.module.econtract.controller.admin.bpm.register.vo;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.common.FlowableConfigParamBaseRespVO;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/8 18:33
 */
@Data
public class BigContractRegisterListApproveRespVO  extends FlowableConfigParamBaseRespVO {
    /**
     * 列表信息
     */
    private PageResult<ContractRegisterListApproveRespVO> pageResult;
}
