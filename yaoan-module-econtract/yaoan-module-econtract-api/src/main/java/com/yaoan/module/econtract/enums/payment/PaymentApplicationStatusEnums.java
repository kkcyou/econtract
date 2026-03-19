package com.yaoan.module.econtract.enums.payment;

import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-3 16:44
 */
@Getter
public enum PaymentApplicationStatusEnums {
  /** 款项类型 枚举类 */
  DOWN_PAYMENT(1, "首付款"),
  PROGRESS_PAYMENT(2, "进度款"),
  FINAL_PAYMENT(3, "尾款"),
  RETENTION_PAYMENT(4, "质保金"),
  ;

  private final Integer code;
  private final String info;

  PaymentApplicationStatusEnums(Integer code, String info) {
    this.code = code;
    this.info = info;
  }

  public static PaymentApplicationStatusEnums getInstance(Integer code) {
    for (PaymentApplicationStatusEnums value : PaymentApplicationStatusEnums.values()) {
      if (value.getCode().equals(code)) {
        return value;
      }
    }
    return null;
  }
}
