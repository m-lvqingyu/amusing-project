package com.amusing.start.order.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Lv.QingYu
 * @since 2021/12/24
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdminShopCreateReq {

    @NotBlank(message = "商铺名称不能为空")
    @Length(min = 1, max = 31, message = "商铺名称不合规")
    private String name;

    @NotNull(message = "等级不能为空")
    private Integer grade;

}
