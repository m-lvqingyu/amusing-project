package com.amusing.start.log.annotation;

import com.amusing.start.log.enums.OperateType;

import java.lang.annotation.*;

/**
 * @author Lv.QingYu
 * @since 2023/12/28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * @return 操作类型 {@link OperateType}
     */
    OperateType type();

    /**
     * @return 操作描述
     */
    String value() default "";

    /**
     * @return SPel表达式
     */
    String expression() default "";

}
