package com.yaoan.module.econtract.controller.admin.ledger.vo.flowable;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/19 19:41
 */
@Data
public class ApproveStarterRespVO {

    /**
     * 发起人名称
     */
    private String name;

    /**
     * 发起时间
     */
    private LocalDateTime startTime;



}
