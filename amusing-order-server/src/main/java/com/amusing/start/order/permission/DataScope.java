package com.amusing.start.order.permission;

import java.lang.annotation.*;

/**
 * @author Lv.QingYu
 * @since 2025/3/3
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataScope {

    DataScopeType value() default DataScopeType.ADMIN;

}
