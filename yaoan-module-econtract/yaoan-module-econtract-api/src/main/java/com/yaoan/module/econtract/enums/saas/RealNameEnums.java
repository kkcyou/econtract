package com.yaoan.module.econtract.enums.saas;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-28 15:48
 */
@Getter
public enum RealNameEnums {
  /** 实名情况 枚举类 */
  TODO(0, "未实名"),

  DONE(1, "已实名"),
  ;

  private final Integer code;
  private final String info;

  RealNameEnums(Integer code, String info) {
    this.code = code;
    this.info = info;
  }

  public static RealNameEnums getInstance(Integer code) {
    for (RealNameEnums value : RealNameEnums.values()) {
      if (value.getCode().equals(code)) {
        return value;
      }
    }
    return null;
  }
}
