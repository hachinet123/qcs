package com.tre.centralkitchen.common.utils;

import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>Reflect Util</p>
 *
 * @author 10225441
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectUtils {
    /**
     * <p>Get bean's get method.</p>
     *
     * @param objectClass Object's class
     * @param fieldName   Object's field name
     * @return Attribute's get method of the field
     */
    public static Method getGetMethod(Class<?> objectClass, String fieldName) {
        StringBuilder sb = new StringBuilder();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase(Locale.ROOT));
        sb.append(fieldName.substring(1));
        try {
            return objectClass.getMethod(sb.toString());
        } catch (NoSuchMethodException e) {
            throw new SysBusinessException(e.getMessage());
        }
    }

    /**
     * <p>Get bean's set method.</p>
     *
     * @param objectClass Object's class
     * @param fieldName   Object's field name
     * @return Attribute's set method of the field
     */
    public static Method getSetMethod(Class<?> objectClass, String fieldName) {
        try {
            Class<?>[] parameterTypes = new Class<?>[1];
            Field field = objectClass.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            String sb = "set" + fieldName.substring(0, 1).toUpperCase(Locale.ROOT) + fieldName.substring(1);
            return objectClass.getMethod(sb, parameterTypes);
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            throw new SysBusinessException(e.getMessage());
        }
    }

    /**
     * <p>Do bean's set method.</p>
     *
     * @param obj       Object
     * @param fieldName Object's field name
     * @param value     Value of attribute
     */
    public static void invokeSet(Object obj, String fieldName, Object value) {
        Method method = getSetMethod(obj.getClass(), fieldName);
        try {
            method.invoke(obj, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SysBusinessException(e.getMessage());
        }
    }

    /**
     * <p>Do bean's get method.</p>
     *
     * @param obj       Object
     * @param fieldName Object's field name
     * @return Value of attribute
     */
    public static Object invokeGet(Object obj, String fieldName) {
        Method method = getGetMethod(obj.getClass(), fieldName);
        try {
            return method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SysBusinessException(e.getMessage());
        }
    }

    /**
     * <p>Do bean's method.</p>
     *
     * @param obj        Object
     * @param methodName Object's method name
     * @return Method's results of enforcement
     */
    public static Object invokeMethod(Object obj, String methodName) {
        try {
            return obj.getClass().getMethod(methodName).invoke(obj);
        } catch (Exception e) {
            throw new SysBusinessException(e.getMessage());
        }
    }

    /**
     * <p>Get bean's field names.</p>
     *
     * @param clazz Bean's class
     * @return Field's name list
     */
    public static List<String> getFieldNameList(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>(fields.length);
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }

    /**
     * <p>Change annotation's value of field.</p>
     *
     * @param clazz      Class
     * @param aClazz     Annotation's class
     * @param fieldName  Field name
     * @param aFieldName Annotation's field name
     * @param value      New value
     */
    public static void changeAnnotationValue(Class<?> clazz, Class<? extends Annotation> aClazz, String fieldName, String aFieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            Object annotation = field.getAnnotation(aClazz);
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
            Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
            memberValues.setAccessible(true);
            Map<String, Object> memberValuesMap = (Map<String, Object>) memberValues.get(invocationHandler);
            memberValuesMap.put(aFieldName, value);
        } catch (Exception e) {
            throw new SysBusinessException(e.getMessage());
        }
    }

    /**
     * <p>Get filed name list if the field has annotation.</p>
     *
     * @param clazz  Class
     * @param aClazz Annotation's class
     * @return field name list
     */
    public static List<String> getFieldNameListHasAnnotation(Class<?> clazz, Class<? extends Annotation> aClazz) {
        List<String> fieldNameList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(aClazz)) {
                fieldNameList.add(field.getName());
            }
        }
        return fieldNameList;
    }

    /**
     * <p>Get filed name list if the field has annotation.</p>
     *
     * @param clazz  Class
     * @param aClazz Annotation's class
     * @return field name list
     */
    public static List<Object> getAnnotationValueList(Class<?> clazz, Class<? extends Annotation> aClazz, String aKey) {
        List<Object> aValueList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(aClazz)) {
                aValueList.add(invokeMethod(field.getAnnotation(aClazz), aKey));
            }
        }
        return aValueList;
    }
}
