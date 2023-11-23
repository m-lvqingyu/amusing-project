package com.amusing.start.platform.service.pay.ali.impl;

import com.amusing.start.platform.service.pay.ali.ScanCodePayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/9
 */
@Slf4j
@Service
public class ScanCodePayServiceImpl implements ScanCodePayService {

    @Autowired
    private ScanCodePayServiceImpl() {

    }

    @Override
    public Boolean create(String orderNo, Integer amount, String subject, String buyerId, String timeoutExpress) {
        return null;
    }
}
