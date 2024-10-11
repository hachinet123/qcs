package com.tre.centralkitchen.common.aspectj;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tre.centralkitchen.common.annotation.UserId;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.ObjConvertUtils;
import com.tre.centralkitchen.common.utils.SpringUtils;
import com.tre.centralkitchen.common.utils.StringUtil;
import com.tre.centralkitchen.domain.vo.system.MtEmployeesVo;
import com.tre.centralkitchen.service.UserMasterService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class UserIdAspect {

    private static String DICT_TEXT_SUFFIX = "Name";

    @Pointcut("@annotation(com.tre.centralkitchen.common.annotation.UserIdDict)")
    public void userId() {

    }

    @Around("userId()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object responseObj = null;
        try {
            responseObj = joinPoint.proceed();
            this.dealDateByType(responseObj);
        } catch (Throwable throwable) {
            log.error("Data exception:{}", JSON.toJSONString(responseObj), throwable);
        }
        return responseObj;
    }

    private void dealDateByType(Object var) {
        ResponseResult response = (ResponseResult) var;
        if (response.getData() instanceof TableDataInfo) {
//            TableDataInfo pageResult = (TableDataInfo) response.getData();
//            List list = pageResult.getRows();
//            List<JSONObject> items = new ArrayList<>();
//            for (Object o : list) {
//                items.add(dealPrecisionField(o));
//            }
//            pageResult.setRows(items);
        }
        if (response.getData() instanceof List) {
//            List list = (List) var;
//            List<JSONObject> items = new ArrayList<>();
//            for (Object o : list) {
//                items.add(dealPrecisionField(o));
//            }
//            ((ResponseResult) var).setData(items);
        } else {
            ((ResponseResult) var).setData(dealPrecisionField(response.getData()));
        }
    }

    private JSONObject dealPrecisionField(Object record) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{}";
        try {
            json = mapper.writeValueAsString(record);
        } catch (JsonProcessingException e) {
            log.error("Json parsing failed：" + e);
        }
        JSONObject item = JSONObject.parseObject(json);
        for (Field field : ObjConvertUtils.getAllFields(record)) {
            if (field.getAnnotation(UserId.class) != null) {
                String text = field.getAnnotation(UserId.class).userName();
                String key = String.valueOf(item.get(field.getName()));
                String textValue = dealFieldValue(key);
                if (!StringUtil.isBlank(text)) {
                    item.put(text, textValue);
                } else {
                    item.put(field.getName() + DICT_TEXT_SUFFIX, textValue);
                }
            }
        }
        return item;
    }

    private String dealFieldValue(String key) {
        if (key.equals("0")) {
            return "";
        }
        if (StringUtil.isNotBlank(key) && !key.equals("null")) {
            MtEmployeesVo mtEmployeesVo = SpringUtils.getBean(UserMasterService.class).getUserName(key);
            if (!Objects.isNull(mtEmployeesVo)) {
                return mtEmployeesVo.getEmployeeName();
            }
        }
        return "";
    }
}
