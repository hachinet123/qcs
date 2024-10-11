package com.tre.centralkitchen.common.annotation;

import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.OperatorType;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String title() default "";

    BusinessType businessType() default BusinessType.OTHER;

    OperatorType operatorType() default OperatorType.MANAGE;

    boolean isSaveRequestData() default true;

    boolean isSaveResponseData() default true;
}
