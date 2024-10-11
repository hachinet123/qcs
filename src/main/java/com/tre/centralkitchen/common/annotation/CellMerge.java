package com.tre.centralkitchen.common.annotation;

import java.lang.annotation.*;

/**
 * excel Column cell merging (combining the same items in the column)
 * <p>
 * It needs to be used with {@link CellMergeStrategy} strategy
 *
 * @author JDev
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CellMerge {

    /**
     * col index
     */
    int index() default -1;

}
