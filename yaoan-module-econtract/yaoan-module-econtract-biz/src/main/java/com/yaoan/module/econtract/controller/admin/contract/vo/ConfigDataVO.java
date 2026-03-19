package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

import java.util.List;
@Data
public class ConfigDataVO {

    /**
     * 合同配置实体类
     */

        private List<String> contractInfo;
        private List<String> partySign;
        private List<String> payPlan;
        private List<String> collectionPlan;
        private List<String> sign;

}
