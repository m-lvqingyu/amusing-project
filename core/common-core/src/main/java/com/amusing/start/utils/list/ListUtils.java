package com.amusing.start.utils.list;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @date 2021/12/14 17:01
 */
public class ListUtils {

    public static <T> List<T> customFilter(List<T> list, Condition<T> t) {
        List<T> result = new ArrayList<>();
        list.stream().forEach(i -> {
            if (t.check(i)) {
                result.add(i);
            }
        });
        return result;
    }


    @FunctionalInterface
    interface Condition<T> {
        /**
         * 判断是否有效
         *
         * @param t
         * @return
         */
        boolean check(T t);
    }


    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        List<Integer> result = ListUtils.customFilter(list, (i) -> i >= 3);
        System.out.println(result);

    }

}
