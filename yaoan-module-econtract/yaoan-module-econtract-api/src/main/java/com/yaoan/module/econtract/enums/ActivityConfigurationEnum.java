package com.yaoan.module.econtract.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author doujiale
 */

@Getter
public enum ActivityConfigurationEnum {
    /**
     * 工作流配置 枚举类
     */

    CONTRACT_DRAFT_APPROVE("合同草拟审批", 0, "contract_approve"),
    TEMPLATE_APPROVE("范本审批", 1, "template_submit_approve"),
    MODEL_APPROVE("模版审批", 2, "model_submit_approve"),
    PERFORMANCE_SUSPEND_APPROVE("履约中止审批", 3, "performance_suspend"),
    PAYMENT_APPLICATION_APPROVE("付款申请审批", 4, "payment_application_approve"),
    TERM_APPLICATION_APPROVE("条款审批", 5, "term_submit_approve"),
    CONTRACT_BORROW_SUBMIT_APPROVE("合同借阅审批", 6, "contract_borrow_submit_approve"),
    CONTRACT_CHANGE_APPLICATION_APPROVE("合同变动审批", 7, "contract_change_application_approve"),
    CONTRACT_REGISTER_APPLICATION_APPROVE("合同登记审批", 8, "contract_register_application_approve"),
    ECMS_CONTRACT_BOTH("双方合同确认签章流程", 9, "ecms_contract_both"),
    ECMS_CONTRACT_TRIPARTITE("三方合同确认签章流程", 10, "ecms_contract_tripartite"),

    ECMS_RELATIVE_BLACKLIST("相对方黑名单流程", 11, "contract_relative_blacklist"),
    ECMS_CONTRACT_INVOICE("发票申请审批流程", 12, "contract_invoice_approve"),
    PAYMENT_PLAN_DEFERRED_APPLICATION("付款计划延期申请审批", 13, "payment_plan_deferred_application"),
    CONTRACT_ARCHIVE_APPLY("归档申请审批", 14, "contract_archive_apply"),
    SEAL_APPLICATION_APPROVE("用印申请审批", 15, "seal_application_approve"),
    RELATIVE_APPLICATION_APPROVE("相对方申请审批", 16, "relative_application_approve"),

    ECMS_CONTRACT_RELATIVES("多相对方确认签署", 17, "ecms_contract_relatives_v2"),
    SAAS_COMPANY_JOIN("加入公司审批", 18, "saas_company_join"),



    ;

    private final String name;
    private final int value;
    private final String definitionKey;

    ActivityConfigurationEnum(String name, int value, String definitionKey) {
        this.name = name;
        this.value = value;
        this.definitionKey = definitionKey;
    }

    public static ActivityConfigurationEnum getInstance(int number) {
        for (ActivityConfigurationEnum value : ActivityConfigurationEnum.values()) {
            if (value.getValue() == number) {
                return value;
            }
        }
        return null;
    }


    public static ActivityConfigurationEnum getInstanceByDefKey(String definitionKey) {
        for (ActivityConfigurationEnum value : ActivityConfigurationEnum.values()) {
            if (StringUtils.equals(value.getDefinitionKey(), definitionKey)) {
                return value;
            }
        }
        return null;
    }
}
