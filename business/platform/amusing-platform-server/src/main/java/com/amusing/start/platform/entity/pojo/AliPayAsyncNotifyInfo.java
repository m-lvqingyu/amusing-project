package com.amusing.start.platform.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description: 支付宝异步回调历史记录
 * @since 2023/10/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AliPayAsyncNotifyInfo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 通知ID
     */
    private String notifyId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 支付宝交易流水号
     */
    private String tradeNo;

    /**
     * 交易状态
     */
    private String tradeStatus;

    /**
     * 处理状态（0:待处理 5:已处理 10:忽略）
     */
    private Integer status;

    /**
     * 1:未删除 0:已删除
     */
    private Integer isDel;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

}
