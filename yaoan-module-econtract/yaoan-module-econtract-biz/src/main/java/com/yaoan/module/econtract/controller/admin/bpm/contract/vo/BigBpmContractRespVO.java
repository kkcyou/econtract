package com.yaoan.module.econtract.controller.admin.bpm.contract.vo;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.common.FlowableConfigParamBaseRespVO;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/11 17:03
 */
@Data
public class BigBpmContractRespVO extends FlowableConfigParamBaseRespVO {
    private static final long serialVersionUID = 1027714145564652422L;
    private PageResult<BpmContractRespVO> pageResult;
}
