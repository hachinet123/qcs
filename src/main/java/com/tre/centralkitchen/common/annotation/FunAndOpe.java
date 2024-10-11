package com.tre.centralkitchen.common.annotation;

import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FunAndOpe {
    FunType funType() default FunType.FOREIGN;
    OpeType opeType() default OpeType.FOREIGN;
    BusinessType businessType() default BusinessType.OTHER;
}
