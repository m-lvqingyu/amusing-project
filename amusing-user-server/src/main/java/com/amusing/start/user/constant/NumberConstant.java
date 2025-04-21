package com.amusing.start.user.constant;

import cn.hutool.core.util.NumberUtil;

/**
 * @author Lv.QingYu
 * @since 2024/12/5
 */
public class NumberConstant {

    private final static int ZERO = 0;

    private final static int ONE = 1;

    private final static int TEN_THOUSAND = 10000;

    private final static int NINETY_NINE_THOUSAND_NINE_HUNDRED_AND_NINETY_NINE = 99999;

    public static int generateSixFiguresNum() {
        int[] numbers = NumberUtil.generateRandomNumber(TEN_THOUSAND, NINETY_NINE_THOUSAND_NINE_HUNDRED_AND_NINETY_NINE, ONE);
        return numbers[ZERO];
    }

}
