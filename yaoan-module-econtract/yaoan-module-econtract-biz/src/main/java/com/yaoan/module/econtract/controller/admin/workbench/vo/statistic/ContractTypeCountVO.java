package com.yaoan.module.econtract.controller.admin.workbench.vo.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/4 11:54
 */
@Data
@AllArgsConstructor
public class ContractTypeCountVO {

    /**
     * 合同类型名称
     */
    private String name;
    /**
     * 合同数量
     */
    private Long contractCount;;

}
