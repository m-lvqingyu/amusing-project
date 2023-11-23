package com.amusing.start.platform.entity.vo.pay.ali;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ScanCodePayPreCreateVo {

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 当前预下单请求生成的二维码码串，有效时间2小时，可以用二维码生成工具根据该码串值生成对应的二维码
     */
    private String qrCode;

    /**
     * 当前预下单请求生成的吱口令码串，有效时间2小时，可以在支付宝app端访问对应内容
     */
    private String shareCode;

}
