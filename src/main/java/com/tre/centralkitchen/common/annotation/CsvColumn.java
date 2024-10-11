package com.tre.centralkitchen.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CsvColumn {
    /**
     * show column name
     * @return
     */
    String columnName();

    /**
     * column position
     * @return
     */
    int position() default -1;
}
