package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * 对接系统类型
 */
@Getter
public enum ProjectTypeEnums {

    /**
     * 四种审批状态
     */
//模板
// ”未审批“ ：删除和更改
// ”未通过审批“：只能编辑。
    PROJECT_PURCHASING(1, "项目采购系统"),
    FRAME_WORK(2, "框架协议采购系统"),
    ELECTRONICS_STORE(3, "电子卖场系统");

    private final Integer code;
    private final String info;

    ProjectTypeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ProjectTypeEnums getInstance(Integer code) {
        for (ProjectTypeEnums value : ProjectTypeEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
