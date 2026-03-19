package com.yaoan.module.econtract.controller.admin.performance.v2.vo;

import com.yaoan.framework.common.pojo.PageResult;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/22 11:45
 */
@Data
public class BigContractPerformV2RespVO {

    private ContractPerformV2CountRespVO v2CountRespVO;

    private PageResult<ContractPerformV2RespVO> pageResult;
}
