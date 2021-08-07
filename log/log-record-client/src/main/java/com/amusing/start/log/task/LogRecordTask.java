package com.amusing.start.log.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create By 2021/7/24
 *
 * @author lvqingyu
 */
public class LogRecordTask {

    @Async("logRecordTaskExecutor")
    public void logRecord(String address, String uri, Object[] objArgs, Object result) {
        try {
            Thread.sleep(10000);
            String param = findParams(objArgs);
            System.out.println("address:" + address + ", uri:" + uri + ",param: " + param + ",result:" + result);
        } catch (Exception e) {

        }
    }

    private String findParams(Object[] objArgs) {
        String param = "";
        if (objArgs == null || objArgs.length <= 0) {
            return param;
        }
        for (Object obj : objArgs) {
            if (obj instanceof MultipartFile ||
                    obj instanceof HttpServletRequest ||
                    obj instanceof HttpServletResponse) {
                continue;
            }
            param = param + obj.toString();
        }
        return param;
    }
}
