package com.amusing.start.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lv.qingyu
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface AmusingBehaviorStatistics {

    /**
     * 用户标识
     *
     * @return
     */
    String userId() default "";

    /**
     * 业务模块(商品服务、订单服务等)
     *
     * @return
     */
    String businessModel() default "";

    /**
     * 业务编码
     *
     * @return
     */
    String businessNum() default "";

    /**
     * 业务类型(增删改查)
     *
     * @return
     */
    int businessType() default 1;

    /**
     * 请求路径
     *
     * @return
     */
    String path() default "";

    /**
     * 方法类型
     *
     * @return
     */
    String method() default "";

    /**
     * 请求参数
     *
     * @return
     */
    String[] params() default {};

    String ip() default "";

}
