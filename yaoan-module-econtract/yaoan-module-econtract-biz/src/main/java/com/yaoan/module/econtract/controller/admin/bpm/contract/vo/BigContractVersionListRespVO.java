package com.yaoan.module.econtract.controller.admin.bpm.contract.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/29 11:32
 */
@Data
public class BigContractVersionListRespVO {

    /**
     * 当前版本文件名称
     */
    private String contractName;

    /**
     * 历史版本列表
     */
    private List<ContractVersionListRespVO> respVOS;
}
