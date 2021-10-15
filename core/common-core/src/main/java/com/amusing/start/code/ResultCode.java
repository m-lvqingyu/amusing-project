package com.amusing.start.code;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 公共枚举封装
 * @date 2021/10/15 22:43
 */
public interface ResultCode<C extends Enum> {

    /**
     * 获取当前枚举本身
     *
     * @return
     */
    C get();

    /**
     * 获取枚举key
     *
     * @return
     */
    Integer key();

    /**
     * 获取枚举value
     *
     * @return
     */
    String value();

}
