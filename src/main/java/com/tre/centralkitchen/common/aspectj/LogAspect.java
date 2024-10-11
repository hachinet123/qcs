package com.tre.centralkitchen.common.aspectj;

import cn.hutool.core.util.ObjectUtil;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.OperLog;
import com.tre.centralkitchen.common.enums.BusinessStatus;
import com.tre.centralkitchen.common.enums.HttpMethod;
import com.tre.centralkitchen.common.utils.JsonUtils;
import com.tre.centralkitchen.common.utils.ServletUtils;
import com.tre.centralkitchen.common.utils.SpringUtils;
import com.tre.centralkitchen.common.utils.StringUtil;
import com.tre.centralkitchen.service.commom.OperLogService;
import com.tre.jdevtemplateboot.common.authority.TokenTakeApart;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class LogAspect {
    @Autowired
    private TokenTakeApart tokenTakeApart;
    @Value("${com.tre.jdev.exclude-path-patterns}")
    private String excludeUrl;

    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            OperLog operLog = new OperLog();
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            String ip = ServletUtils.getClientIP();
            operLog.setOperIp(ip);
            operLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
            try {
                operLog.setOperUserId(tokenTakeApart.takeDecryptedUserId());
            } catch (Exception exception) {
                operLog.setOperUserId("");
            }
            if (e != null) {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtil.substring(e.getMessage(), 0, 2000));
            }
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
            SpringUtils.getBean(OperLogService.class).recordOper(operLog);
        } catch (Exception exp) {
            log.error("==Pre notification exception==");
            log.error("Abnormal information:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, OperLog operLog, Object jsonResult) throws Exception {
        operLog.setBusinessType(log.businessType().getCode());
        operLog.setTitle(log.title());
        operLog.setOperatorType(log.operatorType().getCode());
        if (log.isSaveRequestData()) {
            setRequestValue(joinPoint, operLog);
        }
        if (log.isSaveResponseData() && ObjectUtil.isNotNull(jsonResult)) {
            operLog.setJsonResult(StringUtil.substring(JsonUtils.toJsonString(jsonResult), 0, 2000));
        }
    }

    private void setRequestValue(JoinPoint joinPoint, OperLog operLog) throws Exception {
        String requestMethod = operLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            operLog.setOperParam(StringUtil.substring(params, 0, 2000));
        } else {
            Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            operLog.setOperParam(StringUtil.substring(paramsMap.toString(), 0, 2000));
        }
    }

    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (ObjectUtil.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        params.append(JsonUtils.toJsonString(o)).append(" ");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return params.toString().trim();
    }

    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                if (value instanceof MultipartFile) {
                    return true;
                } else {
                    break;
                }
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                if (entry.getValue() instanceof MultipartFile) {
                    return true;
                } else {
                    break;
                }
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
