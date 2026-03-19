package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule;

import lombok.Data;

import java.util.List;

@Data
public class RelCheckListRuleCommonRespVO {

    //清单名称
    private String reviewListname;

    //规则list
    private List<ContractRuleCommonRespVO> reviewItems;
}
