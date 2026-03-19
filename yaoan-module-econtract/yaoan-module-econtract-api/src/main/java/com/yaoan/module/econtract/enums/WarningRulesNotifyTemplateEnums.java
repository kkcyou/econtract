package com.yaoan.module.econtract.enums;

import lombok.Getter;

@Getter
public enum WarningRulesNotifyTemplateEnums {
    SIGN_EXPECTED_REMINDER  ("sign_expected_reminder", "合同签署风险预期提醒"),
    SIGN_EXPIRE_REMINDER  ("sign_expire_reminder", "合同签署风险到期提醒"),
    SIGN_OVERDUE_REMINDER  ("sign_overdue_reminder", "合同签署风险超期提醒"),
    PAYMENT_EXPECTED_REMINDER  ("payment_expected_reminder", "合同付款风险预期提醒"),
    PAYMENT_EXPIRE_REMINDER  ("payment_expire_reminder", "合同付款风险到期提醒"),
    PAYMENT_OVERDUE_REMINDER  ("payment_overdue_reminder", "合同付款风险超期提醒"),
    COLLECTION_EXPECTED_REMINDER  ("collection_expected_reminder", "合同收款风险预期提醒"),
    COLLECTION_EXPIRE_REMINDER  ("collection_expire_reminder", "合同收款风险到期提醒"),
    COLLECTION_OVERDUE_REMINDER  ("collection_overdue_reminder", "合同收款风险超期提醒"),
    FILE_REMINDER  ("file_reminder", "合同归档风险提醒"),
    BORROW_EXPIRE_REMINDER  ("borrow_expire_reminder", "借阅归还风险到期提醒"),
    BORROW_OVERDUE_REMINDER  ("borrow_overdue_reminder", "借阅归还风险超期提醒"),
    RELATIVE_REJECT_REMINDER  ("relative_reject_reminder", "相对方退回合同提醒"),
    SAAS_ADMIN_TRANSFER_CONTRACT_REMINDER  ("saas_admin_transfer_contract_reminder", "saas管理员转发合同提醒模板"),
    SAAS_COMPANY_JOIN_APPLICATION_SUBMITTED_REMINDER  ("saas_company_join_application_submitted_reminder", "已发起加入企业申请提醒"),

    ;

    private final String code;
    private final String name;

    WarningRulesNotifyTemplateEnums(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public static WarningRulesNotifyTemplateEnums getInstance(String code) {
        for (WarningRulesNotifyTemplateEnums value : WarningRulesNotifyTemplateEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
