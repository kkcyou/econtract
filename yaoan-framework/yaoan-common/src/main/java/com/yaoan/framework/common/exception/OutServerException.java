package com.yaoan.framework.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务逻辑异常 Exception
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class OutServerException extends RuntimeException {


    //     *  500：服务异常
    //     *  -1：数据缺失--订单数据缺失，合同数据缺失
    //     *  -2：签名错误
    //     *  -3：数据重复：订单重复
    //     *  -4：操作失败：取消失败
    private Integer code;
    /**
     * 错误提示
     */
    private String message;

    /**
     * 空构造方法，避免反序列化问题
     */
    public OutServerException() {
    }

    public OutServerException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
    }

    public OutServerException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public OutServerException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public OutServerException setMessage(String message) {
        this.message = message;
        return this;
    }

}
