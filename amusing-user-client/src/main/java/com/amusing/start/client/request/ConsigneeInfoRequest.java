package com.amusing.start.client.request;

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
public class ConsigneeInfoRequest {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 收获地址ID
     */
    private String addressId;

}
