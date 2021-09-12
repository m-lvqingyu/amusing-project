package com.amusing.start.settlement.controller.inward;

import com.amusing.start.client.api.SettlementClient;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create By 2021/9/12
 *
 * @author lvqingyu
 */
@RestController
public class SettlementOutwardController implements SettlementClient {
    @Override
    public void settleAmount(String id) {
        System.out.println("开始结算");
        if (id.equals("0")) {
            throw new RuntimeException("降级测试");
        }

    }
}
