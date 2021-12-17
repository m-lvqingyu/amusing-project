package com.amusing.start.order.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lv.qingyu
 * @version 1.0
 * @date 2021/12/17 17:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderShopDto {

    private String shopsId;

    List<OrderProductDto> productDtoList;

}
