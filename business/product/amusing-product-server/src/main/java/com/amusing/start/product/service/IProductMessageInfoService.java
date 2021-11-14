package com.amusing.start.product.service;

import com.amusing.start.product.pojo.ProductMessageInfo;

/**
 * Create By 2021/11/14
 *
 * @author lvqingyu
 */
public interface IProductMessageInfoService {

    /**
     * 保存扣减库存消息
     *
     * @param productMessageInfo
     * @return
     */
    int save(ProductMessageInfo productMessageInfo);

    /**
     * 更新消息状态
     *
     * @param txId         事务ID
     * @param status       状态
     * @param resultStatus 结果状态
     * @return
     */
    int updateStatus(String txId, Integer status, Integer resultStatus);
}
