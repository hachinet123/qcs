package com.tre.centralkitchen.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserId {
    String userName() default "";
}
