package com.amusing.start.order.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Lv.QingYu
 * @since 2021/10/15
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ApiCreateOrderReq {

    @NotBlank(message = "请选择收件人")
    @Pattern(regexp = "\\d{14,31}$", message = "收件人信息ID格式不合法")
    private String id;

}
