package com.yaoan.framework.common.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yaoan.framework.common.exception.ErrorCode;
import com.yaoan.framework.common.exception.ServiceException;
import com.yaoan.framework.common.exception.enums.GlobalErrorCodeConstants;
import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 外部接口 通用返回
 *
 * @param <T> 数据泛型
 */
@Data
public class ExternallnterfaceResult<T> implements Serializable {

    private Integer code;

    /**
     * 错误码
     *
     * @see ErrorCode#getCode()
     */
    private Integer status;
    /**
     * 返回数据
     */
    private T data;
    /**
     * 错误提示，用户可阅读
     *
     * @see ErrorCode#getMsg() ()
     */
    private String message;
    /**
     * 加密后的字符串
     */
    private String mac;
    /**
     * 加密后的字符串
     */
    private String responseTime;

    /**
     * 将传入的 result 对象，转换成另外一个泛型结果的对象
     * <p>
     * 因为 A 方法返回的 CommonResult 对象，不满足调用其的 B 方法的返回，所以需要进行转换。
     *
     * @param result 传入的 result 对象
     * @param <T>    返回的泛型
     * @return 新的 CommonResult 对象
     */
    public static <T> ExternallnterfaceResult<T> error(ExternallnterfaceResult<?> result) {
        return error(result.getStatus(), result.getMessage());
    }

    public static <T> ExternallnterfaceResult<T> error(Integer status, String message) {
        Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.getCode().equals(status), "status 必须是错误的！");
        ExternallnterfaceResult<T> result = new ExternallnterfaceResult<>();
        result.status = status;
        result.message = message;
        result.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return result;
    }

    public static <T> ExternallnterfaceResult<T> error(Integer status, String message, String responseTime) {
        Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.getCode().equals(status), "status 必须是错误的！");
        ExternallnterfaceResult<T> result = new ExternallnterfaceResult<>();
        result.status = status;
        result.message = message;
        result.responseTime = responseTime != null ? null : LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return result;
    }

    public static <T> ExternallnterfaceResult<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    //    public static <T> ExternallnterfaceResult<T> success(T data) {
//        ExternallnterfaceResult<T> result = new ExternallnterfaceResult<>();
//        result.status = GlobalErrorCodeConstants.SUCCESS.getCode();
//        result.data = data;
//        result.responseTime = new Date().toString();
//        result.msg = "";
//        return result;
//    }
    public static <T> ExternallnterfaceResult<T> success(T data, String mac) {
        ExternallnterfaceResult<T> result = new ExternallnterfaceResult<>();
        result.status = GlobalErrorCodeConstants.SUCCESS.getCode();
        result.data = data;
        result.mac = mac;
        result.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        result.message = "";
        result.code = 0;
        return result;
    }

    public static <T> ExternallnterfaceResult<T> success_200(T data) {
        ExternallnterfaceResult<T> result = new ExternallnterfaceResult<>();
        result.status = GlobalErrorCodeConstants.SUCCESS_200.getCode();
        result.data = data;
        result.message = "";
        return result;
    }

    public static boolean isSuccess(Integer status) {
        return Objects.equals(status, GlobalErrorCodeConstants.SUCCESS.getCode());
    }

    public static <T> ExternallnterfaceResult<T> error(ServiceException serviceException) {
        return error(serviceException.getCode(), serviceException.getMessage());
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isSuccess() {
        return isSuccess(status);
    }

    // ========= 和 Exception 异常体系集成 =========

    @JsonIgnore // 避免 jackson 序列化
    public boolean isError() {
        return !isSuccess();
    }

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     */
    public void checkError() throws ServiceException {
        if (isSuccess()) {
            return;
        }
        // 业务异常
        throw new ServiceException(status, message);
    }

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     * 如果没有，则返回 {@link #data} 数据
     */
    @JsonIgnore // 避免 jackson 序列化
    public T getCheckedData() {
        checkError();
        return data;
    }

}
