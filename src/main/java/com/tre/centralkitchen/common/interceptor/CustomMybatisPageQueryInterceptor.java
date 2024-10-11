package com.tre.centralkitchen.common.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.tre.centralkitchen.common.annotation.SqlserverCustomPage;
import com.tre.centralkitchen.common.domain.CustomPageData;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @date 2022-11-28
 */
@Component
@Intercepts({@Signature(type= StatementHandler.class,method = "query",args = {Statement.class, ResultHandler.class})})
public class CustomMybatisPageQueryInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        List<Object> pageVoList = new ArrayList<>();
        if(result instanceof List){
            List<List<Object>> resultList = (ArrayList<List<Object>>) result;
            CustomPageData<Object> customPageData = (CustomPageData<Object>) resultList.get(0).get(0);
            customPageData.setRows(resultList.get(1));
            pageVoList.add(customPageData);
            return pageVoList;
        }
        return result;

    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            try {
                PluginUtils.MPStatementHandler mappedStatement =  PluginUtils.mpStatementHandler((StatementHandler) target);
                String namespace = mappedStatement.mappedStatement().getId();
                String className = namespace.substring(0,namespace.lastIndexOf("."));
                String methedName= namespace.substring(namespace.lastIndexOf(".") + 1,namespace.length());
                Method[] ms = Class.forName(className).getMethods();
                for(Method m : ms){
                    if(m.getName().equals(methedName)) {
                        SqlserverCustomPage annotation = m.getAnnotation(SqlserverCustomPage.class);
                        if(annotation != null){
                            return Plugin.wrap(target, this);
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return target;
    }

}
