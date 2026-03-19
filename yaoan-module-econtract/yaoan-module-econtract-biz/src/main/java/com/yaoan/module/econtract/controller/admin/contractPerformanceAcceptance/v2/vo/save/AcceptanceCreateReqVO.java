package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.save;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025/4/22 11:53
 */
@Data
public class AcceptanceCreateReqVO {
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 状态
     * 1=验收中
     * 2=验收通过
     * 5=验收不通过
     */
    private Integer status;

    /**
     * 申请日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date applyDate;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 计划id
     */
    private String planId;

    /**
     * 验收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date acceptanceDate;

    /**
     * 预计结款时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date expectedPayDate;


    /**
     * 验收备注
     */
    private String remark;

    /**
     * 验收计划
     */
    @NotEmpty(message = "验收计划不能为空")
    private List<AcceptancePlanReqVO> acceptancePlanReqVOList;

    /**
     * 验收材料
     */
    private List<Long> fileIdList;


}
