package com.yaoan.module.econtract.controller.admin.aop.anno;

import com.yaoan.framework.operatelog.core.enums.OperateTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyOperateLog {
    String value() default "";


    // ========== 模块字段 ==========

    /**
     * 操作模块
     *
     * 为空时，会尝试读取 {@link Tag#name()} 属性
     */
    String module() default "";
    /**
     * 操作名
     *
     * 为空时，会尝试读取 {@link Operation#summary()} 属性
     */
    String name() default "";
    /**
     * 操作分类
     *
     * 实际并不是数组，因为枚举不能设置 null 作为默认值
     */
    OperateTypeEnum[] type() default {};

    // ========== 开关字段 ==========

    /**
     * 是否记录操作日志
     */
    boolean enable() default true;
    /**
     * 是否记录方法参数
     */
    boolean logArgs() default true;
    /**
     * 是否记录方法结果的数据
     */
    boolean logResultData() default true;
    /**
     * url 接口路径
     */
    String url() default "";
}
