package com.yaoan.module.econtract.controller.admin.amount.vo;

import com.yaoan.module.econtract.controller.admin.amount.vo.small.SmallMoneySmallRespVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.small.SmallNumberSmallRespVO;
import lombok.Data;

import java.util.List;

/**
 * @description: 合同签约类型统计
 * @author: Pele
 * @date: 2023/11/9 10:56
 */
@Data
public class ContractTypeSignedStatisticRespVO {
    /**
     * 金额统计信息
     */
    private List<SmallMoneySmallRespVO> smallMoneySmallRespVOList;

    /**
     * 份数统计信息
     */
    private List<SmallNumberSmallRespVO> smallNumberSmallRespVOList;
}
