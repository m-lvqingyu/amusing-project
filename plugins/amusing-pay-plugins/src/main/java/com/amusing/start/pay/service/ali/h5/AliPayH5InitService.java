package com.amusing.start.pay.service.ali.h5;

import com.amusing.start.pay.request.AliPayH5InitRequest;
import com.amusing.start.pay.response.AliPayH5InitResponse;

/**
 * @author Lv.QingYu
 * @since 2024/8/7
 */
public interface AliPayH5InitService {

    /**
     * 统一下单
     *
     * @param request 下单信息
     * @return 下单结果
     */
    AliPayH5InitResponse init(AliPayH5InitRequest request);

}
