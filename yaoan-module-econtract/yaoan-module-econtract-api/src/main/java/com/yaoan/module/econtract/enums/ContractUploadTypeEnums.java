package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/26 16:41
 */
@Getter
public enum ContractUploadTypeEnums {
    /**
     * 合同上传类型 枚举
     * */
    MODEL_DRAFT (0, "模板起草"),
    UPLOAD_FILE (1, "上传文件起草"),
    //合同补录 合同登记
    REGISTER(2, "合同补录 上传文件"),
    ORDER_DRAFT(3, "依据已成交的采购项目或订单起草"),
    THIRD_PARTY(4, "第三方系统"),
    COMPANY_LEVEL(5, "公司级别，角色或签"),
    UPLOAD_CONTRACT_FILE(6, "上传合同文件"),

    ;


    private final Integer code;
    private final String info;

    ContractUploadTypeEnums(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public static ContractUploadTypeEnums getInstance(Integer code) {
        for (ContractUploadTypeEnums value : ContractUploadTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
