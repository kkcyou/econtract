package com.yaoan.framework.security.core.annotations;

import java.lang.annotation.*;

/**
 * 声明用户需要认证
 *
 * @author 芋道源码
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PrePermissioned {
}
