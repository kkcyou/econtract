package com.yaoan.module.econtract.controller.admin.aop;


import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.util.json.JsonUtils;
import com.yaoan.framework.operatelog.core.enums.OperateTypeEnum;
import com.yaoan.framework.operatelog.core.service.OperateLog;
import com.yaoan.framework.operatelog.core.service.OperateLogFrameworkService;
import com.yaoan.module.econtract.api.contract.dto.mongolia.EncryptResponseDto;
import com.yaoan.module.econtract.controller.admin.aop.anno.MyOperateLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.yaoan.framework.common.exception.enums.GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR;
import static com.yaoan.framework.common.exception.enums.GlobalErrorCodeConstants.SUCCESS;

@Slf4j
@Aspect
@Component
public class MyOperateLogAspect {
    @Resource
    private OperateLogFrameworkService operateLogFrameworkService;
    /**
     * 用于记录操作内容的上下文
     *
     * @see OperateLog#getContent()
     */
    private static final ThreadLocal<String> CONTENT = new ThreadLocal<>();
    /**
     * 用于记录拓展字段的上下文
     *
     * @see OperateLog#getExts()
     */
    private static final ThreadLocal<Map<String, Object>> EXTS = new ThreadLocal<>();

    @Pointcut("@annotation(com.yaoan.module.econtract.controller.admin.aop.anno.MyOperateLog)")
    public void myAnnotationPointcut() {
        // 用于定义切点的空方法
    }

    @Around("myAnnotationPointcut() && execution(* com.yaoan.module.econtract.controller.admin.aop.service.*.*(..))")
    public Object logAroundServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        OperateLog operateLog = new OperateLog();
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取方法名
        String methodName = signature.getMethod().getName();
        operateLog.setJavaMethod(methodName);
        // 获取方法参数
        Object[] args = new Object[0];
        String jsonString = null;
        try {
            args = joinPoint.getArgs();
            jsonString = JsonUtils.toJsonString(args);
        } catch (Exception e) {
            jsonString = e.getMessage();
        }
        operateLog.setJavaMethodArgs(jsonString);
        Method method = signature.getMethod();
        // 获取方法上的MyCustomAnnotation注解实例
        MyOperateLog myAnnotation = method.getAnnotation(MyOperateLog.class);
        if (myAnnotation != null) {
            // 获取注解的值
            String url = myAnnotation.url();
            operateLog.setRequestUrl(url);
            String module = myAnnotation.module();
            operateLog.setModule(module);
            String name = myAnnotation.name();
            operateLog.setName(name);
        }


        // 记录开始时间
        LocalDateTime startTime = LocalDateTime.now();
        operateLog.setStartTime(startTime);

        Object result = null;
        try {
            // 执行方法
            result = joinPoint.proceed(args);
            setOperateLogInfo(result, operateLog, startTime, null);
            return result;
        } catch (Throwable ex) {
            setOperateLogInfo(result, operateLog, startTime, ex);
            return result;
        } finally {
            clearThreadLocal();
        }
    }

    private void setOperateLogInfo(Object result, OperateLog operateLog, LocalDateTime startTime, Throwable exception) {
        String resultStr = obtainResultData(result);
        // 真正记录操作日志
        operateLog.setResultData(resultStr);
        operateLog.setResultCode(SUCCESS.getCode());
        operateLog.setDuration((int) (LocalDateTimeUtil.between(startTime, LocalDateTime.now()).toMillis()));
        // （异常）处理 resultCode 和 resultMsg 字段
        if (exception != null) {
            operateLog.setResultCode(INTERNAL_SERVER_ERROR.getCode());
            operateLog.setResultMsg(ExceptionUtil.getRootCauseMessage(exception));
        }
        // 异步记录日志
        operateLogFrameworkService.createOperateLog(operateLog);
    }

    private static void clearThreadLocal() {
        CONTENT.remove();
        EXTS.remove();
    }

    /**
     * @param request 请求
     * @return ua
     */
    public static String getUserAgent(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        return ua != null ? ua : "";
    }

    private static String obtainResultData(Object result) {
        // TODO 提升：结果脱敏和忽略
        if (result instanceof CommonResult) {
            result = ((CommonResult<?>) result).getData();
        }
        if (result instanceof EncryptResponseDto) {
            result = ((EncryptResponseDto) result).getData();
        }
        return JsonUtils.toJsonString(result);
    }

    private static RequestMethod[] obtainRequestMethod(ProceedingJoinPoint joinPoint) {
        RequestMapping requestMapping = AnnotationUtils.getAnnotation( // 使用 Spring 的工具类，可以处理 @RequestMapping 别名注解
                ((MethodSignature) joinPoint.getSignature()).getMethod(), RequestMapping.class);
        return requestMapping != null ? requestMapping.method() : new RequestMethod[]{};
    }

    private static RequestMethod obtainFirstMatchRequestMethod(RequestMethod[] requestMethods) {
        if (ArrayUtil.isEmpty(requestMethods)) {
            return null;
        }
        // 优先，匹配最优的 POST、PUT、DELETE
        RequestMethod result = obtainFirstLogRequestMethod(requestMethods);
        if (result != null) {
            return result;
        }
        // 然后，匹配次优的 GET
        result = Arrays.stream(requestMethods).filter(requestMethod -> requestMethod == RequestMethod.GET)
                .findFirst().orElse(null);
        if (result != null) {
            return result;
        }
        // 兜底，获得第一个
        return requestMethods[0];
    }

    private static OperateTypeEnum convertOperateLogType(RequestMethod requestMethod) {
        if (requestMethod == null) {
            return null;
        }
        switch (requestMethod) {
            case GET:
                return OperateTypeEnum.GET;
            case POST:
                return OperateTypeEnum.CREATE;
            case PUT:
                return OperateTypeEnum.UPDATE;
            case DELETE:
                return OperateTypeEnum.DELETE;
            default:
                return OperateTypeEnum.OTHER;
        }
    }

    private static RequestMethod obtainFirstLogRequestMethod(RequestMethod[] requestMethods) {
        if (ArrayUtil.isEmpty(requestMethods)) {
            return null;
        }
        return Arrays.stream(requestMethods).filter(requestMethod ->
                requestMethod == RequestMethod.POST
                        || requestMethod == RequestMethod.PUT
                        || requestMethod == RequestMethod.DELETE)
                .findFirst().orElse(null);
    }

    public static void setContent(String content) {
        CONTENT.set(content);
    }

    public static void addExt(String key, Object value) {
        if (EXTS.get() == null) {
            EXTS.set(new HashMap<>());
        }
        EXTS.get().put(key, value);
    }
}
