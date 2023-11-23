package com.amusing.start.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Lv.QingYu
 * @description: 接口访问频次限制(比如 : 在1秒钟内只能通过一个请求)
 * @since 2023/03/06
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {

    /**
     * 时间窗口
     *
     * @return 时间
     */
    int time() default 1;

    /**
     * 次数
     *
     * @return 限制次数
     */
    int count() default 1;

}
