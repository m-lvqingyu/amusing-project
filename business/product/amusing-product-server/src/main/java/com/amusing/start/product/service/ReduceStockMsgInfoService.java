package com.amusing.start.product.service;

import com.amusing.start.product.pojo.ReduceStockMsgInfo;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2021/11/4 22:54
 */
public interface ReduceStockMsgInfoService {

    int updateMsgStatus(String msgId, Integer status);

    ReduceStockMsgInfo getMsgInfo(String msgId);

    int saveReduceStockMsgInfo(ReduceStockMsgInfo reduceStockMsgInfo);
}
