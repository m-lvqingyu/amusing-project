package com.amusing.start.user.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.service.SmsService;
import com.google.common.base.Throwables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Lv.QingYu
 * @since 2024/12/3
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SmsServiceImpl implements SmsService {

    private final RedissonClient redissonClient;

    private static final String SMS_SEND_LOCK = "sms:lock:";

    private static final String REGISTER_CODE_KEY_PREFIX = "sms:register:code:";

    private static final long REGISTER_CODE_KEY_TIME_TO_LIVE = 5L;

    private static final String REGISTER_CODE_MINUTE_LIMIT_PREFIX = "sms:register:minute:limit:";

    private static final String REGISTER_CODE_DAILY_LIMIT_PREFIX = "sms:register:daily:limit:";

    private static final int MINUTE_LIMIT = 1;

    private static final String MINUTE_LIMIT_VALUE = "1";

    private static final int DAILY_LIMIT = 5;

    @Override
    public void sendRegisterCode(String phone, String code) {
        String lockKey = SMS_SEND_LOCK + phone;
        RLock lock = redissonClient.getLock(lockKey);
        if (!lock.tryLock()) {
            throw new CustomException(CommunalCode.REQUEST_LIMIT_ERR);
        }
        try {
            // 每分钟是否超过限制
            RBucket<String> minuteBucket = redissonClient.getBucket(REGISTER_CODE_MINUTE_LIMIT_PREFIX + phone);
            if (StringUtils.isNotBlank(minuteBucket.get())) {
                throw new CustomException(CommunalCode.REQUEST_LIMIT_ERR);
            }
            // 每天是否超过限制
            RBucket<Integer> dailyBucket = redissonClient.getBucket(REGISTER_CODE_DAILY_LIMIT_PREFIX + phone);
            Integer dailyLimit = dailyBucket.get();
            System.out.println(dailyLimit);
            dailyLimit = dailyLimit == null ? 0 : dailyLimit;
            if (dailyLimit > DAILY_LIMIT) {
                throw new CustomException(CommunalCode.REQUEST_LIMIT_ERR);
            }
            // TODO SMS
            RBucket<String> codeBucket = redissonClient.getBucket(getRegisterCodeKey(phone));
            codeBucket.set(code, REGISTER_CODE_KEY_TIME_TO_LIVE, TimeUnit.MINUTES);
            dailyBucket.set(dailyLimit + 1);
            if (dailyLimit == 0) {
                DateTime dateTime = DateUtil.endOfDay(new Date());
                long time = dateTime.getTime() - System.currentTimeMillis();
                dailyBucket.expire(time, TimeUnit.MILLISECONDS);
            }
            minuteBucket.set(MINUTE_LIMIT_VALUE, MINUTE_LIMIT, TimeUnit.MINUTES);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("[SendRegisterCode]-err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommunalCode.SERVICE_ERR);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String getRegisterCode(String phone) {
        RBucket<String> bucket = redissonClient.getBucket(getRegisterCodeKey(phone));
        String code = bucket.get();
        bucket.delete();
        return code;
    }

    private String getRegisterCodeKey(String phone) {
        return REGISTER_CODE_KEY_PREFIX + phone;
    }

}
