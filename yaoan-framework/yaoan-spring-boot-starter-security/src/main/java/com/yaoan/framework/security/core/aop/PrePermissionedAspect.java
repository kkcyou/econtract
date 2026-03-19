package com.yaoan.framework.security.core.aop;

import com.yaoan.framework.common.exception.ServiceException;
import com.yaoan.framework.common.util.date.DateUtils;
import com.yaoan.framework.security.core.annotations.PrePermissioned;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Date;

import static com.yaoan.framework.common.exception.enums.GlobalErrorCodeConstants.ERROR_PERMISSION_CODE;
import static com.yaoan.framework.common.exception.enums.GlobalErrorCodeConstants.OVERTIME;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @author doujiale
 */
@Aspect
@Slf4j
public class PrePermissionedAspect {

    @Around("@annotation(prePermissioned)")
    public Object around(ProceedingJoinPoint joinPoint, PrePermissioned prePermissioned) throws Throwable {
        if (SecurityFrameworkUtils.getClientInfo() == null || SecurityFrameworkUtils.getExpiresTime() == null) {
            throw exception(ERROR_PERMISSION_CODE);
        }
        if(SecurityFrameworkUtils.getExpiresTime().before(new Date())){
            throw exception(OVERTIME);
        }

        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 1) {
            //取出第2个参数
            Object obj = joinPoint.getArgs()[1];
            if (obj instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult) obj;
                if (bindingResult.hasErrors()) {
                    StringBuilder errorMessage = new StringBuilder();
                    for (FieldError fieldError : bindingResult.getFieldErrors()) {
                        errorMessage.append(fieldError.getDefaultMessage()).append(",");
                    }
                    throw new ServiceException(400, errorMessage.toString());
                }
            }
        }
        return joinPoint.proceed();
    }

}
