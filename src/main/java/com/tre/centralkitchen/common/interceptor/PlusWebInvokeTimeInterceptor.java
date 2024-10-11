package com.tre.centralkitchen.common.interceptor;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.tre.centralkitchen.common.filter.RepeatedlyRequestWrapper;
import com.tre.centralkitchen.common.utils.JsonUtils;
import com.tre.centralkitchen.common.utils.SpringUtils;
import com.tre.centralkitchen.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Map;

@Slf4j
public class PlusWebInvokeTimeInterceptor implements HandlerInterceptor {

    private final TransmittableThreadLocal<StopWatch> invokeTimeTL = new TransmittableThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!"prod".equals(SpringUtils.getActiveProfile())) {
            String url = request.getMethod() + " " + request.getRequestURI();

            if (isJsonRequest(request)) {
                String jsonParam = "";
                if (request instanceof RepeatedlyRequestWrapper) {
                    BufferedReader reader = request.getReader();
                    jsonParam = IoUtil.read(reader);
                }
                log.info("[PLUS]Start Request => URL[{}],Parameter Type[json],Parameter:[{}]", url, jsonParam);
            } else {
                Map<String, String[]> parameterMap = request.getParameterMap();
                if (MapUtil.isNotEmpty(parameterMap)) {
                    String parameters = JsonUtils.toJsonString(parameterMap);
                    log.info("[PLUS]Start Request => URL[{}],Parameter Type[param],Parameter:[{}]", url, parameters);
                } else {
                    log.info("[PLUS]Start Request => URL[{}],No parameters", url);
                }
            }

            StopWatch stopWatch = new StopWatch();
            invokeTimeTL.set(stopWatch);
            stopWatch.start();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (!"prod".equals(SpringUtils.getActiveProfile())) {
            StopWatch stopWatch = invokeTimeTL.get();
            stopWatch.stop();
            log.info("[PLUS]End Request => URL[{}],time consuming:[{}]millisecond", request.getMethod() + " " + request.getRequestURI(), stopWatch.getTime());
            invokeTimeTL.remove();
        }
    }

    private boolean isJsonRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType != null) {
            return StringUtil.startsWithIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE);
        }
        return false;
    }

}
