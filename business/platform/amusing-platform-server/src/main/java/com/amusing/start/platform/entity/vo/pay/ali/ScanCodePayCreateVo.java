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
public class ScanCodePayCreateVo {

    private String orderNo;

    private String tradeNo;
}
