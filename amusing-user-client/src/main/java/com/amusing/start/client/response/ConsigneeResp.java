package com.amusing.start.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @since 2024/3/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ConsigneeResp {

    /**
     * 收件人姓名
     */
    private String name;

    /**
     * 收件人手机号
     */
    private String phone;

    /**
     * 收件地址-省编码
     */
    private String provinces;

    /**
     * 收件地址-市编码
     */
    private String cities;

    /**
     * 收件地址-区编码
     */
    private String districts;

    /**
     * 地址详情
     */
    private String address;

}
