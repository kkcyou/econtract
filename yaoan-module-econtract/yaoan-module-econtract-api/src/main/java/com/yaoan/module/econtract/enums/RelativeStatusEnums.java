package com.yaoan.module.econtract.enums;

import lombok.Getter;

@Getter
public enum RelativeStatusEnums {
    Normal(0, "否","正常"),
    ToBlacklist(1, "移入中", "移入中"),
    Blacklist(2, "是", "黑名单"),
    RemoveBlacklist(3, "移出中", "移出中"),
    DRAFT(4, "否", "草稿"),
    APPROVING(5, "否","审批中")

            ;
    private final Integer code;
    //黑名单状态
    private final String info;
    //相对方状态
    private final String description;
    RelativeStatusEnums(Integer code, String info,String description)
    {
        this.code = code;
        this.info = info;
        this.description = description;
    }

    public static RelativeStatusEnums getInstance(Integer code) {
        for (RelativeStatusEnums value : RelativeStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
