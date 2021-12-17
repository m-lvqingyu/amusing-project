package com.amusing.start.map.utils;

import cn.hutool.crypto.SecureUtil;
import com.amusing.start.map.constant.MapConstant;
import com.amusing.start.utils.MapUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * @author lv.QingYu
 * @version 1.0
 * @date 2021/12/7 20:35
 */
public class GaoDeUtils {

    /**
     * 获取请求数字签名
     *
     * @param secretKey 密钥
     * @param param     参数
     * @return
     */
    public static String getSign(String secretKey, Map<String, Object> param) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> resultMap = MapUtils.sortByKey(param, false);
        Iterator<Map.Entry<String, Object>> iterator = resultMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            Object value = next.getValue();
            sb.append(MapConstant.AND_SIGN);
            sb.append(key);
            sb.append(MapConstant.EQUAL_SIGN);
            sb.append(value);
        }
        String requestParam = sb.append(secretKey).toString().replaceFirst(MapConstant.AND_SIGN, "");
        return SecureUtil.md5(requestParam);
    }

}
