package com.amusing.start.order.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lv.QingYu
 * @since 2025/3/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminOrderPageReq {

    private Integer pageNum;

    private Integer pageSize;

    private String orderNo;
    
}
